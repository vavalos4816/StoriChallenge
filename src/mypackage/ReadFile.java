package mypackage;
import java.io.File;
import java.util.Scanner;

public class ReadFile {
	static String fileName = "src/mypackage/transactions.csv";
	static ConnectDB connection;
	public static void main(String[] args) {
		connection = new ConnectDB();
		boolean connectDB = connection.getConnection();
		if(connectDB) {
			File file = getCSV(fileName);
			if(file != null) {
				readFile(file);
			}
		}
	}
	public static File getCSV(String fileName) {
		try {
			  File file = new File(fileName);
		      return file;
		    } catch (Exception e) {
		      System.out.println("An error occurred while trying to read the CSV file");
		      e.printStackTrace();
		      return null;
		    }
	}
	public static boolean readFile(File file) {
		String line = "";
	    try {
	      Scanner scanner = new Scanner(file);
	      scanner.nextLine();
	      while (scanner.hasNextLine()) {
	        line = scanner.nextLine();
	        connection.insertEntry(line);
	      }
	      scanner.close();
	      connection.getEntries();
	      return true;
	    } catch (Exception e) {
	      System.out.println("An error occurred while trying to read the CSV file");
	      e.printStackTrace();
	      return false;
	    }
	  }
}
