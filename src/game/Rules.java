package game;

import java.util.List;
import model.Card;
import model.CardPile;
import model.Rank;

public class Rules {
    
    private CardPile main;
    private List<CardPile> slot;
    private List<CardPile> playerSlot;
    private List<CardPile> hand;
    
    public Rules(){
        
    }

    public Rules(CardPile main, List<CardPile> slot, List<CardPile> playerSlot, List<CardPile> hand) {
        this.main = main;
        this.slot = slot;
        this.playerSlot = playerSlot;
        this.hand = hand;
    }
    
    public CardPile getMain() {
        return main;
    }

    public void setMain(CardPile main) {
        this.main = main;
    }

    public List<CardPile> getSlot() {
        return slot;
    }

    public void setSlot(List<CardPile> slot) {
        this.slot = slot;
    }

    public List<CardPile> getPlayerSlot() {
        return playerSlot;
    }

    public void setPlayerSlot(List<CardPile> playerSlot) {
        this.playerSlot = playerSlot;
    }

    public List<CardPile> getHand() {
        return hand;
    }

    public void setHand(List<CardPile> hand) {
        this.hand = hand;
    }
    
    public boolean isSameSuit(Card card1 , Card card2){
        return card1.getSuit() == card2.getSuit();
    }
    public boolean isSameRank(Card card1 , Card card2){
        return card1.getRank() == card2.getRank();
    }
    
   /* public boolean isMoveValid(Card card, CardPile destPile){
       if(destPile.getType() == CardPile.Type.Deck) return false;
       if(destPile.getType() == CardPile.Type.Hand) return false;
       if(destPile.getType() == CardPile.Type.Slot){
           if(destPile.isEmpty()) return false;
           else
               return card.getRank() == destPile.getTopCard().getRank();
       }
       if(destPile.getType() == CardPile.Type.PlayerSlot){
           
       }
       if(destPile.getType() == CardPile.Type.Main) return true;
       
       return false;
    }*/
    
    public boolean isMoveValid(List<Card> cards, CardPile destPile){
       boolean temp = false;
       Rank tempRank = cards.get(0).getRank();
       if(destPile.getType() == CardPile.Type.Deck) return false;
       if(destPile.getType() == CardPile.Type.Hand) return false;
       
       
       if(destPile.getType() == CardPile.Type.Slot){
           if(destPile.isEmpty()) 
               return false;
               
           
           else{
               System.out.println(destPile.getTopCard());
               for(Card card : cards){
                    temp = card.getRank() == destPile.getTopCard().getRank();
                    if(!temp) return false;
               }
               return true;
           }
       }
      
       if(destPile.getType() == CardPile.Type.PlayerSlot){
           if(destPile.isEmpty()) {
               return cards.size()>1;
           }
           
           if(!destPile.getCards().isEmpty() && cards.size()<2) return cards.get(0).getRank()==destPile.getTopCard().getRank();
           else if(cards.size()>=2){
               System.out.println("pile found");
               for(Card card : cards){
                   if(card.getRank() != tempRank) return false;
                   tempRank = card.getRank();
               }
               return true;
           }
           
           
       }
       if(destPile.getType() == CardPile.Type.Main){
           if(cards.size()>1) return false;
           else return true;
       }
       
       return false;
    }
    
    
    public CardPile lookForPile(Card card){
        for(CardPile pile : playerSlot){
            if(pile.getCards().contains(card)) return pile;
        }
        for(CardPile pile : slot){
            if(pile.getCards().contains(card)) return pile;
        }
        for(CardPile pile : hand){
            if(pile.getCards().contains(card)) return pile;
        }
        if(main.getCards().contains(card)) return main;
        
        return null;
    }
    
    
}
