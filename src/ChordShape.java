package algomusicfall2021.jerry.finalproject;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.MusicShape;

public class ChordShape extends MusicShape {
	double amp, hold, modFreq, modAmp, delayInterval, reverseFilter, stereoDelayTime;
	double[] chordArray;

	public ChordShape(DimensionNameSpace dimensionNameSpace) {
		super(dimensionNameSpace);
		amp = 0.5;
		hold = 4;
		reverseFilter = 1.0;
		modFreq = 220;
		modAmp = 100;
		delayInterval = 0.1;
		stereoDelayTime = 0.5;
		chordArray = new double[4];
		chordArray[0] = 60;
		chordArray[1] = 57;
		chordArray[2] = 58;
		chordArray[3] = 53;
	}

	public void wholeNote(int chordIndex) {
		removeAll();
		prefab(1);
		set(4,0,0);
		set(chordArray[chordIndex],0,1);
		set(amp,0,2);
		set(hold,0,3);
		set(modFreq,0,4);
		set(modAmp,0,5);
		set(delayInterval,0,6);
		set(reverseFilter,0,7);
		set(stereoDelayTime,0,8);
	}
	
	public void pickupMeasure() {
		removeAll();
		prefab(1);
		set(8,0,0);
		set(0,0,1);
		set(8,0,3);
	}

	public void setAmp(double amp) {
		this.amp = amp;
	}

	public void setChordArray(double[] chordArray) {
		this.chordArray = chordArray;
	}

	public void setHold(double hold) {
		this.hold = hold;
	}

	public void setReverseFilter(double reverseFilter) {
		this.reverseFilter = reverseFilter;
	}

	public void setModFreq(double modFreq) {
		this.modFreq = modFreq;
	}

	public void setModAmp(double modAmp) {
		this.modAmp = modAmp;
	}

	public void setDelayInterval(double delayInterval) {
		this.delayInterval = delayInterval;
	}

	public void setStereoDelayTime(double stereoDelayTime) {
		this.stereoDelayTime = stereoDelayTime;
	}

	public void setTempo(double tempo) {
		setTimeStretch(60./tempo);
	}
}
