package application;

public class ChemicalProperty {
	private int id;
	private String tradingCompany;
	private String mfgCompany;
	private double rate;
	
	public ChemicalProperty(int id, String tradingCompany, String mfgCompany, double rate) {
		this.id = id;
		this.tradingCompany = tradingCompany;
		this.mfgCompany = mfgCompany;
		this.rate = rate;
	}
	
	public ChemicalProperty(String tradingCompany, String mfgCompany, double rate) {
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
	
	public void entryProperty(){
		Database data = new Database();
		data.update("insert into chemical_property (trading_company, mfg_company, weight, rate) values (?, ?, ?, ?)",
				this.getTradingCompany(), this.getMfgCompany(), 0.0d, this.getRate());
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
}
