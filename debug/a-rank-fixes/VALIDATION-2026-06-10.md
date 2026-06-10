# Live + offline validation of the four deferred A-rank fixes (2026-06-10)

Follow-up to commits `de63674a` (the four fixes) and `71ceb040` (registry lint).
All four fixes are now validated against live SUTs where applicable; the
remainder is locked by tests. mist-core suite: **328 green** (314 baseline + 14
new across the two commits + this run's additions).

## Fix #1 — query parameters emitted (Sock Shop, LIVE)

Run: `java -jar mist.jar sockshop-demo-local.properties` against the live
kind-mist Sock Shop (front-end port-forward on :8080, `jaeger.enabled=false`
override — the cluster's shared jaeger pod is in a pre-existing CrashLoopBackOff,
unrelated to MIST and not needed for this fix). Exit 0, 202 generated / 216
executed.

- **Generated source**: `Flow_Scenario_65.java` (root `GET /catalogue`) emits
  `req.queryParam("page"/"size"/"tags"/"sort", …)`. Pre-fix these were dropped.
- **Form-encoding works**: a value `[a, b]` is emitted as `%5Ba%2C+b%5D`; a sniper
  OVERFLOW value lands as a long `XXXX…` string in `sort`. No request-line
  corruption.
- **Live front-end access log** (`kubectl logs deploy/front-end`): **301 of 301**
  catalogue hits carried a query string (`GET /catalogue?page=…&size=…&tags=…&sort=…`),
  **0** bare `GET /catalogue`. Pre-fix this ratio was 0 / N.
- **Sniper fault reaches query position**: injected values are visible in the live
  log, e.g. `sort=|+cat+/etc/passwd`, `sort=<img src=x onerror=alert('XSS')>`,
  `size=true`, `page=a` — all returned by the SUT (Sock Shop's catalogue is
  permissive; the point is the value *reached* the parameter, which it never did
  before).

## Fix #2 — Phase 3.5 dedup live (Sock Shop, LIVE)

Same run. Sock Shop's spec exposes catalogue at four root shapes
(`/catalogue`, `/catalogue/size`, `/catalogue/{id}`, `/tags`). The generator
emitted **exactly four** `Flow_Scenario_*.java` classes — one per distinct root
API, no duplicate single-root classes. With the pre-fix flag leak a fresh run
re-emitted duplicates for the same key. Logic is additionally locked by
`DedupFlagLeakRegressionTest` (6/6).

## Fix #3 — tracker idempotency (offline only — honest scope)

Locked by `FaultDetectionTrackerIdempotencyTest` (6/6) and the corrected
`FaultDetectionTrackerSummaryTest`.

**The Sock Shop run did NOT exercise this fix.** It ran with `jaeger.enabled=false`
(see the environment note below), and the generated oracle hook is
`if (!JAEGER_ENABLED) return;` at the top of `attachJaegerTrace`
(`MultiServiceRESTAssuredWriter.java:302`). So no Trace Shape Oracle verdict was
produced, `recordVerdict` was never called, and the "no oracle anomalies across
216 executed tests" line means "the oracle did not run", NOT "the oracle ran and
found nothing." The double-count branch this fix targets (a positive variant
failing on an ERROR verdict, recorded once on the success path and again in the
catch path) only occurs with the oracle live, so it is covered by the unit tests,
not by this run.

The oracle itself is unregressed by the writer/tracker changes: the offline
reproduction path of record (`OracleCheck` on committed traces, no Jaeger) still
fires — Bookinfo `HIDDEN_DOWNSTREAM_FAILURE` ERROR, Online Boutique gRPC
`HIDDEN_DOWNSTREAM_FAILURE` WARN, response-level oracle PASS/misses on both. A
live end-to-end re-confirmation of the double-count fix needs a healthy Jaeger
(see below) and is the one remaining gap; it does not affect any paper claim.

## Fix #4 — registry de-poison (TrainTicket, LIVE + offline)

Live TrainTicket reachable at `http://129.62.148.112:32677` (admin login 200,
`/api/v1/stationservice/stations` returns the station name set).

- **`TTEndStationLiveCheck`** (gated by `-Dtt.live.base.url` + `DEEPSEEK_API_KEY`):
  constructs MIST's own `SmartInputFetcher` with the shipped (de-poisoned)
  registry and calls `fetchSmartInput("endStation")` for consumer
  `POST /api/v1/adminrouteservice/adminroute`. The returned value is a member of
  the live `/stations` name set — i.e. grounded from the station producer, not
  the (previously poison-ranked) `trains` producer. **1/1 pass.**
- **`ShippedRegistryDepoisonTest`** (offline data lint): both shipped registries
  carry all-zero successRate, and the real endStation mappings rank stations over
  trains via `rankingScore`. **2/2 pass.**
- **`ProducerRankingTest`**: cold-start name-affinity logic. **4/4 pass.**

## Jaeger health and reviewer reproducibility

How MIST consumes traces (two independent paths):
- **Generation input** — `TraceWorkflowExtractor` reads the committed
  `evaluation/suts/*/traces/*.json` corpus (bookinfo 7, boutique 2, sockshop 4,
  trainticket 2). No live Jaeger. This is what builds scenarios.
- **Runtime oracle** — the generated test code queries `JAEGER_BASE_URL/traces/...`
  at execution time to run the Trace Shape Oracle. This needs a live Jaeger, and
  is gated by `jaeger.enabled` (default false).

Current state of the shared istio-system Jaeger: **CrashLoopBackOff**, restart
count 119 over 8 days. Root cause (diagnosed, not a MIST defect):
- It is the stock Istio demo addon (`samples/addons/jaeger.yaml`): all-in-one
  Jaeger v2.14.0, badger storage on an **emptyDir**, a liveness probe on
  `/status:13133` (failureThreshold 3 × periodSeconds 10, timeout 1s), and **no
  startupProbe**.
- emptyDir persists across container restarts within the same pod, so badger
  accumulated **1745 SST tables** over 8 days. Reopening them on boot now takes
  longer than the ~30s liveness window, so the query service is killed before it
  reports healthy → restart loop. No panic, exit 2 = liveness kill.
- Deleting/recreating the pod wipes the emptyDir → empty badger → sub-second
  startup → healthy. (Not done here: the pod is shared istio-system infra, and a
  delete was correctly blocked.)

Reviewer impact: **none on a fresh deploy.** `deploy/deploy.sh` installs a clean
Jaeger with empty badger; a reviewer's session (minutes–hours) never approaches
the 8-day accumulation that broke this long-lived instance. The committed
Bookinfo/Boutique in-process evidence was captured 2026-06-02 when this same
Jaeger was fresh and healthy, before the degradation. And the headline result
reproduces with **no Jaeger at all** via the offline `OracleCheck` path of record
(re-run today, still fires — see Fix #3 above).

Other notes:
- Live TT IP/port `129.62.148.112:32677` confirmed reachable from this host
  (an earlier "timeout" reading was a stale probe; re-checked with `curl -v` and a
  raw TCP open — both succeed, login returns 200).
- Local-only config copies (`sockshop-demo-local.properties`,
  `sockshop-mst-local.properties`) are gitignored and not committed.

## Remaining gap

A live end-to-end re-confirmation of Fix #3 (tracker double-count) needs a healthy
Jaeger. Two ways to get one for a follow-up: restart the istio-system jaeger pod
(wipes emptyDir), or run any SUT with `jaeger.enabled=true` against a fresh deploy.
This gap is oracle-reporting only and touches no paper claim.
