package sample;



import java.io.*;
import java.net.*;


public class GameServerThread extends Thread {

    static DatagramSocket socket;
    static Ball ball;
    static Pad pad1;
    static Pad pad2;
    static byte[] buff = new byte[13];
    static InetAddress adress1 = null;
    static InetAddress adress2 = null;
    static int port1 = 0;
    static int port2 = 0;
    static int pad1Pos;
    static int pad2Pos;
    boolean player1Done = false;
    boolean player2Done = false;
    ClientThread Player1;
    ClientThread Player2;

    DatagramPacket player = new DatagramPacket(buff, buff.length);
    String[] recarr1;
    String[] recarr2;
    public GameServerThread(Ball ball, Pad pad1, Pad pad2)throws IOException {
        Player1 = new ClientThread(8768);
        Player2 = new ClientThread(7537);
        this.ball = ball;
        this.pad1 = pad1;
        this.pad2 = pad2;
    }

        public void run() {

            while (true) {
                try {
                    //receive request

                        Player1.run();
                        Player2.run();
                        if(Player1.getRecString() != null) {
                            recarr1 = Player1.getRecString().split(",");
                            pad1.setCurrentPos(Integer.parseInt(recarr1[1]));
                        }
                        if(Player2.getRecString() != null) {
                            recarr2 = Player2.getRecString().split(",");
                            pad2.setCurrentPos(Integer.parseInt(recarr2[1]));
                        }
                        if (recarr1 != null)
                            setAddressAndPort(Player1.getPacket(),recarr1);

                        if (recarr2 != null)
                            setAddressAndPort(Player2.getPacket(),recarr2);






                        //figure out response
                        ball.ballMove();

                        String ballPos = ball.getCurrentX() + "," + ball.getCurrentY();

                        buff =(Integer.valueOf(pad2.getCurrentPos()) + "," + ballPos).getBytes();
                        //send response to the client at ip and port
                        player = new DatagramPacket(buff, buff.length, adress1, port1);
                        socket = Player1.getSocket();
                        Player1.sendPack(player);


                        buff = (Integer.valueOf(pad1.getCurrentPos()) + "," + ballPos).getBytes();
                        player = new DatagramPacket(buff, buff.length, adress2, port2);
                        socket = Player2.getSocket();
                        Player2.sendPack(player);


                    }catch(InterruptedException e1){
                        e1.printStackTrace();
                    }catch(IOException e1){
                        e1.printStackTrace();}
                }
            }

    private static void setAddressAndPort(DatagramPacket player, String[] recarr){
        String posPlayer = recarr[0];

        switch (posPlayer){
            case "0":
                adress1 = player.getAddress();
                port1 = player.getPort();
                break;
            case "1":
                adress2 = player.getAddress();
                port2 = player.getPort();
                break;
            default: break;

        }

    }
}


class ClientThread extends Thread
{
    DatagramSocket socket;
    DatagramPacket recPack;
    byte[] buff = new byte[13];
    String recString;
    InetAddress address;
    int port;

    public ClientThread(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
        recPack = new DatagramPacket(buff, 0, buff.length);
    }
    @Override
    public void run(){
    try {
        socket.receive(recPack);
        address = recPack.getAddress();
        port = recPack.getPort();

        recString = new String(recPack.getData(), 0, recPack.getLength());
    } catch (IOException e) {
        e.printStackTrace();
    }

        }


    public String getRecString(){
        return recString;
    }
    public DatagramPacket getPacket(){
        return recPack;
    }
    public DatagramSocket getSocket(){
        return socket;
    }
    public void sendPack(DatagramPacket packet) throws IOException {
        packet.setPort(port);
        packet.setAddress(address);
        socket.send(packet);
    }
}


