package AircraftWar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class Main extends JPanel{
    // 定义静态资源 - 图片
    static BufferedImage hero0;
    static BufferedImage hero1;
    static BufferedImage airplane;
    static BufferedImage bee;
    static BufferedImage bullet;
    static BufferedImage background;
    static BufferedImage bigplane;
    static BufferedImage start;
    static BufferedImage pause;
    static BufferedImage gameover;
    static BufferedImage bee_ember0;
    static BufferedImage bee_ember1;
    static BufferedImage bee_ember2;
    static BufferedImage bee_ember3;

    // 定义四种游戏状态
    public final int START = 0;
    public final int RUNNING = 1;
    public final int PAUSE = 2;
    public final int GAME_OVER = 3;
    public int state = START;  // 游戏状态 Start Running Pause GameOver

    static{   // 将磁盘上的文件读取到内存当中去了，非常消耗资源，不能在使用时才加载，需要提前读取
        try {
            bee_ember0 = ImageIO.read(Main.class.getResource("pic/bee_ember0.png"));
            bee_ember1 = ImageIO.read(Main.class.getResource("pic/bee_ember1.png"));
            bee_ember2 = ImageIO.read(Main.class.getResource("pic/bee_ember2.png"));
            bee_ember3 = ImageIO.read(Main.class.getResource("pic/bee_ember3.png"));
            start = ImageIO.read(Main.class.getResource("pic/start.png"));
            pause = ImageIO.read(Main.class.getResource("pic/pause.png"));
            gameover = ImageIO.read(Main.class.getResource("pic/gameover.png"));
            hero0 = ImageIO.read(Main.class.getResource("pic/hero0.png"));
            hero1 = ImageIO.read(Main.class.getResource("pic/hero1.png"));
            airplane = ImageIO.read(Main.class.getResource("pic/airplane.png"));
            bee = ImageIO.read(Main.class.getResource("pic/bee.png"));
            bullet = ImageIO.read(Main.class.getResource("pic/bullet.png"));
            background = ImageIO.read(Main.class.getResource("pic/background.png"));
            bigplane = ImageIO.read(Main.class.getResource("pic/bigplane.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// 静态代码块，是在加载类时执行的，加载  .java ->编译->.class->加载->jvm运行  ,将图片在程序开始之前读好
    Timer timer = new Timer();
    Hero hero = new Hero();
    // 好多子弹
    List<Bullet> bullets = new ArrayList<Bullet>();

    // 好多飞行物
    List<FlyingObject> flys = new ArrayList<FlyingObject>();

    // 控制定时器启动
    public void action(){
        /**
         * period：时间间隔 1000ms
         * 这个方法中放入周期性的任务
         */
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                if(state == RUNNING) {
                    // 不断生成敌机/小蜜蜂/大敌机
                    createFlyObject();
                    // 移动方法
                    stepAction();
                    // 不断生成子弹
                    shootBullet();
                    // 判断子弹和飞行物的碰撞
                    bangAction();
                    // 判断英雄机和飞行物的碰撞   ->生命值减少 -> 判断游戏是否结束 用gameOverAction()方法
                    crush();
                    // 判断子弹/飞行物 有没有越界
                    outOfBounds();
                }
                // 界面刷新
                repaint();// 实际上就是重新调用了paint方法,使得生成的新对象被不断地画出来
            }
        },10,20);// 20 控制的是移动的速度  不断的重复调用run方法，也不断地重复调用move方法，掉一次y减一次speed，然后不断repaint

        // 添加鼠标事件监听器
        MouseAdapter adapter = new MouseAdapter(){   // JDK提供了这个类，实现了我们需要的两个接口，虽然也是抽象类，方法都写了，只不过里面都是空的，方法都非抽象了
            /*可以检测到鼠标的单击动作，并且可以执行对应的代码*/
            public void mouseClicked(MouseEvent e) {
                // 单击游戏开始 开始 正在运行 暂停 游戏结束
                if(state == START){
                    state = RUNNING;
                }
            }
            /*可以检测到鼠标移动的动作*/
            public void mouseMoved(MouseEvent e) {
                if(state == RUNNING){
                    // 让英雄机坐标和鼠标坐标一致
                    hero.setX(e.getX() - hero.getWidth()/2);
                    hero.setY(e.getY() - hero.getHeight()/2);
                    // 刷新界面
                    repaint();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if(state == PAUSE){
                    state = RUNNING;
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                state = PAUSE;
            }
        };
        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);
        // 添加键盘监听器
        KeyAdapter keyAdapter = new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                // 判断键盘敲击的是不是P按键
                char c = e.getKeyChar();
                if(c == 'q'){
                    System.exit(0);  // 退出JAVA虚拟机，强制退出
                }
            }
        };
        this.requestFocus();
        this.addKeyListener(keyAdapter);   // this是JPanel，这里只是在JPanel上面添加了键盘监听器，而键盘监听器是在焦点所在的组件上起效的，所以上一行要加代码
    }
//    // 控制定时器结束
//    public void gameOver(){
//        timer.cancel();
//    }
    // 生成飞行物
    int indexFly = 0;
    public void createFlyObject() {
        indexFly++;
        if (indexFly % 50 == 0) {     // 因为生成频率和往下的速度冲突了，往下速度正常生成的太快了，所以用计数器使得生成频率减慢五十倍
            int random = (int) (Math.random() * 20);
            switch (random) {
                case 0:
                    flys.add(new Bee());
                    break;
                case 1:
                case 2:
                case 3:
                    flys.add(new BigPlane());
                    break;
                default:
                    flys.add(new Airplane());
            }
        }
    }
    public void stepAction(){
        // 敌机/小蜜蜂/大敌机[飞行物] 移动
        for(FlyingObject fly:flys){
            fly.move();
        }
        // 子弹移动
        for(Bullet b : bullets){
            b.move();
        }
        // 英雄机移动
        hero.move();
    }

    // 控制子弹发射的控制器
    int indexBullet = 0;
    public void shootBullet(){   // 发射子弹
        indexBullet++;
        if (hero.getPlusSpeedBullet() > 0) {
            if(indexBullet % 2  == 0){
                bullets.addAll(hero.shoot());  // 将一个指定的集合全部元素添加到另一个集合中去用addAll方法
            }
            hero.setPlusSpeedBullet(hero.getPlusSpeedBullet() -1);
        }
        else{
            if(indexBullet % 20  == 0){
                bullets.addAll(hero.shoot());  // 将一个指定的集合全部元素添加到另一个集合中去用addAll方法
            }
        }
    }
    String death ;
    int score = 0;   // 定义一个计分器
    private void bangAction(){
        for(int i = 0;i<bullets.size();i++){    // 外层是遍历所有子弹
            for(int j = 0;j<flys.size();j++){   // 内层是具体一个子弹，遍历所有飞行物，看有哪个飞行物被这个子弹撞击
                FlyingObject fly = flys.get(j);   // airplane bee bigplane  --> Award Enemy
                boolean isShoot = fly.shootBy(bullets.get(i));
                if (isShoot) {
                    flys.get(j).setLife(flys.get(j).getLife() - 1);
                    // 撞击到，飞行物就移除掉，子弹也移除掉
                    if(flys.get(j).getLife() == 0){

                        flys.remove(j);
                        bullets.remove(i);

                        // 1、敌机 加分
                        if(fly instanceof Enemy){
                            score += ((Enemy) fly).getScore();
                        }
                        // 2、奖励 加生命值 / 加双倍子弹
                        if(fly instanceof Award){
                            int awardType = ((Award) fly).getType();
                            switch(awardType){
                                case Award.DOUBLE_FIRE:
                                    hero.doubleFire();
                                    break;       // 注意break不能漏
                                case Award.LIFE:
                                    hero.addLife();
                                    break;
                                case Award.speed_plus:
                                    hero.speedPlus();
                                    break;
                            }
                        }
                        break;
                    }
                    else{
                        bullets.remove(i);
                    }
                }
            }
        }
    }
    private void crush(){
        for(int i = 0;i<flys.size();i++){
            FlyingObject fly = flys.get(i);
            boolean isCrush = fly.crushWith(hero);
            if (isCrush){
                // ->生命值减少 -> 判断游戏是否结束 用gameOverAction()方法
                if(hero.getLife()>0){
                    hero.setLife(hero.getLife()-1);
                    flys.remove(flys.get(i));
                }else{
                    gameOverAction();
                }
            }
            if(hero.getLife()==0){
                gameOverAction();
            }
        }
    }
    // 判断游戏是否结束
    private void gameOverAction(){
//        timer.cancel();   // 取消定时器不是最优方案，因为开一个定时器就开了一个线程，对内存消耗比较大，为了程序运行效率，一般情况下定时器只有一个
        // 修改游戏状态即可
        state = GAME_OVER;
    }

    public void outOfBounds(){
        /*for(FlyingObject fly: flys){
            if(fly.getY() > 800){
                flys.remove(fly);  // 越界了就将这个对象从集合中移除,使得内存有所释放
            } */   // ConcurrentModificationException   List迭代删除，集合发生了改变，要换迭代器
//        }
        Iterator<FlyingObject> flysIterator = flys.iterator();
        while (flysIterator.hasNext()) {
            FlyingObject fly = flysIterator.next();
            if (fly.getY() > 800) {
                if(fly instanceof Enemy){
                    flysIterator.remove();
                    hero.setLife(hero.getLife() -1);
                }
                else{
                    flysIterator.remove();
                }
            }
        }
        Iterator<Bullet> bulletsIterator = bullets.iterator();
        while (bulletsIterator.hasNext()) {
            Bullet bullet = bulletsIterator.next();
            if (bullet.getY() > 800) {
                bulletsIterator.remove();   // 移除的是迭代器当前指向的对象
            }
        }
    }
//    public void drawDeath(Graphics g,int x,int y,FlyingObject o){
//        BufferedImage[] images = o.getDeathImages();
//        for(BufferedImage image:images){
//            g.drawImage(image,x,y,null);
//        }
//    }

    public void paint(Graphics g) {
        super.paint(g);
        // 自定义绘画内容,画一张背景图片
        g.drawImage(background,0,0,null);// 最后一个是一个视图
        // 画英雄机
        g.drawImage(hero.getImage(),hero.getX(),hero.getY(),null);
        // 画好多个飞行物
        for(FlyingObject fly:flys){
            g.drawImage(fly.getImage(),fly.getX(),fly.getY(),null);
        }
        // 画好多个子弹
        for(Bullet bullet:bullets){
            g.drawImage(bullet.getImage(),bullet.getX(),bullet.getY(),null);
        }
        // 画分数和生命值
        g.drawString("生命值:"+hero.getLife(),10,20);
        g.drawString("分数"+score,10,35);
        // 画游戏状态
        if(state == START){
            g.drawImage(Main.start,0,0,null);
        } else if (state == PAUSE){
            g.drawImage(Main.pause,0,0,null);
        } else if(state == GAME_OVER){
            g.drawImage(Main.gameover,0,0,null);
        }
        // 画死亡过程
//        drawDeath(g,this.getX(),this.getY(),);
    }

    public void showMe(){   // 跟窗口有关，用来窗口显示功能
        JFrame window = new JFrame("飞机大战");// 创建窗口对象
        window.setSize(400,800);  // 设置窗口大小
        window.setLocationRelativeTo(null);// 设置关闭窗口时退出程序(设置默认关闭选项)
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//类常量，括号内不填表示只关闭窗口，不关闭程序，默认为2,这个为3，表示在关闭的时候同时关闭程序
        // 新建画板对象:JPanel
//        Main panel = new Main();// new 子类，会默认调用父类的构造方法，调用了paint方法，用来绘画的，自定义绘画只需要重写paint方法
        window.add(this);  // 将画板对象添加到窗口对象上
        // 显示窗口
        window.setVisible(true);

//        this.action();  // 调用定时器启动
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.showMe();
        main.action();
    }
}
// 使小蜜蜂在碰界时x方向速度反向                                完成
// 实现碰撞方法                                              完成
// 画分数                                                   完成
// 画状态                                                   完成
// 让大敌机击中三次算死,小蜜蜂两次,普通敌机一次                    完成
// 加敌机Enemy到底部时英雄机的生命值减一                         完成
// 加一个奖励为子弹产生的速度加快                                完成
// 画死亡过程
