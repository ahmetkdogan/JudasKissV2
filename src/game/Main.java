package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Card;
import model.CardDeck;
import model.CardPile;
import view.CardPileView;
import view.CardView;

public class Main extends Application {

    private GameArea gameArea = new GameArea(new Image("/images/background.png"));
    private static final double WIDTH = 1872;
    private static final double HEIGHT = 936;
    private Game game = new Game();
    private Player player = new Player("nonick");
    private static int temp = 0;
    private static List<GameRoom> roomList = new ArrayList<>();
    CountDownLatch latch = new CountDownLatch(1);//----/-/-/-/-
    private Client client = new Client(gameArea, game, player,latch,roomList,this);
    private MouseUtil mouseUtil = new MouseUtil(game, gameArea, client, player);
    private final String IP = "localhost";
    private final int PORT = 5555;
    private GameRoomView gameRoomView;
    static List<CardView> cardViewList = new ArrayList<>();
    Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
        primaryStage.setScene(mainMenu());
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
                System.exit(0);
            });
        
        
        
        
    }
    public Scene mainMenu(){
        Button startButton = new Button("START");
        Button multiplayerButton = new Button("MULTIPLAYER");
        Button howToPlayButton = new Button("HOW TO PLAY");
        Button optionButton = new Button("OPTIONS");
        Button exitButton = new Button("EXIT");
        
        /*startButton.setOnAction(e -> {
            primaryStage.setScene(startGame());
        });
        */
        multiplayerButton.setOnAction(e -> {
            primaryStage.setScene(multiplayer());
        });
        howToPlayButton.setOnAction(e -> {
            primaryStage.setScene(howToPlay());
        });
        optionButton.setOnAction(e -> {
            primaryStage.setScene(options());
        });
        exitButton.setOnAction(e -> {
            System.exit(0);
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(startButton,multiplayerButton,howToPlayButton,optionButton,exitButton);
        layout.setLayoutX(WIDTH/2);
        layout.setLayoutY(100);
        
        return new Scene(layout,WIDTH,HEIGHT);
    }
    
    private void inGameSettings(){
        Stage settingStage = new Stage();
        Button resume = new Button("RESUME");
        Button exit = new Button("EXIT");
        resume.setOnAction(e -> {
            settingStage.close();
        });
        exit.setOnAction(e -> {
            primaryStage.setScene(mainMenu());
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(resume,exit);
        Scene settingScene = new Scene(layout,600,300);
        settingStage.setScene(settingScene);
        
        settingStage.setAlwaysOnTop(true);
        settingStage.showAndWait();
        
        
    }
    public void sendStartGameInfo(String roomID){
        client.sendStartInfo(roomID);
    }
    
    public Scene startGame(){
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
                Thread.sleep(2000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            if(client.deckArrived){
            game.startNewGame();
            prepareGameAreaForNewGame();

           
            
            break;
            }
            
        }

        return new Scene(bord,WIDTH,HEIGHT);
    }
    private Scene multiplayer(){
        client.start();
        StringBuilder roomNames = new StringBuilder();
        Label rooms = new Label();
        while(true){
            try{
            Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            } 
            if(client.roomsArrived){
                roomNames.append(roomList.get(0).getName());
                roomNames.append("\n");
                roomNames.append(roomList.get(1).getName());
                rooms.setText(roomNames.toString());
                break;
            }
        }
        Button room1 = new Button(roomList.get(0).getName());
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
        });
        VBox layout = new VBox(10);
        Button back = new Button("Back");
        Button start = new Button("Start");
        /*start.setOnAction(e -> {
            primaryStage.setScene(startGame());
        });*/
        back.setOnAction(e -> {
            primaryStage.setScene(mainMenu());
        });
        Button createRoom = new Button("Create Room");
        createRoom.setOnAction(e -> {
            primaryStage.setScene(createRoom());
        });
        Button joinRoom = new Button("Join");
        joinRoom.setOnAction(e -> {
            primaryStage.setScene(joinRoom(e.getSource()));
        });
        layout.getChildren().addAll(back,start,rooms,room1,room2,createRoom,joinRoom);
        return new Scene(layout,WIDTH,HEIGHT);
        
    }
    
    private Scene createRoom() {
        Button back = new Button("Back");
        Label roomName = new Label("Room Name: ");
        TextField roomNameField = new TextField();
        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();
        Button createButton = new Button("CREATE");
        VBox label = new VBox(10);
        back.setOnAction(e -> {
            primaryStage.setScene(multiplayer());
        });
        createButton.setOnAction(e -> {
            //GameRoom room = new GameRoom(roomName.getText(),passwordField.getText(),this);
            
        });
        label.getChildren().addAll(back,roomName,roomNameField,passwordField,passwordLabel,createButton);
        return new Scene(label,WIDTH,HEIGHT);
        
    }
    private Scene joinRoom(Object room){
        return null;
    }
    
    private Scene howToPlay(){
        Label info = new Label("How to play goes here...");
        Button back = new Button("Back");
        back.setOnAction(e -> {
            primaryStage.setScene(mainMenu());
        });
        Pane pane = new Pane();
        pane.getChildren().addAll(info,back);
        back.setLayoutX(0);
        back.setLayoutY(0);
        info.setLayoutX(WIDTH/2);
        info.setLayoutY(HEIGHT/2);
        return new Scene(pane,WIDTH,HEIGHT);
        
    }
    
    private Scene options(){
        Button back = new Button("Back");
        back.setOnAction(e -> {
            primaryStage.setScene(mainMenu());
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
        Button turnDown = new Button("-");
        Button turnUp = new Button("+");
        Label soundLevel = new Label("%71");
        Label fullScreen = new Label("Full Screen: ");
        Button setFullScreen = new Button("ON");
        Label cardTheme = new Label("Card Theme");
        Button selectCardTheme = new Button("Classic");
        Label tableBackground = new Label("Table Background");
        Button selectBackground = new Button("Green");
        VBox layout = new VBox(5);
        layout.getChildren().addAll(back,displayName,displayNameField,setName,sound,turnDown,soundLevel,turnUp,
                fullScreen,setFullScreen,cardTheme,selectCardTheme,tableBackground,selectBackground);
        return new Scene(layout,WIDTH,HEIGHT);
        
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
            pileView.getTopCardView().setMouseTransparent(false);
        }
        }
        else if(player.getPlayerName().equals("player2")){
            for (CardPileView pileView : gameArea.getHand2PileViews()) {
            pileView.getTopCardView().flip();
            pileView.getTopCardView().setMouseTransparent(false);
        }
        }
        else if(player.getPlayerName().equals("player3")){
            for (CardPileView pileView : gameArea.getHand3PileViews()) {
            pileView.getTopCardView().flip();
            pileView.getTopCardView().setMouseTransparent(false);
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
    Client(GameArea gameArea, Game game,Player player,CountDownLatch latch,List<GameRoom> roomList,Main main) {
        this.gameArea = gameArea;
        this.game = game;
        this.player = player;
        this.latch = latch;
        this.roomList = roomList;
        this.main = main;
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

            
            int roomSize = objIn.readInt(); // 1
            System.out.println("Room Size: "+roomSize);
            
            for(int i = 0 ; i<roomSize;i++){
                roomList.add((GameRoom) objIn.readObject()); //2
            }
            System.out.println("Room1: "+ roomList.get(0));
            System.out.println("Room2: "+ roomList.get(1));
            roomsArrived = true;
            
            String playerName = objIn.readObject().toString(); //4
            player.setPlayerName(playerName);
            System.out.println(player.getPlayerName());
            
            while(true){
                String[] roomInfoIn = (String[]) objIn.readObject(); //3
                System.out.println("Room info: "+roomInfoIn[1]);
                if(roomInfoIn[1].equals("start")){
                    System.out.println("start");
                    System.out.println(player.getPlayerName());
                    if(!player.getPlayerName().equals("player0")){
                        System.out.println("yeter");
                        sendStartInfo("start");
                    }
                    Platform.runLater(
                    () -> {
                        main.primaryStage.setScene(main.startGame());
                    }
                    );
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println("game started");
                    break;
                }
                else{
                   // player.getContainingGameRoomView().updateGameRoom();
                }
            }
            
           /* try{
                latch.await();
            }catch(InterruptedException e){
                e.printStackTrace();
            }*/
           
            
            
            
            deckInfo = (String[]) objIn.readObject(); //5
            deckArrived = true;
            // ADD SLEEP HERE//
            while (true) {
                //sendInfo tru mouseUtil
                processInfo(); //6
            }

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
    public void sendGameRoomInfo(String player,String gameRoom){
        String[] info = new String[2];
        info[0] = gameRoom;
        info[1] = player;
        try{
            objOut.writeObject(info);
            objOut.flush();
        }catch(IOException e){
            System.out.println("Problem in send game room info");
            e.printStackTrace();
        }
    }

    public void processInfo() {
        String[] info = new String[5];
        System.out.println("processing info");
        try {
            if ((info = (String[]) objIn.readObject()) != null) {
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
