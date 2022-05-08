package mypackage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConnectDB {
	Connection connection = null;
	public boolean getConnection() {
		String user = "root";
		String password = "";
		try {
			String url1 = "jdbc:mysql://localhost:3307/stori?user=" + user + "&pasword=" + password;
			connection = DriverManager.getConnection(url1);
			if (connection != null) {
				System.out.println("Connection successful");
				return true;
			} else {
				System.out.println("Connection failure");
				return false;
			}
		} catch (Exception e) {
			System.out.println("Connection failure");
			e.printStackTrace();
			return false;
		}
	}
	public void insertEntry(String line) {
		String[] fields = line.split(",");
	    String id = fields[0];
	    @SuppressWarnings("deprecation")
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(fields[1]));
	    String transactionType = "Credit";
	    String transaction = fields[2].replace("+", "").replace("-", "");
	    if(fields[2].startsWith("+")) {
	    	transactionType = "Credit";
	    } else if (fields[2].startsWith("-")) {
	    	transactionType = "Debit";
	    }
	    String insertSQL = "INSERT IGNORE INTO `transactions` (`ID`, `Date`, `TransactionType`, `Transaction`) VALUES ('"+id+"', '"+date+"', '"+transactionType+"', '"+transaction+"');";
	    //System.out.println(insertSQL);
	    try {
	    	PreparedStatement statement = connection.prepareStatement(insertSQL);
	    	statement.execute();
	    	statement.close();
		} catch (Exception e) {
			System.out.println("A problem occured inserting the entries on the database");
			e.printStackTrace();
		}   
	}
	public void getEntries() {
		String totalSQL = "SELECT (SELECT SUM(Transaction) FROM `transactions` WHERE TransactionType = 'Credit') - (SELECT SUM(Transaction) FROM `transactions` WHERE TransactionType = 'Debit') as Total;";
		String monthTransactionsSQL = "SELECT COUNT(Transaction) as Transactions, MONTHNAME(Date) as Month FROM transactions GROUP BY (MONTH(Date));";
		ArrayList<String> transactions = new ArrayList<String>();
		String averageCreditSQL = "SELECT ROUND(AVG(Transaction),2) as AverageCredit from transactions WHERE TransactionType = 'Credit';";
		String averageDebitSQL = "SELECT ROUND(AVG(Transaction),2) as AverageDebit from transactions WHERE TransactionType = 'Debit';";
		try {
			Statement totalStatement = connection.createStatement();
			ResultSet ts = totalStatement.executeQuery(totalSQL);
			ts.next();
			String total = "Total balance is " + ts.getString("Total");
			System.out.println(total);
			
			Statement mtStatement = connection.createStatement();
			ResultSet mts = mtStatement.executeQuery(monthTransactionsSQL);
			while(mts.next()) {
				transactions.add("Number of transactions in " + mts.getString("Month") + ": " + mts.getString("Transactions"));
				System.out.println("Number of transactions in " + mts.getString("Month") + ": " + mts.getString("Transactions"));
			}
			
			Statement adStatement = connection.createStatement();
			ResultSet adS = adStatement.executeQuery(averageDebitSQL);
			adS.next();
			String averageDebit = "Average debit amount: -" + adS.getString("AverageDebit");
			System.out.println(averageDebit);
			
			Statement acStatement = connection.createStatement();
			ResultSet acS = acStatement.executeQuery(averageCreditSQL);
			acS.next();
			String averageCredit = "Average credit amount: " + acS.getString("AverageCredit");
			System.out.println(averageCredit);
			
			SendEmail se = new SendEmail();
			se.submitEmail(total,transactions,averageDebit,averageCredit);
		} catch (SQLException e) {
			System.out.println("A problem ocurred getting the entries from the database");
			e.printStackTrace();
		}
	}
}