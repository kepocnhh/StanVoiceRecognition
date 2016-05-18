package stan.voice.recognition;

import javax.sound.sampled.LineUnavailableException;

public class Voice
	implements Micro.IMicroListener, VoiceRecognition.IRecognitionListener
{
    public interface IResponseListener
    {
        void getSpeech(GoogleResponse deserialized);
        void audioLevel(int al);
    }

	private Micro micro;
	private VoiceRecognition voice;
	private IResponseListener responseListener;

	public Voice(IResponseListener rl, String ak)
	{
		responseListener = rl;
		micro = new Micro(this);
		voice = new VoiceRecognition(this, ak);
	}

	@Override
    public void getAudioLevel(int al)
    {
    	responseListener.audioLevel(al);
    }
	@Override
    public void toRecognize(byte[] data)
    {
    	voice.recognize(data);
    }

	@Override
    public void getResponse(String response)
    {
    	GoogleResponse deserialized = deSerialize(response);
    	//TODO deserialized GoogleResponse object
        responseListener.getSpeech(deserialized);
    }
	
    public GoogleResponse deSerialize(String response)
    {
		return null;
    }

    public void startRecognize()
        throws LineUnavailableException
    {
    	if(!micro.isInit())
    	{
        	micro.initMicrophone();
    	}
        micro.startRecording();
    }
    public void stopRecognize()
    {
		if(micro != null && micro.isInit())
		{
			micro.stopRecording();
		}
    }
}
