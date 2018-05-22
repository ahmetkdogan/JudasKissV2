/**
 *
 * @author ahmet karadogan, onur sozer, burak akyol
 */
package game;

import game.Game;
import game.GameArea;
import game.GameRoom;
import game.GameRoomView;
import game.Main;
import game.MouseUtil;
import game.Player;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import model.Card;
import model.CardPile;
import view.CardPileView;
import view.CardView;

public class Client extends Thread {
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
                String[] roomInfoIn = (String[]) objIn.readObject(); 
                if(roomInfoIn[0].equals("rooms")){
                    for(int i = 0 ; i<2;i++){
                        GameRoom temp = (GameRoom) objIn.readObject(); // READ ROOMS //
                        roomList.add(temp);
                        roomViewList.add(new GameRoomView(temp, main));
                    }
                    roomsArrived = true; //MULTIPLAYER CONTINUES//
                }
                if(roomInfoIn.length > 1 && roomInfoIn[1].equals("start")){
                    if(!player.getPlayerName().equals("player5")){
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
                    deckInfo = (String[]) objIn.readObject();
                    deckArrived = true;
                    beingPlayed = true;
                    while (beingPlayed) {
                        //sendInfo tru mouseUtil
                        
                        processInfo(); 
                        
                    }
                }
                if(roomInfoIn.length == 5){
                    String playerName = "";
                    if(player.getPlayerName() == null){
                        playerName = objIn.readObject().toString();
                        player.setPlayerName(playerName);
                    }
                    Platform.runLater(
                    () -> {
                        player.getContainingGameRoomView().updateGameRoom(roomInfoIn);
                    }
                    );
                    
                }
                if(roomInfoIn[0].equals("message")){
                    Platform.runLater(
                    () -> {
                        player.getContainingGameRoomView().addMsg(roomInfoIn[1]);
                    }
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }
    

    public void sendInfo(Card card, CardView cardView, CardPile sourcePile, CardPileView sourcePileView, CardPileView destPileView) {
        
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
        }catch(EOFException e){
        } 
        catch (IOException e) {
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
    public void sendMsg(String msg,String nick){
        String[] msgPackage = new String[3];
        msgPackage[0] = "message";
        msgPackage[1] = msg;
        msgPackage[2] = nick;
        try{
            objOut.writeObject(msgPackage);
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
            e.printStackTrace();
        }
    }

    public void processInfo() {
       
        String[] info = new String[5];
        try {
            if ((info = (String[]) objIn.readObject()) != null) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(info[0].equals("message")){
            String msg = info[1];
            Platform.runLater(
                    () -> {
                        gameArea.addMsg(msg);
                    }
            );
            return;
        }
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
            try {
                Thread.sleep(160);
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            Platform.runLater(
                    () -> {
                        gameArea.updatePlayerPoint();
                    }
            );
            return;
        }
        try {
            Thread.sleep(160);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        Platform.runLater(
                () -> {
                    gameArea.updatePlayerPoint();
                }
        );
        mouseUtil.startTurn(sourcePile, sourcePileView, cardView);
         

    }

    public void processInfo2(String[] info) {
        
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
