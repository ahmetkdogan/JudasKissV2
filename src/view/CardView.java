/**
 *
 * @author hilal senturk, yaprak bulut, ahmet karadogan
 */

package view;

import model.Card;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class CardView extends ImageView {
    private Image frontFace;
    private String shortID;
    private CardPileView containingPile;
    private Card card;
    private DropShadow dropShadow;
    private Image backFace;
    private boolean faceDown;
    
    public CardView(Card card, Image frontFace, String shortID){
        this.card = card;
        this.frontFace = frontFace;
        this.shortID = shortID;
        this.dropShadow = new DropShadow(2,Color.gray(0, 0.75));
        setEffect(dropShadow);
    }
    
    public CardView(){
        
        this.dropShadow = new DropShadow(2,Color.gray(0, 0.75));
        setEffect(dropShadow);
        
    }

    public DropShadow getDropShadow() {
        return dropShadow;
    }

    public void setDropShadow(DropShadow dropShadow) {
        this.dropShadow = dropShadow;
    }

    public Image getFrontFace() {
        return frontFace;
    }

    public void setFrontFace(Image frontFace) {
        this.frontFace = frontFace;
        if(!faceDown)
            setImage(this.frontFace);
    }

    public Image getBackFace() {
        return backFace;
    }

    public void setBackFace(Image backFace) {
        this.backFace = backFace;
        if(faceDown)
            setImage(this.backFace);
    }
    
    public String getShortID() {
        return shortID;
    }

    public void setShortID(String shortID) {
        this.shortID = shortID;
    }

    public CardPileView getContainingPile() {
        return containingPile;
    }

    public void setContainingPile(CardPileView containingPile) {
        this.containingPile = containingPile;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
    public boolean isFaceDown(){
        return faceDown;
    }

    public void setFaceDown(boolean faceDown) {
        this.faceDown = faceDown;
    }
    
    public void flip(){
        faceDown = !faceDown;
        setImage(faceDown ? backFace:frontFace);
    }
}
