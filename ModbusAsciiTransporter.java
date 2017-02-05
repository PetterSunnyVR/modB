
public class ModbusAsciiTransporter {

	private ModbusRegister register;
	int[] decValuesMessage = new int[6];
	int byteCount = 2;
	
	private enum Message{
		SlaveAddr(0), 
		Func(1),
		StartAdrHi(2), 
		StartAdrLo(3), 
		NumHi(4), 
		NumLo(5);
		
		private final int id;
		Message(int id){this.id=id;}
		public int val() {return id;}
	}
	
	public ModbusAsciiTransporter(ModbusRegister register) {
		this.register = register;
		// TODO Auto-generated constructor stub
	}

	public String[] createResponse(String message) {
		this.decValuesMessage = receiveMessageToArray(message);
		if(decValuesMessage[Message.StartAdrLo.val()]>0) {
			int reg;
			//create array of responses
			String[] responseArray = new String[decValuesMessage[Message.NumLo.val()]];
			//build response - append :
			StringBuilder response = new StringBuilder(":");
			//add address of the slave
			if(decValuesMessage[Message.SlaveAddr.val()]<16)
				response.append("0"+Integer.toHexString(decValuesMessage[Message.SlaveAddr.val()]).toUpperCase());
			else
				response.append(Integer.toHexString(decValuesMessage[Message.SlaveAddr.val()]).toUpperCase());
			//add func
			if(decValuesMessage[Message.Func.val()]<16)
				response.append("0"+Integer.toHexString(decValuesMessage[Message.Func.val()]).toUpperCase());
			else {
				response.append(Integer.toHexString(decValuesMessage[Message.Func.val()]).toUpperCase());
			}
			// every message from input reg is 2 bytes. 00 - assuming that the valu is only in low byte.
			response.append("0"+byteCount);
			//free stringbuilder by saving the result for every response as part1
			String responsePart1 = response.toString();
			//read from register to create specific responses
			for(int i=0 ; i < responseArray.length; i++) {
				reg = register.getInputRegister(decValuesMessage[Message.StartAdrLo.val()]+1+i);
				if(reg<16) {
					response.append("000"+Integer.toHexString(reg).toUpperCase());
				}else if(reg<256) {
					response.append("00"+Integer.toHexString(reg).toUpperCase());
				}else if(reg<4096) {
					response.append("0"+Integer.toHexString(reg).toUpperCase());
				}else {
					response.append(Integer.toHexString(reg).toUpperCase());
				}
				//calculate LRC
				int lrcArr = decValuesMessage[Message.SlaveAddr.val()]+decValuesMessage[Message.Func.val()]+byteCount+reg;
				System.out.println("A "+lrcArr);
				response.append(calculateLRC(lrcArr).toUpperCase());
				response.append("0D0A");
				responseArray[i]=response.toString();
				
				response.setLength(0);
				response.append(responsePart1);
			}
			return responseArray;
			
		}else {
			return new String[1];
		}
		
	}

	private int[] receiveMessageToArray(String msg) {
		int[] decValues = new int[6];
		for (int i = 0; i < 6; i++) {
			decValues[i] = Integer.valueOf(msg.substring(i * 2 + 1, i * 2 + 3), 16);
		}
		return decValues;
	}
	
	private String calculateLRC(int decValueMessage) {
		
		String LRC = Integer.toHexString(-decValueMessage);
		String LRCHex = LRC.substring(LRC.length()-2, LRC.length());
		if(LRCHex.length()<2)
			return "0"+LRCHex;
		else
			return LRCHex;
		
	}
	

}
