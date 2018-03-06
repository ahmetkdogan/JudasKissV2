package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;


public class CardPileView extends Pane implements Iterable<CardView>{
    
    private double cardGap;
    
    private ObservableList<CardView> cards = FXCollections.observableArrayList();
    
    private String shortID;
    
    public CardPileView(double cardGap){
        this.cardGap = cardGap;
    }
    
    public CardPileView(double cardGap,String shortID){
        this.cardGap = cardGap;
        this.shortID = shortID;
    }

    public double getCardGap() {
        return cardGap;
    }

    public void setCardGap(double cardGap) {
        this.cardGap = cardGap;
    }

    public ObservableList<CardView> getCards() {
        return cards;
    }

    public void setCards(ObservableList<CardView> cards) {
        this.cards = cards;
    }

    public String getShortID() {
        return shortID;
    }

    public void setShortID(String shortID) {
        this.shortID = shortID;
    }
    
    public int numOfCards(){
        return cards.size();
    }
    
    public void addCardView(CardView cardView){
        cards.add(cardView);
        cardView.setContainingPile(this);
        cardView.toFront();
        layoutCard(cardView);
    }
    
    private void layoutCard(CardView cardView){
        cardView.relocate(cardView.getLayoutX() + cardView.getTranslateX(),
                cardView.getLayoutY() + cardView.getTranslateY());
        cardView.setTranslateX(0);
        cardView.setTranslateY(0);
        cardView.setLayoutX(getLayoutX() + (cards.size()-1) * cardGap);
        cardView.setLayoutY(getLayoutY() );
    }
    
    public boolean isEmpty(){
        return cards.isEmpty();
    }
    
    public CardView getTopCardView(){
        return cards.get(cards.size() -1);
    }
    
    public void moveCardViewToPile(CardView cardToMove, CardPileView destPile){
        destPile.addCardView(cardToMove);
        cards.remove(cardToMove);
    }

    @Override
    public Iterator<CardView> iterator() {
        return cards.iterator();
    }

    @Override
    public void forEach(Consumer<? super CardView> action) {
        cards.forEach(action);
    }
    
    public List<CardView> cardViewsUnder(CardView cardView){
        return cards.subList(cards.indexOf(cardView), cards.size());
    }
    
}
