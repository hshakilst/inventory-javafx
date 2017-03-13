package application;

public class PaperProperty {
	private int id;
	private String company;
	private String type;
	private String mill;
	private double size;
	
	public PaperProperty(int id, String company, String type, String mill, double size) {
		super();
		this.id = id;
		this.company = company;
		this.type = type;
		this.mill = mill;
		this.size = size;
	}
	
	public PaperProperty(String company, String type, String mill, double size) {
		super();
		this.company = company;
		this.type = type;
		this.mill = mill;
		this.size = size;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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
	public void entryProperty(){
		Database data = new Database();
		data.update("insert into paper_property (trading_company, type, mill, size, weight) values (?, ?, ?, ?, ?)",
				this.getCompany(), this.getType(), this.getMill(), this.getSize(), 0.0d);
		data.close();
	}
	
	public void editProperty(String col, Object val, int id){
		Database data = new Database();
		data.update("Update paper_property set "+col+" = ? where id = ?" , val, id);
		data.close();
	}

	public void deleteProperty(int id){
		 Database data = new Database();
		 data.update("Delete from paper_property where id=?",id);
		 data.close();
	}

}
