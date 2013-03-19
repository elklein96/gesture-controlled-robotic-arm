import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

@SuppressWarnings("restriction")
public class SerialTest implements SerialPortEventListener {
	SerialPort serialPort;
	
	public static boolean connected;
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007W3x", // Mac OS X
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
			};
	// Buffered input stream from the port //
	private InputStream input;
	// The output stream to the port //
	private static OutputStream output;
	// Milliseconds to block while waiting for port open //
	private static final int TIME_OUT = 2000;
	// Default bits per second for COM port. //
	private static final int DATA_RATE = 9600;

	@SuppressWarnings("rawtypes")
	public void initialize() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					connected = true;
					break;
				}
			}
		}

		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);

			serialPort.setSerialPortParams(DATA_RATE,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				int available = input.available();
				byte chunk[] = new byte[available];
				input.read(chunk, 0, available);

				System.out.print(new String(chunk));
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		SerialTest main = new SerialTest();
		main.initialize();
		System.out.println("Started");
		Thread.sleep(1500);
		if(connected == true){
			System.out.println("Connected");
		}
		else if(connected == false){
			System.out.println("Connection Failed");
		}
	}

	public static void passy(int args) throws IOException{
		int datay; 
		int out = 0;
		
		datay = args;
		if (datay > 0){
			out = 1;
		}
		if (datay < 0){
			out = 2;
		}
		if (datay == 0){
			out = 8;
		}
		write(out);
	}
	
	public static void passx(int args) throws IOException{
		int datax;
		int out = 0;
		
		datax = args;
		if (datax > 0){
			out = 3;
		}
		else if (datax < 0){
			out = 4;
		}
		if (datax == 0){
			out = 9;
		}
		write(out);
	}
	
	public static void claw(int args) throws IOException{
		write(args);
	}
	
	public static void write(int out) throws IOException{
		if(connected == true){
			output.write(out);
			System.out.println(out);
			String output = Integer.toString(out);
			trackMouse.fillLog(output);
		}
		else{
			System.out.println(out);
			String output = Integer.toString(out);
			trackMouse.fillLog(output);
		}
	}
}