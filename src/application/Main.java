package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
			Scene scene = new Scene(root,1263,611);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.resizableProperty().set(true);
			MainController.share(primaryStage,scene);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override 
			    public void handle(WindowEvent t) {
			        System.exit(0);
			    }
			});
			primaryStage.show();
			
		} catch(Exception e) {
			DialogueBox.error(e);
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
