import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Statement;


class UsersDatabase
{
	UsersDatabase() throws Exception
	{
		connection = DriverManager.getConnection("jdbc:sqlite:alms.db");
		statement = connection.createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS FACULTY(ID TEXT PRIMARY KEY,FNAME TEXT,MNAME TEXT,LNAME TEXT,DEPARTMENT TEXT,HOD TEXT,PASS TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS ATTENDANCEDATA(ERNO TEXT PRIMARY KEY,SEM NUMBER,DEPT TEXT,LECT1 NUMBER,LECT2 NUMBER,LECT3 NUMBER, LECT4 NUMBER ,LECT5 NUMBER,LECT6 NUMBER)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS SENTREQUESTS(FID TEXT,TOUSER TEXT, MESSAGE TEXT,STATUS TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS RECEIVEDREQUESTS(FID TEXT,FROMUSER TEXT,MESSAGE TEXT,STATUS TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS STUDENTMARKS(ERNO TEXT PRIMARY KEY,SUB1 NUMBER,SUB2 NUMBER,SUB3 NUMBER,SUB4 NUMBER,SUB5 NUMBER,SUB6 NUMBER)");
		insert  = connection.prepareStatement("INSERT INTO FACULTY(ID,FNAME,MNAME,LNAME,DEPARTMENT,HOD,PASS)VALUES(?,?,?,?,?,?,?)");
		insertattendance  = connection.prepareStatement("INSERT INTO ATTENDANCEDATA(ERNO,SEM,DEPT,LECT1,LECT2,LECT3,LECT4,LECT5,LECT6)VALUES(?,?,?,?,?,?,?,?,?)");
		update = connection.prepareStatement("UPDATE ATTENDANCEDATA SET LECT1 = ?,LECT2=?, LECT3 = ?,LECT4 = ?,LECT5 = ?,LECT6 = ? WHERE ERNO=?");
		insertleavetosame = connection.prepareStatement("INSERT INTO SENTREQUESTS(FID,TOUSER,MESSAGE,STATUS)VALUES(?,?,?,?)");
		insertto = connection.prepareStatement("INSERT INTO RECEIVEDREQUESTS(FID,FROMUSER,MESSAGE,STATUS)VALUES(?,?,?,?)");
		insertresult =  connection.prepareStatement("INSERT INTO STUDENTMARKS(ERNO,SUB1,SUB2,SUB3,SUB4,SUB5,SUB6)VALUES(?,?,?,?,?,?,?)");
	}

	synchronized  public String insertData(String fname,String mname,String lname,String dept,String hod,String pass)
	{
		int count = 0;
		try
		{
			
			int result = 0;
			rs = statement.executeQuery("Select * from FACULTY");
			while(rs.next())
			{
				count++;
			}
	    	insert.setString(1,"F"+(count+100));
	    	insert.setString(2,fname);
	    	insert.setString(3,mname);
	    	insert.setString(4,lname);
	    	insert.setString(5,dept);
	    	insert.setString(6,hod);
	    	insert.setString(7,pass);
	    	result =  insert.executeUpdate();
	    	if(result==0)
	    	{
	    		return null;
	    	}

	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    	return null;
	    }
	    return "F"+(count+100);
	}

	synchronized public boolean validateUser(String id,String pass) throws Exception
	{
		rs = statement.executeQuery("SELECT * FROM FACULTY WHERE ID ="+"\""+id +"\""+"AND PASS = "+"\""+pass+"\"");
		return rs.next();
	}

	synchronized public User retriveUserData(String id) throws Exception
	{
		try
		{
			rs = statement.executeQuery("SELECT * FROM FACULTY WHERE ID = "+"\""+id+"\"");
			if(rs==null)
			{
				return null;
			}
			User user = new User(rs.getString("ID"),rs.getString("FNAME"),rs.getString("MNAME"),rs.getString("LNAME"),rs.getString("DEPARTMENT"),rs.getString("HOD"));
			return user;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}

	public void showData() throws Exception
	{
		rs = statement.executeQuery("SELECT * FROM FACULTY");
		while(rs.next())
		{
			System.out.println(rs.getString("ID"));
			System.out.println(rs.getString("FNAME"));
			System.out.println(rs.getString("MNAME"));
			System.out.println(rs.getString("LNAME"));
			System.out.println(rs.getString("DEPARTMENT"));
			System.out.println(rs.getString("HOD"));
			System.out.println(rs.getString("PASS"));
		}
	}
	

	synchronized public int updateData(String erno,int sem,String dept,int l1,int l2,int l3,int l4,int l5,int l6)
	{
		
		try
		{
			
			rs = statement.executeQuery("select LECT1,LECT2,LECT3,LECT4,LECT5,LECT6 from ATTENDANCEDATA where ERNO = "+"\""+erno+"\"");
			if(!rs.next())
			{
				insertattendance.setString(1,erno);
				insertattendance.setInt(2,sem);
				insertattendance.setString(3,dept);
				insertattendance.setInt(4,0);
				insertattendance.setInt(5,0);
				insertattendance.setInt(6,0);
				insertattendance.setInt(7,0);
				insertattendance.setInt(8,0);
				insertattendance.setInt(9,0);
				insertattendance.executeUpdate();

			}
			rs = statement.executeQuery("select LECT1,LECT2,LECT3,LECT4,LECT5,LECT6 from ATTENDANCEDATA where ERNO = "+"\""+erno+"\"");
			if(rs.next())
			{
				update.setInt(1,rs.getInt(1)+l1);
				update.setInt(2,rs.getInt(2)+l2);
				update.setInt(3,rs.getInt(3)+l3);
				update.setInt(4,rs.getInt(4)+l4);
				update.setInt(5,rs.getInt(5)+l5);
				update.setInt(6,rs.getInt(6)+l6);
				update.setString(7,erno);
				return update.executeUpdate();
			}
			return 0;

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	synchronized public String getAttendance(String erno)
	{
		String result = "";
		try
		{
			rs = statement.executeQuery("select * from ATTENDANCEDATA where ERNO = "+"\""+erno+"\"");
			if(rs.next())
				result = rs.getString("ERNO")+"~"+rs.getInt("SEM")+"~"+rs.getString("DEPT")+"~"+rs.getInt("LECT1")+"~"+rs.getInt("LECT2")+"~"+rs.getInt("LECT3")+"~"+rs.getInt("LECT4")+"~"+rs.getInt("LECT5")+"~"+rs.getInt("LECT6");
			return result;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return result;
		}
	}

	synchronized public String getHodNames() throws Exception 
	{
		String hods = "";
		rs = statement.executeQuery("SELECT FNAME,MNAME,LNAME FROM FACULTY WHERE HOD = "+"\""+"Yes"+"\"");
		while(rs.next())
		{
			hods = hods+rs.getString("FNAME")+"@"+rs.getString("MNAME")+"@"+rs.getString("LNAME")+"~";
		}
		System.out.println(hods);
		return hods;
	}

	synchronized public String getId(String fname,String mname,String lname) throws Exception
	{
		System.out.println("Called");
		rs = statement.executeQuery("SELECT ID FROM FACULTY WHERE FNAME = "+"\""+fname+"\""+"And MNAME = "+"\""+mname+"\""+"AND LNAME = "+"\""+lname+"\"");
		if(rs.next())
		{
			return rs.getString("ID");
		}
		return null;
	}	

	synchronized public boolean insertLeave(String id,String to,String message,String toid,String fromname) throws Exception
	{
		insertleavetosame.setString(1,id);
		insertleavetosame.setString(2,to);
		insertleavetosame.setString(3,message);
		insertleavetosame.setString(4,"pending");
		insertto.setString(1,toid);
		insertto.setString(2,fromname);
		insertto.setString(3,message);
		insertto.setString(4,"pending");
		int i =  insertleavetosame.executeUpdate();
		int j = insertto.executeUpdate();
		if((i==0 || j ==0)||(i==0 && j==0))
		{
			return false;
		}
		return true;

		
	}
	synchronized public String getSelfMadeRequests(String id) throws Exception
	{
		String requests = "";
		rs = statement.executeQuery("SELECT * FROM SENTREQUESTS WHERE FID = "+"\""+id+"\"");
		while(rs.next())
		{
			requests = requests+rs.getString("FID")+"@"+rs.getString("TOUSER")+"@"+rs.getString("MESSAGE")+"@"+rs.getString("STATUS")+"~";

		}
		return requests;

	}

	synchronized public String getReceivedRequests(String id) throws Exception
	{
		String requests = "";
		rs = statement.executeQuery("SELECT * FROM RECEIVEDREQUESTS WHERE FID = "+"\""+id+"\"");
		while(rs.next())
		{
			requests = requests+rs.getString("FID")+"@"+rs.getString("FROMUSER")+"@"+rs.getString("MESSAGE")+"@"+rs.getString("STATUS")+"~";

		}
		return requests;
	}

	synchronized public String getResult(String erno) throws Exception
	{
		String result = "";
		System.out.println(erno);
		rs = statement.executeQuery("SELECT * FROM STUDENTMARKS WHERE ERNO = "+"\""+erno+"\"");
		if (rs.next())
		{
			result = rs.getString("ERNO")+"@"+rs.getInt("SUB1")+"@"+rs.getInt("SUB2")+"@"+rs.getInt("SUB3")+"@"+rs.getInt("SUB4")+"@"+rs.getInt("SUB5")+"@"+rs.getInt("SUB6");
		}
		return result;
	}

	synchronized public int enterMarks(String erno,int s1,int s2,int s3,int s4,int s5,int s6) throws Exception
	{
		try
		{
			System.out.println(erno+s1);
			System.out.println(s2);
			System.out.println(s3);
			System.out.println(s4);
			System.out.println(s5);
			System.out.println(s6);

			insertresult.setString(1,erno);
			insertresult.setInt(2,s1);
			insertresult.setInt(3,s2);
			insertresult.setInt(4,s3);
			insertresult.setInt(5,s4);
			insertresult.setInt(6,s5);
			insertresult.setInt(7,s6);
			int i = insertresult.executeUpdate();
			return i;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}


	}



	private Connection connection;
	private Statement statement;
	private PreparedStatement insert;
	private PreparedStatement insertattendance,update;
	private PreparedStatement insertleavetosame,insertto,insertresult;
	private ResultSet rs; 
	

}