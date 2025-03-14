package org.example;

public class Main {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar <pathToJar> <pathToConfigFile>");
            return;
        }
        String configFilePath = args[0];
        ConfigurationFile.initialise(configFilePath);

        LLMCompletionSession session = new LLMCompletionSession();
        String m1 = "Hi my name is Daniel";
        System.out.println(m1);
        System.out.println(session.sendMessage("user", m1));
        String m2 = "What are you up to?";
        System.out.println(m2);
        System.out.println(session.sendMessage("user", m2));
    }
}