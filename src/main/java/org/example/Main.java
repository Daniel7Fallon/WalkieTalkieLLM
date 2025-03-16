package org.example;

import org.example.Completion.CompletionSession;

public class Main {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar <pathToJar> <pathToConfigFile>");
            return;
        }
        String configFilePath = args[0];
        ConfigurationFile.initialise(configFilePath);


        CompletionSession session = new CompletionSession();
        String m1 = "Hi my name is Daniel";
        System.out.println(m1);
        String response = (session.sendMessage("user", m1));
        if (response.startsWith("[ERROR] Request denied")) {
            System.out.println("Assistant refused to answer");
        }
        else System.out.println(response);

        String m2 = "What are you up to?";
        System.out.println(m2);
        String response2 = (session.sendMessage("user", m2));
        if (response2.startsWith("[ERROR] Request denied")) {
            System.out.println("Assistant refused to answer");
        }
        else System.out.println(response2);

        String m3 = "Give me a numbered list of 20 words please.";
        System.out.println(m3);
        String response3 = (session.sendMessage("user", m3));
        if (response3.startsWith("[ERROR] Request denied")) {
            System.out.println("Assistant refused to answer");
        }
        else System.out.println(response3);
    }
}