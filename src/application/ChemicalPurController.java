package application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Created by hshakilst on 3/19/2017.
 */
public class ChemicalPurController implements Initializable {
    @FXML
    private TableView<ChemicalPurchase> tblPurchase;
    @FXML
    private TableColumn<ChemicalPurchase, Number> colPid;
    @FXML
    private TableColumn<ChemicalPurchase, String> colDate;
    @FXML
    private TableColumn<ChemicalPurchase, String> colTCom;
    @FXML
    private TableColumn<ChemicalPurchase, Number> colChallan;
    @FXML
    private TableColumn<ChemicalPurchase, String> colProfile;
    @FXML
    private TableColumn<ChemicalPurchase, String> colMfgCom;
    @FXML
    private TableColumn<ChemicalPurchase, Number> colWeight;
    @FXML
    private TableColumn<ChemicalPurchase, Number> colRate;
    @FXML
    private TableColumn<ChemicalPurchase, Number> colTCost;
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

    private ObservableList<ChemicalPurchase> data;
    private static Stage primaryStage;
    private static Scene scene;
    private ChemicalPurchase p;
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
        Scene s1 = new Scene(bx,1035,560);
        primaryStage.setScene(s1);
        primaryStage.show();
    }

    public void init(){
        colPid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTCom.setCellValueFactory(new PropertyValueFactory<>("tradingCompany"));
        colChallan.setCellValueFactory(new PropertyValueFactory<>("challan"));
        colProfile.setCellValueFactory(new PropertyValueFactory<>("profileName"));
        colMfgCom.setCellValueFactory(new PropertyValueFactory<>("mfgCompany"));
        colWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        colTCost.setCellValueFactory(new PropertyValueFactory<>("transportCost"));
    }

    public void loadTable(ResultSet rs) throws SQLException {
        data = FXCollections.observableArrayList();
        try {
            while(rs.next()){
                String date = rs.getString(4);
                if(date != null){
                    String arr[] = date.split("-");
                    date = arr[2]+"/"+arr[1]+"/"+arr[0].substring(2);
                }
                data.add(new ChemicalPurchase(rs.getInt(1), rs.getString(2), rs.getString(3), date,
                        rs.getString(5), rs.getLong(6), rs.getDouble(7), rs.getDouble(8),
                        rs.getDouble(9)));
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
                        ResultSet rs = db.query("Select * from chemical_purchase");
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
                            p.deletePurchase(p.getId());
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
                        colProfile.setCellFactory(TextFieldTableCell.forTableColumn());
                        colMfgCom.setCellFactory(TextFieldTableCell.forTableColumn());
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
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Warning!");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Invalid input detected!");
                                    alert.showAndWait();
                                    return Long.parseLong(toString());
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
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
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
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Warning!");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Invalid input detected!");
                                    alert.showAndWait();
                                    return Double.parseDouble(toString());
                                }
                            }
                        }));
                        colTCost.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(){
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
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Warning!");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Invalid input detected!");
                                    alert.showAndWait();
                                    return Double.parseDouble(toString());
                                }
                            }
                        }));
                        colDate.setOnEditCommit(
                                new EventHandler<TableColumn.CellEditEvent<ChemicalPurchase, String>>() {
                                    @Override
                                    public void handle(TableColumn.CellEditEvent<ChemicalPurchase, String> tab) {
                                        if(tab.getNewValue().isEmpty() ||
                                                !(Pattern.matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", tab.getNewValue()))){
                                            DialogueBox.warning("Invalid! Date should be \"YYYY-MM-DD\" format!");
                                        }
                                        else{
                                            (tab.getTableView().getItems().get(
                                                    tab.getTablePosition().getRow())
                                            ).editPurchase("date",tab.getNewValue(),
                                                    tab.getTableView().getSelectionModel().getSelectedItem().getId());
                                        }
                                    }
                                }
                        );
                        colTCom.setOnEditCommit(
                                new EventHandler<TableColumn.CellEditEvent<ChemicalPurchase, String>>() {
                                    @Override
                                    public void handle(TableColumn.CellEditEvent<ChemicalPurchase, String> tab) {
                                        if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
                                            DialogueBox.warning("Invalid input detected!");
                                        }
                                        else{
                                            (tab.getTableView().getItems().get(
                                                    tab.getTablePosition().getRow())
                                            ).editPurchase("trading_company",tab.getNewValue(),
                                                    tab.getTableView().getSelectionModel().getSelectedItem().getId());
                                        }
                                    }
                                }
                        );
                        colProfile.setOnEditCommit(
                                new EventHandler<TableColumn.CellEditEvent<ChemicalPurchase, String>>() {
                                    @Override
                                    public void handle(TableColumn.CellEditEvent<ChemicalPurchase, String> tab) {
                                        if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
                                            DialogueBox.warning("Invalid input detected!");
                                        }
                                        else{
                                            (tab.getTableView().getItems().get(
                                                    tab.getTablePosition().getRow())
                                            ).editPurchase("profile_name",tab.getNewValue(),
                                                    tab.getTableView().getSelectionModel().getSelectedItem().getId());
                                        }
                                    }
                                }
                        );
                        colMfgCom.setOnEditCommit(
                                new EventHandler<TableColumn.CellEditEvent<ChemicalPurchase, String>>() {
                                    @Override
                                    public void handle(TableColumn.CellEditEvent<ChemicalPurchase, String> tab) {
                                        if(tab.getNewValue().isEmpty() || !(Pattern.matches("[\\w\\.\\,\\s\\(\\)\\/\\-]+", tab.getNewValue()))){
                                            DialogueBox.warning("Invalid input detected!");
                                        }
                                        else{
                                            (tab.getTableView().getItems().get(
                                                    tab.getTablePosition().getRow())
                                            ).editPurchase("mfg_company",tab.getNewValue(),
                                                    tab.getTableView().getSelectionModel().getSelectedItem().getId());
                                        }
                                    }
                                }
                        );
                        colChallan.setOnEditCommit(
                                new EventHandler<TableColumn.CellEditEvent<ChemicalPurchase, Number>>() {
                                    @Override
                                    public void handle(TableColumn.CellEditEvent<ChemicalPurchase, Number> tab) {
                                        if(String.valueOf(tab.getNewValue()).isEmpty() ||
                                                !(Pattern.matches("[\\d]+", String.valueOf(tab.getNewValue())))){
                                            DialogueBox.warning("Invalid input detected!");
                                        }
                                        else{
                                            (tab.getTableView().getItems().get(
                                                    tab.getTablePosition().getRow())
                                            ).editPurchase("challan_no",tab.getNewValue(),
                                                    tab.getTableView().getSelectionModel().getSelectedItem().getId());
                                        }
                                    }
                                }
                        );
                        colWeight.setOnEditCommit(
                                new EventHandler<TableColumn.CellEditEvent<ChemicalPurchase, Number>>() {
                                    @Override
                                    public void handle(TableColumn.CellEditEvent<ChemicalPurchase, Number> tab) {
                                        if(String.valueOf(tab.getNewValue()).isEmpty() ||
                                                !(Pattern.matches("[\\d\\.]+", String.valueOf(tab.getNewValue())))){
                                            DialogueBox.warning("Invalid input detected!");
                                        }
                                        else{
                                            (tab.getTableView().getItems().get(
                                                    tab.getTablePosition().getRow())
                                            ).editPurchase("weight",tab.getNewValue(),
                                                    tab.getTableView().getSelectionModel().getSelectedItem().getId());
                                        }
                                    }
                                }
                        );
                        colRate.setOnEditCommit(
                                new EventHandler<TableColumn.CellEditEvent<ChemicalPurchase, Number>>() {
                                    @Override
                                    public void handle(TableColumn.CellEditEvent<ChemicalPurchase, Number> tab) {
                                        if(String.valueOf(tab.getNewValue()).isEmpty() ||
                                                !(Pattern.matches("[\\d.]+", String.valueOf(tab.getNewValue())))){
                                            DialogueBox.warning("Invalid input detected!");
                                        }
                                        else{
                                            (tab.getTableView().getItems().get(
                                                    tab.getTablePosition().getRow())
                                            ).editPurchase("rate",tab.getNewValue(),
                                                    tab.getTableView().getSelectionModel().getSelectedItem().getId());
                                        }
                                    }
                                }
                        );
                        colTCost.setOnEditCommit(
                                new EventHandler<TableColumn.CellEditEvent<ChemicalPurchase, Number>>() {
                                    @Override
                                    public void handle(TableColumn.CellEditEvent<ChemicalPurchase, Number> tab) {
                                        if(String.valueOf(tab.getNewValue()).isEmpty() ||
                                                !(Pattern.matches("[\\d.]+", String.valueOf(tab.getNewValue())))){
                                            DialogueBox.warning("Invalid input detected!");
                                        }
                                        else{
                                            (tab.getTableView().getItems().get(
                                                    tab.getTablePosition().getRow())
                                            ).editPurchase("transport_cost",tab.getNewValue(),
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
        if(!(txSearch.getText().isEmpty())){
            Task<Void> task = new Task<Void>(){
                @Override
                protected Void call() throws Exception {
                    Platform.runLater(()->{
                        try{
                            Database data = new Database();
                            ResultSet rs = null;
                            if(dpStart.getValue() == null && dpEnd.getValue() == null){
                                rs = data.query("Select * from chemical_purchase where (profile_name like ? OR " +
                                                "trading_company like ? OR challan_no like ? or mfg_company like ?)",
                                        "%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%"
                                        , "%"+txSearch.getText()+"%");
                            }
                            else if(dpStart.getValue() == null || dpEnd.getValue() == null){
                                String date;
                                if(dpStart.getValue() == null){
                                    date = dpEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
                                }
                                else{
                                    date = dpStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
                                }
                                rs = data.query("Select * from chemical_purchase where (profile_name like ? OR " +
                                                "trading_company like ? OR challan_no like ? or mfg_company like ?)"
                                                + "and date like ?", "%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%",
                                        "%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%", "%"+date+"%");
                            }
                            else{
                                String sDate;
                                String eDate;
                                sDate = dpStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
                                eDate = dpEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
                                rs = data.query("Select * from chemical_purchase where date between ? and ? and "
                                                + "(profile_name like ? OR trading_company like ? OR challan_no like ? or mfg_company like ?)",
                                        sDate, eDate, "%"+txSearch.getText()+"%", "%"+txSearch.getText()+"%","%"+txSearch.getText()+"%",
                                        "%"+txSearch.getText()+"%");

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
                                rs = data.query("Select * from chemical_purchase where date like ?", "%"+date+"%");
                            }
                            else{
                                String sDate;
                                String eDate;
                                sDate = dpStart.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
                                eDate = dpEnd.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
                                rs = data.query("Select * from chemical_purchase where date between ? and ? ", sDate, eDate);

                            }
                            if(rs != null){
                                loadTable(rs);
                                rs.close();
                            }
                            data.close();

                            dpStart.setValue(null);
                            dpEnd.setValue(null);
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
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public String addTotal(ObservableList<ChemicalPurchase> data){
        double total = 0;
        for(ChemicalPurchase d : data){
            total += (d.getRate()+d.getTransportCost());
        }
        return String.valueOf(total);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePickerFormatter("dd/MM/YY", dpStart);
        datePickerFormatter("dd/MM/YY", dpEnd);
        tblPurchase.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblPurchase.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends ChemicalPurchase> change) -> {
            ObservableList<ChemicalPurchase> selectedItems = tblPurchase.getSelectionModel().getSelectedItems();
            txTotal.setText(addTotal(selectedItems));
        });
    }
}
