package com.acc240.solitaire.cards;

/**
 * A deck of cards
 * A collection of cards, such as the deck or any stack of cards in the game
 *
 * @author Aaron Councilman
 * @version 1.01
 */
public class Deck {
    private Card[] cards;
    private int count;

    public Deck() {
        cards = new Card[5];
        count = 0;
    }

    public Deck(Card[] cards) {
        this.cards = cards;
        count = cards.length;
    }

    public static Deck load(String line) {
        if (line != null && line.length() > 0) {
            String[] parts = line.split(",");
            Card[] cards = new Card[parts.length];

            for (int i = 0; i < parts.length; i++) {
                cards[i] = Card.load(parts[i]);
            }

            return new Deck(cards);
        } else
            return new Deck();
    }

    public void add(Card c) {
        if (count >= cards.length) {
            Card[] temp = new Card[cards.length * 2];
            System.arraycopy(cards, 0, temp, 0, cards.length);
            cards = temp;
        }
        cards[count] = c;
        count++;
    }

    public void add(Deck deck) {
        Card[] newCards = deck.getCards();
        for (Card card : newCards)
            add(card);
    }

    public int getCount() {
        return count;
    }

    public Card getTop() {
        if (count > 0)
            return cards[count - 1];
        return null;
    }

    public Card get(int i) {
        if (i >= 0 && i < count)
            return cards[i];
        return null;
    }

    public Card[] getCards() {
        Card[] result = new Card[count];
        System.arraycopy(cards, 0, result, 0, count);
        return result;
    }

    public Deck removeTop(int number) {
        Card[] newCards = new Card[number];
        System.arraycopy(cards, count - number, newCards, 0, number);
        count -= number;
        return new Deck(newCards);
    }

    public void faceDown() {
        for (int i = 0; i < count; i++)
            cards[i].setFaceUp(false);
    }

    public void faceUp() {
        for (int i = 0; i < count; i++)
            cards[i].setFaceUp(true);
    }

    public void reverse() {
        for (int i = 0; i < count / 2; i++) {
            Card t = cards[i];
            cards[i] = cards[count - 1 - i];
            cards[count - 1 - i] = t;
        }
    }

    public void flipTop() {
        cards[count - 1].setFaceUp(true);
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < count; i++) {
            res.append(cards[i]);
            if (i + 1 < count) res.append(",");
        }
        return res.toString();
    }
}
