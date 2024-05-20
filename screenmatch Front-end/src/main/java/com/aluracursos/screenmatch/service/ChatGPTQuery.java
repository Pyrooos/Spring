

package com.aluracursos.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ChatGPTQuery {
    public static String getTranslate(String text) {
        OpenAiService service = new OpenAiService("TU-API-KEY");

        CompletionRequest requisition = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("translate to Spanish the following text: " + text)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var response = service.createCompletion(requisition);
        return response.getChoices().get(0).getText();
    }
}


