package sample;
import java.io.*;

public class GameServer {
    public static void main(String[] args) throws IOException{
        new GameServerThread().start();
    }
}
