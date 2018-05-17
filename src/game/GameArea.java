package game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import view.CardPileView;
import view.CardView;



public class GameArea extends Pane{
    
    List<CardView> cardViewList = new ArrayList<>();
    private List<CardPileView> slotPileViews;
    private List<CardPileView> playerSlotPileViews;
    private Label player0Point;
    private Label player1Point;
    private Label player2Point;
    private Label player3Point;
    private List<CardPileView> hand0PileViews;
    private List<CardPileView> hand1PileViews;
    private List<CardPileView> hand2PileViews;
    private List<CardPileView> hand3PileViews;
    private CardPileView mainPileView;
    private CardPileView deckPileView;
    private TextArea ta;
    private TextField tf;
    private Main main;
    
    public void setTurn(String a){
        addMsg(a);
    }
    public GameArea(){
        this.slotPileViews = FXCollections.observableArrayList();
        this.playerSlotPileViews = FXCollections.observableArrayList();
        this.hand0PileViews = FXCollections.observableArrayList();
        this.hand1PileViews = FXCollections.observableArrayList();
        this.hand2PileViews = FXCollections.observableArrayList();
        this.hand3PileViews = FXCollections.observableArrayList();
        this.player0Point = new Label("0");
        this.player1Point = new Label("0");
        this.player2Point = new Label("0");
        this.player3Point = new Label("0");
        this.mainPileView = new CardPileView(2,"M");
        this.deckPileView = new CardPileView(2,"D");
        initGameArea();
    }
    public GameArea(Image tableBackground,Main main){
        this();
        this.main = main;
        setTableauBackground(tableBackground);
    }
    
    private void initGameArea(){
        buildSlotPiles();
        buildPlayerSlotPiles();
        buildPlayerPoints();
        buildHand0Piles();
        buildHand1Piles();
        buildHand2Piles();
        buildHand3Piles();
        buildMainPile();
        buildDeckPile();
        chatArea();
    }
    public void buildPlayerPoints(){
        player0Point.setLayoutX(900);
        player0Point.setLayoutY(650);
        getChildren().add(player0Point);
        
        player1Point.setLayoutX(1548);
        player1Point.setLayoutY(394);
        getChildren().add(player1Point);
        
        player2Point.setLayoutX(900);
        player2Point.setLayoutY(170);
        getChildren().add(player2Point);
        
        player3Point.setLayoutX(180);
        player3Point.setLayoutY(394);
        getChildren().add(player3Point);
        
//        playerPoints.get(3).setLayoutX(180);
//        playerPoints.get(3).setLayoutY(394);
//        getChildren().add(playerPoints.get(3));

    }
    
    public void updatePlayerPoint(){
           
                player0Point.setText(playerSlotPileViews.get(0).getTotalPoint()+"");
                player1Point.setText(playerSlotPileViews.get(1).getTotalPoint()+"");
                player2Point.setText(playerSlotPileViews.get(2).getTotalPoint()+"");
                player3Point.setText(playerSlotPileViews.get(3).getTotalPoint()+"");
       
    }
    
    public void chatArea(){
        ta = new TextArea();
        ta.setPrefSize(450, 200);
        tf = new TextField();
        tf.setPrefWidth(450);
        tf.setOnAction(e -> {
            main.sendMsg(tf.getText().toString());
            tf.setText("");
        });
        VBox chatLayout = new VBox(5);
        chatLayout.getChildren().addAll(ta,tf);
        chatLayout.setLayoutX(72);
        chatLayout.setLayoutY(800);
        this.getChildren().add(chatLayout);
    }
    public void addMsg(String msg){
        ta.appendText(msg + "\n");
    }

    public CardPileView getDeckPileView() {
        return deckPileView;
    }

    public void setDeckPileView(CardPileView deckPileView) {
        this.deckPileView = deckPileView;
    }
    
    private void buildDeckPile(){
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.gray(0.0,0.2),null,null);
        
