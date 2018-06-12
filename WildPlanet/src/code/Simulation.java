package code;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

//TODO
/*
 * Airlock Textures
 * 
 */

/*
 * cool insider facts:
 * 1. When passing through an airlock, the computer uses that time to clear the RAM
 * 2. //TODO
 */

public class Simulation {
	
	public static int tileWidth = 100;
	public static int tileHeight = 100;
	
	public static double seed;
	
	public static final int tileSize = 64;
	
	public static float cameraX = 50;
	public static float cameraY = 50;
	
	public static float playerX = 47.5f;
	public static float playerY = 50.5f;
	public static final float playerSpeed = 0.1f;
	public static float playerOxygenLevel = 0f;
	public static float playerWaterLevel = 0f;
	public static float playerPoisonLevel = 0f;
	public static float playerHealthLevel = 0f;
	public static int playerInvSlotSelected = 0;
	public static int playerMiningProgress = 0;
	public static int playerMiningX = 0;
	public static int playerMiningY = 0;
	
	
	public static Inventory inv;
	public static Data Data;
	public static Tile[] [] tiles;
	
	
	
	static Random random;
	
	
	public Simulation() throws IOException {
		
		random = new Random();
		inv = new Inventory();
		Data = new Data();
		
	}
	
	void update() {
		
		if(Frame.KeyUp && !Frame.KeyDown) {
			moveY(-playerSpeed);
		}
		if(Frame.KeyDown && !Frame.KeyUp) {
			moveY(playerSpeed);
		}
		if(Frame.KeyRight && !Frame.KeyLeft) {
			moveX(playerSpeed);
		}
		if(Frame.KeyLeft && !Frame.KeyRight) {
			moveX(-playerSpeed);
		}
		
		
		//Camera follows Player
		cameraX += (playerX - cameraX)*0.2;
		cameraY += (playerY - cameraY)*0.2;
		
		
		//Breathing
//		playerOxygenLevel -= 0.01f;
//		if(playerOxygenLevel < 100 && tiles[(int)playerX] [(int)playerY].gas.oxygen > 0) {
//			
//			int space = (int)(100-playerOxygenLevel) - tiles[(int)playerX] [(int)playerY].gas.oxygen;
//			if(space >= 0) {
//				playerOxygenLevel += tiles[(int)playerX] [(int)playerY].gas.oxygen;
//				
//			}else {
//				playerOxygenLevel = 100f;
//				tiles[(int)playerX] [(int)playerY].gas.oxygen = (-1*space);
//			}
//		}
//		if(playerOxygenLevel < 0)playerOxygenLevel = 0;
//		if(playerOxygenLevel > 100)playerOxygenLevel = 100;
		
		//Poison Handeling
		playerPoisonLevel -= 0.02f;
		if(tiles[(int)playerX] [(int)playerY].type == 1) playerPoisonLevel+=0.3f;
		if(playerPoisonLevel < 0)playerPoisonLevel = 0;
		if(playerPoisonLevel > 100)playerPoisonLevel = 100;
		
		//Health Handeling
		playerHealthLevel += 0.005f;
		playerHealthLevel -= playerPoisonLevel /200;
		if(playerOxygenLevel == 0)playerHealthLevel -= 0.4f;
		if(playerHealthLevel < 0)playerHealthLevel = 0;
		if(playerHealthLevel > 100)playerHealthLevel = 100;
		
		//Gas Handler
		
		for(int x = 1; x < tileWidth-1; x++) {
			for(int y = 1; y < tileHeight-1; y++) {
				
				int neigboursSameType = 1;
				
				tiles[x] [y].nextGas.oxygen = tiles[x] [y].gas.oxygen;
				
				if(code.Data.types.get(tiles[x] [y].type).indoor == code.Data.types.get(tiles[x+1] [y].type).indoor) {
					
					tiles[x] [y].nextGas.oxygen += tiles[x+1] [y].gas.oxygen;
					//System.out.println("tile " + x + "|" + y + " got neigbour x+1");
					neigboursSameType++;
					
				}
				if(code.Data.types.get(tiles[x] [y].type).indoor == code.Data.types.get(tiles[x-1] [y].type).indoor) {
					
					tiles[x] [y].nextGas.oxygen += tiles[x-1] [y].gas.oxygen;
					//System.out.println("tile " + x + "|" + y + " got neigbour x-1");
					neigboursSameType++;
					
				}
				if(code.Data.types.get(tiles[x] [y].type).indoor == code.Data.types.get(tiles[x] [y+1].type).indoor) {
					
					tiles[x] [y].nextGas.oxygen += tiles[x] [y+1].gas.oxygen;
					//System.out.println("tile " + x + "|" + y + " got neigbour y+1");
					neigboursSameType++;
					
				}
				if(code.Data.types.get(tiles[x] [y].type).indoor == code.Data.types.get(tiles[x] [y-1].type).indoor) {
					
					tiles[x] [y].nextGas.oxygen += tiles[x] [y-1].gas.oxygen;
					//System.out.println("tile " + x + "|" + y + " got neigbour y-1");
					neigboursSameType++;
					
				}
				
				tiles[x] [y].nextGas.oxygen /= neigboursSameType;
			}
			
			
			
		}
		for(int x = 1; x < tileWidth-1; x++) {
			for(int y = 1; y < tileHeight-1; y++) {
				
				tiles[x] [y].gas.oxygen = tiles[x] [y].nextGas.oxygen/100*10 + tiles[x] [y].gas.oxygen/100*90;
			}
		}
		float totalWorldOxygen = 0f;
		for(int x = 1; x < tileWidth-1; x++) {
			for(int y = 1; y < tileHeight-1; y++) {
				
				totalWorldOxygen += tiles[x] [y].gas.oxygen;
			
			}
		}
		System.out.println(totalWorldOxygen);
		
		
		if(playerMiningProgress > 0) {
			
			playerMiningProgress++;
			if(playerMiningProgress >= 100) {
				
				inv.add(tiles[playerMiningX] [playerMiningY].type, 1);
				tiles[playerMiningX] [playerMiningY].type = 0;
				playerMiningProgress = 0;
				
			}
			
		}
	}
	
