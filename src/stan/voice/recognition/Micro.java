package stan.voice.recognition;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Micro
{
    public interface IMicroListener
    {
        void getAudioLevel(int audiolevel);
        void toRecognize(byte[] data);
    }

    private int TALK_RANG = 10;
    private int TALK_VOLUME = 2000;
    private List<byte[]> buffer;
    private TargetDataLine microphone;
    private Thread runnable;
    private IMicroListener microListener;
    private boolean isInit;
    private boolean isStarted;

    public Micro(IMicroListener ml)
    {
        microListener = ml;
    }
    public void setTalkRangVolume(int r, int v)
    {
        TALK_RANG = r;
        TALK_VOLUME = v;
    }
    public void initMicrophone()
        throws LineUnavailableException
    {
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        microphone = (TargetDataLine)AudioSystem.getLine(info);
        microphone.open(format);
        isInit = true;
    }
    public boolean isInit()
    {
        return isInit;
    }
    public boolean isStarted()
    {
        return isStarted;
    }
    public AudioFormat getAudioFormat()
    {
        float sampleRate = 8000.0F;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 1;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
    private int getAudioLevel(byte[] data)
    {
        int lvl = 0;
        for(int index = 0; index < data.length; index++)
        {
            short sample = (short)((data[index] << 8) | data[index]);
            lvl += (int)Math.abs(sample / 2768f);
        }
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
    private byte[] prepareVoice()
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for(int i = 0; i < buffer.size(); i++)
        {
            try
            {
                outputStream.write(buffer.get(i));
            }
            catch(Exception e)
            {
            }
        }
        return outputStream.toByteArray();
    }
    public void startRecording()
    {
        if(isStarted)
        {
            return;
        }
        microphone.start();
        buffer = new ArrayList<>();
        runnable = new Thread(new Runnable()
        {
            public void run()
            {
                while(true)
                {
                    readMicrophoneData();
                }
            }
        });
        runnable.start();
        isStarted = true;
    }
    public void readMicrophoneData()
    {
        byte[] data = new byte[microphone.getBufferSize() / 5];
        microphone.read(data, 0, data.length);
        int audiolevel = getAudioLevel(data);
        microListener.getAudioLevel(audiolevel);
        if(audiolevel > TALK_VOLUME)
        {
            buffer.add(data);
            if(checkReady())
            {
                microListener.toRecognize(prepareVoice());
            }
        }
        else
        {
            if(getRecordLevel() > 0)
            {
                microListener.toRecognize(prepareVoice());
                buffer.clear();
            }
        }
    }
    public void stopRecording()
    {
        runnable.stop();
        microphone.stop();
        microphone.flush();
        microphone.close();
        microphone = null;
        buffer = null;
        isInit = false;
        isStarted = false;
    }
}