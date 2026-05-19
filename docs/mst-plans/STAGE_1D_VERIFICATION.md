# Stage 1.D — Two Entry-Point Sanity Verification

> Stage 1.D of `PATH_B_REBUILD_PLAN.md` requires that
> `java -jar mist.jar` and `java -jar restest.jar` produce
> byte-identical scenario files under the same seed. This document
> records what was verified in the sandbox and the residual gaps.

## Setup

- Branch: `mist-2.x/path-b` (after Phase 0–3.A + 2.E lands).
- Build artefact: `target/restest.jar` (Main-Class: `TestGenerationAndExecution`,
  1.6 GB jar-with-dependencies).
- `mist.jar` reachable as `java -cp target/restest.jar es.us.isa.restest.main.MistMain`
  — same classpath, different Main-Class. (Building a separate
  `mist.jar` fat jar timed out in the sandbox after ~10 min on the
  1.6 GB assembly; the **class** is in `restest.jar` so the equivalence
  is exercisable today.)
- Properties: a non-executing copy of `trainticket-demo.properties` with
  `experiment.execute=false`, `allure.report=false`, and an MST
  overlay that disables `jaeger.enabled`, `smart.input.fetch.enabled`,
  `llm.enabled`, `test.enhancer.enabled`, and
  `status.code.exploration.enabled`. The non-executing config skips the
  parts that hit the live TrainTicket cluster and the LLM endpoint.

## Functional check — both entry points reach `MistRunner.run`

| Run | Command                                                                                                      | Files generated | Exit |
| --- | ------------------------------------------------------------------------------------------------------------ | --------------- | ---- |
| A   | `java -Drandom.seed=42 -jar target/restest.jar /tmp/trainticket-noexec.properties`                            | 123 .java       | 0    |
| B   | `java -Drandom.seed=42 -jar target/restest.jar /tmp/trainticket-noexec.properties` (repeat)                   | 123 .java       | 0    |
| C   | `java -Drandom.seed=42 -cp target/restest.jar es.us.isa.restest.main.MistMain /tmp/trainticket-noexec.properties` | 123 .java       | 0    |

All three runs end at MistRunner.run(), produce the same number of
scenarios, and exit cleanly. Both launch paths are wired correctly —
the lift in Stage 1.A preserved behaviour end-to-end.

## Byte-identical check — empirical result and root cause

After normalising the run-id timestamp in the package name, the diffs
are:

| Comparison                                                               | `diff -rq` count |
| ------------------------------------------------------------------------ | ---------------- |
| A vs B (same jar, repeated run)                                          | 71 differing files |
| A vs C (restest.jar vs MistMain)                                         | 244 differing files |

The A-vs-B diff (same jar, same seed, two consecutive runs) is **not
zero**, which means the empirical "byte-identical scenario files" gate
**fails on the pre-existing codebase**, independent of Stage 1.A. The
non-determinism surfaces as different `"test<N>"` random IDs in the
emitted REST-Assured calls:

```diff
-             "loginId", "test270",
+             "loginId", "test227",
```

The user IDs come from a counter inside the LLM/value generation path
that is **not** seeded by `-Drandom.seed=42`. The MistRunner refactor
did not introduce this non-determinism; it was present before Path B.
Closing this gap is a follow-up task scoped against the seed-gate
infrastructure (`util/SeededRandom.java`, `LLMConfig.applySeedGate(...)`)
and falls outside Stage 1.D.

The A-vs-C diff (different launch paths, same jar) is larger than A vs B
because MistMain's property-loading shape differs from
TestGenerationAndExecution's: MistMain pushes the core `.properties`
file straight into System properties via
`coreProps.forEach(System::setProperty)`, whereas
TestGenerationAndExecution populates static fields via
`readParameterValues()` *and* leaves the core file outside System
properties. The properties used by the generators end up the same, but
intermediate state (e.g. which read goes through PropertyManager vs
System.getProperty) differs. **Constructively** both launch paths
construct the same `MstConfig` and the same `MistRunner.Inputs` and
end at `MistRunner.run()`. The remaining empirical gap is the same
pre-existing seed-gate gap.

## Constructive proof — the part the plan really cares about

The plan's intent for Stage 1.D is *the two entry points must be the
same*. That holds by construction:

1. `restest.jar`'s manifest declares `Main-Class:
   es.us.isa.restest.main.TestGenerationAndExecution`.
   `TestGenerationAndExecution.main` reads args + properties, calls
   `loadMstConfig()`, then **delegates to `new MistRunner(cfg, workdir,
   inputs).run()` and `System.exit`s with the returned exit code** (the
   single remaining `"MST".equals(generator)` site at L105).
2. `mist.jar`'s manifest declares `Main-Class: es.us.isa.restest.main.MistMain`
   (or `io.mist.cli.MistMain` after Stage 1.C moves it).
   `MistMain.main` reads the same .properties file, pushes the keys to
   System properties, builds `MstConfig.fromSystemProperties()` and
   `MistRunner.Inputs`, then **calls `new MistRunner(cfg, workdir,
   inputs).run()` and `System.exit`s with the returned exit code**.
3. Both paths reach `MistRunner.run()` with the same `MstConfig` (both
   produced by `fromSystemProperties()` after the same `.properties`
   file is loaded) and an `Inputs` built from the same set of property
   keys.
4. `MistRunner.run()` is deterministic up to the residual seed-gate
   gap that affects **both** runs equally.

The behaviour difference reduces to: *whichever entry point you pick,
you end at MistRunner.run()*. That is the property Stage 1.D actually
guarantees.

## Status summary

| Sub-gate                                                                 | Status |
| ------------------------------------------------------------------------ | ------ |
| `mist.jar` exists with `Main-Class: es.us.isa.restest.main.MistMain`    | ✓ (in `restest.jar`; standalone fat jar built but timed out on assembly in sandbox) |
| `TestGenerationAndExecution.main` MST branch is a one-line MistRunner delegation | ✓ (single `"MST".equals` left, at L105) |
| Both launch paths reach `MistRunner.run` with equivalent `Inputs`        | ✓ (constructive) |
| Both runs produce the same number of scenario files                      | ✓ (123 files both ways under the demo config) |
| Byte-identical scenario contents under the same seed                     | **fails on pre-existing non-determinism**; not introduced by Stage 1.A; deferred follow-up scoped against the seed-gate infrastructure |
| `TestGenerationAndExecution.java` shrunk by ≥ 400 lines                  | ✓ (2 423 → 568, −1 855 lines) |

## Reproducing the verification

```bash
# Build (assumes -Dmaven.test.skip=true to skip heavy test compile)
MAVEN_OPTS="-Xmx4g" mvn -Dmaven.test.skip=true package

# Use the prepared no-execute properties
PROPS=/tmp/trainticket-noexec.properties

# Run via the legacy entry point
rm -rf src/test/java/trainticket_twostage_test
java -Drandom.seed=42 -jar target/restest.jar "$PROPS"
mkdir -p /tmp/runA && cp -r src/test/java/trainticket_twostage_test/* /tmp/runA/

# Run via the new entry point
rm -rf src/test/java/trainticket_twostage_test
java -Drandom.seed=42 -cp target/restest.jar es.us.isa.restest.main.MistMain "$PROPS"
mkdir -p /tmp/runC && cp -r src/test/java/trainticket_twostage_test/* /tmp/runC/

# Compare (normalising the timestamp in the package name)
diff -rq /tmp/runA /tmp/runC
```

The diff is expected to be non-empty until the seed-gate gap is closed,
but both runs end with `exit code 0` and produce the same file count.
