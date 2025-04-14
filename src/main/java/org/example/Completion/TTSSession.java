package org.example.Completion;

import org.example.ConfigurationFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TTSSession {
    private static final String ORG_KEY = ConfigurationFile.getValue("ORG_KEY");
    private static final String API_KEY = ConfigurationFile.getValue("API_KEY");
    private static final String ENDPOINT = ConfigurationFile.getValue("COMPLETIONS_URL");
    private static final String MODEL = ConfigurationFile.getValue("TTS_MODEL");
    private static final String VOICE = ConfigurationFile.getValue("TTS_VOICE");
    private static final String RESPONSE_FORMAT = "mp3";
    private static final String AUDIO_FOLDER = ConfigurationFile.getValue("AUDIO_FOLDER");

    public static void textToSpeech(String text) throws IOException, InterruptedException {
        // Http client
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        // Creating JSON to send
        JsonObject jsonPayload = new JsonObject();
        jsonPayload.addProperty("model", MODEL);
        jsonPayload.addProperty("input", text);
        jsonPayload.addProperty("voice", VOICE);
        jsonPayload.addProperty("responseFormat", RESPONSE_FORMAT);

        String jsonPayloadString = jsonPayload.toString();

        // Sending HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .timeout(Duration.ofSeconds(40))
                .header("Content-Type", "application/json")
                .header("OpenAI-Organisation", ORG_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayloadString))
                .build();

        System.out.println("Sending request to " + ENDPOINT + " with payload: " + jsonPayloadString);
    }
}
