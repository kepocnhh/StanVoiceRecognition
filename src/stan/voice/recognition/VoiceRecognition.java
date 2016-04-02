package stan.voice.recognition;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class VoiceRecognition
{
    public interface IRecognitionListener
    {
        void getResponse(String response);
    }

    private static final String MAIN_URL =
        "http://www.google.com/speech-api/v2/recognize?client=chromium&output=json";
    private static final String CONTENT_TYPE_FLAC = "audio/x-flac";
    private static final String CONTENT_TYPE_WAV = "audio/l16";

    private String apiKey;
    private IRecognitionListener recognitionListener;

	public VoiceRecognition(IRecognitionListener rl, String ak)
	{
		recognitionListener = rl;
        apiKey = ak;
	}

    public void recognize(final byte[] data)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                postRecognize(data);
            }
        }).start();
    }

    private void postRecognize(byte[] data)
    {
        StringBuilder completeResponse = createRequest(data);
        recognitionListener.getResponse(createRequest(data).toString());
    }
    private StringBuilder createRequest(byte[] data)
    {
        StringBuilder completeResponse = null;
        URLConnection urlConn = null;
        try
        {
            urlConn = createURLConnectionFromUrl(createUrl());
            otputDataFromUrlConnection(urlConn, data);
            completeResponse = inputDataFromUrlConnection(urlConn);
        }
        catch(IOException e)
        {

        }
        return completeResponse;
    }
    private String createUrl()
    {
        StringBuilder sb = new StringBuilder(MAIN_URL);
        sb.append("&lang=");
        sb.append("ru-RU");
        if(apiKey != null)
        {
            sb.append("&key=");
            sb.append(apiKey);
        }
        return sb.toString();
    }
    private URLConnection createURLConnectionFromUrl(String url)
        throws IOException
    {
        URLConnection urlConn = new URL(url).openConnection();
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", CONTENT_TYPE_WAV + "; rate=" + 8000);
        return urlConn;
    }
    private void otputDataFromUrlConnection(URLConnection urlConn, byte[] data)
        throws IOException
    {
        OutputStream outputStream = urlConn.getOutputStream();
        outputStream.write(data, 0, data.length);
        outputStream.close();
    }
    private StringBuilder inputDataFromUrlConnection(URLConnection urlConn)
        throws IOException
    {
        BufferedReader br = new BufferedReader(
            new InputStreamReader(urlConn.getInputStream(), Charset.forName("UTF-8")));
        StringBuilder completeResponse = new StringBuilder();
        String response = br.readLine();
        while(response != null)
        {
            completeResponse.append(response);
            response = br.readLine();
        }
        br.close();
        return completeResponse;
    }
}