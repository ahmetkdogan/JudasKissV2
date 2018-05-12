package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class GameRoom implements Serializable {
    private String name;
    private String roomPassword;
    private int roomID;
    private static int totalRooms = 0;
    private List<Player> players;
    private List<String> players2;
    private int totalPlayers = 0;
    
    public GameRoom(String name,int roomID){
        totalRooms++;
        this.name=name;
        this.roomID = roomID;
        players = new ArrayList<>();
        players2 = new ArrayList<>();
    }
    public void addPlayer(Player player){
        totalPlayers++;
        players.add(player);
    }
    
    public void addPlayer(String player){
        totalPlayers++;
        players2.add(player);
    }
    public void removePlayer(String player){
        totalPlayers--;
        players2.remove(player);
    }
    public void removePlayer(Player player){
        players.remove(player);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public static int getTotalRooms() {
        return totalRooms;
    }

    public static void setTotalRooms(int totalRooms) {
        GameRoom.totalRooms = totalRooms;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<String> getPlayers2() {
        return players2;
    }

    public void setPlayers2(List<String> players2) {
        this.players2 = players2;
    }

}
