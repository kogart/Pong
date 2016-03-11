package sample;


import java.io.*;
import java.net.*;
import javafx.scene.image.Image;
import java.util.*;
import java.util.function.BooleanSupplier;

public class GameServerThread extends Thread {

    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreData = true;
    private File f = new File("indata.txt");
    Ball ball;


    public GameServerThread()throws IOException {
        this("GameServerThread");
    }

    public GameServerThread(String name)throws IOException {
        super(name);

        if(!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
            }

        socket = new DatagramSocket(7537);
        try {
            in = new BufferedReader(new FileReader(f.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Could not open data file.");
        }
    }
        public void run() {

            ball = new Ball(640,360);
            while (moreData) {
                try{
                    byte[] buff = new byte[8];

                    //recive request
                    DatagramPacket packet = new DatagramPacket(buff, buff.length);
                    socket.receive(packet);
                    Boolean gameOn = false;
                    String recived = new String(packet.getData(), 0, packet.getLength());
                    if(recived.equals("0")) gameOn = false;
                    else if(recived.equals("1")) gameOn = true;
                    if(gameOn) {
                        //figure out response
                        ball.ballMove();
                        String ballPos = ball.getCurrentX() + " " + ball.getCurrentY();
                        buff = ballPos.getBytes();
                        System.out.println(ballPos);
                        //send response to the client at ip and port
                        InetAddress address = packet.getAddress();
                        int port = packet.getPort();
                        packet = new DatagramPacket(buff, buff.length, address, port);
                        socket.send(packet);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    moreData=true;
                }
            } socket.close();
        }


    protected String getNextData() {
        String returnValue;
        try {
            if((returnValue = in.readLine()) == null){
                in.close();
                moreData = false;
            }
        } catch (IOException e) {
            returnValue = "IOException occurred in server.";
        }
        return returnValue;
    }


}
