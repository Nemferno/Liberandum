package org.xodia.multitaskgame;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.xodia.multitaskgame.SceneManager.SceneType;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.android.gms.games.Games;

public class GameOverWindow extends Sprite {

	private Text deathText;
	
	private int score;
	private boolean isSent;
	
	private ButtonSprite sendButton;
	private ButtonSprite resetButton;
	private ButtonSprite menuButton;
	
	public GameOverWindow(final boolean isHard, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(100, 50, 600, 500, Resources.getInstance().gameoverBase, pVertexBufferObjectManager);
		
		deathText = new Text(10, 10, Resources.getInstance().gameFont, "DEFAULT BLOCK FOR DEATH SENTENCE: NULL!!! PLAY THE GAME FOR THIS TO CHANGE", pVertexBufferObjectManager);
		attachChild(deathText);
		
		sendButton = new ButtonSprite(45, 200, Resources.getInstance().send, pVertexBufferObjectManager, new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				Resources.getInstance().buttonSound.play();
				// Send Highscore
				try{
					Resources.getInstance().activity.runOnUiThread(new Runnable(){
						public void run(){
							if(Resources.getInstance().activity.getGameHelper().isSignedIn()){
								if(!isSent){
									if(!isHard)
										Games.Leaderboards.submitScore(Resources.getInstance().activity.getGameHelper().getApiClient(), Resources.getInstance().activity.getString(R.string.leaderboard_norm_highscore), score);
									else
										Games.Leaderboards.submitScore(Resources.getInstance().activity.getGameHelper().getApiClient(), Resources.getInstance().activity.getString(R.string.leaderboard_hard_highscore), score);
									
									isSent = true;
									Resources.getInstance().activity.getGameHelper().makeSimpleDialog("Score Sent!");
								}
							}else{
								new AlertDialog.Builder(Resources.getInstance().activity).setTitle("Please sign in").setMessage(R.string.gamehelper_not_loged_in2).setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
										Resources.getInstance().activity.getGameHelper().beginUserInitiatedSignIn();
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
		resetButton = new ButtonSprite(getWidth() - 295, 200, Resources.getInstance().restart, pVertexBufferObjectManager, new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				Resources.getInstance().buttonSound.play();
				// Reset Game
				if(isHard)
					SceneManager.getInstance().createLoadingScene(SceneType.SCENE_HARD);
				else
					SceneManager.getInstance().createLoadingScene(SceneType.SCENE_NORM);
			}
		});
		menuButton = new ButtonSprite(getWidth() / 2 - 125, 360, Resources.getInstance().menu, pVertexBufferObjectManager, new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				Resources.getInstance().buttonSound.play();
				// Main Menu
				SceneManager.getInstance().createMenuScene();
			}
		});
		
		attachChild(sendButton);
		attachChild(resetButton);
		attachChild(menuButton);
	}
	
	public void display(String death, int score, Scene s, Camera c){
		deathText.setText(death);
		deathText.setPosition(getWidth() / 2 - deathText.getWidth() / 2, 100);
		this.score = score;
		c.getHUD().setVisible(false);
		
		if(getParent() == null){
			s.attachChild(this);
			s.registerTouchArea(sendButton);
			s.registerTouchArea(resetButton);
			s.registerTouchArea(menuButton);
		}
	}

}
