
public class YourRequests {
	
	// Constructor
	public YourRequests(String fid,String to,String message,String status){
		this.fid = fid;
		this.to =to;
		this.message = message;
		this.status = status;
	}


	public String getFid()
	{
		return this.fid;
	}

	public String getTo()
	{
		return to;
	}
	public String getMessage()
	{
		return message;
	}
	public String getStatus()
	{
		return status;
	}

	private String fid,to,message,status;
	
}