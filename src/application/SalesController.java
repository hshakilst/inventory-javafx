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
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
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

public class SalesController implements Initializable{
	@FXML
	private TableView<Sales> tblSales;
	@FXML
	private TableColumn<Sales, Number> colSid;
	@FXML
	private TableColumn<Sales, String> colDate;
	@FXML
	private TableColumn<Sales, Number> colChallanNo;
	@FXML
	private TableColumn<Sales, Number> colPoNo;
	@FXML
	private TableColumn<Sales, Number> colVatNo;
	@FXML
	private TableColumn<Sales, Number> colBillNo;
	@FXML
	private TableColumn<Sales, String> colCompanyName;
	@FXML
	private TableColumn<Sales, String> colProductName;
	@FXML
	private TableColumn<Sales, Number> colQty;
	@FXML
	private TableColumn<Sales, Number> colRate;
	@FXML
	private TableColumn<Sales, Number> colVatAmnt;
	@FXML
	private TableColumn<Sales, Number> colAitAmnt;
	@FXML
	private Button btSaleRefresh;
	@FXML
	private Button btSaleDel;
	@FXML
	private Button btSaleEdit;
	@FXML
	private Button btSaleSearch;
	@FXML
	private TextField txSaleSearch;
	@FXML
	private TextField txSaleTotal;
	@FXML
	private DatePicker dpSaleStart;
	@FXML
	private DatePicker dpSaleEnd;
	
