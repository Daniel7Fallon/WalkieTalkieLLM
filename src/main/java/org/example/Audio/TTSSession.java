package org.example.Audio;

import org.example.Utils.ConfigurationFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.google.gson.JsonObject;

public class TTSSession {
    private static final String ORG_KEY = ConfigurationFile.getValue("ORG_KEY");
    private static final String API_KEY = ConfigurationFile.getValue("API_KEY");
    private static final String TTS_ENDPOINT = ConfigurationFile.getValue("TTS_URL");
    private static final String MODEL = ConfigurationFile.getValue("TTS_MODEL");
    private static final String VOICE = ConfigurationFile.getValue("TTS_VOICE");
    private static final String RESPONSE_FORMAT = "mp3";

    /**
     * Sends the given text to the configured TTS API and returns the audio stream response.
     * Constructs and sends an HTTP POST request with necessary headers and JSON payload.
     *
     * @param text The text to be converted into speech.
     * @return An HttpResponse containing the audio data as an InputStream if successful (status 200).
     * @throws InterruptedException if the HTTP request sending thread is interrupted.
     * @throws IOException if an I/O error occurs during the HTTP request/response process.
     */
    public static HttpResponse<InputStream> textToSpeech(String text) throws InterruptedException, IOException {
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

        // Handling the response
        return client.send(request, HttpResponse.BodyHandlers.ofInputStream());
    }


}
