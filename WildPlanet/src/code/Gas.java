package code;

public class Gas {
	
	int oxygen = 0;
	int nitrogen = 0;
	int carbon_dioxide = 0;
	//int pressure = 0;
	
	public Gas(int oxygen, int nitrogen, int carbon_dioxide) {
		
		this.oxygen = oxygen;
		this.nitrogen = nitrogen;
		this.carbon_dioxide = carbon_dioxide;
		//pressure = this.oxygen + this.nitrogen + this.carbon_dioxide;
	}
	
//	void takeGas(char type, int amount) {
//		
//		if(type == 'o') {
//			this.oxygen -= amount;
//			if(this.oxygen < 0);
//		}else if(type == 'n') {
//			this.nitrogen -= amount;
//		}else if(type == 'c') {
//			this.carbon_dioxide -= amount;
//		}
//		
//		
//	}
//	
}
