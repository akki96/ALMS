import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;



public class ConnectedUsers
{
	ConnectedUsers(String fname,String mname,String lname,String department,String id,DataInputStream dis,DataOutputStream dos,Socket client)
	{
		this.fname = fname;
		this.mname = mname;
		this.lname = lname;
		this.department = department;
		this.id = id;
		this.dis = dis;
		this.dos = dos;
		this.client = client;
	}

	public String getFName()
	{
		return this.fname;
	}
		public String getMName()
	{
		return this.mname;
	}
		public String getLName()
	{
		return this.lname;
	}
	public String getId()
	{
		return this.id;
	}
	public String getDept()
	{
		return this.department;
	}
	public DataInputStream getDis()
	{
		return this.dis;
	}
	public DataOutputStream getDos()
	{
		return this.dos;
	}
	public Socket getClient()
	{
		return this.client;
	}

	private String fname,mname,lname,department;
	private String id;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Socket client;

}