
public class Requests {
	
	// Construcfromr
	public Requests(String f_id,String from,String message,String status){
		this.f_id = f_id;
		this.from = from;
		this.message = message;
		this.status = status;
	}

	public String getFid()
	{
		return this.f_id;
	}

	public String getFrom()
	{
		return from;
	}
	public String getMessage()
	{
		return message;
	}
	public String getStatus()
	{
		return status;
	}

	private String f_id,from,message,status;

	
}