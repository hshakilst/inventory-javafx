package application;


import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.util.converter.NumberStringConverter;
public class ProductsController implements Initializable{
	@FXML
	private TableView<Products> tblProduct;
	@FXML
	private TableColumn<Products, Number> colPid;
	@FXML
	private TableColumn<Products, String> colComName;
	@FXML
	private TableColumn<Products, String> colPName;
	@FXML
	private TableColumn<Products, Number> colUPin;
	@FXML
	private TableColumn<Products, Number> colUPrint;
	@FXML
	private TableColumn<Products, Number> colUMedia;
	@FXML
	private TableColumn<Products, Number> colULiner;
	@FXML
	private TableColumn<Products, Number> colRate;
	@FXML
	private Button btRefresh;
	@FXML
	private Button pDelete;
	@FXML
	private Button pEdit;
	@FXML
	private Button pSearchBtn;
	@FXML
	private TextField pSearch;
	
	private ObservableList<Products> data;
	
	private static Stage primaryStage;
	private static Scene scene;
	private Products p;
	
	public static void share(Stage p, Scene s){
		primaryStage = p;
		scene = s;
	}
	
	public void loading(){
		final VBox bx = new VBox();
	    bx.setAlignment(Pos.CENTER);
	    ProgressIndicator pi = new ProgressIndicator();
	    Label label = new Label("Loading...");
	    bx.getChildren().addAll(pi,label);
	    Scene s1 = new Scene(bx,893,490);
	    primaryStage.setScene(s1);
	    primaryStage.show();
	}
	
	public void init(){
		colPid.setCellValueFactory(new PropertyValueFactory<>("pid"));
		colComName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
		colPName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		colUPin.setCellValueFactory(new PropertyValueFactory<>("uPin"));
		colUPrint.setCellValueFactory(new PropertyValueFactory<>("uPrint"));
		colUMedia.setCellValueFactory(new PropertyValueFactory<>("uMedia"));
		colULiner.setCellValueFactory(new PropertyValueFactory<>("uLiner"));
		colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
	}
	
