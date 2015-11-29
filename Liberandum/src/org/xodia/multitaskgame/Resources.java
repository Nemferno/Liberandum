package org.xodia.multitaskgame;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;
import android.graphics.Typeface;

public class Resources {

	private static class Instance { public static final Resources instance = new Resources(); }
	private Resources(){}
	
	public Engine engine;
	public MainActivity activity;
	public Camera camera;
	public VertexBufferObjectManager vbom;
	
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	
	public ITextureRegion gameoverBase;
	public ITextureRegion restart;
	public ITextureRegion menu;
	public ITextureRegion send;
	
	public ITextureRegion background;
	public ITextureRegion pauseButton;
	public ITextureRegion resumeButton;
	public ITextureRegion asteroid;
	public ITextureRegion bullet;
	public ITextureRegion normFireButton;
	public ITextureRegion downFireButton;
	public ITextureRegion ship;
	public ITextureRegion player;
	public ITextureRegion aiPlayer;
	public ITextureRegion goal;
	public ITextureRegion onScreenControlBase;
	public ITextureRegion onScreenControlKnob;
	public ITextureRegion alert;
	public ITextureRegion laser;
	
	public ITiledTextureRegion asteroidDeathAnimation;
	public ITiledTextureRegion shieldAnimation;
	public ITiledTextureRegion transportDeathAnimation;
	
	public Sound asteroidCollisionSound;
	public Sound collisionSound;
	public Sound gameoverSound;
	public Sound goalSound;
	public Sound laserSound;
	public Sound powerUpSound;
	
	public Font gameFont;
	public Font timeFont;
	public Font readyFont;
	
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	
	public ITextureRegion menuBackground;
	public ITextureRegion normButton;
	public ITextureRegion hardButton;
	public ITextureRegion hardHighscoreButton;
	public ITextureRegion normHighscoreButton;
	public ITextureRegion loginButton;
	public ITextureRegion achivementButton;
	
	public Sound buttonSound;
	
	public Music menuMusic;
	
	private BuildableBitmapTextureAtlas splashTextureAtlas;
	
	public ITextureRegion splashBackground;
	
	public void loadMenuResources(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		normButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "NormalButton.png");
		hardButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "HardButton.png");
		normHighscoreButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "NormScores.png");
		hardHighscoreButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "HardScores.png");
		menuBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "MenuBackground.png");
		loginButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "LogInIcon.png");
		achivementButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "Achievements.png");
		
		try{
			MusicFactory.setAssetBasePath("music/");
			menuMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity, "POLSkySanctuary.ogg");
			menuMusic.setLooping(true);
			
			SoundFactory.setAssetBasePath("sound/");
			buttonSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "ButtonClick.ogg");
			
			menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			menuTextureAtlas.load();
		}catch(TextureAtlasBuilderException e){
			Debug.e(e);
		}catch(IOException e){
			Debug.e(e);
		}
	}
	
	public void loadSplashResources(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");
		splashTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 800, 600, TextureOptions.BILINEAR);
		splashBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "GameLoadingScreen.png");
		
		try{
			splashTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			splashTextureAtlas.load();
		}catch(TextureAtlasBuilderException e){
			Debug.e(e);
		}
	}
	
	public void loadGameResources(){
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2050, 2050);
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/over/");
		gameoverBase = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "GameOverBase.png");
		menu = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "MenuButton.png");
		restart = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "RestartButton.png");
		send = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "SendScoreButton.png");

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		background = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Background.png");
		onScreenControlBase = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "UpdatedKnobBase.png");
		onScreenControlKnob = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "UpdatedKnob.png");
		ship = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ShipFighter.png");
		player = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ShipFighterHelper.png");
		pauseButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "PauseButton.png");
		resumeButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ResumeButton.png");
		bullet = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Bullet.png");
		alert = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Alert.png");
		goal = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "TransportShip.png");
		aiPlayer = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ShipAlien.png");
		asteroid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "UpdatedAsteroid.png");
		normFireButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "FireNormalButton.png");
		downFireButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "FireDownButton.png");
		laser = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "LaserSightPowerUp.png");
		
		asteroidDeathAnimation = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "AsteroidDeathSpriteSheet.png", 9, 1);
		shieldAnimation = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "ShieldSpriteSheet.png", 6, 1);
		transportDeathAnimation = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "TransportShipExplosionSpriteSheet.png", 10, 1);
		
    	gameFont = FontFactory.create(activity.getFontManager(),activity.getTextureManager(), 256, 256,Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, Color.WHITE);
        gameFont.load();
        
        timeFont = FontFactory.create(activity.getFontManager(), activity.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 12, Color.WHITE);
        timeFont.load();
        
        readyFont = FontFactory.create(activity.getFontManager(), activity.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, Color.BLUE);
        readyFont.load();
		
		try{
			SoundFactory.setAssetBasePath("sound/");
			asteroidCollisionSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "AsteroidCollision.ogg");
			collisionSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "Collision.ogg");
			gameoverSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "GameOver.ogg");
			goalSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "Goal.ogg");
			laserSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "LaserShot.ogg");
			powerUpSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "PowerUp.ogg");
			
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			gameTextureAtlas.load();
		}catch(TextureAtlasBuilderException e){
			Debug.e(e);
		}catch(IOException e){
			Debug.e(e);
		}
		
	}
	
	public void unloadMenuResources(){
		menuTextureAtlas.unload();
		normButton = null;
		hardButton = null;
		normHighscoreButton = null;
		hardHighscoreButton = null;
		menuMusic = null;
	}
	
	public void unloadLoadingResources(){
		splashTextureAtlas.unload();
		splashBackground = null;
	}
	
	public void unloadGameResources(){
		gameTextureAtlas.unload();
		onScreenControlBase = onScreenControlKnob = ship = player = pauseButton = resumeButton = bullet = alert
				= goal = aiPlayer = asteroid = normFireButton = downFireButton = laser = background = menu = restart = send = gameoverBase 
				= asteroidDeathAnimation = shieldAnimation = transportDeathAnimation = null;
		asteroidCollisionSound = collisionSound = gameoverSound = goalSound = laserSound = powerUpSound = null;
		gameFont.unload();
		timeFont.unload();
		readyFont.unload();
	}
	
	public static void createResource(MainActivity activity){
		getInstance().engine = activity.getEngine();
		getInstance().camera = activity.getEngine().getCamera();
		getInstance().vbom = activity.getVertexBufferObjectManager();
		getInstance().activity = activity;
	}
	
	public static Resources getInstance(){
		return Instance.instance;
	}
	
}
