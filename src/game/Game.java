/**
 *
 * @author ahmet karadogan
 */

package game;

import java.util.Iterator;
import java.util.List;
import model.Card;
import model.CardDeck;
import model.CardPile;
import javafx.collections.FXCollections;


public class Game {
    private CardDeck deck;
    private CardPile mainPile;
    private List<CardPile> slotPiles;
    private List<CardPile> playerSlotPiles;
    private List<CardPile> hand0Piles;
    private List<CardPile> hand1Piles;
    private List<CardPile> hand2Piles;
    private List<CardPile> hand3Piles;
    private CardPile deckPile;
    private Rules rules;
    
    
    public Game(){
        this.mainPile = new CardPile(CardPile.Type.Main,"M");
        this.slotPiles = FXCollections.observableArrayList();
        this.playerSlotPiles = FXCollections.observableArrayList();
        this.hand0Piles = FXCollections.observableArrayList();
        this.hand1Piles = FXCollections.observableArrayList();
        this.hand2Piles = FXCollections.observableArrayList();
        this.hand3Piles = FXCollections.observableArrayList();
        this.deckPile=new CardPile(CardPile.Type.Deck,"D");
        
        // Create all card piles
        for(int i = 0 ; i<4 ; i++)
            slotPiles.add(new CardPile(CardPile.Type.Slot,"S"+i));
        for(int i = 0; i<4 ; i++)
            playerSlotPiles.add(new CardPile(CardPile.Type.PlayerSlot,"P"+i));
        for(int i = 0; i<4 ; i++)
            hand0Piles.add(new CardPile(CardPile.Type.Hand,"P0H"+i));
        for(int i = 0; i<4 ; i++)
            hand1Piles.add(new CardPile(CardPile.Type.Hand,"P1H"+i));
        for(int i = 0; i<4 ; i++)
            hand2Piles.add(new CardPile(CardPile.Type.Hand,"P2H"+i));
        for(int i = 0; i<4 ; i++)
            hand3Piles.add(new CardPile(CardPile.Type.Hand,"P3H"+i));
        
        this.rules = new Rules(mainPile,slotPiles,playerSlotPiles,hand0Piles);
        
        
        
    }

    public CardPile getMainPile() {
        return mainPile;
    }

    public void setMainPile(CardPile mainPile) {
        this.mainPile = mainPile;
    }

    public List<CardPile> getSlotPiles() {
        return slotPiles;
    }

    public void setSlotPiles(List<CardPile> slotPiles) {
        this.slotPiles = slotPiles;
    }

    public List<CardPile> getPlayerSlotPiles() {
        return playerSlotPiles;
    }

    public void setPlayerSlotPiles(List<CardPile> playerSlotPiles) {
        this.playerSlotPiles = playerSlotPiles;
    }

    public List<CardPile> getHand0Piles() {
        return hand0Piles;
    }

    public void setHand0Piles(List<CardPile> hand0Piles) {
        this.hand0Piles = hand0Piles;
    }

    public List<CardPile> getHand1Piles() {
        return hand1Piles;
    }

    public void setHand1Piles(List<CardPile> hand1Piles) {
        this.hand1Piles = hand1Piles;
    }

    public List<CardPile> getHand2Piles() {
        return hand2Piles;
    }

    public void setHand2Piles(List<CardPile> hand2Piles) {
        this.hand2Piles = hand2Piles;
    }

    public List<CardPile> getHand3Piles() {
        return hand3Piles;
    }

    public void setHand3Piles(List<CardPile> hand3Piles) {
        this.hand3Piles = hand3Piles;
    }

    

    public CardPile getDeckPile() {
        return deckPile;
    }

    public void setDeckPile(CardPile deckPile) {
        this.deckPile = deckPile;
    }
    
    
    public CardDeck getDeck() {
        return deck;
    }

    public void setDeck(CardDeck deck) {
        this.deck = deck;
    }

    
    
    public void startNewGame(){
       deck = CardDeck.createCardDeckByArray(Client.deckInfo);
       Iterator<Card> deckIterator = deck.iterator();
        
       
       // Deal the cards from the deck to players when the game started.
       for(CardPile handPile : hand0Piles){
           handPile.addCard(deckIterator.next());
       }
       for(CardPile handPile : hand0Piles){
           handPile.addCard(deckIterator.next());
       }
       for(CardPile handPile : hand0Piles){
           handPile.addCard(deckIterator.next());
       }
       //
       for(CardPile handPile : hand1Piles){
           handPile.addCard(deckIterator.next());
       }
       for(CardPile handPile : hand1Piles){
           handPile.addCard(deckIterator.next());
       }
       for(CardPile handPile : hand1Piles){
           handPile.addCard(deckIterator.next());
       }
       //
       for(CardPile handPile : hand2Piles){
           handPile.addCard(deckIterator.next());
       }
       for(CardPile handPile : hand2Piles){
           handPile.addCard(deckIterator.next());
       }
       for(CardPile handPile : hand2Piles){
           handPile.addCard(deckIterator.next());
       }
       //
       for(CardPile handPile : hand3Piles){
           handPile.addCard(deckIterator.next());
       }
       for(CardPile handPile : hand3Piles){
           handPile.addCard(deckIterator.next());
       }
       for(CardPile handPile : hand3Piles){
           handPile.addCard(deckIterator.next());
       }
       //
       for(CardPile slotPile : slotPiles){
           slotPile.addCard(deckIterator.next());
       }
       
        
       deckIterator.forEachRemaining(deckPile::addCard);
       
       
       
              
    }
    
    public void moveCards(List<Card> cardsToMove , CardPile from, CardPile to){
        if(cardsToMove == null) return;
        
        from.moveCardsToPile(cardsToMove, to);
    }
    
    
    public CardPile getPileById(String id){
        CardPile result;
        
        result = slotPiles.stream()
                .filter(pile -> pile.getId().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        
        result = playerSlotPiles.stream()
                .filter(pile -> pile.getId().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        
        result = hand0Piles.stream()
                .filter(pile -> pile.getId().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        result = hand1Piles.stream()
                .filter(pile -> pile.getId().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        result = hand2Piles.stream()
                .filter(pile -> pile.getId().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        result = hand3Piles.stream()
                .filter(pile -> pile.getId().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        
        if(mainPile.getId().equals(id)) return mainPile;
        if(deckPile.getId().equals(id)) return deckPile;
        return null;
        
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }
    
    
}
