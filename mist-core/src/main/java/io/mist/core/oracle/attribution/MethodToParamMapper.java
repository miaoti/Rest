package io.mist.core.oracle.attribution;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Phase 2: maps a leaf-error span's (service, operation) to the set of
 * parameter names that span is likely responsible for validating.
 *
 * <p>Three tiers (Phase 1 spec):
 * <ol>
 *   <li><b>OpenAPI extension hint</b> — exact match via
 *       {@code x-mist-param-validator-method}. Highest reliability;
 *       implementation deferred (no existing SUT publishes this).</li>
 *   <li><b>Naming heuristic</b> — split operation name into tokens
 *       (camelCase, snake_case, dot.case) and check token overlap with
 *       the candidate param name's tokens. {@code validateSeatNumber}
 *       overlaps with {@code seatNumber} via tokens {seat, number}.</li>
 *   <li><b>Probe cache</b> — deliberately-malformed-body probe records
 *       which param correlates with each leaf. Deferred to follow-up.</li>
 * </ol>
 *
 * <p>This Phase 2 release lands tier 2 only; tier 1 is wired but won't
 * fire until SUTs publish the extension; tier 3 is left as a hook.
 */
public final class MethodToParamMapper {

    /** Lowercase prefixes commonly used in validator-like method names; stripped before token analysis. */
    private static final Set<String> METHOD_PREFIX_NOISE = new HashSet<>(Arrays.asList(
            "validate", "check", "verify", "is", "has", "ensure", "assert", "require"
    ));

    private MethodToParamMapper() {}

    /**
     * Return true when the candidate parameter name is plausibly the one
     * the leaf span is validating. Implements tier 2 naming heuristic:
     * compare normalized tokens of operation and candidate.
     *
     * @param operation operation/method name from the leaf error span
     *                  (e.g., {@code "OrderServiceImpl.validateSeat"})
     * @param candidateParam parameter name from the target test (e.g.,
     *                  {@code "seatNumber"})
     */
    public static boolean isResponsibleFor(String operation, String candidateParam) {
        if (operation == null || operation.isEmpty()
                || candidateParam == null || candidateParam.isEmpty()) {
            return false;
        }
        Set<String> opTokens = stripPrefixNoise(tokenize(operation));
        Set<String> paramTokens = tokenize(candidateParam);
        if (opTokens.isEmpty() || paramTokens.isEmpty()) return false;
        // Any token overlap is enough — operation names typically include
        // the validated entity ("validateSeat", "checkSeatAvailability").
        for (String t : paramTokens) {
            if (opTokens.contains(t)) return true;
        }
        return false;
    }

    /**
     * Tokenize a CamelCase / snake_case / dot.case identifier into a set
     * of lowercase tokens. Drops 1-char tokens.
     */
    static Set<String> tokenize(String identifier) {
        if (identifier == null) return Collections.emptySet();
        // Replace dots, underscores, slashes with space; insert space before
        // each uppercase letter that follows a lowercase one.
        String spaced = identifier
                .replaceAll("[._/]+", " ")
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1 $2") // ACRONYMNext -> ACRONYM Next
                .toLowerCase();
        String[] parts = spaced.split("\\s+");
        Set<String> tokens = new HashSet<>();
        for (String p : parts) {
            if (p.length() < 2) continue;
            tokens.add(p);
        }
        return tokens;
    }

    static Set<String> stripPrefixNoise(Set<String> tokens) {
        Set<String> out = new HashSet<>(tokens);
        out.removeAll(METHOD_PREFIX_NOISE);
        return out;
    }
}
