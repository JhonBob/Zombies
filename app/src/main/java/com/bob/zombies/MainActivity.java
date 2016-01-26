package com.bob.zombies;

import android.app.Activity;
import android.os.Bundle;

import com.bob.zombies.Layer.WelcomeLayer;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends Activity {
    private CCGLSurfaceView ccglSurfaceView;
    private CCDirector ccDirector;
    private CCScene ccScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ccglSurfaceView=new CCGLSurfaceView(this);
        setContentView(ccglSurfaceView);
        //导演(单列模式)
        ccDirector=CCDirector.sharedDirector();
        //开启线程
        ccDirector.attachInView(ccglSurfaceView);
        //显示帧率
        ccDirector.setDisplayFPS(true);
        //屏幕适配
        ccDirector.setScreenSize(480, 320);
        //屏幕方向
        ccDirector.setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);
        //创建场景
        ccScene=CCScene.node();
        ccScene.addChild(new WelcomeLayer());
        //运行场景
        ccDirector.runWithScene(ccScene);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ccDirector.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ccDirector.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ccDirector.end();
    }
}
