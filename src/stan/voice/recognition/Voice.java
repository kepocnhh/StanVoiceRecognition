package stan.voice.recognition;

import stan.voice.recognition.google.response.GoogleResponse;

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

	public Voice(IResponseListener rl)
	{
		responseListener = rl;
		micro = new Micro(this);
		voice = new VoiceRecognition(this);
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
    	GoogleResponse deserialized = null;
    	//TODO deserialized GoogleResponse object
        responseListener.getSpeech(deserialized);
    }

    public void startRecognize()
    {
        micro.startRecording();
    }
    public void stopRecognize()
    {
        micro.stopRecording();
    }
}
