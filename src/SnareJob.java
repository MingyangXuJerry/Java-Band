package algomusicfall2021.jerry.finalproject;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.MusicJob;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;

public class SnareJob extends MusicJob {

	SnareShape snareShape;
	double timeStretch;
	JSynUnitVoiceInstrument snareIns;
	DimensionNameSpace dns;
	
	public SnareJob() {
		snareIns = new JSynUnitVoiceInstrument(1, Snare.class.getName());
		dns = snareIns.getDimensionNameSpace();
		timeStretch = 1.0;
		setInstrument(snareIns);
		setRepeats(1024);
		snareShape = new SnareShape(dns);
	}
	
	@Override
	public double start(double playTime) {
		if (getInstrument() != null) {
			if (dns != null) {
				snareShape.pickupMeasure();
				for (int i=0; i<snareShape.size(); i++) {
					double[] dar = snareShape.get(i);
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
				snareShape = new SnareShape(dns);
				if (getRepeatCount() % 4 >= 3) {
					snareShape.sixTeenthFill();;
				} else {
					snareShape.eighthStraight();
				}
				for (int i=0; i<snareShape.size(); i++) {
					double[] dar = snareShape.get(i);
					playTime = getInstrument().play(playTime, getTimeStretch(), dar);
				}
			}
		}
		return playTime;
	}

	public void setTempo(double tempo) {
		snareShape.setTempo(tempo);
	}
	
	public static void main(String[] args) {
		JMSL.clock.setAdvance(1);
		JSynMusicDevice.instance().open();
		SnareJob snareJob = new SnareJob();
		JMSLMixerContainer mixer = new JMSLMixerContainer();
		mixer.start();
		mixer.addInstrument(snareJob.getInstrument());
		snareJob.launch(JMSL.now());
	}
}
