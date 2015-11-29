package org.xodia.multitaskgame.entity;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.xodia.multitaskgame.Resources;

public class Goal extends GameObject {

	private TimerHandler aliveTimer;
	
	public Goal(float pX, float pY) {
		super(pX, pY, 25, 25, Resources.getInstance().goal, Resources.getInstance().vbom);
		
		aliveTimer = new TimerHandler(11, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				setAlive(false);
				unregisterUpdateHandler(pTimerHandler);
			}
		});
		registerUpdateHandler(aliveTimer);
		
	}

	public float getSecondsLeft(){
		return (aliveTimer.getTimerSeconds() - aliveTimer.getTimerSecondsElapsed());
	}

}
