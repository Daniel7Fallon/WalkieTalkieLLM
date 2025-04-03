Run the JAR file with the following command:
java -jar <path-to-jar> <path-to-configuration-file>

Example configuration file:
```
#Hashtag denotes a comment.
#Key-Value pairs are tab-separated.

SOURCELANGUAGE	FRENCH
TARGETLANGUAGE	GERMAN

#Below are mandatory for running the application this sprint
COMPLETIONS_URL	https://api.openai.com/v1/chat/completions
EMBEDDINGS_URL	https://api.openai.com/v1/embeddings
MODELS_URL	https://api.openai.com/v1/models

ORG_KEY	org-gZ7peQP5XmIRVhs78U8WH
API_KEY	abc-useyourownkey-123

MODEL	gpt-4o-mini

TRANSLATIONS_FOLDER	Translations

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
