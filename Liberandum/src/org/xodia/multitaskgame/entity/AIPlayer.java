package org.xodia.multitaskgame.entity;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.xodia.multitaskgame.Resources;

public class AIPlayer extends GameObject{

	private Player target;
	
	private boolean isNightmareMode;
	private boolean isReadyToFire;
	private boolean isPreFiring;
	
	private TimerHandler fireTimer, preFireTimer;
	
	public AIPlayer(float pX, float pY, Player target, boolean nightmare) {
		super(pX, pY, 25, 25, Resources.getInstance().aiPlayer, Resources.getInstance().vbom);

		this.target = target;
		
		setSpeed(0.655f);
		isNightmareMode = nightmare;
		
		if(isNightmareMode){
			setSpeed(0.755f);
			
			fireTimer = new TimerHandler(1, new ITimerCallback() {
				public void onTimePassed(TimerHandler pTimerHandler) {
					isPreFiring = false;
					isReadyToFire = true;
					AIPlayer.this.registerUpdateHandler(preFireTimer);
					pTimerHandler.reset();
					unregisterUpdateHandler(pTimerHandler);
				}
			});
			preFireTimer = new TimerHandler(3, new ITimerCallback() {
				public void onTimePassed(TimerHandler pTimerHandler) {
					isPreFiring = true;
					AIPlayer.this.registerUpdateHandler(fireTimer);
					pTimerHandler.reset();
					unregisterUpdateHandler(pTimerHandler);
				}
			});
			
			registerUpdateHandler(preFireTimer);
		}
	}
	
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		if(target.getX() > getX()){
			setXDir(1);
		}else if(target.getX() < getX()){
			setXDir(-1);
		}
		
		if(target.getY() > getY()){
			setYDir(1);
		}else if(target.getY() < getY()){
			setYDir(-1);
		}
		
		setX(getX() + getXDir() * getSpeed());
		setY(getY() + getYDir() * getSpeed());
		
		setRotation((float) Math.toDegrees(Math.atan2(target.getY() - getY(), target.getX() - getX())) + 90);
	}
	
	public void setReadyToFire(boolean fire){
		isReadyToFire = fire;
	}
	
	public boolean isReadyToFire(){
		return isReadyToFire;
	}
	
	public boolean isPreFiring(){
		return isPreFiring;
	}
	
}
