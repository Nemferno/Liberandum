package org.xodia.multitaskgame.ui;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.xodia.multitaskgame.util.Timer;

public class ChatBubble extends ButtonSprite{
	
	private IChatBubbleListener listener;
	
	private int currentPageCount;
	
	private List<Chat> chatList;
	
	public ChatBubble(float pX, float pY, ITextureRegion pNormalTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, final Scene scene,
			IChatBubbleListener listener) {
		super(pX, pY, pNormalTextureRegion, pVertexBufferObjectManager);
		
		this.listener = listener;
		
		chatList = new ArrayList<Chat>(10);
		
		setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				// If the chat that's being current has ended its animation, then it goes to its next one
				if(chatList.get(currentPageCount).isTextAnimationEnded()){
					currentPageCount++;
					
					if(currentPageCount < chatList.size()){
						detachChild(chatList.get(currentPageCount - 1));
						attachChild(chatList.get(currentPageCount));
						chatList.get(currentPageCount).startTextAnimation();
						// Requires the previous count
						ChatBubble.this.listener.changeBubble(currentPageCount - 1);
					}else{
						ChatBubble.this.listener.finishedBubble();
						close(scene);
					}
				}else{
					// If not, then we force the text to print out the whole text rather than going to od an animation
					chatList.get(currentPageCount).forceEndAnimation();
				}
			}
		});
	}
	
	private void close(Scene scene){
		detachSelf();
		scene.unregisterTouchArea(this);
	}
	
	public void addChat(String text){
		/*chatList.add(new Chat(0, 0, text, Resource.mChatTextureRegion, MainActivity.getSharedInstance().getVertexBufferObjectManager()));
		
		// If it is the first chat, attach it!
		if(chatList.size() == 1){
			attachChild(chatList.get(0));
			chatList.get(0).startTextAnimation();
		}*/
	}
	
	public void setIChatBubbleListener(IChatBubbleListener listener){
		this.listener = listener;
	}
	
	private static class Chat extends Sprite {
		
		private boolean isTextAnimationStarted;
		private boolean isTextAnimationEnded;
		
		private boolean isNotSpace;
		
		private Text text;
		
		private Timer textTimer;
		private Timer delayTextTimer;
		
		private int searchIndex;
		private String textString;
		
		public Chat(float pX, float pY, String text, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
			
			/*this.text = new Text(pX, pY, MainActivity.getSharedInstance().mTimerFont, "", 640, MainActivity.getSharedInstance().getVertexBufferObjectManager());
			this.textString = text;*/
			
			this.text.setAutoWrap(AutoWrap.WORDS);
			this.text.setAutoWrapWidth(getWidth());
			
			textTimer = new Timer(0.050f);
			delayTextTimer = new Timer(0.1f);
			
			char c = text.charAt(searchIndex++);
			
			if(c != ' '){
				textTimer.start();
				isNotSpace = true;
			}else{
				delayTextTimer.start();
				isNotSpace = false;
			}
			
			this.text.setText(this.text.getText().toString() + c);
			
			attachChild(this.text);
			
			setVisible(true);
		}
		
		public void startTextAnimation(){
			isTextAnimationStarted = true;
		}
		
		public void forceEndAnimation(){
			isTextAnimationStarted = false;
			isTextAnimationEnded = true; 
			text.setText(textString);
		}
		
		protected void onManagedUpdate(float pSecondsElapsed) {
			super.onManagedUpdate(pSecondsElapsed);
			
			if(isTextAnimationStarted){
				if(isNotSpace){
					if(textTimer.isTimeElapsed()){
						text.setText(text.getText().toString() + textString.charAt(searchIndex));
						
						char c = textString.charAt(searchIndex++);
						
						if(searchIndex >= textString.length()){
							isTextAnimationEnded = true;
							isTextAnimationStarted = false;
						}else{
							if(c != ' '){
								if(!textTimer.isRunning())
									textTimer.start();
								
								textTimer.reset();
								isNotSpace = true;
							}else{
								if(!delayTextTimer.isRunning())
									delayTextTimer.start();
								
								delayTextTimer.reset();
								isNotSpace = false;
							}
						}
						
					}else{
						textTimer.tick(pSecondsElapsed);
					}
				}else{
					if(delayTextTimer.isTimeElapsed()){
						text.setText(text.getText().toString() + textString.charAt(searchIndex));
						
						char c = textString.charAt(searchIndex++);
						
						if(searchIndex >= textString.length()){
							isTextAnimationEnded = true;
							isTextAnimationStarted = false;
						}else{
							if(c != ' '){
								if(!textTimer.isRunning())
									textTimer.start();
								
								textTimer.reset();
								isNotSpace = true;
							}else{
								if(!delayTextTimer.isRunning())
									delayTextTimer.start();
								
								delayTextTimer.reset();
								isNotSpace = false;
							}
						}
					}else{
						delayTextTimer.tick(pSecondsElapsed);
					}
				}
			}
			
		}
		
		public boolean isTextAnimationEnded(){
			return isTextAnimationEnded;
		}
		
	}

	public static interface IChatBubbleListener {
		// Called when ever the chat bubble is turned or pressed on
		// Usually you want this to start animations or movement of characters, etc
		void changeBubble(int pageNum);
		
		// Called when the last page of the chat bubble is pressed
		void finishedBubble();
	}
	
}
