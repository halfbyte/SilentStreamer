package de.jankrutisch.soundtools;

import java.applet.Applet;
import java.awt.*;
import java.util.Timer;
import java.io.IOException;

import netscape.javascript.*;

import javax.sound.sampled.*;

/**
 * Created by IntelliJ IDEA.
 * User: jankrutisch
 * Date: 20.01.2010
 * Time: 12:54:14
 * To change this template use File | Settings | File Templates.
 */
public class SoundStreamer extends Applet {


    private JSObject window;
    private String greeting = "Aha2";
    private SourceDataLine sdl;
    private static AudioFormat audioformat = new AudioFormat(44100, 16, 1, true, true);
    private Timer timer;
    public SoundStreamer() throws HeadlessException {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);    //To change body of overridden methods use File | Settings | File Templates.
        g.drawString(greeting, 10,20);
    }

    @Override
    public void init() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        timer = new Timer();
    }

    @Override
    public void start() {
        super.start();    //To change body of overridden methods use File | Settings | File Templates.
        try {
            window = JSObject.getWindow(this);
        } catch(JSException e) {
            e.printStackTrace();

        }
        try {

        System.out.println(Short.MIN_VALUE);

		Mixer.Info mixerinfo[] = AudioSystem.getMixerInfo();
		for(int i = 0; i<mixerinfo.length; i++) {
			System.out.printf("Mixer #%d: %s\n", i, mixerinfo[0].toString());
		}

        SourceDataLine.Info info = new DataLine.Info(
	            SourceDataLine.class,
	            audioformat,
	            512);
	    sdl = (SourceDataLine) AudioSystem.getLine(info);
	    sdl.open(audioformat);

        sdl.start();
        System.out.printf("Starting Output, is open? %s\n", sdl.isOpen());
        if (window == null) return;

        timer.schedule(new SoundRunner(sdl, window), 0, 20);
	    } catch (LineUnavailableException e) {
	    	System.err.println("Line was unavailable");
	    }
    }

    @Override
    public void stop() {
        super.stop();    //To change body of overridden methods use File | Settings | File Templates.
        timer.cancel();
        sdl.drain();
        sdl.stop();
    }

    public void well(String test) {
        String dings[] = new String[] {"test"};
        JSObject returnVal;
        greeting = "Click'd";

        repaint();
        if (window != null) {
            returnVal = (JSObject)window.call("otherDirection", dings);
            System.out.println(returnVal.getSlot(0).getClass().getName());
        } else {
            System.out.println("we have a null window. dammit.");
        }
    }

}
