package org.gettingStarted;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

import java.time.Duration;
import java.util.List;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import static java.lang.System.exit;

public class MusicianAssistant
{
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");

    public static void main(String[] args)
    {
        String name = args[0];

        ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1") // using a free version of the openai just for learning purpose
                .apiKey( OPENAI_API_KEY )
                .modelName( GPT_4_O_MINI )
                .temperature( 0.7 )
                .timeout( Duration.ofSeconds(60) )
                .logRequests( true )
                .logResponses( true )
                .build();

        Musician musician = new MusicianAssistant().generateTopThreeAlbums(chatLanguageModel, name);
        System.out.println(musician);
        exit(0);
    }

    public Musician generateTopThreeAlbums(ChatLanguageModel model, String name)
    {
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
        Response<AiMessage> aiMessageResponse = model.generate( messages );
        String topThreeAlbums = aiMessageResponse.content().text();

        return new Musician( topThreeAlbums, name );
    }
}
