package sample;


import java.io.*;
import java.net.*;


public class GameServerThread extends Thread {

    protected DatagramSocket socket;
    protected BufferedReader in = null;
    protected boolean moreData = true;
    static Ball ball;
    static Pad pad1;
    static Pad pad2;

    public GameServerThread()throws IOException {
        socket = new DatagramSocket(7537);
        socket.setBroadcast(true);
    }

        public void run() {

            ball = new Ball(603, 323);
            pad1 = new Pad(20);
            pad2 = new Pad(1180);
            InetAddress adress1 = null;
            InetAddress adress2 = null;
            int port1 = 0;
            int port2 = 0;
            while (true) {

                try {
                    byte[] buff = new byte[8];
                    //receive request
                    DatagramPacket player = new DatagramPacket(buff, buff.length);
                    socket.receive(player);

                    if(adress1 == null || adress2 == null) {
                        if (adress1 == null) {
                            adress1 = player.getAddress();
                        } else if (adress1 != player.getAddress()) {
                            adress2 = player.getAddress();
                        }
                    }
                    if(port1 == 0 || port2 == 0) {
                        if (port1 == 0) {
                            port1 = player.getPort();
                        } else if (port1 != player.getPort()) {
                            port2 = player.getPort();
                        }
                    }


                    String received = new String(player.getData(), 0, player.getLength());
                    int pad1Pos = Integer.valueOf(received);
                    pad1.setCurrentPos(pad1Pos);

                    if (adress1 != null && adress2 != null) {

                        //figure out response
                        ball.ballMove();

                        String ballPos = ball.getCurrentX() + " " + ball.getCurrentY();
                        buff = ballPos.getBytes();

                        //send response to the client at ip and port
                        player = new DatagramPacket(buff, buff.length, adress1 , port1);
                        socket.send(player);
                        player.setAddress(adress2);
                        player.setPort(port2);
                        socket.send(player);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                //socket.close();
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
