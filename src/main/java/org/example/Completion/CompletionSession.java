package org.example.Completion;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.Utils.ConfigurationFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CompletionSession {
    String completionsEndPoint = ConfigurationFile.getValue("COMPLETIONS_URL");
    String orgKey = ConfigurationFile.getValue("ORG_KEY");
    String apiKey = ConfigurationFile.getValue("API_KEY");
    private final List<CompletionMessage> messageList = new ArrayList<>();
    //Inner class represents message
    public static class CompletionMessage {
        private final String role;
        private final String content;
        public CompletionMessage(String role, String message) {
            this.role = role;
            this.content = message;
        }
        public String getRole() {
            return role;
        }
        public String getContent() {
            return content;
        }
    }

    public String sendMessage(String role, String content) {
        messageList.add(new CompletionMessage(role, content));
        JsonArray jsonMessageList = new JsonArray();
        for(CompletionMessage m : messageList) {
            JsonObject jsonMessage = new JsonObject();
            jsonMessage.addProperty("role", m.getRole());
            jsonMessage.addProperty("content", m.getContent());
            jsonMessageList.add(jsonMessage);
        }
        JsonObject jsonInput = new JsonObject();
        jsonInput.addProperty("model", ConfigurationFile.getValue("MODEL"));
        jsonInput.add("messages", jsonMessageList);
        Gson gson = new Gson();
        String jsonInputString = gson.toJson(jsonInput);


        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(completionsEndPoint))
                .header("Content-Type", "application/json")
                .header("OpenAI-Organisation", orgKey)
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println("Response Code: " + response.statusCode());
            //System.out.println("Response Body: " + response.body());
            CompletionResponse completionResponse = gson.fromJson(response.body(), CompletionResponse.class);
            String responseContent = completionResponse.getFirstMessageContent();

            if (ResponseValidator.isDenial(responseContent)) {
                System.err.println("[DENIAL DETECTED]\nAssistant refused to answer prompt: " + content + "\nResonse: " + responseContent);
                return "[ERROR] Denial: " + responseContent;
            }
            messageList.add(new CompletionMessage("assistant", responseContent));
            return responseContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Message failed";
    }



}
