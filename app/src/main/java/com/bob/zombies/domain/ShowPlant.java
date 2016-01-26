package com.bob.zombies.domain;

import org.cocos2d.nodes.CCSprite;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/1/25.
 */
public class ShowPlant {

    public static  HashMap<Integer,HashMap<String,String>> plants;
    static {
        String format="image/fight/chose/choose_default%02d.png";
        //模拟数据库
        plants=new HashMap<>();
        for (int i=1;i<=9;i++){
            HashMap<String,String> value=new HashMap<>();
            value.put("path",String.format(format,i));
            value.put("sun",50+"");
            plants.put(i,value);
        }
    }

    private CCSprite showSprite; //展示用的精灵
    private CCSprite bgSprite; //背景精灵

    public ShowPlant(int id){
        HashMap<String, String> hashMap = plants.get(id);
        String path = hashMap.get("path");
        showSprite=CCSprite.sprite(path);
        showSprite.setAnchorPoint(0,0);

        bgSprite=CCSprite.sprite(path);
        bgSprite.setOpacity(100);// 给背景精灵设置透明度
        bgSprite.setAnchorPoint(0,0);

    }

    public CCSprite getShowSprite() {
        return showSprite;
    }

    public CCSprite getBgSprite() {
        return bgSprite;
    }
}
