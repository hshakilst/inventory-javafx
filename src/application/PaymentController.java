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

public class PaymentController implements Initializable{
	@FXML
	private TableView<Payment> tblPayment;
	@FXML
	private TableColumn<Payment, Number> colPid;
	@FXML
	private TableColumn<Payment, String> colDate;
	@FXML
	private TableColumn<Payment, String> colCompany;
	@FXML
	private TableColumn<Payment, Number> colChallan;
	@FXML
	private TableColumn<Payment, Number> colBill;
	@FXML
	private TableColumn<Payment, Number> colPo;
	@FXML
	private TableColumn<Payment, Number> colAmount;
	@FXML
	private TableColumn<Payment, String> colCheque;
	@FXML
	private Button payDel;
	@FXML
	private Button payEdit;
	@FXML
	private Button payRefresh;
	@FXML
	private Button btPaySearch;
	@FXML
	private TextField txPaySearch;
	@FXML
	private TextField payTotal;
	@FXML
	private DatePicker dpPayStart;
	@FXML
	private DatePicker dpPayEnd;
	
	private ObservableList<Payment> data;
	
	private static Stage primaryStage;
	private static Scene scene;
	private Payment p;
	
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
	    Scene s1 = new Scene(bx,893,526);
	    primaryStage.setScene(s1);
	    primaryStage.show();
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
	
	public void init(){
		colPid.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
		colChallan.setCellValueFactory(new PropertyValueFactory<>("challanNo"));
		colBill.setCellValueFactory(new PropertyValueFactory<>("billNo"));
		colPo.setCellValueFactory(new PropertyValueFactory<>("poNo"));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		colCheque.setCellValueFactory(new PropertyValueFactory<>("chequeNo"));
	}
	
	public void loadTable(ResultSet rs) throws SQLException{
		data = FXCollections.observableArrayList();
		try {
			while(rs.next()){
				String date = rs.getString(2);
				if(date != null){
					String arr[] = date.split("-");
					date = arr[2]+"/"+arr[1]+"/"+arr[0].substring(2);
				}
				data.add(new Payment(rs.getInt(1), date, rs.getString(3), rs.getLong(4), 
						rs.getLong(5), rs.getLong(6), rs.getDouble(7), rs.getString(8)));
			}
		} catch (SQLException e) {
			DialogueBox.error(e);
		}
		tblPayment.setItems(data);
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
						ResultSet rs = db.query("Select * from payment");
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
		if(tblPayment.getSelectionModel().getSelectedItem() != null){	
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							p = tblPayment.getSelectionModel().getSelectedItem();
							p.deletePayment(p.getId());
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
						colCompany.setCellFactory(TextFieldTableCell.forTableColumn());
						colCheque.setCellFactory(TextFieldTableCell.forTableColumn());
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
						colBill.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
						colPo.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
						colAmount.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
					            new EventHandler<CellEditEvent<Payment, String>>() {
					                @Override
					                public void handle(CellEditEvent<Payment, String> tab) {
					                	if(tab.getNewValue().isEmpty() || 
					                			!(Pattern.matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid! Date should be \"YYYY-MM-DD\" format!");
					                	}
					                	else{
					                		((Payment) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editPayment("date",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colCompany.setOnEditCommit(
					            new EventHandler<CellEditEvent<Payment, String>>() {
					                @Override
					                public void handle(CellEditEvent<Payment, String> tab) {
					                	if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Payment) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editPayment("company",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colCheque.setOnEditCommit(
					            new EventHandler<CellEditEvent<Payment, String>>() {
					                @Override
					                public void handle(CellEditEvent<Payment, String> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Payment) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editPayment("cheque_no",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colChallan.setOnEditCommit(
					            new EventHandler<CellEditEvent<Payment, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Payment, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Payment) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editPayment("challan_no",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colBill.setOnEditCommit(
								new EventHandler<CellEditEvent<Payment, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Payment, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Payment) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editPayment("bill_no",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colPo.setOnEditCommit(
								new EventHandler<CellEditEvent<Payment, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Payment, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Payment) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editPayment("po_no",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colAmount.setOnEditCommit(
								new EventHandler<CellEditEvent<Payment, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Payment, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Payment) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editPayment("amount",tab.getNewValue(), 
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
		if(!(txPaySearch.getText().length() < 1)){
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							Database data = new Database();
							ResultSet rs = null;
							if(dpPayStart.getValue() == null && dpPayEnd.getValue() == null){	
								rs = data.query("Select * from payment where (company like ? OR challan_no like ? or po_no like ? or "
										+ "cheque_no like ? or bill_no like ?) ", 
										"%"+txPaySearch.getText()+"%", "%"+txPaySearch.getText()+"%", "%"+txPaySearch.getText()+"%"
										, "%"+txPaySearch.getText()+"%", "%"+txPaySearch.getText()+"%");
							}
							else if(dpPayStart.getValue() == null || dpPayEnd.getValue() == null){
								String date;
								if(dpPayStart.getValue() == null){
									date = dpPayEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								else{
									date = dpPayStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								rs = data.query("Select * from payment where (company like ? or challan_no like ? or po_no like ? or "
										+ "cheque_no like ? or bill_no like ?) and date like ?", "%"+txPaySearch.getText()+"%", 
										"%"+txPaySearch.getText()+"%", "%"+txPaySearch.getText()+"%", "%"+txPaySearch.getText()+"%", 
										"%"+txPaySearch.getText()+"%", "%"+date+"%");
							}
							else{
								String sDate;
								String eDate;
								sDate = dpPayStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								eDate = dpPayEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								rs = data.query("Select * from payment where date between ? and ? and "
										+ "(company like ? or challan_no like ? or po_no like ? or vat_no like ? or bill_no like ?)", 
										sDate, eDate, "%"+txPaySearch.getText()+"%", "%"+txPaySearch.getText()+"%", "%"+txPaySearch.getText()+"%",
										"%"+txPaySearch.getText()+"%", "%"+txPaySearch.getText()+"%");
								
							}
							loadTable(rs);
							data.close();
							rs.close();
							dpPayStart.setValue(null);
							dpPayEnd.setValue(null);
							txPaySearch.setText(null);
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
		else if(txPaySearch.getText().length() < 1 && (dpPayStart.getValue() != null || dpPayEnd.getValue() != null)){
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							Database data = new Database();
							ResultSet rs = null;
							if(dpPayStart.getValue() == null || dpPayEnd.getValue() == null){
								String date;
								if(dpPayStart.getValue() == null){
									date = dpPayEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								else{
									date = dpPayStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								rs = data.query("Select * from payment where date like ?", "%"+date+"%");
							}
							else{
								String sDate;
								String eDate;
								sDate = dpPayStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								eDate = dpPayEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								rs = data.query("Select * from payment where date between ? and ? ", sDate, eDate);
								
							}
							if(rs != null){
								loadTable(rs);
								rs.close();
							}
							data.close();
							dpPayStart.setValue(null);
							dpPayEnd.setValue(null);
							txPaySearch.setText(null);
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
	
	public String addTotal(ObservableList<Payment> data){
		double total = 0;
		for(Payment d : data){
			total += (d.getAmount());
		}
		return String.valueOf(total);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datePickerFormatter("dd/MM/YY", dpPayStart);
		datePickerFormatter("dd/MM/YY", dpPayEnd);
		tblPayment.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tblPayment.getSelectionModel().getSelectedItems().addListener((Change<? extends Payment> change) -> {
			ObservableList<Payment> selectedItems = tblPayment.getSelectionModel().getSelectedItems();
			payTotal.setText(addTotal(selectedItems));
		});
	}

}
