package com.bob.zombies.Layer;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGSize;

/**
 * Created by Administrator on 2016/1/25.
 */
public class BaseLayer extends CCLayer {
    public CGSize phoneSize;

    public BaseLayer() {
        phoneSize= CCDirector.sharedDirector().getWinSize();
    }
}
