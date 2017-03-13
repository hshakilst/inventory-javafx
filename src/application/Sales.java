package application;

public class Sales {
	
	private int id;
	private String date;
	private long challanNo;
	private long poNo;
	private long vatNo;
	private long billNo;
	private String companyName;
	private String itemName;
	private int qty;
	private double rate;
	private double vat;
	private double ait;
	
	public Sales(int id, String date, long challanNo, long poNo, long vatNo, long billNo, String companyName, String itemName, int qty,
			double rate, double vat, double ait) {
		super();
		this.id = id;
		this.date = date;
		this.challanNo = challanNo;
		this.poNo = poNo;
		this.vatNo = vatNo;
		this.billNo = billNo;
		this.companyName = companyName;
		this.itemName = itemName;
		this.qty = qty;
		this.rate = rate;
		this.vat = vat;
		this.ait = ait;
	}
	
	public Sales(String date, long challanNo, long poNo, long vatNo, long billNo, String companyName, String itemName, int qty,
			double rate, double vat, double ait) {
		this.date = date;
		this.challanNo = challanNo;
		this.poNo = poNo;
		this.vatNo = vatNo;
		this.billNo = billNo;
		this.companyName = companyName;
		this.itemName = itemName;
		this.qty = qty;
		this.rate = rate;
		this.vat = vat;
		this.ait = ait;
	}

	public Sales() {
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public long getChallanNo() {
		return challanNo;
	}
	public void setChallanNo(long challanNo) {
		this.challanNo = challanNo;
	}
	public long getPoNo() {
		return poNo;
	}
	public void setPoNo(long poNo) {
		this.poNo = poNo;
	}
	public long getVatNo() {
		return vatNo;
	}
	public void setVatNo(long vatNo) {
		this.vatNo = vatNo;
	}
	public long getBillNo() {
		return billNo;
	}
	public void setBillNo(long billNo) {
		this.billNo = billNo;
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
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getVat() {
		return vat;
	}
	public void setVat(double vat) {
		this.vat = vat;
	}
	public double getAit() {
		return ait;
	}
	public void setAit(double ait) {
		this.ait = ait;
	}
	
	public void salesEntry(){
		Database data = new Database();
		data.update("insert into sales(date, challan_no, po_no, vat_no, bill_no, company, item, qty, rate, vat_amnt, ait_amnt)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", this.getDate(), this.getChallanNo(), this.getPoNo(), this.getVatNo(),
				this.getBillNo(), this.getCompanyName(), this.getItemName(), this.getQty(), this.getRate(), this.getVat(), this.getAit());
		data.close();
	}
	
	public void editSales(String col, Object val, int id){
		Database data = new Database();
		data.update("Update sales Set "+col+" = ? where sid = ?" , val, id);
		data.close();
	}

	public void deleteSales(int id){
		 Database data = new Database();
		 data.update("Delete from sales where sid=?",id);
		 data.close();
	}
}
