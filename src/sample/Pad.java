package sample;
import javafx.scene.image.Image;

public class Pad {
    Image padImage = new Image("sample/images/padPlayer1.png");
    final double PAD_MIN_Y = 0;
    final double PAD_MAX_Y = 520;
    final double PAD_SPEED = 7;
    double currentPos = 300;
    double startX;

    public Pad(double startX){
        this.startX = startX;
    }

    public Image getImage(){
        return this.padImage;
    }

    public double getStartX(){
        return this.startX;
    }

    public double getCurrentPos(){
        return this.currentPos;
    }

    public void moveUp(){
        this.currentPos = this.currentPos - PAD_SPEED < PAD_MIN_Y ? PAD_MIN_Y : this.currentPos - PAD_SPEED;
    }

    public void moveDown() {
        this.currentPos = this.currentPos + PAD_SPEED > PAD_MAX_Y ? PAD_MAX_Y : this.currentPos + PAD_SPEED;
    }
}
