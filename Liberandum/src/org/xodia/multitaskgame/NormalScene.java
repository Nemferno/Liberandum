package org.xodia.multitaskgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.xodia.multitaskgame.SceneManager.SceneType;
import org.xodia.multitaskgame.entity.AIPlayer;
import org.xodia.multitaskgame.entity.Asteroid;
import org.xodia.multitaskgame.entity.Bullet;
import org.xodia.multitaskgame.entity.Goal;
import org.xodia.multitaskgame.entity.LaserSight;
import org.xodia.multitaskgame.entity.Player;
import org.xodia.multitaskgame.entity.PowerUp;
import org.xodia.multitaskgame.entity.Ship;

import com.google.android.gms.games.Games;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLES20;

/**
 * 
 * Any object will be removed if it is no longer alive.
 * 
 * @author Jasper Bae
 *
 */
public class NormalScene extends BaseScene {

	private final int MAX_DIFFICULTY = 3;
	private final int maxAliveBullets = 10;
	
	private int currentAliveBullets;
	private int currentPoints;
	private int currentDifficulty;
	private int dodgedAsteroids;
	
	private List<Bullet> aliveBullets;
	private List<Asteroid> aliveAsteroids;
	private List<PowerUp> alivePowerUps;
	private List<Asteroid> pastAsteroids;
	private List<IEntity> deadEntities;
	
	private HUD hud;
	private ButtonSprite fire;
	
	private Text textPoints;
	private Text goalTimeText;
	
	private boolean isPaused;
	
	private Player player;
	private AIPlayer aiPlayer;
	private Goal goal;
	private Ship ship;
	
	private Line laserSight;
	
	private AnalogOnScreenControl mDigitalOnScreenControl;
	
	// This is used so that the game can actually go the game over scene
	// This is the only way!
	private boolean hasEndAnimationEnded;
	private boolean isEndAnimationStarted;
	
	private Line divider, divider2;
	
	private boolean isGameOver;
	private GameOverWindow window;
	
	private SensorEventListener listener;
	
	private void gameover(){
		clearUpdateHandlers();
		clearChildScene();
		clearTouchAreas();
		SensorManager manager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
		manager.unregisterListener(listener);
		Resources.getInstance().gameoverSound.play();
		
		if(dodgedAsteroids >= 50)
			if(activity.getGameHelper().isSignedIn())
				Games.Achievements.unlock(activity.getGameHelper().getApiClient(), activity.getString(R.string.achievement_holymoly));
		
		isGameOver = true;
	}
	
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		if(!isPaused && !isGameOver){
			update(pSecondsElapsed);
			checkCollision(pSecondsElapsed);
			removeDeadObjects();
		}
		
		// Check death
		if(!goal.isAlive())
			checkDeath();
		
