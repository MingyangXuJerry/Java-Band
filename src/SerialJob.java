package algomusicfall2021.jerry.finalproject;

import java.util.Random;

import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.MusicJob;

public class SerialJob extends MusicJob {
	SerialProvider sp;
	JavaBand jb;
	
	public SerialJob() {
		jb = new JavaBand();
		sp = new SerialProvider();
		setRepeats(Integer.MAX_VALUE);
		setRepeatPause(0.1);
	}
	@Override
	public double start(double playTime) throws InterruptedException {
		System.out.println("hi");
		return super.start(playTime);
	}
	
	@Override
	public double repeat(double playTime) throws InterruptedException {
		sp.dump();
		if (sp.getLK()) {
			jb.setPan(sp.getCurrentInstruemnt(), sp.getMappedLX());
			jb.setAmp(sp.getCurrentInstruemnt(), sp.getMappedLY());
		}
		// constantly pulling data from the SerialProvider
		// set the Mode of LeadJob based on RK (right click of joystick)
		if (sp.getRK()) {
			jb.getLeadJob().setMode(new Random().nextInt(4)+1);
		} else {
			jb.getLeadJob().setMode(0);
		}
		switch(sp.getCurrentInstruemnt()) {
		case 0: 
			jb.getLeadJob().setDelayTime(sp.getMappedRY());
			jb.getLeadJob().getLeadShape().setModFreq(1000*sp.getMappedRX());
			jb.getLeadJob().getLeadShape().setModAmp(1600*sp.getMappedRY());
			break;
		case 1:
			jb.getChordShape().setModFreq(440*sp.getMappedRX());
			jb.getChordShape().setModAmp(200*sp.getMappedRY());
			jb.getChordShape().setReverseFilter(sp.getMappedRY());
			break;
		case 2:
			jb.getBassShape().setModFreq(1000*sp.getMappedRX());
			jb.getBassShape().setModAmp(1600*sp.getMappedRY());
			jb.getBassShape().setLPF_Cut(8000*sp.getMappedRY());
		}
		return super.repeat(playTime);
	}
	
	@Override
	public Thread launch(double startTime) {
		jb.launch();
		sp.initialize();
		return super.launch(startTime);
	}
	
	public SerialProvider getSerialProvider(){
		return sp;
	}
	
	public JavaBand getJavaBand(){
		return jb;
	}
	
	public void finishAll() {
		jb.finishAll();
		sp.close();
		finishAll();
	}
	public static void main(String[] args) {
		JMSL.clock.setAdvance(0.5);
		SerialJob sj = new SerialJob();
		sj.launch(JMSL.now());
	}
}
