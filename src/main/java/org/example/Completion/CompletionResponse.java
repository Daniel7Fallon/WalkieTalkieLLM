package org.example.Completion;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompletionResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("choices")
    private List<Choice> choices;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<Choice> getChoices() {
        return choices;
    }
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return "CompletionResponse [id=" + id + ", choices=" + choices + "]";
    }

    public String getFirstMessageContent() {
         return this.getChoices().getFirst().getMessage().getContent();
    }

    public static class Choice {
        @SerializedName("index")
        private int index;
        @SerializedName("message")
        private Message message;
        @SerializedName("finish_reason")
        private String finishReason;

        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
        public Message getMessage() {
            return message;
        }
        public void setMessage(Message message) {
            this.message = message;
        }
        public String getFinishReason() {
            return finishReason;
        }
        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }

        @Override
        public String toString() {
            return "Choice [index=" + index + ", message=" + message + ", finishReason=" + finishReason + "]";
        }
    }

    public static class Message {
        @SerializedName("role")
        private String role;
        @SerializedName("content")
        private String content;
        @SerializedName("refusal")
        private String refusal;

        public String getRole() {
            return role;
        }
        public void setRole(String role) {
            this.role = role;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
        public String getRefusal() {
            return refusal;
        }
        public void setRefusal(String refusal) {
            this.refusal = refusal;
        }

        @Override
        public String toString() {
            return "Message [role=" + role + ", content=" + content + ", refusal=" + refusal + "]";
        }
    }
}
