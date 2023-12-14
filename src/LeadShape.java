package algomusicfall2021.jerry.finalproject;

import java.util.Random;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.JMSLRandom;
import com.softsynth.jmsl.MusicShape;
import com.softsynth.jmsl.util.EventDistributions;
import com.softsynth.jmsl.util.ExponentialInterpolator;
import com.softsynth.jmsl.util.LinearInterpolator;

public class LeadShape extends MusicShape {
	double amp, modFreq, modAmp, LPF_Cut;
	double[] chordArray;
	private int[] stepArray = { 0, 2, 4, 5, 7, 9, 11, 12, 14, 16 };
	private Random rg;
	ExponentialInterpolator interp;

	public LeadShape(DimensionNameSpace dimensionNameSpace) {
		super(dimensionNameSpace);
		interp = new ExponentialInterpolator(0, 10, 1, 12000);
		amp = 0.5;
		chordArray = new double[4];
		chordArray[0] = 60 + 12;
		chordArray[1] = 57 + 12;
		chordArray[2] = 58 + 12;
		chordArray[3] = 53 + 12;
		modFreq = 500;
		modAmp = 800;
		LPF_Cut = 4000;
		rg = new Random();
	}

	public void rest() {
		prefab(1);
		set(1, 0, 0);
		set(1, 0, 3);
		set(0, 0, 1);
	}

	public void chop1(int chordIndex) {
		removeAll();
		int pitchIndex = (int) JMSLRandom.choose(0, stepArray.length - 2);
		double pitch = chordArray[chordIndex] + stepArray[pitchIndex];
		double[] dar = { 0.5, pitch, amp, JMSLRandom.choose(0.1, 0.5), modFreq,
				modAmp, LPF_Cut };
		add(dar);
		add(dar.clone());
		set(chordArray[chordIndex] + stepArray[pitchIndex + 1], 1, 1);
	}

	public void chop2(int chordIndex) {
		removeAll();
		int pitchIndex = (int) JMSLRandom.choose(0, stepArray.length - 3);
		double pitch = chordArray[chordIndex] + stepArray[pitchIndex];
		double[] dar = { 0.33, pitch, amp, JMSLRandom.choose(0.1, 0.33), modFreq,
				modAmp, LPF_Cut };
		add(dar);
		add(dar.clone());
		add(dar.clone());
		set(chordArray[chordIndex] + stepArray[pitchIndex + 1], 1, 1);
	}

	public void chop3(int chordIndex) {
		removeAll();
		int pitchIndex = (int) JMSLRandom.choose(0, stepArray.length - 5);
		double pitch = chordArray[chordIndex] + stepArray[pitchIndex];
		double[] dar = { 0.25, pitch, amp, JMSLRandom.choose(0.1, 0.33), modFreq,
				modAmp, LPF_Cut };
		add(dar);
		add(dar.clone());
		add(dar.clone());
		add(dar.clone());
		set(chordArray[chordIndex] + stepArray[pitchIndex + 1], 1, 1);
		set(chordArray[chordIndex] + stepArray[pitchIndex + 3], 2, 1);
		set(0, 3, 1);
	}

	public void chop4(int chordIndex) {
		removeAll();
		int pitchIndex = (int) JMSLRandom.choose(5, stepArray.length - 1);
		double pitch = chordArray[chordIndex] + stepArray[pitchIndex];
		double[] dar = { 0.25, pitch, amp, JMSLRandom.choose(0.1, 0.33), modFreq,
				modAmp, LPF_Cut };
		add(dar);
		add(dar.clone());
		add(dar.clone());
		add(dar.clone());
		set(chordArray[chordIndex] + stepArray[pitchIndex - 1], 1, 1);
		set(chordArray[chordIndex] + stepArray[pitchIndex - 3], 2, 1);
		set(chordArray[chordIndex] + stepArray[pitchIndex - 5], 3, 1);
	}

	public void pickupMeasure() {
		removeAll();
		add(8, 0, 0, 8, 0, 0, 0);
	}
	
	public void setChordArray(double[] chordArray) {
		this.chordArray = chordArray;
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
		setTimeStretch(60. / tempo);
	}
}
