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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
//	@FXML
//	private Button pCreate;
//	@FXML
//	private Button pRecords;
	
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
//	@FXML
//	private Button sSubmit;
//	@FXML
//	private Button sRecords;
	
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
//	@FXML
//	private Button paySubmit;
//	@FXML
//	private Button payRecords;
	
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
//	@FXML
//	private Button paperSubmit;
//	@FXML
//	private Button paperPRecords;
	
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
//	@FXML
//	private Button paperCSubmit;
//	@FXML
//	private Button paperCRecords;
	
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
//	@FXML
//	private Button btCreatePaper;
//	@FXML
//	private Button btPProperty;

	//Chemical variables
	@FXML
	private TextField txCreateCProfile;
	@FXML
	private TextField txCreateCTCom;
	@FXML
	private TextField txCreateCMCom;
	@FXML
	private TextField txCreateCRate;
//	@FXML
//	private Button btChemCreate;
//	@FXML
//	private Button chemPropView;

	@FXML
	private ComboBox<String> chemPProfile;
	@FXML
	private TextField chemPTCom;
	@FXML
	private DatePicker chemPDate;
	@FXML
	private TextField chemPChallan;
	@FXML
	private TextField chemPMfgCom;
	@FXML
	private TextField chemPWeight;
	@FXML
	private TextField chemPCost;
	@FXML
	private TextField chemPTCost;

    @FXML
    private ComboBox<String> chemCProfile;
	@FXML
    private TextField chemCTCom;
    @FXML
    private DatePicker chemCDate;
    @FXML
    private TextField chemCChallan;
    @FXML
    private TextField chemCMfgCom;
    @FXML
    private TextField chemCWeight;

	private static Stage primaryStage;
	private static Scene scene;
	private static int pCount = 0;
	private static int sCount = 0;
	private static int payCount = 0;
	private static int paperPCount = 0;
	private static int paperCCount = 0;
	private static int paperPropCount = 0;
	private static int chemPropCount = 0;
	private static int chemPCount = 0;
	private static int chemCCount = 0;
	private static Stage secondary;
	private static Stage secondary1;
	private static Stage secondary2;
	private static Stage secondary3;
	private static Stage secondary4;
	private static Stage secondary5;
	private static Stage secondary6;
	private static Stage secondary7;
	private static Stage secondary8;
	private ToggleGroup toggle;
	
	static void share(Stage p, Scene s){
		primaryStage = p;
		scene = s;
		
	}
	
	private void datePickerFormatter(DatePicker date){
		Task<Void> task = new Task<Void>(){
			@Override
            protected Void call() throws Exception {
				Platform.runLater(()->{
					try{
						date.setConverter(new StringConverter<LocalDate>() {
						     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YY");

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
	
	private void loading(){
		final VBox bx = new VBox();
	    bx.setAlignment(Pos.CENTER);
	    ProgressIndicator pi = new ProgressIndicator();
	    Label label = new Label("Loading...");
	    bx.getChildren().addAll(pi,label);
	    Scene s1 = new Scene(bx,1263,611);
	    primaryStage.setScene(s1);
	    primaryStage.show();
	}
	
	public void onClickPCreate(){
		if(pComName.getText().isEmpty() || pItemName.getText().isEmpty() || pUPin.getText().isEmpty() ||
				pUPrint.getText().isEmpty() || pUMedia.getText().isEmpty() || pULiner.getText().isEmpty() ||
				pRate.getText().isEmpty() ){
			DialogueBox.warning("Please fill all the required fields!");
		}
		else if(!(Pattern.matches("[\\w.,\\s()/]+", pComName.getText())) ||
				!(Pattern.matches("[\\w.,\\s()/]+", pItemName.getText())) ||
				!(Pattern.matches("[\\d.]+", pUPin.getText())) ||
                !(Pattern.matches("[\\d.]+", pUPrint.getText())) ||
				!(Pattern.matches("[\\d.]+", pUMedia.getText())) ||
                !(Pattern.matches("[\\d.]+", pULiner.getText())) ||
				!(Pattern.matches("[\\d.]+", pRate.getText()))){
			DialogueBox.warning("Invalid input detected!");
		}
		else{
			Task<Void> task = new Task<Void>(){
				@Override
                protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							Products product = new Products(
							        pComName.getText(), pItemName.getText(), Double.parseDouble(pUPin.getText()),
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
	
	public void onClickPRecords() throws IOException{
		if(pCount < 1){
			secondary = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Products.fxml"));
			Parent root = loader.load();
			ProductsController controller = loader.getController();
			Scene s = new Scene(root,893,490);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary.setScene(s);
			secondary.resizableProperty().set(false);
			ProductsController.share(secondary, s);
			secondary.setOnCloseRequest(t -> {
                secondary.close();
                pCount = 0;
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
				!(Pattern.matches("[\\d]+", sQty.getText())) || !(Pattern.matches("[\\d.]+", sRate.getText()))){
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
							Sales sales = new Sales(date.format(f), Long.parseLong(sChallan.getText()),
                                    Long.parseLong(sPo.getText()),
									Long.parseLong(sVat.getText()), Long.parseLong(sBill.getText()),
                                    sCompany.getSelectionModel().getSelectedItem(),
                                    sItem.getSelectionModel().getSelectedItem(), Integer.parseInt(sQty.getText()),
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
	
	private void initSalesCombo(){
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
							if(sCompany.getSelectionModel().getSelectedItem().contains("None")){
								item.removeAll();
								item.add("None");
							}
							else{
								item.removeAll();
								Database dat = new Database();
								ResultSet rs1 = dat.query("select distinct item_name from products where company_name = ?",
                                        sCompany.getSelectionModel().getSelectedItem());
								try {
									while(rs1.next()){
										item.add(rs1.getString(1));
									}
									if(item.isEmpty()){
										item.removeAll();
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
                                        sCompany.getSelectionModel().getSelectedItem(),
                                        sItem.getSelectionModel().getSelectedItem());
								double rate = 0.0;
								try {
									while(rs2.next()){
										rate = rs2.getDouble(1);
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
                    if (data != null) {
                        data.close();
                    }
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
			Parent root = loader.load();
			SalesController controller = loader.getController();
			Scene s = new Scene(root,1077,560);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary1.setScene(s);
			secondary1.resizableProperty().set(false);
			SalesController.share(secondary1, s);
			secondary1.setOnCloseRequest(t -> {
                secondary1.close();
                sCount = 0;
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
	
	private void initPayCombo(){
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
	
	private void initPayTextField(){
		Task<Void> task = new Task<Void>(){
			@Override
            protected Void call() throws Exception {
				Platform.runLater(()->{
					try{
						Database data = new Database();
						ResultSet rs = data.query("Select bill_no, po_no from sales where company = ? and challan_no = ?",
                                payCompany.getSelectionModel().getSelectedItem(), Long.parseLong(payChallan.getText()));
						while(rs.next()){
							payBill.setText(String.valueOf(rs.getLong(1)));
							payPo.setText(String.valueOf(rs.getLong(2)));
						}
                        rs.close();
                        data.close();
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
				!(Pattern.matches("[\\d]+", payPo.getText())) || !(Pattern.matches("[\\d.]+", payAmount.getText())) ||
				!(Pattern.matches("[\\w.,\\s()/\\-]+", payCheque.getText()))){
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
							Payment pay = new Payment(
							        date.format(f), payCompany.getSelectionModel().getSelectedItem(),
									Long.parseLong(payChallan.getText()),
                                    Long.parseLong(payBill.getText()), Long.parseLong(payPo.getText()),
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
			Parent root = loader.load();
			PaymentController controller = loader.getController();
			Scene s = new Scene(root,893,526);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary2.setScene(s);
			secondary2.resizableProperty().set(false);
			PaymentController.share(secondary2, s);
			secondary2.setOnCloseRequest(t -> {
                secondary2.close();
                payCount = 0;
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
		else if(!(Pattern.matches("[\\w.,\\s()/\\-]+", txCreateTCompany.getText())) ||
				!(Pattern.matches("[\\w.,\\s()/\\-]+", txCreatePType.getText())) ||
				!(Pattern.matches("[\\w.,\\s()/\\-]+", txCreatePMill.getText())) ||
				!(Pattern.matches("[\\d.]+", txCreatePSize.getText())) ||
				!(Pattern.matches("[\\d.]+", txCreatePRate.getText()))){
			DialogueBox.warning("Invalid input detected!");
		}
		else{
			Task<Void> task = new Task<Void>(){
				@Override
                protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							PaperProperty prop = new PaperProperty(
							        txCreateTCompany.getText(), txCreatePType.getText(), txCreatePMill.getText(),
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
		if(paperTCompany.getSelectionModel().getSelectedItem() == null ||
                paperDate.getValue() == null || paperChallan.getText().isEmpty() ||
				paperType.getSelectionModel().getSelectedItem() == null ||
                paperMill.getSelectionModel().getSelectedItem() == null ||
				paperSize.getSelectionModel().getSelectedItem() == null ||
                paperWeight.getText().isEmpty() || paperRate.getText().isEmpty() ||
				paperTCost.getText().isEmpty()){
			DialogueBox.warning("Fill the required field!");
		}
		else if(!(Pattern.matches("[\\d]+", paperChallan.getText())) ||
                !(Pattern.matches("[\\d.]+", paperWeight.getText())) ||
				!(Pattern.matches("[\\d.]+", paperRate.getText())) ||
                !(Pattern.matches("[\\d.]+", paperTCost.getText()))){
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
							PaperPurchase paper = new PaperPurchase(
                                    paperTCompany.getSelectionModel().getSelectedItem(), date.format(f),
									Long.parseLong(paperChallan.getText()),
                                    paperType.getSelectionModel().getSelectedItem(),
                                    paperMill.getSelectionModel().getSelectedItem(),
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
		if(paperCTCompany.getSelectionModel().getSelectedItem() == null || paperCDate.getValue() == null ||
                paperCChallan.getText().isEmpty() || paperCType.getSelectionModel().getSelectedItem() == null ||
                paperCMill.getSelectionModel().getSelectedItem() == null ||
                paperCSize.getSelectionModel().getSelectedItem() == null ||
                paperCWeight.getText().isEmpty()){
			DialogueBox.warning("Fill the required field!");
		}
		else if(!(Pattern.matches("[\\d]+", paperCChallan.getText())) ||
                !(Pattern.matches("[\\d.]+", paperCWeight.getText()))){
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
							PaperConsumption paper = new PaperConsumption(
                                    paperCTCompany.getSelectionModel().getSelectedItem(),
                                    date.format(f),
									Long.parseLong(paperCChallan.getText()),
                                    paperCType.getSelectionModel().getSelectedItem(),
                                    paperCMill.getSelectionModel().getSelectedItem(),
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
	
	private void initPaperCombo(ComboBox<String> paperTCompany, ComboBox<String> paperType,
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
							if(paperTCompany.getSelectionModel().getSelectedItem().contains("None")){
								type.removeAll();
								type.add("None");
							}
							else{
								type.removeAll();
								Database dat = new Database();
								ResultSet rs1 = dat.query("select distinct type from paper_property where trading_company = ?",
                                        paperTCompany.getSelectionModel().getSelectedItem());
								try {
									while(rs1.next()){
										type.add(rs1.getString(1));
									}
									if(type.isEmpty()){
										type.removeAll();
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
							if(paperType.getSelectionModel().getSelectedItem().contains("None")){
								mill.removeAll();
								mill.add("None");
							}
							else{
								mill.removeAll();
								Database dat = new Database();
								ResultSet rs1 = dat.query("select distinct mill from paper_property where " +
                                                "trading_company = ? and type = ?",
                                        paperTCompany.getSelectionModel().getSelectedItem(),
                                        paperType.getSelectionModel().getSelectedItem());
								try {
									while(rs1.next()){
										mill.add(rs1.getString(1));
									}
									if(mill.isEmpty()){
										mill.removeAll();
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
							if(paperMill.getSelectionModel().getSelectedItem().contains("None")){
								size.removeAll();
								size.add(0.0);
							}
							else{
								size.removeAll();
								Database dat = new Database();
								ResultSet rs1 = dat.query("select distinct size from paper_property where " +
                                                "trading_company = ? and type = ? and mill = ?",
                                        paperTCompany.getSelectionModel().getSelectedItem(),
                                        paperType.getSelectionModel().getSelectedItem(),
                                        paperMill.getSelectionModel().getSelectedItem());
								try {
									while(rs1.next()){
										size.add(rs1.getDouble(1));
									}
									if(size.isEmpty()){
										size.removeAll();
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
									!(Pattern.matches("[\\d.]+", paperWeight.getText()))){
								paperRate.setText("0");
							}
							else{
								Database db = new Database();
								ResultSet rs2 = db.query("select rate from paper_property where " +
                                                "trading_company = ? and type = ? "
										+ "and mill = ? and size = ?",
                                        paperTCompany.getSelectionModel().getSelectedItem(),
                                        paperType.getSelectionModel().getSelectedItem(),
                                        paperMill.getSelectionModel().getSelectedItem(),
										paperSize.getSelectionModel().getSelectedItem().toString());
								double rate = 0.0;
								try {
									while(rs2.next()){
										rate = rs2.getDouble(1);
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
                    if (data != null) {
                        data.close();
                    }
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
			Parent root = loader.load();
			PaperPurController controller = loader.getController();
			Scene s = new Scene(root,1077,560);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary3.setScene(s);
			secondary3.resizableProperty().set(false);
			PaperPurController.share(secondary3, s);
			secondary3.setOnCloseRequest(t -> {
                secondary3.close();
                paperPCount = 0;
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
			Parent root = loader.load();
			PaperConController controller = loader.getController();
			Scene s = new Scene(root,879,594);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary4.setScene(s);
			secondary4.resizableProperty().set(false);
			PaperConController.share(secondary4, s);
			secondary4.setOnCloseRequest(t -> {
                secondary4.close();
                paperCCount = 0;
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
		if(paperPropCount < 1){
			secondary5 = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PaperProperty.fxml"));
			Parent root = loader.load();
			PaperPropController controller = loader.getController();
			Scene s = new Scene(root,646,490);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary5.setScene(s);
			secondary5.resizableProperty().set(false);
			PaperPropController.share(secondary5, s);
			secondary5.setOnCloseRequest(t -> {
                secondary5.close();
                paperPropCount = 0;
            });
			controller.onClickRefresh();
			secondary5.show();
			paperPropCount++;
		}
		else{
			if(secondary5 != null){
				secondary5.requestFocus();
			}
		}
	}
	
	public void onClickChemCreate(){
		if(txCreateCProfile.getText().isEmpty() || txCreateCTCom.getText().isEmpty() || txCreateCMCom.getText().isEmpty() || txCreateCRate.getText().isEmpty()){
			DialogueBox.warning("Fill the required field!");
		}
		else if(!(Pattern.matches("[\\w.,\\s()/\\-]+", txCreateCProfile.getText())) ||
				!(Pattern.matches("[\\w.,\\s()/\\-]+", txCreateCTCom.getText())) ||
				!(Pattern.matches("[\\w.,\\s()/\\-]+", txCreateCMCom.getText())) ||
				!(Pattern.matches("[\\d.]+", txCreateCRate.getText()))){
			DialogueBox.warning("Invalid input detected!");
		}
		else{
			Task<Void> task = new Task<Void>(){
				@Override
                protected Void call() throws Exception {
					Platform.runLater(()->{
						try{
							ChemicalProperty prop = new ChemicalProperty(txCreateCProfile.getText(), txCreateCTCom.getText(),
									txCreateCMCom.getText(), Double.parseDouble(txCreateCRate.getText()));
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
        this.initChemCombo(this.chemPProfile, this.chemPTCom, this.chemPMfgCom);
        this.initChemCombo(this.chemCProfile, this.chemCTCom, this.chemCMfgCom);
	}
	
	public void onClickChemRecords() throws IOException{
		if(chemPropCount < 1){
			secondary6 = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChemicalProperty.fxml"));
			Parent root = loader.load();
			ChemicalPropController controller = loader.getController();
			Scene s = new Scene(root,612,490);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary6.setScene(s);
			secondary6.resizableProperty().set(false);
			ChemicalPropController.share(secondary6, s);
			secondary6.setOnCloseRequest(t -> {
                secondary6.close();
                chemPropCount = 0;
            });
			controller.onClickRefresh();
			secondary6.show();
			chemPropCount++;
		}
		else{
			if(secondary6 != null){
				secondary6.requestFocus();
			}
		}
	}

	public void onClickChemPurRecords() throws IOException{
		if(chemPCount < 1){
			secondary7 = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChemicalPurchase.fxml"));
			Parent root = loader.load();
			ChemicalPurController controller = loader.getController();
			Scene s = new Scene(root,1035,560);
			s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			secondary7.setScene(s);
			secondary7.resizableProperty().set(false);
			ChemicalPurController.share(secondary7, s);
			secondary7.setOnCloseRequest(t -> {
				secondary7.close();
				chemPCount = 0;
			});
			controller.onClickRefresh();
			secondary7.show();
			chemPCount++;
		}
		else{
			if(secondary7 != null){
				secondary7.requestFocus();
			}
		}
	}

	public void onClickChemConRecords() throws IOException{
        if(chemCCount < 1){
            secondary8 = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChemicalConsume.fxml"));
            Parent root = loader.load();
            ChemicalConController controller = loader.getController();
            Scene s = new Scene(root,879,594);
            s.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            secondary8.setScene(s);
            secondary8.resizableProperty().set(false);
            ChemicalConController.share(secondary8, s);
            secondary8.setOnCloseRequest(t -> {
                secondary8.close();
                chemCCount = 0;
            });
            controller.onClickRefresh();
            secondary8.show();
            chemCCount++;
        }
        else{
            if(secondary8 != null){
                secondary8.requestFocus();
            }
        }
    }

	private void initChemCombo(ComboBox<String> chemProfile, TextField chemTCom, TextField chemMfgCom){
		Task<Void> task = new Task<Void>(){
			@Override
			protected Void call() throws Exception {
				Platform.runLater(()->{
					Database data = null;
					try{
						ObservableList<String> profileList = FXCollections.observableArrayList();
						data = new Database();
						ResultSet rs = data.query("Select profile_name from chemical_property");
						while (rs.next()){
							profileList.add(rs.getString(1));
						}
						rs.close();
						if(profileList.isEmpty()){
							profileList.add("None");
						}
						chemProfile.setItems(profileList);
						chemProfile.setOnAction((event) -> {
							if(chemProfile.getSelectionModel().getSelectedItem().contains("None")){
								chemTCom.setText("None");
								chemMfgCom.setText("None");
							}
							else{
							    Database db = new Database();
								ResultSet rs1 = db.query("Select trading_company, mfg_company from chemical_property where " +
										"profile_name = ?", chemProfile.getSelectionModel().getSelectedItem());
								try {
									while (rs1.next()){
                                        chemTCom.setText(rs1.getString(1));
                                        chemMfgCom.setText(rs1.getString(2));
                                    }
								} catch (SQLException e) {
									DialogueBox.error(e);
								}
								finally {
								    db.close();
                                    try {
                                        rs1.close();
                                    } catch (SQLException e) {
                                        DialogueBox.error(e);
                                    }
                                }
                            }
							chemPWeight.setOnAction((event1) -> {
								if(chemProfile.getSelectionModel().getSelectedItem() == null ||
										!(Pattern.matches("[\\d.]+", chemPWeight.getText())) ||
										chemProfile.getSelectionModel().getSelectedItem().contains("None")){
									chemPCost.setText("0.0");
								}
								else{
								    Database db = new Database();
									ResultSet rs2 = db.query("select rate from chemical_property where " +
													"profile_name = ?",
											chemProfile.getSelectionModel().getSelectedItem());
									double rate = 0.0;
									try {
										while(rs2.next()){
											rate = rs2.getDouble(1);
										}
										chemPCost.setText(String.valueOf(rate*Double.parseDouble(chemPWeight.getText())));
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
						});
					}catch(Exception e){
						DialogueBox.error(e);
					}
                    if (data != null) {
                        data.close();
                    }
				});
				return null;
			}
		};
		Thread t = new Thread(task);
		t.start();
	}

	public void onClickChemPurchase(){
        if(chemPProfile.getSelectionModel().getSelectedItem() == null || chemPTCom.getText().isEmpty() ||
                chemPMfgCom.getText().isEmpty() || chemPDate.getValue() == null || chemPChallan.getText().isEmpty() ||
                chemPWeight.getText().isEmpty() || chemPCost.getText().isEmpty() || chemPTCost.getText().isEmpty()){
            DialogueBox.warning("Fill the required field!");
        }
        else if(!(Pattern.matches("[\\d]+", chemPChallan.getText())) ||
                !(Pattern.matches("[\\d.]+", chemPWeight.getText()) ||
                !(Pattern.matches("[\\d.]+", chemPTCost.getText())))){
            DialogueBox.warning("Invalid input detected!");
        }
        else{
            Task<Void> task = new Task<Void>(){
                @Override
                protected Void call() throws Exception {
                    Platform.runLater(()->{
                        try{
                            LocalDate date = chemPDate.getValue();
                            DateTimeFormatter f = DateTimeFormatter.ofPattern("YYYY-MM-dd");
                            ChemicalPurchase chem = new ChemicalPurchase(chemPProfile.getSelectionModel().getSelectedItem(),
                                    chemPTCom.getText(), date.format(f), chemPMfgCom.getText(),
                                    Long.parseLong(chemPChallan.getText()), Double.parseDouble(chemPWeight.getText()),
                                    Double.parseDouble(chemPCost.getText()), Double.parseDouble(chemPTCost.getText()));
                            chem.entryPurchase();
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

    public void onClickChemConsume(){
        if(chemCProfile.getSelectionModel().getSelectedItem() == null || chemCTCom.getText().isEmpty() ||
                chemCMfgCom.getText().isEmpty() || chemCDate.getValue() == null || chemCChallan.getText().isEmpty() ||
                chemCWeight.getText().isEmpty()){
            DialogueBox.warning("Fill the required field!");
        }
        else if(!(Pattern.matches("[\\d]+", chemCChallan.getText())) ||
                !(Pattern.matches("[\\d.]+", chemCWeight.getText()))){
            DialogueBox.warning("Invalid input detected!");
        }
        else{
            Task<Void> task = new Task<Void>(){
                @Override
                protected Void call() throws Exception {
                    Platform.runLater(()->{
                        try{
                            LocalDate date = chemCDate.getValue();
                            DateTimeFormatter f = DateTimeFormatter.ofPattern("YYYY-MM-dd");
                            ChemicalConsumption chem = new ChemicalConsumption(chemCProfile.getSelectionModel().getSelectedItem(),
                                    chemCTCom.getText(), date.format(f), chemCMfgCom.getText(),
                                    Long.parseLong(chemCChallan.getText()), Double.parseDouble(chemCWeight.getText()));
                            chem.entryConsume();
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
		datePickerFormatter(sDate);
		datePickerFormatter(payDate);
		datePickerFormatter(paperDate);
		datePickerFormatter(paperCDate);
		
		toggle = new ToggleGroup();
		sInclude.setToggleGroup(toggle);
		sInclude.setUserData("Include");
		sExclude.setToggleGroup(toggle);
		sExclude.setUserData("Exclude");
		toggle.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if(toggle.getSelectedToggle().getUserData().toString().contains("Include") &&
                    sRate.getText().length() > 0){
                  sVAmount.setText(String.valueOf(Double.parseDouble(sRate.getText())*0.15));
                  sAAmount.setText(String.valueOf(Double.parseDouble(sRate.getText())*0.04));
              }
            else if(toggle.getSelectedToggle().getUserData().toString().contains("Exclude")){
                sVAmount.setText("0.0");
                sAAmount.setText("0.0");
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
		this.sRate.setEditable(false);

		this.datePickerFormatter(this.chemPDate);
		this.datePickerFormatter(this.chemCDate);
		this.initChemCombo(this.chemPProfile, this.chemPTCom, this.chemPMfgCom);
		this.initChemCombo(this.chemCProfile, this.chemCTCom, this.chemCMfgCom);
	}
	
}
