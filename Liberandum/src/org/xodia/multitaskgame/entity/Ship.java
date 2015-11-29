package org.xodia.multitaskgame.entity;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.xodia.multitaskgame.Resources;

public class Ship extends GameObject{

	private boolean isLaserSightOnline;
	private boolean isDelayed;
	private boolean isRegistered;
	
	private float radsRotation;
	
	public Ship(float pX, float pY) {
		super(pX, pY, 25, 25, Resources.getInstance().ship, Resources.getInstance().vbom);
		
		setSpeed(2.5f);
	}
	
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		setX(getX() + getXDir() * getSpeed());
		setY(getY() + getYDir() * getSpeed());
		
		if(isDelayed && !isRegistered){
			registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() {
				public void onTimePassed(TimerHandler pTimerHandler) {
					isDelayed = false;
					isRegistered = false;
					unregisterUpdateHandler(pTimerHandler);
				}
			}));
			isRegistered = true;
		}
	}
	
	public void setRadsRotation(float rads){
		radsRotation = rads;
	}
	
	public void setDelayed(boolean delayed){
		isDelayed = delayed;
	}

	public void activateLaserSight(){
		isLaserSightOnline = true;
	}
	
	public void deactivateLaserSight(){
		isLaserSightOnline = false;
	}
	
	public boolean isLaserSightOnline(){
		return isLaserSightOnline;
	}
	
	public boolean getDelayed(){
		return isDelayed;
	}
	
	public float getRadianRotation(){
		return radsRotation;
	}
	
}
