package org.example;

import org.example.Assets.AssetMapFile;
import org.example.Assets.Translator;
import org.example.Assets.Vignette;
import org.example.Completion.CompletionSession;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("Usage: java -jar <pathToJar> <pathToConfigFile> <pathToPosePairingWithBackgrounds.tsv> <pathToTranslations.txt>");
            return;
        }
        String configFilePath = args[0];
        ConfigurationFile.initialize(configFilePath);
        String assetMappingPath = args[1];
        AssetMapFile.initialize(assetMappingPath);
        String translationFilePath = args[2];
        for(Vignette v: AssetMapFile.getVignettesInRange(0, 20)) {
            System.out.println(v);
        }
        try {
            Translator.translateVignetteList(AssetMapFile.getVignettesInRange(0, 10), translationFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Translator.translateVignetteList(AssetMapFile.getVignettesInRange(0, 5), translationFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Translator.translateVignetteList(AssetMapFile.getVignettesInRange(0, 20), translationFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }


        CompletionSession session = new CompletionSession();
        String m1 = "Hi my name is Daniel";
        System.out.println(m1);
        String response = (session.sendMessage("user", m1));
        if (response.startsWith("[ERROR]")) {
            System.out.println("\n" + response);
        } else {
            System.out.println(response);
        }


        String m2 = "What are you up to?";
        System.out.println(m2);
        String response2 = (session.sendMessage("user", m2));
        if (response2.startsWith("[ERROR]")) {
            System.out.println("\n" + response2);
        } else {
            System.out.println(response2);
        }

        String m3 = "Give me a numbered list of 20 words please.";
        System.out.println(m3);
        String response3 = (session.sendMessage("user", m3));
        if (response3.startsWith("[ERROR]")) {
            System.out.println("\n" + response3);
        } else {
            System.out.println(response3);
        }
    }
}