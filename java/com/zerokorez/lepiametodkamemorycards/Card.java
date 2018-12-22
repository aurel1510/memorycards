package com.zerokorez.lepiametodkamemorycards;

public class Card {
    private Group group;

    private String index;
    private String value;

    private String text;
    private String note;

    private boolean known = false;

    public Card(Group group) {
        this.group = group;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void toggleKnown() { known = !known; }

    public String getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public String getNote() {
        return note;
    }

    public boolean getKnown() {
        return known;
    }
}
