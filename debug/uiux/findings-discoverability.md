# Are MIST's findings discoverable enough? — a UI/UX assessment

Date 2026-06-01. Question: when a user runs MIST, can they actually **notice** that it found a
hidden-downstream failure / soft error — the headline contribution? Assessed from a UI/UX
standpoint, against how mature finding tools surface results. Verdict + evidence + ranked fixes.

## Verdict (short)
**Partially. The strong-signal path is fine; the most novel path is under-surfaced.**
- **ERROR-severity hidden-downstream** (a swallowed HTTP **5xx**, e.g. Bookinfo) → the positive
  test throws `AssertionError` → **the test goes RED**. Strong, impossible to miss. ✅ Enough.
- **WARN-severity hidden-downstream** (a swallowed **gRPC/otel** error, no HTTP 5xx, e.g. Online
  Boutique — *the gRPC generalization that is the paper's second-SUT claim*) → the test stays
  **GREEN**; the finding is only an Allure `addAttachment` + a **custom label that the Allure UI
  cannot even filter on** + a line in a side `.txt` report. ❌ **Not enough** — this is exactly the
  pattern practitioners call "you might as well not use the warning severity, it gets ignored."
- **Cross-cutting (any severity):** there is **no end-of-run console summary** of anomalies, the
  **process exit code is not tied to findings**, and there is **no CI/SARIF surface**. A developer
  reading the terminal or a CI gate sees nothing about hidden failures. ❌

For a tool whose entire value is *catching failures nobody else sees*, burying the WARN finding in
a side file reproduces the very pathology it exists to catch: **a finding the user never sees ≈ not
found.** The fix is **visibility, not more red** — the research supports keeping WARN non-failing
*by default* while making its count and location impossible to miss + giving one opt-in switch to
escalate.

## What MIST surfaces today (code-grounded)
Per-step in the generated test (`MultiServiceRESTAssuredWriter.java`):
- Positive variant: `if (!verdict.isPassed()) throw new AssertionError(...)` (`:2336-2350`) — but
  `TraceShapeVerdict.isPassed()` returns false **only for ERROR-severity** failures
  (`TraceShapeVerdict.java:65-67`); WARN/INFO never flip it. `HiddenDownstreamFailure` is **ERROR**
  for a swallowed `http>=500`, **WARN** for an `otel=ERROR`-only span
  (`HiddenDownstreamFailureInvariant.java:107-109`). So ERROR→red, WARN→green.
- On any violation: `Allure.addAttachment("🕳️ HIDDEN DOWNSTREAM FAILURE …")` + `Allure.label(
  "mist.anomaly", "HIDDEN_DOWNSTREAM_FAILURE")` + `Allure.parameter(...)` (`:718-746`).
- `FaultDetectionTracker.recordVerdict()` (`:755-757`) → an "ORACLE ANOMALIES" section in
  `logs/fault-detection-reports/*.txt` (severity preserved).
Gaps found in the codebase:
- **No aggregate console summary** — only scattered per-test prints (`🔍 FAULT DETECTED`, `✅ No
  parameter-related errors`). No "MIST found N hidden-downstream failures" headline.
- **Exit code not tied to findings** — `MistMain` exits 2 on error, else `result.exitCode()`; the
  run exits 0 whether or not anomalies were found.
- **Allure overview plumbing exists but unused for findings** — `MistRunner.java:2665-2666` already
  *copies* `categories.json`/`executor.json` into `allure-results` **if present**, but MIST never
  generates them with anomaly content.

## Evidence (cited)

### A. Allure cannot surface a finding on a green test by default
- Statuses are passed/failed/broken/skipped/unknown — **no "passed-with-warning"**; `statusDetails`
  (the message pane) is **empty on passing tests**. JUnit4: `AssertionError→FAILED`, other
  `Throwable→BROKEN`, `AssumptionViolatedException→SKIPPED`.
  [test-statuses](https://allurereport.org/docs/test-statuses/) ·
  [ResultsUtils.java](https://raw.githubusercontent.com/allure-framework/allure-java/main/allure-java-commons/src/main/java/io/qameta/allure/util/ResultsUtils.java)
- The UI filters on **title, `tag:`, status, marks** — **not arbitrary custom labels**. So
  `Allure.label("mist.anomaly", …)` is **not filterable or discoverable** in the report; only a
  `tag` is. [sorting-and-filtering](https://allurereport.org/docs/sorting-and-filtering/)
- `categories.json` accepts `matchedStatuses:["passed"]`, but a regex needs a message and **passed
  tests have no message** unless the producer writes `statusDetails` itself.
  [categories](https://allurereport.org/docs/how-it-works-categories-file/) ·
  [allure-python#790](https://github.com/allure-framework/allure-python/issues/790)
- Cheap run-level headline: `executor.json` `reportName` shows "on top of the Overview tab".
  [executor file](https://allurereport.org/docs/how-it-works-executor-file/)

### B. Mature tools split GATING from VISIBILITY — and keep warnings VISIBLE
- **GitHub code scanning** (closest analog): `error/critical/high` **fail** the PR check;
  `warning/note` **pass** the check **but are still shown inline as PR annotations + in the Security
  tab** — *non-blocking ≠ invisible.*
  [triaging-in-PRs](https://docs.github.com/en/code-security/code-scanning/managing-code-scanning-alerts/triaging-code-scanning-alerts-in-pull-requests)
- **SonarQube**: severity is a label; the **Quality Gate** decides pass/fail; warnings still appear
  in counts. [quality-gates](https://docs.sonarsource.com/sonarqube-server/quality-standards-administration/managing-quality-gates/introduction-to-quality-gates)
- **ESLint**: warnings exit 0 by default → community verdict "warnings get ignored… you might as
  well not use the warning severity," fixed only by `--max-warnings`.
  [eslint CLI](https://eslint.org/docs/latest/use/command-line-interface) ·
  [warnings get ignored](https://medium.com/@tangiblej/you-might-as-well-not-use-eslints-warning-severity-warnings-will-get-ignored-38d52848238e)
- **Semgrep**: severity decoupled from blocking; non-blocking *Comment* mode still posts the finding.
  [blocking](https://semgrep.dev/docs/semgrep-ci/configuring-blocking-and-errors-in-ci)
- **API testers** (Schemathesis/Newman/RESTler): a discovered bug **is** the headline (summary
  count + non-zero exit). There is essentially no "green + bug in a side file" pattern.
  [schemathesis CLI](https://schemathesis.readthedocs.io/en/stable/reference/cli/)

### C. HCI: a buried finding is an unseen finding
- Progressive disclosure is fine **only if you disclose what users frequently need up front**;
  hiding the *finding itself* in a secondary layer violates it.
  [NN/g progressive disclosure](https://www.nngroup.com/articles/progressive-disclosure/)
- Alert fatigue: don't make *everything* red — supports keeping uncertain WARN findings
  non-blocking. [NN/g alert fatigue](https://www.nngroup.com/videos/alert-fatigue-user-interfaces/)
- Developers look at red/green → exit code → run summary → (rarely) logs/side files. GitHub built
  Job Summaries because logs/annotations were "where information goes to not be found."
  [job summaries](https://github.blog/news-insights/product-news/supercharging-github-actions-with-job-summaries/)
- Static-analysis precision threshold: FP > ~20–30% → tool abandonment ⇒ only make a finding
  prominent to the degree you trust it. [Nguyen et al.](https://sanadlab.org/assets/pdf/NguyenTSE2022.pdf)

### D. CLI + demo conventions
- Good end-of-run summary = headline count + breakdown by category/severity + pointer to detail,
  brief, last, colored (honor `NO_COLOR`). [clig.dev](https://clig.dev/) ·
  [pytest](https://docs.pytest.org/en/7.1.x/how-to/output.html)
- Exit codes (semgrep model): **0** clean · **1** found (ERROR) · **2** *tool crashed* — keep the
  crash code distinct from the found-a-bug code. [semgrep CLI](https://semgrep.dev/docs/cli-reference)
- **SARIF 2.1.0** is the de-facto findings format → GitHub Security tab + PR annotations.
  [OASIS SARIF](https://docs.oasis-open.org/sarif/sarif/v2.1.0/sarif-v2.1.0.html) ·
  [GitHub SARIF](https://docs.github.com/en/code-security/code-scanning/integrating-with-code-scanning/sarif-support-for-code-scanning)
- Tool-demo money shot: problem→run→**before/after side-by-side (baseline PASS vs MIST flags)**,
  zoom+callout on the masked bytes, one finding, end on it. Video quality is a scored criterion.
  [ICSE'27 demos](https://conf.researchr.org/track/icse-2027/icse-2027-demonstrations) ·
  [money shot](https://advids.co/blog/software-demo-presentation)

## Recommendations (ranked by impact ÷ effort)

**P0 — cheap, high-impact, needed for the demo to land**
1. **End-of-run console summary** (stdout, last, colored): headline anomaly count grouped by
   category × severity + where the detail is. MIST has none today; this is the single biggest gap.
   Sketch:
   ```
   MIST run complete — 142 requests across 37 endpoints (5m12s)
     🕳️ Hidden downstream failures   ERROR 2   WARN 5
     🟡 Soft errors (2xx body=error) ERROR 3
     ▸ detail: logs/fault-detection-reports/<ts>.txt   ▸ Allure: target/site/.../index.html
   Exit 1 (ERROR findings). Use --fail-on=warn to also gate on WARN.
   ```
2. **Exit-code policy**: 0 clean · 1 ERROR findings present · 2 MIST itself failed (keep distinct).
   WARN → 0 by default + `--fail-on=warn` / `--max-warnings N` opt-in. Plumbing exists
   (`MistRunResult.exitCode()`); tie it to `FaultDetectionTracker` counts.
3. **`executor.json reportName` + `environment.properties`** with the anomaly count → Allure
   Overview headline. **The copy plumbing already exists** (`MistRunner:2665`); just generate them.

**P1 — make the Allure report actually show it**
4. **Emit the anomaly as a `tag`** (e.g. `MIST_HIDDEN_DOWNSTREAM`) in addition to the (unfilterable)
   `mist.anomaly` label, so users can filter `tag:MIST_HIDDEN_DOWNSTREAM`.
5. **"Hidden Downstream Failure (MIST)" Allure category** backed by MIST writing
   `statusDetails.message` on the green test → a named, counted, clickable bucket on the Categories
   tab + Overview widget. Verify it lands in `*-result.json` (off the happy path; pin the version).

**P2 — CI-grade**
6. **`--sarif` output** → GitHub Security tab + PR annotations; the standard way a finding tool's
   results flow into CI UIs.

**Demo / screencast**
7. **Money shot**: same 200 response, baseline/response-level oracle **PASS (green)** beside MIST
   **flagging** it, zoom+callout on the swallowed span. For **Bookinfo (ERROR)** show the **red
   test**; for **Online Boutique (WARN)** you **cannot** show a red test — show the **console
   summary count + the Allure 🕳️ attachment/category** instead. P0#1 is the cleanest money-shot
   surface and removes the need to hand-wave the WARN case.

## Status (2026-06-01)
- **P0#1 end-of-run console summary — implemented, a BLOCKER found in review and FIXED.**
  `FaultDetectionTracker.summarizeAnomalies()` + `AnomalySummary.render(reportDir, ascii)` (mist-core)
  print a prominent stdout block grouped by kind × severity, **plus the top findings inline
  (offending endpoint + the swallowed span)**, a distinct-vs-occurrences count line, a one-line
  gating explainer (ERROR fails the test; WARN/INFO don't), and pointers to the `.txt` + Allure.
  - **Blocker (caught by review, fixed):** the first cut printed via `System.out.println`, but
    `MistRunner.run()` replaces `System.out` with a log4j `LoggerStream` at INFO and the console
    appender is gated at WARN+ — so the summary was logged to the file but **never reached the
    terminal** (my "smoke test" was a false positive: it called `render()` before the interception).
    Fix: print via `ConsoleProgressBar.printRaw()` (raw `FileDescriptor.out`, UTF-8), the same
    channel the startup banner uses to bypass the filter. ASCII fallback when stdout isn't UTF-8 or
    `NO_COLOR` is set. Verified: `FaultDetectionTrackerSummaryTest` (4 tests) + mvn compile.
- **P0#2 exit code — deliberately held** (reviewers agree it's the right default but flag the risk).
  Tying exit≠0 to ERROR findings is the CI-standard model (semgrep/Schemathesis/ESLint) but changes
  the process exit code and could break callers expecting 0 (the TrainTicket scripts). One-line
  follow-up, gate it: `.exitCode(summary.errorCount > 0 ? 1 : 0)` behind `--fail-on=error|warn|never`
  (default `error`).
- P0#3 / P1 / P2 — not yet done.

## Independent review & disposition (2026-06-01)
Three adversarial reviewers (user-perspective, competitor-perspective, code-bug) — none had reviewed
this before; the user asked. Per-finding disposition (grep-verified):
- **[BLOCKER] Summary swallowed by the log4j WARN+ console filter — ACCEPT, FIXED.** Confirmed:
  `MistRunner:237` setupConsoleInterception → `System.setOut(LoggerStream(INFO, mirrorToStream=false))`
  (`LoggerStream:66-69`), console appender at WARN+. Fixed via `ConsoleProgressBar.printRaw` (raw FD).
- **[MAJOR] Emoji mojibake under non-UTF-8 charset — ACCEPT, FIXED.** UTF-8 raw stream + ASCII fallback.
- **[MAJOR] "Noticeable but not actionable" (no endpoint shown) — ACCEPT, FIXED.** `render` now inlines
  the top findings' endpoint + swallowed-span detail (data already on `OracleAnomaly`).
- **[MAJOR] WARN-keeps-test-green not communicated — ACCEPT, FIXED.** Added the gating explainer line.
- **[MINOR] "N test cases executed" / "distinct" wording — ACCEPT, FIXED.** Reworded to
  "M anomalies across N executed test cases" + "K unique findings, H occurrences".
- **[code A/B] run-path coverage + same-JVM tracker — VERIFIED PASS** (no bug): the print is on the
  single common return of `run()`; generated tests run in-process via `JUnitCore` sharing the
  singleton (not forked), so the tracker is populated before the print.
- **[code D/E/F] encapsulation / thread-safety / counts — PASS:** `byKind`/`findings` are fresh
  snapshots; `summarizeAnomalies()` is `synchronized`; counts are distinct, TARGET_ATTRIBUTION excluded.
- **[BLOCKER/competitor] no exit-code gating on findings (CI no-op) — ACCEPT, DEFER** = P0#2 above.
- **[BLOCKER/competitor] confidence welded to surfacing (WARN = invisible) — ACCEPT, PARTIAL:** the
  console summary now surfaces WARN regardless of test colour; full decoupling needs SARIF + exit code.
- **[BLOCKER/competitor] no SARIF/JSON machine output — ACCEPT, DEFER (P2).** The fingerprint already
  computed (`fingerprintViolation`) makes this ~80% done; emit SARIF 2.1.0 → GitHub Security tab + PR.
- **[MAJOR/competitor] no PR/CI-native surfacing — ACCEPT, DEFER** (follows SARIF).
- **[MAJOR/competitor] no cross-run baseline/suppression → alert fatigue — ACCEPT, DEFER.** Persist
  the existing fingerprint set; classify new vs known; gate on new.
- **[MODERATE/competitor] no severity-gating config — ACCEPT, DEFER** (pairs with P0#2).
- **[competitor] FP/precision number on a real SUT with tolerated downstream errors — ACCEPT, OPEN:**
  a study item for the paper before gating CI on the detector.
- **[nits] %-format overflow, double blank line, unconditional Allure line — partial/won't-fix (cosmetic).**

## Bottom line
Keeping WARN non-failing by default is well-supported (alert fatigue, symptom-vs-cause, uncertain
precision). But "green test + an unfilterable label + a side `.txt`" is the weakest placement in
every framework surveyed. The contribution needs a **run-level count a user sees without hunting**
(console summary + exit code + Allure overview), one **filterable handle** (tag/category), and one
**opt-in escalation** (`--fail-on=warn`). P0 items 1–3 are low-effort (plumbing partly exists) and
are what make the demo's hidden-downstream claim *visible* rather than asserted.
