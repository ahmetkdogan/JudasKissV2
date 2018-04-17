package game;

import java.util.ArrayList;
import java.util.Iterator;
import model.Card;
import model.CardPile;
import view.CardPileView;
import view.CardView;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.List;
import java.util.ListIterator;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;


public class MouseUtil {
    
    public static boolean pileExists = false;
    
    public static boolean cardPlayed = false;
    
    public static boolean pilePlayed = false;
    
    private final MousePos mousePos = new MousePos();
    
    private Card draggedCard;
    
    private List<Card> draggedCards;
    
    private List<CardView> draggedCardViews;
    
    private CardView draggedCardView;
    
    private Game game;
    
    private GameArea gameArea;
    
    private Turn turn;
    
    public static int turnPlayed = 0;
    
    private static int currentRound = 1;
    
    private CardPileView destPileView2;
    
    private Client client;
    
    private CardPileView dummy = new CardPileView(10, "dummy");
    
    EventHandler<MouseEvent> onMouseClickedHandler = e -> {
        if(e.getButton() == MouseButton.SECONDARY){
        CardView cardView = (CardView) e.getSource();
        
        Card card = game.getDeck().getById(cardView.getShortID());
        
        CardPileView activePileView = cardView.getContainingPile();
        
        CardPile activePile = game.getPileById(activePileView.getShortID());
        
        draggedCardViews = activePileView.cardViewsUnder(cardView);
        draggedCards = activePile.cardsUnder(card);
        
        client.sendInfo(card, cardView, activePile, activePileView, dummy);
       /* if(draggedCards.size() > 1 && !pilePlayed  && (activePile.equals(game.getPlayerSlotPiles().get(0))|| activePile.equals(game.getPlayerSlotPiles().get(1)) 
                || activePile.equals(game.getPlayerSlotPiles().get(2)) || activePile.equals(game.getPlayerSlotPiles().get(3)))){
            pilePlayed = true;
            cardView.setLayoutX(cardView.getLayoutX()+10);
            startTurn(activePile, activePileView, cardView);
            return;
            
        }*/
        
        }
    };
    
    
    EventHandler<MouseEvent> onMousePressedHandler = e -> {
        if(e.getButton()!=MouseButton.SECONDARY){
        mousePos.x = e.getSceneX();
        mousePos.y = e.getSceneY();
        }
    };
    
    EventHandler<MouseEvent> onMouseDraggedHandler = e-> {
        if(e.getButton() != MouseButton.SECONDARY){
        double offsetX = e.getSceneX() - mousePos.x;
        double offsetY = e.getSceneY() - mousePos.y;
        
        CardView cardView = (CardView) e.getSource();
        Card card = game.getDeck().getById(cardView.getShortID());
        
        cardView.getDropShadow().setRadius(20);
        cardView.getDropShadow().setOffsetX(10);
        cardView.getDropShadow().setOffsetY(10);
        
        CardPileView activePileView = cardView.getContainingPile();
        
        CardPile activePile = game.getPileById(activePileView.getShortID());
        
        draggedCardViews = activePileView.cardViewsUnder(cardView);
        draggedCards = activePile.cardsUnder(card);
        
        draggedCardViews.forEach(cw -> {
            cw.toFront();
            cw.setTranslateX(offsetX);
            cw.setTranslateY(offsetY);
        });
        }
    };
    
    EventHandler<MouseEvent> onMouseReleasedHandler = e ->{
        if(e.getButton() != MouseButton.SECONDARY){
        if(draggedCards == null && draggedCardViews == null) return;
        
        CardView cardView = (CardView) e.getSource();
        
        Card card = game.getDeck().getById(cardView.getShortID());
        
        CardPileView activePileView = cardView.getContainingPile();
        
        CardPile activePile = game.getPileById(activePileView.getShortID());
        
        if(checkAllPiles(card,cardView,activePile,activePileView) && game.getRules().isMoveValid(draggedCards, activePile)){
            System.out.println(destPileView2);
            System.out.println(client);
            client.sendInfo(card, cardView, activePile, activePileView, destPileView2);
            
             
            //startTurn(activePile, activePileView, cardView);
            return;
            
            
        }
        draggedCardViews.forEach(this::slideBack);
        //draggedCard=null;
        draggedCardViews = null;
        draggedCards=null;
        }
        
    };
    
    public MouseUtil(Game game,GameArea gameArea,Client client){
        this.game = game;
        this.gameArea = gameArea;
        this.client = client;
    }
    
    public void makeDraggable(CardView card){
        card.setOnMouseClicked(null);
        card.setOnMousePressed(onMousePressedHandler);
        card.setOnMouseDragged(onMouseDraggedHandler);
        card.setOnMouseReleased(onMouseReleasedHandler);
        card.setOnMouseClicked(onMouseClickedHandler);
    }
    
