package algomusicfall2021.jerry.finalproject;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.swing.SoundTweaker;
import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.InterpolatingDelay;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.LinearRamp;
import com.jsyn.unitgen.MixerStereo;
import com.jsyn.unitgen.Multiply;
import com.jsyn.unitgen.PassThrough;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.softsynth.jmsl.JMSLPlugin;
import com.softsynth.shared.time.TimeStamp;

public class SineDreamChord extends Circuit implements UnitVoice, JMSLPlugin {

	//////////////// voices ////////////////
	public SineFM[] oscs = new SineFM[4];
	//////////////// intervals ////////////////
	public double[] freqmultipliers = new double[] { 1.0, 1.2599, 1.4983, 1.8877 };
	//////////////// notes delay time ////////////////
	public double delaytime = 0.1;
	//////////////// stereo delay ////////////////
	public double stereodelay = 0.5;

	PassThrough mFrequencyPassThrough;
	public UnitInputPort frequency;
	PassThrough mAmplitudePassThrough;
	public UnitInputPort amplitude;
	public LinearRamp rampLin;
	public UnitInputPort modfrequency;
	public UnitInputPort modamplitude;
	public UnitInputPort notedelayport = new UnitInputPort("Note Delay Interval");
	public UnitInputPort ampenvattack = new UnitInputPort("Amp Env Attack");
	public UnitInputPort ampenvdecay = new UnitInputPort("Amp Env Decay");
	public UnitInputPort ampenvrelease = new UnitInputPort("Amp Env Release");
	public UnitInputPort stereodelayport = new UnitInputPort("Stereo Delay Time");
	public ReverseFilter reversefilter;
	public UnitInputPort reversefilterport;
	public MixerStereo mixerL, mixerR;
	public InterpolatingDelay delay;
	public PassThrough modFrqPass, modAmpPass;
	public TimeStamp dt;
	SineOscillator sineMod;
	Add adder;
	public Multiply mult;
	public double NOTE_A3 = 220.0, NOTE_CS4 = 277.183, NOTE_E4 = 329.628, NOTE_GS4 = 415.305, NOTE_A4 = 440.0,
			NOTE_A6 = 1760.0;

	//////////////// amp envelop ////////////////

	double[] ampData = { .08, 0.9, // duration, value frame 0, "attack"
			0.10, 0.45, // frame 1, "decay"
			0.10, 0.95, // frame 2
			1.50, 0.0 // frame 3, "release"
	};
	SegmentedEnvelope ampEnvelope;
	VariableRateMonoReader ampEnvelopeReader;

	public SineDreamChord() {
		// add to circuit
		add(sineMod = new SineOscillator());
		add(mult = new Multiply());
		add(adder = new Add());
		add(modFrqPass = new PassThrough());
		add(modAmpPass = new PassThrough());
		add(mFrequencyPassThrough = new PassThrough());
		add(mAmplitudePassThrough = new PassThrough());
		add(mixerL = new MixerStereo(4));
		add(mixerR = new MixerStereo(2));
		add(ampEnvelopeReader = new VariableRateMonoReader());
		add(delay = new InterpolatingDelay());
		add(rampLin = new LinearRamp());
		buildSegmentedEnvelope();
		buildStereoMixer();
		
		mAmplitudePassThrough.output.connect(mixerL.amplitude);
		mAmplitudePassThrough.output.connect(mixerR.amplitude);
		for (int i = 0; i < oscs.length; i++) {
			add(oscs[i] = new SineFM(freqmultipliers[i]));
			modFrqPass.output.connect(oscs[i].modfrequency);
			modAmpPass.output.connect(oscs[i].modamplitude);
			mFrequencyPassThrough.output.connect(rampLin.input);
			rampLin.output.connect(oscs[i].carfrequency);
			oscs[i].getOutput().connect(mixerL.input);
			ampEnvelopeReader.output.connect(oscs[i].caramplitude);
		}

		// add ports
		addPort(frequency = mFrequencyPassThrough.input, "frequency");
		addPort(amplitude = mAmplitudePassThrough.input, "amplitude");
		addPort(modfrequency = modFrqPass.input, "Mod Frequency");
		addPort(modamplitude = modAmpPass.input, "Mod Amplitude");
		addPort(notedelayport, "Note Delay Interval");
		buildReverseFilter();
		addPort(ampenvattack, "Amp Env Attack");
		addPort(ampenvdecay, "Amp Env Decay");
		addPort(ampenvrelease, "Amp Env Release");
		addPort(stereodelayport = delay.delay, "Stereo Delay Time");

		// set up default
		modfrequency.setup(0.0, NOTE_A3, 1000.0);
		modamplitude.setup(0.0, 100.0, 1000.0);
		notedelayport.setup(0.0, delaytime, 0.5);
		frequency.setup(0.0, NOTE_A3, NOTE_A6);
		amplitude.setup(0.0, 0.5, 1.0);
		ampenvattack.setup(0.01, 0.08, 1.0);
		ampenvdecay.setup(0.01, 0.10, 2.0);
		ampenvrelease.setup(0.01, 1.5, 2.0);
		dt = new TimeStamp(delaytime);
		stereodelayport.setup(0.0, stereodelay, 1.0);
        rampLin.time.set(0.5);
	}

