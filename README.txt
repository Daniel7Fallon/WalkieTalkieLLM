Run the JAR file with the following command:
java -jar <path-to-jar> <path-to-configuration-file>

Example configuration file:
```
#Hashtag denotes a comment

SOURCELANGUAGE	ENGLISH
TARGETLANGUAGE	GERMAN

#Below are mandatory for running the application this sprint
COMPLETIONS_URL	https://api.openai.com/v1/chat/completions
EMBEDDINGS_URL	https://api.openai.com/v1/embeddings
MODELS_URL	https://api.openai.com/v1/models

ORG_KEY	org-gZ7peQP5XmIRVhs78U8WH
API_KEY	abc-useyourownkey-123

MODEL	gpt-4o-mini

```
Key-Value pairs are tab-separated.


Dependencies:
Gson
 - Google library for serialising and deserialising between JSON and Java objects.

Note: Using Maven Shade Plugin to package dependencies in an uber-jar




#To Write
#Code structure explanation


