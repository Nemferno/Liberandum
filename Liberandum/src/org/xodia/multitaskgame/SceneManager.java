package org.xodia.multitaskgame;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

public class SceneManager {

	private BaseScene normScene;
	private BaseScene hardScene;
	private BaseScene menuScene;
	private BaseScene loadingScene;
	
	private static class Instance { public static final SceneManager instance = new SceneManager(); }
	private SceneManager(){}
	
	private SceneType currentSceneType = SceneType.SCENE_LOADING;
	private BaseScene currentScene;
	
	private Engine engine = Resources.getInstance().engine;
	
	public enum SceneType {
		SCENE_LOADING,
		SCENE_NORM,
		SCENE_HARD,
		SCENE_MENU;
	}
	
	public void setScene(BaseScene scene){
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}
	
	public void setScene(SceneType type){
		switch(type){
		case SCENE_HARD:
			setScene(hardScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		case SCENE_NORM:
			setScene(normScene);
			break;
		case SCENE_MENU:
			setScene(menuScene);
			break;
		}
	}
	
	public void createInitLoadingScene(OnCreateSceneCallback pOnCreateSceneCallback){
		Resources.getInstance().loadSplashResources();
		loadingScene = new LoadingScene();
		((LoadingScene) loadingScene).setNextScene(SceneType.SCENE_MENU);
		loadingScene.createScene();
		setScene(loadingScene);
		pOnCreateSceneCallback.onCreateSceneFinished(loadingScene);
	}
	
	public void createLoadingScene(SceneType scene){
		SceneType oldType = currentSceneType;
		Resources.getInstance().loadSplashResources();
		loadingScene = new LoadingScene();
		((LoadingScene) loadingScene).setNextScene(scene);
		loadingScene.createScene();
		setScene(loadingScene);
		dispose(oldType);
	}
	
	public void createMenuScene(){
		SceneType oldType = currentSceneType;
		Resources.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		menuScene.createScene();
		setScene(menuScene);
		dispose(oldType);
	}
	
	public void createNormGameScene(){
		SceneType oldType = currentSceneType;
		Resources.getInstance().loadGameResources();
		normScene = new NormalScene();
		normScene.createScene();
		setScene(normScene);
		dispose(oldType);
	}
	
	public void createHardGameScene(){
		SceneType oldType = currentSceneType;
		Resources.getInstance().loadGameResources();
		hardScene = new HardScene();
		hardScene.createScene();
		setScene(hardScene);
		dispose(oldType);
	}
	
	private void dispose(SceneType old){
		switch(old){
		case SCENE_HARD:
			disposeHardScene();
			break;
		case SCENE_LOADING:
			disposeLoadingScene();
			break;
		case SCENE_NORM:
			disposeNormalScene();
			break;
		case SCENE_MENU:
			disposeMenuScene();
			break;
		}
	}
	
	private void disposeMenuScene(){
		Resources.getInstance().unloadMenuResources();
		menuScene.disposeScene();
		menuScene = null;
	}
	
	private void disposeLoadingScene(){
		Resources.getInstance().unloadLoadingResources();
		loadingScene.disposeScene();
		loadingScene = null;
	}
	
	private void disposeHardScene(){
		Resources.getInstance().unloadGameResources();
		hardScene.disposeScene();
		hardScene = null;
	}
	
	private void disposeNormalScene(){
		Resources.getInstance().unloadGameResources();
		normScene.disposeScene();
		normScene = null;
	}
	
	public static SceneManager getInstance(){
		return Instance.instance;
	}
	
	public SceneType getCurrentSceneType(){
		return currentSceneType;
	}
	
	public BaseScene getCurrentScene(){
		return currentScene;
	}
	
}
