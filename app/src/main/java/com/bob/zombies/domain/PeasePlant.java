package com.bob.zombies.domain;

import com.bob.zombies.baseinterface.AttackPlant;
import com.bob.zombies.baseinterface.Bullet;
import com.bob.zombies.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;

/**
 * Created by Administrator on 2016/1/26.
 */

//豌豆射手
public class PeasePlant extends AttackPlant {

    public PeasePlant() {
        super("image/plant/pease/p_2_01.png");
        baseAction();
    }

    @Override
    public Bullet createBullet() {
        if(bullets.size()<1){
            final Pease pease=new Pease();  // 豌豆没有坐标
            pease.setPosition(ccp(this.getPosition().x+25, this.getPosition().y+35));
            pease.move();
            this.getParent().addChild(pease);// 把子弹添加到了地图上
            bullets.add(pease);
            pease.setDieListener(new DieListener() {
                // 当子弹死亡的时候  在弹夹中移除出去
                @Override
                public void die() {
                    bullets.remove(pease);
                }
            });
        }
        return null;
    }

    @Override
    public void baseAction() {

        CCAction animate = CommonUtils.animate("image/plant/pease/p_2_%02d.png", 8, true);
        this.runAction(animate);

    }
}
