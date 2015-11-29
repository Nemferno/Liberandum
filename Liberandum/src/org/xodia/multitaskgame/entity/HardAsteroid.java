package org.xodia.multitaskgame.entity;

import java.util.Random;

public class HardAsteroid extends Asteroid{

	private float currentHealth;
	private float maxHealth;
	
	public HardAsteroid(float pX, float pY) {
		super(pX, pY);
		
		Random rand = new Random();
		
		// Randomly create health 
		maxHealth = rand.nextInt(4) + 1;
		currentHealth = maxHealth;
	}
	
	public void setCurrentHealth(float health){
		currentHealth = health;
	}
	
	public float getCurrentHealth(){
		return currentHealth;
	}
	
	public float getMaxHealth(){
		return maxHealth;
	}
	
}
