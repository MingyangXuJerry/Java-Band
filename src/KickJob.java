package algomusicfall2021.jerry.finalproject;

import com.softsynth.jmsl.DimensionNameSpace;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.MusicJob;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;

public class KickJob extends MusicJob {

	KickShape kickShape;
	double timeStretch;
	JSynUnitVoiceInstrument kickIns;
	DimensionNameSpace dns;
	
	public KickJob() {
		kickIns = new JSynUnitVoiceInstrument(1, Kick.class.getName());
		dns = kickIns.getDimensionNameSpace();
		timeStretch = 1.0;
		setInstrument(kickIns);
		setRepeats(1024);
		kickShape = new KickShape(dns);
	}
	
	@Override
	public double start(double playTime) {
		if (getInstrument() != null) {
			if (dns != null) {
				kickShape.pickupMeasure();
				for (int i=0; i<kickShape.size(); i++) {
					double[] dar = kickShape.get(i);
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
				kickShape = new KickShape(dns);
				kickShape.citypop();
				for (int i=0; i<kickShape.size(); i++) {
					double[] dar = kickShape.get(i);
					playTime = getInstrument().play(playTime, getTimeStretch(), dar);
				}
			}
		}
		return playTime;
	}
	
	public void setTempo(double tempo) {
		kickShape.setTempo(tempo);
	}
	
	public static void main(String[] args) {
		JMSL.clock.setAdvance(1);
		JSynMusicDevice.instance().open();
		KickJob kickJob = new KickJob();
		JMSLMixerContainer mixer = new JMSLMixerContainer();
		mixer.start();
		mixer.addInstrument(kickJob.getInstrument());
		kickJob.launch(JMSL.now());
	}

}