	public void buildReverseFilter() {
		add(reversefilter = new ReverseFilter(stereodelay/2));
		addPort(reversefilterport = reversefilter.reverseInputPort, "Reverse Filter Shimmers");
		reversefilterport.setup(0.0, 1.0, 1.5);
		mixerR.output.connect(reversefilter.input);
		reversefilter.output.connect(mixerL.input);
	}
	
	public void buildStereoMixer() {
		delay.delay.set(stereodelay);
		mixerL.output.connect(delay.input);
		delay.output.connect(mixerR.input);
		delay.allocate(441000);
	}
	public void buildSegmentedEnvelope() {
		ampEnvelope = new SegmentedEnvelope(ampData);
		ampEnvelope.setSustainBegin(1);
		ampEnvelope.setSustainEnd(3);
	}

	public void updateSegmentedEnvelope() {
		ampData[0] = ampenvattack.getValue();
		ampData[2] = ampenvdecay.getValue();
		ampData[6] = ampenvrelease.getValue();
		buildSegmentedEnvelope();
		for (int i = 0; i < oscs.length; i++) {
			oscs[i].ampEnvelope = ampEnvelope;
		}
	}
	@Override
	public UnitOutputPort getOutput() {
		return getOutputL();
	}
	public UnitOutputPort getOutputL() {
		return mixerL.output;
	}
	
	public UnitOutputPort getOutputR() {
		return mixerR.output;
	}

	@Override
	public void noteOff(TimeStamp ts) {
		for (int i = 0; i < oscs.length; i++) {
			oscs[i].noteOff(ts);
			ts = ts.makeRelative(delaytime);
		}

	}

	@Override
	public void noteOn(double frequency, double amplitude, TimeStamp ts) {	
		updateSegmentedEnvelope();
		delaytime = notedelayport.getValue();
		this.frequency.set(frequency);
		this.amplitude.set(amplitude/2);
		for (int i = 0; i < oscs.length; i++) {
			oscs[i].noteOn(frequency, amplitude, ts);
			ts = ts.makeRelative(delaytime);
		}
	}

	public static void main(String[] args) {

		final Synthesizer synth = JSyn.createSynthesizer();

		synth.start();

// CHANGE THE FOLLOWING LINE OF CODE TO YOURS

		SineDreamChord voice = new SineDreamChord();

		synth.add(voice);

		LineOut out = new LineOut();

		synth.add(out);

		out.start();

		voice.getOutputL().connect(0, out.input, 0);

		voice.getOutputR().connect(0, out.input, 1);

		JFrame jf = new JFrame();

		SoundTweaker tweaker = new SoundTweaker(synth, "Test this sound", voice);

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
