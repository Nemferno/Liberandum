package org.xodia.multitaskgame.entity;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class PowerUp extends GameObject{

	private boolean hasGivenPower;
	
	public PowerUp(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		
	}
	
	public void setGivenPower(boolean given){
		hasGivenPower = given;
	}
	
	public boolean hasGivenPower(){
		return hasGivenPower;
	}
	
}
