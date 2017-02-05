
public class app {
	
	private static int[] decValues = new int[6];
	static String[] response;
	public static void main(String[] args) {
		String msg = ":0A0400080001E20D0A";
		ModbusRegister register = new ModbusRegister(10);
		register.setInputRegister(9, 100);
		ModbusAsciiTransporter transporter = new ModbusAsciiTransporter(register);
		long time = System.currentTimeMillis();
		response = transporter.createResponse(msg);
		System.out.println(response[0]);
		//byte[] b = msg.substring(1,13).getBytes();
		//System.out.println("HEX Result: " +b[0]);
	}
	
}
