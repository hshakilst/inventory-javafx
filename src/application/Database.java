package application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.sqlite.SQLiteConnection;

class Database{
	private final String DB_URL=System.getProperty("user.dir") + "\\db\\database.db";
	private SQLiteConnection connect;
	private PreparedStatement statement;
	private ResultSet result;
	
	public Database(){
		try{
			System.out.println("Establishing connection to database...");
			connect = new SQLiteConnection("DataSource={0};Version=3", DB_URL);
			System.out.println("Connected to Database.");
		}catch(SQLException se){
			DialogueBox.error(se);
		}catch(Exception e){
			DialogueBox.error(e);
		}
	}
	
	public ResultSet query(String sql, Object...objects){
            try{
		statement=connect.prepareStatement(sql);
		int i = 1;
		for(Object o : objects){
			if(o instanceof String){
				statement.setString(i, (String) o);
			}
			else if(o instanceof Integer){
				statement.setInt(i, (int) o);
			}
			else if(o instanceof Double){
				statement.setDouble(i, (double) o);
			}
			else if(o instanceof Long){
				statement.setLong(i, (long) o);
			}
			i++;
		}
		System.out.println("Query in progress...");
		result=statement.executeQuery();
            }catch(SQLException se){
            	DialogueBox.error(se);
            }
            return result;
	}
	
	public boolean update(String sql, Object...objects){
		int flag=-1;
		try{
			statement=connect.prepareStatement(sql);
			int i = 1;
			for(Object o : objects){
				if(o instanceof String){
					//System.out.println(i+" "+o);
					statement.setString(i, (String) o);
				}
				else if(o instanceof Integer){
					//System.out.println(i+" "+o);
					statement.setInt(i, (int) o);
				}
				else if(o instanceof Double){
					statement.setDouble(i, (double) o);
				}
				else if(o instanceof Long){
					statement.setLong(i, (long) o);
				}
				i++;
			}
			System.out.println("Update in progress...");
			flag=statement.executeUpdate();
		}catch(SQLException se){
			DialogueBox.error(se);
		}
		if(flag>-1)
			return true;
		else
			return false;
	}
	
	public void close(){
		try{
				if(statement!=null)
					statement.close();
			}catch(SQLException se){
				DialogueBox.error(se);
			}
			try{
				if(connect!=null)
					connect.close();
			}catch(SQLException se){
				DialogueBox.error(se);
			}
	}
	
}
