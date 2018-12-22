package com.zerokorez.lepiametodkamemorycards;

import java.util.ArrayList;

public class Group {
    private String name;
    private ArrayList<Card> cards;

    public Group(String name) {
        this.name = name;
        cards = new ArrayList<>();
        Constants.LOAD_MANAGER.getLoadedGroups().add(this);
    }

    public Group(ArrayList<Card> cards) {
        this.name = "N/A";
        this.cards = cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
