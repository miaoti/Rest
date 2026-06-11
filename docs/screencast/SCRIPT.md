# MIST Screencast — Production Script (ISSTA 2026 Tool Demo)

**Target length:** 4:15–4:30 (venue allows 3–5 min; leave headroom).
**Resolution:** 1920×1080, 30 fps. **Audio:** voice-over, no music.
**Tone:** calm, factual, paper-aligned. Every claim spoken here is a claim the
paper makes and the artifact reproduces — do not improvise stronger wording.

Every command and every expected output line below was **executed and verified
on 2026-06-09/10** (see `debug/reproduce/README.md`, V1–V12). Where an output
snippet is shown, that is the literal text the run produces.

---

## 0. What the viewer must walk away with

1. The **problem is real and invisible to status-level testing**: a clean
   HTTP 200 can hide a failed downstream call (hidden downstream failure) or a
   failure-valued body (soft error).
2. **MIST exists and runs**: one jar, one properties file, real SUTs.
3. The **trace-shape oracle catches what response-level oracles miss** — shown
   *live* on Istio Bookinfo, with the swallowed 503 caught from the trace.
4. It **scales and reproduces**: 15,036 generated tests / 10 of 10 injected
   faults on TrainTicket (registry-confirmed); byte-identical generation under
   a seed; one offline command replays the headline detections from committed
   traces — no SUT, no LLM.

---

## 1. Pre-recording checklist (do ALL of this before pressing record)

Environment (lessons from the 2026-06-09 audit — each item below bit us once):

- [ ] **Machine**: the kind cluster host (≥8 cores free; do not run other
      heavy jobs while recording — the cluster apiserver gets flaky under load).
- [ ] **Cluster up**: `evaluation/suts/bookinfo/deploy/deploy.sh` already run;
      `kubectl get pods` all `2/2 Running` (bookinfo in `default`).
- [ ] **Ports 8080/16686 are free** — kill stray `kubectl port-forward`s from
      other terminals/sessions first: `pkill -f "port-forward"` (check
      `ss -tlnp | grep -E ':8080|:16686'` shows nothing). A stray forward to a
      different service answers your curls with confusing 404s.
- [ ] **Port-forwards in restart loops** (a bare port-forward dies mid-recording):
      ```bash
      ( while true; do kubectl port-forward -n istio-system svc/istio-ingressgateway 8080:80; sleep 2; done ) &
      ( while true; do kubectl port-forward -n istio-system svc/tracing 16686:80; sleep 2; done ) &
      ```
- [ ] **Smoke**: `curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8080/productpage` → `200`.
- [ ] **Ratings healthy** at start: `kubectl scale deploy ratings-v1 --replicas=1` and wait Ready.
- [ ] **Repo**: fresh-ish clone at the submission commit, `mist.jar` already
      built (`export JAVA_HOME=/path/to/jdk21 && mvn -q -DskipTests install`).
      Build is NOT shown in the video (dead air); jar must exist beforehand.
- [ ] **JDK 21** on `JAVA_HOME` (a JRE breaks the source-launched harnesses).
- [ ] **DeepSeek key** at `.api_keys/DEEPSEEK_API_KEY` (Scene 5 makes one real call).
- [ ] **Stale generated tests removed** so Scene 4's file count is clean:
      `rm -rf mist-cli/src/test/java/trainticket_twostage_test`.
- [ ] **Browser tabs** (pre-loaded, zoomed to ~125%):
      1. `https://github.com/miaoti/Rest` (must land on `inject-detection` — flip
         the default branch first!)
      2. Jaeger UI `http://localhost:16686/jaeger/search` with Service =
         `productpage.default` selected (verify this view in the dry run).
- [ ] **Terminal**: dark theme, ≥18 pt font, window ~120×30, minimal prompt
      (`export PS1='$ '`), `clear` before each scene.
- [ ] **Dry run the whole script once** and time it. Scene 3's trace-ingest
      sleep and Scene 4's generation are the variable parts.
- [ ] **After recording**: restore `ratings-v1 --replicas=1`.

---

## 2. Scene-by-scene

Notation: **SAY** = narration (read verbatim, natural pace ≈ 140 wpm).
**DO** = exactly what is on screen. **EXPECT** = the verified output to point
at (move the mouse cursor under the line as you say it). **CUT** = editing note.

---

### Scene 0 — Title card (0:00 – 0:20)

**DO:** Full-screen slide, 3 lines:

> **MIST — Microservice Integration & Scenario Tester**
> Trace-driven test generation + a trace-shape oracle for microservice REST APIs
> ISSTA 2026 Tool Demonstrations · github.com/miaoti/Rest

