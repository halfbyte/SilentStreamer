package de.jankrutisch.soundtools;

import netscape.javascript.JSObject;

import java.nio.ByteBuffer;
import java.util.TimerTask;

import javax.sound.sampled.SourceDataLine;

class SoundRunner extends TimerTask {
	final static int BUFFER_LEN = 2048;
    private long samplePosition;
		private SourceDataLine myLine;
        private JSObject window;

		// int carryOver = 0;
		public SoundRunner(SourceDataLine line, JSObject window) {
			myLine = line;
            this.window = window;
		}
		
		public void run() {
            Integer bufferLen[] = new Integer[] {(BUFFER_LEN / 2)};
			String retVal;
            if (!myLine.isOpen()) return;
			int available = myLine.available();
			if(available < BUFFER_LEN) return;
            try {
                retVal = (String)window.call("generateSound", bufferLen);
                if (retVal == null) {
                    System.out.println("got null");
                    return;                    
                }
                System.out.println(retVal);
                // System.out.println(retVal);
                int sizeToWrite = BUFFER_LEN; // ((Double)retVal.getMember("length")).intValue();
                ByteBuffer bb = ByteBuffer.allocate(sizeToWrite);
                for(int i=0;i<(sizeToWrite / 2);i++) {
                    int low = retVal.charAt((i*2));
                    int high = retVal.charAt((i*2)+1);
                    short word = (short)(low | (high << 8));
        		    bb.putShort(word);
                }
			    int written = myLine.write(bb.array(), 0, BUFFER_LEN);
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
}