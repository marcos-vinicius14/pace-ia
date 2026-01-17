package com.paceai.infrastructure.ai.pgvector;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * pgvector RAG Adapter.
 * <p>
 * Handles vector similarity search for RAG-based coach functionality.
 * </p>
 */
@Component
public class PgVectorStore {

    // TODO: Inject JdbcTemplate or Spring AI VectorStore

    /**
     * Searches for similar documents based on query embedding.
     */
    public List<CoachingDocument> searchSimilar(float[] queryEmbedding, int topK) {
        // TODO: Implement vector similarity search
        // SELECT * FROM coaching_knowledge
        // ORDER BY embedding <=> query_embedding
        // LIMIT topK
        return List.of();
    }

    /**
     * Adds a document with its embedding to the vector store.
     */
    public void addDocument(String content, String source, float[] embedding) {
        // TODO: Implement document insertion
    }

    /**
     * Coaching document record.
     */
    public record CoachingDocument(
            String id,
            String content,
            String source,
            double similarity
    ) {}
}
