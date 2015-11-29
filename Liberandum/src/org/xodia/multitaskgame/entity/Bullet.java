package org.xodia.multitaskgame.entity;

import org.xodia.multitaskgame.Resources;

public class Bullet extends GameObject{

	public Bullet(float pX, float pY) {
		super(pX, pY, 1, 5, Resources.getInstance().bullet, Resources.getInstance().vbom);
			
		setSpeed(3.5f);
	}
	
	protected void onManagedUpdate(float pSecondsElapsed) {
		setX(getX() + (getXDir() * getSpeed()));
		setY(getY() + (getYDir() * getSpeed()));
	}

}
