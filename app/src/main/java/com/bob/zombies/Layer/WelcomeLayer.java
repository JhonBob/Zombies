package com.bob.zombies.Layer;

import android.os.AsyncTask;
import android.view.MotionEvent;

import com.bob.zombies.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.instant.CCShow;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 * Created by Administrator on 2016/1/25.
 */


//游戏欢迎界面
public class WelcomeLayer extends BaseLayer {

    public CCSprite logo;
    public   CCSprite start;

    public WelcomeLayer() {
        asysTask();
        init();
    }

    private void asysTask() {
        new AsyncTask<Void, Void, Void>(){
            // 在子线程中执行的代码
            @Override
            protected Void doInBackground(Void... params) {
                // 模拟耗时的操作
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
            //在子线程之后执行的代码
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                start.setVisible(true);
                setIsTouchEnabled(true);//  触摸事件能早关就早关 能晚开就晚开
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

        start = CCSprite.sprite("image/loading/loading_start.png");
        start.setPosition(phoneSize.width/2, 30);
        this.addChild(start);
        start.setVisible(false);//不可见   当后台数据加载完成的时候可见
    }
}