	void moveX(float dx) {
		
		if(dx < 0) {
			if(playerX <= 19.999999f) {
				playerX = 20;
			}else if (!code.Data.types.get(tiles[(int)(playerX+dx-0.3)] [(int)playerY].type).passable && !code.Data.types.get(tiles[(int)(playerX)] [(int)playerY].type).indoor) {
				
			}else if(code.Data.types.get(tiles[(int)(playerX+dx-0.3)] [(int)playerY].type).indoor != code.Data.types.get(tiles[(int)(playerX)] [(int)playerY].type).indoor) {
				
			}else {
				playerX += dx;
			}
			
		}else if (dx > 0){
			
			if(playerX > tileWidth-19.999999f){
				playerX = tileWidth-20f;
			}else if (!code.Data.types.get(tiles[(int)(playerX+dx+0.3)] [(int)playerY].type).passable  && !code.Data.types.get(tiles[(int)(playerX)] [(int)playerY].type).indoor) {
				
			}else if(code.Data.types.get(tiles[(int)(playerX+dx+0.3)] [(int)playerY].type).indoor != code.Data.types.get(tiles[(int)(playerX)] [(int)playerY].type).indoor) {
				
			}else {
				playerX += dx;
			}
			
		}
		
	}
	
	void moveY(float dy) {
		
		if(dy < 0) {
			if(playerY <= 19.999999f) {
				playerY = 20;
			}else if(!code.Data.types.get(tiles[(int)(playerX)] [(int)(playerY+dy-0.3)].type).passable  && !code.Data.types.get(tiles[(int)(playerX)] [(int)playerY].type).indoor) {
				
			}else if(code.Data.types.get(tiles[(int)playerX] [(int)(playerY+dy-0.3)].type).indoor != code.Data.types.get(tiles[(int)(playerX)] [(int)playerY].type).indoor) {
				
			}else {
				playerY += dy;
			}
			
		}else if (dy > 0){
			
			if(playerY > tileWidth-19.999999f){
				playerY = tileWidth-20f;
			}else if(!code.Data.types.get(tiles[(int)playerX] [(int)(playerY+dy+0.3)].type).passable  && !code.Data.types.get(tiles[(int)(playerX)] [(int)playerY].type).indoor) {
				
			}else if( code.Data.types.get(tiles[(int)playerX] [(int)(playerY+dy+0.3)].type).indoor != code.Data.types.get(tiles[(int)(playerX)] [(int)playerY].type).indoor) {
				
			}else {
				playerY += dy;
			}
			
		}
		
	}
	
