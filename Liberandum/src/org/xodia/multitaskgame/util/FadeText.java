package org.xodia.multitaskgame.util;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * 
 * Creates a Text object and then makes it slowly fades...
 * It also has a listener everytime its alpha has been changed.
 * 
 * @author Jasper Bae
 *
 */
public class FadeText extends Sprite{
	
	private final float MAX_ALPHA = 1f;
	private final float MIN_ALPHA = 0f;
	
	private String text;
	
	private float alpha;
	
	private Text textObject;
	
	private Timer timer;
	
	private FadeListener listener;
	
	public FadeText(float pX, float pY, String text, IFont font, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, null, pVertexBufferObjectManager);
		
		this.alpha = MAX_ALPHA;
		this.text = text;
		this.textObject = new Text(0, 0, font, text, pVertexBufferObjectManager);
		this.textObject.setAlpha(alpha);
		
		this.timer = new Timer(0.2f);
		this.timer.start();
	}

	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		if(timer.isTimeElapsed()){
			alpha -= 0.15f;
			listener.alphaChanged(alpha);
			
			if(alpha <= MIN_ALPHA){
				alpha = MIN_ALPHA;
				detachSelf();
			}
			
			timer.reset();
		}else{
			timer.tick(pSecondsElapsed);
		}
	}
	
	public static interface FadeListener {
		void alphaChanged(float alpha);
	}
	
	public void setListener(FadeListener listener){
		this.listener = listener;
	}
	
	public String getText(){
		return text;
	}
	
	public float getAlpha(){
		return alpha;
	}
	
}
