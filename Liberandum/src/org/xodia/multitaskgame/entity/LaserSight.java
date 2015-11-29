package org.xodia.multitaskgame.entity;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.xodia.multitaskgame.Resources;

public class LaserSight extends PowerUp{

	private Ship ship;
	
	private boolean powerDepleted;
	private boolean instantDeath;
	private boolean registered;
	
	private TimerHandler onlineTimer;
	
	public LaserSight(Ship ship, float pX, float pY) {
		super(pX, pY, 25, 25, Resources.getInstance().laser, Resources.getInstance().vbom);

		this.ship = ship;
		
		onlineTimer = new TimerHandler(5, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				setAlive(false);
				instantDeath = true;
				unregisterUpdateHandler(pTimerHandler);
			}
		});
		registerUpdateHandler(onlineTimer);
	}
	
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		if(!ship.isLaserSightOnline() && !powerDepleted){
			if(hasGivenPower() && !isAlive()){
				setVisible(false);
				
				if(!registered){
					unregisterUpdateHandler(onlineTimer);
					registerUpdateHandler(new TimerHandler(8, new ITimerCallback() {
						public void onTimePassed(TimerHandler pTimerHandler) {
							ship.deactivateLaserSight();
							setAlive(false);
							powerDepleted = true;
							instantDeath = true;
							
							unregisterUpdateHandler(pTimerHandler);
						}
					}));
					registered = true;
				}

				ship.activateLaserSight();
			}
		}
	}
	
	public void setInstantDeath(boolean death){
		instantDeath = death;
	}
	
	public boolean hasInstantDeath(){
		return instantDeath;
	}
	
	public boolean isPowerDepleted(){
		return powerDepleted;
	}
	
}
