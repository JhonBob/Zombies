package com.bob.zombies.Layer;

import android.view.MotionEvent;

import com.bob.zombies.domain.ShowPlant;
import com.bob.zombies.domain.ShowZombies;
import com.bob.zombies.engine.GameController;
import com.bob.zombies.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2016/1/25.
 */


//对战视图
public class FighterLayer extends BaseLayer {
    public static final int TAG_CHOSE =5 ;
    private CCTMXTiledMap map;
    private List<CGPoint> zombiesPoint;
    private CCSprite choose;
    private CCSprite chose;
    private List<ShowPlant> showPlants;
    private List<ShowPlant> selectPlants=new CopyOnWriteArrayList<>();//已选植物
    private CCSprite rock;
    private float distance;
    private List<ShowZombies> zombiesList;
    private CCSprite ready;

    public FighterLayer() {
        init();
    }

    private void init() {
        loadMap();
        loadShowZombies();
    }

    private void loadShowZombies() {
        zombiesList=new ArrayList<>();
        zombiesPoint=CommonUtils.getMapPoint(map,"zombies");
        for (CGPoint point:zombiesPoint){
            ShowZombies zombies=new ShowZombies();
            zombies.setPosition(point);
            map.addChild(zombies);
            zombiesList.add(zombies);
        }
        moveMap(map);
    }

    private void loadMap() {
        map=CCTMXTiledMap.tiledMap("image/fight/map_day.tmx");
        map.setAnchorPoint(0.5f, 0.5f);
        map.setPosition(map.getContentSize().width / 2, map.getContentSize().height / 2);
        this.addChild(map);

    }

    private void moveMap(CCTMXTiledMap map) {
        //移动地图
        distance=map.getContentSize().width-phoneSize.width;
        CCMoveBy ccMoveBy=CCMoveBy.action(2,ccp(-distance,0));
        CCSequence ccSequence=CCSequence.actions(CCDelayTime.action(3),ccMoveBy,
                CCDelayTime.action(1), CCCallFunc.action(this,"loadContainer"));
        map.runAction(ccSequence);
    }


    //玩家容器
    public void loadContainer(){
        choose=CCSprite.sprite("image/fight/chose/fight_choose.png");
        chose=CCSprite.sprite("image/fight/chose/fight_chose.png");

        choose.setAnchorPoint(0, 0);
        chose.setAnchorPoint(0, 1);
        chose.setPosition(0, phoneSize.height);

        this.addChild(chose,0,TAG_CHOSE);
        this.addChild(choose);

        loadPlant();

        rock = CCSprite.sprite("image/fight/chose/fight_start.png");
        rock.setPosition(choose.getContentSize().width / 2, 30);
        choose.addChild(rock); // 添加了一起来摇滚的按钮
    }

    private void loadPlant() {
        showPlants=new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            ShowPlant plant = new ShowPlant(i);
            CCSprite bgSprite = plant.getBgSprite();
            CCSprite showSprite = plant.getShowSprite();

            // 设置坐标
            bgSprite.setPosition(16 + ((i - 1) % 4) * 56,

                    175 - ((i - 1) / 4) * 59);

            showSprite.setPosition(16 + ((i - 1) % 4) * 56,

                    175 - ((i - 1) / 4) * 59);

            showPlants.add(plant);
            choose.addChild(bgSprite);
            choose.addChild(showSprite);
        }

        this.setIsTouchEnabled(true);
    }

    //锁操作，移动时不允许再次添加植物
    boolean islock=false;
    boolean isDelected;
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        CGPoint point = this.convertTouchToNodeSpace(event);
        if (GameController.isStart){
            GameController.getController().handleTouch(point);
        }else {
            //判读已选植物
            if (CGRect.containsPoint(chose.getBoundingBox(), point)) {
                //反选
                isDelected = false;
                for (ShowPlant plant : selectPlants) {
                    CCSprite showSprite = plant.getShowSprite();
                    if (CGRect.containsPoint(showSprite.getBoundingBox(), point)) {
                        //反选了
                        CCSprite bgSprite = plant.getBgSprite();
                        CGPoint position = bgSprite.getPosition();
                        CCMoveTo ccMoveTo = CCMoveTo.action(0.2f, position);
                        showSprite.runAction(ccMoveTo);
                        selectPlants.remove(plant);//移除
                        isDelected = true;
                        //继续向后遍历
                        continue;
                    }
                    if (isDelected) {
                        //之后的元素全部左移
                        CCMoveBy ccMoveBy = CCMoveBy.action(0.2f, ccp(-53, 0));
                        showSprite.runAction(ccMoveBy);
                    }
                }
            }
            //判读容器是否被点击
            else if (CGRect.containsPoint(choose.getBoundingBox(), point)) {
                if (CGRect.containsPoint(rock.getBoundingBox(), point)) {
                    startGame();
                } else {
                    //判读植物是否被电击
                    for (ShowPlant plant : showPlants) {
                        CCSprite showSprite = plant.getShowSprite();
                        if (CGRect.containsPoint(showSprite.getBoundingBox(), point)) {
                            if (selectPlants.size() < 5 && !islock) {//控制植物数量
                                //已选择了植物
                                islock = true;
                                CCMoveTo ccMoveTo = CCMoveTo.action(0.5f, ccp(75 + selectPlants.size() * 53, 255));
                                CCSequence ccSequence = CCSequence.actions(ccMoveTo, CCCallFunc.action(this, "unlock"));
                                showSprite.runAction(ccSequence);
                                selectPlants.add(plant);//记录
                            }
                        }
                    }
                }
            }
        }
            return super.ccTouchesBegan(event);
    }



    private void startGame() {
        System.out.println("开始游戏");
        // 回收容器
        choose.removeSelf();
        chose.setScale(0.65f);// 缩小玩家已选植物的容器
        // 把玩家选择的植物 重新添加到容器中
        for (ShowPlant plant : selectPlants) {

            plant.getShowSprite().setScale(0.65f);// 因为父容器缩小了 孩子一起缩小

            plant.getShowSprite().setPosition(
                    plant.getShowSprite().getPosition().x * 0.65f,
                    plant.getShowSprite().getPosition().y

                            + (CCDirector.sharedDirector().getWinSize().height - plant

                            .getShowSprite().getPosition().y)

                            * 0.35f);// 设置坐标

            this.addChild(plant.getShowSprite());

        }

        // 移动地图
        CCMoveBy ccMoveBy = CCMoveBy.action(1, ccp(distance, 0));
        CCSequence sequence = CCSequence.actions(ccMoveBy,
                CCCallFunc.action(this, "removeZombies"));
        map.runAction(sequence);
    }

    public void unlock(){
        islock=false;
    }

    //回收僵尸
    public void removeZombies(){
        for (ShowZombies zombies:zombiesList){
            zombies.removeSelf();
        }
        zombiesList.clear();
        zombiesList=null;
        ready();
    }

    private void ready() {
        ready = CCSprite.sprite("image/fight/startready_01.png");
        // winSize.width
        ready.setPosition(phoneSize.width / 2, phoneSize.height / 2);
        this.addChild(ready);
        // 准备开始 游戏的序列帧
        CCAction animate = CommonUtils.animate(
                "image/fight/startready_%02d.png", 3, false);

        CCSequence ccSequence = CCSequence.actions((CCAnimate) animate,
                CCCallFunc.action(this, "start"));
        ready.runAction(ccSequence);
    }

    //真正开始游戏
    public void start() {
        ready.removeSelf();
        GameController.getController().startGame(map, selectPlants); // 目的就是为了优化代码结构
        // 增加阅读性
    }
}
