package org.gettingStarted;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import org.gettingStarted.models.Models;

import java.util.Collections;
import java.util.Map;

public class MessageTemplates
{
    public static void main(String[] args) {
        PromptTemplate promptTemplate = PromptTemplate.from( "When was {{country}} freed from British?" ); // you can add as much as variables with curly braces
        Map<String, Object> variables = Collections.singletonMap( "country", "Sri Lanka");

        Prompt prompt = promptTemplate.apply( variables );

        ChatModel model = Models.getOpenAiChatModelGPT_4_0_MINI();

        System.out.println( model.chat(prompt.toUserMessage()).aiMessage() );

//        LangChain4j also supports special variables that are automatically filled with the current date and
//        time: {{current_date}}, {{current_time}}, and {{current_date_time}} are automatically filled with
//        LocalDate.now(), LocalTime.now(), and LocalDateTime.now() respectively.
    }
}
