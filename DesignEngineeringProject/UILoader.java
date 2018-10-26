import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.net.Socket;

public class UILoader extends Application
{
	public static void main(String args[])
	{
		launch(args);
	}

	public void start(Stage stage)
	{
		try
		{
			BorderPane root = FXMLLoader.load(getClass().getResource("/loginui.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			loginstage = stage;
			object = this;
			scene.getStylesheets().add("/Styles.css");
			stage.show();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void openMainUI(Socket client,String uid) 
	{
		try{
		loginstage.close();
		FXMLLoader loader  =  new FXMLLoader(getClass().getResource("/MainUI.fxml"));
		MainUIController mainuicontroller = new MainUIController(client,uid);
		loader.setController(mainuicontroller);
		BorderPane root = loader.load();
		Scene scene = new Scene(root);
					scene.getStylesheets().add("/Styles.css");

		mainstage  = new Stage();
		mainstage.setResizable(false);
		mainstage.setMaximized(true);
		mainstage.setScene(scene);

		mainstage.show();
		
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
		
	}

	public static UILoader getObject()
	{
		return object;
	}

	private Stage loginstage,mainstage;
	private static UILoader object;


}