	private ObservableList<Sales> data;
	private static Stage primaryStage;
	private static Scene scene;
	private Sales s;
	
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
	    Scene s1 = new Scene(bx,1077,560);
	    primaryStage.setScene(s1);
	    primaryStage.show();
	}
	
	public void init(){
		colSid.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colChallanNo.setCellValueFactory(new PropertyValueFactory<>("challanNo"));
		colPoNo.setCellValueFactory(new PropertyValueFactory<>("poNo"));
		colVatNo.setCellValueFactory(new PropertyValueFactory<>("vatNo"));
		colBillNo.setCellValueFactory(new PropertyValueFactory<>("billNo"));
		colCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
		colProductName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
		colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
		colVatAmnt.setCellValueFactory(new PropertyValueFactory<>("vat"));
		colAitAmnt.setCellValueFactory(new PropertyValueFactory<>("ait"));
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
				data.add(new Sales(rs.getInt(1), date, rs.getLong(3), rs.getLong(4), 
						rs.getLong(5), rs.getLong(6), rs.getString(7), rs.getString(8), rs.getInt(9), 
						rs.getDouble(10),rs.getDouble(11), rs.getDouble(12)));
			}
		} catch (SQLException e) {
			DialogueBox.error(e);
		}
		tblSales.setItems(data);
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
						ResultSet rs = db.query("Select sid, date, challan_no, po_no, vat_no, bill_no, company, "
								+ "item, qty, rate, vat_amnt, ait_amnt from sales");
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
		if(tblSales.getSelectionModel().getSelectedItem() != null){	
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							s = tblSales.getSelectionModel().getSelectedItem();
							s.deleteSales(s.getId());
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
						colCompanyName.setCellFactory(TextFieldTableCell.forTableColumn());
						colProductName.setCellFactory(TextFieldTableCell.forTableColumn());
						colChallanNo.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
						colPoNo.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
						colVatNo.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
						colBillNo.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
						colQty.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
							@Override
				            public String toString(Number number) {
				                return String.valueOf(number.intValue());
				            }
							@Override
							public Integer fromString(String string) {
				                try {
				                    int value = Integer.parseInt(string);
				                    return value;
				                } catch (NumberFormatException e) {
				                	Alert alert = new Alert(AlertType.WARNING);
				            		alert.setTitle("Warning!");
				            		alert.setHeaderText(null);
				            		alert.setContentText("Invalid input detected!");
				            		alert.showAndWait();
				            		return Integer.parseInt(toString());
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
						colVatAmnt.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
						colAitAmnt.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
					            new EventHandler<CellEditEvent<Sales, String>>() {
					                @Override
					                public void handle(CellEditEvent<Sales, String> tab) {
					                	if(tab.getNewValue().isEmpty() || 
					                			!(Pattern.matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid! Date should be \"YYYY-MM-DD\" format!");
					                	}
					                	else{
					                		((Sales) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editSales("date",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colCompanyName.setOnEditCommit(
					            new EventHandler<CellEditEvent<Sales, String>>() {
					                @Override
					                public void handle(CellEditEvent<Sales, String> tab) {
					                	if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Sales) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editSales("company",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colProductName.setOnEditCommit(
								new EventHandler<CellEditEvent<Sales, String>>() {
					                @Override
					                public void handle(CellEditEvent<Sales, String> tab) {
					                	if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Sales) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editSales("item",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colChallanNo.setOnEditCommit(
								new EventHandler<CellEditEvent<Sales, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Sales, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Sales) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editSales("challan_no",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colVatNo.setOnEditCommit(
								new EventHandler<CellEditEvent<Sales, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Sales, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Sales) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editSales("vat_no",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colBillNo.setOnEditCommit(
								new EventHandler<CellEditEvent<Sales, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Sales, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Sales) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editSales("bill_no",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colPoNo.setOnEditCommit(
								new EventHandler<CellEditEvent<Sales, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Sales, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Sales) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editSales("po_no",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colRate.setOnEditCommit(
								new EventHandler<CellEditEvent<Sales, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Sales, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Sales) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editSales("rate",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colVatAmnt.setOnEditCommit(
								new EventHandler<CellEditEvent<Sales, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Sales, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Sales) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editSales("vat_amnt",tab.getNewValue(), 
							                        		tab.getTableView().getSelectionModel().getSelectedItem().getId());
					                	}
					                }
					            }
					        );
						colAitAmnt.setOnEditCommit(
								new EventHandler<CellEditEvent<Sales, Number>>() {
					                @Override
					                public void handle(CellEditEvent<Sales, Number> tab) {
					                	if(String.valueOf(tab.getNewValue()).isEmpty() || 
					                			!(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
					                		DialogueBox.warning("Invalid input detected!");
					                	}
					                	else{
					                		((Sales) tab.getTableView().getItems().get(
							                        tab.getTablePosition().getRow())
							                        ).editSales("ait_amnt",tab.getNewValue(), 
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
		if(!(txSaleSearch.getText().length() < 1)){
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							Database data = new Database();
							ResultSet rs = null;
							if(dpSaleStart.getValue() == null && dpSaleEnd.getValue() == null){	
								rs = data.query("Select * from sales where (company like ? OR item"
										+ " like ? or challan_no like ? or po_no like ? or vat_no like ? or bill_no like ?) ", 
										"%"+txSaleSearch.getText()+"%", "%"+txSaleSearch.getText()+"%", "%"+txSaleSearch.getText()+"%"
										, "%"+txSaleSearch.getText()+"%", "%"+txSaleSearch.getText()+"%", "%"+txSaleSearch.getText()+"%");
							}
							else if(dpSaleStart.getValue() == null || dpSaleEnd.getValue() == null){
								String date;
								if(dpSaleStart.getValue() == null){
									date = dpSaleEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								else{
									date = dpSaleStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								rs = data.query("Select * from sales where (company like ? OR item"
										+ " like ? or challan_no like ? or po_no like ? or vat_no like ? or bill_no like ?) "
										+ "and date like ?", "%"+txSaleSearch.getText()+"%", "%"+txSaleSearch.getText()+"%", 
										"%"+txSaleSearch.getText()+"%", "%"+txSaleSearch.getText()+"%", "%"+txSaleSearch.getText()+"%", 
										"%"+txSaleSearch.getText()+"%", "%"+date+"%");
							}
							else{
								String sDate;
								String eDate;
								sDate = dpSaleStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								eDate = dpSaleEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								rs = data.query("Select * from sales where date between ? and ? and "
										+ "(company like ? OR item like ? or challan_no like ? or po_no like ? or vat_no like ? or bill_no like ?)", 
										sDate, eDate, "%"+txSaleSearch.getText()+"%", "%"+txSaleSearch.getText()+"%", "%"+txSaleSearch.getText()+"%",
										"%"+txSaleSearch.getText()+"%", "%"+txSaleSearch.getText()+"%", "%"+txSaleSearch.getText()+"%");
							}
							loadTable(rs);
							data.close();
							rs.close();
							dpSaleStart.setValue(null);
							dpSaleEnd.setValue(null);
							txSaleSearch.setText(null);
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
		else if(txSaleSearch.getText().length() < 1 && (dpSaleStart.getValue() != null || dpSaleEnd.getValue() != null)){
			Task<Void> task = new Task<Void>(){
				@Override
	            protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							Database data = new Database();
							ResultSet rs = null;
							if(dpSaleStart.getValue() == null || dpSaleEnd.getValue() == null){
								String date;
								if(dpSaleStart.getValue() == null){
									date = dpSaleEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								else{
									date = dpSaleStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								}
								rs = data.query("Select * from sales where date like ?", "%"+date+"%");
							}
							else{
								String sDate;
								String eDate;
								sDate = dpSaleStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								eDate = dpSaleEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
								rs = data.query("Select * from sales where date between ? and ? ", sDate, eDate);
							}
							if(rs != null){
								loadTable(rs);
								rs.close();
							}
							data.close();
							dpSaleStart.setValue(null);
							dpSaleEnd.setValue(null);
							txSaleSearch.setText(null);
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
	
	public String addTotal(ObservableList<Sales> data){
		double total = 0;
		for(Sales d : data){
			total += (d.getRate()+d.getVat()+d.getAit());
		}
		return String.valueOf(total);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datePickerFormatter("dd/MM/YY", dpSaleStart);
		datePickerFormatter("dd/MM/YY", dpSaleEnd);
		tblSales.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tblSales.getSelectionModel().getSelectedItems().addListener((Change<? extends Sales> change) -> {
			ObservableList<Sales> selectedItems = tblSales.getSelectionModel().getSelectedItems();
			txSaleTotal.setText(addTotal(selectedItems));
		});
	}

}
