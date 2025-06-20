package org.gettingStarted.assistant;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.gettingStarted.embeddings.EmbeddingModel;
import org.gettingStarted.embeddings.EmbeddingStore;
import org.gettingStarted.models.Models;

public class DissanayakeRestaurent
{
    private static InMemoryEmbeddingStore embeddingStore;
    private static
    dev.langchain4j.model.embedding.EmbeddingModel embeddingModel;

    public static void main(String[] args) {
        initiateChatBot();
    }

    public static void initiateChatBot()
    {
        updateEmbedStore();

        ChatMemory memory = MessageWindowChatMemory.withMaxMessages( 20 );
        ChatModel model = Models.getOpenAiChatModelGPT_4_0_MINI();

        SystemMessage systemMessage = new SystemMessage("You are an assistant for a restaurant called Dissanayaka. This is a chinese food restaurant." +
                "You are required to provide minimal responses in a polite manner so that customer will make an order.");

        memory.add( systemMessage );

        Embedding question = embeddingModel.embed( "I wonder where this Dissanayake Restaurent located and its master chef?").content();

        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(question)
                .maxResults(3)
                .minScore(0.5)
                .build();
        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);

        searchResult.matches().forEach(match -> {
            memory.add(UserMessage.from(match.embedded().text()));
        });

        UserMessage userMessage = UserMessage.from("I wonder where this Dissanayake Restaurent located and its master chef?");
        memory.add( userMessage );
        ChatResponse chatResponse = model.chat( memory.messages() );

        System.out.println( chatResponse.aiMessage().text() );
    }

    public static void updateEmbedStore()
    {
        // embedding text segments
        TextSegment segment1 = TextSegment.from("Dissanayake Restaurant is located in Sri Lanka, in Hambantota District, Tissamaharama city");
        TextSegment segment2 = TextSegment.from("The restuarant offers few dishes exclusive that includes spicy Kottu and Fried rices");
        TextSegment segment3 = TextSegment.from("The master chef is known as Subramaniam a.k.a 'Anna'. He is a famous one among all the customers");
        TextSegment segment4 = TextSegment.from("The owner who is widely known as Dissanayake a.k.a 'Sameera'");
        TextSegment segment5 = TextSegment.from("It has a rating of 5 star");
        TextSegment segment6 = TextSegment.from("The important thing you gotta remember is the restaurant is closed on every Poya day");

        embeddingModel = EmbeddingModel.getEmbedding_AllMiniLmL6V2EmbeddingModel();
        embeddingStore = EmbeddingStore.getEmbeddingStore_InMemoryEmbeddingStore();

        Response<Embedding> embed1 = embeddingModel.embed( segment1 );
        Response<Embedding> embed2 = embeddingModel.embed( segment2 );
        Response<Embedding> embed3 = embeddingModel.embed( segment3 );
        Response<Embedding> embed4 = embeddingModel.embed( segment4 );
        Response<Embedding> embed5 = embeddingModel.embed( segment5 );
        Response<Embedding> embed6 = embeddingModel.embed( segment6 );

        embeddingStore.add( embed1.content(), segment1 );
        embeddingStore.add( embed2.content(), segment2 );
        embeddingStore.add( embed3.content(), segment3 );
        embeddingStore.add( embed4.content(), segment4 );
        embeddingStore.add( embed5.content(), segment5 );
        embeddingStore.add( embed6.content(), segment6 );

    }
}
