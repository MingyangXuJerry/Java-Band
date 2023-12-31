package algomusicfall2021.jerry.finalproject;

/**************
** WARNING - this code automatically generated by Syntona.
** The real source is probably a Syntona patch.
** Do NOT edit this file unless you copy it to another directory and change the name.
** Otherwise it is likely to get clobbered the next time you
** export Java source code from Syntona.
**
** Syntona is available from: http://www.softsynth.com/syntona/
*/

import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.RangeConverter;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.PassThrough;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.LineOut;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.Circuit;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.swing.SoundTweaker;
import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.Multiply;
import com.softsynth.jmsl.JMSLPlugin;
import com.softsynth.shared.time.TimeStamp;


// Env that modulates the amplitude of ModOsc
// ModOsc with an enveloped freq
// fundamental SineOsc
// frequency mod
// Sub
public class FMEnvSawLaser extends Circuit implements UnitVoice, JMSLPlugin {
	// Declare units and ports.
	PassThrough mFrequencyPassThrough;
	public UnitInputPort frequency;
	PassThrough mAmplitudePassThrough;
	public UnitInputPort amplitude;
	PassThrough mOutputPassThrough;
	public UnitOutputPort output;
	SineOscillator mModOsc;
	Add mAplus;
	PassThrough mModFreqPassThrough;
	public UnitInputPort modFreq;
	PassThrough mModAmpPassThrough;
	public UnitInputPort ModAmp;
	VariableRateMonoReader mMonoRdr;
	SegmentedEnvelope mSegEnv;
	SegmentedEnvelope mModEnv;
	VariableRateMonoReader mMonoRdr2;
	Multiply mAtimes;
	SegmentedEnvelope mModEnv2;
	VariableRateMonoReader mMonoRdr22;
	Multiply mAtimes2;
	SawtoothOscillator mSawOsc;
	SineOscillator mSineOsc;
	Multiply mAtimes3;
	Multiply mAtimes4;
	FilterLowPass mLowPass;
	Multiply mAtimes5;
	Multiply mAtimes6;
	PassThrough mLPF_CutPassThrough;
	public UnitInputPort LPF_Cut;
	RangeConverter mARanger;

	// Declare inner classes for any child circuits.

