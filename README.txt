Run the JAR file with the following command:
java -jar <path-to-jar> <path-to-configuration-file>

Example configuration file:
```
###Hyperparameters###
SOURCE_LANGUAGE	ENGLISH
TARGET_LANGUAGE	GERMAN

COMPLETIONS_URL	https://api.openai.com/v1/chat/completions
EMBEDDINGS_URL	https://api.openai.com/v1/embeddings
MODELS_URL	https://api.openai.com/v1/models
TTS_URL	https://api.openai.com/v1/audio/speech
ORG_KEY	org-gZ7peQP5XmIRVhs78U8WH
API_KEY	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
MODEL	gpt-4o-mini
TTS_MODEL	tts-1
TTS_VOICE	alloy

TRANSLATION_BATCH_SIZE	40

###Output###
#Target for output
LESSON_TARGET	lesson.xml
#Order of comic elements for output
#Valid arguments are: "conjugation", "left", "whole", and "story"
#Arguments are space separated
LESSON_SCHEDULE	conjugation left whole story

###External Resources###
TRANSLATIONS_FOLDER	Translations
AUDIO_FOLDER	Audio
AUDIO_INDEX	audioIndex.txt
```

Dependencies:
Gson
 - Google library for serialising and deserialising between JSON and Java objects.
JDOM2
 - Used for constructing the XML file and formatting it.

Note: Using Maven Shade Plugin to package dependencies in an uber-jar

The main functionality of the program is in these classes:
Sprint 1:
-ConfigurationFile: Reads the configuration file and stores its contents in an internal map.
-CompletionSession: Manages talking to the API and maintaining context.
-CompletionResponse: Gson serialises JSON response from completion API onto this class and provides methods for conveniently accessing fields.
-MessageParser: Parses content of responses into formats more usable in our program. Currently only parses and serialises lists.
-ResponseValidator: Detects "soft" denials of service via regex.
CompletionSession and CompletionResponse packaged inside Completion package.

Sprint 2:
-AssetMapFile: Reads pose pairings with backgrounds.tsv and deserialises rows into List<Vignette> vignettes
-Utils: Package
  -StringUtil: Class for string utilities, currently only capitalise method
-Dictionary: Class for managing translation folder and translation files
-Translator: Manages translating lists of vignettes into source and target languages. Interacts with CompletionSession and Dictionary.
-MessageParser: extended to ensure only one translation is returned.

Sprint 3:
-XMLGenerator: Generates an xml file formatted to work with the renderer.
-VignetteManager: Reads pose pairings with backgrounds.tsv and deserialises rows into List<VignetteSchema> static object.
-VignetteSchema: Corresponds to a row in the asset file. Has method for returning random permutation of fields, i.e. a vignette.
-Vignette: Some permutation of a a vignette schema.

-Changes to Translator: Now ensures redundant calls to the API are avoided. Also segments longer requests into batches.

Sprint 4:
-XMLParser deserialises XML comic into in memory comic object.
-ComicPostProcessor takes the comic object and duplicates panels with the second character saying the translations.
-Now have classes representing the tags for in memory storage of comics.

Sprint 5:
-Created StoryManager class to handle this sprint's requirements.
  -Takes 10 random scenes from the XML specification file specified in the configuration file.
  -Generates Audiovisual descriptions for each mini-story, and calls the LLM to generate dialogue for the stories.
  -Translates the dialogue and creates a new XML, with the characters speaking the new dialogue followed by a duplicate slide with the translations in place of the previous dialogue.
  -Writes this output to a file specified in the configuration file.

Sprint 6:
-Created TTSSession, textToSpeech method takes phrase and returns a HTTPResponse<InputStream> of the .mp3 file.
-Created AudioManager for calling textToSpeech and interacting with files related to audio, i.e. storing mp3's and maintaining index file.
-Massive refactoring of code to remove god-classes and feature-envy, poor code purpose and the likes.