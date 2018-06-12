package code;

import java.awt.Graphics;
import java.io.IOException;

public class Main {
	
	public static int WindowWidth = 1;
	public static int WindowHeight = 1;
	
	
	
	public static boolean running = false;
	
	public static Frame f;
	
	//public static ArrayList<Particle> particles;
	//public static int particlesRemoved = 0;
	//public static int particlesAdded = 0;
	

	
	public static void main(String[] args) throws IOException{
		
		
		f = new Frame();
		
		WindowWidth = f.getWidth();
		WindowHeight = f.getHeight();
		
		Frame.createBG();
		
		//particles = new ArrayList<Particle>();
		
		
		long lu = System.currentTimeMillis();
		long tpu = (long) (1000/(60));
		
		long ls = System.currentTimeMillis();
		long tps = (long) (10 * 1000);
		
		while(true){
			
			long time = System.currentTimeMillis();
			long tslu = time - lu;
			long tsls = time - ls;
			
			Counter.update();	
			if(tslu >= tpu) {
				
				lu = System.currentTimeMillis();
				if(running) f.update();
				f.render();
				Counter.count();
				WindowWidth = f.getWidth();
				WindowHeight = f.getHeight();
				//System.gc();
			}
			if(tsls >= tps) {
				ls = System.currentTimeMillis();
				if(running) {
//					System.out.println("autosave started");
//					Simulation.saveMap();
//					System.out.println("Success!");
				}
			}
			
		}
			
		
		
	}
	
	

	
	public static void paint(Graphics g){
		
		f.draw(g);
		
		
	}
	
}
