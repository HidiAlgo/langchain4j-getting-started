package org.gettingStarted;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.redis.RedisChatMemoryStore;
import org.gettingStarted.models.Models;

import java.util.Scanner;

public class Conversations
{
    public static void main(String[] args)
    {
//        startBasicConvoWithChatMemory();
        startConvoWithRedisChatStore();
    }

    private static void startBasicConvoWithChatMemory()
    {
        System.out.println( "Greetings!!!!" );
        System.out.println( "Please start your conversation" );

        ChatModel model = Models.getOpenAiChatModelGPT_4_0_MINI();
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages( 20 );

        Scanner scanner = new Scanner( System.in );

        int count = 0;

        while ( count < 20 )
        {
            System.out.println( "question: " );
            String question = scanner.nextLine();
            chatMemory.add( new UserMessage( question ) );

            AiMessage answer = model.chat( chatMemory.messages() ).aiMessage();
            chatMemory.add( answer );
            System.out.println( answer.text() );
            System.out.println( "\n" );
            count++;
        }

        System.out.println( chatMemory.messages() );
    }

    private static void startConvoWithRedisChatStore()
    {
        ChatMemoryStore chatMemoryStore = RedisChatMemoryStore.builder()
                .host( "localhost" )
                .port( 6792 )
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .chatMemoryStore( chatMemoryStore )
                .build();

        ChatModel model = Models.getOpenAiChatModelGPT_4_0_MINI();

        chatMemory.add(new UserMessage("What is my name?"));
        AiMessage answer = model.chat(chatMemory.messages()).aiMessage();
        System.out.println( answer );
    }
}
