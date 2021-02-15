package com.tyagiabhinav.dialogflowchatlibrary.util;

import android.util.Log;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.dialogflow.v2beta1.DetectIntentRequest;

import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
import com.google.cloud.dialogflow.v2beta1.KnowledgeAnswers;
import com.google.cloud.dialogflow.v2beta1.KnowledgeAnswers.Answer;
import com.google.cloud.dialogflow.v2beta1.QueryInput;
import com.google.cloud.dialogflow.v2beta1.QueryParameters;
import com.google.cloud.dialogflow.v2beta1.QueryResult;
import com.google.cloud.dialogflow.v2beta1.SessionName;
import com.google.cloud.dialogflow.v2beta1.SessionsClient;
import com.google.cloud.dialogflow.v2beta1.TextInput;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.opencensus.tags.Tag;

public class DetectIntentKnowledge {


    public static Map<String, KnowledgeAnswers> detectIntentKnowledge(
            String projectId,
            String knowledgeBaseName,
            String sessionId,
            String languageCode,
            List<String> texts)
            throws IOException, ApiException {
        // Instantiates a client
        Map<String, KnowledgeAnswers> allKnowledgeAnswers = Maps.newHashMap();
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            // Set the session name using the sessionId (UUID) and projectID (my-project-id)
            SessionName session = SessionName.of(projectId, sessionId);
            System.out.println("Session Path: " + session.toString());

            // Detect intents for each text input
            for (String text : texts) {
                // Set the text and language code (en-US) for the query
                TextInput.Builder textInput =
                        TextInput.newBuilder().setText(text).setLanguageCode(languageCode);
                // Build the query with the TextInput
                QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

                QueryParameters queryParameters =
                        QueryParameters.newBuilder().addKnowledgeBaseNames(knowledgeBaseName).build();

                DetectIntentRequest detectIntentRequest =
                        DetectIntentRequest.newBuilder()
                                .setSession(session.toString())
                                .setQueryInput(queryInput)
                                .setQueryParams(queryParameters)
                                .build();
                // Performs the detect intent request
                DetectIntentResponse response = sessionsClient.detectIntent(detectIntentRequest);

                // Display the query result
                QueryResult queryResult = response.getQueryResult();
                String intentName = queryResult.getIntent().getDisplayName();

                KnowledgeAnswers knowledgeAnswers = queryResult.getKnowledgeAnswers();
                for (KnowledgeAnswers.Answer answer : knowledgeAnswers.getAnswersList()) {
                    System.out.format(" - Answer: '%s'\n", answer.getAnswer());
                    System.out.format(" - Confidence: '%s'\n", answer.getMatchConfidence());
                    Log.d("Nazar", " knowledge answers is "+ answer.getAnswer());
                }

                KnowledgeAnswers answers = queryResult.getKnowledgeAnswers();
                allKnowledgeAnswers.put(text, answers);
            }
        }
        return allKnowledgeAnswers;
    }
}
