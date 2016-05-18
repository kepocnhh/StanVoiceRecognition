package stan.voice.recognition;

import java.util.List;

public class GoogleResponse<J>
{
    public J responseObject;
    public String responseString;
	
	public GoogleResponse(J rO, String rS)
	{
		this.responseObject = rO;
		this.responseString = rS;
	}
}