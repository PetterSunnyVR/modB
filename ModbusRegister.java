
public class ModbusRegister {
	static private int[] InputRegister;
	
	public ModbusRegister(int numOfRegister) {
		this.InputRegister = new int[numOfRegister];
		for(int i=0; i<numOfRegister; i++) {
			this.InputRegister[i]=0;
		}
	}
	
	public int getInputRegister(int indexOfRegister) {
		if(indexOfRegister<InputRegister.length) {
			return InputRegister[indexOfRegister];
		}
		else{
			System.out.println("ERROR. INDEX OF OF BOUNDS");
			return 0;
		}
		
	}
	
	public void setInputRegister(int indexOfRegister, int valueOfRegister) {
		if(indexOfRegister<InputRegister.length) {
			InputRegister[indexOfRegister]=valueOfRegister;
		}else {
			System.out.println("ERROR. INDEX OF OF BOUNDS");
		}
	}
	
	
	
}
