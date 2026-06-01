# A-main 行动方案(可查询 · 带 citation)

> 配套战略文档:`docs/main-contribution/A_MAIN_ROADMAP.md`(deep-research 全文,2026-06-01)。
> 本文件是**可执行计划**:① 两个命门的探测方法;② grounding 5 个修复 + 验证。
> 状态(2026-06-01):方案已定;grounding 修复 = 进行中;两个命门探测 = 待办。

---

## §1 A-main 贡献定位(一句话 + 必须/禁止 claim)

**贡献**:首个 label-free **分布式-trace** oracle,检测 (i) swallowed-downstream-failure(网关 2xx,下游 span 藏被吞的 5xx/gRPC-error)与 (ii) trace-only silent-acceptance——**客户端响应里看不见**;配一个"这类失败在真实系统多普遍 + oracle 检出多少"的测量研究。

- **禁止 claim**:soft-error(2xx-body-域错误)检测**不是新颖点**——已被 LogiAgent / RBCTest / RESTifAI 在 response-level 占了。
  - LogiAgent: arXiv 2503.15079 ; RBCTest (ICSE'26): arXiv 2504.17287 ; RESTifAI (ICSE'26 demo): arXiv 2512.08706。
- **必须 claim**:**trace-only / 跨服务**那一类。上述全部 + AGORA+(TOSEM'25, javalenzuela.com/.../2025_tosem_agoraPlus.pdf)**都是单响应/单操作、无一吃分布式 trace**(deep-research 3-0 核实)。

**动机(引用)**:Uber《The Tale of Errors in Microservices》SIGMETRICS'25 / POMACS 8(3),**DOI 10.1145/3700436**——~**29.35%** 的成功(2xx)请求,调用链深处藏被吞非致命错误(11B RPC、6000+ 服务)。
**真实 bug ground truth(引用)**:TrainTicket TSE'18(IEEE TSE.2018.2887384 + github.com/FudanSELab/train-ticket-fault-replicate),F6/F7/F10/F12 = 被吞/未处理下游;**措辞=real-world-derived re-injected,非 wild bug**。

---

## §2 两个命门 + 具体探测方法(这是"摸"的计划)

### 命门 A:intent-conditioned attribution 能不能从"弱"做到"可量化、抗 ablation"?
- **为什么是命门**:这是唯一真新颖的 method 点。做不实 → 方法腿塌、全压到 study 腿(B),B 就必须跑到真生产级规模才扛得住。现状:memory `project_phase2_attribution_gap` —— TT 上 `TARGET_REJECTION=0`,只 service-level、param-level 不支持。
- **探测方法(只摸、先不大改)**:
  1. 读 `TargetAttributionInvariant` + `TraceAttribution.attribute(...)`,定位"为什么 param-level 归因拿不到"(span 里有没有 param 粒度信息?还是只有 controller 粒度?)。**file:line 取证**。
  2. 在 1 个能注入的端点(TT adminroute)上,手动构造"攻 param X、期望被拒"的 trace,跑 attribute(),看它能否分出 TARGET / WRONG_PARAM / UPSTREAM。记录实际输出。
  3. 判定:**(a)** 能到 param-level(需要 span 里有足够信息)→ attribution 可做强,方法腿成立;**(b)** span 本质上只有 service/controller 粒度 → param-level 归因**信息论上不可得**,attribution 只能停在 service-level → **方法腿降级**,论文重心移到 study 腿。
- **产出**:`debug/a-main/probe-attribution.md`(取证 + 判定 a/b)。

### 命门 B:除 TrainTicket 外,到底有没有可挖的**野生(commit 历史)**被吞-下游 / silent-accept bug 源?
- **为什么是命门**:AGORA+ 的 bar 是 **32 个确认的真实 bug**;reviewer 对 wild bug 权重远高于注入。deep-research **未能证明**这种语料存在(列为 aspirational)。
- **探测方法(只摸)**:
  1. 在目标 OSS 微服务项目(train-ticket、sock-shop、online-boutique、DeathStarBench、以及更广的 Spring-Cloud / Go-microservices 项目)的 issue/PR/commit 里,按关键词检索:`swallow exception`、`silent failure`、`ignored error`、`graceful degrad* 导致的 wrong result`、`catch.*return null/empty`、`2xx but wrong`。
  2. 对命中,人工判定是否="下游故障被吞在 2xx 后面 / 非法输入被静默接受",且**可复现**(有 repro 步骤 / 修复 commit)。
  3. 统计:能找到几条**可复现的 wild bug**。判定:**≥~5–10 条可复现** → study 腿能用 wild bug 加固,A-main 可达;**几乎找不到** → 只能靠 TrainTicket re-injected + 大规模 trace 测量,A-main 风险显著上升。
- **产出**:`debug/a-main/probe-wildbugs.md`(命中清单 + 可复现性 + 计数 + 判定)。**附**:也核 RCAEval(github.com/phamquiluan/RCAEval)、《Simple Testing Can Prevent Most Critical Failures》(distributed-systems failure 语料)是否含可用 ground truth。

**决策门**:两命门**任一为否**,路线降级(见 ROADMAP §9)。**先并行摸这两个,再决定要不要 all-in。**

---

## §3 Grounding 修复(5 个 defect)+ 验证 —— 战术,服务 tool/demo,不进 A-main 承重墙

> 诊断来源:本会话 a3b7d1e045c9cdbb1 agent + 我手动复验。file:line 见下(可能 ±,改前再 Read 确认)。

| # | Defect | 位置 | 修法 | 影响 |
|---|---|---|---|---|
| **A** | percentage=0.3 门 → 70% 值直接 LLM 瞎编 | `SmartInputFetcher.java:312`(`if random < percentage`) | 反转语义:**先 fetchFromSmartSource,返回 null 才回退 LLM**;`1-percentage` 仅作"故意注入多样性"的小概率。同时 SUT `*-mst.properties` `smart.input.fetch.percentage` 调高。 | 主因,最高影响 |
| **B** | pool 先建先用、按 0.3 稀释 | `SharedPoolSupport.java:201-234` 建池;`MistGenerator.java:1182-1209` 消费 | 池**优先放 fetch 到的 live 值**(用 `fetchSmartInputWithProvenance` 的 `RESOLVED_LIVE` 标签),LLM 仅补足;消费端 `preferVerifiedValues` 也认 `RESOLVED_LIVE` | 结构性 |
| **C** | whitelist 太严,drop 掉 LLM 发现的端点 | `SmartInputFetcher.java:563-586` | 白名单**从 conf 的 OpenAPI path 段播种** + 模糊/子串匹配 + 允许返回端点 path(对着 spec 校验) | 对无 registry 的 SUT |
| **D** | 抽取不校验,污染 DB 的 `{}`/`12345678901` 被当真值 | `SmartInputFetcher.java:974-989`(直连字段匹配分支) | 一行:`return value.toString()` 前加 `isValidValueForParameter(...)` 守卫(对齐 992-1008 的语义分支) | 次要但简单 |
| **E** | 学坏的"毒映射"(endStation→`/routes/{start}/{end}` + 记录的 HTTP-400) | registry(`trainticket/input-fetch-registry.yaml`)+ `fetchFromSmartSource` 选映射处 | 选映射时**跳过带 `errorType/VALIDATION_ERROR` 记录的、或 successRate 低的**;对同名站类参数**泛化到最高 successRate 的 `/stations` 源** | 是 percentage=1.0 那次仍失败的真因 |

**验证(重试)**:改完 → `mvn -q package -pl mist-cli -am -Dmaven.test.skip=true` 重建 jar → 在 TT(临时缩规模 properties + 单 adminroute trace,**注意 testsperoperation/variants 是 file 属性、`-D` 覆盖不了,要改文件**)重跑 → **量正例"被接受率(HTTP 200)"修前(≈0)vs 修后**,并直接看生成测试里 `startStation/endStation/stationList` 是不是真实站名。**修前基线已知:0/1608 正例通过;percentage=1.0 单改无效(endStation 仍 "Guangzhou South")。**

**判定**:被接受率明显抬升 + 站名是 `/stations` 真值 → grounding 可修、且我们才算真正摸到天花板;仍≈0 → 还有未识别层,继续诊断。

---

## §4 里程碑 / 顺序
1. (本轮)落本方案 → **修 grounding 5 条 + 重建 + TT 重试量被接受率**。
2. **并行摸两个命门**(§2,各出一个 probe-*.md)。
3. 据命门结果**定/降级** A-main 路线。
4. 不耽误:**ISSTA tool demo(6/26)** 照常推进(它不依赖以上)。

---

## References(一手源,deep-research 已 3-0/近一致核实)
- RESTifAI (ICSE'26 demo): https://arxiv.org/html/2512.08706v1
- LogiAgent: https://arxiv.org/abs/2503.15079 ; https://arxiv.org/html/2503.15079v1
- RBCTest (ICSE'26): https://arxiv.org/html/2504.17287
- AGORA+ (TOSEM'25): https://www.javalenzuela.com/publication/2025_tosem_agoraplus/2025_tosem_agoraPlus.pdf
- Uber《Tale of Errors》SIGMETRICS'25 / POMACS 8(3): https://dl.acm.org/doi/10.1145/3700436
- TrainTicket faults: IEEE TSE.2018.2887384 ; https://github.com/FudanSELab/train-ticket-fault-replicate
- 旁证:RCAEval https://github.com/phamquiluan/RCAEval ; Tracetest https://tracetest.io/ ; 《Simple Testing Can Prevent Most Critical Failures》(OSDI'14 失败分析)
- 代码诊断:本会话 grounding-诊断 agent + 手动复验(SmartInputFetcher / SharedPoolSupport / MistGenerator file:line 见 §3)
