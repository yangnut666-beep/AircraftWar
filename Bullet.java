package AircraftWar;

public class Bullet extends FlyingObject{
    public Bullet(int x,int y) {
        image = Main.bullet;
        width = image.getWidth();
        height = image.getHeight();
        // 子弹的x，y取决于英雄机
        this.x = x;
        this.y = y;
        speed = 6;
    }


    public void move(){
        y -= speed;
    }
}
