package io.mist.adapter.restest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * One-way converter from RESTest's
 * {@code es.us.isa.restest.configuration.pojos.*} configuration POJOs
 * to the byte-equivalent vendored copies under
 * {@code io.mist.core.spec.*}.
 *
 * <p>The converter exists because the vendored copies are
 * type-distinct from the originals despite holding the same shape:
 * RESTest's own configuration loader produces the
 * {@code es.us.isa.restest.*} version, while mist-core-resident
 * code consumes the {@code io.mist.core.spec.*} version. This class
 * is the per-call boundary translator.
 *
 * <p>Conversion is shallow per call but recursive across nested
 * fields (a converted {@code TestConfigurationObject} drags along
 * its nested {@code Auth}, {@code TestConfiguration}, etc.).
 *
 * <p>{@code null} inputs convert to {@code null} outputs at every
 * level so the wrappers can be used unconditionally on optional
 * fields.
 */
public final class PojoConverter {

    private PojoConverter() {}

    public static io.mist.core.spec.TestConfigurationObject toCore(
            es.us.isa.restest.configuration.pojos.TestConfigurationObject src) {
        if (src == null) return null;
        io.mist.core.spec.TestConfigurationObject dst =
                new io.mist.core.spec.TestConfigurationObject();
        dst.setAuth(toCore(src.getAuth()));
        dst.setTestConfiguration(toCore(src.getTestConfiguration()));
        return dst;
    }

    public static io.mist.core.spec.TestConfiguration toCore(
            es.us.isa.restest.configuration.pojos.TestConfiguration src) {
        if (src == null) return null;
        io.mist.core.spec.TestConfiguration dst =
                new io.mist.core.spec.TestConfiguration();
        dst.setServices(src.getServices());
        if (src.getOperations() != null) {
            List<io.mist.core.spec.Operation> ops =
                    src.getOperations().stream()
                            .map(PojoConverter::toCore)
                            .collect(Collectors.toList());
            dst.setOperations(ops);
        }
        return dst;
    }

    public static io.mist.core.spec.Auth toCore(
            es.us.isa.restest.configuration.pojos.Auth src) {
        if (src == null) return null;
        io.mist.core.spec.Auth dst = new io.mist.core.spec.Auth();
        dst.setRequired(src.getRequired());
        dst.setQueryParams(src.getQueryParams());
        dst.setHeaderParams(src.getHeaderParams());
        dst.setApiKeysPath(src.getApiKeysPath());
        dst.setHeadersPath(src.getHeadersPath());
        dst.setOauthPath(src.getOauthPath());
        return dst;
    }

    public static io.mist.core.spec.Operation toCore(
            es.us.isa.restest.configuration.pojos.Operation src) {
        if (src == null) return null;
        io.mist.core.spec.Operation dst = new io.mist.core.spec.Operation();
        dst.setTestPath(src.getTestPath());
        dst.setOperationId(src.getOperationId());
        dst.setMethod(src.getMethod());
        dst.setExpectedResponse(src.getExpectedResponse());
        dst.setOpenApiOperation(src.getOpenApiOperation());
        if (src.getTestParameters() != null) {
            List<io.mist.core.spec.TestParameter> params =
                    src.getTestParameters().stream()
                            .map(PojoConverter::toCore)
                            .collect(Collectors.toList());
            dst.setTestParameters(params);
        }
        return dst;
    }

    public static io.mist.core.spec.TestParameter toCore(
            es.us.isa.restest.configuration.pojos.TestParameter src) {
        if (src == null) return null;
        io.mist.core.spec.TestParameter dst = new io.mist.core.spec.TestParameter();
        dst.setName(src.getName());
        dst.setIn(src.getIn());
        dst.setWeight(src.getWeight());
        dst.setDescription(src.getDescription());
        dst.setType(src.getType());
        dst.setFormat(src.getFormat());
        dst.setPattern(src.getPattern());
        dst.setEnumValues(src.getEnumValues());
        dst.setMinimum(src.getMinimum());
        dst.setMaximum(src.getMaximum());
        dst.setMinLength(src.getMinLength());
        dst.setMaxLength(src.getMaxLength());
        dst.setExample(src.getExample());
        dst.setRequired(src.getRequired());
        if (src.getGenerators() != null) {
            List<io.mist.core.spec.Generator> gens =
                    src.getGenerators().stream()
                            .map(PojoConverter::toCore)
                            .collect(Collectors.toList());
            dst.setGenerators(gens);
        }
        return dst;
    }

    public static io.mist.core.spec.Generator toCore(
            es.us.isa.restest.configuration.pojos.Generator src) {
        if (src == null) return null;
        io.mist.core.spec.Generator dst = new io.mist.core.spec.Generator();
        dst.setType(src.getType());
        dst.setValid(src.isValid());
        if (src.getGenParameters() != null) {
            List<io.mist.core.spec.GenParameter> ps =
                    src.getGenParameters().stream()
                            .map(PojoConverter::toCore)
                            .collect(Collectors.toList());
            dst.setGenParameters(ps);
        }
        return dst;
    }

    public static io.mist.core.spec.GenParameter toCore(
            es.us.isa.restest.configuration.pojos.GenParameter src) {
        if (src == null) return null;
        io.mist.core.spec.GenParameter dst = new io.mist.core.spec.GenParameter();
        dst.setName(src.getName());
        dst.setValues(src.getValues());
        dst.setObjectValues(src.getObjectValues());
        return dst;
    }

    /**
     * Convenience map-level conversion for the common
     * {@code Map<String, TestConfigurationObject>} shape used by
     * {@code MicroserviceTestConfigurationIO.loadMultiServiceConfiguration}.
     * Preserves iteration order via {@link LinkedHashMap}.
     */
    public static Map<String, io.mist.core.spec.TestConfigurationObject> toCoreMap(
            Map<String, es.us.isa.restest.configuration.pojos.TestConfigurationObject> src) {
        if (src == null) return null;
        Map<String, io.mist.core.spec.TestConfigurationObject> dst =
                new LinkedHashMap<>(src.size());
        for (Map.Entry<String, es.us.isa.restest.configuration.pojos.TestConfigurationObject> e : src.entrySet()) {
            dst.put(e.getKey(), toCore(e.getValue()));
        }
        return dst;
    }

    /**
     * Map-level conversion from RESTest's
     * {@code Map<String, OpenAPISpecification>} (the wrapper that owns the
     * parser) to a plain {@code Map<String, OpenAPI>} (the swagger-core
     * model) — the shape {@code SemanticDependencyRegistry.build} now
     * expects after the B1 move into mist-core.
     */
    public static Map<String, io.swagger.v3.oas.models.OpenAPI> toOpenApiMap(
            Map<String, es.us.isa.restest.specification.OpenAPISpecification> src) {
        if (src == null) return null;
        Map<String, io.swagger.v3.oas.models.OpenAPI> dst =
                new LinkedHashMap<>(src.size());
        for (Map.Entry<String, es.us.isa.restest.specification.OpenAPISpecification> e : src.entrySet()) {
            es.us.isa.restest.specification.OpenAPISpecification spec = e.getValue();
            dst.put(e.getKey(), spec == null ? null : spec.getSpecification());
        }
        return dst;
    }
}
