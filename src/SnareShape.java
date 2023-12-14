package algomusicfall2021.jerry.finalproject;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.JMSLRandom;
import com.softsynth.jmsl.MusicShape;

public class SnareShape extends MusicShape {

	double amp;
	
	public SnareShape(DimensionNameSpace dimensionNameSpace) {
		super(dimensionNameSpace);
		amp = 0.5;
		// TODO Auto-generated constructor stub
	}
	
	public void setAmp(double amp) {
		this.amp = amp;
	}

	public void eighthStraight() {
		removeAll();
		amp = JMSLRandom.choose(0.4, 0.6);
		add(1, 0, 0, 1);
		add(1, 60, amp, 1);
		amp = JMSLRandom.choose(0.4, 0.6);
		add(1, 0, 0, 1);
		add(1, 60, amp, 1);
	}
	
	public void sixTeenthFill() {
		removeAll();
		amp = JMSLRandom.choose(0.4, 0.6);
		add(1, 0, 0, 1);
		add(1, 60, amp, 1);
		amp = JMSLRandom.choose(0.4, 0.6);
		add(1, 0, 0, 1);
		add(0.25, 60, amp, 0.25);
		add(0.25, 60, amp*1.15, 0.25);
		add(0.25, 60, amp*1.3, 0.25);
		add(0.25, 60, amp*1.5, 0.25);
	}
	
	public void pickupMeasure() {
		removeAll();
		add(7, 0, 0, 7);
		amp = JMSLRandom.choose(0.4, 0.6);
		add(0.25, 60, amp, 0.25);
		add(0.25, 60, amp*1.15, 0.25);
		add(0.25, 60, amp*1.3, 0.25);
		add(0.25, 60, amp*1.5, 0.25);
	}
	

	public void setTempo(double tempo) {
		setTimeStretch(60./tempo);
	}
}