**SAY:**
> "This is MIST, a test generator for microservice REST APIs. MIST turns
> OpenTelemetry traces and an OpenAPI spec into runnable cross-service tests,
> and checks them with a trace-shape oracle. In this demo I'll show the one
> failure class that motivates it: a service that returns HTTP 200 while one
> of its downstream dependencies has actually failed."

**CUT:** hard cut to Scene 1 terminal.

---

### Scene 1 — The tool in one breath (0:20 – 0:50)

**DO:** Terminal in the repo root. Type (or pre-type and just press Enter):

```bash
ls mist-core mist-llm mist-cli
ls mist-cli/target/mist.jar
head -30 REPRODUCE.md
```

**SAY:**
> "MIST is a three-module Maven project: mist-core holds the five pipeline
> stages and the oracle; mist-llm is LLM dispatch with a call cache; mist-cli
> ships the single entry point — one jar, driven by one properties file per
> system under test. The repository bundles four ready-to-run systems:
> TrainTicket, Istio Bookinfo, Sock Shop, and Online Boutique, plus a
> reproduction guide. Everything you'll see is in REPRODUCE-dot-md."

**CUT:** none; flows into Scene 2.

---

### Scene 2 — LIVE: the hidden downstream failure (0:50 – 2:20) ★ core scene

Bookinfo is live on kind. Split narration into three beats.

**Beat A — break the dependency (0:50 – 1:10)**

**DO:**
```bash
evaluation/suts/bookinfo/workload/inject-ratings-outage.sh on
curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8080/api/v1/products/0/ratings
```

**EXPECT:** the inject script prints `ratings OUTAGE = ON (hidden-downstream active)`;
the direct ratings call settles to `503` (re-run the curl once if the first
shows 200 — pod teardown takes a few seconds).

**SAY:**
> "Bookinfo on a local kind cluster, with Istio tracing into Jaeger. I'm
> taking the ratings service down — a real availability outage, not a code
> change. Called directly, ratings now fails loudly: 503."

**Beat B — the lie (1:10 – 1:40)**

**DO:**
```bash
curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8080/api/v1/products/0/reviews
curl -s http://localhost:8080/api/v1/products/0/reviews | python3 -m json.tool | head -14
```

**EXPECT (verified):** first command prints `200`; the body shows
`"rating": {"error": "Ratings service is currently unavailable"}` buried in an
otherwise valid reviews payload.

**SAY:**
> "But the reviews endpoint — which depends on ratings — still answers
> HTTP 200. The reviews service caught the failure and degraded gracefully.
> A status oracle passes. A schema oracle passes: this is a perfectly valid
> response body. Every black-box REST tester that judges the HTTP response
> would call this test green. The failure has been swallowed."

**Beat C — the trace knows; MIST's oracle fires (1:40 – 2:20)**

**DO:**
```bash
sleep 6   # let Jaeger ingest the spans we just produced
curl -s "http://localhost:16686/jaeger/api/traces?service=productpage.default&limit=20&lookback=10m" -o /tmp/live-traces.json
"$JAVA_HOME/bin/java" -cp mist-cli/target/mist.jar evaluation/suts/bookinfo/OracleCheck.java \
    /tmp/live-traces.json "GET /api/v1/products/0/reviews"
```

**EXPECT (verified, point at each line as you speak):**
```
  --> RESPONSE-LEVEL oracle (status/schema/soft-error sees the client response): PASS  (root is 2xx — looks successful, MISSES the failure)
  --> TRACE oracle HIDDEN_DOWNSTREAM_FAILURE: FIRES  severity=ERROR
      reviews.default ──▶ ratings.default.svc.cluster.local:9080/* (http=503 otel=ERROR)
```

**SAY:**
> "I pull the distributed traces of the requests we just made, straight from
> Jaeger, and replay MIST's shipped oracle on them. The response-level verdict:
> PASS — it only sees the 200. MIST's HiddenDownstreamFailure invariant: FIRES,
> severity ERROR — it sees, inside the trace, the reviews-to-ratings call that
> returned 503. This invariant is structural: label-free, and it needs no LLM.
> In MIST's full pipeline this verdict fails the generated test red; the
> committed run report shows the same finding across 166 generated tests
> executed in one in-process run."

**DO (close the beat):**
```bash
evaluation/suts/bookinfo/workload/inject-ratings-outage.sh off
```

