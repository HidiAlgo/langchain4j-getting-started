package org.gettingStarted.models;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.time.Duration;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

// this class provides different models with related api keys
public class Models
{
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String HUGGING_FACE_API_KEY = System.getenv( "HUGGING_FACE_API_KEY" );

    // returns GPT_4_0_MINI
    public static ChatModel getOpenAiChatModelGPT_4_0_MINI()
    {
        // Chat Language Model ( good for conversation like text based generations )
        ChatModel chatLanguageModel = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1") // using a free version of the openai just for learning purpose
                .apiKey( OPENAI_API_KEY ) // need to give the api key to access the models
                .modelName( GPT_4_O_MINI )  // open ai model name
                .temperature( 0.7 ) // this defines how random the response is, 2 is maximum 0 is low
                .timeout( Duration.ofSeconds(60) ) // time it waits until a response come
                .logRequests( false ) // any logs defined in requests
                .logResponses( false ) // any logs defined in response
                .build();

        return chatLanguageModel;
    }

    public static ChatModel getOllamaChatModelTINY_OLLAMA()
    {
        ChatModel chatLanguageModel = OllamaChatModel.builder()
                .baseUrl( "http://localhost:53782" )
                .modelName("tinyllama")
                .build();
        return chatLanguageModel;
    }

    public static ChatModel getHuggingFaceChatModel__dynamic_model( String modelId )
    {
        ChatModel chatLanguageModel = HuggingFaceChatModel.builder()
                .accessToken( HUGGING_FACE_API_KEY )
                .modelId( modelId )
                .build();

        return chatLanguageModel;
    }

}
