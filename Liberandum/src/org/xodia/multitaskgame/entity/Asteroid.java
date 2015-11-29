package org.xodia.multitaskgame.entity;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.xodia.multitaskgame.Resources;
import org.xodia.multitaskgame.util.Timer;

/**
 * In nightmare mode, asteroids have health and be able to break up to mini asteroids!
 * Tier 1 - 1 mini asteroids
 * Tier 2 - 3 mini asteroids
 * Tier 3 - 5 mini asteroids
 * 
 * @author Jasper Bae
 *
 */
public class Asteroid extends GameObject{
	
	private Timer rotationTimer;
	private float rotation;
	
	private boolean hasStartedDeathAnimation;
	private boolean isDeathAnimationEnded;
	
	private AnimatedSprite deathAnimation;
	
	private int screenID;
	
	public Asteroid(float pX, float pY) {
		super(pX, pY, 32, 32, Resources.getInstance().asteroid, Resources.getInstance().vbom);
		
		setSpeed(0.655f);
		setYDir(1);
		
		registerUpdateHandler(new TimerHandler(0.1f, true, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				if(rotation < 360){
					setRotation(rotation);
					rotation += 5.5f;
				}else{
					// reset
					rotation = 0;
					setRotation(rotation);
				}
			}
		}));
		
		rotationTimer = new Timer(0.1f);
		rotationTimer.start();
	}
	
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		setX(getX() + getXDir() * getSpeed());
		setY(getY() + getYDir() * getSpeed());
	}
	
	public void startDeathAnimation(Scene scene){
		hasStartedDeathAnimation = true;
		
		deathAnimation = new AnimatedSprite(getX(), getY(), Resources.getInstance().asteroidDeathAnimation, Resources.getInstance().vbom);
		deathAnimation.animate(100, false, new IAnimationListener() {
			public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
					int pInitialLoopCount) {
			}
			
			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
					int pRemainingLoopCount, int pInitialLoopCount) {
			}
			
			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
					int pOldFrameIndex, int pNewFrameIndex) {
			}
			
			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				isDeathAnimationEnded = true;
			}
		});
		deathAnimation.setRotation(rotation);
		
		scene.attachChild(deathAnimation);
	}
	
	public void setScreenID(int id){
		screenID = id;
	}
	
	public int getScreenID(){
		return screenID;
	}
	
	public void endDeathAnimation(){
		deathAnimation.detachSelf();
	}
	
	public boolean hasStartedDeathAnimation(){
		return hasStartedDeathAnimation;
	}
	
	public boolean isDeathAnimationEnded(){
		return isDeathAnimationEnded;
	}

}
