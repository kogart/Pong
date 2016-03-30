package sample;

import javafx.application.Application;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main extends Application
{
    Integer ballPosX;
    Integer ballPosY;
    static String adress;
    static String received = "";
    static Pad player1;
    static Pad player2;
    Image ball = new Image("sample/images/ball-cap.png");
    Image pad1 = new Image("sample/images/padPlayer1.png");
    Image pad2 = new Image("sample/images/padPlayer2-can.png");
    static String recData[];
    static DatagramSocket socket = null;
    static double pad2pos;
    int playerPos = 0;
    byte[] buff;
    DatagramPacket packet;

    public static void main(String[] args)throws IOException {
        adress = args[0];
      if (args.length != 1){
            System.out.println("Usage: java Client <hostname>");
            return;
        }

        launch(args);
    }

    @Override
    public void start(Stage theStage) throws SocketException {
        theStage.setTitle( "PONG" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        Canvas canvas = new Canvas( 1280, 720 );
        root.getChildren().add( canvas );

        ArrayList<String> input = new ArrayList<String>();

        theScene.setOnKeyPressed(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();

                        // only add once... prevent duplicates
                        if ( !input.contains(code) )
                            input.add( code );
                    }
                });

        theScene.setOnKeyReleased(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();
                        input.remove( code );
                    }
                });



        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image space = new Image("sample/images/bg.png");


        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );

        final long timeStart = System.currentTimeMillis();


        switch (playerPos){
            case 0: player1 = new Pad(20);
                    player2 = new Pad(1180);
                break;
            case 1: player1 = new Pad(1180);
                    player2 = new Pad(20);
                break;
            default: break;
        }
        socket = new DatagramSocket(0);
        buff = new byte[13];
        packet = new DatagramPacket(buff, buff.length);

        Reciver rec = new Reciver(socket,this,packet);
        rec.start();

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.034),        //         30 FPS
                //Duration.seconds(0.017),        //         60 FPS
                new EventHandler<ActionEvent>()
                {
                    double padPosY = 300;

                    public void handle(ActionEvent ae)
                    {

                        try {
                            //System.out.printf("Sending request\n");

                                //send request

                            switch (playerPos){
                                case 0:  buff = ("0," + Integer.toString(player1.getCurrentPos())).getBytes();
                                    break;
                                case 1:  buff = ("1," + Integer.toString(player1.getCurrentPos())).getBytes();
                                    break;
                                default:
                                    break;
                            }
                                InetAddress address = InetAddress.getByName(adress);
                                packet = new DatagramPacket(buff, buff.length, address, 8768);
                                socket.send(packet);

                                //get response
                                //buff = new byte[13];
                                //packet = new DatagramPacket(buff, buff.length);
                                //socket.receive(packet);

                                //display response
                                //received = new String(packet.getData(),0,packet.getLength());
                                //recData = received.split(",");
                            if (recData != null) {
                                pad2pos = Double.parseDouble(recData[0]);
                                ballPosX = Integer.valueOf(recData[1]);
                                ballPosY = Integer.valueOf(recData[2]);
                            }

                        }catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        if(input.contains("UP")) {
                            player1.moveUp();
                        }

                        if(input.contains("DOWN")) {
                            player1.moveDown();
                        }

                        // Clear the canvas
                        gc.clearRect(0, 0, 512,512);

                        // background image clears canvas
                        gc.drawImage( space, 0, 0 );




                        switch (playerPos){
                            case 0: gc.drawImage( pad1, player1.getStartX(), player1.getCurrentPos() );
                                    gc.drawImage( pad2, player2.getStartX(), pad2pos);
                                break;
                            case 1: gc.drawImage( pad2, player1.getStartX(), player1.getCurrentPos() );
                                    gc.drawImage( pad1, player2.getStartX(), pad2pos);
                                break;
                            default: break;
                        }
                        if(ballPosX != null)
                        gc.drawImage( ball , ballPosX , ballPosY );
                    }
                });
            received = "";
        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();

        theStage.show();
    }
}
class Reciver extends Thread{
    DatagramSocket socket;
    DatagramPacket packet;
    Main main;
    Byte[] buff;

    public Reciver(DatagramSocket socket, Main main, DatagramPacket packet){
        this.socket = socket;
        this.main = main;
        this.packet = packet;
    }

    @Override
    public void run() {
        while (true) {
            buff = new Byte[13];
            try {
                socket.receive(packet);
                main.packet = this.packet;
                main.received = new String(packet.getData(),0,packet.getLength());
                main.recData = main.received.split(",");

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
