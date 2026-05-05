package es.us.isa.restest.llm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Locks in the env-var placeholder resolver used for the optional
 * {@code llm.local.api.key} property. The resolver lets the secret stay
 * outside the committed properties file (read from {@code $ENV_VAR} or
 * {@code -DSYS_PROP}).
 */
public class LLMConfigEnvResolverTest {

    @Test
    public void literalValuePassesThrough() {
        assertEquals("sk-abc", LLMConfig.resolveEnvPlaceholder("sk-abc"));
    }

    @Test
    public void emptyAndNullReturnEmpty() {
        assertEquals("", LLMConfig.resolveEnvPlaceholder(""));
        assertEquals("", LLMConfig.resolveEnvPlaceholder(null));
    }

    @Test
    public void placeholderWithoutMatchingEnvReturnsEmpty() {
        assertEquals("", LLMConfig.resolveEnvPlaceholder("${RESTEST_NO_SUCH_VAR_XYZ}"));
    }

    @Test
    public void placeholderWithDefaultUsesDefault() {
        assertEquals("fallback",
                LLMConfig.resolveEnvPlaceholder("${RESTEST_NO_SUCH_VAR_XYZ:fallback}"));
    }

    @Test
    public void systemPropertyFallback() {
        String key = "RESTEST_LLMCONFIG_TEST_KEY";
        try {
            System.setProperty(key, "sk-from-sys");
            assertEquals("sk-from-sys",
                    LLMConfig.resolveEnvPlaceholder("${" + key + "}"));
        } finally {
            System.clearProperty(key);
        }
    }

    @Test
    public void surroundingWhitespaceIsTrimmed() {
        assertEquals("sk-abc", LLMConfig.resolveEnvPlaceholder("  sk-abc  "));
    }
}
