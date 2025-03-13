package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Main {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar <pathToJar> <pathToConfigFile>");
            return;
        }
        String configFilePath = args[0];
        ConfigurationFile.initialise(configFilePath);

        JsonObject message1 = new JsonObject();
        message1.addProperty("role", "developer");
        message1.addProperty("content", "You are a helpful assistant");
        JsonObject message2 = new JsonObject();
        message2.addProperty("role", "user");
        message2.addProperty("content", "Hello!");
        JsonArray messages = new JsonArray();
        messages.add(message1);
        messages.add(message2);

        JsonObject jsonInput = new JsonObject();
        jsonInput.addProperty("model", ConfigurationFile.getValue("MODEL"));
        jsonInput.add("messages", messages);
        Gson gson = new Gson();
        String jsonInputString = gson.toJson(jsonInput);

        LLMCompletionSession session = new LLMCompletionSession();
        String response = session.sendMessage(jsonInputString);
        System.out.println(response);
    }
}