package org.gettingStarted;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.redis.RedisChatMemoryStore;
import org.gettingStarted.models.Models;
import org.gettingStarted.tools.LegalDocumentsTool;

import java.util.*;

public class Conversations {
    public static void main(String[] args) {
//        startBasicConvoWithChatMemory();
//        startConvoWithRedisChatStore();
//        startBasicConvoWithLangChains();
        startConvoWithTools();
    }

    private static void startBasicConvoWithChatMemory() {
        System.out.println("Greetings!!!!");
        System.out.println("Please start your conversation");

        ChatModel model = Models.getOpenAiChatModelGPT_4_0_MINI();
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);

        Scanner scanner = new Scanner(System.in);

        int count = 0;

        while (count < 20) {
            System.out.println("question: ");
            String question = scanner.nextLine();
            chatMemory.add(new UserMessage(question));

            AiMessage answer = model.chat(chatMemory.messages()).aiMessage();
            chatMemory.add(answer);
            System.out.println(answer.text());
            System.out.println("\n");
            count++;
        }

        System.out.println(chatMemory.messages());
    }

    private static void startConvoWithRedisChatStore() {
        ChatMemoryStore chatMemoryStore = RedisChatMemoryStore.builder()
                .host("localhost")
                .port(6792)
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .chatMemoryStore(chatMemoryStore)
                .build();

        ChatModel model = Models.getOpenAiChatModelGPT_4_0_MINI();

        chatMemory.add(new UserMessage("What is my name?"));
        AiMessage answer = model.chat(chatMemory.messages()).aiMessage();
        System.out.println(answer);
    }

    private static void startBasicConvoWithLangChains()
    {
        ChatMemory memory = MessageWindowChatMemory.withMaxMessages( 20 );

        ChatModel model = Models.getOpenAiChatModelGPT_4_0_MINI();

        ConversationalChain chain = ConversationalChain.builder()
                .chatModel( model )
                .chatMemory( memory )
                .build();

        System.out.println( chain.execute("Hello my name is Hashan") );
        System.out.println( chain.execute("Do you know my name?") );

    }

    private static void startConvoWithTools()
    {
        LegalDocumentsTool legalDocumentsTool = new LegalDocumentsTool();

        List<ChatMessage> chatMessages = new ArrayList<>();
        UserMessage userMessage = UserMessage.from( "When was the privacy document last updated on?" );
        chatMessages.add( userMessage );

        ChatModel model = Models.getOpenAiChatModelGPT_4_0_MINI__withTools();

        AiMessage toolsResult = model.chat( userMessage ).aiMessage();
        chatMessages.add( toolsResult );

        List<ToolExecutionRequest> toolsRequest = toolsResult.toolExecutionRequests();

        toolsRequest.forEach( req -> {
            ToolExecutor executor = new DefaultToolExecutor( legalDocumentsTool, req ); // find the relevant method
            String res = executor.execute( req, UUID.randomUUID().toString() ); // call the method and assign it with a unique identifier (UUID)
            ToolExecutionResultMessage resultMessage = ToolExecutionResultMessage.from( req, res );
            chatMessages.add( resultMessage ); // add the req and result mapping to original list
        });

        ChatResponse finalResult = model.chat( chatMessages );

        System.out.println( finalResult.aiMessage() );
    }
}
