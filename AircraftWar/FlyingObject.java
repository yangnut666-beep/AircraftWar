package AircraftWar;

import java.awt.image.BufferedImage;

public abstract class FlyingObject {
    protected int x;
    protected int y;
    protected BufferedImage image;
    protected int speed;
    protected int width;
    protected int height;
    protected int life;
    protected BufferedImage[] deathImages;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // 判断当前飞行物有没有被指定的子弹撞击
    public boolean shootBy(Bullet bullet){
        int x = bullet.getX();
        int y = bullet.getY();
        boolean isShoot = this.x<x && x<(this.x + this.width) && this.y < y && y < (this.y+this.height);
        return isShoot;
    }
    public boolean crushWith(Hero hero){
        int x = hero.getX();
        int y = hero.getY();
        boolean isCrush = this.x+this.getWidth()>x &&this.x<x+hero.getWidth() &&this.y<y+hero.getHeight() && this.y+this.getHeight()>y;
        return isCrush;
    }

    public BufferedImage[] getDeathImages() {
        return deathImages;
    }

    public void setDeathImages(BufferedImage[] deathImages) {
        this.deathImages = deathImages;
    }

    public void drawDeath(int x, int y){

    }

    public abstract void move();  // 要求子类全部要完成这个抽象方法
}
