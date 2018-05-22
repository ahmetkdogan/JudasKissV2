/**
 *
 * @author ahmet karadogan
 */

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
    
    public static CardDeck createCardDeckByArray(String[] deck){
        CardDeck result = new CardDeck();
        Rank rank = null;
        Suit suit = null;
        for(int i = 0 ; i < 52 ; i++){
            if(deck[i].length() == 2){
                rank = Rank.getRank(deck[i].charAt(0)+"");
                suit = Suit.getSuit(deck[i].charAt(1)+"");
            }
            else if(deck[i].length() == 3){
                rank = Rank.getRank(deck[i].substring(0,2));
                suit = Suit.getSuit(deck[i].charAt(2)+"");
            }
            result.addCard(new Card(suit,rank));
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
