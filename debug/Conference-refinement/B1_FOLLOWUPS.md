# B1 follow-ups — work that the in-place sever did not do

> Tracking file for items deferred out of the `claude/fix-tool-inheritance-3gRB0`
> branch. See `PROMPT_B1_SEVER_RESTEST_INHERITANCE.md` for the full plan.

## What landed on this branch (in-place sever, Phase B1.D-only)

- `MultiServiceTestCaseGenerator` no longer `extends AbstractTestCaseGenerator`.
- `MistRunner` uses the concrete `MultiServiceTestCaseGenerator` type.
- Inventory captured in `B1_INVENTORY.md`.

## What is still pending

1. **Phase B1.B — vendor data classes into `mist-core`.** `TestCase`,
   `TestParameter`, `Operation`, `OpenAPISpecification` (or a wrap surface),
   `MultiServiceTestCase`. Each entry in `B1_INVENTORY.md` § 4 marked
   *Defer*.
2. **Phase B1.C — move the MIST generator and its pipeline into `mist-core`.**
   `MultiServiceTestCaseGenerator` → `io.mist.core.generation.MistGenerator`;
   workflow/pipeline/registry packages move with it. List under
   `B1_INVENTORY.md` § 7.
3. **Phase B1.E — define the SPI surface (`MistSpecLoader`, `MistTestWriter`,
   `MistTestExecutor`)** in `mist-core/.../spi` and rewire `mist-core`
   consumers off the remaining `es.us.isa.*` imports.
4. **Phase B1.F — adapter becomes an SPI provider** via
   `META-INF/services/`.
5. **Phase B1.G — final cleanup**: positioning-doc citations, README
   architecture diagram, `flow.md` `Mst`→`Mist` brand sweep (system
   property keys MUST stay `mst.*`).
6. **Byte-identical demo proof** (`-Drandom.seed=42` against
   `trainticket-demo.properties`). Needs either the remote TrainTicket
   cluster reachable from the build host, or a recorded fixture.

## Bugs spotted while severing (out of scope for this branch)

None new. (`MultiServiceTestCaseGenerator`'s constructor still takes
`primarySpec` and `dummyPrimaryConf` parameters that became unused once
the `super(...)` call was removed; the callers in `MistRunner.java` and
`TestGenerationAndExecution.java` pass them anyway. Cleaning the
signature requires a follow-up touching all call sites and is held back
for Phase B1.C, which moves the class wholesale.)
