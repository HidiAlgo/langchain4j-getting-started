package org.gettingStarted;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.Response;

import java.util.List;

import static dev.langchain4j.model.huggingface.HuggingFaceModelName.SENTENCE_TRANSFORMERS_ALL_MINI_LM_L6_V2;
import static dev.langchain4j.model.huggingface.HuggingFaceModelName.TII_UAE_FALCON_7B_INSTRUCT;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import static java.lang.System.exit;

public class MusicianAssistant
{
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String HUGGING_FACE_API_KEY = System.getenv( "HUGGING_FACE_API_KEY" );

    // please uncomment necessary parts as needed
    public static void main(String[] args)
    {
        String name = args[0];

        // I have given the priority to open ai, thats why that implementation is directly written on main method
        // however, there are hugging face and ollama implementations are defined in seperate methods

        // Chat Language Model ( good for conversation like text based generations )
//        ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()
//                .baseUrl("http://langchain4j.dev/demo/openai/v1") // using a free version of the openai just for learning purpose
//                .apiKey( OPENAI_API_KEY )
//                .modelName( GPT_4_O_MINI )
//                .temperature( 0.7 )
//                .timeout( Duration.ofSeconds(60) )
//                .logRequests( true )
//                .logResponses( true )
//                .build();

//        Musician musician = new MusicianAssistant().generateTopThreeAlbums(chatLanguageModel, name);
//        System.out.println(musician);

        // Language Model ( good for text based content generation )
        // currently this isn't working due to api key is not purchesed.
//        LanguageModel languageModel = OpenAiLanguageModel.builder().baseUrl( "http://langchain4j.dev/demo/openai/v1" ).apiKey( OPENAI_API_KEY ).build();
//
//        Response<String> response = languageModel.generate( "Who is the president of Sri Lanka?" );
//
//        System.out.println( response.content() );

//        ollamaChatModel();
        huggingFaceChatModal();

        exit( 0 );
    }

    public Musician generateTopThreeAlbums(ChatModel model, String name)
    {
        // system messages defines the context for your chat
        SystemMessage systemMessage = SystemMessage.from("""
                You are an expert in Jazz music.
                Reply with only the names of the artists, albums, etc.
                Be very concise.
                If a list is given, separate the items with commas.
                """
        );

        UserMessage message = UserMessage.from(
                String.format("Only list the top 3 albums of %s", name)
        );
        List<ChatMessage> messages = List.of( systemMessage, message );
        ChatResponse aiMessageResponse = model.chat( messages );
        String topThreeAlbums = aiMessageResponse.aiMessage().text();

        return new Musician( topThreeAlbums, name );
    }

    // shows how to access ollama
    public static void ollamaChatModel()
    {
        OllamaChatModel ollamaChatModel = OllamaChatModel.builder()
                .baseUrl("http://localhost:56770")
                .modelName("tinyllama")
                .build();
        System.out.println( ollamaChatModel.chat("Who sings bad boy?") );
    }

    // hugging face implementation
    // since we're using remote deployed model, it takes so long to return results this might throw timeout error
    public static void huggingFaceChatModal()
    {
        HuggingFaceChatModel huggingFaceChatModel = HuggingFaceChatModel.builder()
                .accessToken( HUGGING_FACE_API_KEY ).modelId( "mistralai/Mistral-7B-Instruct-v0.3" ).build();

        System.out.println( huggingFaceChatModel.chat( "Hello how are you? ") );
    }
}
