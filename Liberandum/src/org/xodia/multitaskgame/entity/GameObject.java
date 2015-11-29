package org.xodia.multitaskgame.entity;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class GameObject extends Sprite{

	private float xdir, ydir;
	private float speed;
	
	private boolean isAlive;
	
	public GameObject(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		
		isAlive = true;
		
		setVisible(true);
	}
	
	public void setXDir(float dir){
		this.xdir = dir;
	}
	
	public void setYDir(float dir){
		this.ydir = dir;
	}
	
	public void setSpeed(float speed){
		this.speed = speed;
	}
	
	public void setAlive(boolean alive){
		this.isAlive = alive;
	}
	
	public float getXDir(){
		return xdir;
	}
	
	public float getYDir(){
		return ydir;
	}
	
	public float getSpeed(){
		return speed;
	}
	
	public boolean isAlive(){
		return isAlive;
	}
	
}
