package org.xodia.multitaskgame;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.xodia.multitaskgame.SceneManager.SceneType;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.android.gms.games.Games;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener{

	private final int 	MENU_NORM = 1,
						MENU_HARD = 2,
						MENU_NORM_HIGHSCORE = 3,
						MENU_HARD_HIGHSCORE = 4;
	
	private MenuScene menuChildScene;
	private ButtonSprite login;
	private ButtonSprite achievements;
	
	public MainMenuScene(){}

	public void createScene() {
		Resources.getInstance().menuMusic.play();
		setBackground(new SpriteBackground(new Sprite(0, 0, Resources.getInstance().menuBackground, vbom)));
		
		login = new ButtonSprite(736, 0, Resources.getInstance().loginButton, vbom);
		login.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				Resources.getInstance().buttonSound.play();
				try{
					activity.runOnUiThread(new Runnable(){
						public void run(){
							if(activity.getGameHelper().isSignedIn()){
								activity.getGameHelper().signOut();
								new AlertDialog.Builder(activity).setMessage("Signed Out...").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
									}
								}).show();
							}else{
								activity.getGameHelper().beginUserInitiatedSignIn();
							}
						}
					});
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		achievements = new ButtonSprite(0, 0, Resources.getInstance().achivementButton, vbom);
		achievements.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				Resources.getInstance().buttonSound.play();
				try{
					activity.runOnUiThread(new Runnable(){
						public void run(){
							if(activity.getGameHelper().isSignedIn()){
								activity.startActivityForResult(Games.Achievements.getAchievementsIntent(activity.getGameHelper().getApiClient()), 5001);
							}else{
								new AlertDialog.Builder(activity).setTitle("Please sign in").setMessage(R.string.gamehelper_not_loged_in2).setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
										activity.getGameHelper().beginUserInitiatedSignIn();
									}
								}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
									}
								}).show();
							}
						}
					});
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		registerTouchArea(login);
		registerTouchArea(achievements);
		attachChild(login);
		attachChild(achievements);
		
		menuChildScene = new MenuScene(camera);
		
		final IMenuItem norm = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_NORM, Resources.getInstance().normButton, vbom), 1.2f, 1);
		final IMenuItem hard = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_HARD, Resources.getInstance().hardButton, vbom), 1.2f, 1);
		final IMenuItem normHighscore = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_NORM_HIGHSCORE, Resources.getInstance().normHighscoreButton, vbom), 1.2f, 1);
		final IMenuItem hardHighscore = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_HARD_HIGHSCORE, Resources.getInstance().hardHighscoreButton, vbom), 1.1f, 1);
		
		menuChildScene.addMenuItem(norm);
		menuChildScene.addMenuItem(hard);
		menuChildScene.addMenuItem(normHighscore);
		menuChildScene.addMenuItem(hardHighscore);
		
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);
		
		norm.setPosition(norm.getX(), norm.getY() + 125);
		hard.setPosition(hard.getX(), norm.getY() + 125);
		normHighscore.setPosition(normHighscore.getX() - 260, 475);
		hardHighscore.setPosition(535, 475);
		
		menuChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(menuChildScene);
	}
	
	public void onBackKeyPressed() {
		System.exit(0);
	}

	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	public void disposeScene() {
		detachChild(login);
		detachChild(achievements);
	}

	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		Resources.getInstance().buttonSound.play();
		switch(pMenuItem.getID()){
		case MENU_NORM:
			Resources.getInstance().menuMusic.stop();
			SceneManager.getInstance().createLoadingScene(SceneType.SCENE_NORM);
			
			return true;
		case MENU_HARD:
			
			Resources.getInstance().menuMusic.stop();
			SceneManager.getInstance().createLoadingScene(SceneType.SCENE_HARD);
			
			return true;
		case MENU_NORM_HIGHSCORE:
			
			// Load Highscore
			try{
				activity.runOnUiThread(new Runnable(){
					public void run(){
						if(activity.getGameHelper().isSignedIn()){
							activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(activity.getGameHelper().getApiClient(), activity.getString(R.string.leaderboard_norm_highscore)), 5001);
						}else{
							new AlertDialog.Builder(activity).setTitle("Please sign in").setMessage(R.string.gamehelper_not_loged_in).setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									activity.getGameHelper().beginUserInitiatedSignIn();
								}
							}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							}).show();
						}
					}
				});
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return true;
		case MENU_HARD_HIGHSCORE:
			
			try{
				activity.runOnUiThread(new Runnable(){
					public void run(){
						if(activity.getGameHelper().isSignedIn()){
							activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(activity.getGameHelper().getApiClient(), activity.getString(R.string.leaderboard_hard_highscore)), 5001);
						}else{
							new AlertDialog.Builder(activity).setTitle("Please sign in").setMessage(R.string.gamehelper_not_loged_in).setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									activity.getGameHelper().beginUserInitiatedSignIn();
								}
							}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							}).show();
						}
					}
				});
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return true;
		default:
			return false;
		}
	}
	
}
