package org.xodia.multitaskgame.util;

public class Timer {

	private float second;
	private float millisecond;
	
	private boolean isRunning;
	private boolean hasStopped;
	
	private boolean isTimeElapsed;
	
	public Timer(float second){
		this.second = second;
		this.millisecond = second;
		
		// Choices are
		// Thread or Method?
	}
	
	public void tick(float delta){
		if(millisecond <= 0){
			isTimeElapsed = true;
			isRunning = false;
			hasStopped = true;
		}else{
			if(isRunning)
				millisecond -= delta;
		}
	}
	
	public void start(){
		try{
			if(!hasStopped)
				isRunning = true;
			else
				throw new Exception("Reset timer or create another timer!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void resume(){
		try{
			if(!hasStopped){
				isRunning = true;
			}else{
				throw new Exception("Reset timer or create another timer!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void pause(){
		try{
			if(!hasStopped){
				isRunning = false;
			}else{
				throw new Exception("Reset timer or create another timer!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void reset(){
		hasStopped = false;
		isRunning = true;
		isTimeElapsed = false;
		millisecond = second;
	}
	
	public void stop(){
		isRunning = false;
		hasStopped = true;
	}
	
	public float getTimeLeft(){
		return millisecond;
	}
	
	public boolean isTimeElapsed(){
		return isTimeElapsed;
	}
	
	public boolean hasStopped(){
		return hasStopped;
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
}
