package code;

public class Inventory {
	
	public Slot[] data;
	
	public Inventory() {
		
		data = new Slot[10];
		for(int i = 0; i < data.length; i++) {
			data[i] = new Slot(0,0);
			
		}
		
	}
	
	public void clear() {
		
		for(int i = 0; i < data.length; i++) {
			data[i].type = 0;
			data[i].count = 0;
			
		}
	}
	
	public int add(int type, int count) {
		
		for(int i = 0; i < data.length; i++) {
			
			if(data[i].type == type || data[i].type == 0) {
				data[i].type = type;
				int space = 10-(data[i].count + count);
				if(space >= 0) {
					data[i].count += count;
					return 0;
				}else {
					data[i].count = 10;
					count = -1*space;
				}
			}
			
		}
		
		return count;
	}
	
	public int take(int type, int count) {

		for(int i = 0; i < data.length; i++) {
			
			if(data[i].type == type) {
				
				int space = data[i].count - count;
				if(space > 0) {
					data[i].count -= count;
					return 0;
				}else {
					data[i].count = 0;
					data[i].type = 0;
					count = -1*space;
				}
			}
			
		}
		
		return count;
	}
}

class Slot {
	
	int type = 0;
	int count = 0;
	
	public Slot(int type, int count) {
		
		this.type = type;
		this.count = count;
		
	}
	
	
	
}