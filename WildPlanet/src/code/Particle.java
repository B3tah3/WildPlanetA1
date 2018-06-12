package code;

import java.awt.Color;

public class Particle {
	
	float x=500,y=500;
	//float speedX,speedY;
	float dir = 0;
	int age = 30;
	Color c = new Color(0f,0f,0f,0f);
	float speed;
	
	public Particle(float x, float y, float speed, float dir, Color c) {
		
		this.speed = speed;
		this.dir = dir;
		
		this.x = x;
		this.y = y;
		this.c = c;
		
	}
	
	public void setX(float x) {
		
		this.x = x;
		
	}
	
	public void setY(float y) {
		
		this.y = y;
		
	}
	
}
