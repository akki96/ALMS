import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Statement;


public class AttendanceDB {
	
	// Constructor
	public AttendanceDB() throws Exception{
		connection = DriverManager.getConnection("jdbc:sqlite:attendance.db");
		statement = connection.createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS ATTENDANCEDATA(ERNO TEXT PRIMARY KEY,SEM NUMBER,DEPT TEXT,LECT1 NUMBER,LECT2 NUMBER,LECT3 NUMBER, LECT4 NUMBER ,LECT5 NUMBER,LECT6 NUMBER)");
		insert  = connection.prepareStatement("INSERT INTO ATTENDANCEDATA(ERNO,SEM,DEPT,LECT1,LECT2,LECT3,LECT4,LECT5,LECT6)VALUES(?,?,?,?,?,?,?,?,?)");
		update = connection.prepareStatement("UPDATE ATTENDANCEDATA SET LECT1 = ?,LECT2=?, LECT3 = ?,LECT4 = ?,LECT5 = ?,LECT6 = ? WHERE ERNO=?");
	}

	synchronized public int updateData(String erno,int sem,String dept,int l1,int l2,int l3,int l4,int l5,int l6)
	{
		System.out.println("Called");
		try
		{
			System.out.println("Called");
			rs = statement.executeQuery("select LECT1,LECT2,LECT3,LECT4,LECT5,LECT6 from ATTENDANCEDATA where ERNO = "+"\""+erno+"\"");
			if(!rs.next())
			{
				insert.setString(1,erno);
				insert.setInt(2,sem);
				insert.setString(3,dept);
				insert.setInt(4,0);
				insert.setInt(5,0);
				insert.setInt(6,0);
				insert.setInt(7,0);
				insert.setInt(8,0);
				insert.setInt(9,0);
				insert.executeUpdate();

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
	

	private Connection connection;
	private Statement statement;
	private PreparedStatement insert,update;
	private ResultSet rs; 
	
}