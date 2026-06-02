# Reviewer-remediation experiments (real runs, 2026-06-02)

Host: OpenJDK 21, prebuilt `mist-cli/target/mist.jar`, `.api_keys/DEEPSEEK_API_KEY`
present. All runs use MIST's **shipped** classes (no reimplementation).

## A1 (F1) — Boutique HiddenDownstreamFailure fire counts, independently re-run
`java -cp mist.jar evaluation/suts/bookinfo/OracleCheck.java <trace> "GET /"`
(OracleCheck instantiates the shipped `HiddenDownstreamFailureInvariant`).

| trace file (committed) | loaded | FIRES | paper claim | match |
|---|---|---|---|---|
| boutique_adservice_outage.json | 12 | **7** | 7 of 12 | ✅ |
| boutique_frontend_healthy.json | 12 | **0** | 0 of 12 | ✅ |
| boutique_adservice_outage_recapture.json | 40 | **24** | 24/40 | ✅ |
| boutique_frontend_healthy_recapture.json | 30 | **0** | 0/30 | ✅ |

Conclusion: all four paper numbers reproduce exactly. Reviewer R3's "paper 7/12
contradicts evidence 8/12" was a MISREAD — `boutique_e2e_pipeline.md` says 8
*route through* adservice but **7 fire** (the 8th adservice-routing trace is rooted
at `productcatalogservice`, an internal entry, correctly not flagged). Paper fix =
tighten the §5 parenthetical only (number is right); also answers R2-Q3 (the 5
non-firing outage traces are true negatives, not misses).

## A3 (F8) — ResponseEnvelope LLM classifier, live DeepSeek calls
`java -cp mist.jar -Dllm.openai_compatible.* … evaluation/suts/trainticket/ResponseEnvelopeLiveCheck.java <body> <rootApiKey>`
(real `ZeroShotLLMGenerator.validateResponse`, fresh store ⇒ live LLM call each time).

| body | RESPONSE_ENVELOPE | expected | note |
|---|---|---|---|
| `{"status":0,"msg":"start or end station not include…","data":null}` (real, committed) | **FAIL** | FAIL | ✅ live re-confirm of the soft-error flip |
| `{"status":1,"msg":"Save success!","data":{…route…}}` | **pass** | pass | ✅ no FP on a clear success |
| `{"status":1,"msg":"Success","data":null}` | **FAIL** | pass | ⚠️ **FALSE POSITIVE** — LLM flagged a `status:1` success as failure ("status=1 classified as failure") |

Conclusion: the classifier correctly flips clear `status:0` soft errors (the paper's
claim holds, re-confirmed live) and passes clear successes, but **can false-positive
on an ambiguous `status:1`+`data:null` envelope**. This is exactly the LLM FP risk
R1/R2 flagged, and it justifies keeping G1/ResponseEnvelope modest: it is the
**LLM-backed, lower-assurance** complement to the label-free HiddenDownstreamFailure.
Paper fix = add that honest caveat in §5; do NOT claim a clean FP rate. (Caveat: the
3rd body is a plausible-but-constructed control, not a captured TT response, so this
is a *risk demonstration*, not a measured FP rate — recorded here for the team; the
classifier's `data:null` handling is worth a follow-up if G1 is ever foregrounded.)
