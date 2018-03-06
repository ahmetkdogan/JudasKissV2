package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;


public class CardPile implements Iterable<Card>{
    
    private List<Card> cards = new ArrayList<>();
    private Type type;
    private String id;

    public CardPile(Type type,String id){
        this.type = type;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    
    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    
    public void addCard(Card card){
        cards.add(card);
    }
    
    public boolean isEmpty(){
        return cards.isEmpty();
    }
    
    public Card getTopCard(){
        return cards.get(cards.size()-1);
    }
    
    public List<Card> cardsUnder(Card card){ //!!//
        return cards.subList(cards.indexOf(card), cards.size());
    }
    
    public void moveCardToPile(Card cardToMove, CardPile destPile){
        destPile.addCard(cardToMove);
        cards.remove(cardToMove);
    }
    
    public void moveCardsToPile(List<Card> cardsToMove,CardPile destPile){
        cardsToMove.forEach(destPile::addCard);
        cardsToMove.clear();
    }
    
    @Override
    public Iterator<Card> iterator() {
       return cards.iterator();
    }

    @Override
    public void forEach(Consumer<? super Card> action) {
        cards.forEach(action);
    }
    
    public enum Type{
        Main,
        Slot,
        PlayerSlot,
        Hand,
        Deck
    }
    
}
