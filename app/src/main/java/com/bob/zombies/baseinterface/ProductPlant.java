package com.bob.zombies.baseinterface;
//生产型植物

public abstract class ProductPlant extends Plant {

	public ProductPlant(String filepath) {
		super(filepath);
	}

	/**
	 * 阳光、金币
	 */
	public abstract void create();


}
