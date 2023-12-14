package algomusicfall2021.jerry.finalproject;

import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.MusicJob;
import com.softsynth.jmsl.ParallelCollection;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;

public class JavaBand {

	static JMSLMixerContainer mixer;
	static ChordJob chordJob;
	static BassJob bassJob;
	static HatJob hatJob;
	static SnareJob snareJob;
	static KickJob kickJob;
	static LeadJob leadJob;
	static MusicJob[] musicJobArray;
	static double[] ampArr;
	static double[] panArr;
	static double[] ampScaler;
	static ParallelCollection parBand, parLead;
	static double tempo;

	public JavaBand() {
		tempo = 100.;
		JMSL.clock.setAdvance(5);
		JSynMusicDevice.instance().open();
		leadJob = new LeadJob();
		chordJob = new ChordJob();
		bassJob = new BassJob();
		hatJob = new HatJob();
		snareJob = new SnareJob();
		kickJob = new KickJob();
		musicJobArray = new MusicJob[6];
		musicJobArray[0] = leadJob;
		musicJobArray[1] = chordJob;
		musicJobArray[2] = bassJob;
		musicJobArray[3] = hatJob;
		musicJobArray[4] = snareJob;
		musicJobArray[5] = kickJob;
		parBand = new ParallelCollection();
		parLead = new ParallelCollection();
		mixer = new JMSLMixerContainer();
		mixer.start();
		ampArr = new double[6];
		ampArr[0] = 0.5;
		ampArr[1] = 0.5;
		ampArr[2] = 0.5;
		ampArr[3] = 0.5;
		ampArr[4] = 0.5;
		ampArr[5] = 0.5;
		panArr = new double[6];
		panArr[0] = 0.2;
		panArr[1] = 0.6;
		panArr[2] = 0.45;
		panArr[3] = 0.3;
		panArr[4] = 0.6;
		panArr[5] = 0.55;

		ampScaler = new double[6];
		ampScaler[0] = 0.2;
		ampScaler[1] = 0.5;
		ampScaler[2] = 0.5;
		ampScaler[3] = 0.5;
		ampScaler[4] = 1;
		ampScaler[5] = 1;

		mixer.addInstrument(leadJob.getInstrument(), panArr[0], ampArr[0] * ampScaler[0]);
		parLead.add(leadJob);
		leadJob.setTimeStretch(60. / tempo);
		for (int i = 1; i < musicJobArray.length; i++) {
			mixer.addInstrument(musicJobArray[i].getInstrument(), panArr[i], ampArr[i] * ampScaler[i]);
			parBand.add(musicJobArray[i]);
			musicJobArray[i].setTimeStretch(60. / tempo);
		}
		mixer.addInstrument(leadJob.getSignalSourceInstrument(), 1. - panArr[0], ampArr[0] * ampScaler[0]);
	}

	public LeadShape getLeadShape() {
		return leadJob.leadShape;
	}

	public LeadJob getLeadJob() {
		return leadJob;
	}

	public ChordShape getChordShape() {
		return chordJob.chordShape;
	}

	public BassShape getBassShape() {
		return bassJob.bassShape;
	}

	public HatShape getHatShape() {
		return hatJob.hatShape;
	}

	public SnareShape getSnareShape() {
		return snareJob.snareShape;
	}

	public KickShape getKickShape() {
		return kickJob.kickShape;
	}

	public MusicJob[] getMusicShapeArray() {
		return musicJobArray;
	}

	public void setTempo(double tempo) {
		for (int i = 0; i < musicJobArray.length; i++) {
			musicJobArray[i].setTimeStretch(60. / tempo);
		}
	}

	public void setAmp(int i, double amp) {
		ampArr[i] = amp;
		int fixedIndex = (i > 1) ? i + 1 : i;
		mixer.panAmpChange(fixedIndex, panArr[i], ampArr[i] * ampScaler[i]);
		if (i == 0) {
			mixer.panAmpChange(mixer.getNumFaders() - 1, 1 - panArr[0], ampArr[i] * ampScaler[i]);
		}
	}

	public void setPan(int i, double pan) {
		panArr[i] = pan;
		int fixedIndex = (i > 1) ? i + 1 : i;
		mixer.panAmpChange(fixedIndex, panArr[i], ampArr[i] * ampScaler[i]);
		if (i == 0) {
			mixer.panAmpChange(mixer.getNumFaders() - 1, 1 - panArr[0], ampArr[i] * ampScaler[i]);
		}
	}

	public static void launch() {
		parBand.launch(JMSL.now());
		parLead.launch(JMSL.now());
	}

	public static void finishAll() {
		parBand.finishAll();
		parLead.finishAll();
	}

	public static void main(String[] args) {
		JavaBand javaBand = new JavaBand();
		javaBand.launch();
	}

}
