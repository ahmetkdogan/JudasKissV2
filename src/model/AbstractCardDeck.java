package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCardDeck implements Iterable<Card>{
    protected List<Card> cards;
    
    public AbstractCardDeck(){
        this.cards = new ArrayList<>();
    }
    
    public List<Card> getCards(){
        return cards;
    }
    
    public void addCard(Card card){
        cards.add(card);
    }
    
    public boolean removeCard(Card card){
        return cards.remove(card);
    }
    
    public void shuffle(){
        for(int i = 0 ; i<7;i++){
            Collections.shuffle(cards);
        }
        
    }
    
    public Card getById(String id){
        return cards.stream().filter(card -> card.getId().equals(id)).findFirst().orElseGet(()->null); 
    }
    
    
}
