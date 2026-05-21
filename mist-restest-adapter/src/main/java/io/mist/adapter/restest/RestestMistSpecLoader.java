package io.mist.adapter.restest;

import es.us.isa.restest.specification.OpenAPISpecification;
import io.mist.core.spi.MistSpec;
import io.mist.core.spi.MistSpecLoader;

/**
 * {@link MistSpecLoader} implementation registered via
 * {@code META-INF/services/io.mist.core.spi.MistSpecLoader}.
 * Delegates parsing to {@link OpenAPISpecification}'s string-constructor.
 */
public final class RestestMistSpecLoader implements MistSpecLoader {

    /** Required no-arg constructor for {@link java.util.ServiceLoader}. */
    public RestestMistSpecLoader() {}

    @Override
    public MistSpec load(String location) {
        return new RestestMistSpec(new OpenAPISpecification(location));
    }
}
