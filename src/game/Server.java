package game;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.Card;
import model.CardDeck;
import javafx.scene.control.*;

public class Server {

    private static final int PORT = 5555;
    private static GameRoom room1 = new GameRoom("room1", 1);
    private static GameRoom room2 = new GameRoom("room2", 2);

    static CardDeck deck = CardDeck.createCardDeck();

    public static void main(String[] args) {

        try (ServerSocket ss = new ServerSocket(PORT)) {
            deck.shuffle();
            while (true) {
                new GameProtocol(ss.accept(), deck, room1, room2).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class GameProtocol extends Thread {

    private static List<GameProtocol> clients = new ArrayList<>();
    private static GameRoom room1;
    private static GameRoom room2;
    private static List<GameProtocol> room1Protocol = new ArrayList<>();
    private static List<GameProtocol> room2Protocol = new ArrayList<>();
    private static int temp = 0;
    Socket socket;
    String[] info = new String[5];
    String[] roomInfoIn = new String[2];
    String[] roomInfoOut = new String[5];
    ObjectOutputStream objOut;
    ObjectInputStream objIn;
    CardDeck deck;
    GameRoom containingRoom;
    Player player;

    public GameProtocol(Socket socket, CardDeck deck, GameRoom room1, GameRoom room2) {
        this.socket = socket;
        this.deck = deck;
        this.room1 = room1;
        this.room2 = room2;
        try{
            objOut = new ObjectOutputStream(socket.getOutputStream());
            objIn = new ObjectInputStream(socket.getInputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
        clients.add(this);
    }

    public void run() {
        try {

            

            objOut.writeInt(2); //1 

            try {
                objOut.writeObject(room1);//2
                objOut.writeObject(room2);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
                        //SEND PLAYER NAME
                String playerName = "player"+temp;
                sendObj(playerName);
                temp++;
                System.out.println(playerName);
            

            while (true) {
                try {
                    roomInfoIn = (String[]) objIn.readObject(); //3       //UPDATE ROOMS
                    if(roomInfoIn[0].equals("start")) break;
                    if(roomInfoIn[1].equals("start")){
                        roomInfoOut[1] = "start"; 
                        roomInfoOut[0] = roomInfoIn[0];
                        System.out.println("start");
                        clients.forEach(e -> {
                            System.out.println("start sent");
                            e.sendObj(roomInfoIn);
                        });
                        break;
                    }
                    containingRoom = roomInfoIn[1].equals("room1") ? room1:room2;
                    if (roomInfoIn[0].equals("room1")) {
                        room1.addPlayer(roomInfoIn[1]);
                        room1Protocol.add(this);
                        updateRoomInfo(roomInfoOut,roomInfoIn);
                        clients.forEach(e -> {
                            e.sendInfo(roomInfoOut);
                        });

                    } else if (room2.equals(roomInfoIn[0])) {
                        room2.addPlayer(roomInfoIn[1]);
                        room2Protocol.add(this);
                        updateRoomInfo(roomInfoOut, roomInfoIn);
                        room2Protocol.forEach(e -> {
                            e.sendInfo(roomInfoOut);
                        });
                        
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            
            

            Iterator<Card> deckIterator = deck.iterator();      //SEND DECK
            List<String> deckInfoList = new ArrayList<>();
            deckIterator.forEachRemaining(card -> {
                deckInfoList.add(card.getId());
            });
            String[] deckInfo = deckInfoList.toArray(new String[52]); 
           
               sendInfo(deckInfo);
            
            while (true) {
                // MOUSE UTIL LOOP //
               try {
                    info = (String[]) objIn.readObject(); //6
                    System.out.println("info received");
                    clients.forEach(e -> e.sendInfo(info));
                }catch(EOFException e){
                    System.out.println("eofexception");
                } 
               catch (ClassNotFoundException e) {
                    System.out.println("problem in 6");
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            System.out.println("problem in outer 6");
            e.printStackTrace();
        }

    }

    private void sendInfo(String[] info) {      //SEND INFO
        try {
            objOut.writeObject(info);
        } catch (IOException e) {
            System.out.println("problem in send info");
            e.printStackTrace();
        }
    }

    private void sendObj(Object obj) {              //SEND OBJECT
        try {
            objOut.writeObject(obj);
        } catch (IOException e) {
            System.out.println("problem in server send obj");
            e.printStackTrace();
        }
    }
    private void updateRoomInfo(String[] roomInfoOut,String[] roomInfoIn){          //UPDATE ROOM INFO
        roomInfoOut[0] = roomInfoIn[0];
        if(roomInfoOut[0].equals("room1")){
            for(int i = 0 ; i<room1.getPlayers2().size();i++){
                roomInfoOut[i+1] = room1.getPlayers2().get(i);
            }
        }
        for(int i = 0 ; i<roomInfoOut.length;i++){
            if(roomInfoOut[i]==null){
                roomInfoOut[i] = "EMPTY";
            }
        }
        
    }
}
