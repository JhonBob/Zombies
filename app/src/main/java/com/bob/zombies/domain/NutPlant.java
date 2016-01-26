package com.bob.zombies.domain;

import com.bob.zombies.baseinterface.DefancePlant;
import com.bob.zombies.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;

/**
 * Created by Administrator on 2016/1/26.
 */

//坚果植物
public class NutPlant extends DefancePlant {

    public NutPlant() {
        super("image/plant/nut/p_3_01.png");
        baseAction();
    }

    @Override
    public void baseAction() {

        CCAction animate = CommonUtils.animate("image/plant/nut/p_3_%02d.png", 11, true);
        this.runAction(animate);
    }
}
