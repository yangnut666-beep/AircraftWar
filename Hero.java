package AircraftWar;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Hero extends FlyingObject{
    private int life;
    private int count = 0;
    private int double_fire = 0;//双倍子弹剩余数量
    private int plusSpeedBullet = 0;

    public int getPlusSpeedBullet() {
        return plusSpeedBullet;
    }

    public void setPlusSpeedBullet(int plusSpeedBullet) {
        this.plusSpeedBullet = plusSpeedBullet;
    }

    /*
        英雄机发射子弹，一个或者两个
         */
    public List<Bullet> shoot(){
        List<Bullet> list = new ArrayList<>();
        // 判断有没有双倍子弹火力
        if (double_fire > 0) {
            list.add(new Bullet(x + width/4,y));
            list.add(new Bullet(x + width*3/4,y));
            double_fire -= 2;
        }else {
            list.add(new Bullet(x + width/2,y));
        }
        return list;
    }

    public void speedPlus(){
        plusSpeedBullet += 12;   // 这个奖励12颗加速了的子弹
    }
    // 设置英雄机移动的频率
    int indexHero = 0;
    public void move() {
        indexHero ++;
        // 在一个类中使用另一个Main类的资源，但是new对象不合适，只能是静态资源，所以要把静态代码块中的改为静态的
        // 切换图片
        if(indexHero%3 == 0){
            BufferedImage[] images = {Main.hero0,Main.hero1};
            image = images[count++ % 2];
        }
    }

    public void addLife(){
        life++;
    }
    public void doubleFire(){
        double_fire += 20;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public Hero() {
        life = 3;
        x = 200;
        y = 500;
        image = Main.hero0;
        width = image.getWidth();
        height = image.getHeight();
    }
}
