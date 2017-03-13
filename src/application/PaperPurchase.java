package application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaperPurchase {
	private int id;
	private String tradingCompany;
	private String date;
	private long challan;
	private String type;
	private String mill;
	private double size;
	private double weight;
	private double rate;
	private double transportCost;
	
	public PaperPurchase(String tradingCompany, String date, long challan, String type, String mill, double size, double weight, 
			double rate, double transportCost) {
		super();
		this.tradingCompany = tradingCompany;
		this.date = date;
		this.challan = challan;
		this.type = type;
		this.mill = mill;
		this.size = size;
		this.weight = weight;
		this.rate = rate;
		this.transportCost = transportCost;
	}
	
	public PaperPurchase(int id, String tradingCompany, String date, long challan, String type, String mill, double size, double weight,
			double rate, double transportCost) {
		super();
		this.id = id;
		this.tradingCompany = tradingCompany;
		this.date = date;
		this.challan = challan;
		this.type = type;
		this.mill = mill;
		this.size = size;
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

	public long getChallan() {
		return challan;
	}

	public void setChallan(long challan) {
		this.challan = challan;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMill() {
		return mill;
	}

	public void setMill(String mill) {
		this.mill = mill;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
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
	
	public void entryPurchase() throws SQLException{
		Database data = new Database();
		ResultSet rs = data.query("select exists (select 1 from paper_property where trading_company = ? and type = ? and mill = ? and size = ?)",
				this.getTradingCompany(), this.getType(), this.getMill(), this.getSize());
		rs.next();
		if(rs.getInt(1) == 1){
			data.update("insert into paper_purchase (trading_company, date, challan_no, type, mill, size, weight, rate, transport_cost) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)", this.getTradingCompany(), this.getDate(), this.getChallan(), this.getType(), this.getMill(), 
					this.getSize(), this.getWeight(), this.getRate(), this.getTransportCost());
			rs = data.query("select weight from paper_property where trading_company = ? and type = ? and mill = ? and size = ?", 
					this.getTradingCompany(), this.getType(), this.getMill(), this.getSize());
			rs.next();
			double currentWeight = rs.getDouble(1);
			data.update("update paper_property set weight = ? where trading_company = ? and type = ? and mill = ? and size = ?", 
					(this.getWeight()+currentWeight), this.getTradingCompany(), this.getType(), this.getMill(), this.getSize());
		}
		else{
			DialogueBox.warning("Create paper property first!");
		}
		data.close();
		rs.close();
	}
	
	public void editPurchase(String col, Object val, int id){
		Database data = new Database();
		data.update("Update paper_purchase Set "+col+" = ? where id = ?" , val, id);
		data.close();
	}

	public void deletePurchase(int id){
		 Database data = new Database();
		 data.update("Delete from paper_purchase sales where id = ?",id);
		 data.close();
	}
}
