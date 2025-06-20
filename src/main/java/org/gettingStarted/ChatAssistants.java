package org.gettingStarted;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.gettingStarted.models.Models;

import java.util.List;

import static java.lang.System.exit;

public class ChatAssistants
{
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String HUGGING_FACE_API_KEY = System.getenv( "HUGGING_FACE_API_KEY" );

    // please uncomment necessary parts as needed
    public static void main(String[] args)
    {
        String name = args[0];

//        ChatModel model = Models.getOpenAiChatModelGPT_4_0_MINI();
//        ChatModel model = Models.getHuggingFaceChatModel__dynamic_model( "mistralai/Mistral-7B-Instruct-v0.3" );
        ChatModel model = Models.getOllamaChatModelTINY_OLLAMA();

        Politician musician = new ChatAssistants().getDescription(model, name);
        System.out.println(musician);

        exit( 0 );
    }

    public Politician getDescription(ChatModel model, String name)
    {
        // system messages defines the context for your chat
        SystemMessage systemMessage = SystemMessage.from("""
                You are an expert in Politics of Sri Lanka.
                Reply a short bio of a given politician name
                Be very concise.
                """
        );

        UserMessage message = UserMessage.from(
                String.format("Who is %s", name)
        );
        List<ChatMessage> messages = List.of( systemMessage, message );
        ChatResponse aiMessageResponse = model.chat( messages );
        String description = aiMessageResponse.aiMessage().text();

        return new Politician( name, description );
    }
}
