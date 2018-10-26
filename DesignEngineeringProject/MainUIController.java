
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.concurrent.Task;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Timer;
import java.util.TimerTask;





public class MainUIController implements Initializable
{
		MainUIController(Socket client,String uid) throws Exception
		{
			this.client  = client;
			this.uid = uid;
			dis = new DataInputStream(this.client.getInputStream());
			dos = new DataOutputStream(this.client.getOutputStream());
			data = FXCollections.observableArrayList();
					

		}

		public void initialize(URL url,ResourceBundle rb)
		{
			try
			{
				dos.writeUTF("$getUserData$");
				dos.writeUTF(this.uid);
				id.setText(dis.readUTF());
				fname.setText(dis.readUTF());
				mname.setText(dis.readUTF());
				lname.setText(dis.readUTF());
				department.setText(dis.readUTF());
				if(!initializehods.isRunning())
				{
					initializehods.reset();
					initializehods.start();
				}
				Platform.runLater(new Runnable()
					{
						public void run()
						{
							new Timer().schedule(new TimerTask()
							{
								public void run()
								{
									System.out.println("Start");
									loadIfhod.reset();
									loadIfhod.start();
								}
							},5000,5000);
						}
					});
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		public void refreshS(ActionEvent ae) throws Exception
		{
			if(!loadrequests.isRunning())
			{
				loadrequests.reset();
				loadrequests.start();
			}
		}

		public void refreshR(ActionEvent ae) throws Exception
		{

			if(!loadIfhod.isRunning())
			{
					if(ishod)
					{
						loadIfhod.reset();
						loadIfhod.start();
					}
					
			}
		}

		public void fillAttendance(ActionEvent ae)
		{
			if(!attendanceprocess.isRunning())
			{
				attendanceprocess.reset();
				attendanceprocess.start();
				attendanceprocess.setOnSucceeded(new EventHandler<WorkerStateEvent>()
					{
						public void handle(WorkerStateEvent wse)
						{
							Platform.runLater(new Runnable()
								{
									public void run()
									{
										clearStatus();
									}
								});
						}
					});
			}
		}

		Service<Void> initializehods = new Service<Void>(){
			public Task<Void> createTask()
			{
				return new Task<Void>(){
					protected Void call() 
					{
						try
						{
							dos.writeUTF("$gethods$");
							String names = dis.readUTF();
							if(names.equals(""))
							{
								return null;
							}
							String fullnames[] = names.split("~");
							for(int i=0;i<fullnames.length;i++)
							{
								System.out.println(fullnames[i]);
								String parts[] = fullnames[i].split("@");
								if(fname.equals(parts[0])&&mname.equals(parts[1])&&lname.equals(parts[2]))
								{
									ishod = true;
								}

								String perfectname = parts[0]+" "+parts[1]+" "+parts[2];
								data.add(perfectname);
							}
							Platform.runLater(new Runnable(){
								public void run()
								{
									to.setItems(data);
								}
							});
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						return null;
					}
				};
			}
		};

		Service<Void> attendanceprocess = new Service<Void>(){
			public Task<Void> createTask()
			{
				return new Task<Void>(){
					protected Void call()
					{
						try
						{
							String er = erno.getText();
							
							String dep = (String)dept.getSelectionModel().getSelectedItem();
							
							String sm = new Integer(sem.getSelectionModel().getSelectedItem()).toString();
							
							String l1 =  ((RadioButton)lect1.getSelectedToggle()).getText();
							
							String l2 =  ((RadioButton)lect2.getSelectedToggle()).getText();
							
							String l3 =  ((RadioButton)lect3.getSelectedToggle()).getText();
							
							String l4 =  ((RadioButton)lect4.getSelectedToggle()).getText();
							
							String l5 =  ((RadioButton)lect5.getSelectedToggle()).getText();
							
							String l6 =  ((RadioButton)lect6.getSelectedToggle()).getText();
							
						
							dos.writeUTF("$fillatndnc$");
							dos.writeUTF(er);
							dos.writeUTF(sm);
							dos.writeUTF(dep);
							switch(l1)
							{
								case "Present":dos.writeUTF("1");break;
								default:dos.writeUTF("0");break;
							}
							switch(l2)
							{
								case "Present":dos.writeUTF("1");break;
								default:dos.writeUTF("0");break;
							}
							switch(l3)
							{
								case "Present":dos.writeUTF("1");break;
								default:dos.writeUTF("0");break;
							}
							switch(l4)
							{
								case "Present":dos.writeUTF("1");break;
								default:dos.writeUTF("0");break;
							}
							switch(l5)
							{
								case "Present":dos.writeUTF("1");break;
								default:dos.writeUTF("0");break;
							}
							switch(l6)
							{
								case "Present":dos.writeUTF("1");break;
								default:dos.writeUTF("0");break;
							}
							String response = dis.readUTF();
							System.out.println(response);
							if(response.equals("$afail$"))
							{
								Platform.runLater(new Runnable()
									{
										public void run()
										{
											status.setText("Failed in filling attendance");
										}
									});
								

							}
							else
							{
								Platform.runLater(new Runnable()
									{
										public void run()
										{
											status.setText("Attendance filled");
										}
									});
								
							}
							
						}
						catch(Exception e)
						{
							System.out.println(e.getMessage());
							e.printStackTrace();
						}

						return null;
					}
				};
			}
		};

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

	public void clearStatus()
	{
		if(!clearstatus.isRunning())
		{
			clearstatus.reset();
			clearstatus.start();
		}
	}

	public void seeAttendance(ActionEvent ae) throws Exception
	{
		if(!seeattendanceprocess.isRunning())
		{
			seeattendanceprocess.reset();
			seeattendanceprocess.start();
			seeattendanceprocess.setOnSucceeded(new EventHandler<WorkerStateEvent>()
				{
					public void handle(WorkerStateEvent wse)
					{
						clearStatus();
					}
				});
		}
	}

	Service<Void> seeattendanceprocess = new Service<Void>(){
		public Task<Void> createTask()
		{
			return new Task<Void>(){
				protected Void call() throws Exception
				{
					dos.writeUTF("$seeATDS$");
					String enroll = en.getText();
					dos.writeUTF(enroll);
					String res = dis.readUTF();
					String splited[] = res.split("~");
					for(int i=0;i<splited.length;i++)
					{
						System.out.println(splited[i]);
					}
					Platform.runLater(new Runnable()
						{
							public void run()
							{
								name.setText(splited[0]);
								sems.setText(splited[1]);
								depart.setText(splited[2]);
								se.setText(splited[3]);
								aj.setText(splited[4]);
								toc.setText(splited[5]);
								cg.setText(splited[6]);
								wt.setText(splited[7]);
								de.setText(splited[8]);
							}
						});
					
					return null;
				}
			};
		}
	};

	public void sendLeaveRequest()
	{
		if(!leaveRequestprocess.isRunning())
		{
			leaveRequestprocess.reset();
			leaveRequestprocess.start();
		}
	}


		Service<Void> leaveRequestprocess = new Service<Void>(){
			public Task<Void> createTask()
			{
				return new Task<Void>(){
					protected Void call() throws Exception
					{
						String fname =(String) to.getSelectionModel().getSelectedItem();
						if(fname.isEmpty())
						{
							return null;
						}
						String parts[] = fname.split(" ");
						dos.writeUTF("$getfidbyname$");
						dos.writeUTF(parts[0]);
						dos.writeUTF(parts[1]);
						dos.writeUTF(parts[2]);
						String id = dis.readUTF();
						System.out.println(id);
						String message = cause.getText();
						YourRequests yr  = new YourRequests(uid ,fname,message,"pending");
						Platform.runLater(new Runnable()
							{
								public void run()
								{
									ObservableList<YourRequests> data = srview.getItems();
									data.add(yr);
								}
							});
						dos.writeUTF("$sendleaverequest$");
						dos.writeUTF(uid);
						dos.writeUTF(fname);
						dos.writeUTF(message);
						dos.writeUTF(id);
						dos.writeUTF(fname+" "+mname+" "+lname);
						String response = dis.readUTF();

						if(response.equals("$succed$"))
						{
							Platform.runLater(new Runnable(){
								public void run()
								{
									status.setText("Leave sent successfully");
									clearStatus();
								}
							});
						}
						else
						{
							Platform.runLater(new Runnable(){
								public void run()
								{
									status.setText("Failed to send leave request");
									clearStatus();
								}
							});
						}


						return null;
					}
				};
			}
		};

		Service<Void> loadrequests = new Service<Void>(){
			public Task<Void> createTask()
			{
				return new Task<Void>(){
					protected Void call() throws Exception
					{
						ObservableList<YourRequests> rq = srview.getItems();
						rq.clear();
						dos.writeUTF("$loadreq$");
						dos.writeUTF(uid);
						String req = dis.readUTF();
						if(req.isEmpty())
						{
							return null;
						}
						String parts[] = req.split("~");
						
						for(int i=0;i<parts.length;i++)
						{
							System.out.println(parts[i]);
							String items[] = parts[i].split("@");
							YourRequests yr  = new YourRequests(items[0] ,items[1],items[2],items[3]);
							rq.add(yr);
						}
						Platform.runLater(new Runnable()
							{
								public void run()
								{
									srview.setItems(rq);
								}
							});
						

						return null;
					}
				};
			}
		};
		Service<Void> loadIfhod = new Service<Void>(){
			public Task<Void> createTask()
			{
				return new Task<Void>(){
					protected Void call() throws Exception
					{
						System.out.println("Load if hod started");
						ObservableList<Requests> rr = rrview.getItems();
						rr.clear();
						dos.writeUTF("$loadforhod$");
						dos.writeUTF(uid);
						String req = dis.readUTF();
						if(req.isEmpty())
						{
							return null;
						}
						String parts[] = req.split("~");
						
						for(int i=0;i<parts.length;i++)
						{
							
							String items[] = parts[i].split("@");
							String fullname[] = items[1].split(" ");
							System.out.println(items[0]);
							Requests yr  = new Requests(items[0] ,(fullname[0]+" "+fullname[1]+" "+fullname[2]),items[2],items[3]);
							rr.add(yr);
							System.out.println(rr.size());
						}
						Platform.runLater(new Runnable()
							{
								public void run()
								{
									rrview.setItems(rr);
								}
							});
						
						return null;
					}
				};
			}
		};



		public void seeResult(ActionEvent ae) throws Exception
		{
			if(!seeresultprocess.isRunning())
			{
				seeresultprocess.reset();
				seeresultprocess.start();
			}
		}

		Service<Void> seeresultprocess = new Service<Void>(){
			public Task<Void> createTask()
			{
				return new Task<Void>(){
					protected Void call() throws Exception
					{
						String enroll = marksno.getText();
						dos.writeUTF("$getresult$");
						dos.writeUTF(enroll);
						String result = dis.readUTF();
						System.out.println(result);
						if(result.isEmpty())
						{
							Platform.runLater(new Runnable()
							{
								public void run()
								{
									status.setText("No Result Available");
									clearStatus();
								}
							});
						}
						Platform.runLater(new Runnable()
							{
								public void run()
								{
									marksno.setText("");
								}
							});
						String splited[] = result.split("@");
						Platform.runLater(new Runnable()
						{
							public void run()
							{
								e.setText(splited[0]);
								SE.setText(splited[1]);
								AJ.setText(splited[2]);
								TOC.setText(splited[3]);
								CG.setText(splited[4]);
								WT.setText(splited[5]);
								DE.setText(splited[6]);
							}
						});
						Platform.runLater(new Runnable()
							{
								public void run()
								{
									status.setText("Result displayed");
									clearStatus();
								}
							});
						return null;
					}
				};
			}
		};

		public void submitMarks(ActionEvent ae) throws Exception
		{
			if(!entryprocess.isRunning())
			{
				entryprocess.reset();
				entryprocess.start();
			}
		}

		Service<Void> entryprocess = new Service<Void>(){
			public Task<Void> createTask()
			{
				return new Task<Void>(){
					protected Void call() throws Exception
					{
						String erno = entermarksno.getText();
						String se = marksse.getText();
						String aj = marksaj.getText();
						String toc = markstoc.getText();
						String cg = markscg.getText();
						String wt = markswt.getText();
						String de = marksde.getText();
						dos.writeUTF("$entermarks$");
						dos.writeUTF(erno);
						dos.writeUTF(se);
						dos.writeUTF(aj);
						dos.writeUTF(toc);
						dos.writeUTF(cg);
						dos.writeUTF(wt);
						dos.writeUTF(de);
						String res = dis.readUTF();
						if(res.equals("$entrys"))
						{
							Platform.runLater(new Runnable()
								{
									public void run()
									{
										status.setText("Marks Entered");
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
										status.setText("Marks entry error");
										clearStatus();
									}
								});
						}
						Platform.runLater(new Runnable(){
							public void run()
							{
								entermarksno.setText("");
								marksse.setText("");
								marksaj.setText("");
								markstoc.setText("");
								markscg.setText("");
								markswt.setText("");
								marksde.setText("");
							}
						});
						return null;
					}
				};
			}
		};

		private Socket client;
		private DataInputStream dis;
		private DataOutputStream dos;
		private String uid;
		private String atstatus;
		private boolean ishod;
		@FXML private ToggleGroup lect1,lect2,lect3,lect4,lect5,lect6;
		@FXML private ComboBox<String> dept;
		@FXML private ComboBox<Integer> sem;
		@FXML private TextField erno;
		@FXML private Label id,fname,mname,lname,department,status;
		@FXML private Label name,sems,depart,se,wt,aj,toc,cg,de;
		@FXML private TextField en,marksno;
		/* This references for leave requests*/
		@FXML private ComboBox<String> to;
		@FXML private TextField cause;
		@FXML private TableView<YourRequests> srview;
		@FXML private TableView<Requests> rrview;
		@FXML private Label e,s,d,SE,DE,AJ,TOC,WT,CG;
		@FXML private TextField entermarksno,marksse,markswt,marksde,markstoc,marksaj,markscg;
		ObservableList<String> data ;

}