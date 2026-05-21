package io.mist.adapter.restest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * One-way converter from mist-core's vendored test-case carriers
 * ({@code io.mist.core.testcase.{TestCase, MultiServiceTestCase}})
 * to the RESTest-side equivalents in
 * {@code es.us.isa.restest.testcases.*}.
 *
 * <p>{@link io.mist.core.generation.MistGenerator} produces mist-core
 * test cases; the RESTAssured writer in {@code mist-restest-adapter}
 * still consumes the RESTest carriers (prompt § 10 forbids touching
 * the writer's internals). This converter is the per-call boundary
 * bridge that sits between the two.
 *
 * <p>Conversion preserves every data field on the carriers: the
 * base {@code TestCase} fields (id, faulty, operationId, method,
 * path, all parameter maps, body, formats, oracle flags), the
 * {@code MultiServiceTestCase}-specific fields (scenario name,
 * fault metadata, status-code exploration toggles, parameter
 * provenance), and the {@code StepCall} list with each step's
 * dependency graph and trace bindings.
 *
 * <p>{@link io.mist.core.spec.Operation} references inside the
 * mist-core {@code StepCall} are converted back to RESTest's
 * {@code Operation} pojo via {@link PojoConverter#toRestest}
 * (this class's symmetric counterpart for the configuration
 * pojos).
 */
public final class TestCaseConverter {

    private TestCaseConverter() {}

    /**
     * Convert a mist-core test case to its RESTest equivalent.
     * Dispatches on the runtime type so {@link io.mist.core.testcase.MultiServiceTestCase}
     * preserves its workflow steps; plain {@link io.mist.core.testcase.TestCase}
     * copies the base data carrier surface.
     */
    public static es.us.isa.restest.testcases.TestCase fromCore(io.mist.core.testcase.TestCase src) {
        if (src == null) return null;
        if (src instanceof io.mist.core.testcase.MultiServiceTestCase) {
            return fromCoreMst((io.mist.core.testcase.MultiServiceTestCase) src);
        }
        es.us.isa.restest.testcases.TestCase dst =
                new es.us.isa.restest.testcases.TestCase(
                        src.getId(), src.getFaulty(), src.getOperationId(),
                        src.getPath(), src.getMethod());
        copyBaseFields(src, dst);
        return dst;
    }

    /**
     * Apply {@link #fromCore} across a {@link Collection}, returning a
     * {@link List} so the writer's index-based access works.
     */
    public static List<es.us.isa.restest.testcases.TestCase> fromCoreCollection(
            Collection<? extends io.mist.core.testcase.TestCase> src) {
        if (src == null) return null;
        List<es.us.isa.restest.testcases.TestCase> out = new ArrayList<>(src.size());
        for (io.mist.core.testcase.TestCase tc : src) out.add(fromCore(tc));
        return out;
    }

    private static es.us.isa.restest.testcases.MultiServiceTestCase fromCoreMst(
            io.mist.core.testcase.MultiServiceTestCase src) {
        es.us.isa.restest.testcases.MultiServiceTestCase dst =
                new es.us.isa.restest.testcases.MultiServiceTestCase();
        // Replace the default-ctor-assigned synthetic id with the source's
        // own id so trace correlation keys stay aligned across the bridge.
        dst.setId(src.getId());
        copyBaseFields(src, dst);
        // ── multi-service workflow surface ─────────────────────────────
        dst.setScenarioName(src.getScenarioName());
        if (src.getFaultyParameters() != null) {
            for (String fp : src.getFaultyParameters()) {
                // RESTest's addFaultyParameter takes (name, value); the
                // mist-core list stores the joined "name=value" form.
                int eq = fp.indexOf('=');
                if (eq > 0) {
                    dst.addFaultyParameter(fp.substring(0, eq), fp.substring(eq + 1));
                } else {
                    dst.addFaultyParameter(fp, "");
                }
            }
        }
        dst.setTargetFaultRootId(src.getTargetFaultRootId());
        dst.setFaultTypeCategory(src.getFaultTypeCategory());
        dst.setTargetFaultRootApiPath(src.getTargetFaultRootApiPath());
        dst.setTargetFaultParamLocation(src.getTargetFaultParamLocation());
        if (src.hasTargetFaultValue()) {
            dst.setTargetFaultValue(src.getTargetFaultValue());
        }
        dst.setStatusCodeExplorationTest(src.isStatusCodeExplorationTest());
        if (src.getTargetStatusCode() >= 0) {
            dst.setTargetStatusCode(src.getTargetStatusCode());
        }
        // parameter provenance map carries straight across (same enum type).
        for (Map.Entry<String, io.mist.core.value.ValueProvenance> e
                : src.getParameterProvenance().entrySet()) {
            String key = e.getKey();
            int colon = key.indexOf(':');
            if (colon > 0) {
                int stepIdx = Integer.parseInt(key.substring(0, colon));
                String pname = key.substring(colon + 1);
                dst.recordParameterProvenance(stepIdx, pname, e.getValue());
            }
        }
        // ── workflow steps ─────────────────────────────────────────────
        if (src.getSteps() != null) {
            for (io.mist.core.testcase.MultiServiceTestCase.StepCall coreStep : src.getSteps()) {
                dst.addStepCall(fromCoreStep(coreStep));
            }
        }
        return dst;
    }

    private static es.us.isa.restest.testcases.MultiServiceTestCase.StepCall fromCoreStep(
            io.mist.core.testcase.MultiServiceTestCase.StepCall src) {
        es.us.isa.restest.testcases.MultiServiceTestCase.StepCall dst =
                new es.us.isa.restest.testcases.MultiServiceTestCase.StepCall(
                        src.getServiceName(),
                        PojoConverter.toRestest(src.getMethod()),
                        src.getPath(),
                        copyMap(src.getPathParams()),
                        copyMap(src.getQueryParams()),
                        copyMap(src.getHeaders()),
                        src.getBody(),
                        src.getExpectedStatus(),
                        copyMap(src.getBodyFields()));
        if (src.getCookies() != null) {
            for (Map.Entry<String, String> e : src.getCookies().entrySet()) {
                dst.getCookies().put(e.getKey(), e.getValue());
            }
        }
        if (src.getCaptureOutputKeys() != null) {
            for (String k : src.getCaptureOutputKeys()) dst.addCaptureOutputKey(k);
        }
        if (src.getParamDependencies() != null) {
            for (Map.Entry<String, io.mist.core.testcase.MultiServiceTestCase.Dependency> e
                    : src.getParamDependencies().entrySet()) {
                io.mist.core.testcase.MultiServiceTestCase.Dependency cd = e.getValue();
                dst.addParamDependency(e.getKey(), cd.sourceStepIndex, cd.sourceOutputKey);
            }
        }
        if (src.getWorkflowDependencies() != null) {
            for (Integer i : src.getWorkflowDependencies()) dst.addWorkflowDependency(i);
        }
        dst.setDependencyType(
                es.us.isa.restest.testcases.MultiServiceTestCase.DependencyType.valueOf(
                        src.getDependencyType().name()));
        dst.setHierarchicalId(src.getHierarchicalId());
        dst.setTopLevelRoot(src.isTopLevelRoot());
        dst.setMergedRootStep(src.isMergedRootStep());
        dst.setProducerRootIndex(src.getProducerRootIndex());
        for (Map.Entry<String, String> pb : src.getProvenanceBindings().entrySet()) {
            dst.addProvenanceBinding(pb.getKey(), pb.getValue());
        }
        dst.setTraceResponseBody(src.getTraceResponseBody());
        return dst;
    }

    private static void copyBaseFields(
            io.mist.core.testcase.TestCase src,
            es.us.isa.restest.testcases.TestCase dst) {
        dst.setFaulty(src.getFaulty());
        dst.setFulfillsDependencies(src.getFulfillsDependencies());
        dst.setFaultyReason(src.getFaultyReason());
        dst.setEnableOracles(src.getEnableOracles());
        dst.setOperationId(src.getOperationId());
        dst.setMethod(src.getMethod());
        dst.setPath(src.getPath());
        dst.setInputFormat(src.getInputFormat());
        dst.setOutputFormat(src.getOutputFormat());
        dst.setHeaderParameters(copyMap(src.getHeaderParameters()));
        dst.setPathParameters(copyMap(src.getPathParameters()));
        dst.setQueryParameters(copyMap(src.getQueryParameters()));
        dst.setFormParameters(copyMap(src.getFormParameters()));
        dst.setBodyParameter(src.getBodyParameter());
        dst.setExpectedResponse(src.getExpectedResponse());
    }

    private static Map<String, String> copyMap(Map<String, String> src) {
        if (src == null) return null;
        return new LinkedHashMap<>(src);
    }
}
