package algomusicfall2021.jerry.finalproject;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.MusicJob;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;

public class HatJob extends MusicJob {

	HatShape hatShape;
	double timeStretch;
	JSynUnitVoiceInstrument hatIns;
	DimensionNameSpace dns;

	public HatJob() {
		hatIns = new JSynUnitVoiceInstrument(1, Hat.class.getName());
		dns = hatIns.getDimensionNameSpace();
		timeStretch = 1.0;
		setInstrument(hatIns);
		setRepeats(1024);
		hatShape = new HatShape(dns);
	}

	@Override
	public double start(double playTime) {
		if (getInstrument() != null) {
			if (dns != null) {
				hatShape.pickupMeasure();
				for (int i=0; i<hatShape.size(); i++) {
					double[] dar = hatShape.get(i);
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
				hatShape = new HatShape(dns);
				if (getRepeatCount() % 4 >= 3) {
					hatShape.eighthFill();
				} else {
					hatShape.eighthStraight();
				}
				for (int i = 0; i < hatShape.size(); i++) {
					double[] dar = hatShape.get(i);
					playTime = getInstrument().play(playTime, getTimeStretch(), dar);
				}
			}
		}
		return playTime;
	}
	
	public void setTempo(double tempo) {
		hatShape.setTempo(tempo);
	}

	public static void main(String[] args) {
		JMSL.clock.setAdvance(1);
		JSynMusicDevice.instance().open();
		HatJob hatJob = new HatJob();
		hatJob.setTempo(120);
		JMSLMixerContainer mixer = new JMSLMixerContainer();
		mixer.start();
		mixer.addInstrument(hatJob.getInstrument());
		hatJob.launch(JMSL.now());
	}

}
