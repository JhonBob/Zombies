package com.bob.zombies.engine;

import com.bob.zombies.Layer.FighterLayer;
import com.bob.zombies.baseinterface.Plant;
import com.bob.zombies.domain.NutPlant;
import com.bob.zombies.domain.PeasePlant;
import com.bob.zombies.domain.PrimaryZombies;
import com.bob.zombies.domain.ShowPlant;
import com.bob.zombies.utils.CommonUtils;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.CCScheduler;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/1/26.
 */


//游戏控制引擎运行在对战视图层
public class GameController {
    public static boolean isStart;
    private static GameController controller = new GameController();

    //行战场记录
    private static List<FightLine> lines = new ArrayList<>();

    //五行
    static {
        for (int i = 0; i < 5; i++) {
            FightLine fightLine = new FightLine(i);
            lines.add(fightLine);
        }
    }

    public static GameController getController() {
        return controller;
    }

    private CCTMXTiledMap map;
    private List<ShowPlant> seletPlants;
    private List<CGPoint> roads;

    //开始游戏
    public void startGame(CCTMXTiledMap map, List<ShowPlant> seletPlants) {
        isStart = true;
        this.map = map;
        this.seletPlants = seletPlants;
        loadRoads();
        //定时添加
        CCScheduler.sharedScheduler().schedule("addZombies", this,5, false);
        //addZombies();
        //解析安放植物的点
        loadTowers();
        //加载进度条
        progress();

    }

    // 用来存放安放植物的点
    CGPoint[][] towers=new CGPoint[5][9];
    private void loadTowers() {
        String format="tower%02d";
        for(int i=1;i<=5;i++){
            List<CGPoint> mapPoint = CommonUtils.getMapPoint(map,
                    String.format(format, i));
            for(int j=0;j<mapPoint.size();j++){
                towers[i-1][j]=mapPoint.get(j);
            }
        }

    }

    //加载地图
    private void loadRoads() {
        roads = CommonUtils.getMapPoint(map, "road");
    }

    //向地图添加僵尸,t是间隔时间

    int process=0;

    public void addZombies(float t) {
        //随机战场
        Random random = new Random();//左闭右开1-4
        int lineNum = random.nextInt(5);
        FightLine fightLine = lines.get(lineNum);
        //初始化僵尸
        PrimaryZombies Zombies = new PrimaryZombies(roads.get(lineNum * 2),
                roads.get(lineNum * 2 + 1));
        //地图添加僵尸
        map.addChild(Zombies,1);

        //行战场记录
        fightLine.addZombies(Zombies);

        process+=5;
        progressTimer.setPercentage(process);// 修改进度
    }

    public void endGame() {
        isStart = false;
    }


    private ShowPlant setplant;
    private Plant installPlant;

    //游戏开始后
    public void handleTouch(CGPoint point) {
        //获取容器
        CCSprite chose = (CCSprite) map.getParent().getChildByTag(FighterLayer.TAG_CHOSE);
        if (setplant!=null){
            setplant.getShowSprite().setOpacity(255);
        }
        if (CGRect.containsPoint(chose.getBoundingBox(), point))
        {
            for (ShowPlant plant : seletPlants)
            {
                CCSprite showSprite = plant.getShowSprite();
                if (CGRect.containsPoint(showSprite.getBoundingBox(), point)) {
                    this.setplant = plant;
                    setplant.getShowSprite().setOpacity(150);
                    switch (setplant.id){
                        case 1:
                            installPlant=new PeasePlant();
                            break;
                        case 4:
                            installPlant=new NutPlant();
                            break;
                        default:
                            break;
                    }
                }
            }
        } else {
            //安放植物
            if (setplant!=null&&installPlant!=null){
                if (isBuilder(point)) {
                    //installPlant.setPosition(point);
                    FightLine fightLine=lines.get(installPlant.getLine());
                    if (!fightLine.hasPlant(installPlant)) {
                        fightLine.addPlant(installPlant);//记录到行
                        map.addChild(installPlant);
                    }
                }
                setplant.getShowSprite().setOpacity(255);
                setplant=null;
                installPlant=null;
            }
        }
    }

    //有效范围
    private boolean isBuilder(CGPoint point) {
        float x=point.x;
        float y= CCDirector.sharedDirector().winSize().height-point.y;
        int row=(int)(x/46);
        int line=(int)(y/54);
        row=row-1;
        line=line-1;
        if (row <= 8 && row >= 0 && line >= 0 && line <= 4) {
            installPlant.setLine(line);
            installPlant.setRow(row);
            installPlant.setPosition(towers[line][row]);  // 修正了植物的坐标
            return true;
        } else {

            return false;
        }
    }


    CCProgressTimer progressTimer;
    /**
     * 更新进度
     */
    private void progress() {
        //  创建了一个进度条
        progressTimer = CCProgressTimer.progressWithFile("image/fight/progress.png");
        //  设置进度条的位置
        progressTimer.setPosition(CCDirector.sharedDirector().getWinSize().width - 80, 13);
        // 获取到图层 添加进度条
        map.getParent().addChild(progressTimer);
        //  设置缩放
        progressTimer.setScale(0.6f);
        //  设置了进度条的百分比
        progressTimer.setPercentage(0);// 每增加一个僵尸需要调整进度，增加5
        // 设置了进度条的方向   从右向左的进度条
        progressTimer.setType(CCProgressTimer.kCCProgressTimerTypeHorizontalBarRL);

        CCSprite sprite = CCSprite.sprite("image/fight/flagmeter.png");
        sprite.setPosition(CCDirector.sharedDirector().getWinSize().width - 80, 13);
        map.getParent().addChild(sprite);
        sprite.setScale(0.6f);
        CCSprite name = CCSprite.sprite("image/fight/FlagMeterLevelProgress.png");
        name.setPosition(CCDirector.sharedDirector().getWinSize().width - 80, 5);
        map.getParent().addChild(name);
        name.setScale(0.6f);
    }


}
