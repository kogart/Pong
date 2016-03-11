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

            ball = new Ball(603,323);


            while (moreData) {
                try{
                    byte[] buff = new byte[8];

                    //receive request
                    DatagramPacket packet = new DatagramPacket(buff, buff.length);
                    socket.receive(packet);
                    Boolean gameOn = false;
                    String received = new String(packet.getData(), 0, packet.getLength());
                    if(received.equals("0")) gameOn = false;
                    else if(received.equals("1")) gameOn = true;
                    if(gameOn) {

                        //figure out response
                        try {
                            ball.ballMove();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        String ballPos = ball.getCurrentX() + " " + ball.getCurrentY();
                        buff = ballPos.getBytes();

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
