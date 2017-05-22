package com.passwordboss.android.event;

public class CardTypeSelectedEvent {
    private String cardType;

    public CardTypeSelectedEvent(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }
}
