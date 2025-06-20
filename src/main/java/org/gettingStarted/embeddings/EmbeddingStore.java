package org.gettingStarted.embeddings;

import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class EmbeddingStore {

    public static InMemoryEmbeddingStore getEmbeddingStore_InMemoryEmbeddingStore()
    {
        return new InMemoryEmbeddingStore();
    }
}
