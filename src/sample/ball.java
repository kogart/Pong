package sample;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {

    Image image = new Image("sample/images/pad.jpg");
    private ImageView ball = new ImageView();
    private int startX;
    private int startY;
    private int currentX;
    private int currentY;
    private int ballSpeed;

    public Ball(int startX, int startY){
        this.startX = startX;
        this.startY = startY;
        ball.setImage(image);
        ball.setFitWidth(100);
        ball.setFitHeight(100);
        ball.setPreserveRatio(true);
    }
    public Image getBall(){return this.image;}

    public int getStartX(){return this.startX;}

    public int getStartY(){return this.startY;}

    public int getCurrentX(){return this.currentX;}

    public int getCurrentY(){return this.currentY;}

    public void ballMove(){this.currentX += 1;this.currentY += 1;}

}
