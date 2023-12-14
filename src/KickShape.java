package algomusicfall2021.jerry.finalproject;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.MusicShape;
import com.softsynth.jmsl.util.EventDistributions;
import com.softsynth.jmsl.util.LinearInterpolator;

public class KickShape extends MusicShape {
	double amp;
	LinearInterpolator interp;

	public KickShape(DimensionNameSpace dimensionNameSpace) {
		super(dimensionNameSpace);
		interp = new LinearInterpolator(0, 1.01, 8, 2.5);
		amp = 0.5;
	}

	public void citypop() {
		removeAll();
		amp = EventDistributions.genEntryDelayMyhill(2, interp.interp(1));
		add(0.25, 0, 0, 0.25);
		add(0.25, 60, amp, 0.25);
		add(0.25, 60, amp / 1.5, 0.25);
		add(0.25, 0, 0, 0.25); // beat1
		amp = EventDistributions.genEntryDelayMyhill(2, interp.interp(2));
		add(0.25, 0, 0, 0.25);
		add(0.25, 60, amp, 0.25);
		add(0.25, 0, 0, 0.25);
		add(0.25, 60, amp / 1.5, 0.25); // beat2
		amp = EventDistributions.genEntryDelayMyhill(2, interp.interp(3));
		add(0.25, 60, amp, 0.25);
		add(0.75, 0, 0, 0.75); // beat3
		amp = EventDistributions.genEntryDelayMyhill(2, interp.interp(4));
		add(0.75, 0, 0, 0.75);
		add(0.25, 60, amp, 0.25); // beat4
	}
	
	public void pickupMeasure() {
		removeAll();
		amp = EventDistributions.genEntryDelayMyhill(2, interp.interp(1));
		add(7.75, 0, 0, 7.75);
		add(0.25, 60, amp, 0.25); // beat4
	}

	public void setAmp(double amp) {
		this.amp = amp;
	}
	
	public void setTempo(double tempo) {
		setTimeStretch(60./tempo);
	}
}
