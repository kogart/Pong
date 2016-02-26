package sample;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServerThread extends Thread {

    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreData = true;
    private File f = new File("indata.txt");
    private String ballPos;


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

        socket = new DatagramSocket(4455);
        try {
            in = new BufferedReader(new FileReader(f.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Could not open data file.");
        }
    }
        public void run() {
            while (moreData) {
                try{
                    byte[] buff = new byte[256];

                    //recive request
                    DatagramPacket packet = new DatagramPacket(buff, buff.length);
                    socket.receive(packet);

                    //figure out response
                    buff = ballPos.getBytes();

                    //send response to the client at ip and port
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    packet = new DatagramPacket(buff,buff.length,address,port);
                    socket.send(packet);

                }catch (IOException e){
                    e.printStackTrace();
                    moreData=false;
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
