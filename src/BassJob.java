package algomusicfall2021.jerry.finalproject;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.MusicJob;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;

public class BassJob extends MusicJob {

	BassShape bassShape;
	double timeStretch;
	JSynUnitVoiceInstrument bassIns;
	DimensionNameSpace dns;
	
	public BassJob() {
		bassIns = new JSynUnitVoiceInstrument(1, SawLaserBass.class.getName());
		dns = bassIns.getDimensionNameSpace();
		setInstrument(bassIns);
		setRepeats(1024);
		bassShape = new BassShape(dns);
	}
	
	@Override
	public double start(double playTime) {
		if (getInstrument() != null) {
			if (dns != null) {
				bassShape.pickupMeasure();
				for (int i=0; i<bassShape.size(); i++) {
					double[] dar = bassShape.get(i);
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
				bassShape = new BassShape(dns);
				bassShape.citypop(getRepeatCount()%4);
				for (int i=0; i<bassShape.size(); i++) {
					double[] dar = bassShape.get(i);
					playTime = getInstrument().play(playTime, getTimeStretch(), dar);
				}
			}
		}
		return playTime;
	}

	public void setTempo(double tempo) {
		bassShape.setTempo(tempo);
	}
	
	public static void main(String[] args) {
		JMSL.clock.setAdvance(1);
		JSynMusicDevice.instance().open();
		BassJob bassJob = new BassJob();
		bassJob.setTempo(120.);
		JMSLMixerContainer mixer = new JMSLMixerContainer();
		mixer.start();
		mixer.addInstrument(bassJob.getInstrument());
		bassJob.launch(JMSL.now());
	}

}