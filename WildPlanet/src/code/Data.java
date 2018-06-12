package code;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Data {
	
	public BufferedImage C1_1;
	public BufferedImage C1_2;
	public BufferedImage C1_3;
	public BufferedImage C1_4;
	
	public BufferedImage C2_1;
	public BufferedImage C2_2;
	public BufferedImage C2_3;
	public BufferedImage C2_4;
	
	public BufferedImage C3_1;
	public BufferedImage C3_2;
	public BufferedImage C3_3;
	public BufferedImage C3_4;
	
	public BufferedImage C4_1;
	
	BufferedImage S1;
	BufferedImage S2;
	BufferedImage S3;
	BufferedImage S4;
	
	
	static ArrayList<Type> types;
	
	
	public Data() throws IOException {
		
		types = new ArrayList<Type>();
		for(int i = 0; i < 100; i++) {
			types.add(i, null);
		}
		
		
		C1_1 = ImageIO.read(new File("resources/C1.png"));
		C1_2 = rotateImg(C1_1);
		C1_3 = rotateImg(C1_2);
		C1_4 = rotateImg(C1_3);
		
		C2_1 = ImageIO.read(new File("resources/C2.png"));
		C2_2 = rotateImg(C2_1);
		C2_3 = rotateImg(C2_2);
		C2_4 = rotateImg(C2_3);
		
		C3_1 = ImageIO.read(new File("resources/C3.png"));
		C3_2 = rotateImg(C3_1);
		C3_3 = rotateImg(C3_2);
		C3_4 = rotateImg(C3_3);
		
		C4_1 = ImageIO.read(new File("resources/C4.png"));
		
		S1 = ImageIO.read(new File("resources/stone.png"));
		S2 = rotateImg(ImageIO.read(new File("resources/stone.png")));
		S3 = rotateImg(rotateImg(ImageIO.read(new File("resources/stone.png"))));
		S4 = rotateImg(rotateImg(rotateImg(ImageIO.read(new File("resources/stone.png")))));
		
		
		
		types.add(0, new Type("Stone", ImageIO.read(new File("resources/stone.png")), false, true, false));
		
		types.add(1, new Type("Lake", ImageIO.read(new File("resources/lake.png")), false, true, false));
		
		types.add(10, new Type("Sand", ImageIO.read(new File("resources/sand.png")), true, true, false));
		
		types.add(11, new Type("Rock", ImageIO.read(new File("resources/rock.png")), true, false, false));
		
		types.add(12, new Type("Scrap", ImageIO.read(new File("resources/scrap.png")), true, false, false));
		
		types.add(20, new Type("Rocket", ImageIO.read(new File("resources/rocket.png")), false, false, false));
		
		types.add(21, new Type("Room", ImageIO.read(new File("resources/room.png")), false, false, true));
		
		types.add(22, new Type("Opened Airlock", ImageIO.read(new File("resources/room.png")), false, true, false));
		
		types.add(23, new Type("Closed Airlock", ImageIO.read(new File("resources/room.png")), false, false, true));
		
		types.add(30, new Type("Shovel", ImageIO.read(new File("resources/shovel.png"))));
		
	}
	
	static BufferedImage rotateImg( BufferedImage img ) {
		
		int width = img.getWidth();
		int height = img.getHeight();
		BufferedImage newImage = new BufferedImage(height, width, img.getType());
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				newImage.setRGB(height-1-j, i, img.getRGB(i, j));
			}
		}
		
		return newImage;
		
	}

}
