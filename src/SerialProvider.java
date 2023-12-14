package algomusicfall2021.jerry.finalproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerialProvider implements SerialPortEventListener, ArduinoDataInterface {
	double mappedLX = 0, mappedLY = 0, mappedRX = 0, mappedRY = 0;
	boolean lk = false, rk = false, lastrk = false;
	int currentInstrument = 3;
	ArrayList<Double> doubleAL;
	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
	};
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize() {
		// the next line is for Raspberry Pi and
		// gets us into the while loop and was suggested here was suggested
		// https://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
//                System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = input.readLine();
				doubleAL = parseDoubles(inputLine);
				mappedLX = (doubleAL.size() > 0) ? doubleAL.get(0) : 0.5;
				mappedLY = (doubleAL.size() > 1) ? doubleAL.get(1) : 0.5;
				mappedRX = (doubleAL.size() > 2) ? doubleAL.get(2) : 0.5;
				mappedRY = (doubleAL.size() > 3) ? doubleAL.get(3) : 0.5;
				lk = ((doubleAL.size() > 4) ? doubleAL.get(4) : 0) == 0;
				rk = ((doubleAL.size() > 5) ? doubleAL.get(5) : 0) == 0;
				currentInstrument = (int) ((doubleAL.size() > 6) ? doubleAL.get(6) : 0.);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}

		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	public ArrayList<Double> parseDoubles(String input) {
		ArrayList<Double> inputAL = new ArrayList<Double>();
		Matcher matcher = Pattern.compile("[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?").matcher(input);

		while (matcher.find()) {
			double element = Double.parseDouble(matcher.group());
			inputAL.add(element);
		}
		return inputAL;

	}

	public void dump() {
		System.out.println("-------------------" + "\n" 
				+ "Arduino Data:" + "\n" 
				+ "mappedLX= " + getMappedLX() + "\n"
				+ "mappedLY= " + getMappedLY() + "\n" 
				+ "mappedRX= " + getMappedRX() + "\n" 
				+ "mappedRY= " + getMappedRY() + "\n" 
				+ "lk=" + getLK() + "\n" 
				+ "rk=" + getRK() + "\n" 
				+ "lastrk=" + lastrk + "\n" 
				+ "currentInstrument= " + getCurrentInstruemnt() + "\n"
				+ "-------------------");
	}

	@Override
	public double getMappedLX() {
		// TODO Auto-generated method stub
		return mappedLX;
	}

	@Override
	public double getMappedLY() {
		// TODO Auto-generated method stub
		return mappedLY;
	}

	@Override
	public double getMappedRX() {
		// TODO Auto-generated method stub
		return mappedRX;
	}

	@Override
	public double getMappedRY() {
		// TODO Auto-generated method stub
		return mappedRY;
	}

	@Override
	public boolean getLK() {
		// TODO Auto-generated method stub
		return lk;
	}

	@Override
	public boolean getRK() {
		// TODO Auto-generated method stub
		return rk;
	}

	@Override
	public int getCurrentInstruemnt() {
		// TODO Auto-generated method stub
		return currentInstrument;
	}

	/**
	 * This should be called when you stop using the port. This will prevent port
	 * locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public static void main(String[] args) throws Exception {
		SerialProvider main = new SerialProvider();
		main.initialize();
		Thread t = new Thread() {
			public void run() {
				// the following line will keep this app alive for 1000 seconds,
				// waiting for events to occur and responding to them (printing incoming
				// messages to console).
				try {
					Thread.sleep(1000000);
				} catch (InterruptedException ie) {
				}
			}
		};
		t.start();
//		System.out.println("Started");
	}
}