package model;

import java.util.Iterator;
import java.util.function.Consumer;

public class CardDeck extends AbstractCardDeck{
    
    public static CardDeck createCardDeck(){
        CardDeck result = new CardDeck();
        
        for(Suit suit : Suit.values()){
            for(Rank rank : Rank.values()){
                result.addCard(new Card(suit,rank));
            }
        }
        return result;
    }
    
    public Iterator<Card> iterator(){
        return cards.iterator();
    }
    
    public void forEach(Consumer<? super Card> action){
        cards.forEach(action);
    }
    
}
