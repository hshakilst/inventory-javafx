package application;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hshakilst on 7/3/2017.
 */
public class ChemicalConsumption {
    private int id;
    private String profileName;
    private String tradingCompany;
    private String date;
    private String mfgCompany;
    private long challan;
    private double weight;

    public ChemicalConsumption(int id, String profileName, String tradingCompany, String date, String mfgCompany,
                               long challan, double weight) {
        this.id = id;
        this.profileName = profileName;
        this.tradingCompany = tradingCompany;
        this.date = date;
        this.mfgCompany = mfgCompany;
        this.challan = challan;
        this.weight = weight;
    }

    public ChemicalConsumption(String profileName, String tradingCompany, String date, String mfgCompany, long challan, double weight) {
        this.profileName = profileName;
        this.tradingCompany = tradingCompany;
        this.date = date;
        this.mfgCompany = mfgCompany;
        this.challan = challan;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void entryConsume() throws SQLException {
        Database data = new Database();
        ResultSet rs = data.query("select exists (select 1 from chemical_property where profile_name = ?)",
                this.getProfileName());
        rs.next();
        if(rs.getInt(1) == 1){
            data.update("insert into chemical_consumption (profile_name, trading_company, date, mfg_company, " +
                            "challan_no, weight) values (?, ?, ?, ?, ?, ?)", this.getProfileName(),
                    this.getTradingCompany(), this.getDate(), this.mfgCompany, this.getChallan(), this.getWeight());
            rs = data.query("select weight from chemical_property where profile_name = ?",
                    this.getProfileName());
            rs.next();
            double currentWeight = rs.getDouble(1);
            if(currentWeight < this.getWeight()){
                DialogueBox.warning("Insufficient amount in inventory!");
            }
            else{
                data.update("update chemical_property set weight = ? where profile_name = ?",
                        (currentWeight-this.getWeight()), this.getProfileName());
            }
        }
        else{
            DialogueBox.warning("Create chemical property first!");
        }
        data.close();
        rs.close();
    }

    public void editConsume(String col, Object val, int id){
        Database data = new Database();
        data.update("Update chemical_consumption Set "+col+" = ? where id = ?" , val, id);
        data.close();
    }

    public void deleteConsume(int id){
        Database data = new Database();
        data.update("Delete from chemical_consumption where id = ?",id);
        data.close();
    }
}
