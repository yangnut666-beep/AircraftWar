package AircraftWar;

import java.awt.image.BufferedImage;

public class Bee extends FlyingObject implements Award{
    private int xSpeed;
    private int awardType; // 奖励类型
    private boolean flag = true;
    public Bee() {
        image = Main.bee;
        width = image.getWidth();
        height = image.getHeight();
        // 测试 - 固定值， 运行中-随机值
        x = (int)(Math.random() *350);
        y = -height;
        speed = 3;
        xSpeed = 2;
        awardType = (int) (Math.random()*3);
        life = 2;
        deathImages = new BufferedImage[]{Main.bee_ember0,Main.bee_ember1,Main.bee_ember2,Main.bee_ember3};
    }

    public void move(){
        if(this.getX() < (370-this.getWidth())&& flag){
            x += xSpeed;
        }else{
            x -= xSpeed;
            flag = false;
        }
        y += speed;
    }
    public void drawDeath(){

    }

    @Override
    public int getType() {
        return awardType;
    }
}
// x太大，就消失了，y还在正常范围，修改方法:将x速度反方向