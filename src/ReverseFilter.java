package algomusicfall2021.jerry.finalproject;

/*
 * Created on Sep 22, 2012 by Nick
 *
 */

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.swing.SoundTweaker;
import com.jsyn.unitgen.*;

/**
 * Custom unit generator that can be used with other JSyn units.
 * 
 * Define your own UnitInputPort in addition to UnitFilter’s inherited input, do
 * some arbitrary math to the input signals, and create your own signal
 * processor!
 * 
 * @author Phil Burk (C) 2010 Mobileer Inc modded by ND for lecture notes
 *         9/22/2012.
 * 
 */
public class ReverseFilter extends UnitFilter {

	public UnitInputPort reverseInputPort;
    private int numSamples = 44100;
    private float[] buffer = new float[numSamples];
    private int cursor;
    private int pointer;
    
    public ReverseFilter() {
    	this(1.0);
    }
    public ReverseFilter(double d) {
    	addPort(reverseInputPort = new UnitInputPort("reverseInputPort"));
		reverseInputPort.setup(0, 0.4, 1.0);
		numSamples = (int) (44100.0 * d);
	}
    
    @Override
    public void generate(int start, int limit) {
        double[] inputs = input.getValues();
        double[] outputs = output.getValues();


        for (int i = start; i < limit; i++) {
            outputs[i] = buffer[cursor];
            buffer[cursor] = (float) inputs[i];
            double reverseMultiplier = reverseInputPort.getValue();
            cursor += 1;
            if (cursor >= numSamples) {
                cursor = 0;
            }
            if (pointer <= 1) {
                pointer = 8 - 1;
            }
            pointer --;

            outputs[i]= outputs[pointer]*reverseMultiplier;
        }
    }

	public static void main(String[] args) {

		final Synthesizer synth = JSyn.createSynthesizer();
		synth.start();

		SineOscillator voice = new SineOscillator();
		synth.add(voice);

		ReverseFilter mathFilter = new ReverseFilter();
		synth.add(mathFilter);

		LineOut out = new LineOut();
		synth.add(out);
		out.start();

		voice.getOutput().connect(0, mathFilter.input, 0);

		voice.getOutput().connect(0, out.input, 0);
		mathFilter.getOutput().connect(0, out.input, 1);

		JFrame jf = new JFrame();
		jf.setLayout(new FlowLayout());
		SoundTweaker tweaker = new SoundTweaker(synth, "Test this sound", voice);
		jf.add(tweaker);
		tweaker = new SoundTweaker(synth, "Test this filter", mathFilter);
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