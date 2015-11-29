package org.xodia.multitaskgame;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.xodia.multitaskgame.SceneManager.SceneType;

public abstract class BaseScene extends Scene {

	protected Engine engine;
	protected MainActivity activity;
	protected Resources resource;
	protected VertexBufferObjectManager vbom;
	protected Camera camera;
	
	public BaseScene(){
		resource = Resources.getInstance();
		engine = resource.engine;
		activity = resource.activity;
		vbom = resource.vbom;
		camera = resource.camera;
	}
	
	public abstract void createScene();
	public abstract void onBackKeyPressed();
	public abstract SceneType getSceneType();
	public abstract void disposeScene();
	
}
