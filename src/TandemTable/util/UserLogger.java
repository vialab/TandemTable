package TandemTable.util;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Logs information associated with the user to their log file [#].user
 *
 */
public class UserLogger {

	FileWriter writer;
	int user;
	String path;
	
	
	public UserLogger(String path, int user) {
		this.user = user;
		this.path = path;
		
		try {
			writer = new FileWriter(path);
		} catch (IOException e) {
			System.out.println("User: " + user + ". Problem creating file writer at path: " + path);
			e.printStackTrace();
		}
	}
	/**
	 * Writes the info to the log file [user].user
	 * @param user
	 * @param info
	 */
	public void log(String info){
		try {
			writer.write(info + "\n");
			writer.flush();
		} catch (IOException e) {
			System.out.println("User: " + user + ". Problem writing to file at path: " + path);
			e.printStackTrace();
		}
	}
	
	/*public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			System.out.println("User: " + user + ". Problem closing writers");
			e.printStackTrace();
		}
		
	}*/
}
