package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class PaperConController implements Initializable{
	@FXML
	private TableView<PaperConsumption> tblPurchase;
	@FXML
	private TableColumn<PaperConsumption, Number> colPid;
	@FXML
	private TableColumn<PaperConsumption, String> colDate;
	@FXML
	private TableColumn<PaperConsumption, String> colTCom;
	@FXML
	private TableColumn<PaperConsumption, Number> colChallan;
	@FXML
	private TableColumn<PaperConsumption, String> colType;
	@FXML
	private TableColumn<PaperConsumption, String> colMill;
	@FXML
	private TableColumn<PaperConsumption, Number> colSize;
	@FXML
	private TableColumn<PaperConsumption, Number> colWeight;
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
	@FXML
	private TextField txTotal;
	@FXML
	private DatePicker dpStart;
	@FXML
	private DatePicker dpEnd;
	
	private ObservableList<PaperConsumption> data;
	private static Stage primaryStage;
	private static Scene scene;
	private PaperConsumption p;
	public static Stage secondary;
	
	public static void share(Stage p, Scene s){
		primaryStage = p;
		scene = s;
	}
	
	public void datePickerFormatter(String pattern, DatePicker date){
		Task<Void> task = new Task<Void>(){
			@Override
            protected Void call() throws Exception {
				Platform.runLater(()->{
					try{
						date.setConverter(new StringConverter<LocalDate>() {
						     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

						     @Override 
						     public String toString(LocalDate date) {
						         if (date != null) {
						             return dateFormatter.format(date);
						         } else {
						             return "";
						         }
						     }

						     @Override 
						     public LocalDate fromString(String string) {
						         if (string != null && !string.isEmpty()) {
						             return LocalDate.parse(string, dateFormatter);
						         } else {
						             return null;
						         }
						     }
						 });
					}catch(Exception e){
						DialogueBox.error(e);
					}
				});
				return null;
			}
		};
		Thread t = new Thread(task);
		t.start();
	}
	
	public void loading(){
		final VBox bx = new VBox();
	    bx.setAlignment(Pos.CENTER);
	    ProgressIndicator pi = new ProgressIndicator();
	    Label label = new Label("Loading...");
	    bx.getChildren().addAll(pi,label);
	    Scene s1 = new Scene(bx,879.0,594);
	    primaryStage.setScene(s1);
	    primaryStage.show();
	}
	
	public void init(){
		colPid.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colTCom.setCellValueFactory(new PropertyValueFactory<>("tradingCompany"));
		colChallan.setCellValueFactory(new PropertyValueFactory<>("challan"));
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));
		colMill.setCellValueFactory(new PropertyValueFactory<>("mill"));
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
		colWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));/*
		colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));*/
	}
	
	public void loadTable(ResultSet rs) throws SQLException{
		data = FXCollections.observableArrayList();
		try {
			while(rs.next()){
				String date = rs.getString(3);
				if(date != null){
					String arr[] = date.split("-");
					date = arr[2]+"/"+arr[1]+"/"+arr[0].substring(2);
				}
				data.add(new PaperConsumption(rs.getInt(1), rs.getString(2), date, rs.getLong(4), rs.getString(5), rs.getString(6), 
						rs.getDouble(7), rs.getDouble(8)));
			}
		} catch (SQLException e) {
			DialogueBox.error(e);
		}
		tblPurchase.setItems(data);
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
						ResultSet rs = db.query("Select * from paper_consumption");
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
		if(tblPurchase.getSelectionModel().getSelectedItem() != null){	
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							p = tblPurchase.getSelectionModel().getSelectedItem();
							p.deleteConsume(p.getId());
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
						colDate.setCellFactory(TextFieldTableCell.forTableColumn());
						colTCom.setCellFactory(TextFieldTableCell.forTableColumn());
						colType.setCellFactory(TextFieldTableCell.forTableColumn());
						colMill.setCellFactory(TextFieldTableCell.forTableColumn());
						colChallan.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
							@Override
				            public String toString(Number number) {
				                return String.valueOf(number.longValue());
				            }
							@Override
							public Long fromString(String string) {
				                try {
				                    long value = Long.parseLong(string);
				                    return value;
				                } catch (NumberFormatException e) {
				                	Alert alert = new Alert(AlertType.WARNING);
				            		alert.setTitle("Warning!");
				            		alert.setHeaderText(null);
				            		alert.setContentText("Invalid input detected!");
				            		alert.showAndWait();
				            		return Long.parseLong(toString());
				                }
				            }
						}));
						colSize.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
						colWeight.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
						colDate.setOnEditCommit(
					            new EventHandler<CellEditEvent<PaperConsumption, String>>() {
					                @Override
					                public void handle(CellEditEvent<PaperConsumption, String> tab) {
					                	if(tab.getNewValue().isEmpty() || 
					                			!(Pattern.matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid! Date should be \"YYYY-MM-DD\" format!");
					                	}
					                	else{
					                		((PaperConsumption) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editConsume("date",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colTCom.setOnEditCommit(
					            new EventHandler<CellEditEvent<PaperConsumption, String>>() {
					                @Override
					                public void handle(CellEditEvent<PaperConsumption, String> tab) {
					                	if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((PaperConsumption) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editConsume("trading_company",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colType.setOnEditCommit(
								new EventHandler<CellEditEvent<PaperConsumption, String>>() {
					                @Override
					                public void handle(CellEditEvent<PaperConsumption, String> tab) {
					                	if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((PaperConsumption) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editConsume("type",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colMill.setOnEditCommit(
								new EventHandler<CellEditEvent<PaperConsumption, String>>() {
					                @Override
					                public void handle(CellEditEvent<PaperConsumption, String> tab) {
					                	if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((PaperConsumption) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editConsume("mill",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colChallan.setOnEditCommit(
								new EventHandler<CellEditEvent<PaperConsumption, Number>>() {
					                @Override
					                public void handle(CellEditEvent<PaperConsumption, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((PaperConsumption) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editConsume("challan_no",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colSize.setOnEditCommit(
								new EventHandler<CellEditEvent<PaperConsumption, Number>>() {
					                @Override
					                public void handle(CellEditEvent<PaperConsumption, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((PaperConsumption) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editConsume("size",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colWeight.setOnEditCommit(
								new EventHandler<CellEditEvent<PaperConsumption, Number>>() {
					                @Override
					                public void handle(CellEditEvent<PaperConsumption, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((PaperConsumption) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editConsume("weight",tab.getNewValue(), 
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
							ResultSet rs = null;
							if(dpStart.getValue() == null && dpEnd.getValue() == null){	
								rs = data.query("Select * from paper_consumption where (trading_company like ? OR challan_no"
										+ " like ? or type like ? or mill like ? or size like ? ) ", 
										"%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%"
										, "%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%");
							}
							else if(dpStart.getValue() == null || dpEnd.getValue() == null){
								String date;
								if(dpStart.getValue() == null){
									date = dpEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								else{
									date = dpStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								rs = data.query("Select * from paper_consumption where (trading_company like ? OR challan_no"
										+ " like ? or type like ? or mill like ? or size like ?) "
										+ "and date like ?", "%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%", 
										"%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%", 
										"%"+date+"%");
							}
							else{
								String sDate;
								String eDate;
								sDate = dpStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								eDate = dpEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								rs = data.query("Select * from paper_consumption where date between ? and ? and "
										+ "(trading_company like ? OR challan_no like ? or type like ? or mill like ? or size like ?)", 
										sDate, eDate, "%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%","%"+txSearch.getText()+"%", 
										"%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%");
								
							}
							loadTable(rs);
							data.close();
							rs.close();
							dpStart.getEditor().setText("");
							dpEnd.getEditor().setText("");
							txSearch.setText("");
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
		}
		else if(txSearch.getText().length() < 1 && (dpStart.getValue() != null || dpEnd.getValue() != null)){
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							Database data = new Database();
							ResultSet rs = null;
							if(dpStart.getValue() == null || dpEnd.getValue() == null){
								String date;
								if(dpStart.getValue() == null){
									date = dpEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								else{
									date = dpStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								rs = data.query("Select * from paper_consumption where date like ?", "%"+date+"%");
							}
							else{
								String sDate;
								String eDate;
								sDate = dpStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								eDate = dpEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								rs = data.query("Select * from paper_consumption where date between ? and ? ", sDate, eDate);
								
							}
							if(rs != null){
								loadTable(rs);
								rs.close();
							}
							data.close();

							dpStart.getEditor().setText("");
							dpEnd.getEditor().setText("");
							txSearch.setText("");
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
		}
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public String addTotal(ObservableList<PaperConsumption> data){
		double total = 0;
		for(PaperConsumption d : data){
			total += d.getWeight();
		}
		return String.valueOf(total);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datePickerFormatter("dd/MM/YY", dpStart);
		datePickerFormatter("dd/MM/YY", dpEnd);
		tblPurchase.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tblPurchase.getSelectionModel().getSelectedItems().addListener((Change<? extends PaperConsumption> change) -> {
			ObservableList<PaperConsumption> selectedItems = tblPurchase.getSelectionModel().getSelectedItems();
			txTotal.setText(addTotal(selectedItems));
		});
	}

}
