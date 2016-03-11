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
    Image image = new Image("sample/images/ball.png");
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
        Pad player = new Pad(20);

        // Create opponent pad
        Pad opponent = new Pad(870);

        // Create Ball
        Ball ball = new Ball(500, 100);

        DatagramSocket socket = new DatagramSocket();

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),        //         60 FPS
               // Duration.seconds(0.007),        //         60 FPS
                new EventHandler<ActionEvent>()
                {
                    double padPosY = 300;

                    public void handle(ActionEvent ae)
                    {

                        try {




                                //send request
                                byte[] buff = "1".getBytes();
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

                        double t = (System.currentTimeMillis() - timeStart) / 1000.0;

                        if(input.contains("UP")) {
                            player.moveUp();
                        }

                        if(input.contains("DOWN")) {
                            player.moveDown();
                        }

                        if(ball.getCurrentY() == player.getCurrentPos())



                        // Clear the canvas
                        gc.clearRect(0, 0, 512,512);

                        // background image clears canvas
                        gc.drawImage( space, 0, 0 );
                        gc.drawImage( player.getImage(), player.getStartX(), player.getCurrentPos());
                        gc.drawImage( opponent.getImage(), opponent.getStartX(), opponent.getCurrentPos());
                        gc.drawImage( image , ballPosX , ballPosY );
                    }
                });
            received = "";
        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();

        theStage.show();
    }
}