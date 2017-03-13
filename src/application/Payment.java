package application;

public class Payment {
	private int id;
	private String date;
	private String company;
	private long challanNo;
	private long poNo;
	private long billNo;
	private double amount;
	private String chequeNo;
	
	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Payment(String date, String company, long challanNo, long billNo, long poNo, double amount, String chequeNo) {
		super();
		this.date = date;
		this.company = company;
		this.challanNo = challanNo;
		this.billNo = billNo;
		this.poNo = poNo;
		this.amount = amount;
		this.chequeNo = chequeNo;
	}
	
	public Payment(int id, String date, String company, long challanNo, long billNo, long poNo, double amount, String chequeNo) {
		super();
		this.id = id;
		this.date = date;
		this.company = company;
		this.challanNo = challanNo;
		this.billNo = billNo;
		this.poNo = poNo;
		this.amount = amount;
		this.chequeNo = chequeNo;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public long getBillNo() {
		return billNo;
	}

	public void setBillNo(long billNo) {
		this.billNo = billNo;
	}
	
	public void payEntry(){
		Database data = new Database();
		data.update("insert into payment (date, company, challan_no, bill_no, po_no, amount, cheque_no) "
				+ "values(?, ?, ?, ?, ?, ?, ?)",this.getDate(), this.getCompany(), this.getChallanNo(), 
				this.getBillNo(), this.getPoNo(), this.getAmount(), this.getChequeNo());
		data.close();
	}
	
	public void editPayment(String col, Object val, int id){
		Database data = new Database();
		data.update("Update payment Set "+col+" = ? where pid = ?" , val, id);
		data.close();
	}

	public void deletePayment(int id){
		 Database data = new Database();
		 data.update("Delete from payment where pid=?",id);
		 data.close();
	}
}
