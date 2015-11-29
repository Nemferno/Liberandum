package org.xodia.multitaskgame.ui;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.xodia.multitaskgame.MainActivity;

public class Button extends ButtonSprite{

	private Text text;
	
	public Button(float pX, float pY, String text, ITextureRegion pNormalTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pNormalTextureRegion, pVertexBufferObjectManager);
		
		/*this.text = new Text(0, 0, MainActivity.getSharedInstance().mButtonFont, text, MainActivity.getSharedInstance().getVertexBufferObjectManager());
		this.text.setPosition(getWidth() / 2 - this.text.getWidth() / 2, getHeight() / 2 - this.text.getHeight() / 2);
		
		attachChild(this.text);*/
	}
	
}
