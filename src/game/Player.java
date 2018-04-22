
package game;

import java.io.Serializable;


public class Player implements Serializable{
    String playerName;
    String playerNick;
    private String playerIP;
    private boolean playersTurn;
    private int playerScore;
    private GameRoom containingRoom;
    private GameRoomView containingGameRoomView;
    
    public Player(String playerNick){
        this.playerNick = playerNick;
    }
    public Player(){
        
    }

    public GameRoomView getContainingGameRoomView() {
        return containingGameRoomView;
    }

    public void setContainingGameRoomView(GameRoomView containingGameRoomView) {
        this.containingGameRoomView = containingGameRoomView;
    }
    public void setContainingRoom(GameRoom containingRoom){
        this.containingRoom = containingRoom;
    }
    public GameRoom getContainingRoom(){
        return containingRoom;
    }

    public String getPlayerNick() {
        return playerNick;
    }

    public void setPlayerNick(String playerNick) {
        this.playerNick = playerNick;
    }
    
    
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerIP() {
        return playerIP;
    }

    public void setPlayerIP(String playerIP) {
        this.playerIP = playerIP;
    }

    public boolean isPlayersTurn() {
        return playersTurn;
    }

    public void setPlayersTurn(boolean playersTurn) {
        this.playersTurn = playersTurn;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }
    
    
}
