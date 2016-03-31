package stan.voice.recognition;

public class VoiceRecognition
{
    public interface IRecognitionListener
    {
        void getResponse(String response);
    }

    private IRecognitionListener recognitionListener;

	public VoiceRecognition(IRecognitionListener rl)
	{
		recognitionListener = rl;
	}

    public void recognize(byte[] data)
    {
    }

    private void postrecognize(byte[] data)
    {
    }
}