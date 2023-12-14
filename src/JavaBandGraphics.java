package algomusicfall2021.jerry.finalproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.softsynth.jmsl.JMSL;

public class JavaBandGraphics extends JPanel {
	SerialJob serialJob;
	boolean doDrawing = false;
	boolean doPlaying = false;
	double drawMultiplier;
	double[] lengthMultiplier;
	protected boolean isDoDrawing() {
		return doDrawing;
	}
	
	protected void setDoDrawing(boolean b) {
		doDrawing = b;		
	}
	protected boolean isDoPlaying() {
		return doPlaying;
	}
	protected void setDoPlaying(boolean doPlaying) {
		this.doPlaying = doPlaying;
	}
	
	public JavaBandGraphics() {
		super();
		serialJob = new SerialJob();
		drawMultiplier = 2.0;
		doPlaying = false;
		lengthMultiplier = new double[100];
		setPreferredSize(new Dimension(400,300));
		JFrame jFrame = new JFrame("close to quit");
		jFrame.setLayout(new BorderLayout());
		jFrame.add(BorderLayout.NORTH, this);
		
		//set up a button
		JButton enableDrawingButton = new JButton("Java Band!");
		enableDrawingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDoPlaying(!isDoPlaying()); //invert doDrawing
				if (isDoPlaying()) {
					serialJob.launch(JMSL.now());
				} else {
					serialJob.finishAll();
				}
				String msg = isDoPlaying()? "Stop Playing" : "Start Playing";
				setDoDrawing(isDoPlaying());
				enableDrawingButton.setText(msg);
			}
		});
		
		//add button to the bottom
		JPanel p = new JPanel();
		p.add(enableDrawingButton);
		jFrame.add(BorderLayout.SOUTH, p);
		
		jFrame.pack();
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			try {
				Thread.sleep(100); //sleep 3 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			jFrame.repaint();
		}
	}
	public void paintComponent(Graphics g) {
		if (isDoDrawing()) {
			g.setColor(Color.blue);
			for (int i = 0; i < 100; i++) {
				int xPos = (int) (Math.random() * getWidth());
				int yPos = (int) (Math.random()  * getHeight());
				g.drawLine(xPos, yPos, yPos, xPos);
			}
		}
	}
	
	public void setDrawMultiplier(double drawMultiplier) {
		this.drawMultiplier = drawMultiplier;
	}
	
	public static void main(String[] args) {
		JavaBandGraphics g = new JavaBandGraphics();
	}
}
