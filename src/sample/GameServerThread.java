package sample;

import java.io.*;
import java.net.*;

public class GameServerThread extends Thread {

    Ball ball;
    Pad pad1;
    Pad pad2;
    byte[] buff = new byte[13];
    SocketThread socketThread;

    public GameServerThread(Ball ball, Pad pad1, Pad pad2) throws IOException {
        this.ball = ball;
        this.pad1 = pad1;
        this.pad2 = pad2;
        socketThread = new SocketThread(this, 8768);
    }

        public void run() {
            socketThread.start();

            while (true) {
                try {
                        //figure out response
                        ball.ballMove();

                        String ballPos = ball.getCurrentX() + "," + ball.getCurrentY();

                        buff =(Integer.valueOf(pad2.getCurrentPos()) + "," + ballPos).getBytes();
                        //send response to the client at ip and port
                        socketThread.sendToPlayer1(buff);
                        buff = (Integer.valueOf(pad1.getCurrentPos()) + "," + ballPos).getBytes();
                        socketThread.sendToPlayer2(buff);
                    }catch(InterruptedException e1){
                        e1.printStackTrace();
                    }catch(IOException e1){
                        e1.printStackTrace();}
                }
            }

    private class SocketThread extends Thread {
        private DatagramSocket socket;
        private Player[] players = new Player[2];
        private GameServerThread gameServerThread;

        public SocketThread(GameServerThread gameServerThread, int port) throws SocketException {
            this.gameServerThread = gameServerThread;
            this.socket = new DatagramSocket(port);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    byte[] buff = new byte[13];
                    DatagramPacket recPack = new DatagramPacket(buff, 0, buff.length);
                    socket.receive(recPack);
                    SocketAddress address = recPack.getSocketAddress();
                    System.out.printf("<- [%s] %d bytes\n",
                            address,
                            recPack.getLength());

                    for (int i = 0; i < 2; i++) {
                        if (players[i] == null) {
                            // Add to players
                            players[i] = new Player(address);
                            System.out.printf("Assigning as player #%d\n", i);
                        }
                        if (players[i].address.equals(address)) {
                            String received = new String(recPack.getData(), 0, recPack.getLength());
                            System.out.printf("Player #%d sent us %s\n", i, received);
                            String[] recarr = received.split(",");
                            int position = Integer.parseInt(recarr[1]);
                            if (i == 0) {
                                gameServerThread.pad1.setCurrentPos(position);
                            } else {
                                gameServerThread.pad2.setCurrentPos(position);
                            }
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendToPlayer1(byte[] data) throws IOException {
            sendToPlayer(0, data);
        }

        public void sendToPlayer2(byte[] data) throws IOException {
            sendToPlayer(1, data);
        }

        public void sendToPlayer(int player, byte[] data) throws IOException {
            if (players[player] != null) {
                DatagramPacket packet = new DatagramPacket(data, data.length);
                packet.setSocketAddress(players[player].address);
                sendPack(packet);
            }
        }

        public void sendPack(DatagramPacket packet) throws IOException {
            socket.send(packet);
        }
    }

    private class Player {
        boolean ready = false;
        SocketAddress address;

        public Player(SocketAddress address) {
            this.address = address;
        }
    }

}
