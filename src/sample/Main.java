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

        // Create player pad
        player1 = new Pad(20);

        // Create opponent pad
        player2 = new Pad(1180);



        DatagramSocket socket = new DatagramSocket();

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.034),        //         30 FPS
                //Duration.seconds(0.017),        //         60 FPS
                new EventHandler<ActionEvent>()
                {
                    double padPosY = 300;

                    public void handle(ActionEvent ae)
                    {

                        try {

                                //send request
                                byte[] buff = Integer.toString(player1.getCurrentPos()).getBytes();
                                InetAddress address = InetAddress.getByName(adress);
                                DatagramPacket packet = new DatagramPacket(buff, buff.length, address, 7537);
                                socket.send(packet);

                                //get response
                                buff = new byte[8];
                                packet = new DatagramPacket(buff, buff.length);
                                socket.receive(packet);

                                //display response
                                received = new String(packet.getData(),0,packet.getLength());
                                String[] ballpos = received.split(" ");

                                ballPosX = Integer.valueOf(ballpos[0]);
                                ballPosY = Integer.valueOf(ballpos[1]);


                        }catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        //double t = (System.currentTimeMillis() - timeStart) / 1000.0;

                        if(input.contains("UP")) {
                            player1.moveUp();
                            player2.moveUp();
                        }

                        if(input.contains("DOWN")) {
                            player1.moveDown();
                            player2.moveDown();
                        }

                        // Clear the canvas
                        gc.clearRect(0, 0, 512,512);

                        // background image clears canvas
                        gc.drawImage( space, 0, 0 );
                        gc.drawImage( pad1, player1.getStartX(), player1.getCurrentPos());
                        gc.drawImage( pad2, player2.getStartX(), player2.getCurrentPos());
                        gc.drawImage( ball , ballPosX , ballPosY );
                    }
                });
            received = "";
        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();

        theStage.show();
    }
}