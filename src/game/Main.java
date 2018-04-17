package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Card;
import model.CardDeck;
import model.CardPile;
import view.CardPileView;
import view.CardView;

public class Main extends Application {

    private GameArea gameArea;
    private static final double WIDTH = 1872;
    private static final double HEIGHT = 936;
    private Game game;
    private MouseUtil mouseUtil;
    private Client client;
    static List<CardView> cardViewList = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameArea = new GameArea(new Image("/images/background.png"));

        BorderPane bord = new BorderPane();
        bord.setCenter(gameArea);
        Button b1 = new Button("b1");
        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(b1);
        bord.setBottom(hbox);
        b1.setOnAction(e -> {
            Card card = game.getDeck().getById(gameArea.getHand0PileViews().get(0).getTopCardView().getShortID());
            CardView cardView = gameArea.getHand0PileViews().get(0).getTopCardView();
            CardPile sourcePile = game.getHand0Piles().get(0);
            CardPileView sourcePileView = gameArea.getHand0PileViews().get(0);
            CardPileView destPileView = gameArea.getMainPileView();
            List<CardView> draggedCardViews = sourcePileView.cardViewsUnder(cardView);
            List<Card> draggedCards = sourcePile.cardsUnder(card);

            mouseUtil.handleValidMove(card, sourcePile, sourcePileView, destPileView, draggedCardViews, draggedCards);
            mouseUtil.startTurn(sourcePile, sourcePileView, cardView);

        });
        game = new Game();
        
       
        client = new Client(gameArea, game);
        mouseUtil = new MouseUtil(game, gameArea, client);
        client.start();
        while(true){
            Thread.sleep(1000);
            if(client.deckArrived){
            game.startNewGame();
            prepareGameAreaForNewGame();

            Scene scene = new Scene(bord, WIDTH, HEIGHT);

            primaryStage.setTitle("Game");
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setOnCloseRequest(e -> {
                System.exit(0);
            });
            break;
            }
            
        }


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
            pileView.getTopCardView().flip();
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

    MouseUtil mouseUtil;
    GameArea gameArea;
    Game game;
    ObjectInputStream objIn;
    ObjectOutputStream objOut;
    static String[] deckInfo = new String[52];
    boolean deckArrived = false;
    Client(GameArea gameArea, Game game) {
        this.gameArea = gameArea;
        this.game = game;
        mouseUtil = new MouseUtil(game, gameArea, this);
    }

    public void run() {
        try (
                Socket socket = new Socket("localhost", 5555);
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));) {
            objOut = new ObjectOutputStream(socket.getOutputStream());
            objIn = new ObjectInputStream(socket.getInputStream());
            deckInfo = (String[]) objIn.readObject();
            deckArrived = true;
            
            while (true) {
                //sendInfo tru mouseUtil
                processInfo();
            }

        } catch (IOException e) {
            e.getStackTrace();
        }catch(ClassNotFoundException e){
            e.getStackTrace();
        }

    }

    public void sendInfo(Card card, CardView cardView, CardPile sourcePile, CardPileView sourcePileView, CardPileView destPileView) {
        String[] info = new String[5];
        info[0] = card.getId();
        System.out.println(info[0]);
        info[1] = cardView.getShortID();
        System.out.println(info[1]);
        info[2] = sourcePile.getId();
        System.out.println(info[2]);
        info[3] = sourcePileView.getShortID();
        System.out.println(info[3]);
        info[4] = destPileView.getShortID();
        System.out.println(info[4]);
        try {
            objOut.writeObject(info);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void processInfo() {
        String[] info = new String[5];
        try {
            if ((info = (String[]) objIn.readObject()) != null) {
                System.out.println("Info Received");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
}
