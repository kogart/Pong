package sample;

import java.io.*;



public class GameServer {
    static Pad pad1 = new Pad(20);
    static Pad pad2 = new Pad(1180);
    static Ball ball = new Ball(603, 323 ,pad1, pad2);


    public static void main(String[] args)  throws IOException {
        new GameServerThread(ball, pad1, pad2).start();
    }
}