        Background background = new Background(backgroundFill);
        
        
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        deckPileView.setPrefSize(72, 96);
        deckPileView.setBackground(background);
        deckPileView.setLayoutX(1260);
        deckPileView.setLayoutY(64);
        deckPileView.setEffect(gaussianBlur);
        getChildren().add(deckPileView);
        
    }
    
    private void buildMainPile(){
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.gray(0.0,0.2),null,null);
        
        Background background = new Background(backgroundFill);
        
        
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        mainPileView.setPrefSize(72, 96);
        mainPileView.setBackground(background);
        mainPileView.setLayoutX(900);
        mainPileView.setLayoutY(350);
        mainPileView.setEffect(gaussianBlur);
        getChildren().add(mainPileView);
        
    }
    
    private void buildHand0Piles(){
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.gray(0.0,0.2),null,null);
        
        Background background = new Background(backgroundFill);
        
        
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        
        IntStream.range(0, 4).forEach(i -> {
            hand0PileViews.add(new CardPileView(2,"P0H"+i));
            hand0PileViews.get(i).setPrefSize(72, 96);
            hand0PileViews.get(i).setBackground(background);
            hand0PileViews.get(i).setLayoutX(684 + i*144);
            hand0PileViews.get(i).setLayoutY(800);
            hand0PileViews.get(i).setEffect(gaussianBlur);
            getChildren().add(hand0PileViews.get(i));
        });
    }
    private void buildHand1Piles(){
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.gray(0.0,0.2),null,null);
        
        Background background = new Background(backgroundFill);
        
        
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        
        IntStream.range(0, 4).forEach(i -> {
            hand1PileViews.add(new CardPileView(2,"P1H"+i));
            hand1PileViews.get(i).setPrefSize(72, 96);
            hand1PileViews.get(i).setBackground(background);
            hand1PileViews.get(i).setLayoutX(1728);
            hand1PileViews.get(i).setLayoutY(160+ i*160);
            hand1PileViews.get(i).setEffect(gaussianBlur);
            getChildren().add(hand1PileViews.get(i));
        });
    }
    private void buildHand2Piles(){
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.gray(0.0,0.2),null,null);
        
        Background background = new Background(backgroundFill);
        
        
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        
        IntStream.range(0, 4).forEach(i -> {
            hand2PileViews.add(new CardPileView(2,"P2H"+i));
            hand2PileViews.get(i).setPrefSize(72, 96);
            hand2PileViews.get(i).setBackground(background);
            hand2PileViews.get(i).setLayoutX(684 + i * 144);
            hand2PileViews.get(i).setLayoutY(64);
            hand2PileViews.get(i).setEffect(gaussianBlur);
            getChildren().add(hand2PileViews.get(i));
        });
    }
    private void buildHand3Piles(){
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.gray(0.0,0.2),null,null);
        
        Background background = new Background(backgroundFill);
        
        
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        
        IntStream.range(0, 4).forEach(i -> {
            hand3PileViews.add(new CardPileView(2,"P3H"+i));
            hand3PileViews.get(i).setPrefSize(72, 96);
            hand3PileViews.get(i).setBackground(background);
            hand3PileViews.get(i).setLayoutX(72);
            hand3PileViews.get(i).setLayoutY(160+ i*160);
            hand3PileViews.get(i).setEffect(gaussianBlur);
            getChildren().add(hand3PileViews.get(i));
        });
    }
    private void buildSlotPiles(){
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.gray(0.0,0.2),null,null);
        
        Background background = new Background(backgroundFill);
        
        
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        
        IntStream.range(0, 4).forEach(i -> {
            slotPileViews.add(new CardPileView(2,"S"+i));
            slotPileViews.get(i).setPrefSize(72, 96);
            slotPileViews.get(i).setBackground(background);
            slotPileViews.get(i).setLayoutX(684 + i*144);
            slotPileViews.get(i).setLayoutY(500);
            slotPileViews.get(i).setEffect(gaussianBlur);
            getChildren().add(slotPileViews.get(i));
        });
    }
    private void buildPlayerSlotPiles(){
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.gray(0.0,0.2),null,null);
        
        Background background = new Background(backgroundFill);
        
        
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        
        
        IntStream.range(0, 4).forEach(i -> {
            playerSlotPileViews.add(new CardPileView(2,"P"+i));
            playerSlotPileViews.get(i).setPrefSize(72, 96);
            playerSlotPileViews.get(i).setBackground(background);
            playerSlotPileViews.get(i).setEffect(gaussianBlur);
        });
        playerSlotPileViews.get(0).setLayoutX(900);
        playerSlotPileViews.get(0).setLayoutY(672);
        getChildren().add(playerSlotPileViews.get(0));
        playerSlotPileViews.get(1).setLayoutX(1548);
        playerSlotPileViews.get(1).setLayoutY(416);
        getChildren().add(playerSlotPileViews.get(1));
        playerSlotPileViews.get(2).setLayoutX(900);
        playerSlotPileViews.get(2).setLayoutY(192);
        getChildren().add(playerSlotPileViews.get(2));
        playerSlotPileViews.get(3).setLayoutX(180);
        playerSlotPileViews.get(3).setLayoutY(416);
        getChildren().add(playerSlotPileViews.get(3));
    }
    
    
    

    public List<CardView> getCardViewList() {
        return cardViewList;
    }

    public void setCardViewList(List<CardView> cardViewList) {
        this.cardViewList = cardViewList;
    }

    public List<CardPileView> getSlotPileViews() {
        return slotPileViews;
    }

    public void setSlotPileViews(List<CardPileView> slotPileViews) {
        this.slotPileViews = slotPileViews;
    }

    public List<CardPileView> getPlayerSlotPileViews() {
        return playerSlotPileViews;
    }

    public void setPlayerSlotPileViews(List<CardPileView> playerSlotPileViews) {
        this.playerSlotPileViews = playerSlotPileViews;
    }

    public List<CardPileView> getHand0PileViews() {
        return hand0PileViews;
    }

    public void setHand0PileViews(List<CardPileView> hand0PileViews) {
        this.hand0PileViews = hand0PileViews;
    }

    public List<CardPileView> getHand1PileViews() {
        return hand1PileViews;
    }

    public void setHand1PileViews(List<CardPileView> hand1PileViews) {
        this.hand1PileViews = hand1PileViews;
    }

    public List<CardPileView> getHand2PileViews() {
        return hand2PileViews;
    }

    public void setHand2PileViews(List<CardPileView> hand2PileViews) {
        this.hand2PileViews = hand2PileViews;
    }

    public List<CardPileView> getHand3PileViews() {
        return hand3PileViews;
    }

    public void setHand3PileViews(List<CardPileView> hand3PileViews) {
        this.hand3PileViews = hand3PileViews;
    }

    

    public CardPileView getMainPileView() {
        return mainPileView;
    }

    public void setMainPileView(CardPileView mainPileView) {
        this.mainPileView = mainPileView;
    }
    public CardPileView getPileViewById(String id){
        CardPileView result;
        
        result = slotPileViews.stream()
                .filter(pileView -> pileView.getShortID().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        
        result = playerSlotPileViews.stream()
                .filter(pileView -> pileView.getShortID().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        
        result = hand0PileViews.stream()
                .filter(pileView -> pileView.getShortID().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        result = hand1PileViews.stream()
                .filter(pileView -> pileView.getShortID().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        result = hand2PileViews.stream()
                .filter(pileView -> pileView.getShortID().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        result = hand3PileViews.stream()
                .filter(pileView -> pileView.getShortID().equals(id)).findFirst().orElse(null);
        if(result != null) return result;
        
        if(mainPileView.getShortID().equals(id)) return mainPileView;
        if(deckPileView.getShortID().equals(id)) return deckPileView;
        System.out.println("PileView not found");
        return null;
        
    }

    
    
    public void setTableauBackground(Image tableauBackground){
        setBackground(new Background(new BackgroundImage(tableauBackground,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    }
    
    
    
    
   
}
