package algomusicfall2021.jerry.finalproject;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.JMSLRandom;
import com.softsynth.jmsl.MusicShape;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;

public class HatShape extends MusicShape {

	private double amp;

	public HatShape(DimensionNameSpace dimensionNameSpace) {
		super(dimensionNameSpace);
		amp = 0.5;
		// TODO Auto-generated constructor stub
	}

	public void eighthStraight() {
		removeAll();
		for (int i = 0; i < 8; i++) {
			amp = JMSLRandom.choose(0.3, 0.8);
			add(0.5, 60, amp, 0.5);
		}
	}

	public void eighthFill() {
		removeAll();
		for (int i = 0; i < 6; i++) {
			amp = JMSLRandom.choose(0.3, 0.8);
			add(0.5, 60, amp, 0.5);
		}
		add(1, 0, 0, 1);
	}

	public void pickupMeasure() {
		removeAll();
		add(2, 60, amp, 2);
		add(2, 60, amp, 2);
		add(1, 60, amp, 1);
		add(1, 60, amp, 1);
		add(1, 60, amp, 1);
	}

	public void setAmp(double amp) {
		this.amp = amp;
	}

	public void setTempo(double tempo) {
		setTimeStretch(60./tempo);
	}
	
}
