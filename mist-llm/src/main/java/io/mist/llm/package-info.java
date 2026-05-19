/**
 * Future home for MIST's LLM dispatch + cache layer (LLMService, LLMConfig,
 * LLMCallCache, GeminiApiClient, OllamaApiClient). Currently a placeholder —
 * the legacy {@code llm.*} classes still live in {@code mist-restest-adapter}
 * because they pull in RESTest-specific helpers. Migration is a follow-up
 * task that does not affect the Stage 1.C gate (an empty mist-llm module
 * already proves it can build independently of mist-core and restest-core).
 */
package io.mist.llm;
