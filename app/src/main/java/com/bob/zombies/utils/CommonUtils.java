package com.bob.zombies.utils;

import com.bob.zombies.Layer.MenuLayer;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.layers.CCTMXObjectGroup;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.types.CGPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/1/25.
 */
public class CommonUtils {

    public static CCAction animate(String path,int number,boolean isForever){

        ArrayList<CCSpriteFrame> frames=new ArrayList<>();
        String form=path;
        for (int i=1;i<=number;i++){
            CCSpriteFrame displayedFrame= CCSprite.sprite(String.format(form, i)).displayedFrame();
            frames.add(displayedFrame);
        }
        CCAnimation ccAnimation=CCAnimation.animation("", 0.2f, frames);

        if (isForever){
            CCAnimate animate=CCAnimate.action(ccAnimation);
            CCRepeatForever ccRepeatForever=CCRepeatForever.action(animate);
            return ccRepeatForever;
        }else {
            CCAnimate animate=CCAnimate.action(ccAnimation,false);
            return animate;
        }
    }

    public static void changeLayer(CCLayer ccLayer){
        //切换界面
        CCScene ccScene=CCScene.node();
        ccScene.addChild(ccLayer);
        //CCJumpZoomTransition transition=CCJumpZoomTransition.transition(2,ccScene);
        //CCPageTurnTransition transition=CCPageTurnTransition.transition(2,ccScene);
        CCFlipAngularTransition transition=CCFlipAngularTransition.transition(2, ccScene, 1);
        CCDirector.sharedDirector().replaceScene(transition);
    }


    //解析地图的点
    public static List<CGPoint> getMapPoint(CCTMXTiledMap map, String name) {
        List<CGPoint> cgPoints = new ArrayList<>();
        CCTMXObjectGroup objectGroupNamed = map.objectGroupNamed(name);
        ArrayList<HashMap<String, String>> objects = objectGroupNamed.objects;
        for (HashMap<String, String> hashMap : objects) {
            int x = Integer.parseInt(hashMap.get("x"));
            int y = Integer.parseInt(hashMap.get("y"));
            CGPoint point = CCNode.ccp(x, y);
            cgPoints.add(point);
        }
        return cgPoints;
    }
}
