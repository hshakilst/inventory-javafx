package application;

import javafx.concurrent.Task;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;


public class MainController implements Initializable{
	//Products variables
	@FXML
	private TextField pComName;
	@FXML
	private TextField pItemName;
	@FXML
	private TextField pUPin;
	@FXML
	private TextField pUPrint;
	@FXML
	private TextField pUMedia;
	@FXML
	private TextField pULiner;
	@FXML
	private TextField pRate;
	@FXML
	private Button pCreate;
	@FXML
	private Button pRecords;
	
	//Sales variables
	@FXML
	private DatePicker sDate;
	@FXML
	private TextField sChallan;
	@FXML
	private TextField sPo;
	@FXML
	private TextField sVat;
	@FXML
	private TextField sBill;
	@FXML
	private ComboBox<String> sCompany;
	@FXML
	private ComboBox<String> sItem;
	@FXML
	private TextField sQty;
	@FXML
	private TextField sRate;
	@FXML
	private RadioButton sInclude;
	@FXML
	private RadioButton sExclude;
	@FXML
	private TextField sVAmount;
	@FXML
	private TextField sAAmount;
	@FXML
	private Button sSubmit;
	@FXML
	private Button sRecords;
	
	//payment members
	@FXML
	private DatePicker payDate;
	@FXML
	private ComboBox<String> payCompany;
	@FXML
	private TextField payChallan;
	@FXML
	private TextField payBill;
	@FXML
	private TextField payPo;
	@FXML
	private TextField payAmount;
	@FXML
	private TextField payCheque;
	@FXML
	private Button paySubmit;
	@FXML
	private Button payRecords;
	
	//paper variables
	@FXML
	private ComboBox<String> paperTCompany;
	@FXML
	private DatePicker paperDate;
	@FXML
	private TextField paperChallan;
	@FXML
	private ComboBox<Double> paperSize;
	@FXML
	private TextField paperWeight;
	@FXML
	private TextField paperRate;
	@FXML
	private TextField paperTCost;
	@FXML
	private ComboBox<String> paperType;
	@FXML
	private ComboBox<String> paperMill;
	@FXML
	private Button paperSubmit;
	@FXML
	private Button paperPRecords;
	
	@FXML
	private ComboBox<String> paperCTCompany;
	@FXML
	private DatePicker paperCDate;
	@FXML
	private TextField paperCChallan;
	@FXML
	private ComboBox<Double> paperCSize;
	@FXML
	private TextField paperCWeight;
	@FXML
	private ComboBox<String> paperCType;
	@FXML
	private ComboBox<String> paperCMill;
	@FXML
	private Button paperCSubmit;
	@FXML
	private Button paperCRecords;
	
	@FXML
	private TextField txCreateTCompany;
	@FXML
	private TextField txCreatePType;
	@FXML
	private TextField txCreatePMill;
	@FXML
	private TextField txCreatePSize;
	@FXML
	private TextField txCreatePRate;
	@FXML
	private Button btCreatePaper;
	@FXML
	private Button btPProperty;
	
