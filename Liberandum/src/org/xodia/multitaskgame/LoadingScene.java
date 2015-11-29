package org.xodia.multitaskgame;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.Sprite;
import org.xodia.multitaskgame.SceneManager.SceneType;

import android.os.AsyncTask;

public class LoadingScene extends BaseScene {

	private Sprite background;
	private SceneType next;
	
	private class LoaderTask extends AsyncTask<String, Void, String> {
		@SuppressWarnings("incomplete-switch")
		protected String doInBackground(String... params){
			switch(next){
			case SCENE_HARD:
				SceneManager.getInstance().createHardGameScene();
				break;
			case SCENE_NORM:
				SceneManager.getInstance().createNormGameScene();
				break;
			case SCENE_MENU:
				SceneManager.getInstance().createMenuScene();
				break;
			}
			
			return null;
		}
	}
	
	public void setNextScene(SceneType scene){
		next = scene;
	}
	
	public void createScene() {
		background = new Sprite(0, 0, resource.splashBackground, vbom);
		
		engine.registerUpdateHandler(new TimerHandler(2, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				engine.unregisterUpdateHandler(pTimerHandler);
				new LoaderTask().execute();
			}
		}));
		
		attachChild(background);
	}

	public void onBackKeyPressed() {
		System.exit(0);
	}

	public SceneType getSceneType() {
		return SceneType.SCENE_LOADING;
	}
	
	public SceneType getNextScene(){
		return next;
	}

	public void disposeScene() {
		background.detachSelf();
		background.dispose();
		this.detachSelf();
		this.dispose();
	}

}
