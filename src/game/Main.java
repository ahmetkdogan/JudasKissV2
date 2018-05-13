    package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.Card;
import model.CardDeck;
import model.CardPile;
import view.CardPileView;
import view.CardView;

public class Main extends Application {

    private GameArea gameArea = new GameArea(new Image("/images/background.png"));
    private static final double WIDTH = 1920; //1872
    private static final double HEIGHT = 1080; //936
    private Game game = new Game();
    private Player player = new Player("nonick");
    private static int temp = 0;
    private static List<GameRoom> roomList = new ArrayList<>();
    private static List<GameRoomView> roomViewList = new ArrayList<>();
    CountDownLatch latch = new CountDownLatch(1);//----/-/-/-/-
    private Client client = new Client(gameArea, game, player,latch,roomList,this,roomViewList);
    private MouseUtil mouseUtil = new MouseUtil(game, gameArea, client, player);
    static List<CardView> cardViewList = new ArrayList<>();
    Stage primaryStage;
    Media knight = new Media(new File("knight.mp3").toURI().toString());
    Media metin2 = new Media(new File("metin2.mp3").toURI().toString());
    MediaPlayer music = new MediaPlayer(knight);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        client.start();
        music.play();
        this.primaryStage=primaryStage;
        primaryStage.setScene(new Scene(mainMenu(),WIDTH,HEIGHT));
        primaryStage.setFullScreen(true);
        //primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
                System.exit(0);
            });
        
        
        
        
    }

    public Player getPlayer() {
        return player;
    }
    
    public Pane mainMenu(){
        
        Button startButton = new Button("START");
        startButton.setLayoutX(726);
        startButton.setLayoutY(42);
        startButton.setStyle(
                "-fx-background-radius: 1em; " +
                "-fx-max-width: 502px; " +
                "-fx-max-height: 201px;"
        );
        startButton.setPrefSize(502, 201);
        
        Button multiplayerButton = new Button("MULTIPLAYER");
        multiplayerButton.setStyle(
                "-fx-background-radius: 1em; " +
                "-fx-max-width: 360px; " +
                "-fx-max-height: 152px;"
        );
        multiplayerButton.setPrefSize(360, 152);
        multiplayerButton.setLayoutX(801);
        multiplayerButton.setLayoutY(285);
        
        
        Button howToPlayButton = new Button("HOW TO PLAY");
        howToPlayButton.setStyle(
                "-fx-background-radius: 1em; " +
                "-fx-max-width: 360px; " +
                "-fx-max-height: 152px;"
        ); 
        howToPlayButton.setPrefSize(360, 152);
        howToPlayButton.setLayoutX(801);
        howToPlayButton.setLayoutY(467);
        
        
        Button optionButton = new Button("OPTIONS");
        optionButton.setStyle(
                "-fx-background-radius: 1em; " +
                "-fx-max-width: 360px; " +
                "-fx-max-height: 152px;"
        );
        optionButton.setPrefSize(360, 152);
        optionButton.setLayoutX(801);
        optionButton.setLayoutY(653);
        
        Button exitButton = new Button("EXIT");
        exitButton.setStyle(
                "-fx-background-radius: 1em; " +
                "-fx-max-width: 360px; " +
                "-fx-max-height: 152px;"
        );
        exitButton.setPrefSize(360, 152);
        exitButton.setLayoutX(801);
        exitButton.setLayoutY(837);
        
        
        /*startButton.setOnAction(e -> {
            primaryStage.setScene(startGame());
        });
        */
        multiplayerButton.setOnAction(e -> {
            primaryStage.getScene().setRoot(multiplayer());
        });
        howToPlayButton.setOnAction(e -> {
            primaryStage.getScene().setRoot(howToPlay());
        });
        optionButton.setOnAction(e -> {
             primaryStage.getScene().setRoot(options());
        });
        exitButton.setOnAction(e -> {
            System.exit(0);
        });
        Pane layout = new Pane();
        layout.getChildren().addAll(startButton,multiplayerButton,howToPlayButton,optionButton,exitButton);
        layout.setLayoutX(0);
        layout.setLayoutY(0);
        layout.setBackground(new Background(new BackgroundImage(new Image("/images/background.png"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));    
        
        return layout;
    }
    
    private void inGameSettings(){
        Popup popup = new Popup();
        Button resume = new Button("RESUME");
        Button exit = new Button("EXIT");
        resume.setOnAction(e -> {
            popup.hide();
        });
        exit.setOnAction(e -> {
            popup.hide();
            primaryStage.getScene().setRoot(mainMenu());
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(resume,exit);
        
        popup.getContent().add(layout);
        popup.show(primaryStage);
        
        
    }
    public void sendStartGameInfo(String roomName){
        client.sendStartInfo(roomName);
    }
    public void sendRoomInfo(String roomName){
        client.sendGameRoomInfo(roomName,player.getPlayerNick());
    }
    public void sendExitRoomInfo(String roomName){
        client.sendExitGameRoomInfo(roomName,player.getPlayerNick(),player.getPlayerName());
    }
    
    public Pane startGame(){
        Button inGameSettings = new Button("S");
        inGameSettings.setOnAction(e -> {
            inGameSettings();
        });
        
        
         
        BorderPane bord = new BorderPane();
        bord.setCenter(gameArea);
        bord.setBottom(inGameSettings);
        
        //mouseUtil = new MouseUtil(game, gameArea, client,player);
        //client.start();
        //latch.countDown();
        while(true){
            try{
                Thread.sleep(10);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            if(client.deckArrived){
            game.startNewGame();
            prepareGameAreaForNewGame();

           
            
            break;
            }
            
        }
        

        return bord;
    }
     Pane multiplayer(){
        if(!client.roomsArrived) client.sendInfo("rooms");
        StringBuilder roomNames = new StringBuilder();
        Label rooms = new Label();
        VBox layout = new VBox(10);
        while(true){
            try{
            Thread.sleep(10);
            }catch(InterruptedException e){
                e.printStackTrace();
            } 
            if(client.roomsArrived){
                roomNames.append(roomList.get(0).getName());
                roomNames.append("\n");
                roomNames.append(roomList.get(1).getName());
                rooms.setText(roomNames.toString());
                roomViewList.forEach(e -> {
                    Button temp = new Button(e.getName());
                    layout.getChildren().add(temp);
                    temp.setOnMouseClicked(ee -> {
                        e.mouseClicked(ee);
                    });
                    
                });
                break;
            }
        }
        /*Button room1 = new Button(roomList.get(0).getName());
        Button room2 = new Button(roomList.get(1).getName());
        room1.setOnAction(e -> {
            roomList.get(0).addPlayer(player);
            //player.setPlayerName(client.getPlayerName());  //
            roomList.get(0).addPlayer(player.getPlayerNick());
            player.setContainingRoom(roomList.get(0));
            gameRoomView = new GameRoomView(roomList.get(0),this);
            player.setContainingGameRoomView(gameRoomView);
            client.sendGameRoomInfo("room1",player.getPlayerNick()); //
            primaryStage.setScene(new Scene(gameRoomView,WIDTH,HEIGHT));
        });
        room2.setOnAction(e -> {
            roomList.get(1).addPlayer(player);
            //primaryStage.setScene(new Scene(roomList.get(1),WIDTH,HEIGHT));           
        });*/
        Button back = new Button("Back");
        Button start = new Button("Start");
        /*start.setOnAction(e -> {
            primaryStage.setScene(startGame());
        });*/
        back.setOnAction(e -> {
            primaryStage.getScene().setRoot(mainMenu());
        });
        Button createRoom = new Button("Create Room");
        createRoom.setOnAction(e -> {
            primaryStage.getScene().setRoot(createRoom());
        });
        Button joinRoom = new Button("Join");
        /*joinRoom.setOnAction(e -> {
            primaryStage.getScene().setRoot(joinRoom(e.getSource()));
        });*/
        layout.getChildren().addAll(back,start,rooms,createRoom,joinRoom);
        layout.setBackground(new Background(new BackgroundImage(new Image("/images/background.png"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        return layout;
        
    }
    
    public void openRoom(GameRoomView gameRoomView){
        primaryStage.getScene().setRoot(gameRoomView);
    }
    
    private Pane createRoom() {
        Button back = new Button("Back");
        Label roomName = new Label("Room Name: ");
        TextField roomNameField = new TextField();
        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();
        Button createButton = new Button("CREATE");
        VBox label = new VBox(10);
        back.setOnAction(e -> {
            primaryStage.getScene().setRoot(mainMenu());
        });
        createButton.setOnAction(e -> {
            //GameRoom room = new GameRoom(roomName.getText(),passwordField.getText(),this);
            
        });
        label.getChildren().addAll(back,roomName,roomNameField,passwordField,passwordLabel,createButton);
        return label;
        
    }
    private Scene joinRoom(Object room){
        return null;
    }
    
    
    private Pane howToPlay(){
        Label info = new Label("How to play goes here...");
        Button back = new Button("Back");
        back.setOnAction(e -> {
            primaryStage.getScene().setRoot(mainMenu());
        });
        Pane pane = new Pane();
        pane.getChildren().addAll(info,back);
        back.setLayoutX(0);
        back.setLayoutY(0);
        info.setLayoutX(WIDTH/2);
        info.setLayoutY(HEIGHT/2);
        pane.setBackground(new Background(new BackgroundImage(new Image("/images/background.png"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        return pane;
        
    }
    
    private Pane options(){
        Button back = new Button("Back");
        back.setOnAction(e -> {
            primaryStage.getScene().setRoot(mainMenu());
        });
        Label displayName = new Label("Display Name: ");
        TextField displayNameField = new TextField();
        displayNameField.setPromptText(player.getPlayerNick());
        Button setName = new Button("OK");
        setName.setOnAction(e -> {
            player.setPlayerNick(displayNameField.getText());
            displayNameField.setPromptText(player.getPlayerNick());
        });
        Label sound = new Label("Sound: ");
        
        
       
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(1.0);
        slider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(javafx.beans.Observable observable) {
                music.setVolume(slider.getValue());
            }
        });
        slider.setValue(music.getVolume());
        Label soundLevel = new Label("%" + slider.getValue());
        soundLevel.textProperty().bind(slider.valueProperty().asString());
           
        
        Label fullScreen = new Label("Full Screen: ");
        Button setFullScreen = new Button("ON");
        setFullScreen.setOnAction(e -> {
            music.stop();
            music = new MediaPlayer(metin2);
            music.setVolume(slider.getValue());
            music.play();
        });
        Label cardTheme = new Label("Card Theme");
        Button selectCardTheme = new Button("Classic");
        Label tableBackground = new Label("Table Background");
        Button selectBackground = new Button("Green");
        VBox layout = new VBox(5);
        layout.setPadding(new Insets(50,50,50,50));
        layout.getChildren().addAll(back,displayName,displayNameField,setName,sound,slider,soundLevel,
                fullScreen,setFullScreen,cardTheme,selectCardTheme,tableBackground,selectBackground);
        layout.setBackground(new Background(new BackgroundImage(new Image("/images/background.png"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        return layout;
        
    }
    
    

    private void prepareGameAreaForNewGame() {
        Iterator<Card> deckIterator = CardDeck.createCardDeckByArray(Client.deckInfo).iterator();

        for (CardPileView pileView : gameArea.getHand0PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        for (CardPileView pileView : gameArea.getHand0PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        for (CardPileView pileView : gameArea.getHand0PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        //

        for (CardPileView pileView : gameArea.getHand1PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        for (CardPileView pileView : gameArea.getHand1PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        for (CardPileView pileView : gameArea.getHand1PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        //
        for (CardPileView pileView : gameArea.getHand2PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        for (CardPileView pileView : gameArea.getHand2PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        for (CardPileView pileView : gameArea.getHand2PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        //
        for (CardPileView pileView : gameArea.getHand3PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        for (CardPileView pileView : gameArea.getHand3PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        for (CardPileView pileView : gameArea.getHand3PileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            gameArea.getChildren().add(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
        }
        //
        for (CardPileView pileView : gameArea.getSlotPileViews()) {
            pileView.addCardView(createCardView(deckIterator.next()));
            gameArea.getChildren().add(pileView.getTopCardView());
            gameArea.cardViewList.add(pileView.getTopCardView());
            mouseUtil.makeDraggable(pileView.getTopCardView());
            pileView.getTopCardView().setMouseTransparent(true);
            pileView.getTopCardView().flip();
        }

        deckIterator.forEachRemaining(card -> {
            gameArea.getDeckPileView().addCardView(createCardView(card));
            mouseUtil.makeDraggable(gameArea.getDeckPileView().getTopCardView());
            gameArea.cardViewList.add(gameArea.getDeckPileView().getTopCardView());
            gameArea.getChildren().add(gameArea.getDeckPileView().getTopCardView());
            gameArea.getDeckPileView().getTopCardView().setMouseTransparent(false);
            gameArea.getDeckPileView().getTopCardView().flip();
        });
        if(player.getPlayerName().equals("player0")){
            for (CardPileView pileView : gameArea.getHand0PileViews()) {
            pileView.getTopCardView().flip();
            pileView.getTopCardView().setMouseTransparent(false);
        }
        }
        else if(player.getPlayerName().equals("player1")){
            for (CardPileView pileView : gameArea.getHand1PileViews()) {
            pileView.getTopCardView().flip();
           // pileView.getTopCardView().setMouseTransparent(false);
        }
        }
        else if(player.getPlayerName().equals("player2")){
            for (CardPileView pileView : gameArea.getHand2PileViews()) {
            pileView.getTopCardView().flip();
            //pileView.getTopCardView().setMouseTransparent(false);
        }
        }
        else if(player.getPlayerName().equals("player3")){
            for (CardPileView pileView : gameArea.getHand3PileViews()) {
            pileView.getTopCardView().flip();
           // pileView.getTopCardView().setMouseTransparent(false);
        }
        }

    }

    private CardView createCardView(Card card) {
        CardView result = new CardView();
        String image = "/card/" + card.getId() + ".png";
        result.setFaceDown(true);
        result.setCard(card);
        result.setFrontFace(new Image(image));
        result.setBackFace(new Image("/card/backFace.png"));
        result.setShortID(card.getId());
        cardViewList.add(result);

        return result;
    }

    public static CardView getCardViewById(String id) {

        return cardViewList.stream().filter(cardView -> cardView.getShortID().equals(id)).findFirst().orElseGet(() -> null);
    }

}

class Client extends Thread {
    private final String IP = "localhost";
    private final int PORT = 5555;
    private List<GameRoom> roomList;
    private List<GameRoomView> roomViewList;
    Main main;
    Player player;
    MouseUtil mouseUtil;
    GameArea gameArea;
    Game game;
    ObjectOutputStream objOut;
    ObjectInputStream objIn;
    CountDownLatch latch;
    static String[] deckInfo = new String[52];
    boolean deckArrived = false;
    boolean roomsArrived = false;
    int roomSize = 0;
    boolean beingPlayed = false;
    Client(GameArea gameArea, Game game,Player player,CountDownLatch latch,
            List<GameRoom> roomList,Main main,List<GameRoomView> roomViewList){
        this.gameArea = gameArea;
        this.game = game;
        this.player = player;
        this.latch = latch;
        this.roomList = roomList;
        this.main = main;
        this.roomViewList = roomViewList;
        mouseUtil = new MouseUtil(game, gameArea, this,player);
        try{
            Socket socket = new Socket(IP, PORT);
            objOut = new ObjectOutputStream(socket.getOutputStream());
            objIn = new ObjectInputStream(socket.getInputStream());           
        }catch(IOException e){
            e.printStackTrace();
        }
        
        
    }

    public void run() {
        
        try {

            
            
            try(final DatagramSocket socket = new DatagramSocket()){
             socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                System.out.println(socket.getLocalAddress().getHostAddress());
            }
            
            while(true){
                String[] roomInfoIn = (String[]) objIn.readObject(); //3
                if(roomInfoIn[0].equals("rooms")){
                    for(int i = 0 ; i<2;i++){
                        GameRoom temp = (GameRoom) objIn.readObject(); // READ ROOMS //
                        roomList.add(temp);
                        roomViewList.add(new GameRoomView(temp, main));
                    }
                    roomsArrived = true; //MULTIPLAYER CONTINUES//
                }
                if(roomInfoIn[0].equals("room1") || roomInfoIn[0].equals("room2")){
                    
                }
                if(roomInfoIn.length > 1 && roomInfoIn[1].equals("start")){
                    System.out.println("start");
                    System.out.println(player.getPlayerName());
                    if(!player.getPlayerName().equals("player5")){
                        System.out.println("yeter");
                        sendStartInfo("start");
                    }
                    Platform.runLater(
                    () -> {
                        main.primaryStage.getScene().setRoot(main.startGame());
                    }
                    );
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println("game started");
                    deckInfo = (String[]) objIn.readObject(); //5
                    deckArrived = true;
                    // ADD SLEEP HERE//
                    beingPlayed = true;
                    while (beingPlayed) {
                        //sendInfo tru mouseUtil
                        processInfo(); //6
                    }
                }
                if(roomInfoIn.length == 5){
                    System.out.println("538");
                    /*for(int i = 1 ; i<5 ; i++){
                        if(player.getContainingRoom().getPlayers2().size() < i){
                            player.getContainingRoom().addPlayer(roomInfoIn[i]);
                        }
                    }*/
                    String playerName = "";
                    if(player.getPlayerName() == null){
                        System.out.println("occ");
                        playerName = objIn.readObject().toString(); //4
                        player.setPlayerName(playerName);
                    }
                        
                        System.out.println("playerName: "+player.getPlayerName());
                    for (String a : roomInfoIn) {
                        System.out.print(a + ", ");
                    }
                    Platform.runLater(
                    () -> {
                        player.getContainingGameRoomView().updateGameRoom(roomInfoIn);
                    }
                    );
                    
                }
            }
            
           /* try{
                latch.await();
            }catch(InterruptedException e){
                e.printStackTrace();
            }*/
           
            
            
            


        } catch (IOException e) {
            System.out.println("problem in while process info ioex");
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            System.out.println("problem in while process info cnfe");
            e.printStackTrace();
        }

    }
    

    public void sendInfo(Card card, CardView cardView, CardPile sourcePile, CardPileView sourcePileView, CardPileView destPileView) {
        
        System.out.println("sending info");
        String[] info = new String[5];
        info[0] = card.getId();
        info[1] = cardView.getShortID();
        info[2] = sourcePile.getId();
        info[3] = sourcePileView.getShortID();
        info[4] = destPileView.getShortID();
        try {
            objOut.reset();
            objOut.writeObject(info);
            objOut.flush();
            System.out.println(info[0]);
            System.out.println(info[1]);
            System.out.println("info sent in main");
        }catch(EOFException e){
            System.out.println("eofexception");
        } 
        catch (IOException e) {
            System.out.println("Problem in sendInfo");
            e.printStackTrace();
        }

    }
    public void sendInfo(String info){
        String[] infoPackage = new String[1];
        infoPackage[0] = info;
        try{
            objOut.writeObject(infoPackage);
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    public void sendGameRoomInfo(String gameRoom,String playerNick){
        String[] info = new String[2];
        info[0] = gameRoom;
        info[1] = playerNick;
        try{
            objOut.writeObject(info);
            objOut.flush();
        }catch(IOException e){
            System.out.println("Problem in send game room info");
            e.printStackTrace();
        }
    }
    public void sendExitGameRoomInfo(String gameRoom,String playerNick,String playerName){
        String[] info = new String[3];
        info[0] = "exit"+gameRoom;
        info[1] = playerNick;
        info[2] = playerName;
        try{
            objOut.writeObject(info);
            objOut.flush();
        }catch(IOException e){
            System.out.println("Problem in send exit game room info");
            e.printStackTrace();
        }
    }

    public void processInfo() {
        String[] info = new String[5];
        System.out.println("processing info");
        try {
            if ((info = (String[]) objIn.readObject()) != null && info.length == 5) {
                System.out.println("Info Received");
            }
        } catch (IOException e) {
            System.out.println("problem in process info ioex");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("problem in process info cnfe");
            e.printStackTrace();
        }
        System.out.println(info[0]);
        Card card = game.getDeck().getById(info[0]);
        CardView cardView = Main.getCardViewById(info[1]);
        CardPile sourcePile = game.getPileById(info[2]);
        CardPileView sourcePileView = gameArea.getPileViewById(info[3]);
        if(info[4].equals("dummy")){
            processInfo2(info);
            return;
        }
        
        CardPileView destPileView = gameArea.getPileViewById(info[4]);
        List<CardView> draggedCardViews = sourcePileView.cardViewsUnder(cardView);
        List<Card> draggedCards = sourcePile.cardsUnder(card);

        if (!mouseUtil.handleValidMove(card, sourcePile, sourcePileView, destPileView, draggedCardViews, draggedCards)) {
            draggedCardViews.forEach(mouseUtil::slideBack);
            //draggedCard=null;
            draggedCardViews = null;
            draggedCards = null;
            return;
        }
        mouseUtil.startTurn(sourcePile, sourcePileView, cardView);

    }

    public void processInfo2(String[] info) {
        
        System.out.println(info[0]);
        Card card = game.getDeck().getById(info[0]);
        CardView cardView = Main.getCardViewById(info[1]);
        CardPile sourcePile = game.getPileById(info[2]);
        CardPileView sourcePileView = gameArea.getPileViewById(info[3]);
        CardPileView destPileView = gameArea.getPileViewById(info[4]);
        List<CardView> draggedCardViews = sourcePileView.cardViewsUnder(cardView);
        List<Card> draggedCards = sourcePile.cardsUnder(card);
        
        if (draggedCards.size() > 1 && !mouseUtil.pilePlayed && (sourcePile.equals(game.getPlayerSlotPiles().get(0)) || sourcePile.equals(game.getPlayerSlotPiles().get(1))
                || sourcePile.equals(game.getPlayerSlotPiles().get(2)) || sourcePile.equals(game.getPlayerSlotPiles().get(3)))) {
            mouseUtil.pilePlayed = true;
            cardView.setLayoutX(cardView.getLayoutX() + 10);
            mouseUtil.startTurn(sourcePile, sourcePileView, cardView);
            return;

        }
    }
    public void sendStartInfo(String roomID){
        String[] startInfo = new String[2];
        startInfo[0] = roomID;
        startInfo[1] = "start";
        
        try{
            objOut.writeObject(startInfo);
            objOut.flush();
        }catch(IOException e){
            System.out.println("problem in send start info");
            e.printStackTrace();
        }
    }
    public String getPlayerName(){
        String name = "";
        try{
            name = objIn.readObject().toString();
        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return name;
    }
}