    private boolean checkAllPiles(Card card,CardView cardView,CardPile activePile,
            CardPileView activePileView){
        //check slot piles//
         if(checkPiles(card,cardView,activePile,activePileView,gameArea.getSlotPileViews())) return true;
        if(checkPiles(card,cardView,activePile,activePileView,gameArea.getPlayerSlotPileViews())) return true;
        return checkPile(card,cardView,activePile,activePileView,gameArea.getMainPileView());
    }
    private boolean checkPile(Card card,CardView cardView,CardPile activePile,
            CardPileView activePileView,CardPileView pileView){
        boolean result = false;
        
        if(!pileView.equals(activePileView) && 
                isOverPile(cardView,pileView) /*&&
                handleValidMove(card,activePile,activePileView,pileView)*/)
            result = true;
        
        return result;
    }
    
    private boolean checkPiles(Card card,CardView cardView,CardPile activePile,
            CardPileView activePileView,List<CardPileView> pileViews){
        boolean result = false;
        
        for(CardPileView pileView : pileViews){
            if(pileView.equals(activePileView)){
                result = false; 
                break;/////////// 17.35 - 17.04.2017
            }
            
            if(isOverPile(cardView,pileView) /*&& 
                    handleValidMove(card,activePile,activePileView,pileView)*/){
                result = true;
            }
            
        }
        return result;
    }
    
    private boolean isOverPile(CardView cardView,CardPileView pileView){
        if(pileView.isEmpty()){
            if(cardView.getBoundsInParent().intersects(pileView.getBoundsInParent())){
                destPileView2 = pileView;
                return cardView.getBoundsInParent().intersects(pileView.getBoundsInParent());
            }
            else return cardView.getBoundsInParent().intersects(pileView.getBoundsInParent());
        }
        else{
            if(cardView.getBoundsInParent().intersects(pileView.getTopCardView().getBoundsInParent())){
                destPileView2 = pileView;
                return cardView.getBoundsInParent().intersects(pileView.getTopCardView().getBoundsInParent());
            }
            else return cardView.getBoundsInParent().intersects(pileView.getTopCardView().getBoundsInParent());
        }
            
    }
    
    private boolean handleValidMove(Card card, CardPile sourcePile,
            CardPileView sourcePileView,CardPileView destPileView){
        CardPile destPile = game.getPileById(destPileView.getShortID());
        
        if(game.getRules().isMoveValid(draggedCards, destPile)){
            game.moveCards(draggedCards, sourcePile, destPile);
            slideToPile(draggedCardViews,sourcePileView,destPileView);
            draggedCards = null;
            draggedCardViews = null;
            return true;
        }
        else {
         return false;   
        }
        
    }
    public boolean handleValidMove(Card card, CardPile sourcePile,
            CardPileView sourcePileView,CardPileView destPileView,List<CardView> draggedCardViews,List<Card> draggedCards){
        CardPile destPile = game.getPileById(destPileView.getShortID());
        if(game.getRules().isMoveValid(draggedCards, destPile)){
            game.moveCards(draggedCards, sourcePile, destPile);
            slideToPile(draggedCardViews,sourcePileView,destPileView);
            draggedCards = null;
            draggedCardViews = null;
            return true;
        }
        else {
         return false;   
        }
        
    }
    
     void slideBack(CardView card){
        double sourceX = card.getLayoutX() + card.getTranslateX();
        double sourceY = card.getLayoutY() + card.getTranslateY();
        
        double targetX = card.getLayoutX();
        double targetY = card.getLayoutY();
        
        animateCardMovement(card,sourceX,sourceY,
                targetX, targetY, Duration.millis(150), e->{
                    card.getDropShadow().setRadius(2);
                    card.getDropShadow().setOffsetX(0);
                    card.getDropShadow().setOffsetY(0);
                });
    }
    
