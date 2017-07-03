package application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChemicalProperty {
	private int id;
	private String profileName;
	private String tradingCompany;
	private String mfgCompany;
	private double rate;
	
	public ChemicalProperty(int id, String profileName,  String tradingCompany, String mfgCompany, double rate) {
		this.id = id;
		this.setProfileName(profileName);
		this.tradingCompany = tradingCompany;
		this.mfgCompany = mfgCompany;
		this.rate = rate;
	}
	
	public ChemicalProperty(String profileName, String tradingCompany, String mfgCompany, double rate) {
		this.setProfileName(profileName);
		this.tradingCompany = tradingCompany;
		this.mfgCompany = mfgCompany;
		this.rate = rate;
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

	public String getMfgCompany() {
		return mfgCompany;
	}

	public void setMfgCompany(String mfgCompany) {
		this.mfgCompany = mfgCompany;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	public void entryProperty() throws SQLException {
		Database data = new Database();
		ResultSet rs = data.query("select exists (select 1 from chemical_property where profile_name = ?)", this.getProfileName());
		rs.next();
		if (!(rs.getInt(1) == 1)){
			data.update("insert into chemical_property (profile_name, trading_company, mfg_company, weight, rate) values (?, ?, ?, ?, ?)",
					this.getProfileName(), this.getTradingCompany(), this.getMfgCompany(), 0.0d, this.getRate());
		}
		else{
			DialogueBox.warning("Profile name already exists!");
		}
		data.close();
	}
	
	public void editProperty(String col, Object val, int id){
		Database data = new Database();
		data.update("Update chemical_property set "+col+" = ? where id = ?" , val, id);
		data.close();
	}

	public void deleteProperty(int id){
		 Database data = new Database();
		 data.update("Delete from chemical_property where id=?",id);
		 data.close();
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
}
