package code;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	
	public static BufferStrategy bs;
	static Simulation sim;
	
	static BufferedImage bg;
	private static Random random;
	static BufferedImage play;
	static BufferedImage back;
	static BufferedImage gear;
	
	public static boolean KeyUp    = false;
	public static boolean KeyDown  = false;
	public static boolean KeyRight = false;
	public static boolean KeyLeft  = false;
	public static boolean showOxygen = true;
	public static boolean dead = false;
	
	
	public static int UIStatus = 2; //0=ingame; 2=MainMenu; 3=Options
	 
	public Frame() throws IOException {
		//this.setSize(500, 400);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setTitle("WildPlanet");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setUndecorated(true);
		this.setVisible(true);
		
		sim = new Simulation();
		
		//createBG();
		
		play = ImageIO.read(new File("resources/play.png"));
		back = ImageIO.read(new File("resources/back.png"));
		gear = ImageIO.read(new File("resources/gear.png"));
		
		
		
		createStrat();
		
			
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(UIStatus==0) { //InGame
					
					if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
						
						Main.running = !Main.running;
					
					}
					if(e.getKeyCode()==KeyEvent.VK_F3) {
						
						showOxygen = !showOxygen;
					
					}
					if(e.getKeyCode()==KeyEvent.VK_X) {
						
						Simulation.Airlock();
					
					}
					
					
				}else if(UIStatus==1) { //LevelSelect
					
					if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
						
						UIStatus = 2;
					
					}
					
				}else if(UIStatus==3) { //Options
					
					if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
						
						UIStatus = 2;
					
					}
					
				}
				
				if(e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_W) {
					
					KeyUp = true;
				
				}

				if(e.getKeyCode()==KeyEvent.VK_DOWN || e.getKeyCode()==KeyEvent.VK_S) {
					
					KeyDown = true;
				
				}

				if(e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D) {
					
					KeyRight = true;
				
				}

				if(e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A) {
					
					KeyLeft = true;
				
				}
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				if(e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_W) {
				
					KeyUp = false;
				
				}
	
				if(e.getKeyCode()==KeyEvent.VK_DOWN || e.getKeyCode()==KeyEvent.VK_S) {
					
					KeyDown = false;
				
				}
	
				if(e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D) {
					
					KeyRight = false;
				
				}
	
				if(e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A) {
					
					KeyLeft = false;
				
				}
			}
						
		});
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				if(e.getButton() == MouseEvent.BUTTON1) {
				
					if(UIStatus == 0) {
						
						if(!Main.running) {
							//play
							if(		MouseInfo.getPointerInfo().getLocation().x >= (Main.WindowWidth/5*3)-64 &&
									MouseInfo.getPointerInfo().getLocation().x <= (Main.WindowWidth/5*3)+64 &&
									MouseInfo.getPointerInfo().getLocation().y >= (Main.WindowHeight/4*3)-64 &&
									MouseInfo.getPointerInfo().getLocation().y <= (Main.WindowHeight/4*3)+64 ) {
								
								Main.running = true;
								
							}
							
							//back
							if(		MouseInfo.getPointerInfo().getLocation().x >= (Main.WindowWidth/5*2)-64 &&
									MouseInfo.getPointerInfo().getLocation().x <= (Main.WindowWidth/5*2)+64 &&
									MouseInfo.getPointerInfo().getLocation().y >= (Main.WindowHeight/4*3)-64 &&
									MouseInfo.getPointerInfo().getLocation().y <= (Main.WindowHeight/4*3)+64 ) {
								
								try {
									Simulation.saveMap();
								} catch (IOException e1) {
									System.err.println("World Saving Error");
								}
								System.gc();
								UIStatus = 2;
								
							}
						}else {
							
							int MouseFieldX = (int)((MouseInfo.getPointerInfo().getLocation().x - (Main.WindowWidth/2)) / Simulation.tileSize + Simulation.cameraX);
							int MouseFiledY = (int)((MouseInfo.getPointerInfo().getLocation().y - (Main.WindowHeight/2)) / Simulation.tileSize + Simulation.cameraY);
							
							if(Data.types.get(Simulation.tiles[MouseFieldX] [MouseFiledY].type).minable && Simulation.inv.data[Simulation.playerInvSlotSelected].type == 30) {
							
								Simulation.playerMiningProgress = 1;
								Simulation.playerMiningX = MouseFieldX;
								Simulation.playerMiningY = MouseFiledY;
							}
						}
					}
					if(UIStatus == 2) {
					
					
						//Play klicked
						if(		MouseInfo.getPointerInfo().getLocation().x >= (Main.WindowWidth/5*3)-64 &&
								MouseInfo.getPointerInfo().getLocation().x <= (Main.WindowWidth/5*3)+64 &&
								MouseInfo.getPointerInfo().getLocation().y >= (Main.WindowHeight/2)-64 &&
								MouseInfo.getPointerInfo().getLocation().y <= (Main.WindowHeight/2)+64 ) {
							
							sim.startGame();
							Main.running = true;
							UIStatus = 0;
							
						}
						
						//Gear klicked
						if(		MouseInfo.getPointerInfo().getLocation().x >= (Main.WindowWidth/5*2)-64 &&
								MouseInfo.getPointerInfo().getLocation().x <= (Main.WindowWidth/5*2)+64 &&
								MouseInfo.getPointerInfo().getLocation().y >= (Main.WindowHeight/2)-64 &&
								MouseInfo.getPointerInfo().getLocation().y <= (Main.WindowHeight/2)+64 ) {
							
							UIStatus = 3;
							
						}
					
						
					}
					
				}else if(e.getButton() == MouseEvent.BUTTON3) {
					
					//System.out.println(Simulation.inv.data[Simulation.playerInvSlotSelected]);
					
					
					if(UIStatus == 0) {
						
						if(Main.running) {
							
							int MouseFieldX = (int)((MouseInfo.getPointerInfo().getLocation().x - (Main.WindowWidth/2)) / Simulation.tileSize + Simulation.cameraX);
							int MouseFiledY = (int)((MouseInfo.getPointerInfo().getLocation().y - (Main.WindowHeight/2)) / Simulation.tileSize + Simulation.cameraY);
							
							int t = Simulation.inv.data[Simulation.playerInvSlotSelected].type;
							
							if(Data.types.get(t).minable && Simulation.tiles[MouseFieldX] [MouseFiledY].type == 0) {
							
								Simulation.tiles[MouseFieldX] [MouseFiledY].type = t;
								Simulation.inv.take(t, 1);
							}
							
						}
					}
					
				}
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
				if(UIStatus == 0) {
					
					Simulation.playerMiningProgress = 0;
					
				}
				
			}
			
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(UIStatus == 0) {
					Simulation.playerInvSlotSelected += e.getWheelRotation();
					if(Simulation.playerInvSlotSelected < 0)Simulation.playerInvSlotSelected = 9;
					if(Simulation.playerInvSlotSelected > 9)Simulation.playerInvSlotSelected = 0;
				}
			}
		});
		
		
	}
	
	void update() {
		
		sim.update();
		
	}
	
	public void draw(Graphics g) {
		
		g.clearRect(0, 0, Main.WindowWidth, Main.WindowHeight);
		
		Font font = new Font("SanSerif", 1, 100);
		g.setFont(font);
		
		if(UIStatus == 0) { //InGame
			
			sim.draw(g);
			
			//Pause Menu
			if(Simulation.playerHealthLevel == 0) {
				
				g.setFont(new Font("SanSerif", 1, 200));
				g.setColor(Color.red);
				g.drawString("U DEAD", Main.WindowWidth/2-700, Main.WindowHeight/2+100);
				Main.running = false;
				g.drawImage(back, (Main.WindowWidth/5*2)-64, (Main.WindowHeight/4*3)-64, 128, 128, null);
				dead = true;
				
			}else if(!Main.running) {
				
				g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.8f));
				g.fillRect(0, 0, Main.WindowWidth, Main.WindowHeight);
				
				g.drawImage(play, (Main.WindowWidth/5*3)-64, (Main.WindowHeight/4*3)-64, 128, 128, null);
					
				g.drawImage(back, (Main.WindowWidth/5*2)-64, (Main.WindowHeight/4*3)-64, 128, 128, null);
				
				
				g.setColor(Color.WHITE);
				g.setFont(font);
				g.drawString("Paused", Main.WindowWidth/2-175, Main.WindowHeight/4);
				
			}
			
			
		}else if(UIStatus == 2) { //Main Menu
			
			//BackgraoudImage
			g.drawImage(bg, 0, 0, Main.WindowWidth, Main.WindowHeight, null);
			
			//Headline
			g.setColor(Color.WHITE);
			g.drawString("Wild Planet", Main.WindowWidth/2-300, Main.WindowHeight/5);
			
			//Buttons
			g.drawImage(play, (Main.WindowWidth/5*3)-64, (Main.WindowHeight/2)-64, 128, 128, null);
			g.drawImage(gear, (Main.WindowWidth/5*2)-64, (Main.WindowHeight/2)-64, 128, 128, null);
			
			
		}else if(UIStatus == 3) { //Options
			
			//BackgraoudImage
			g.drawImage(bg, 0, 0, Main.WindowWidth, Main.WindowHeight, null);
			
			//Headline
			g.setColor(Color.WHITE);
			g.drawString("Settings", Main.WindowWidth/2-200, Main.WindowHeight/6);
			
		}
		
	}
	

	
	
	public static void createBG() {
		
		bg = new BufferedImage(Main.WindowWidth, Main.WindowHeight, BufferedImage.TYPE_INT_RGB);
		
		Graphics g = bg.getGraphics();
//		for(int i = 0; i<Main.WindowWidth; i++){
//			
//			int red = (i*320/Main.WindowWidth)-64;
//			if(red<0)red=0;
//			int green = ((Main.WindowWidth-i)*319/Main.WindowWidth)-64;
//			if(green<0)green=0;
//			int blue = 255;
//			Color c = new Color( red , green , blue );
//			g.setColor( c );
//			g.drawLine(i, 0, i, Main.WindowHeight);
//			
//		}
		random = new Random();
		
		for(int x = 0; x < Main.WindowWidth/Simulation.tileSize +1; x++) {
			for(int y = 0; y < Main.WindowHeight/Simulation.tileSize +1; y++) {
				if(random.nextBoolean()){
					g.drawImage(Data.types.get(0).img, x*Simulation.tileSize, y*Simulation.tileSize, null);
				}else {
					g.drawImage(Data.types.get(1).img, x*Simulation.tileSize, y*Simulation.tileSize, null);
				}
			}
		}
	}
	
 	public void createStrat(){
	 createBufferStrategy(2);
	 bs = getBufferStrategy();
	}
	
	public void render() { 
		Graphics2D fin = (Graphics2D) bs.getDrawGraphics();
		Main.paint(fin);
		fin.dispose();
		bs.show();
	 
	}
}