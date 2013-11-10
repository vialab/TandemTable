package Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Logs information associated with the user to their log file [#].user
 *
 */
public class UserLogger {

	FileWriter writer;
	BufferedWriter bWriter;
	
	/**
	 * Writes the info to the log file [user].user
	 * @param user
	 * @param info
	 */
	public void logInfo(int user, String info){
		try {
			
			///////////////////////////////////////////////////////////////////////////
			// Instead of user, I could use rotatableDrawZone.getfilesCount to 
			//determine what file number the image was saved as for when they create
			//a new avatar
			/////////////////////////////////////////////////////////////////////////
			writer = new FileWriter(".\\data\\users\\info\\" + user + ".user");
			bWriter = new BufferedWriter(writer);
	
			bWriter.write(info);
			bWriter.flush();
			
			bWriter.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
