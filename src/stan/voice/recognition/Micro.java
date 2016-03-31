package stan.voice.recognition;

public class Micro
{
    public interface IMicroListener
    {
        void getAudioLevel(int al);
        void toRecognize(byte[] data);
    }

    private IMicroListener microListener;

	public Micro(IMicroListener ml)
	{
		microListener = ml;
	}
    private int getAudioLevel(byte[] buf)
    {
        int lvl = 0;
        return lvl;
    }
    private int getRecordLevel()
    {
        return 0;
    }
    public void startRecording()
    {
    }
    public void stopRecording()
    {
    }
}