	public FMEnvSawLaser() {
		// Create unit generators.
		add(mFrequencyPassThrough = new PassThrough());
		addPort(frequency = mFrequencyPassThrough.input, "frequency");
		add(mAmplitudePassThrough = new PassThrough());
		addPort(amplitude = mAmplitudePassThrough.input, "amplitude");
		add(mOutputPassThrough = new PassThrough());
		addPort(output = mOutputPassThrough.output, "output");
		add(mModOsc = new SineOscillator());
		add(mAplus = new Add());
		add(mModFreqPassThrough = new PassThrough());
		addPort(modFreq = mModFreqPassThrough.input, "modFreq");
		add(mModAmpPassThrough = new PassThrough());
		addPort(ModAmp = mModAmpPassThrough.input, "ModAmp");
		add(mMonoRdr = new VariableRateMonoReader());
		double[] mSegEnvData = { 0.10474835763456174, 1.0, 0.7541881749688445, 0.7393162393162394, 1.152231933980179,
				0.3162393162393162, 1.442409865256813, 0.2264957264957265, 4.68582521589661, 0.0, };
		mSegEnv = new SegmentedEnvelope(mSegEnvData);
		double[] mModEnvData = { 0.0, 0.0, 0.4266612978385324, 0.3504273504273504, 0.054441400575849064, 1.0,
				11.28003367052731, 0.6495726495726496, 10.679789618318827, 0.5982905982905983, 13.021517620031197,
				0.004273504273504274, };
		mModEnv = new SegmentedEnvelope(mModEnvData);
		add(mMonoRdr2 = new VariableRateMonoReader());
		add(mAtimes = new Multiply());
		double[] mModEnv2Data = { 0.0, 1.0, 1.1286870669031854, 0.5170940170940171, 0.6967969202435642,
				0.2094017094017094, 3.492714101028315, 0.0641025641025641, 4.155413588434678, 0.0, };
		mModEnv2 = new SegmentedEnvelope(mModEnv2Data);
		add(mMonoRdr22 = new VariableRateMonoReader());
		add(mAtimes2 = new Multiply());
		add(mSawOsc = new SawtoothOscillator());
		add(mSineOsc = new SineOscillator());
		add(mAtimes3 = new Multiply());
		add(mAtimes4 = new Multiply());
		add(mLowPass = new FilterLowPass());
		add(mAtimes5 = new Multiply());
		add(mAtimes6 = new Multiply());
		add(mLPF_CutPassThrough = new PassThrough());
		addPort(LPF_Cut = mLPF_CutPassThrough.input, "LPF_Cut");
		add(mARanger = new RangeConverter());
		// Connect units and ports.
		mFrequencyPassThrough.output.connect(mAtimes3.inputA);
		mFrequencyPassThrough.output.connect(mAplus.inputA);
		mAmplitudePassThrough.output.connect(mMonoRdr.amplitude);
		mModOsc.output.connect(mAplus.inputB);
		mAplus.output.connect(mSawOsc.frequency);
		mModFreqPassThrough.output.connect(mAtimes2.inputA);
		mModAmpPassThrough.output.connect(mAtimes.inputB);
		mMonoRdr.output.connect(mAtimes4.inputA);
		mMonoRdr.output.connect(mSawOsc.amplitude);
		mMonoRdr2.output.connect(mAtimes.inputA);
		mAtimes.output.connect(mModOsc.amplitude);
		mMonoRdr22.output.connect(mAtimes2.inputB);
		mMonoRdr22.output.connect(mAtimes5.inputA);
		mAtimes2.output.connect(mModOsc.frequency);
		mSawOsc.output.connect(mLowPass.input);
		mSineOsc.output.connect(mARanger.input);
		mAtimes3.output.connect(mSineOsc.frequency);
		mAtimes4.output.connect(mSineOsc.amplitude);
		mLowPass.output.connect(mARanger.input);
		mAtimes5.output.connect(mLowPass.frequency);
		mLPF_CutPassThrough.output.connect(mAtimes5.inputB);
		mARanger.output.connect(mOutputPassThrough.input);
		// Setup
		frequency.setup(0.0, 165.547512319236, 331.095024638472);
		amplitude.setup(0.0, 0.1175719, 1.0);
		modFreq.setup(0.0, 867.5516, 1735.1032);
		ModAmp.setup(0.0, 1798.8352, 3597.6704);
		mMonoRdr.rate.set(1.0);
		mMonoRdr2.amplitude.set(0.679);
		mMonoRdr2.rate.set(1.0);
		mMonoRdr22.amplitude.set(0.679);
		mMonoRdr22.rate.set(1.0);
		mAtimes3.inputB.set(0.25);
		mAtimes4.inputB.set(0.5);
		mLowPass.amplitude.set(1.0);
		mLowPass.Q.set(6.0);
		mAtimes6.inputA.set(0.0);
		mAtimes6.inputB.set(0.0);
		LPF_Cut.setup(120.0, 10021.130624, 12000.0);
		mARanger.min.set(-0.9);
		mARanger.max.set(0.9);
	}

	public void noteOn(double frequency, double amplitude, TimeStamp timeStamp) {
		this.frequency.set(frequency, timeStamp);
		this.amplitude.set(amplitude, timeStamp);
		mMonoRdr.dataQueue.queueOn(mSegEnv, timeStamp);
		mMonoRdr2.dataQueue.queueOn(mModEnv, timeStamp);
		mMonoRdr22.dataQueue.queueOn(mModEnv2, timeStamp);
	}

	public void noteOff(TimeStamp timeStamp) {
		mMonoRdr.dataQueue.queueOff(mSegEnv, false, timeStamp);
		mMonoRdr2.dataQueue.queueOff(mModEnv, false, timeStamp);
		mMonoRdr22.dataQueue.queueOff(mModEnv2, false, timeStamp);
	}

	public UnitOutputPort getOutput() {
		return output;
	}

	public static void main(String[] args) {
		final Synthesizer synth = JSyn.createSynthesizer(); // final: locks the variable
		synth.start();

		FMEnvSawLaser osc = new FMEnvSawLaser();
		synth.add(osc);

		LineOut out = new LineOut();
		synth.add(out);

		osc.getOutput().connect(out.input);

		out.start();

		JFrame jFrame = new JFrame("Watch Out For Some Lasers!");
		jFrame.add(new JLabel("Close to stop...circuit version"));
		SoundTweaker soundTweaker = new SoundTweaker(synth, "Press keyboard to play", osc);
		jFrame.add(soundTweaker);
		jFrame.pack();
		jFrame.setVisible(true);
		jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { // overrides window closing action
				System.out.println("byebye");
				synth.stop();
				System.exit(0);
			}
		});
	}
}