		// Check if the player accidentally broke the fourth wall
		if(ship.getX() < 400 || ship.getX() > 800){
			if(activity.getGameHelper().isSignedIn())
				Games.Achievements.unlock(activity.getGameHelper().getApiClient(), activity.getString(R.string.achievement_breakingfourthwall));
		}else if(player.getX() > 400 || player.getX() < -10){
			if(activity.getGameHelper().isSignedIn())
				Games.Achievements.unlock(activity.getGameHelper().getApiClient(), activity.getString(R.string.achievement_breakingfourthwall));
		}
	}
	
	private void checkDeath(){
		if(hasEndAnimationEnded){
			// Print out
			//NewResource.getInstance().gameMusic.pause();
			//NewResource.getInstance().gameMusic.seekTo(0);
			detachChildren();
			window.display(activity.getString(R.string.game_death_by_goal), currentPoints, this, camera);
		}else{
			if(!isEndAnimationStarted){
				goal.setVisible(false);

				final AnimatedSprite death = new AnimatedSprite(goal.getX(), goal.getY(), Resources.getInstance().transportDeathAnimation, vbom);
				death.setRotation(90);
				death.animate(200, false, new IAnimationListener() {
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
						hasEndAnimationEnded = true;
						//detachChild(pAnimatedSprite);
					}
				});
				attachChild(death);
				
				gameover();
				isEndAnimationStarted = true;
			}
		}
	}
	
	private void update(float delta){
		// Update texts
		if(goal != null && goalTimeText != null){
			goalTimeText.setText("" + (int) goal.getSecondsLeft());
		}
		
		// Update positioning for others
		if(ship.isLaserSightOnline()){
			laserSight.setPosition(ship.getX() + 12.5f, ship.getY(), ship.getX() + 12.5f, 0);
		}else{
			if(laserSight.hasParent()){
				detachChild(laserSight);
			}
		}
	}
	
	private void checkCollision(float delta){
		if(player.getX() <= 0){
			player.setX(0);
		}else if(player.getX() + player.getWidth() >= 800 / 2){
			player.setX(800 / 2 - player.getWidth());
		}
		
		if(player.getY() <= 0){
			player.setY(0);
		}else if(player.getY() + player.getHeight() >= 330){
			player.setY(330 - player.getHeight());
		}
		
		if(aiPlayer != null){
			if(aiPlayer.getX() <= 0){
				aiPlayer.setX(0);
			}else if(aiPlayer.getX() + aiPlayer.getWidth() >= 800 / 2){
				aiPlayer.setX(800 / 2 - aiPlayer.getWidth());
			}
			
			if(aiPlayer.getY() <= 0){
				aiPlayer.setY(0);
			}else if(aiPlayer.getY() + aiPlayer.getHeight() >= 330){
				aiPlayer.setY(330 - aiPlayer.getHeight());
			}
			
			if(aiPlayer.collidesWith(player)){
				// Game Over
				//NewResource.getInstance().gameMusic.pause();
				//NewResource.getInstance().gameMusic.seekTo(0);

				gameover();
				detachChildren();
				window.display(activity.getString(R.string.game_death_by_ai_collision), currentPoints, this, camera);
			}
		}
		
		if(player.collidesWith(goal)){
			Resources.getInstance().goalSound.play();
			
			if(goal.getSecondsLeft() < 0.75f){
				if(activity.getGameHelper().isSignedIn())
					Games.Achievements.unlock(activity.getGameHelper().getApiClient(), activity.getString(R.string.achievement_neardeath));
			}
			
			spawnGoal();
		}
		
		if(ship.getX() <= 800 / 2){
			ship.setX(800 / 2);
		}
		
		if(ship.getX() + ship.getWidth() >= 800){
			ship.setX(800 - ship.getWidth());
		}
		
		// ACTUAL COLLISION DETECTION
		for(Asteroid asteroid : aliveAsteroids){
			if(asteroid.isAlive()){
				for(Bullet bullet : aliveBullets){
					if(bullet.isAlive()){
						if(asteroid.collidesWith(bullet)){
							Resources.getInstance().asteroidCollisionSound.play();
							
							asteroid.setAlive(false);
							bullet.setAlive(false);
							
							// Make explosion and make current asteroid dead
							asteroid.setVisible(false);
							asteroid.startDeathAnimation(this);
							
							currentPoints += 1;
							
							spawnLaserSight(asteroid);
						}
					}
				}
				
				if(asteroid.collidesWith(ship)){
					// Game Over
					//NewResource.getInstance().gameMusic.pause();
					//NewResource.getInstance().gameMusic.seekTo(0);
					
					gameover();
					detachChildren();
					window.display(activity.getString(R.string.game_death_by_asteroid_ship), currentPoints, this, camera);
				}
				
				if(asteroid.collidesWith(player)){
					// Game Over
					//NewResource.getInstance().gameMusic.pause();
					//NewResource.getInstance().gameMusic.seekTo(0);
					
					gameover();
					detachChildren();
					window.display(activity.getString(R.string.game_death_by_asteroid_player), currentPoints, this, camera);
				}
				
				if(asteroid.collidesWith(goal)){
					Resources.getInstance().collisionSound.play();
					
					asteroid.setAlive(false);
					
					asteroid.setVisible(false);
					asteroid.startDeathAnimation(this);
					
					AnimatedSprite a = new AnimatedSprite(goal.getX() - 10, goal.getY() - 10, Resources.getInstance().shieldAnimation, vbom);
					a.animate(100, false, new IAnimationListener() {
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
							deadEntities.add(pAnimatedSprite);
						}
					});
					attachChild(a);
				}
				
				if(aiPlayer != null){
					if(asteroid.collidesWith(aiPlayer)){
						Resources.getInstance().collisionSound.play();
						
						asteroid.setAlive(false);
						
						asteroid.setVisible(false);
						asteroid.startDeathAnimation(this);
						
						final AnimatedSprite a = new AnimatedSprite(aiPlayer.getX() - 10, aiPlayer.getY() - 10, Resources.getInstance().shieldAnimation, vbom);
						a.animate(100, false, new IAnimationListener() {
							public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
									int pInitialLoopCount) {
							}
							public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
									int pRemainingLoopCount, int pInitialLoopCount) {
							}
							public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
									int pOldFrameIndex, int pNewFrameIndex) {
								a.setX(aiPlayer.getX() - 10);
								a.setY(aiPlayer.getY() - 10);
							}
							
							public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
								deadEntities.add(pAnimatedSprite);
							}
						});
						attachChild(a);
					}
				}
				
				if(asteroid.getY() + asteroid.getHeight() >= ship.getY() + ship.getHeight()){
					// Game Over
					if(asteroid.getX() > 400){
						float absX = asteroid.getX() - (800 / 2);
						asteroid.setX(absX);
						asteroid.setY(0);
					}else{
						dodgedAsteroids++;
						pastAsteroids.add(asteroid);
					}
				}
			}
		}
		
		for(Bullet bullet : aliveBullets){
			if(bullet.isAlive()){
				if(bullet.getY() <= 0){
					bullet.setAlive(false);
				}
				
				for(PowerUp power : alivePowerUps){
					if(power instanceof LaserSight){
						LaserSight sight = (LaserSight) power;
						if(bullet.collidesWith(power)){
							Resources.getInstance().powerUpSound.play();
							
							if(activity.getGameHelper().isSignedIn())
								Games.Achievements.unlock(activity.getGameHelper().getApiClient(), activity.getString(R.string.achievement_laserz));
							
							if(power.isAlive() && !sight.hasInstantDeath()){
								Resources.getInstance().powerUpSound.play();
								
								if(!ship.isLaserSightOnline()){
									sight.setGivenPower(true);
									power.setAlive(false);
								}else{
									sight.setGivenPower(false);
									sight.setInstantDeath(true);
									power.setAlive(false);
								}
								
								if(!laserSight.hasParent())
									attachChild(laserSight);
								
								bullet.setAlive(false);
							}
							
							break;
						}
					}
				}
			}
		}
	}
	
	private void removeDeadObjects(){
		List<PowerUp> deadPowerUps = new ArrayList<PowerUp>(4);
		List<Asteroid> deadAsteroids = new ArrayList<Asteroid>(6);
		List<Bullet> deadBullets = new ArrayList<Bullet>(10);
		
		for(Asteroid a : aliveAsteroids){
			if(a.isAlive() == false && a.isDeathAnimationEnded()){
				a.endDeathAnimation();
				
				deadAsteroids.add(a);
				
				detachChild(a);
			}
		}
		
		for(Asteroid a : pastAsteroids){
			detachChild(a);
			deadAsteroids.add(a);
		}
		
		for(Bullet b : aliveBullets){
			if(b.isAlive() == false){
				deadBullets.add(b);
				
				detachChild(b);
				
				currentAliveBullets--;
			}
		}
		
		for(PowerUp up : alivePowerUps){
			if(up instanceof LaserSight){
				LaserSight sight = (LaserSight) up;
				
				if(sight.hasInstantDeath()){
					deadPowerUps.add(sight);
					
					detachChild(sight);
				}
			}
		}
		
		for(IEntity e : deadEntities){
			detachChild(e);
		}
		
		deadEntities.clear();
		aliveAsteroids.removeAll(deadAsteroids);
		aliveBullets.removeAll(deadBullets);
		alivePowerUps.removeAll(deadPowerUps);
	}
	
	private void spawnGoal(){
		if(goal != null){
			detachChild(goal);
		}
		
		Random random = new Random();
		int spawnX = random.nextInt(240);
		int spawnY = random.nextInt(280);
		
		goal = new Goal(20 + spawnX, 20 + spawnY);
		
		if(goalTimeText == null){
			goalTimeText = new Text(goal.getX(), goal.getY() - 15, Resources.getInstance().timeFont, "" + goal.getSecondsLeft(), 250, vbom);
			attachChild(goalTimeText);
		}else{
			goalTimeText.setText("" + goal.getSecondsLeft());
			goalTimeText.setPosition(goal.getX(), goal.getY() - 15);
		}
		
		attachChild(goal);
	}
	
	private void spawnAsteroids(){
		Random random = new Random();
		
		for(int i = 0; i < currentDifficulty; i++){
			int spawnX = random.nextInt(240);
			
			Asteroid asteroid = new Asteroid(800 / 2 + spawnX + 20, 0);
			
			// Different Speed
			int spd = random.nextInt(3);
			
			if(currentDifficulty == 3){
				if(spd == 0){
					asteroid.setSpeed(0.85f);
				}else if(spd == 1){
					asteroid.setSpeed(1.12f);
				}else if(spd == 2){
					asteroid.setSpeed(1.4f);
				}
			}
			
			attachChild(asteroid);
			aliveAsteroids.add(asteroid);
		}
	}
	
	private void spawnLaserSight(Asteroid asteroid){
		Random random = new Random();
		int rate = random.nextInt(100) + 1;
		
		if(rate <= 5){
			LaserSight power = new LaserSight(ship, asteroid.getX() - 12.5f, asteroid.getY() - 12.5f);
			alivePowerUps.add(power);
			attachChild(power);
		}
	}
	
	public Scene createPauseScene(){
		MenuScene scene = new MenuScene(camera);
		scene.setBackgroundEnabled(false);
		
		ButtonSprite resume = new ButtonSprite(800 / 2 - Resources.getInstance().resumeButton.getWidth() / 2, 400, Resources.getInstance().resumeButton, vbom);
		resume.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				if(NormalScene.this.hasChildScene()){
					Resources.getInstance().buttonSound.play();
					NormalScene.this.clearChildScene();
					isPaused = false;
				}
			}
		});
		
		Text title = new Text(0, 0, Resources.getInstance().gameFont, "Paused", vbom);
		title.setPosition(800 / 2 - title.getWidth() / 2, 25);
		
		scene.attachChild(title);
		scene.registerTouchArea(resume);
		scene.attachChild(resume);
		
		return scene;
	}

	public void createScene() {
		setBackground(new SpriteBackground(new Sprite(0, 0, Resources.getInstance().background, vbom)));
		
		aliveBullets = new ArrayList<Bullet>();
		aliveAsteroids = new ArrayList<Asteroid>();
		alivePowerUps = new ArrayList<PowerUp>();
		pastAsteroids = new ArrayList<Asteroid>();
		
		hud = new HUD();
		
		mDigitalOnScreenControl = new AnalogOnScreenControl(25f, 425, camera, Resources.getInstance().onScreenControlBase, Resources.getInstance().onScreenControlKnob, 0.1f, vbom, new IAnalogOnScreenControlListener() {
			public void onControlChange(BaseOnScreenControl pBaseOnScreenControl,
					float pValueX, float pValueY) {
				player.setXDir(pValueX);
				player.setYDir(pValueY);
				
				player.setRotation((float) Math.toDegrees(Math.atan2(pValueY, pValueX) + 90));
			}
			
			public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl) {
				
			}
		});
		mDigitalOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		hud.setChildScene(mDigitalOnScreenControl);
		
		player = new Player(800 / 4, 600 / 2);
		ship = new Ship(800 / 2 + 800 / 2, 298);
		
		attachChild(ship);
		attachChild(player);
		
		textPoints = new Text(0, 0, Resources.getInstance().gameFont, activity.getText(R.string.points) + " " + currentPoints, 500, vbom);
		textPoints.setPosition(800 / 2 - textPoints.getWidth() / 2, 440);
		hud.attachChild(textPoints);
		
		registerUpdateHandler(new TimerHandler(6f, true, new ITimerCallback(){
			public void onTimePassed(TimerHandler pTimerHandler){
				spawnAsteroids();
			}
		}));
		registerUpdateHandler(new TimerHandler(45f, true, new ITimerCallback(){
			public void onTimePassed(TimerHandler pTimerHandler){
				if(!(currentDifficulty + 1 > MAX_DIFFICULTY)){
					currentDifficulty += 1;
					
					if(currentDifficulty == 2){
						aiPlayer.setSpeed(aiPlayer.getSpeed() + (aiPlayer.getSpeed() * .05f));
						player.setSpeed(player.getSpeed() + (player.getSpeed() * .025f));
					}else if(currentDifficulty == 3){
						aiPlayer.setSpeed(aiPlayer.getSpeed() + (aiPlayer.getSpeed() * 0.15f));
						player.setSpeed(player.getSpeed() + (player.getSpeed() * .1f));
						unregisterUpdateHandler(pTimerHandler);
					}
				}
			}
		}));
		registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback(){
			public void onTimePassed(TimerHandler pTimerHandler){
				currentPoints += 1;
				textPoints.setText("" + activity.getText(R.string.points) + currentPoints);
				textPoints.setPosition(800 / 2 - textPoints.getWidth() / 2, 440);
			}
		}));
		registerUpdateHandler(new TimerHandler(15f, false, new ITimerCallback(){
			public void onTimePassed(TimerHandler pTimerHandler){
				aiPlayer = new AIPlayer(0, 0, player, false);
				attachChild(aiPlayer);
				unregisterUpdateHandler(pTimerHandler);
			}
		}));
		
		fire = new ButtonSprite(672, 425, Resources.getInstance().normFireButton, Resources.getInstance().downFireButton, vbom){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()){
					if(!ship.getDelayed()){
						if(currentAliveBullets < maxAliveBullets){
							Resources.getInstance().laserSound.play();
							float x = ship.getX() + 10;
							float y = ship.getY();

							Bullet bullet = new Bullet(x, y);
							bullet.setYDir(-1);
							NormalScene.this.attachChild(bullet);
							aliveBullets.add(bullet);
							currentAliveBullets++;
							ship.setDelayed(true);
						}
					}
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		
		registerTouchArea(fire);
		hud.attachChild(fire);
		
		SensorManager manager = (SensorManager) activity.getSystemService(MainActivity.SENSOR_SERVICE);
		manager.registerListener(listener = new SensorEventListener() {
			public void onSensorChanged(SensorEvent event) {
				switch(event.sensor.getType()){
				case Sensor.TYPE_ACCELEROMETER:
					ship.setXDir(event.values[1]);
					break;
				}
			}
			
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				
				
				
			}
		}, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
		
		divider = new Line(800 / 2, 0, 800 / 2, 330, 3, vbom);
		divider.setColor(Color.WHITE);
		divider2 = new Line(0, 330, 800, 330, 3, vbom);
		divider2.setColor(Color.WHITE);
		hud.attachChild(divider);
		hud.attachChild(divider2);
		
		ButtonSprite pause = new ButtonSprite(800 / 2 - Resources.getInstance().pauseButton.getWidth() / 2, 400, Resources.getInstance().pauseButton, vbom);
		pause.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				Resources.getInstance().buttonSound.play();
				NormalScene.this.setChildScene(createPauseScene(), false, true, true);
				isPaused = true;
			}
		});
		
		registerTouchArea(pause);
		hud.attachChild(pause);
		
		laserSight = new Line(0, 0, 0, 0, vbom);
		laserSight.setColor(Color.RED);
		
		currentDifficulty = 1;
		
		aliveBullets = new ArrayList<Bullet>(10);
		aliveAsteroids = new ArrayList<Asteroid>(6);
		alivePowerUps = new ArrayList<PowerUp>(4);
		deadEntities = new ArrayList<IEntity>();

		camera.setHUD(hud);
		
		window = new GameOverWindow(false, vbom);
		
		spawnGoal();
	}

	public void onBackKeyPressed() {
		SceneManager.getInstance().setScene(SceneType.SCENE_MENU);
	}

	public SceneType getSceneType() {
		return SceneType.SCENE_NORM;
	}

	public void disposeScene() {
		setBackground(null);
		camera.setHUD(null);
		clearTouchAreas();
		clearUpdateHandlers();
		for(Asteroid a : aliveAsteroids)
			detachChild(a);
		for(Bullet b : aliveBullets)
			detachChild(b);
		for(PowerUp up : alivePowerUps)
			detachChild(up);
		aliveAsteroids.clear();
		aliveBullets.clear();
		alivePowerUps.clear();
		textPoints = goalTimeText = null;
		player = null;
		aiPlayer = null;
		goal = null;
		ship = null;
		laserSight = null;
		mDigitalOnScreenControl = null;
		divider = divider2 = null;
		window = null;
	}
	
}
