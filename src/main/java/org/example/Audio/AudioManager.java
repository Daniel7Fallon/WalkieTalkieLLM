package org.example.Audio;

import org.example.Comic.Comic;
import org.example.Comic.Panel;
import org.example.Comic.Scene;
import org.example.Utils.ConfigurationFile;

import java.io.*;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class AudioManager {

    private static final String AUDIO_FOLDER = ConfigurationFile.getValue("AUDIO_FOLDER");
    private static final String AUDIO_INDEX_FILE_PATH = ConfigurationFile.getValue("AUDIO_INDEX");
    private static final String RESPONSE_FORMAT = "mp3";

    /**
     * Creates a new audio file for the given phrase using TTS if it doesn't already exist.
     * Ensures the audio folder and index file exist, calls the TTS service,
     * saves the audio, and updates the index file.
     *
     * @param phrase The text phrase to convert to speech and save.
     * @throws IOException if file operations fail.
     * @throws InterruptedException if the TTS HTTP request is interrupted.
     * @throws RuntimeException if the TTS API call fails (any status code other than 200).
     */
    public static void createNewAudio(String phrase) throws IOException, InterruptedException {
        //Ensure Audio folder exists
        Path audioFolderPath = Paths.get(AUDIO_FOLDER);
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
        //Ensure Audio Index file exists
        File audioIndexFile = new File(AUDIO_INDEX_FILE_PATH);
        try {
            if(audioIndexFile.createNewFile()) {
                System.out.println("Audio Index file created: " + audioIndexFile);
            }
        } catch (IOException e) {
            throw new IOException("Error creating Audio Index file: " + e.getMessage());
        }

        if(phraseMappingExists(phrase)) return;

        int newIndex = getLastIndex() + 1;
        Path outputFile = audioFolderPath.resolve(newIndex + "." + RESPONSE_FORMAT);

        HttpResponse<InputStream> response = TTSSession.textToSpeech(phrase);

        if (response.statusCode() == 200) {
            try (InputStream audioStream = response.body()) {
                Files.copy(audioStream, outputFile, StandardCopyOption.REPLACE_EXISTING);
                AudioManager.addToAudioIndex(phrase, "" + newIndex);
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

    /**
     * Appends a mapping between a text phrase and its audio file index to the index file.
     * Format: phrase<tab>index<newline>. Sanitises the phrase to remove tabs and/or newlines.
     *
     * @param text The original text phrase.
     * @param index The numerical index (as a String) corresponding to the audio file name.
     */
    private static void addToAudioIndex(String text, String index) {
        Path indexFilePath = Paths.get(AUDIO_INDEX_FILE_PATH);

        // Formatting the line to be appended: text<tab>absolute_path<newline>
        // Replacing tabs and newlines in the original text to avoid breaking the index format
        String sanitizedText = text.replace("\t", " ").replace("\n", " ").replace("\r", " ");
        String lineToAppend = sanitizedText + "\t" + index + System.lineSeparator();

        try {
            Files.writeString(indexFilePath,
                    lineToAppend,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);

        } catch (IOException e) {
            System.err.println("Error: Failed to write to audio index file '" + indexFilePath.toAbsolutePath() + "': " + e.getMessage());
        }
    }

    /**
     * Reads the audio index file and returns the last numerical index found.
     * Assumes the index is the second tab-separated value on each line.
     *
     * @return The last index found in the file, or -1 if the file is empty or indices cannot be parsed.
     * @throws IOException if reading the index file fails.
     */
    private static int getLastIndex() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(AUDIO_INDEX_FILE_PATH));
        String last = null;
        String line;
        while ((line = br.readLine()) != null) {
            last = line;
        }
        if(last != null) {
            String[] tokens = last.split("\t");
            return Integer.parseInt(tokens[1]);
        }
        return -1;
    }

    /**
     * Searches the audio index file for a given phrase and returns its corresponding index.
     *
     * @param phrase The text phrase to search for in the index.
     * @return The numerical index associated with the phrase, or -1 if the phrase is not found.
     * @throws IOException if reading the index file fails.
     */
    public static int getIndexByPhrase(String phrase) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(AUDIO_INDEX_FILE_PATH));
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split("\t");
            if(tokens[0].equals(phrase)) return Integer.parseInt(tokens[1]);
        }
        return -1;
    }

    /**
     * Checks if a mapping for the given phrase already exists in the audio index file.
     *
     * @param phrase The text phrase to check for existence.
     * @return true if a line starting with the phrase exists in the index, false otherwise.
     * @throws IOException if reading the index file fails.
     */
    private static boolean phraseMappingExists(String phrase) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(AUDIO_INDEX_FILE_PATH));
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split("\t");
            if(tokens[0].equals(phrase)) return true;
        }
        return false;
    }

}
