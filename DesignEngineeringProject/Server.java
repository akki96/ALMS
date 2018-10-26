import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.SocketException;
import java.net.*;

class MainServer
{
	private ServerSocket server;
	private LinkedHashMap<String,ConnectedUsers> connectedusers = null;
	private UsersDatabase usersdatabase = null;
	MainServer()
	{
		try
		{
			server = new ServerSocket(10220);

			connectedusers = new LinkedHashMap<String,ConnectedUsers>();
			usersdatabase = new UsersDatabase();
			

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void acceptClients()
	{
		while(true)
		{

			try
			{

				Socket client = server.accept();
				System.out.println(client.getInetAddress()+"Connected");
				new Thread(new ClientHandle(client,connectedusers,usersdatabase)).start();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}

class ClientHandle extends Thread
{
	ClientHandle(Socket client,LinkedHashMap<String,ConnectedUsers> connectedusers,UsersDatabase usersdatabase) 
	{
		try
		{
			this.client = client;
			this.connectedusers = connectedusers;
			this.usersdatabase = usersdatabase;
			
			dis = new DataInputStream(client.getInputStream());
			dos = new DataOutputStream(client.getOutputStream());
				
				handleClient = new Thread(new Runnable()
				{
					public void run()
					{
					ConnectedUsers c = null;
					  while(true)
						{	try{
								
								String command = dis.readUTF();
								System.out.println(command);
								if(command.equals("$register"))
								{
									
									synchronized(this)
									{
										String fname = dis.readUTF();
										String mname = dis.readUTF();
										String lname = dis.readUTF();
										String department  = dis.readUTF();
										String hod = dis.readUTF();
										String pass = dis.readUTF();
										String result = usersdatabase.insertData(fname,mname,lname,department,hod,pass);
		
										if(result!=null)
										{
											dos.writeUTF("$success");
											dos.writeUTF(result);
											c = new ConnectedUsers(fname,mname,lname,department,result,dis,dos,client);
											connectedusers.put(result,c);
										}
										else
										{
											dos.writeUTF("$notsuccess");
										}									
									}

									
								}
									if(command.equals("$login"))
									{
										
										synchronized(this)
										{
											String id = dis.readUTF();
											String pass = dis.readUTF();
											if(usersdatabase.validateUser(id,pass))
											{
												dos.writeUTF("$login");
												User user = usersdatabase.retriveUserData(id);
												if(user!=null)
												{
													c = new ConnectedUsers(user.getFname(),user.getMname(),user.getLname(),user.getDept(),id,dis,dos,client);
													connectedusers.put(id,c);
												}
											}
											else
											{
												dos.writeUTF("$retry");
											}
										}

									}

									if(command.equals("$fillatndnc$"))
									{
										synchronized(this)
										{
											String erno = dis.readUTF();
											int sem = new Integer(dis.readUTF().toString());
											String dept = dis.readUTF();
											int l1 = new Integer(dis.readUTF().toString());
											int l2 = new Integer(dis.readUTF().toString());
											int l3 = new Integer(dis.readUTF().toString());
											int l4 = new Integer(dis.readUTF().toString());
											int l5 = new Integer(dis.readUTF().toString());
											int l6 = new Integer(dis.readUTF().toString());
											
											int response = usersdatabase.updateData(erno,sem,dept,l1,l2,l3,l4,l5,l6);
											
											if(response==0)
											{
												dos.writeUTF("$afail$");
											}
											else
											{
												dos.writeUTF("$asuccs$");
											}
										}

									}

									if(command.equals("$getUserData$"))
									{
										String fid = dis.readUTF();
										ConnectedUsers cu = connectedusers.get(fid);
										dos.writeUTF(cu.getId());
										dos.writeUTF(cu.getFName());
										dos.writeUTF(cu.getMName());
										dos.writeUTF(cu.getLName());
										dos.writeUTF(cu.getDept());

									}

									if(command.equals("$seeATDS$"))
									{
										String erno = dis.readUTF();
										dos.writeUTF(usersdatabase.getAttendance(erno));
									}

									if(command.equals("$gethods$"))
									{
										dos.writeUTF(usersdatabase.getHodNames());
									}

									if(command.equals("$getfidbyname$"))
									{
										String fname = dis.readUTF();
										String mname = dis.readUTF();
										String lname = dis.readUTF();
										System.out.println(fname+mname+lname);
										String id = usersdatabase.getId(fname,mname,lname);
										dos.writeUTF(id);
									}

									if(command.equals("$sendleaverequest$"))
									{
										String fromid = dis.readUTF();
										String to = dis.readUTF();
										String message = dis.readUTF();
										String toid = dis.readUTF();
										String fromname = dis.readUTF();
										boolean response = usersdatabase.insertLeave(fromid,to,message,toid,fromname);
										if(response)
										{
											dos.writeUTF("$succed$");
										}
										else
										{
											dos.writeUTF("$failedleave$");
										}
									}

									if(command.equals("$loadreq$"))
									{
										String id = dis.readUTF();
										dos.writeUTF(usersdatabase.getSelfMadeRequests(id));
									}



									if(command.equals("$loadforhod$"))
									{
										String id = dis.readUTF();
										dos.writeUTF(usersdatabase.getReceivedRequests(id));
									}

									if(command.equals("$getresult$"))
									{
										String er = dis.readUTF();
										String result = usersdatabase.getResult(er);
										dos.writeUTF(result);
									}

									if(command.equals("$entermarks$"))
									{
										String erno = dis.readUTF();
											int se = new Integer(dis.readUTF().toString());
											int aj = new Integer(dis.readUTF().toString());
											int toc = new Integer(dis.readUTF().toString());
											int cg = new Integer(dis.readUTF().toString());
											int wt = new Integer(dis.readUTF().toString());
											int de = new Integer(dis.readUTF().toString());
										int res = usersdatabase.enterMarks(erno,se,aj,toc,cg,wt,de);
										if(res==0)
										{
											dos.writeUTF("$unsuccess$");
										}
										else
										{
											dos.writeUTF("$entrys$");
										}
									}



								
									handleClient.sleep(100);}
									catch(Exception e)
									{
										break;
									}
								}
							}
						});
				
				handleClient.start();
				
			}catch(SocketException se)
			{
				if(se.getMessage().equals("Connection reset"))
				{
					System.out.println("Client offline");
				}
			}
			catch(Exception e)
			{
				System.out.println("Here");
				e.printStackTrace();
			}
	}
	Thread handleClient,handleFunctions;
	private LinkedHashMap<String,ConnectedUsers> connectedusers;
	private UsersDatabase usersdatabase;
	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;


}




public class Server {
	
	public static void main(String[] args){
		try
		{
			MainServer main = new MainServer();
			main.acceptClients();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	
	}	
}