	static void Airlock() {
		
		for(int x = (int)playerX-1; x < (int)playerX+1; x++) {
			for(int y = (int)playerY-1;	 y < (int)playerY+1; y++) {
				
				if(tiles[x] [y].type == 22) {
					Gas temp = tiles[x] [y].gas;
					tiles[x] [y].gas = tiles[x] [y].storedGas;
					tiles[x] [y].storedGas = temp;
					tiles[x] [y].type = 23;
					System.out.println("airlock closed!");
				}else if(tiles[x] [y].type == 23) {
					Gas temp = tiles[x] [y].storedGas;
					tiles[x] [y].storedGas = tiles[x] [y].gas;
					tiles[x] [y].gas = temp;
					tiles[x] [y].type = 22;
					System.out.println("airlock opened");
				}
				
				System.gc();
			}
		}
		
	}
	

	
	
	void draw(Graphics g) {
		
		//Rendering Map
		for(int x = 0; x < tileWidth; x++) {
			
			for(int y = 0; y < tileWidth; y++) {
				
				if((int)((x-cameraX)*tileSize+Main.WindowWidth/2) >= -tileSize && (int)((x-cameraX)*tileSize+Main.WindowWidth/2) <= Main.WindowWidth && (int)((y-cameraY)*tileSize+Main.WindowHeight/2) >= -tileSize && (int)((y-cameraY)*tileSize+Main.WindowHeight/2) <= Main.WindowHeight) {
					if(tiles[x][y].type == 21) {
						
						//Top Left Corner
						if(tiles[x-1] [y].type == 21) {
							if(tiles[x] [y-1].type == 21) {
								if(tiles[x-1] [y-1].type == 21) {
									g.drawImage(Data.C4_1,
											(int)((x-cameraX)*tileSize+Main.WindowWidth/2)           , (int)((y-cameraY)*tileSize+Main.WindowHeight/2)           , tileSize/2, tileSize/2, null);
								}else {
									g.drawImage(Data.C3_4,
											(int)((x-cameraX)*tileSize+Main.WindowWidth/2)           , (int)((y-cameraY)*tileSize+Main.WindowHeight/2)           , tileSize/2, tileSize/2, null);
								}
							}else {
								g.drawImage(Data.C2_1,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)           , (int)((y-cameraY)*tileSize+Main.WindowHeight/2)           , tileSize/2, tileSize/2, null);
							}
						}else {
							if(tiles[x] [y-1].type == 21) {
								g.drawImage(Data.C2_4,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)           , (int)((y-cameraY)*tileSize+Main.WindowHeight/2)           , tileSize/2, tileSize/2, null);
							}else {
								g.drawImage(Data.C1_4,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)           , (int)((y-cameraY)*tileSize+Main.WindowHeight/2)           , tileSize/2, tileSize/2, null);
							}
						}
						
						
						//Top Right Corner
						if(tiles[x+1] [y].type == 21) {
							if(tiles[x] [y-1].type == 21) {
								if(tiles[x+1] [y-1].type == 21) {
									g.drawImage(Data.C4_1,
											(int)((x-cameraX)*tileSize+Main.WindowWidth/2)+tileSize/2, (int)((y-cameraY)*tileSize+Main.WindowHeight/2)           , tileSize/2, tileSize/2, null);
								}else {
									g.drawImage(Data.C3_1,
											(int)((x-cameraX)*tileSize+Main.WindowWidth/2)+tileSize/2, (int)((y-cameraY)*tileSize+Main.WindowHeight/2)           , tileSize/2, tileSize/2, null);
								}
							}else {
								g.drawImage(Data.C2_1,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)+tileSize/2, (int)((y-cameraY)*tileSize+Main.WindowHeight/2)           , tileSize/2, tileSize/2, null);
							}
						}else {
							if(tiles[x] [y-1].type == 21) {
								g.drawImage(Data.C2_2,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)+tileSize/2, (int)((y-cameraY)*tileSize+Main.WindowHeight/2)           , tileSize/2, tileSize/2, null);
							}else {
								g.drawImage(Data.C1_1,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)+tileSize/2, (int)((y-cameraY)*tileSize+Main.WindowHeight/2)           , tileSize/2, tileSize/2, null);
							}
						}
						
						
						//Bottom Left Corner
						if(tiles[x-1] [y].type == 21) {
							if(tiles[x] [y+1].type == 21) {
								if(tiles[x-1] [y+1].type == 21) {
									g.drawImage(Data.C4_1,
											(int)((x-cameraX)*tileSize+Main.WindowWidth/2)           , (int)((y-cameraY)*tileSize+Main.WindowHeight/2)+tileSize/2, tileSize/2, tileSize/2, null);
								}else {
									g.drawImage(Data.C3_3,
											(int)((x-cameraX)*tileSize+Main.WindowWidth/2)           , (int)((y-cameraY)*tileSize+Main.WindowHeight/2)+tileSize/2, tileSize/2, tileSize/2, null);
								}
							}else {
								g.drawImage(Data.C2_3,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)           , (int)((y-cameraY)*tileSize+Main.WindowHeight/2)+tileSize/2, tileSize/2, tileSize/2, null);
							}
						}else {
							if(tiles[x] [y+1].type == 21) {
								g.drawImage(Data.C2_4,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)           , (int)((y-cameraY)*tileSize+Main.WindowHeight/2)+tileSize/2, tileSize/2, tileSize/2, null);
							}else {
								g.drawImage(Data.C1_3,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)           , (int)((y-cameraY)*tileSize+Main.WindowHeight/2)+tileSize/2, tileSize/2, tileSize/2, null);
							}
						}
						
						//Bottom Right Corner
						if(tiles[x+1] [y].type == 21) {
							if(tiles[x] [y+1].type == 21) {
								if(tiles[x+1] [y+1].type == 21) {
									g.drawImage(Data.C4_1,
											(int)((x-cameraX)*tileSize+Main.WindowWidth/2)+tileSize/2, (int)((y-cameraY)*tileSize+Main.WindowHeight/2)+tileSize/2, tileSize/2, tileSize/2, null);
								}else {
									g.drawImage(Data.C3_2,
											(int)((x-cameraX)*tileSize+Main.WindowWidth/2)+tileSize/2, (int)((y-cameraY)*tileSize+Main.WindowHeight/2)+tileSize/2, tileSize/2, tileSize/2, null);
								}
							}else {
								g.drawImage(Data.C2_3,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)+tileSize/2, (int)((y-cameraY)*tileSize+Main.WindowHeight/2)+tileSize/2, tileSize/2, tileSize/2, null);
							}
						}else {
							if(tiles[x] [y+1].type == 21) {
								g.drawImage(Data.C2_2,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)+tileSize/2, (int)((y-cameraY)*tileSize+Main.WindowHeight/2)+tileSize/2, tileSize/2, tileSize/2, null);
							}else {
								g.drawImage(Data.C1_2,
										(int)((x-cameraX)*tileSize+Main.WindowWidth/2)+tileSize/2, (int)((y-cameraY)*tileSize+Main.WindowHeight/2)+tileSize/2, tileSize/2, tileSize/2, null);
							}
						}
	
						
					}else if(tiles[x][y].type == 0){
						
						//System.out.println(tiles[x][y].rx + "; " + tiles[x][y].ry);
						if(tiles[x][y].rx) {
							
							if(tiles[x][y].ry) {
								g.drawImage(Data.S1, (int)((x-cameraX)*tileSize+Main.WindowWidth/2), (int)((y-cameraY)*tileSize+Main.WindowHeight/2), tileSize, tileSize, null);
							}else {
								g.drawImage(Data.S2, (int)((x-cameraX)*tileSize+Main.WindowWidth/2), (int)((y-cameraY)*tileSize+Main.WindowHeight/2), tileSize, tileSize, null);
							}
						}else {
							
							if(tiles[x][y].ry) {
								g.drawImage(Data.S3, (int)((x-cameraX)*tileSize+Main.WindowWidth/2), (int)((y-cameraY)*tileSize+Main.WindowHeight/2), tileSize, tileSize, null);
							}else {
								g.drawImage(Data.S4, (int)((x-cameraX)*tileSize+Main.WindowWidth/2), (int)((y-cameraY)*tileSize+Main.WindowHeight/2), tileSize, tileSize, null);
							}
						}
						
					}else {
						
						g.drawImage(code.Data.types.get(tiles[x] [y].type).img, (int)((x-cameraX)*tileSize+Main.WindowWidth/2), (int)((y-cameraY)*tileSize+Main.WindowHeight/2), tileSize, tileSize, null);
						
					}
					if(Frame.showOxygen) {
						if(tiles[x] [y].gas.oxygen > 0){
							g.setColor(new Color(0.5f, 0.7f, 1f, (tiles[x] [y].gas.oxygen/150)));
							//System.out.println(tiles[x] [y].gas.oxygen);
							g.fillRect((int)((x-cameraX)*tileSize+Main.WindowWidth/2), (int)((y-cameraY)*tileSize+Main.WindowHeight/2), tileSize, tileSize);
						}
					}
				}
				
			}
		}
		
		
		//Rendering Player
		g.setColor(new Color(67, 70, 75));
		g.fillOval(
				(int)((playerX-cameraX)*tileSize+Main.WindowWidth/2 - (tileSize*0.3)),
				(int)((playerY-cameraY)*tileSize+Main.WindowHeight/2 - (tileSize*0.3)),
				(int)(tileSize*0.6),
				(int)(tileSize*0.6));
		
		
		//FPS
		g.setColor(Color.WHITE);
		g.drawString(Counter.lastCount + "", 10, 200);
		
		//UI
		//Inventory
		g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.8f));
		g.fillRect(Main.WindowWidth/10*2, Main.WindowHeight/10*9, Main.WindowWidth/10*6, Main.WindowHeight/10*1);
		
		g.setColor(Color.WHITE);
		for(int i = 0; i < 10; i++) {
			g.drawRect(Main.WindowWidth/10*2 + Main.WindowWidth/100*6*i + 10, Main.WindowHeight/10*9 + Main.WindowHeight/100*1, 100, Main.WindowHeight/100*8);
			if(inv.data[i].type != 0) {
				try {
				g.drawImage(code.Data.types.get(inv.data[i].type).img, Main.WindowWidth/10*2 + Main.WindowWidth/100*6*i + 10, Main.WindowHeight/10*9 + Main.WindowHeight/100*1, 100, Main.WindowHeight/100*8, null);
				}catch(NullPointerException e) {
					System.err.println("Error occured loading image from inventory slot: " + i + " with type: " + inv.data[i].type + "!");
				}
			}
			g.setFont(new Font("SanSerif", 1, 10));
			g.drawString(inv.data[i].count + "", Main.WindowWidth/10*2 + Main.WindowWidth/100*6*i + 10, Main.WindowHeight/10*9 + Main.WindowHeight/100*1);
		}
		g.setColor(Color.red);
		g.drawRect(Main.WindowWidth/10*2 + Main.WindowWidth/100*6*playerInvSlotSelected + 10, Main.WindowHeight/10*9 + Main.WindowHeight/100*1, 100, Main.WindowHeight/100*8);
		
		
		//Player Stats
		g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.8f));
		g.fillRect(Main.WindowWidth-260, 0, 260, 240);
		
		//Oxygen Level
		g.setColor(new Color(166, 253, 240));
		g.fillRect(Main.WindowWidth-230, 20, (int)playerOxygenLevel*2, 40);
		g.setColor(Color.WHITE);
		g.drawRect(Main.WindowWidth-230, 20, 200, 40);
		g.setFont(new Font("SanSerif", 1, 40));
		g.setColor(new Color(60, 145, 132));
		g.drawString("O\u2082", Main.WindowWidth-230, 55);
		
		g.drawString(Math.round(tiles[(int)playerX] [(int)playerY].gas.oxygen) + "%", Main.WindowWidth-130, 55);
		
		//Mining Progress
		g.drawString(playerMiningProgress + "%", Main.WindowWidth/2, 50);
		
		
		//Mouse Tile
		int mouseTileX = (int)(((MouseInfo.getPointerInfo().getLocation().x - (Main.WindowWidth/2) )/ Simulation.tileSize) + Simulation.cameraX);
		int mouseTileY = (int)(((MouseInfo.getPointerInfo().getLocation().y - (Main.WindowHeight/2) )/ Simulation.tileSize) + Simulation.cameraY);
		g.drawString("x=" + mouseTileX + "; y=" + mouseTileY + ";", 0, 50);
		g.drawRect(
		(int)((mouseTileX-cameraX)*tileSize+Main.WindowWidth/2),
		(int)((mouseTileY-cameraY)*tileSize+Main.WindowHeight/2),
		tileSize, tileSize);
		
		//Tile Info
		g.drawString(code.Data.types.get(tiles[mouseTileX] [mouseTileY].type).name + "", 300, 50);
		
		
		//Water Level
		g.setColor(new Color(50, 50, 230));
		g.fillRect(Main.WindowWidth-230, 70, (int)playerWaterLevel*2, 40);
		g.setColor(Color.WHITE);
		g.drawRect(Main.WindowWidth-230, 70, 200, 40);
		g.setFont(new Font("SanSerif", 1, 40));
		g.setColor(Color.BLACK);
		g.drawString("H\u2082O", Main.WindowWidth-230, 105);
		
		//Poison Level
		g.setColor(new Color(150, 180, 80));
		g.fillRect(Main.WindowWidth-230, 120, (int)playerPoisonLevel*2, 40);
		g.setColor(Color.WHITE);
		g.drawRect(Main.WindowWidth-230, 120, 200, 40);
		g.setFont(new Font("SanSerif", 1, 40));
		g.setColor(Color.BLACK);
		g.drawString("Toxins", Main.WindowWidth-230, 155);
		
		//Health Level
		g.setColor(new Color(230, 30, 30));
		g.fillRect(Main.WindowWidth-230, 170, (int)playerHealthLevel*2, 40);
		g.setColor(Color.WHITE);
		g.drawRect(Main.WindowWidth-230, 170, 200, 40);
		g.setFont(new Font("SanSerif", 1, 40));
		g.setColor(Color.BLACK);
		g.drawString("Health", Main.WindowWidth-230, 205);
		
		//Handheld Item
		if(inv.data[playerInvSlotSelected].type != 0) {
			try {
				g.drawImage(code.Data.types.get(inv.data[playerInvSlotSelected].type).img, MouseInfo.getPointerInfo().getLocation().x-32, MouseInfo.getPointerInfo().getLocation().y-32, 64, 64, null);
			}catch(NullPointerException e) {
				System.err.println("Error occured loading image from handheld item with type: " + inv.data[playerInvSlotSelected].type + "!");
			}
		}			
	}
	
	void startGame() {
		if(Frame.dead) {
			System.out.println("you were dead. generating new world");
			Frame.dead = false;
			generateMap();
		}else {
			try {
				readMap();
			} catch (FileNotFoundException e) {
				
				System.out.println("map not found. generating new");
				generateMap();
				
			} catch (NoSuchElementException e) {
				
				System.err.println("map reading err. generating new");
				generateMap();
			}
		}
	}
	
	@SuppressWarnings("resource")
	static void readMap() throws FileNotFoundException, NoSuchElementException{
		
		Scanner scanner;
		
		File file = new File("maps/map.txt");
		scanner = new Scanner(file);
		tiles = new Tile[tileWidth] [tileHeight];
		for(int y = 0; y < tileHeight; y++) {
			for(int x = 0; x < tileWidth; x++) {
				
				int t = scanner.nextInt();
				int o = scanner.nextInt();
				int n = scanner.nextInt();
				int c = scanner.nextInt();
				
				tiles[x] [y] = new Tile(x,y,t,new Gas(o,n,c), random.nextBoolean(), random.nextBoolean());
				
			
			}
		}
		
		playerX = Float.parseFloat(scanner.next());
		playerY = Float.parseFloat(scanner.next());
		playerHealthLevel = Float.parseFloat(scanner.next());
		playerOxygenLevel = Float.parseFloat(scanner.next());
		playerPoisonLevel = Float.parseFloat(scanner.next());
		playerWaterLevel = Float.parseFloat(scanner.next());
		seed = Double.parseDouble(scanner.next());
		for(int i = 0; i<10; i++) {
			inv.data[i].type = scanner.nextInt();
			inv.data[i].count = scanner.nextInt();
			
		}
	}
	
	
	
	static void saveMap() throws IOException{
		
		FileWriter fw = new FileWriter("maps/map.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(int y = 0; y < tileWidth; y++) {
			for(int x = 0; x < tileHeight; x++) {
				bw.write(tiles[x] [y].type + " " + tiles[x] [y].gas.oxygen +  " " + tiles[x] [y].gas.nitrogen + " " + tiles[x] [y].gas.carbon_dioxide + " ");
			}
			bw.write("\n");
		}
		
		bw.write(playerX + " " + playerY + " " + playerHealthLevel + " " + playerOxygenLevel + " " + playerPoisonLevel + " "+ playerWaterLevel + " \n" + seed + "\n");
		for(int i = 0; i<10; i++) {
			bw.write(inv.data[i].type + " " + inv.data[i].count + " ");
		}
		bw.close();
		
		
	}
	
