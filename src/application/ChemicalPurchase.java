package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class ChemicalPurchase implements Initializable{

	private int id;
	private String tradingCompany;
	private String date;
	private String mfgCompany;
	private long challan;
	private int noOfDrums;
	private double weight;
	private double rate;
	private double transportCost;
	
	public ChemicalPurchase(int id, String tradingCompany, String date, String mfgCompany, long challan, int noOfDrums,
			double weight, double rate, double transportCost) {
		super();
		this.id = id;
		this.tradingCompany = tradingCompany;
		this.date = date;
		this.mfgCompany = mfgCompany;
		this.challan = challan;
		this.noOfDrums = noOfDrums;
		this.weight = weight;
		this.rate = rate;
		this.transportCost = transportCost;
	}
	
	public ChemicalPurchase(String tradingCompany, String date, String mfgCompany, long challan, int noOfDrums,
			double weight, double rate, double transportCost) {
		super();
		this.tradingCompany = tradingCompany;
		this.date = date;
		this.mfgCompany = mfgCompany;
		this.challan = challan;
		this.noOfDrums = noOfDrums;
		this.weight = weight;
		this.rate = rate;
		this.transportCost = transportCost;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTradingCompany() {
		return tradingCompany;
	}

	public void setTradingCompany(String tradingCompany) {
		this.tradingCompany = tradingCompany;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMfgCompany() {
		return mfgCompany;
	}

	public void setMfgCompany(String mfgCompany) {
		this.mfgCompany = mfgCompany;
	}

	public long getChallan() {
		return challan;
	}

	public void setChallan(long challan) {
		this.challan = challan;
	}

	public int getNoOfDrums() {
		return noOfDrums;
	}

	public void setNoOfDrums(int noOfDrums) {
		this.noOfDrums = noOfDrums;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(double transportCost) {
		this.transportCost = transportCost;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void entryPurchase() throws SQLException{
		Database data = new Database();
		ResultSet rs = data.query("select exists (select 1 from chemical_property where trading_company = ? and mfg_company = ?)",
				this.getTradingCompany(), this.getMfgCompany());
		rs.next();
		if(rs.getInt(1) == 1){
			data.update("insert into chemical_purchase (trading_company, date, mfg_company, challan_no, weight, rate, transport_cost) "
					+ "values (?, ?, ?, ?, ?, ?, ?)", this.getTradingCompany(), this.getDate(), this.getMfgCompany(), this.getChallan(), 
					this.getWeight(), this.getRate(), this.getTransportCost());
			rs = data.query("select weight from chemical_property where trading_company = ? and mfg_company = ?", 
					this.getTradingCompany(), this.getMfgCompany());
			rs.next();
			double currentWeight = rs.getDouble(1);
			data.update("update chemical_property set weight = ? where trading_company = ? and mfg_company = ?", 
					(this.getWeight()+currentWeight), this.getTradingCompany(), this.getMfgCompany());
		}
		else{
			DialogueBox.warning("Create chemical property first!");
		}
		data.close();
		rs.close();
	}
	
	public void editPurchase(String col, Object val, int id){
		Database data = new Database();
		data.update("Update chemical_purchase Set "+col+" = ? where id = ?" , val, id);
		data.close();
	}

	public void deletePurchase(int id){
		 Database data = new Database();
		 data.update("Delete from chemical_purchase sales where id = ?",id);
		 data.close();
	}

}
