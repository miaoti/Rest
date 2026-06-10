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

## Fix #3 — tracker idempotency (offline)

Locked by `FaultDetectionTrackerIdempotencyTest` (6/6) and the corrected
`FaultDetectionTrackerSummaryTest`. The Sock Shop run reported "no oracle
anomalies across 216 executed test cases" (catalogue tolerates the fuzzed
inputs), so it exercised the recording path without a positive-fail double
record to dedup; the unit tests cover that branch directly.

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

## Environment notes

- Live TT IP/port `129.62.148.112:32677` confirmed reachable from this host
  (an earlier "timeout" reading was a stale probe; re-checked with `curl -v` and a
  raw TCP open — both succeed, login returns 200).
- The shared istio-system jaeger pod is in CrashLoopBackOff (8 days, pre-existing,
  badger storage). Not touched (it is shared infra). Sock Shop validation used
  `jaeger.enabled=false`; the query-emission fix does not depend on Jaeger.
- Local-only config copies (`sockshop-demo-local.properties`,
  `sockshop-mst-local.properties`) are gitignored and not committed.
