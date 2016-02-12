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

import java.util.ArrayList;

public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage theStage)
    {
        theStage.setTitle( "PONG" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );


        Canvas canvas = new Canvas( 960, 720 );
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

        Image earth = new Image("sample/images/ball.png");
        Image space = new Image("sample/images/soccerField.jpg");

        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );

        final long timeStart = System.currentTimeMillis();

        // Create player pad
        Pad player = new Pad(20);

        // Create opponent pad
        Pad opponent = new Pad(870);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),        //         60 FPS
                new EventHandler<ActionEvent>()
                {

                    double padPosY = 300;

                    public void handle(ActionEvent ae)
                    {
                        double t = (System.currentTimeMillis() - timeStart) / 1000.0;

                        double x = 232 + 128 * Math.cos(t);
                        double y = 232 + 128 * Math.sin(t);

                        if(input.contains("UP")) {
                            player.moveUp();
                        }

                        if(input.contains("DOWN")) {
                            player.moveDown();
                        }

                        // Clear the canvas
                        gc.clearRect(0, 0, 512,512);

                        // background image clears canvas
                        gc.drawImage( space, 0, 0 );
                        gc.drawImage( earth, x, y );
                        gc.drawImage( player.getImage(), player.getStartX(), player.getCurrentPos());
                        gc.drawImage( opponent.getImage(), opponent.getStartX(), opponent.getCurrentPos());
                    }
                });

        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();

        theStage.show();
    }
}