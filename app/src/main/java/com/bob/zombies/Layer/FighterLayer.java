package com.bob.zombies.Layer;

import com.bob.zombies.domain.ShowPlant;
import com.bob.zombies.domain.ShowZombies;
import com.bob.zombies.utils.CommonUtils;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

import java.util.List;

/**
 * Created by Administrator on 2016/1/25.
 */
public class FighterLayer extends BaseLayer {
    private CCTMXTiledMap map;
    private List<CGPoint> zombiesPoint;
    private CCSprite choose;
    private CCSprite chose;

    public FighterLayer() {
        init();
    }

    private void init() {
        loadMap();
        loadShowZombies();
    }

    private void loadShowZombies() {
        zombiesPoint=CommonUtils.getMapPoint(map,"zombies");
        for (CGPoint point:zombiesPoint){
            ShowZombies zombies=new ShowZombies();
            zombies.setPosition(point);
            map.addChild(zombies);
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
        float distance=map.getContentSize().width-phoneSize.width;
        CCMoveBy ccMoveBy=CCMoveBy.action(2,ccp(-distance,0));
        CCSequence ccSequence=CCSequence.actions(CCDelayTime.action(3),ccMoveBy,
                CCDelayTime.action(1), CCCallFunc.action(this,"loadContainer"));
        map.runAction(ccSequence);
    }


    //玩家容器
    public void loadContainer(){
        choose=CCSprite.sprite("image/fight/chose/fight_choose.png");
        chose=CCSprite.sprite("image/fight/chose/fight_chose.png");

        choose.setAnchorPoint(0,0);
        chose.setAnchorPoint(0, 1);
        chose.setPosition(0, phoneSize.height);

        this.addChild(chose);
        this.addChild(choose);

        loadPlant();
    }

    private void loadPlant() {

        for (int i = 1; i <= 9; i++) {
            ShowPlant plant = new ShowPlant(i);
            CCSprite bgSprite = plant.getBgSprite();
            CCSprite showSprite = plant.getShowSprite();

            // 设置坐标
            bgSprite.setPosition(16 + ((i - 1) % 4) * 56,

                    175 - ((i - 1) / 4) * 59);

            showSprite.setPosition(16 + ((i - 1) % 4) * 56,

                    175 - ((i - 1) / 4) * 59);


            choose.addChild(bgSprite);
            choose.addChild(showSprite);
        }


    }
}
