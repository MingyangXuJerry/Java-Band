package algomusicfall2021.jerry.finalproject;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.swing.SoundTweaker;
import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.MixerStereo;
import com.jsyn.unitgen.Multiply;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.softsynth.jmsl.JMSLPlugin;
import com.softsynth.shared.time.TimeStamp;

public class SineFM extends Circuit implements UnitVoice, JMSLPlugin {
	SineOscillator modOsc;
	SineOscillator carOsc;
	Add adder;
	public UnitInputPort modfrequency;
	public UnitInputPort modamplitude;
	public UnitInputPort carfrequency;
	public UnitInputPort caramplitude;
	public double carfreqmultiplier, modfreqmultiplier;
	public Multiply mult, mult2;
	public double NOTE_A3 = 220.0, NOTE_CS4 = 277.183, NOTE_E4 = 329.628, NOTE_GS4 = 415.305, NOTE_A4 = 440.0,
			NOTE_A6 = 1760.0;
	double[] ampData = { .08, 0.9, // duration, value frame 0, "attack"
			0.10, 0.45, // frame 1, "decay"
			0.10, 0.95, // frame 2
			1.50, 0.0 // frame 3, "release"
	};
	SegmentedEnvelope ampEnvelope;
	VariableRateMonoReader ampEnvelopeReader;
	// constructor
	public SineFM() {
		this(1.0);
	}

	public SineFM(double freqMultiplier) {
		// add SynthUnits to circuits
		carfreqmultiplier = freqMultiplier;
		add(modOsc = new SineOscillator());
		add(carOsc = new SineOscillator());
		add(adder = new Add());
		add(mult = new Multiply());
		add(mult2 = new Multiply());
		add(ampEnvelopeReader = new VariableRateMonoReader());
		buildSegmentedEnvelope();
		
		// start connecting
		modOsc.output.connect(adder.inputB);
		adder.output.connect(carOsc.frequency);

		// add ports visible to other classes
		addPort(modfrequency = mult2.inputA, "Mod Frequency");
		addPort(modamplitude = modOsc.amplitude, "Mod Amplitude");
		addPort(carfrequency = mult.inputA, "Carrier Frequency");
		addPort(caramplitude = carOsc.amplitude, "Carrier Amplitude");
		ampEnvelopeReader.output.connect(caramplitude);

		mult.inputB.set(freqMultiplier);
		mult.output.connect(adder.inputA);
		mult2.inputB.set(freqMultiplier);
		mult2.output.connect(modOsc.frequency);

		modfrequency.setup(0.0, NOTE_A3, 1000.0);
		modamplitude.setup(0.0, 0.0, 1000.0);
		carfrequency.setup(0.0, NOTE_A3 * freqMultiplier, NOTE_A6);
		caramplitude.setup(0.0, 0.25, 1.0);

	}
	
	public void buildSegmentedEnvelope() {
		ampEnvelope = new SegmentedEnvelope(ampData);
		ampEnvelope.setSustainBegin(1);
		ampEnvelope.setSustainEnd(3);
	}
	
	public UnitOutputPort getOutput() {
		return carOsc.output;
	}

	@Override
	public void noteOff(TimeStamp ts) {
		ampEnvelopeReader.dataQueue.queueOff(ampEnvelope, true, ts);
	}

	@Override
	public void noteOn(double freq, double amp, TimeStamp ts) {
		carfrequency.set(freq * carfreqmultiplier, ts);
		ampEnvelopeReader.amplitude.set(amp, ts);
		ampEnvelopeReader.dataQueue.queueOn(ampEnvelope, ts);

	}

	public static void main(String[] args) {

		final Synthesizer synth = JSyn.createSynthesizer();

		synth.start();

// CHANGE THE FOLLOWING LINE OF CODE TO YOURS

		// SineFM voice = new SineFM();
		SineFM voice1 = new SineFM(1.2599);
		MixerStereo mix = new MixerStereo(2);
		voice1.getOutput().connect(mix.input);
		synth.add(mix);
		synth.add(voice1);

		LineOut out = new LineOut();

		synth.add(out);

		out.start();

		mix.getOutput().connect(0, out.input, 0);

		mix.getOutput().connect(0, out.input, 1);

		JFrame jf = new JFrame();

		SoundTweaker tweaker = new SoundTweaker(synth, "Test this sound", voice1);

		jf.add(tweaker);

		jf.pack();

		jf.setVisible(true);

		jf.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {

				synth.stop();

				System.exit(0);

			}

		});

	}
}