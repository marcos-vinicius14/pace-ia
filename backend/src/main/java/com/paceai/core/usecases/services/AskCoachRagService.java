package com.paceai.core.usecases.services;

/**
 * Ask Coach RAG Use Case (Application Service).
 * <p>
 * Orchestrates the RAG-based coach chatbot functionality.
 * </p>
 */
public class AskCoachRagService {

    // TODO: Inject required ports (AIGatewayPort, VectorStorePort)

    public AskCoachResponse execute(AskCoachRequest request) {
        // TODO: Implement RAG coach logic
        // 1. Search vector store for relevant documents
        // 2. Build context with athlete history
        // 3. Generate AI response with retrieved context
        // 4. Return response with citations
        return null;
    }

    public record AskCoachRequest(
            String athleteId,
            String question
    ) {}

    public record AskCoachResponse(
            String answer,
            java.util.List<String> sources
    ) {}
}
