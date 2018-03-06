package game;

import java.util.List;
import model.CardPile;
import view.CardPileView;



public class Turn {
    
    private static String playersTurn;
    private GameArea gameArea;
    private CardPile activePile;
    private CardPileView activePileView;
    
    public Turn(GameArea gameArea,CardPile activePile,CardPileView activePileView){
        this.gameArea = gameArea;
        this.activePile = activePile;
        this.activePileView=activePileView;
    }
    public Turn(){}
    
    
    public boolean isTurnFinished(){
        List<CardPileView> temp;
        if(activePileView.getShortID().equals("P0H0") ||activePileView.getShortID().equals("P0H1") ||activePileView.getShortID().equals("P0H2")||activePileView.getShortID().equals("P0H3")) {temp=gameArea.getHand0PileViews(); playersTurn = "player0";}
        else if(activePileView.getShortID().equals("P1H0") ||activePileView.getShortID().equals("P1H1") ||activePileView.getShortID().equals("P1H2")||activePileView.getShortID().equals("P1H3")){ temp=gameArea.getHand1PileViews(); playersTurn = "player1";}
        else if(activePileView.getShortID().equals("P2H0") ||activePileView.getShortID().equals("P2H1") ||activePileView.getShortID().equals("P2H2")||activePileView.getShortID().equals("P2H3")){ temp=gameArea.getHand2PileViews();playersTurn = "player2"; }
        else if(activePileView.getShortID().equals("P3H0") ||activePileView.getShortID().equals("P3H1") ||activePileView.getShortID().equals("P3H2")||activePileView.getShortID().equals("P3H3")) {temp=gameArea.getHand3PileViews(); playersTurn = "player3";} 
        else temp=null;
        if(!MouseUtil.cardPlayed) return false;
        
        else{
            if(temp != null)
            for(CardPileView pileView : temp){
                if(!pileView.getCards().isEmpty() && isFlippable(pileView))  {  
                pileView.getTopCardView().flip();
                pileView.getTopCardView().setMouseTransparent(pileView.getTopCardView().isFaceDown());
                }
        }
            if(MouseUtil.pileExists) {
                if(MouseUtil.pilePlayed){
                MouseUtil.cardPlayed = false;
                MouseUtil.pileExists = false;
                MouseUtil.pilePlayed = false;
                return true;
            }
                else return false;
            }
            
            else {
                MouseUtil.cardPlayed = false;
                MouseUtil.pileExists = false;
                MouseUtil.pilePlayed = false;
                return true;
            }
        }
    }
    public void startTurn(){
        List<CardPileView> temp;
        if(playersTurn.equals("player0")) temp=gameArea.getHand1PileViews();
        else if(playersTurn.equals("player1")) temp = gameArea.getHand2PileViews();
        else if(playersTurn.equals("player2")) temp = gameArea.getHand3PileViews();
        else if(playersTurn.equals("player3")) temp=gameArea.getHand0PileViews();
        else temp = null;
        
        if(temp != null)
        for(CardPileView pileView : temp){
                if(!pileView.getCards().isEmpty() && isFlippable(pileView))  {  
                pileView.getTopCardView().flip();
                pileView.getTopCardView().setMouseTransparent(pileView.getTopCardView().isFaceDown());
                }
        }
        
    }
    
    private boolean isFlippable(CardPileView pileView){
        if(MouseUtil.turnPlayed < 16) return pileView.getCards().size()==3;
        if(MouseUtil.turnPlayed >= 16 && MouseUtil.turnPlayed<32) return pileView.getCards().size()==2;
        if(MouseUtil.turnPlayed >= 32 && MouseUtil.turnPlayed<48) return pileView.getCards().size()==1;
        return false;
    }
    
}
