package org.xodia.multitaskgame;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.controller.MultiTouchController;
import org.xodia.multitaskgame.google.services.GBaseGameActivity;

import android.view.KeyEvent;

public class MainActivity extends GBaseGameActivity {

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 600;
 
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
    	return new LimitedFPSEngine(pEngineOptions, 60);
    }
    
    @Override
    public EngineOptions onCreateEngineOptions() {
        Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions option = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
            new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
        option.getAudioOptions().setNeedsMusic(true);
        option.getAudioOptions().setNeedsSound(true);
        return option;
    }
    
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)
    		throws IOException {
    	Resources.createResource(this);
    	pOnCreateResourcesCallback.onCreateResourcesFinished();
    }
    
    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
    		throws IOException {
    	mEngine.setTouchController(new MultiTouchController());
    	SceneManager.getInstance().createInitLoadingScene(pOnCreateSceneCallback);
    }
    
    public void onPopulateScene(Scene pScene,
    		OnPopulateSceneCallback pOnPopulateSceneCallback)
    		throws IOException {
    	pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
    	}
    	return false;
    }
    
    protected synchronized void onPause() {
    	super.onPause();
    	if(Resources.getInstance().menuMusic != null)
    		Resources.getInstance().menuMusic.pause();
    }
    
    protected synchronized void onResume() {
    	super.onResume();
    	System.gc();
    	if(Resources.getInstance().menuMusic != null)
    		Resources.getInstance().menuMusic.play();
    }
    
    protected void onDestroy() {
    	super.onDestroy();
    	
		System.exit(0);
    }
	
}
