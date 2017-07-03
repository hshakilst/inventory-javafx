package application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaperConsumption {
	private int id;
	private String tradingCompany;
	private String date;
	private long challan;
	private String type;
	private String mill;
	private double size;
	private double weight;
	
	public PaperConsumption(int id, String tradingCompany, String date, long challan, String type, String mill,
			double size, double weight) {
		super();
		this.id = id;
		this.tradingCompany = tradingCompany;
		this.date = date;
		this.challan = challan;
		this.type = type;
		this.mill = mill;
		this.size = size;
		this.weight = weight;
	}
	
	public PaperConsumption(String tradingCompany, String date, long challan, String type, String mill,
			double size, double weight) {
		super();
		this.tradingCompany = tradingCompany;
		this.date = date;
		this.challan = challan;
		this.type = type;
		this.mill = mill;
		this.size = size;
		this.weight = weight;
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
	
	public void entryConsume() throws SQLException{
		Database data = new Database();
		ResultSet rs = data.query("select exists (select 1 from paper_property where trading_company = ? and type = ? and mill = ? and size = ?)",
				this.getTradingCompany(), this.getType(), this.getMill(), this.getSize());
		rs.next();
		if(rs.getInt(1) == 1){
			data.update("insert into paper_consumption (trading_company, date, challan_no, type, mill, size, weight) "
					+ "values (?, ?, ?, ?, ?, ?, ?)", this.getTradingCompany(), this.getDate(), this.getChallan(), this.getType(), this.getMill(), 
					this.getSize(), this.getWeight());
			rs = data.query("select weight from paper_property where trading_company = ? and type = ? and mill = ? and size = ?", 
					this.getTradingCompany(), this.getType(), this.getMill(), this.getSize());
			rs.next();
			double currentWeight = rs.getDouble(1);
			if(currentWeight < this.getWeight()){
				DialogueBox.warning("Insufficient amount in inventory!");
			}
			else{
				data.update("update paper_property set weight = ? where trading_company = ? and type = ? and mill = ? and size = ?", 
						(currentWeight-this.getWeight()), this.getTradingCompany(), this.getType(), this.getMill(), this.getSize());
			}
		}
		else{
			DialogueBox.warning("Create paper property first!");
		}
		data.close();
		rs.close();
	}
	
	public void editConsume(String col, Object val, int id){
		Database data = new Database();
		data.update("Update paper_consumption Set "+col+" = ? where id = ?" , val, id);
		data.close();
	}

	public void deleteConsume(int id){
		 Database data = new Database();
		 data.update("Delete from paper_consumption where id = ?",id);
		 data.close();
	}
}
