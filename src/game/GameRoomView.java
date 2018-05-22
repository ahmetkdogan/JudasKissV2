/**
 *
 * @author hilal senturk, yaprak bulut, ahmet karadogan
 */

package game;

import javafx.scene.input.MouseEvent;
import java.io.Serializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GameRoomView extends Pane implements Serializable{
    private String name;
    private int roomID;
    private static int totalRooms = 0;
    private int totalPlayers = 0;
    private GameRoom gameRoom;
    private Label playerNames = new Label("PLAYERS");
    private Label player1 = new Label("EMPTY");
    private Label player2 = new Label("EMPTY");
    private Label player3 = new Label("EMPTY");
    private Label player4 = new Label("EMPTY");
    private Main main;
    private Button start;
    private Button back;
    private TextArea ta;
    private TextField tf;
    String style = getClass().getResource("fxml.css").toExternalForm();
    
    public GameRoomView(GameRoom gameRoom,Main main){
        HBox layout = new HBox(40);
        layout.setLayoutX(305);
        layout.setLayoutY(257);
        ta = new TextArea();
        tf = new TextField();
        ta.setEditable(false);
        this.getStylesheets().add(style);
        this.setBackground(new Background(new BackgroundImage(new Image("/images/background.jpg"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT))); 
        VBox labels = new VBox(10);
        VBox chat = new VBox(5);
        chat.getChildren().addAll(ta,tf);
        tf.setOnAction(e -> {
                main.sendMsg(tf.getText().toString());
            tf.setText("");
        });
        start = new Button("START");
        back = new Button("Back");
        back.setLayoutX(320);
        back.setLayoutY(747);
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
            main.sendExitRoomInfo(name);
            main.sendMsg("left");
            main.getPlayer().setPlayerName(null);
            ta.clear();
            
        });
        labels.getChildren().addAll(playerNames,player1,player2,player3,player4,start);
        layout.getChildren().addAll(chat,labels);
        this.getChildren().addAll(layout,back);
        
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
