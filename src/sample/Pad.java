package sample;

public class Pad {
    final int PAD_MIN_Y = 0;
    final int PAD_MAX_Y = 520;
    final int PAD_SPEED = 7;
    int currentPos = 300;
    int startX;

    public Pad(int startX){
        this.startX = startX;

    }

    public int getStartX(){
        return this.startX;
    }

    public int getCurrentPos(){
        return this.currentPos;
    }

    public void setCurrentPos(int y){
        this.currentPos = y;
    }

    public void moveUp(){
        this.currentPos = this.currentPos - PAD_SPEED < PAD_MIN_Y ? PAD_MIN_Y : this.currentPos - PAD_SPEED;
    }

    public void moveDown() {
        this.currentPos = this.currentPos + PAD_SPEED > PAD_MAX_Y ? PAD_MAX_Y : this.currentPos + PAD_SPEED;
    }
}
