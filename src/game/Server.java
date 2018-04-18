package game;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.Card;
import model.CardDeck;

public class Server {
    static CardDeck deck = CardDeck.createCardDeck();
    public static void main(String[] args) {
        
        try (ServerSocket ss = new ServerSocket(5555)) {
            deck.shuffle();
            while (true) {
                 new GameProtocol(ss.accept(),deck).start();
                
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

}

class GameProtocol extends Thread {
    private static List<GameProtocol> clients = new ArrayList<>();
    Socket socket;
    String[] info = new String[5];
    ObjectOutputStream objOut;
    ObjectInputStream objIn;
    CardDeck deck;

    public GameProtocol(Socket socket,CardDeck deck) {
        this.socket = socket;
        this.deck = deck;
        clients.add(this);
    }

    public void run() {
        try {
            
            
            objOut = new ObjectOutputStream(socket.getOutputStream());
            objIn = new ObjectInputStream(socket.getInputStream());
            String playerName = "player"+(clients.size()-1);
            objOut.writeObject(playerName);
            System.out.println(playerName);
            
            
            Iterator<Card> deckIterator = deck.iterator();
            List<String> deckInfoList = new ArrayList<>();
            deckIterator.forEachRemaining(card -> {
                deckInfoList.add(card.getId());
            });
            String[] deckInfo = deckInfoList.toArray(new String[52]);
            sendInfo(deckInfo);
            while (true) {
                try{
                    info = (String[])objIn.readObject();
                    clients.forEach(e-> e.sendInfo(info));
                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            e.getStackTrace();
        }

    }
    private void sendInfo(String[] info){
        try{
            objOut.writeObject(info);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
