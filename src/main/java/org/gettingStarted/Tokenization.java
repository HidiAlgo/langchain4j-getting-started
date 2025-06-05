package org.gettingStarted;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import dev.langchain4j.model.openai.OpenAiTokenUsage;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import org.gettingStarted.models.Models;

import java.util.List;

import static dev.langchain4j.model.openai.OpenAiChatModelName.*;

public class Tokenization {
    public static void main(String[] args) {
        openAiTokenization();
    }

    private static void openAiTokenization() {
        // it is not always these two models provide the same token counts
        OpenAiTokenCountEstimator tokenizerGptMini = new OpenAiTokenCountEstimator( GPT_4_O_MINI );
        OpenAiTokenCountEstimator tokenizerGpt4 = new OpenAiTokenCountEstimator( GPT_4_O );

        String prompt = "Who is the president of Sri Lanka?";

        Integer numberOfTokensGptMini = tokenizerGptMini.estimateTokenCountInText( prompt );
        Integer numberOfTokensGpt4 = tokenizerGpt4.estimateTokenCountInText( prompt );

        System.out.println( "GPT_4_0_MINI: " + numberOfTokensGptMini );
        System.out.println( "GPT_4_0: " + numberOfTokensGpt4 );

        List<Integer> tokensGptMini = tokenizerGptMini.encode( prompt );
        List<Integer> tokensGpt4 = tokenizerGpt4.encode( prompt );

        System.out.println( "---------------------" );
        System.out.println( "GPT_4_0_MINI" );
        tokensGptMini.forEach(token -> System.out.print(token + " "));

        System.out.println( "\nGPT_4_0" );
        tokensGpt4.forEach(token -> System.out.print(token + " "));

        System.out.println( "\n---------------------" );
        ChatResponse response = Models.getOpenAiChatModelGPT_4_0_MINI().chat( new UserMessage( "Where is Sri Lanka located in?") );

        TokenUsage tokenUsage = response.tokenUsage();
        System.out.println("Token usage for response for the \ninput: Where is Sri Lanka located in? \noutput: " + response.aiMessage().text() );
        System.out.println( tokenUsage.inputTokenCount() );
        System.out.println( tokenUsage.outputTokenCount() );
        System.out.println( tokenUsage.totalTokenCount() );

    }
}
