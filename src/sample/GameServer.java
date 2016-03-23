package sample;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class GameServer extends Application {
    static Ball ball = new Ball(603, 323);
    static Pad pad1 = new Pad(20);
    static Pad pad2 = new Pad(1180);

    public static void main(String[] args)  throws IOException {
        new GameServerThread(ball, pad1, pad2).start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );


        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.034),        //         30 FPS
                //Duration.seconds(0.017),        //         60 FPS
                new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        try {
                            ball.ballMove();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();
    }
}
