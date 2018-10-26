public class User{
	User(String id,String fname,String mname,String lname,String dept,String hod)
	{
		this.id = id;
		this.fname = fname;
		this.mname = mname;
		this.lname = lname;
		this.dept = dept;
		this.hod = hod;
	}

	public String getId()
	{
		return this.id;
	}
	public String getFname()
	{
		return this.fname;
	}

	public String getMname()
	{
		return this.mname;
	}
	public String getLname()
	{
		return this.lname;
	}
	public String getDept()
	{
		return this.dept;
	}
	public boolean isHod()
	{
		if(this.hod.equals("Yes"))
		{
			return true;
		}
		return false;
	}

	private String id;
	private String fname,mname,lname;
	private String dept;
	private String hod;
}