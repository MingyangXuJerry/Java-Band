package algomusicfall2021.jerry.finalproject;

import java.util.Random;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.MusicShape;
import com.softsynth.jmsl.util.EventDistributions;
import com.softsynth.jmsl.util.LinearInterpolator;

public class BassShape extends MusicShape {
	double amp, modFreq, modAmp, LPF_Cut;
	double[] chordArray;
	private double[] rest = { 0.25, 0, 0, 0.25, 0, 0, 0 };
	private Random rg;
	LinearInterpolator interp;

	public BassShape(DimensionNameSpace dimensionNameSpace) {
		super(dimensionNameSpace);
		interp = new LinearInterpolator(0, 1.01, 8, 2.5);
		amp = 0.5;
		chordArray = new double[4];
		chordArray[0] = 60 - 24;
		chordArray[1] = 57 - 24;
		chordArray[2] = 58 - 24;
		chordArray[3] = 53 - 24;
		modFreq = 500;
		modAmp = 800;
		LPF_Cut = 4000;
		rg = new Random();
	}

	public void citypop(int chordIndex) {
		removeAll();
		amp = EventDistributions.genEntryDelayMyhill(2, interp.interp(1));
		add(rest);
		add(0.25, chordArray[chordIndex], amp, 0.25, modFreq, modAmp, LPF_Cut);
		add(0.25, chordArray[chordIndex], amp / 1.5, 0.25, modFreq, modAmp, LPF_Cut);
		add(rest); // beat1
		amp = EventDistributions.genEntryDelayMyhill(2, interp.interp(2));
		add(rest);
		add(0.25, chordArray[chordIndex], amp, 0.25, modFreq, modAmp, LPF_Cut);
		add(rest);
		add(0.25, chordArray[chordIndex], amp / 1.5, 0.25, modFreq, modAmp, LPF_Cut); // beat2
		amp = EventDistributions.genEntryDelayMyhill(2, interp.interp(3));
		add(0.25, chordArray[chordIndex], amp, 0.25, modFreq, modAmp, LPF_Cut);
		add(rest);
		add(rest);
		add(rest); // beat3
		amp = EventDistributions.genEntryDelayMyhill(2, interp.interp(4));
		if (rg.nextBoolean()) {
			add(rest);
			add(rest);
			add(rest);
		} else {
			add(0.5, chordArray[chordIndex], amp, 0.5, modFreq, modAmp, LPF_Cut);
			add(0.25, chordArray[chordIndex]+7, amp, 0.25, modFreq, modAmp, LPF_Cut);
		}
		add(0.25, chordArray[(chordIndex + 1) % 4], amp, 0.25, modFreq, modAmp, LPF_Cut);
	}

	public void pickupMeasure() {
		removeAll();
		amp = EventDistributions.genEntryDelayMyhill(2, interp.interp(1));
		add(7.25, 0, 0, 7.25, modFreq, modAmp, LPF_Cut);
		add(0.25, chordArray[0], amp, 0.25, modFreq, modAmp, LPF_Cut);
		add(rest);
		add(0.25, chordArray[0], amp, 0.25, modFreq, modAmp, LPF_Cut); // beat4
	}

	public void setAmp(double amp) {
		this.amp = amp;
	}

	public void setModFreq(double modFreq) {
		this.modFreq = modFreq;
	}

	public void setModAmp(double modAmp) {
		this.modAmp = modAmp;
	}

	public void setLPF_Cut(double LPF_Cut) {
		this.LPF_Cut = LPF_Cut;
	}

	public void setTempo(double tempo) {
		setTimeStretch(60./tempo);
	}
}
