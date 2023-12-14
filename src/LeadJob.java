package algomusicfall2021.jerry.finalproject;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.Instrument;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.MusicJob;
import com.softsynth.jmsl.MusicShape;
import com.softsynth.jmsl.ParallelCollection;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;

public class LeadJob extends MusicJob {

	LeadShape leadShape;
	MusicShape delayShape;
	double timeStretch;
	JSynUnitVoiceInstrument leadIns, delayIns;
	DimensionNameSpace dns;
	double delayTime;
	int mode;
	int currentBeat;

	public LeadJob() {
		leadIns = new JSynUnitVoiceInstrument(1, SawLaserBass.class.getName());
		delayIns = new JSynUnitVoiceInstrument(1, DelayWithFeedback.class.getName());
		delayIns.addSignalSource(leadIns.getOutput());
		dns = leadIns.getDimensionNameSpace();
		setInstrument(leadIns);
		setRepeats(1024);
		leadShape = new LeadShape(dns);
		delayShape = new MusicShape(delayIns.getDimensionNameSpace());
		mode = 0;
		delayTime = 0.5;
		currentBeat = 0;
	}

	@Override
	public double start(double playTime) {
		if (getInstrument() != null) {
			if (dns != null) {
				leadShape.pickupMeasure();
				for (int i=0; i<leadShape.size(); i++) {
					double[] dar = leadShape.get(i);
					playTime = getInstrument().play(playTime, getTimeStretch(), dar);
				}
			}
		}
		return playTime;

	}

	@Override
	// LeadJob is supposed to repeat at every beat
	// if mode is set to non-zero by SerialJob (right click)
	// 		the lead instrument should play the phrase, else rest
	public double repeat(double playTime) {
		if (getInstrument() != null) {
			// set up the delay instrument
			double[] dar0 = { 1, 60, 0.5, 1, 0, 0.5, delayTime };
			delayIns.play(JMSL.now(), getTimeStretch(), dar0);
			// start a LeadShape with any one-beat phrase
			if (dns != null) {
				leadShape = new LeadShape(dns);
				int chordIndex = (getRepeatCount() % (4 * 4)) / 4;
				switch (mode) {
				default:
					leadShape.rest();
					break;
				case 1:
					leadShape.chop1(chordIndex);
					break;
				case 2:
					leadShape.chop2(chordIndex);
					break;
				case 3:
					leadShape.chop3(chordIndex);
					break;
				case 4:
					leadShape.chop4(chordIndex);
					break;
				}
				// write the LeadShape into the lead instrument
				for (int i = 0; i < leadShape.size(); i++) {
					double[] dar = leadShape.get(i);
					playTime = getInstrument().play(playTime, getTimeStretch(), dar);
				}
			}
		}
		return playTime;
	}

	public LeadShape getLeadShape() {
		return leadShape;
	}

	public void setTempo(double tempo) {
		leadShape.setTempo(tempo);
	}

	@Override
	public Instrument getInstrument() {
		return leadIns;
	}

	public Instrument getSignalSourceInstrument() {
		return delayIns;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setDelayTime(double delayTime) {
		this.delayTime = delayTime;
	}

	public static void main(String[] args) {
		JMSL.clock.setAdvance(1);
		JSynMusicDevice.instance().open();
		LeadJob leadJob = new LeadJob();
		leadJob.setTimeStretch(1);
		JMSLMixerContainer mixer = new JMSLMixerContainer();
		mixer.start();
		mixer.addInstrument(leadJob.getInstrument(), 0, 0.5);
		mixer.addInstrument(leadJob.getSignalSourceInstrument(), 1, 0.5);
		leadJob.launch(JMSL.now());
	}

}