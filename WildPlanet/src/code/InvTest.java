package code;

public class InvTest {

	public static Inventory inv;
	
	public static void main(String[] args) {
		
		inv = new Inventory();
		
		inv.add(3, 8);
		inv.add(3, 4);
		//System.out.println(inv.add(3, 4));
		System.out.println(inv.take(3, 15));
		
		
		for(int i = 0; i < inv.data.length; i++) {
			System.out.println(inv.data[i].type + ": " + inv.data[i].count + ";");
		}
	}

}
