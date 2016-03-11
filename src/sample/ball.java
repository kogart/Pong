package sample;

public class Ball {

    //Image image = new Image("sample/images/ball.png");
    private int playerScore = 0;
    private int currentX;
    private int currentY;
    private int startY;
    private int startX;
    private double ballSpeedY = 5;
    private double ballSpeedX = 5;
    final double BallMinX = -75;
    final double BallMaxX = 1280;
    final double BallMinY = 0;
    final double BallMaxY = 650;

    public Ball(int startX, int startY){
        this.startX = startX;
        this.startY = startY;
    }
    //public Image getBall(){return this.image;}

    public int getCurrentX(){return this.currentX;}

    public int getCurrentY(){return this.currentY;}

    public double getPlayerScore(){return this.playerScore;}

    public void ballMove() throws InterruptedException {
        if (this.currentY <= BallMinY) {
            this.ballSpeedY = 6;
        }

        else if (this.currentY >= BallMaxY) {
            this.ballSpeedY = -6;
        }

        if (this.currentX <= BallMinX) {
            this.currentY = this.startY;
            this.currentX = this.startX;
            this.ballSpeedX = 6;
        }

        else if (this.currentX >= BallMaxX) {
            this.playerScore++;
            this.currentY = this.startY;
            this.currentX = this.startX;
            this.ballSpeedX = -6;
        }

        this.currentY += this.ballSpeedY;
        this.currentX += this.ballSpeedX;
    }
}
