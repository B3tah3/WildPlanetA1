package code;

public class Tile {
	
	int x,y;
	int type;
	//float heat = -50f;
	
	Gas gas       = new Gas(0,0,0);
	Gas nextGas   = new Gas(0,0,0);
	Gas storedGas = new Gas(0,0,0);
	
	int light = 0;
	
	boolean rx = false;
	boolean ry = false;
	
	public Tile(int x, int y, int type, Gas gas, boolean rx, boolean ry) {
		
		this.x=x;
		this.y=y;
		this.type = type;
		this.gas = gas;
		this.rx = rx;
		this.ry = ry;
		
	}
	
	
}
