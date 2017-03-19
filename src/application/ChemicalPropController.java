package application;

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
import javafx.util.converter.NumberStringConverter;

public class ChemicalPropController implements Initializable{
	@FXML
	private TableView<ChemicalProperty> tblProperty;
	@FXML
	private TableColumn<ChemicalProperty, Number> colId;
	@FXML
	private TableColumn<ChemicalProperty, String> colCompany;
	@FXML
	private TableColumn<ChemicalProperty, String> colMfg;
	@FXML
	private TableColumn<ChemicalProperty, Number> colRate;
	@FXML
	private Button btRefresh;
	@FXML
	private Button btDel;
	@FXML
	private Button btEdit;
	@FXML
	private Button btSearch;
	@FXML
	private TextField txSearch;
	
	private ObservableList<ChemicalProperty> data;
	
	private static Stage primaryStage;
	private static Scene scene;
	private ChemicalProperty p;
	
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
	    Scene s1 = new Scene(bx,504,490);
	    primaryStage.setScene(s1);
	    primaryStage.show();
	}
	
	public void init(){
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colCompany.setCellValueFactory(new PropertyValueFactory<>("tradingCompany"));
		colMfg.setCellValueFactory(new PropertyValueFactory<>("mfgCompany"));
		colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
	}
	
	public void loadTable(ResultSet rs) throws SQLException{
		data = FXCollections.observableArrayList();
		try {
			while(rs.next()){
				data.add(new ChemicalProperty(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(5)));
			}
		} catch (SQLException e) {
			DialogueBox.error(e);
		}
		tblProperty.setItems(data);
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
						ResultSet rs = db.query("Select * from chemical_property");
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
		if(tblProperty.getSelectionModel().getSelectedItem() != null){	
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							p = tblProperty.getSelectionModel().getSelectedItem();
							p.deleteProperty(p.getId());
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
						colCompany.setCellFactory(TextFieldTableCell.forTableColumn());
						colMfg.setCellFactory(TextFieldTableCell.forTableColumn());
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
						colCompany.setOnEditCommit(
					            new EventHandler<CellEditEvent<ChemicalProperty, String>>() {
					                @Override
					                public void handle(CellEditEvent<ChemicalProperty, String> tab) {
					                	if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((ChemicalProperty) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editProperty("trading_company",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colMfg.setOnEditCommit(
								new EventHandler<CellEditEvent<ChemicalProperty, String>>() {
					                @Override
					                public void handle(CellEditEvent<ChemicalProperty, String> tab) {
					                	if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((ChemicalProperty) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editProperty("mfg_company",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colRate.setOnEditCommit(
					            new EventHandler<CellEditEvent<ChemicalProperty, Number>>() {
					                @Override
					                public void handle(CellEditEvent<ChemicalProperty, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((ChemicalProperty) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editProperty("rate",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
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
		if(!(txSearch.getText().length() < 1)){
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							Database data = new Database();
							ResultSet rs = data.query("Select * from chemical_property where trading_company like ? OR mfg_company"
									+ " like ?", "%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%");
							loadTable(rs);
							data.close();
							rs.close();
							txSearch.setText(null);
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
		// TODO Auto-generated method stub
		
	}

}
