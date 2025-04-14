package org.example.Completion;

import org.example.ConfigurationFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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
    private static final String TTS_ENDPOINT = ConfigurationFile.getValue("TTS_ENDPOINT_URL");
    private static final String MODEL = ConfigurationFile.getValue("TTS_MODEL");
    private static final String VOICE = ConfigurationFile.getValue("TTS_VOICE");
    private static final String RESPONSE_FORMAT = "mp3";
    private static final String AUDIO_FOLDER = ConfigurationFile.getValue("AUDIO_FOLDER");

    public static void textToSpeech(String fileName, String text) throws IOException, InterruptedException {
        Path audioFolderPath = Paths.get(AUDIO_FOLDER);
        Path outputFile = audioFolderPath.resolve(fileName + "." + RESPONSE_FORMAT);

        try {
            if (!Files.exists(audioFolderPath)) {
                Files.createDirectories(audioFolderPath);
                System.out.println("Audio folder created: " + audioFolderPath.toAbsolutePath());
            } else if (!Files.isDirectory(audioFolderPath)) {
                throw new IOException("The specified audio folder path exists but is not a directory: " + audioFolderPath.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new IOException("Failed to create or access audio folder: " + audioFolderPath.toAbsolutePath(), e);
        }

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
        jsonPayload.addProperty("response_format", RESPONSE_FORMAT);

        String jsonPayloadString = jsonPayload.toString();

        // Sending HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TTS_ENDPOINT))
                .timeout(Duration.ofSeconds(40))
                .header("Content-Type", "application/json")
                .header("OpenAI-Organisation", ORG_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayloadString))
                .build();

        System.out.println("Sending request to " + TTS_ENDPOINT + " with payload: " + jsonPayloadString);

        // Handling the response
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        if (response.statusCode() == 200) {
            System.out.println("API call successful (Status Code: 200). Receiving audio stream...");
            try (InputStream audioStream = response.body()) {
                Files.copy(audioStream, outputFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Audio saved successfully to: " + outputFile.toAbsolutePath());
            } catch (IOException e) {
                throw new IOException("Failed to save audio file to " + outputFile.toAbsolutePath(), e);
            }
        } else {
            System.err.println("API call failed!");
            System.err.println("Status Code: " + response.statusCode());
            String errorBody = "Could not read error body.";
            try (InputStream errorStream = response.body()) {
                errorBody = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.err.println("Failed to read error response body: " + e.getMessage());
            }
            System.err.println("Error Body: " + errorBody);
            throw new RuntimeException("OpenAI TTS API request failed with status code: " + response.statusCode() + " - Body: " + errorBody);
        }
    }
}
