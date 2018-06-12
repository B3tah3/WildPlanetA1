package code;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class NoiseTest {

	static Random random;
	static BufferedImage img;
	
	public static void main(String[] args) throws InterruptedException {
		
		//while(true) {
			random = new Random();
			img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
			
			double seed = random.nextDouble();
			
			
			for(double x = 0; x < 1; x += 0.001) {
				for(double y = 0; y < 1; y += 0.001) {
					int i = (int)(ImprovedNoise.noise(x*10, y*10, seed)*256);
					
					img.setRGB((int)(x*1000), (int)(y*1000), i);
					
				}
			}
			
			File outputfile = new File("image.png");
			try {
				ImageIO.write(img, "png", outputfile);
				System.out.println("new img");
			} catch (IOException e) {
				System.err.println("file writing err");
			}
			
			
		//}
	}
	
}