	public void loadTable(ResultSet rs) throws SQLException{
		data = FXCollections.observableArrayList();
		try {
			while(rs.next()){
				data.add(new Products(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), 
						rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8)));
			}
		} catch (SQLException e) {
			DialogueBox.error(e);
		}
		tblProduct.setItems(data);
		rs.close();
	}
	
	public void onClickRefresh(){
		Task<Void> task = new Task<Void>(){
			@Override
            protected Void call() throws Exception {
				Platform.runLater(()->{
					try{
						init();
						Database db = new Database();
						ResultSet rs = db.query("Select * from products");
						loadTable(rs);
						db.close();
						rs.close();
					}catch(Exception e){
						DialogueBox.error(e);
					}
				});
				return null;
			}
		};
		Thread t = new Thread(task);
		t.start();
		while(t.isAlive()){
			loading();
		}
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void onClickDel(){
		if(tblProduct.getSelectionModel().getSelectedItem() != null){	
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							p = tblProduct.getSelectionModel().getSelectedItem();
							p.deleteProduct(p.getPid());
							onClickRefresh();
						}catch(Exception e){
							DialogueBox.error(e);
						}
					});
					return null;
				}
			};
			Thread t = new Thread(task);
			t.start();
			while(t.isAlive()){
				loading();
			}
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}
	
	public void onClickEdit(){
    	Task<Void> task = new Task<Void>(){
			@Override
            protected Void call() throws Exception {
				Platform.runLater(()->{
					try{
						colComName.setCellFactory(TextFieldTableCell.forTableColumn());
						colPName.setCellFactory(TextFieldTableCell.forTableColumn());
						colUPin.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
							@Override
				            public String toString(Number number) {
				                return String.valueOf(number.doubleValue());
				            }
							@Override
							public Double fromString(String string) {
				                try {
				                    double value = Double.parseDouble(string);
				                    return value;
				                } catch (NumberFormatException e) {
				                	Alert alert = new Alert(AlertType.WARNING);
				            		alert.setTitle("Warning!");
				            		alert.setHeaderText(null);
				            		alert.setContentText("Invalid input detected!");
				            		alert.showAndWait();
				            		return Double.parseDouble(toString());
				                }
				            }
						}));
						colUPrint.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
							@Override
				            public String toString(Number number) {
				                return String.valueOf(number.doubleValue());
				            }
							@Override
							public Double fromString(String string) {
				                try {
				                    double value = Double.parseDouble(string);
				                    return value;
				                } catch (NumberFormatException e) {
				                	Alert alert = new Alert(AlertType.WARNING);
				            		alert.setTitle("Warning!");
				            		alert.setHeaderText(null);
				            		alert.setContentText("Invalid input detected!");
				            		alert.showAndWait();
				            		return Double.parseDouble(toString());
				                }
				            }
						}));
						colUMedia.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
							@Override
				            public String toString(Number number) {
				                return String.valueOf(number.doubleValue());
				            }
							@Override
							public Double fromString(String string) {
				                try {
				                    double value = Double.parseDouble(string);
				                    return value;
				                } catch (NumberFormatException e) {
				                	Alert alert = new Alert(AlertType.WARNING);
				            		alert.setTitle("Warning!");
				            		alert.setHeaderText(null);
				            		alert.setContentText("Invalid input detected!");
				            		alert.showAndWait();
				            		return Double.parseDouble(toString());
				                }
				            }
						}));
						colULiner.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
							@Override
				            public String toString(Number number) {
				                return String.valueOf(number.doubleValue());
				            }
							@Override
							public Double fromString(String string) {
				                try {
				                    double value = Double.parseDouble(string);
				                    return value;
				                } catch (NumberFormatException e) {
				                	Alert alert = new Alert(AlertType.WARNING);
				            		alert.setTitle("Warning!");
				            		alert.setHeaderText(null);
				            		alert.setContentText("Invalid input detected!");
				            		alert.showAndWait();
				            		return Double.parseDouble(toString());
				                }
				            }
						}));
						colRate.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
							@Override
				            public String toString(Number number) {
				                return String.valueOf(number.doubleValue());
				            }
							@Override
							public Double fromString(String string) {
				                try {
				                    double value = Double.parseDouble(string);
				                    return value;
				                } catch (NumberFormatException e) {
				                	Alert alert = new Alert(AlertType.WARNING);
				            		alert.setTitle("Warning!");
				            		alert.setHeaderText(null);
				            		alert.setContentText("Invalid input detected!");
				            		alert.showAndWait();
				            		return Double.parseDouble(toString());
				                }
				            }
						}));
						
						colComName.setOnEditCommit(
					            new EventHandler<CellEditEvent<Products, String>>() {
					                @Override
					                public void handle(CellEditEvent<Products, String> tab) {
					                	if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Products) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editProducts("company_name",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getPid());
					                	}
					                }
					            }
					        );
						colPName.setOnEditCommit(
					            new EventHandler<CellEditEvent<Products, String>>() {
					                @Override
					                public void handle(CellEditEvent<Products, String> tab) {
					                	if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Products) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editProducts("item_name",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getPid());
					                	}
					                }
					            }
					        );
						colUPin.setOnEditCommit(
					            new EventHandler<CellEditEvent<Products, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Products, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Products) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editProducts("upin",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getPid());
					                	}
					                }
					            }
					        );
						colUPrint.setOnEditCommit(
					            new EventHandler<CellEditEvent<Products, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Products, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Products) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editProducts("uprint",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getPid());
					                	}
					                }
					            }
					        );
						colUMedia.setOnEditCommit(
					            new EventHandler<CellEditEvent<Products, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Products, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Products) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editProducts("umedia",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getPid());
					                	}
					                }
					            }
					        );
						colULiner.setOnEditCommit(
					            new EventHandler<CellEditEvent<Products, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Products, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Products) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editProducts("uliner",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getPid());
					                	}
					                }
					            }
					        );
						colRate.setOnEditCommit(
					            new EventHandler<CellEditEvent<Products, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Products, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Products) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editProducts("rate",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getPid());
					                	}
					                }
					            }
					        );
					}catch(Exception e){
						DialogueBox.error(e);
					}
				});
				return null;
			}
		};
		Thread t = new Thread(task);
		t.start();
		while(t.isAlive()){
			loading();
		}
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void onClickSearch(){
		if(!(pSearch.getText().length() < 1)){
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							Database data = new Database();
							ResultSet rs = data.query("Select * from products where company_name like ? OR item_name"
									+ " like ? ", "%"+pSearch.getText()+"%", "%"+pSearch.getText()+"%");
							loadTable(rs);
							data.close();
							rs.close();
							pSearch.setText(null);
						}catch(Exception e){
							DialogueBox.error(e);
						}
					});
					return null;
				}
			};
			Thread t = new Thread(task);
			t.start();
			while(t.isAlive()){
				loading();
			}
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}
