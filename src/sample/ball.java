package sample;

public class Ball {

    private static int currentX;
    private static int currentY;
    private int startY;
    private int startX;
    private double ballSpeedY = 5;
    private double ballSpeedX = 5;
    final double BallMinX = -75;
    final double BallMaxX = 1280;
    final double BallMinY = 0;
    final double BallMaxY = 650;
    Pad pad1;
    Pad pad2;
    /*private int ballcurrentdX;
    private int ballcurrentdy;
    private int pad1currentdY;
    private int pad2currentdY;*/

    public  Ball(int startX, int startY, Pad pad1, Pad pad2){
        this.pad1 = pad1;
        this.pad2 = pad2;
        this.startX = startX;
        this.startY = startY;
    }

    public int getCurrentX(){return this.currentX;}

    public int getCurrentY(){return this.currentY;}

    public void ballMove() throws InterruptedException {
        /*ballcurrentdX = currentX + 75;
        ballcurrentdy = currentY + 75;
        pad1currentdY = pad1.getCurrentPos() + 150;
        pad2currentdY = pad2.getCurrentPos() + 150;*/
        if (this.currentY <= BallMinY) {
            this.ballSpeedY = 2;
        }

        else if (this.currentY >= BallMaxY) {
            this.ballSpeedY = -2;
        }

        if (this.currentX <= BallMinX) {
            this.currentY = this.startY;
            this.currentX = this.startX;
            this.ballSpeedX = 2;
        }

        else if (this.currentX >= BallMaxX) {
            this.currentY = this.startY;
            this.currentX = this.startX;
            this.ballSpeedX = -2;
        }





        this.currentY += this.ballSpeedY;
        this.currentX += this.ballSpeedX;

    }
    private void checkpadcollision(){
            /*if(currentY < pad2currentdY && ballcurrentdy > pad2.getCurrentPos() && ballcurrentdX == pad2.getStartX()){
            if (this.ballSpeedX == 1)
                ballSpeedX = -1;
            if (this.ballSpeedX == -1)
                ballSpeedX = 1;
            if (this.ballSpeedY == -1)
                ballSpeedY = 1;
            if (this.ballSpeedY == 1)
                ballSpeedY = -1;
        }*/

    }
}
