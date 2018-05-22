/**
 *
 * @author onur sozer, burak akyol, ahmet karadogan
 */

package game;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import model.Card;
import model.CardDeck;

public class Server {

    private static final int PORT = 5555;
    private static GameRoom room1 = new GameRoom("room1", 1);
    private static GameRoom room2 = new GameRoom("room2", 2);
    static Map<String,Boolean> room1Players = new HashMap();
    static Map<String,Boolean> room2Players = new HashMap();

    static CardDeck deck = CardDeck.createCardDeck();

    public static void main(String[] args) {
        room1Players.put("player0", false);
        room1Players.put("player1", false);
        room1Players.put("player2", false);
        room1Players.put("player3", false);
        room2Players.put("player0", false);
        room2Players.put("player1", false);
        room2Players.put("player2", false);
        room2Players.put("player3", false);
        

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
    private static int room1Size = 0;
    private static int room2Size = 0;
    Socket socket;
    String[] info = new String[5];
    String[] roomInfoIn = new String[2];
    String[] roomInfoOut = new String[5];
    ObjectOutputStream objOut;
    ObjectInputStream objIn;
    CardDeck deck;
    GameRoom containingRoom;
    Player player;
    boolean beingPlayed = false;

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
            while (true) {
                try {
                    roomInfoIn = (String[]) objIn.readObject(); 
                    if (roomInfoIn[0].equals("start")) {
                        Iterator<Card> deckIterator = deck.iterator();      //SEND DECK
                        List<String> deckInfoList = new ArrayList<>();
                        deckIterator.forEachRemaining(card -> {
                            deckInfoList.add(card.getId());
                        });
                        String[] deckInfo = deckInfoList.toArray(new String[52]);

                        sendInfo(deckInfo);
                        beingPlayed = true;
                        while (beingPlayed) {
                            // MOUSE UTIL LOOP //
                            try {
                                info = (String[]) objIn.readObject(); //6
                                if (info[0].equals("message")) {
                                    String msg = info[2] + ": " + info[1];
                                    if (info[1].equals("left")) {
                                        msg = info[2] + " left the room.";
                                    }
                                    if (info[1].equals("joined")) {
                                        msg = info[2] + " joined the room.";
                                    }
                                    info[1] = msg;

                                    room1Protocol.forEach(e -> {
                                        e.sendInfo(info);
                                    });
                                    continue;
                                }
                                if(info[0].equals("stop")) break;
                                clients.forEach(e -> e.sendInfo(info));
                            } catch (EOFException e) {
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                    if(roomInfoIn[0].equals("rooms")){
                        sendInfo("rooms");
                        objOut.writeObject(room1);
                        objOut.writeObject(room2);
                    }
                    if(roomInfoIn.length>1 &&  roomInfoIn[1].equals("start")){
                        roomInfoOut[1] = "start"; 
                        roomInfoOut[0] = roomInfoIn[0];
                        clients.forEach(e -> {
                            e.sendObj(roomInfoIn);
                        });
                        continue;
                    }
                    if (roomInfoIn[0].equals("room1")) {
                        String playerName = "";
                        for(int i = 0 ; i<4 ; i++){
                            if(!Server.room1Players.get("player"+i)){
                                playerName = "player"+i;
                                Server.room1Players.put("player"+i, true);
                                break;
                            } 
                        }
                        
                        containingRoom = room1;
                        room1.addPlayer(roomInfoIn[1]);
                        room1Protocol.add(this);
                        room1Protocol.forEach(e -> {
                            e.sendInfo(updateRoomInfo(roomInfoIn));
                        });
                        sendObj(playerName);
                                                

                    } else if(roomInfoIn[0].equals("exitroom1")){
                        Server.room1Players.put(roomInfoIn[2], false);
                        containingRoom = null;
                        room1.removePlayer(roomInfoIn[1]);
                        roomInfoIn[0] = "room1";
                        room1Protocol.remove(this);
                        room1Protocol.forEach(e -> {
                            e.sendInfo(updateRoomInfo(roomInfoIn));
                        });
                        
                        
                    }
                    else if(roomInfoIn[0].equals("exitroom2")){
                        containingRoom = null;
                        room2.removePlayer(roomInfoIn[1]);
                        room2Protocol.remove(this);
                        roomInfoIn[0] = "room2";
                        updateRoomInfo(roomInfoIn);
                        room2Protocol.forEach(e -> {
                            e.sendInfo(roomInfoOut);
                        });
                        
                    }
                    else if (roomInfoIn[0].equals("room2")) {
                        String playerName = "";
                        for (int i = 0; i < 4; i++) {
                            if (!Server.room2Players.get("player" + i)) {
                                playerName = "player" + i;
                                Server.room2Players.put("player" + i, true);
                                break;
                            }
                        }

                        containingRoom = room2;
                        room2.addPlayer(roomInfoIn[1]);
                        room2Protocol.add(this);
                        room2Protocol.forEach(e -> {
                            e.sendInfo(updateRoomInfo(roomInfoIn));
                        });
                        sendObj(playerName);

                    }
                    if(roomInfoIn[0].equals("message")){
                        String msg = roomInfoIn[2]+ ": " + roomInfoIn[1];
                        if(roomInfoIn[1].equals("left")){
                            msg = roomInfoIn[2]+ " left the room.";
                        }
                        if(roomInfoIn[1].equals("joined")){
                            msg = roomInfoIn[2]+ " joined the room.";
                        }
                        roomInfoIn[1] = msg;
                        
                        room1Protocol.forEach(e -> {
                                e.sendInfo(roomInfoIn);
                            });
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendInfo(String[] info) {    
        try {
            objOut.writeObject(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    public void sendInfo(String info) {
        String[] infoPackage = new String[1];
        infoPackage[0] = info;
        try {
            objOut.writeObject(infoPackage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendObj(Object obj) {             
        try {
            objOut.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String[] updateRoomInfo(String[] roomInfoIn){   
        String[] temp = new String[5];
        temp[0] = roomInfoIn[0];
        if(temp[0].equals("room1")){
            for(int i = 0 ; i<room1.getPlayers2().size();i++){
                temp[i+1] = room1.getPlayers2().get(i);
            }
        }
        for(int i = room1.getPlayers2().size()+1 ; i<5;i++){
            
                temp[i] = "EMPTY";
            
        }
        return temp;
        
    }
}