**CUT:** trim the `sleep 6` and any curl retries; keep total ≤ 90 s.
(Optional 5-s insert if the dry run looks good: the Jaeger UI tab showing the
same trace with the red error span, while saying "here is the same trace in
Jaeger's UI".)

---

### Scene 3 — Generation at scale + determinism (2:20 – 3:05)

**DO:**
```bash
"$JAVA_HOME/bin/java" -Drandom.seed=42 -jar mist-cli/target/mist.jar \
    mist-cli/src/main/resources/My-Example/trainticket-demo-noexec.properties
```
Let the progress bar run ~5 s on screen, then **time-lapse** to completion
(~2.5 min real time). Then:
```bash
find mist-cli/src/test/java/trainticket_twostage_test -name 'Flow_Scenario_*.java' | wc -l
find mist-cli/src/test/java/trainticket_twostage_test -name 'Flow_Scenario_*.java' -exec sha256sum {} \; | sort | sha256sum
```

**EXPECT (verified):** `123` scenario files; note the aggregate hash on screen.

**SAY:**
> "Generation runs offline. This is the bundled TrainTicket demo — a
> 265-operation OpenAPI spec plus captured traces — generating cross-service
> JUnit scenarios with no SUT, no API key, and no network. Under a fixed random
> seed the output is byte-identical run to run — same files, same hashes —
> which is what makes the artifact reviewable. Negative variants follow the
> Sniper strategy: exactly one fault per variant, so every red test is
> attributable to one cause. Against the live forty-service TrainTicket, this
> engine generated fifteen-thousand thirty-six tests and detected all ten
> injected faults via the fault registry."

**DO (while saying the last sentence, show):**
```bash
grep -A3 "FAULT COVERAGE SUMMARY" debug/negative_test/runs/run22-fault-detection-10of10.txt
```
**EXPECT (verified):** `Total Injected Faults: 10 / Detected Faults: 10 (100.0%)`.

**CUT:** the time-lapse; keep the seed→hash beat tight.

---

### Scene 4 — Soft errors: the LLM-backed envelope check (3:05 – 3:45)

**DO:**
```bash
"$JAVA_HOME/bin/java" -cp mist-cli/target/mist.jar \
  -Dllm.openai_compatible.enabled=true \
  -Dllm.openai_compatible.url=https://api.deepseek.com/v1/chat/completions \
  -Dllm.openai_compatible.model=deepseek-chat \
  -Dllm.openai_compatible.api.key="$(cat .api_keys/DEEPSEEK_API_KEY)" \
  -Dmst.oracle.shape.invariants.span_tree.enabled=false \
  -Dmst.oracle.shape.invariants.status_propagation.enabled=false \
  evaluation/suts/trainticket/ResponseEnvelopeLiveCheck.java
```

**EXPECT (verified, ~20–40 s incl. one real LLM call — trim the wait):**
```
Soft-error response (HTTP 200): {"status":0,"msg":"start or end station not include in stationList.","data":null}
Status-class oracle: PASS (HTTP is 200)
  RESPONSE_ENVELOPE: FAIL  severity=ERROR  detail=status=0 classified as failure (LLM, cached)
```

**SAY:**
> "The second failure class: soft errors. TrainTicket answers HTTP 200 with
> status zero and data null — a domain rejection wearing a success code. The
> status-class oracle passes. MIST's ResponseEnvelope invariant classifies the
> unseen body value with a single LLM call, caches the rule, and fails the
> response. The same check flags Sock Shop's 200-with-status-code-500
> catalogue failure. And the hidden-downstream invariant generalises across
> protocols: on Online Boutique the swallowed call is gRPC — the transport
> says 200, the RPC status says error — and the same invariant fires on
> seven of the twelve committed outage traces, and on none of the healthy ones."

**CUT:** trim LLM latency to ~3 s.

---

### Scene 5 — Reproduce it yourself + close (3:45 – 4:25)

**DO:**
```bash
evaluation/run-offline-oracle.sh
```
Show the tail (skip the build — it is the pre-built jar anyway):
```
==> Done. Both fired offline -- no SUT, no LLM. (Healthy-control traces in the
    same directories stay silent; see REPRODUCE.md for the full matrix.)
```
Then switch to the GitHub repo tab for the close.

**SAY:**
> "Everything in this video reproduces from the repository. One script replays
> both hidden-downstream detections from committed traces — no cluster, no
> LLM, about ten minutes from a fresh clone including the build. The
> reproduction guide maps every paper claim to a command and its committed
> evidence. MIST is open source under LGPL, with four bundled systems under
> evaluation slash suts. Thanks for watching."

**DO:** End card (5 s): repo URL, DOI, "REPRODUCE.md — start at §5".

---

## 3. Copy-paste appendix (everything in run order)

```bash
# ---- prep (NOT recorded) ----
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64       # any JDK 21
cd <repo-root>                                            # jar already built
pkill -f "port-forward" 2>/dev/null                       # free 8080/16686
( while true; do kubectl port-forward -n istio-system svc/istio-ingressgateway 8080:80; sleep 2; done ) &
( while true; do kubectl port-forward -n istio-system svc/tracing 16686:80; sleep 2; done ) &
kubectl scale deploy ratings-v1 --replicas=1
rm -rf mist-cli/src/test/java/trainticket_twostage_test
curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8080/productpage   # expect 200

# ---- Scene 2 ----
evaluation/suts/bookinfo/workload/inject-ratings-outage.sh on
curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8080/api/v1/products/0/ratings   # 503
curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8080/api/v1/products/0/reviews   # 200
curl -s http://localhost:8080/api/v1/products/0/reviews | python3 -m json.tool | head -14
sleep 6
curl -s "http://localhost:16686/jaeger/api/traces?service=productpage.default&limit=20&lookback=10m" -o /tmp/live-traces.json
"$JAVA_HOME/bin/java" -cp mist-cli/target/mist.jar evaluation/suts/bookinfo/OracleCheck.java \
    /tmp/live-traces.json "GET /api/v1/products/0/reviews"
evaluation/suts/bookinfo/workload/inject-ratings-outage.sh off

# ---- Scene 3 ----
"$JAVA_HOME/bin/java" -Drandom.seed=42 -jar mist-cli/target/mist.jar \
    mist-cli/src/main/resources/My-Example/trainticket-demo-noexec.properties
find mist-cli/src/test/java/trainticket_twostage_test -name 'Flow_Scenario_*.java' | wc -l   # 123
find mist-cli/src/test/java/trainticket_twostage_test -name 'Flow_Scenario_*.java' -exec sha256sum {} \; | sort | sha256sum
grep -A3 "FAULT COVERAGE SUMMARY" debug/negative_test/runs/run22-fault-detection-10of10.txt

# ---- Scene 4 ----
"$JAVA_HOME/bin/java" -cp mist-cli/target/mist.jar \
  -Dllm.openai_compatible.enabled=true \
  -Dllm.openai_compatible.url=https://api.deepseek.com/v1/chat/completions \
  -Dllm.openai_compatible.model=deepseek-chat \
  -Dllm.openai_compatible.api.key="$(cat .api_keys/DEEPSEEK_API_KEY)" \
  -Dmst.oracle.shape.invariants.span_tree.enabled=false \
  -Dmst.oracle.shape.invariants.status_propagation.enabled=false \
  evaluation/suts/trainticket/ResponseEnvelopeLiveCheck.java

# ---- Scene 5 ----
evaluation/run-offline-oracle.sh

# ---- restore (NOT recorded) ----
kubectl scale deploy ratings-v1 --replicas=1
```

---

## 4. Fallback plan (if the live cluster misbehaves on recording day)

The §5 offline path is the documented reproduction path of record, so Scene 2
degrades gracefully: run the same `OracleCheck` on the **committed** outage
trace instead of the live capture —

```bash
"$JAVA_HOME/bin/java" -cp mist-cli/target/mist.jar evaluation/suts/bookinfo/OracleCheck.java \
  docs/main-contribution/evidence/bookinfo_e2e_traces/masked_reviews_ratings_outage.json \
  "GET /api/v1/products/0/reviews"
```

— and adjust one narration line: "replayed on the committed outage trace from
the artifact" instead of "the requests we just made". Output is identical in
shape (verified). Scenes 3–5 have no cluster dependency at all. Record scenes
as separate takes and stitch; nothing requires one continuous session.

---

## 5. Post-production

- [ ] Trim: jar startup pauses, `sleep 6`, LLM latency, generation time-lapse.
- [ ] Caption each scene with the command being run (small lower-third).
- [ ] Final length check: 3:00 ≤ t ≤ 5:00 (target 4:25).
- [ ] Export MP4 (H.264, 1080p).
- [ ] Upload: YouTube (unlisted) **and** add the MP4 to the Zenodo deposit
      (so the screencast survives link rot and ships with the DOI).
- [ ] Replace `\todo{SCREENCAST-URL}` in `paper/main_issta.tex` (abstract and
      the artifact paragraph) with the YouTube URL.
- [ ] Update `REPRODUCE.md` §9 and the README pointer to the same URL.
- [ ] Watch once end-to-end with audio on a laptop speaker (the reviewer's
      setup), not headphones.
