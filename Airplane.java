package AircraftWar;
// 大中小敌机
public class Airplane extends FlyingObject implements Enemy{
    private int blood;

    public Airplane() {
        image = Main.airplane;
        width = image.getWidth();
        height = image.getHeight();
        x = (int)(Math.random()*350);
        y = -height;
        speed = 5;
        life = 1;
    }

    public void move(){
        y += speed;
    }

    @Override
    public int getScore() {
        return 5;
    }

}
