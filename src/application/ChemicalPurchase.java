package application;

import java.net.URL;
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

}
