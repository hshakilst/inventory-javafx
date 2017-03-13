package application;

public class Products {
	private int pid;
	private String companyName, itemName;
	private double uPin, uPrint, uMedia, uLiner, rate;
	
	public Products(int id, String cName, String iName, double pin, double print, double media, 
			double liner, double rate){
		this.pid = id;
		this.companyName = cName;
		this.itemName = iName;
		this.uPin = pin;
		this.uPrint = print;
		this.uMedia = media;
		this.uLiner = liner;
		this.rate = rate;
	}
	
	public Products(String cName, String iName, double pin, double print, double media, 
			double liner, double rate){
		this.companyName = cName;
		this.itemName = iName;
		this.uPin = pin;
		this.uPrint = print;
		this.uMedia = media;
		this.uLiner = liner;
		this.rate = rate;
	}

	public Products() {
		// TODO Auto-generated constructor stub
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getUPin() {
		return uPin;
	}

	public void setUPin(double uPin) {
		this.uPin = uPin;
	}

	public double getUPrint() {
		return uPrint;
	}

	public void setUPrint(double uPrint) {
		this.uPrint = uPrint;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getULiner() {
		return uLiner;
	}

	public void setULiner(double uLiner) {
		this.uLiner = uLiner;
	}

	public double getUMedia() {
		return uMedia;
	}

	public void setUMedia(double uMedia) {
		this.uMedia = uMedia;
	}
	
	public void createProduct(){
		try{
			Database data = new Database();
			data.update("Insert into products (company_name, item_name, upin, uprint, umedia, uliner, rate) "
					+ "values (?,?,?,?,?,?,?)", companyName, itemName, uPin, uPrint, uMedia, uLiner, rate);
			data.close();
		}catch(Exception e){
			DialogueBox.error(e);
		}
	}
	
	public void editProducts(String col, Object val, int id){
		Database data = new Database();
		data.update("Update products Set "+col+" = ? where pid = ?" , val, id);
		data.close();
	}

	public void deleteProduct(int pid){
		 Database data = new Database();
		 data.update("Delete from products where pid=?",pid);
		 data.close();
	}

}
