package com.bob.zombies.Layer;

import com.bob.zombies.utils.CommonUtils;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCSprite;

/**
 * Created by Administrator on 2016/1/25.
 */
public class MenuLayer extends BaseLayer{

    public MenuLayer() {
        init();
    }

    private void init() {
        CCSprite bg=CCSprite.sprite("image/menu/main_menu_bg.jpg");
        bg.setAnchorPoint(0, 0);
        this.addChild(bg);
        //创建菜单
        CCMenu ccMenu=CCMenu.menu();
        CCMenuItem ccMenuItem= CCMenuItemSprite.item(
                CCSprite.sprite("image/menu/start_adventure_default.png"),
                CCSprite.sprite("image/menu/start_adventure_press.png"),
                this, "click");

        ccMenu.addChild(ccMenuItem);
        ccMenu.setScale(0.5f);
        ccMenu.setPosition(phoneSize.width / 2 - 25, phoneSize.height / 2 - 110);
        ccMenu.setRotation(4.5f);
        //添加菜单
        this.addChild(ccMenu);
    }

    //必须添加object参数，即ccMenuItem
    public void click(Object object){
        System.out.println("按下");
        CommonUtils.changeLayer(new FighterLayer());
    }
}
