package game;

import javafx.scene.input.MouseEvent;
import java.io.Serializable;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GameRoomView extends Pane implements Serializable{
    private String name;
    private String roomPassword;
    private int roomID;
    private static int totalRooms = 0;
    private List<Player> players;
    private List<String> players2;
    private int totalPlayers = 0;
    private GameRoom gameRoom;
    private Label player1 = new Label("EMPTY");
    private Label player2 = new Label("EMPTY");
    private Label player3 = new Label("EMPTY");
    private Label player4 = new Label("EMPTY");
    private Main main;
    private Button start;
    private Button back;
    private TextArea ta;
    private TextField tf;
    public GameRoomView(GameRoom gameRoom,Main main){
        ta = new TextArea();
        tf = new TextField();
        ta.setEditable(true);
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10,10,10,10));
        VBox labels = new VBox(10);
        layout.setRight(labels);
        layout.setCenter(ta);
        layout.setBottom(tf);
        tf.setOnAction(e -> {
            main.sendMsg(tf.getText().toString());
            tf.setText("");
        });
        start = new Button("START");
        start.setLayoutX(500);
        start.setLayoutY(500);
        back = new Button("Back");
        layout.setTop(back);
        //start.setMouseTransparent(true);
        this.main = main;
        this.gameRoom = gameRoom;
        this.name = gameRoom.getName();
        this.roomID = gameRoom.getRoomID();
        start.setOnAction(e -> {
            main.sendStartGameInfo(name);
            
        });
        back.setOnAction(e -> {
            main.primaryStage.getScene().setRoot(main.multiplayer());
            main.getPlayer().setContainingGameRoomView(null);
            main.getPlayer().setContainingRoom(null);
            //gameRoom.getPlayers2().remove(main.getPlayer().playerName);
            main.sendExitRoomInfo(name);
            main.sendMsg("left");
            main.getPlayer().setPlayerName(null);
            ta.clear();
            
        });
        labels.getChildren().addAll(player1,player2,player3,player4,start);
        this.getChildren().add(layout);
        
    }

    public String getName() {
        return name;
    }
    
    
    public void updateGameRoom(String[] roomInfoIn){
        player1.setText(roomInfoIn[1]);
        player2.setText(roomInfoIn[2]);
        player3.setText(roomInfoIn[3]);
        player4.setText(roomInfoIn[4]);
    }

    public Main getMain() {
        return main;
    }
    
    public void mouseClicked(MouseEvent e) {
        main.getPlayer().setContainingGameRoomView(this);
        main.getPlayer().setContainingRoom(gameRoom);
        main.openRoom(this);
        main.sendRoomInfo(name);
        main.sendMsg("joined");
    }
    
    public void addMsg(String msg){
        ta.appendText(msg + "\n");
    }
    
    
}
