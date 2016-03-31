package stan.voice.recognition;

import java.util.List;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class Micro
{
    public interface IMicroListener
    {
        void getAudioLevel(int al);
        void toRecognize(byte[] data);
    }

    private int TALK_RANG = 6;
    private List<byte[]> buffer;
    private TargetDataLine microphone;
    private IMicroListener microListener;

	public Micro(IMicroListener ml)
	{
		microListener = ml;
		AudioFormat format = new AudioFormat(3200, 16, 1, true, true);
	    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
	    // microphone = AudioSystem.getTargetDataLine(format);
	    try
	    {
		    microphone = (TargetDataLine)AudioSystem.getLine(info);
		    microphone.open(format);
	    }
	    catch(Exception e)
	    {
	    	
	    }
	}
    private int getAudioLevel(byte[] buf)
    {
        int lvl = 0;
        //TODO calculate audiolevel
        return lvl;
    }
    private int getRecordLevel()
    {
        return buffer.size();
    }
    private boolean checkReady()
    {
        return (getRecordLevel() / TALK_RANG) * TALK_RANG == getRecordLevel();
    }
    public void startRecording()
    {
        microphone.start();
        buffer = new ArrayList<>();
	    byte[] data = new byte[microphone.getBufferSize()/5];
	    int numBytesRead;
        int bytesRead = 0;
        while(bytesRead<100000)
        {
        	numBytesRead = microphone.read(data, 0, data.length);
            bytesRead += numBytesRead;
            // out.write(data, 0, numBytesRead);
        }
        stopRecording();
    }
    public void stopRecording()
    {
        microphone.close();
        buffer = null;
    }
}