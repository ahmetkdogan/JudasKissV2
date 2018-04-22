package game;

import java.io.Serializable;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class GameRoomView extends Pane implements Serializable{
    private String name;
    private String roomPassword;
    private int roomID;
    private static int totalRooms = 0;
    private List<Player> players;
    private List<String> players2;
    private int totalPlayers = 0;
    private GameRoom gameRoom;
    private StringBuilder labelMaker;
    private Label playerNames;
    private Main main;
    private Button start;
    public GameRoomView(GameRoom gameRoom,Main main){
        start = new Button("START");
        start.setLayoutX(500);
        start.setLayoutY(500);
        //start.setMouseTransparent(true);
        
        if(gameRoom.getTotalPlayers()==3){
            start.setMouseTransparent(false);
        }
        this.main = main;
        this.gameRoom = gameRoom;
        this.name = gameRoom.getName();
        this.roomID = gameRoom.getRoomID();
        start.setOnAction(e -> {
            main.sendStartGameInfo("room1");
            
        });
        labelMaker = new StringBuilder();
        playerNames = new Label();
        this.players = gameRoom.getPlayers();
        this.players2 = gameRoom.getPlayers2();
        players2.forEach(e -> {
            labelMaker.append(e);
            labelMaker.append("\n");
        });
        playerNames.setText(labelMaker.toString());
        this.getChildren().addAll(playerNames,start);
        
    }
    
    
    public void updateGameRoom(){
        StringBuilder temp = new StringBuilder();
        players2.forEach(e -> {
            labelMaker.append(e);
            labelMaker.append("\n");
        });
        playerNames.setText(temp.toString());
    }

    public Main getMain() {
        return main;
    }
    
}