	private static Stage primaryStage;
	private static Scene scene;
	private static int pCount = 0;
	private static int sCount = 0;
	private static int payCount = 0;
	private static int paperPCount = 0;
	private static int paperCCount = 0;
	private static int pPropCount = 0;
	private static Stage secondary;
	private static Stage secondary1;
	private static Stage secondary2;
	private static Stage secondary3;
	private static Stage secondary4;
	private static Stage secondary5;
	private ToggleGroup toggle;
	
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
	    Scene s1 = new Scene(bx,1263,611);
	    primaryStage.setScene(s1);
	    primaryStage.show();
	}
	
	public void onClickPCreate(ActionEvent evt){
		if(pComName.getText().isEmpty() || pItemName.getText().isEmpty() || pUPin.getText().isEmpty() ||
				pUPrint.getText().isEmpty() || pUMedia.getText().isEmpty() || pULiner.getText().isEmpty() ||
				pRate.getText().isEmpty() ){
			DialogueBox.warning("Please fill all the required fields!");
		}
		else if(!(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/]+", pComName.getText())) || 
				!(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/]+", pItemName.getText())) || 
				!(Pattern.matches("[\\d\\.]+", pUPin.getText())) || !(Pattern.matches("[\\d\\.]+", pUPrint.getText())) || 
				!(Pattern.matches("[\\d\\.]+", pUMedia.getText())) || !(Pattern.matches("[\\d\\.]+", pULiner.getText())) ||
				!(Pattern.matches("[\\d\\.]+", pRate.getText()))){
			DialogueBox.warning("Invalid input detected!");
		}
		else{
			Task<Void> task = new Task<Void>(){
				@Override
                protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							Products product = new Products(pComName.getText(), pItemName.getText(), Double.parseDouble(pUPin.getText()),
									Double.parseDouble(pUPrint.getText()), Double.parseDouble(pUMedia.getText()),
									Double.parseDouble(pULiner.getText()), Double.parseDouble(pRate.getText()));
							product.createProduct();
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
		this.initSalesCombo();
		this.initPayCombo();
	}
	
	public void onClickPRecords(ActionEvent evt) throws IOException{
		if(pCount < 1){
			secondary = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Products.fxml"));
			Parent root = (Parent)loader.load();
			ProductsController controller = (ProductsController)loader.getController();
			Scene s = new Scene(root,893,490);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary.setScene(s);
			secondary.resizableProperty().set(false);
			ProductsController.share(secondary, s);
			secondary.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override 
			    public void handle(WindowEvent t) {
			    	secondary.close();
			    	pCount = 0;
			    }
			});
			controller.onClickRefresh();
			secondary.show();
			pCount++;
		}
		else{
			if(secondary != null){
				secondary.requestFocus();
			}
		}
	}
	
	public void onClickSaleSubmit(){
		if(sDate.getValue() == null || sChallan.getText().isEmpty() || sPo.getText().isEmpty() ||
				sVat.getText().isEmpty() || sBill.getText().isEmpty() || 
				sCompany.getSelectionModel().getSelectedItem() == null ||
				sItem.getSelectionModel().getSelectedItem() == null || 
				sQty.getText().isEmpty() || sRate.getText().isEmpty() || 
				sVAmount.getText().isEmpty() || sAAmount.getText().isEmpty()){
			DialogueBox.warning("Please fill all the required fields!");
		}
		else if(!(Pattern.matches("[\\d]+", sChallan.getText())) || !(Pattern.matches("[\\d]+", sPo.getText())) || 
				!(Pattern.matches("[\\d]+", sVat.getText())) || !(Pattern.matches("[\\d]+", sBill.getText())) ||
				!(Pattern.matches("[\\d]+", sQty.getText())) || !(Pattern.matches("[\\d\\.]+", sRate.getText()))){
			DialogueBox.warning("Invalid input detected!");
		}
		else{
			Task<Void> task = new Task<Void>(){
				@Override
                protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							LocalDate date = sDate.getValue();
							DateTimeFormatter f = DateTimeFormatter.ofPattern("YYYY-MM-dd");
							Sales sales = new Sales(date.format(f), Long.parseLong(sChallan.getText()), Long.parseLong(sPo.getText()), 
									Long.parseLong(sVat.getText()), Long.parseLong(sBill.getText()), 
									sCompany.getSelectionModel().getSelectedItem().toString(), 
									sItem.getSelectionModel().getSelectedItem().toString(), Integer.parseInt(sQty.getText()),
									Double.parseDouble(sRate.getText()),
									Double.parseDouble(sVAmount.getText()), Double.parseDouble(sAAmount.getText()));
							sales.salesEntry();
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
	
	public void initSalesCombo(){
		Task<Void> task = new Task<Void>(){
			@Override
            protected Void call() throws Exception {
				Platform.runLater(()->{
					Database data = null;
					try{
						ObservableList<String> company = FXCollections.observableArrayList();
						ObservableList<String> item = FXCollections.observableArrayList();
						data = new Database();
						ResultSet rs = data.query("Select distinct company_name from products");
						while(rs.next()){
							company.add(rs.getString(1));
						}
						if(company.isEmpty()){
							company.add("None");
						}
						sCompany.setItems(company);
						sCompany.setOnAction((event)->{
							if(sCompany.getSelectionModel().getSelectedItem().toString().contains("None")){
								item.removeAll(item);
								item.add("None");
							}
							else{
								item.removeAll(item);
								Database dat = new Database();
								ResultSet rs1 = dat.query("select distinct item_name from products where company_name = ?",
										sCompany.getSelectionModel().getSelectedItem().toString());
								try {
									while(rs1.next()){
										item.add(rs1.getString(1));
									}
									if(item.isEmpty()){
										item.removeAll(item);
										item.add("None");
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									DialogueBox.error(e);
								} finally{
									dat.close();
									try {
										rs1.close();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										DialogueBox.error(e);
									}
								}
							}
							sItem.setItems(item);
							try {
								rs.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								DialogueBox.error(e);
							}
						});
						sQty.setOnAction((event) -> {
							if(sCompany.getSelectionModel().getSelectedItem() == null ||
									sItem.getSelectionModel().getSelectedItem() == null ||
									!(Pattern.matches("[\\d]+", sQty.getText()))){
								sRate.setText("0.0");
							}
							else{
								Database db = new Database();
								ResultSet rs2 = db.query("select rate from products where company_name = ? and item_name = ? ", 
										sCompany.getSelectionModel().getSelectedItem().toString(), 
										sItem.getSelectionModel().getSelectedItem().toString());
								double rate = 0.0;
								try {
									while(rs2.next()){
										rate = rs2.getDouble(1);
										System.out.println(1);
									}
									sRate.setText(String.valueOf(rate*Double.parseDouble(sQty.getText())));
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									DialogueBox.error(e);
								} finally{
									db.close();
									try {
										rs2.close();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										DialogueBox.error(e);
									}
								}
							}
						});
					}catch(Exception e){
						DialogueBox.error(e);
					}
					data.close();
				});
				return null;
			}
		};
		Thread t = new Thread(task);
		t.start();
	}
	
	public void onClickSRecords() throws IOException{
		if(sCount < 1){
			secondary1 = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Sales.fxml"));
			Parent root = (Parent)loader.load();
			SalesController controller = (SalesController)loader.getController();
			Scene s = new Scene(root,1077,560);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary1.setScene(s);
			secondary1.resizableProperty().set(false);
			SalesController.share(secondary1, s);
			secondary1.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override 
			    public void handle(WindowEvent t) {
			    	secondary1.close();
			    	sCount = 0;
			    }
			});
			controller.onClickRefresh();
			secondary1.show();
			sCount++;
		}
		else{
			if(secondary1 != null){
				secondary1.requestFocus();
			}
		}
	}
	
	public void initPayCombo(){
		Task<Void> task = new Task<Void>(){
			@Override
            protected Void call() throws Exception {
				Platform.runLater(()->{
					try{
						Database data = new Database();
						ResultSet rs = data.query("Select distinct company_name from products");
						ObservableList<String> company = FXCollections.observableArrayList();
						while(rs.next()){
							company.add(rs.getString(1));
						}
						payCompany.setItems(company);
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
	
	public void initPayTextField(){
		Task<Void> task = new Task<Void>(){
			@Override
            protected Void call() throws Exception {
				Platform.runLater(()->{
					try{
						Database data = new Database();
						ResultSet rs = data.query("Select bill_no, po_no from sales where company = ? and challan_no = ?", 
								payCompany.getSelectionModel().getSelectedItem().toString(), Long.parseLong(payChallan.getText()));
						while(rs.next()){
							payBill.setText(String.valueOf(rs.getLong(1)));
							payPo.setText(String.valueOf(rs.getLong(2)));
						}
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
	
	public void onClickPaySubmit(){
		if(payDate.getValue() == null || payCompany.getSelectionModel().getSelectedItem() == null || 
				payChallan.getText().isEmpty() || payBill.getText().isEmpty() || payPo.getText().isEmpty() ||
				payAmount.getText().isEmpty() || payCheque.getText().isEmpty()){
			DialogueBox.warning("Please fill all the required fields!");
		}
		else if(!(Pattern.matches("[\\d]+", payChallan.getText())) || !(Pattern.matches("[\\d]+", payBill.getText())) || 
				!(Pattern.matches("[\\d]+", payPo.getText())) || !(Pattern.matches("[\\d\\.]+", payAmount.getText())) ||
				!(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", payCheque.getText()))){
			DialogueBox.warning("Invalid input detected!");
		}
		else{
			Task<Void> task = new Task<Void>(){
				@Override
                protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							LocalDate date = payDate.getValue();
							DateTimeFormatter f = DateTimeFormatter.ofPattern("YYYY-MM-dd");
							Payment pay = new Payment(date.format(f), payCompany.getSelectionModel().getSelectedItem().toString(), 
									Long.parseLong(payChallan.getText()), Long.parseLong(payBill.getText()), Long.parseLong(payPo.getText()), 
									Double.parseDouble(payAmount.getText()), payCheque.getText());
							pay.payEntry();
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
	
	public void onClickPayRecords() throws IOException{
		if(payCount < 1){
			secondary2 = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Payment.fxml"));
			Parent root = (Parent)loader.load();
			PaymentController controller = (PaymentController)loader.getController();
			Scene s = new Scene(root,893,526);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary2.setScene(s);
			secondary2.resizableProperty().set(false);
			PaymentController.share(secondary2, s);
			secondary2.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override 
			    public void handle(WindowEvent t) {
			    	secondary2.close();
			    	payCount = 0;
			    }
			});
			controller.onClickRefresh();
			secondary2.show();
			payCount++;
		}
		else{
			if(secondary2 != null){
				secondary2.requestFocus();
			}
		}
	}
	
	public void onClickCreatePapers(){
		if(txCreateTCompany.getText().isEmpty() || txCreatePType.getText().isEmpty() || txCreatePMill.getText().isEmpty() 
				|| txCreatePSize.getText().isEmpty() || txCreatePRate.getText().isEmpty()){
			DialogueBox.warning("Fill the required field!");
		}
		else if(!(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", txCreateTCompany.getText())) || 
				!(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", txCreatePType.getText())) || 
				!(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", txCreatePMill.getText())) ||
				!(Pattern.matches("[\\d\\.]+", txCreatePSize.getText())) || 
				!(Pattern.matches("[\\d\\.]+", txCreatePRate.getText()))){
			DialogueBox.warning("Invalid input detected!");
		}
		else{
			Task<Void> task = new Task<Void>(){
				@Override
                protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							PaperProperty prop = new PaperProperty(txCreateTCompany.getText(), txCreatePType.getText(), txCreatePMill.getText(), 
									Double.parseDouble(txCreatePSize.getText()), Double.parseDouble(txCreatePRate.getText()));
							prop.entryProperty();
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
		this.initPaperCombo(paperTCompany, paperType, paperMill, paperSize);
		this.initPaperCombo(paperCTCompany, paperCType, paperCMill, paperCSize);
	}
	
	public void onClickPPurchase(){
		if(paperTCompany.getSelectionModel().getSelectedItem() == null || paperDate.getValue() == null || paperChallan.getText().isEmpty() ||
				paperType.getSelectionModel().getSelectedItem() == null || paperMill.getSelectionModel().getSelectedItem() == null || 
				paperSize.getSelectionModel().getSelectedItem() == null || paperWeight.getText().isEmpty() || paperRate.getText().isEmpty() || 
				paperTCost.getText().isEmpty()){
			DialogueBox.warning("Fill the required field!");
		}
		else if(!(Pattern.matches("[\\d]+", paperChallan.getText())) || !(Pattern.matches("[\\d\\.]+", paperWeight.getText())) || 
				!(Pattern.matches("[\\d\\.]+", paperRate.getText())) || !(Pattern.matches("[\\d\\.]+", paperTCost.getText()))){
			DialogueBox.warning("Invalid input detected!");
		}
		else{
			Task<Void> task = new Task<Void>(){
				@Override
                protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							LocalDate date = paperDate.getValue();
							DateTimeFormatter f = DateTimeFormatter.ofPattern("YYYY-MM-dd");
							PaperPurchase paper = new PaperPurchase(paperTCompany.getSelectionModel().getSelectedItem().toString(), date.format(f), 
									Long.parseLong(paperChallan.getText()), paperType.getSelectionModel().getSelectedItem().toString(),
									paperMill.getSelectionModel().getSelectedItem().toString(), 
									Double.parseDouble(paperSize.getSelectionModel().getSelectedItem().toString()), 
									Double.parseDouble(paperWeight.getText()), Double.parseDouble(paperRate.getText()), 
									Double.parseDouble(paperTCost.getText()));
							paper.entryPurchase();
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
	
	public void onClickPConsume(){
		if(paperCTCompany.getSelectionModel().getSelectedItem() == null || paperCDate.getValue() == null || paperCChallan.getText().isEmpty() ||
				paperCType.getSelectionModel().getSelectedItem() == null || paperCMill.getSelectionModel().getSelectedItem() == null || 
				paperCSize.getSelectionModel().getSelectedItem() == null || paperCWeight.getText().isEmpty()){
			DialogueBox.warning("Fill the required field!");
		}
		else if(!(Pattern.matches("[\\d]+", paperCChallan.getText())) || !(Pattern.matches("[\\d\\.]+", paperCWeight.getText()))){
			DialogueBox.warning("Invalid input detected!");
		}
		else{
			Task<Void> task = new Task<Void>(){
				@Override
                protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							LocalDate date = paperCDate.getValue();
							DateTimeFormatter f = DateTimeFormatter.ofPattern("YYYY-MM-dd");
							PaperConsumption paper = new PaperConsumption(paperCTCompany.getSelectionModel().getSelectedItem().toString(), date.format(f), 
									Long.parseLong(paperCChallan.getText()), paperCType.getSelectionModel().getSelectedItem().toString(),
									paperCMill.getSelectionModel().getSelectedItem().toString(), 
									Double.parseDouble(paperCSize.getSelectionModel().getSelectedItem().toString()), 
									Double.parseDouble(paperCWeight.getText()));
							paper.entryConsume();
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
	
	public void initPaperCombo(ComboBox<String> paperTCompany, ComboBox<String> paperType, 
			ComboBox<String> paperMill, ComboBox<Double> paperSize){
		Task<Void> task = new Task<Void>(){
			@Override
            protected Void call() throws Exception {
				Platform.runLater(()->{
					Database data = null;
					try{
						ObservableList<String> company = FXCollections.observableArrayList();
						ObservableList<String> type = FXCollections.observableArrayList();
						ObservableList<String> mill = FXCollections.observableArrayList();
						ObservableList<Double> size = FXCollections.observableArrayList();
						data = new Database();
						ResultSet rs = data.query("Select distinct trading_company from paper_property");
						while(rs.next()){
							company.add(rs.getString(1));
						}
						if(company.isEmpty()){
							company.add("None");
						}
						paperTCompany.setItems(company);
						paperTCompany.setOnAction((event)->{
							if(paperTCompany.getSelectionModel().getSelectedItem().toString().contains("None")){
								type.removeAll(type);
								type.add("None");
							}
							else{
								type.removeAll(type);
								Database dat = new Database();
								ResultSet rs1 = dat.query("select distinct type from paper_property where trading_company = ?",
										paperTCompany.getSelectionModel().getSelectedItem().toString());
								try {
									while(rs1.next()){
										type.add(rs1.getString(1));
									}
									if(type.isEmpty()){
										type.removeAll(type);
										type.add("None");
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									DialogueBox.error(e);
								} finally{
									dat.close();
									try {
										rs1.close();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										DialogueBox.error(e);
									}
								}
							}
							paperType.setItems(type);
							try {
								rs.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								DialogueBox.error(e);
							}
						});
						paperType.setOnAction((event) -> {
							if(paperType.getSelectionModel().getSelectedItem().toString().contains("None")){
								mill.removeAll(mill);
								mill.add("None");
							}
							else{
								mill.removeAll(mill);
								Database dat = new Database();
								ResultSet rs1 = dat.query("select distinct mill from paper_property where trading_company = ? and type = ?",
										paperTCompany.getSelectionModel().getSelectedItem().toString(), 
										paperType.getSelectionModel().getSelectedItem().toString());
								try {
									while(rs1.next()){
										mill.add(rs1.getString(1));
									}
									if(mill.isEmpty()){
										mill.removeAll(mill);
										mill.add("None");
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									DialogueBox.error(e);
								} finally{
									dat.close();
									try {
										rs1.close();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										DialogueBox.error(e);
									}
								}
							}
							paperMill.setItems(mill);
							try {
								rs.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								DialogueBox.error(e);
							}
						});
						paperMill.setOnAction((event) -> {
							if(paperMill.getSelectionModel().getSelectedItem().toString().contains("None")){
								size.removeAll(size);
								size.add(0.0);
							}
							else{
								size.removeAll(size);
								Database dat = new Database();
								ResultSet rs1 = dat.query("select distinct size from paper_property where trading_company = ? and type = ? and mill = ?",
										paperTCompany.getSelectionModel().getSelectedItem().toString(), 
										paperType.getSelectionModel().getSelectedItem().toString(),
										paperMill.getSelectionModel().getSelectedItem().toString());
								try {
									while(rs1.next()){
										size.add(rs1.getDouble(1));
									}
									if(size.isEmpty()){
										size.removeAll(size);
										size.add(0.0);
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									DialogueBox.error(e);
								} finally{
									dat.close();
									try {
										rs1.close();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										DialogueBox.error(e);
									}
								}
							}
							paperSize.setItems(size);
							try {
								rs.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								DialogueBox.error(e);
							}
						});
						paperWeight.setOnAction((event) -> {
							if(paperTCompany.getSelectionModel().getSelectedItem() == null || 
									paperType.getSelectionModel().getSelectedItem() == null ||
									paperMill.getSelectionModel().getSelectedItem() == null ||
									paperSize.getSelectionModel().getSelectedItem() == null ||
									!(Pattern.matches("[\\d\\.]+", paperWeight.getText()))){
								paperRate.setText("0.0");
							}
							else{
								Database db = new Database();
								ResultSet rs2 = db.query("select rate from paper_property where trading_company = ? and type = ? "
										+ "and mill = ? and size = ?", paperTCompany.getSelectionModel().getSelectedItem().toString(), 
										paperType.getSelectionModel().getSelectedItem().toString(), 
										paperMill.getSelectionModel().getSelectedItem().toString(),
										paperSize.getSelectionModel().getSelectedItem().toString());
								double rate = 0.0;
								try {
									while(rs2.next()){
										rate = rs2.getDouble(1);
										System.out.println(1);
									}
									paperRate.setText(String.valueOf(rate*Double.parseDouble(paperWeight.getText())));
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									DialogueBox.error(e);
								} finally{
									db.close();
									try {
										rs2.close();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										DialogueBox.error(e);
									}
								}
							}
						});
					}catch(Exception e){
						DialogueBox.error(e);
					}
					data.close();
				});
				return null;
			}
		};
		Thread t = new Thread(task);
		t.start();
	}

	public void onClickPPurchaseRecords() throws IOException{
		if(paperPCount < 1){
			secondary3 = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PaperPurchase.fxml"));
			Parent root = (Parent)loader.load();
			PaperPurController controller = (PaperPurController)loader.getController();
			Scene s = new Scene(root,1077,560);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary3.setScene(s);
			secondary3.resizableProperty().set(false);
			PaperPurController.share(secondary3, s);
			secondary3.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override 
			    public void handle(WindowEvent t) {
			    	secondary3.close();
			    	paperPCount = 0;
			    }
			});
			controller.onClickRefresh();
			secondary3.show();
			paperPCount++;
		}
		else{
			if(secondary3 != null){
				secondary3.requestFocus();
			}
		}
	}
	
	public void onClickPConsumeRecords() throws IOException{
		if(paperCCount < 1){
			secondary4 = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PaperConsume.fxml"));
			Parent root = (Parent)loader.load();
			PaperConController controller = (PaperConController)loader.getController();
			Scene s = new Scene(root,879,594);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary4.setScene(s);
			secondary4.resizableProperty().set(false);
			PaperConController.share(secondary4, s);
			secondary4.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override 
			    public void handle(WindowEvent t) {
			    	secondary4.close();
			    	paperCCount = 0;
			    }
			});
			controller.onClickRefresh();
			secondary4.show();
			paperCCount++;
		}
		else{
			if(secondary4 != null){
				secondary4.requestFocus();
			}
		}
	}
	
	public void onClickPPropRecords() throws IOException{
		if(pPropCount < 1){
			secondary5 = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PaperProperty.fxml"));
			Parent root = (Parent)loader.load();
			PaperPropController controller = (PaperPropController)loader.getController();
			Scene s = new Scene(root,646,490);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary5.setScene(s);
			secondary5.resizableProperty().set(false);
			PaperPropController.share(secondary5, s);
			secondary5.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override 
			    public void handle(WindowEvent t) {
			    	secondary5.close();
			    	pPropCount = 0;
			    }
			});
			controller.onClickRefresh();
			secondary5.show();
			pPropCount++;
		}
		else{
			if(secondary5 != null){
				secondary5.requestFocus();
			}
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datePickerFormatter("dd/MM/YY", sDate);
		datePickerFormatter("dd/MM/YY", payDate);
		datePickerFormatter("dd/MM/YY", paperDate);
		datePickerFormatter("dd/MM/YY", paperCDate);
		
		toggle = new ToggleGroup();
		sInclude.setToggleGroup(toggle);
		sInclude.setUserData("Include");
		sExclude.setToggleGroup(toggle);
		sExclude.setUserData("Exclude");
		toggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		      public void changed(ObservableValue<? extends Toggle> ov,
		          Toggle old_toggle, Toggle new_toggle) {
		    	  if(toggle.getSelectedToggle().getUserData().toString().contains("Include") && sRate.getText().length() > 0){
						sVAmount.setText(String.valueOf(Double.parseDouble(sRate.getText())*0.15));
						sAAmount.setText(String.valueOf(Double.parseDouble(sRate.getText())*0.04));
					}
		    	  else if(toggle.getSelectedToggle().getUserData().toString().contains("Exclude")){
		    		  sVAmount.setText("0.0");
		    		  sAAmount.setText("0.0");
		    	  }
		      }
		    });
		
		this.initSalesCombo();
		this.initPayCombo();
		this.initPaperCombo(paperTCompany, paperType, paperMill, paperSize);
		this.initPaperCombo(paperCTCompany, paperCType, paperCMill, paperCSize);
		
		payChallan.setOnAction((event) -> {
			if(payChallan.getText().length() > 0){
				this.initPayTextField();
			}
		});
		
		this.paperRate.setEditable(false);
	}
	
}
