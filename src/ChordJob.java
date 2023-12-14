package algomusicfall2021.jerry.finalproject;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.Instrument;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.MusicJob;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;

public class ChordJob extends MusicJob {

	ChordShape chordShape;
	double timeStretch;
	JSynUnitVoiceInstrument chordIns;
	DimensionNameSpace dns;
	
	public ChordJob() {
		chordIns = new JSynUnitVoiceInstrument(1, SineDreamChord.class.getName());
		dns = chordIns.getDimensionNameSpace();
		timeStretch = 1.0;
		setInstrument(chordIns);
		setRepeats(1024);
		chordShape = new ChordShape(dns);
	}
	
	@Override
	public double start(double playTime) {
		if (getInstrument() != null) {
			if (dns != null) {
				chordShape.pickupMeasure();
				for (int i=0; i<chordShape.size(); i++) {
					double[] dar = chordShape.get(i);
					playTime = getInstrument().play(playTime, getTimeStretch(), dar);
				}
			}
		}
		return playTime;

	}
	
	@Override
	public double repeat(double playTime) {
		if (getInstrument() != null) {
			if (dns != null) {
				chordShape.wholeNote(getRepeatCount()%4);
				for (int i=0; i<chordShape.size(); i++) {
					double[] dar = chordShape.get(i);
					playTime = getInstrument().play(playTime, getTimeStretch(), dar);
				}
			}
		}
		return playTime;
	}
	
	@Override
	public Instrument getInstrument() {
		return chordIns;
	}

	public void setTempo(double tempo) {
		chordShape.setTempo(tempo);
	}
	
	public static void main(String[] args) {
		JMSL.clock.setAdvance(1);
		JSynMusicDevice.instance().open();
		ChordJob chordJob = new ChordJob();
		JMSLMixerContainer mixer = new JMSLMixerContainer();
		mixer.start();
		mixer.addInstrument(chordJob.getInstrument());
		chordJob.launch(JMSL.now());
	}

}