/**
 *
 * @author ahmet karadogan,hilal senturk, yaprak bulut
 */


package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Card;
import model.CardDeck;
import view.CardPileView;
import view.CardView;

public class Main extends Application {

    private GameArea gameArea = new GameArea(new Image("/images/background2.jpg"),this);
    private static final double WIDTH = 1920;
    private static final double HEIGHT = 1080;
    private Game game = new Game();
    private Player player = new Player("nonick");
    private static List<GameRoom> roomList = new ArrayList<>();
    private static List<GameRoomView> roomViewList = new ArrayList<>();
    CountDownLatch latch = new CountDownLatch(1);
    private Client client = new Client(gameArea, game, player,latch,roomList,this,roomViewList);
    private MouseUtil mouseUtil = new MouseUtil(game, gameArea, client, player);
    static List<CardView> cardViewList = new ArrayList<>();
    Stage primaryStage;
    Media knight = new Media(new File("knight.mp3").toURI().toString());
    MediaPlayer music = new MediaPlayer(knight);
    String style = getClass().getResource("fxml.css").toExternalForm();

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
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
                System.exit(0);
            });
        
    }

    public Player getPlayer() {
        return player;
    }
    
    public Pane mainMenu(){
        VBox buttons = new VBox(30);
        buttons.setLayoutX(860);
        buttons.setLayoutY(300);
        buttons.getStylesheets().add(style);
        Button startButton = new Button("START");
        
        Button multiplayerButton = new Button("MULTIPLAYER");
        
        Button howToPlayButton = new Button("HOW TO PLAY");
        
        Button optionButton = new Button("OPTIONS");
        
        Button exitButton = new Button("EXIT");
        
        
        startButton.setOnAction(e -> {
            primaryStage.getScene().setRoot(multiplayer());
        });
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
        buttons.getChildren().addAll(startButton,multiplayerButton,howToPlayButton,optionButton,exitButton);
        layout.getChildren().add(buttons);
        layout.setLayoutX(0);
        layout.setLayoutY(0);
        layout.setBackground(new Background(new BackgroundImage(new Image("/images/background.jpg"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));    
        
        return layout;
    }
    public void openRoom(GameRoomView gameRoomView){
        primaryStage.getScene().setRoot(gameRoomView);
    }
    
    private void inGameSettings(){
        Stage popup = new Stage();
        Button resume = new Button("RESUME");
        Button exit = new Button("EXIT");
        resume.setOnAction(e -> {
            popup.close();
        });
        exit.setOnAction(e -> {
            popup.close();
            primaryStage.getScene().setRoot(mainMenu());
        });
        VBox layout = new VBox(10);
        layout.setLayoutX(150);
        layout.setLayoutY(10);
        layout.getChildren().addAll(resume,exit);
        Scene popupScene = new Scene(layout,500,150);
        popup.initModality(Modality.APPLICATION_MODAL);
        layout.getStylesheets().add(style);
        popup.initOwner(primaryStage);
        popup.setScene(popupScene);
        popup.show();
        
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
    public void sendMsg(String msg){
        client.sendMsg(msg,player.getPlayerNick());
    }
    
    public Pane startGame(){
        Button inGameSettings = new Button("S");
        inGameSettings.setOnAction(e -> {
            inGameSettings();
        });
         
        BorderPane bord = new BorderPane();
        bord.setCenter(gameArea);
        bord.setBottom(inGameSettings);
        
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
        Pane layout = new Pane();
        VBox buttons = new VBox(10);
        buttons.getStylesheets().add(style);
        buttons.setLayoutX(860);
        buttons.setLayoutY(300);
        while(true){
            try{
            Thread.sleep(10);
            }catch(InterruptedException e){
                e.printStackTrace();
            } 
            if(client.roomsArrived){
                roomNames.append(roomList.get(0).getName());
                rooms.setText(roomNames.toString());
                GameRoomView room1 = roomViewList.get(0);
                Button room1Button = new Button(room1.getName().toUpperCase());
                buttons.getChildren().add(room1Button);
                room1Button.setOnMouseClicked(e -> room1.mouseClicked(e));
                break;
            }
        }
        Button back = new Button("Back");
        back.setOnAction(e -> {
            primaryStage.getScene().setRoot(mainMenu());
        });
        layout.getChildren().add(buttons);
        buttons.getChildren().addAll(back);
        layout.setBackground(new Background(new BackgroundImage(new Image("/images/background.jpg"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        return layout;
        
    }
    
    
    private Pane howToPlay(){
        Label info = new Label("How to play goes here...");
        Button back = new Button("Back");
        back.setOnAction(e -> {
            primaryStage.getScene().setRoot(mainMenu());
        });
        Pane pane = new Pane();
        pane.getChildren().addAll(info,back);
        back.setLayoutX(305);
        back.setLayoutY(257);
        info.setLayoutX(WIDTH/2);
        info.setLayoutY(HEIGHT/2);
        
        pane.getStylesheets().add(style);
        pane.setBackground(new Background(new BackgroundImage(new Image("/images/background.jpg"),
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
           
        Pane layout = new Pane();
        VBox buttons = new VBox(10);
        layout.getStylesheets().add(style);
        buttons.setLayoutX(305);
        buttons.setLayoutY(257);
        layout.setLayoutX(0);
        layout.setLayoutY(0);
        layout.setPadding(new Insets(50,50,50,50));
        buttons.getChildren().addAll(back,displayName,displayNameField,setName,sound,slider,soundLevel);
        layout.setBackground(new Background(new BackgroundImage(new Image("/images/background.jpg"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        layout.getChildren().add(buttons);
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
        }
        }
        else if(player.getPlayerName().equals("player2")){
            for (CardPileView pileView : gameArea.getHand2PileViews()) {
            pileView.getTopCardView().flip();
        }
        }
        else if(player.getPlayerName().equals("player3")){
            for (CardPileView pileView : gameArea.getHand3PileViews()) {
            pileView.getTopCardView().flip();
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

