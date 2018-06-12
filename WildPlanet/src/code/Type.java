package code;

import java.awt.image.BufferedImage;

public class Type {
	
	String name;
	BufferedImage img;
	boolean minable;
	boolean passable;
	boolean indoor;
	
	public Type(String name, BufferedImage img, boolean minable, boolean passable, boolean indoor) {
		
		this.name = name;
		this.img = img;
		this.minable = minable;
		this.passable = passable;
		this.indoor = indoor;
		
	}
	
	public Type(String name, BufferedImage img) {
		
		this.name = name;
		this.img = img;
		
	}
	
	
	
}