//	static void generateLakes() {
//		
//		for(int x = 10; x < tileWidth-10; x++) {
//			for(int y = 10; y < tileHeight-10; y++) {
//			
//				if(random.nextInt(300) == 0) {
//					
//					for(int dx = (x-3); dx < (x+3); dx++) {
//						for(int dy = (y-3); dy < (y+3); dy++) {
//							if(random.nextBoolean()) {
//								tiles[dx] [dy].type = 1;
//							}
//						}
//					}
//				}
//			
//			}
//		}//prettying
//		for(int i = 0; i < 2; i++) {
//			for(int x = 1; x < tileWidth-1; x++) {
//				for(int y = 1; y < tileHeight-1; y++) {
//					
//					int neibours = 0;
//					if(tiles[x+1] [y+1].type == 1)neibours++;
//					if(tiles[x+1] [y  ].type == 1)neibours++;
//					if(tiles[x+1] [y-1].type == 1)neibours++;
//					if(tiles[x  ] [y+1].type == 1)neibours++;
//					if(tiles[x  ] [y-1].type == 1)neibours++;
//					if(tiles[x-1] [y+1].type == 1)neibours++;
//					if(tiles[x-1] [y  ].type == 1)neibours++;
//					if(tiles[x-1] [y-1].type == 1)neibours++;
//					if(neibours >= 4) {
//						tiles[x] [y].type = 1;
//					}else if(neibours == 3){
//						if(random.nextInt(5)==0)tiles[x] [y].type = 10;
//					}else if(neibours <= 1) {
//						tiles[x] [y].type = 0;
//					}
//					
//				}
//			}
//		}
//		
//	}
//	
//	private static void generateScrap() {
//		
//		for(int x = 0; x < tileWidth; x++) {
//			for(int y = 0; y < tileHeight; y++) {
//				if(tiles[x] [y].type == 0) {
//					if(random.nextInt(20) == 0) {
//						tiles[x] [y].type = 12;
//					}
//					if(random.nextInt(20) == 0) {
//						tiles[x] [y].type = 11;
//					}
//					
//				}
//				
//			}
//		}
//		
//	}
	
	private static void generatePlayerSpawn() {
		
		for(int x = (int)playerX-4; x < (int)playerX+4; x++) {
			for(int y = (int)playerY-4; y < (int)playerY+4; y++) {
				
				tiles[x] [y].type = 0;
				
			}
		}
		tiles[centerX+1] [centerY].type = 20;
		tiles[centerX-3] [centerY].type = 21;
		tiles[centerX-3] [centerY+1].type = 21;
		tiles[centerX-3] [centerY-1].type = 21;
		tiles[centerX-2] [centerY].type = 23;
		tiles[centerX-4] [centerY].type = 21;
		
	}
	
	static int centerX = tileWidth/2;
	static int centerY = tileHeight/2;
	
	static void generateMap() {
		
		playerHealthLevel = 100f;
		playerOxygenLevel = 100f;
		playerPoisonLevel = 0f;
		playerWaterLevel = 100f;
		
		inv.clear();
		//inv.add(22, 5);
		inv.add(30, 1);
		//inv.add(11, 1);
		
		
		
		
		
		playerX = centerX - 2.5f;
		playerY = centerY + 0.5f;
		
		
		tiles = new Tile[tileWidth] [tileHeight];
		for(int x = 0; x < tileWidth; x++) {
			for(int y = 0; y < tileHeight; y++) {
				
				tiles[x] [y] = new Tile(x, y, 0, new Gas(1,95,4), random.nextBoolean(), random.nextBoolean());
				
			}
		}
		
		seed = random.nextDouble();
		BufferedImage img = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
		
		for(double x = 0; x < 1; x += 0.01) {
			for(double y = 0; y < 1; y += 0.01) {
				int noise = (int)(ImprovedNoise.noise(x*10, y*10, seed)*256);
				
				int type = 0;
				if(noise > 120 && noise <= 125) {
					type = 10;//Sand
				}else if(noise > 125 && noise <= 256) {
					type = 1;//lake
				}else if(noise > -256 && noise <= -150) {
					type = 12;//scrap
				}else if(noise > -150 && noise <= -120) {
					type = 11;//rock
				}
				img.setRGB((int)(x*100), (int)(y*100), noise*64);
				tiles[(int)(x*100)] [(int)(y*100)].type = type;
				
			}
		}
		
		
		
		File outputfile = new File("image.png");
		try {
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			System.err.println("file writing err");
		}

		//generateLakes();
		
		//generateScrap();
		
		generatePlayerSpawn();
		
		for(int x = 0; x < tileWidth; x++) {
			for(int y = 0; y < tileHeight; y++) {
				
				if(tiles[x] [y].type == 21) {
					
					tiles[x] [y].gas.oxygen = 100;
					
				}
				
			}
		}
		
	}
}
