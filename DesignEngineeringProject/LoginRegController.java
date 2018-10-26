import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.Socket;
import java.net.ConnectException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javafx.concurrent.Task;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.application.Platform;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import javafx.event.EventHandler;
import java.net.*;


public class LoginRegController {
	
	
	public LoginRegController(){
		try
		{
			
			client = new Socket("localhost",10220);
			dis = new DataInputStream(client.getInputStream());
			dos = new DataOutputStream(client.getOutputStream());
			uniqueid = "";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

	public void loginuser(ActionEvent ae) throws Exception
	{

			loginprocess.reset();
			loginprocess.start();
			loginprocess.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
				public void handle(WorkerStateEvent wse)
				{
					if(loginstatus.equals("$LS"))
					{
						loginstatus = "";
						UILoader.getObject().openMainUI(client,uniqueid);
					}
					
						clearLoginData();


				}
			});
		
	}

	public void registeruser(ActionEvent ae) throws Exception
	{

			registerprocess.reset();
			registerprocess.start();
			registerprocess.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
				public void handle(WorkerStateEvent wse)
				{
					if(registerstatus.equals("$RC"))
					{
						registerstatus = "";
						UILoader.getObject().openMainUI(client,uniqueid);

					}

					clearRegisterData();

				}
			});
	

	}

	Service<Void> clearstatus = new Service<Void>()
	{
		public Task<Void> createTask()
		{
			return new  Task<Void>()
			{
				protected Void call()
				{
					try
					{
						Thread.sleep(3000);
					}catch(Exception e)
					{
						e.printStackTrace();
					}
					Platform.runLater(new Runnable()
						{
							public void run()
							{
								status.setText("");
							}
						});
					return null;
				}
			};
		}
	};

	Service<Void> loginprocess = new Service<Void>(){
		public Task<Void> createTask()
		{
			return new Task<Void>()
			{
				protected Void call() throws Exception
				{
					String id = fid.getText();
					uniqueid = id;
					String password = pass.getText();
					String newpass = "";
					for(int i=0;i<password.length();i++)
					{
						newpass = newpass+((char)password.charAt(i)+10);
					}
					
					dos.writeUTF("$login");
					dos.writeUTF(id);
					dos.writeUTF(newpass);
					String result = dis.readUTF();
					if(result.equals("$retry"))
					{
						Platform.runLater(new Runnable()
							{
								public void run()
								{
									status.setText("Login failed");
									loginstatus = "$LF";
									uniqueid = "";
									clearStatus();
								}
							});
						
					}
					else
					{
						Platform.runLater(new Runnable()
							{
								public void run()
								{
									status.setText("Login success");
									loginstatus = "$LS";
									clearStatus();
								}
							});
					}
					
					return null;
				}
			};
		}
	};

	Service<Void> registerprocess = new Service<Void>(){
		public Task<Void> createTask()
		{
			return new Task<Void>()
			{
				protected Void call() throws Exception
				{
					String fn  = fname.getText();
					String mn = mname.getText();
					String ln = lname.getText();
					String dpt = (String)dept.getSelectionModel().getSelectedItem();
					String h =(String) hod.getSelectionModel().getSelectedItem();
					String cp = createpass.getText();
					String newpass = "";
					for(int i=0;i<cp.length();i++)
					{
						newpass = newpass+((char)cp.charAt(i)+10);
					}
					if(dpt.equals("choose department"))
					{
						Platform.runLater(new Runnable()
							{
								public void run()
								{
									status.setText("please choose department");
									clearStatus();
								}
								
							});
						
						return null;
					}
					if(h.equals("Are You H.O.D.?"))
					{
						Platform.runLater(new Runnable()
							{
								public void run()
								{
									status.setText("please choose your position");
									clearStatus();
								}
							});

						return null;
					}
					dos.writeUTF("$register");
					dos.writeUTF(fn);
					dos.writeUTF(mn);
					dos.writeUTF(ln);
					dos.writeUTF(dpt);
					dos.writeUTF(h);
					dos.writeUTF(newpass);
					String result = dis.readUTF();

					if(result.equals("$success"))
					{
						uniqueid = dis.readUTF();
						Platform.runLater(new Runnable()
							{
								public void run()
								{
									status.setText("Registration completed");
									registerstatus = "$RC";
									clearStatus();
								}
							});
						
					}
					else
					{
						Platform.runLater(new Runnable()
							{
								public void run()
								{
									status.setText("Registration failed");
									registerstatus = "$RF";
									clearStatus();
									clearregisterdata.start();
								}
							});
						
					}
					return null;
				}
			};
		}
	};

	Service<Void> processreminder = new Service<Void>()
	{
		public Task<Void> createTask()
		{
			return new Task<Void>(){
				protected Void call()
				{
					return null;
				}
			};
		}
	};

	Service<Void> clearlogindata = new Service<Void>()
	{
		public Task<Void> createTask()
		{
			return new Task<Void>(){
				protected Void call()
				{
					Platform.runLater(new Runnable()
						{
							public void run()
							{
								fid.clear();
								pass.clear();
							}
						});
					return null;
				}
			};
		}
	};

	Service<Void> clearregisterdata = new Service<Void>()
	{
		public Task<Void> createTask()
		{
			return new Task<Void>(){
				protected Void call()
				{
					Platform.runLater(new Runnable()
						{
							public void run()
							{
								fname.clear();
								lname.clear();
								mname.clear();
								dept.getSelectionModel().clearSelection();
								hod.getSelectionModel().clearSelection();
							}
						});
					return null;
				}
			};
		}
	};




	public void clearStatus()
	{
		if(!clearstatus.isRunning())
		{
			clearstatus.reset();
			clearstatus.start();
		}
	}

	public void clearLoginData()
	{
		if(!clearlogindata.isRunning())
		{
			clearlogindata.reset();
			clearlogindata.start();
		}
	}

	public void clearRegisterData()
	{
		if(!clearregisterdata.isRunning())
		{
			clearregisterdata.reset();
			clearregisterdata.start();
		}
	}

	@FXML private TextField fname;
	@FXML private TextField lname;
	@FXML private TextField mname;
	@FXML private PasswordField  pass;
	@FXML private TextField fid;
	@FXML private ComboBox<String> dept;
	@FXML private ComboBox<String> hod;
	@FXML private Label status;
	@FXML private PasswordField createpass;
	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String loginstatus,registerstatus;
	private String uniqueid;

	
}