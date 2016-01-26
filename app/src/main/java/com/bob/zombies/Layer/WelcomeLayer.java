package com.bob.zombies.Layer;

import android.os.AsyncTask;
import android.view.MotionEvent;

import com.bob.zombies.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.instant.CCShow;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.layers.CCTMXObjectGroup;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.transitions.CCJumpZoomTransition;
import org.cocos2d.transitions.CCPageTurnTransition;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/25.
 */
public class WelcomeLayer extends BaseLayer {

    private CCSprite logo;
    private  CCSprite start;

    public WelcomeLayer() {
        asyncTask();
        init();
    }

    private void asyncTask() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try{
                    Thread.sleep(8000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                start.setVisible(true);
                setIsTouchEnabled(true);
            }
        }.execute();
    }


    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        CGPoint point=this.convertTouchToNodeSpace(event);
        CGRect boundingBox=start.getBoundingBox();
        if (CGRect.containsPoint(boundingBox,point)){
           CommonUtils.changeLayer(new MenuLayer());
        }
        return super.ccTouchesBegan(event);
    }

    private void init() {
        logo();
    }

    private void logo() {
        logo=CCSprite.sprite("image/popcap_logo.png");
        logo.setPosition(phoneSize.width/2,phoneSize.height/2);
        this.addChild(logo);

        CCHide ccHide=CCHide.action();
        CCShow ccShow=CCShow.action();
        CCDelayTime ccDelayTime=CCDelayTime.action(1);
        CCCallFunc ccCallFunc=CCCallFunc.action(this,"welcome");
        CCSequence ccSequence=CCSequence.actions(ccHide,ccDelayTime,ccShow,ccDelayTime,ccDelayTime,ccHide,ccDelayTime,ccCallFunc);
        logo.runAction(ccSequence);
    }

    public void welcome(){
        logo.removeSelf();
        CCSprite bg=CCSprite.sprite("image/welcome.jpg");
        bg.setAnchorPoint(0, 0);
        this.addChild(bg);

        CCSprite loading=CCSprite.sprite("image/loading/loading_01.png");
        loading.setPosition(phoneSize.width / 2, 30);
        this.addChild(loading);
        CCAction animate=CommonUtils.animate("image/loading/loading_%02d.png",9, false);
        loading.runAction(animate);

        start=CCSprite.sprite("image/loading/loading_start.png");
        start.setPosition(phoneSize.width / 2, 30);
        this.addChild(start);
        start.setVisible(false);
    }
}
