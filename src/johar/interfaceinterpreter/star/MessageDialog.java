package johar.interfaceinterpreter.star;

import javax.swing.JOptionPane;

/**
 * Message Dialog Box for showing messages to users.
 *
 */
public class MessageDialog extends JOptionPane {

	/**
	 * Shows error messages using a dialog box customized for errors.
	 * @param message
	 * message to show
	 * @return
	 * true when the user clicks the OK button.
	 */
	public static boolean showError(String message){
		String[] messageArray = message.split("\n");
		int maxLength = 0;
		
		for (String msg : messageArray){
			if (msg.length() > maxLength)
				maxLength = msg.length();
		}
		
		if (maxLength > 0 && maxLength > 90){
			showMessageDialog(null, "<html><body><p style='width: 400px'>" + message, "Error", JOptionPane.ERROR_MESSAGE);
		}
		else{
			if (maxLength > 0 && maxLength <= 90){
				showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		return true;
	}
	
	/**
	 * Shows warning messages using a dialog box customized for warnings.
	 * @param message
	 * message to show
	 * @return
	 * true when the user clicks the OK button.
	 */
	public static boolean showWarning(String message){		
		String[] messageArray = message.split("\n");
		int maxLength = 0;
		for (String msg : messageArray){
			if (msg.length() > maxLength)
				maxLength = msg.length();
		}
		
		if (maxLength > 0 && maxLength > 90){			
			showMessageDialog(null, "<html><body><p style='width: 400px'>" + message, "Message", JOptionPane.WARNING_MESSAGE);
		}
		else{
			if (maxLength > 0 && maxLength <= 90){
				showMessageDialog(null, message, "Message", JOptionPane.WARNING_MESSAGE);
			}
		}
		return true;
	}
	
	/**
	 * Shows warning messages using a dialog box customized for warnings.
	 * @param message
	 * message to show
	 * @param title
	 * title of the dialog box
	 * @return
	 * true when the user clicks the OK button.
	 */
	public static boolean showWarning(String message, String title){
		String[] messageArray = message.split("\n");
		int maxLength = 0;
		for (String msg : messageArray){
			if (msg.length() > maxLength)
				maxLength = msg.length();
		}
		
		if (maxLength > 0 && maxLength > 90){			
			showMessageDialog(null, "<html><body><p style='width: 400px'>" + message, title, JOptionPane.WARNING_MESSAGE);
		}
		else{
			if (maxLength > 0 && maxLength <= 90){
				showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
			}
		}
		return true;
	}
	
	/**
	 * Shows information using a dialog box customized for that purpose (e.g. a successful operation or non-error/non-warning messages).
	 * @param message
	 * message to show
	 * @return
	 * true when the user clicks the OK button.
	 */
	public static boolean showInformation(String message){
		String[] messageArray = message.split("\n");
		int maxLength = 0;
		for (String msg : messageArray){
			if (msg.length() > maxLength)
				maxLength = msg.length();
		}
		
		if (maxLength > 0 && maxLength > 90){
			showMessageDialog(null, "<html><body><p style='width: 400px'>" + message, "Message", JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			if (maxLength > 0 && maxLength <= 90){
				showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		return true;
	}
	
	/**
	 * Shows information using a dialog box customized for that purpose (e.g. a successful operation or non-error/non-warning messages).
	 * @param message
	 * message to show
	 * @param title
	 * title of the dialog box
	 * @return
	 * true when the user clicks the OK button.
	 */
	public static boolean showInformation(String message, String title){
		String[] messageArray = message.split("\n");
		int maxLength = 0;
		for (String msg : messageArray){
			if (msg.length() > maxLength)
				maxLength = msg.length();
		}
		
		if (maxLength > 0 && maxLength > 90){
			showMessageDialog(null, "<html><body><p style='width: 400px'>" + message, title, JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			if (maxLength > 0 && maxLength <= 90){
				showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		return true;
	}
	
	/**
	 * Shows messages using a general dialog box. 
	 * @param message
	 * message to show 
	 */
	public static boolean show(String message){
		String[] messageArray = message.split("\n");
		int maxLength = 0;
		for (String msg : messageArray){
			if (msg.length() > maxLength)
				maxLength = msg.length();
		}
		
		if (maxLength > 0 && maxLength > 90){			
			showMessageDialog(null, "<html><body><p style='width: 400px'>" + message, "Message", JOptionPane.PLAIN_MESSAGE);
		}
		else{
			if (maxLength > 0 && maxLength <= 90){				
				showMessageDialog(null, message, "Message", JOptionPane.PLAIN_MESSAGE);
			}
		}
		
		return true;
	}
	
	/**
	 * Shows messages using a general dialog box. 
	 * @param message
	 * message to show 
	 * @param title
	 * title of the dialog box
	 */
	public static boolean show(String message, String title){
		String[] messageArray = message.split("\n");
		int maxLength = 0;
		for (String msg : messageArray){
			if (msg.length() > maxLength)
				maxLength = msg.length();
		}
		
		if (maxLength > 0 && maxLength > 90){			
			showMessageDialog(null, "<html><body><p style='width: 400px'>" + message, "Message", JOptionPane.PLAIN_MESSAGE);
		}
		else{
			if (maxLength > 0 && maxLength <= 90){				
				showMessageDialog(null, message, "Message", JOptionPane.PLAIN_MESSAGE);
			}
		}
		
		return true;
	}
	
	

}
