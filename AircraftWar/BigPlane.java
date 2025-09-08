package AircraftWar;

public class BigPlane extends FlyingObject implements Enemy,Award{
    private int awardType;
    @Override
    public void move() {
        y += speed;
    }

    public BigPlane() {
        image = Main.bigplane;
        width = image.getWidth();
        height = image.getHeight();
        x = (int)(Math.random()*350);
        y = -height;
        speed = 3;
        awardType = (int) (Math.random()*3);
        life = 3;
    }

    @Override
    public int getType() {
        return awardType;
    }

    @Override
    public int getScore() {
        return 10;
    }
}