    private void slideToPile(List<CardView> cardsToSlide, CardPileView sourcePile,
            CardPileView destPile){
        if(cardsToSlide==null) return;
        
        if(!destPile.isEmpty() && destPile.getTopCardView().getCard().getRank()== cardsToSlide.get(0).getCard().getRank()){
            if(destPile.getCards().size()>=3){
                if(destPile.getCards().get(destPile.getCards().size()-3).getCard().getRank() == destPile.getCards().get(destPile.getCards().size()-2).getCard().getRank() &&
                      destPile.getCards().get(destPile.getCards().size()-2).getCard().getRank() == destPile.getCards().get(destPile.getCards().size()-1).getCard().getRank()  ){
                    
                    destPile.getCards().get(destPile.getCards().size()-3).setMouseTransparent(false);
                    destPile.getCards().get(destPile.getCards().size()-3).setLayoutX(destPile.getCards().get(destPile.getCards().size()-3).getLayoutX()-10);
                }
                else if(destPile.getCards().get(destPile.getCards().size()-2).getCard().getRank() == destPile.getCards().get(destPile.getCards().size()-1).getCard().getRank() ){
                    destPile.getCards().get(destPile.getCards().size()-2).setMouseTransparent(false);
                    destPile.getCards().get(destPile.getCards().size()-2).setLayoutX(destPile.getCards().get(destPile.getCards().size()-2).getLayoutX()-10);
                }
                else{  
                destPile.getTopCardView().setMouseTransparent(false);
            destPile.getTopCardView().setLayoutX(destPile.getTopCardView().getLayoutX() - 10 );
                }
            }
            else if(destPile.getCards().size()==2){
                if(destPile.getCards().get(destPile.getCards().size()-2).getCard().getRank() == destPile.getCards().get(destPile.getCards().size()-1).getCard().getRank() ){
                    destPile.getCards().get(destPile.getCards().size()-2).setMouseTransparent(false);
                    destPile.getCards().get(destPile.getCards().size()-2).setLayoutX(destPile.getCards().get(destPile.getCards().size()-2).getLayoutX()-10);
            }
                else{  
                destPile.getTopCardView().setMouseTransparent(false);
            destPile.getTopCardView().setLayoutX(destPile.getTopCardView().getLayoutX() - 10 );
                }
            
                
        }
            else{  
                destPile.getTopCardView().setMouseTransparent(false);
            destPile.getTopCardView().setLayoutX(destPile.getTopCardView().getLayoutX() - 10 );
                }
            pileExists = true;
            
        }
        
        double destCardGap = destPile.getCardGap();
        
        double targetX;
        double targetY;
        
        if(destPile.isEmpty()){
            targetX = destPile.getLayoutX();
            targetY =destPile.getLayoutY();
        } else{
            targetX = destPile.getTopCardView().getLayoutX();
            targetY = destPile.getTopCardView().getLayoutY();
        }
        
        for(int i = 0 ; i<cardsToSlide.size();i++){
            CardView currentCardView = cardsToSlide.get(i);
            double sourceX=
                    currentCardView.getLayoutX() + currentCardView.getTranslateX();
            double sourceY=
                    currentCardView.getLayoutY() + currentCardView.getTranslateY();
            animateCardMovement(currentCardView, sourceX, sourceY,
                    targetX + ((destPile.isEmpty() ? i : i + 1) *destCardGap),
          targetY ,
          Duration.millis(150),
          e -> {
            sourcePile.moveCardViewToPile(currentCardView, destPile);
            currentCardView.getDropShadow().setRadius(2);
            currentCardView.getDropShadow().setOffsetX(0);
            currentCardView.getDropShadow().setOffsetY(0);
          });
        }
        if(cardsToSlide.size() == 1) cardPlayed = true;
        if(!destPile.isEmpty() && cardsToSlide.size() > 1 && destPile.getTopCardView().getCard().getRank().equals(cardsToSlide.get(0).getCard().getRank())) pilePlayed = false;
        else if(cardsToSlide.size() > 1) pilePlayed = true;
               
    }
        
    
    private void animateCardMovement(
        CardView card,double sourceX,double sourceY,
            double targetX, double targetY, Duration duration,
            EventHandler<ActionEvent> doAfer){
        
        Path path = new Path();
        path.getElements().add(new MoveToAbs(card,sourceX,sourceY));
        path.getElements().add(new LineToAbs(card,targetX, targetY));
        
        PathTransition pathTransition = 
                new PathTransition(duration,path,card);
        pathTransition.setInterpolator(Interpolator.EASE_IN);
        pathTransition.setOnFinished(doAfer);
        Timeline blurReset = new Timeline();
        KeyValue bx = new KeyValue(card.getDropShadow().offsetXProperty(),0,Interpolator.EASE_IN);
        KeyValue by = new KeyValue(card.getDropShadow().offsetYProperty(),0,Interpolator.EASE_IN);
        KeyValue br = new KeyValue(card.getDropShadow().radiusProperty(),2,Interpolator.EASE_IN);
        KeyFrame bKeyFrame = new KeyFrame(duration,bx,by,br);
        blurReset.getKeyFrames().add(bKeyFrame);
        
        ParallelTransition pt = new ParallelTransition(card,pathTransition,blurReset);
        pt.play();
    }
    
    private static class MoveToAbs extends MoveTo{
        public MoveToAbs(Node node,double x, double y){
            super(x-node.getLayoutX() + node.getLayoutBounds().getWidth()/2,
                    y-node.getLayoutY() + node.getLayoutBounds().getHeight()/2);
        }
    }
    
    private static class LineToAbs extends LineTo{
        public LineToAbs(Node node, double x, double y){
            super(x-node.getLayoutX() + node.getLayoutBounds().getWidth()/2,
                    y-node.getLayoutY() + node.getLayoutBounds().getHeight()/2);
        }
    }
    
    private static class MousePos{
        double x,y;
    }
    
    public void startTurn(CardPile activePile, CardPileView activePileView,CardView cardView){
        turn = new Turn(gameArea, activePile, activePileView);
            if(turn.isTurnFinished()){ turn.startTurn(); turnPlayed++;}
            if(turnPlayed == 16 && currentRound==1){turn.startTurn(); currentRound++;}
            if(turnPlayed == 32 && currentRound==2){turn.startTurn(); currentRound++;}
            
            cardView.setMouseTransparent(true);
            if(turnPlayed == 48 ) System.out.println("Game Over");
            
             if(cardView.isFaceDown())cardView.flip();
    }
    
}
