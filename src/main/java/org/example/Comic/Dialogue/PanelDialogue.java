package org.example.Comic.Dialogue;

import java.util.ArrayList;
import java.util.List;

public class PanelDialogue {
    private List<CharacterDialogue> characterDialogues = new ArrayList<>();

    public List<CharacterDialogue> getCharacterDialogues() {
        return characterDialogues;
    }
    public void setCharacterDialogues(List<CharacterDialogue> characterDialogues) {
        this.characterDialogues = characterDialogues;
    }
    public void addCharacterDialogue(CharacterDialogue characterDialogue) {
        characterDialogues.add(characterDialogue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Panel Dialogue:\n");
        for (CharacterDialogue characterDialogue : characterDialogues) {
            sb.append("  " + characterDialogue + "\n");
        }
        return sb.toString();
    }
}