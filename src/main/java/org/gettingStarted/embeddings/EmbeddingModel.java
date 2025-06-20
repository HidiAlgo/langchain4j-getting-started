package org.gettingStarted.embeddings;

import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.model.output.Response;

public class EmbeddingModel
{
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    private static AllMiniLmL6V2EmbeddingModel allMiniLmL6V2EmbeddingModel = null;

    public static void main(String[] args)
    {
        dev.langchain4j.model.embedding.EmbeddingModel model = getEmbedding_OpenAiEmbeddingModel();
        Response<dev.langchain4j.data.embedding.Embedding> response = model.embed( "I want to live a fucking awesome life!" );

        System.out.println( response.content() );
    }

    public static dev.langchain4j.model.embedding.EmbeddingModel getEmbedding_OpenAiEmbeddingModel()
    {
        dev.langchain4j.model.embedding.EmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey( OPENAI_API_KEY )
                .modelName( OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL )
                .build();
        return embeddingModel;
    }

    public static dev.langchain4j.model.embedding.EmbeddingModel getEmbedding_AllMiniLmL6V2EmbeddingModel()
    {
        if ( allMiniLmL6V2EmbeddingModel == null )
        {
            allMiniLmL6V2EmbeddingModel = new AllMiniLmL6V2EmbeddingModel();
        }
        return allMiniLmL6V2EmbeddingModel;
    }
}
