package org.xodia.multitaskgame.entity;

import org.xodia.multitaskgame.Resources;


/**
 * 
 * This is the box player on the left side
 * 
 * @author Jasper Bae
 *
 */
public class Player extends GameObject{
	
	public Player(float pX, float pY) {
		super(pX, pY, 25, 25, Resources.getInstance().player, Resources.getInstance().vbom);
		
		setSpeed(1.65f);
	}
	
	protected void onManagedUpdate(float pSecondsElapsed) {
		setX(getX() + getXDir() * getSpeed());
		setY(getY() + getYDir() * getSpeed());
	}
	
}
