package sample;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {

    Image image = new Image("sample/images/ball.png");
    private int playerScore = 0;
    private double currentX;
    private double currentY;
    private double startY;
    private double startX;
    private double ballSpeedY = 5;
    private double ballSpeedX = 5;
    final double BallMinX = 0;
    final double BallMaxX = 900;
    final double BallMinY = 0;
    final double BallMaxY = 520;

    public Ball(int startX, int startY){
        this.startX = startX;
        this.startY = startY;
    }
    public Image getBall(){return this.image;}

    public double getCurrentX(){return this.currentX;}

    public double getCurrentY(){return this.currentY;}

    public double getPlayerScore(){return this.playerScore;}

    public void ballMove() {
        if (this.currentY == BallMinY) {
            this.ballSpeedY = 5;
        }
        else if (this.currentY == BallMaxY) {
            this.ballSpeedY = -5;
        }
        if (this.currentX == BallMinX) {
            this.currentY = this.startY;
            this.currentX = this.startX;
            this.ballSpeedX = 5;
        }
        else if (this.currentX == BallMaxX) {
            this.playerScore++;
            this.currentY = this.startY;
            this.currentX = this.startX;
            this.ballSpeedX = -5;
        }
        this.currentY += this.ballSpeedY;
        this.currentX += this.ballSpeedX;
    }
}
