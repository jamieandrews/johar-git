/*
 * The Star IntI
 */

package johar.interfaceinterpreter.star;

import javax.swing.JOptionPane;
import johar.idf.*;
import johar.gem.*;

public class Star implements ShowTextHandler {
	private Idf idf;
	private GemSetting gem;
	private StarFrame starFrame;
	private String starTitle;
	private CommandMenu cmdMenu;
	private CommandMenuItem cmdMenuItem;
	private StatusBar statusBar;
	private TextDisplayArea textArea;
	private ScrollingWidget scrollWidget;
	private CommandController cc;
	
	public Star(String idfName) {
		try {
			idf = Idf.idfFromFile(idfName);
			if (!idf.getIdfVersion().equals("1.0")){
				showText("The specified IDF Version is not supported. The only IDF version allowed is 1.0.", 3000);
				System.exit(1);
			}
			gem = GemFactory.newGemSetting(idf, this);
			gem.validate();
			cc = new CommandController(gem, idf, this);		 //Create the Command Controller
			createStarGUI();
			gem.initializeAppEngine();			
		} catch (Exception e) {
			showText(e.getMessage(), 3000);
			//showText("An error occurred while launching the application. Please seek assistance from the system administrator(s).", 3000);
		}
	}
	
	//Create the Menus
	private void createMenus() {
		IdfCommandGroup cmdGroup;
		IdfCommand cmd;
		int numOfCmdGrps = idf.getNumCommandGroups();
		int numOfCmds = idf.getNumCommands();
		int numOfMembers = 0;
		boolean isActive;
				
		for (int i = 0; i < numOfCmdGrps; i++){
			cmdMenu = new CommandMenu();
			cmdGroup = idf.getCommandGroupNumber(i);
			cmdMenu.setName(cmdGroup.getLabel());
			cmdMenu.setText(cmdGroup.getLabel());
			starFrame.addMenu(cmdMenu);
			numOfMembers = cmdGroup.getNumMembers();
			for (int j = 0; j < numOfMembers; j++){
				for (int k = 0; k < numOfCmds; k++){
					cmd = idf.getCommandNumber(k);
					isActive = gem.methodIsActive(cmd.getCommandName());
					if (cmdGroup.getMemberNumber(j).equals(cmd.getCommandName())){
						cmdMenuItem = new CommandMenuItem(cmd.getCommandName(), cc);
						cmdMenuItem.setName(cmd.getCommandName());
						cmdMenuItem.setText(cmd.getLabel());
						cmdMenuItem.setEnabled(isActive);
						starFrame.addMenuItem(cmdMenuItem, cmdGroup.getLabel());
						break;
					}
				}
			}
		}
	}
	
	//Create the Status bar
	private void createStatusBar() {
		statusBar = new StatusBar("Welcome to the new Star GUI");
		statusBar.setName("Status");
		starFrame.setStatusBar(statusBar);
	}
	
	//Create the Text Display Area
	private void createTextDisplayArea() {
		textArea = new TextDisplayArea();
		scrollWidget = new ScrollingWidget(textArea);
		starFrame.setTextArea(scrollWidget);
	}
	
	//Set specified message in the Status bar
	private void setStatusMessage(String message) {
		statusBar = (StatusBar)starFrame.getStatusBar("Status");
		if (statusBar != null){
			statusBar.setStatusText(message);
		}
	}
	
	//Set the title of the Star GUI
	private void setStarTitle() {
		starTitle = idf.getApplication();
		starFrame.setTitle(starTitle);
	}
		
	//Create the Star GUI
	private void createStarGUI() {
		starFrame = new StarFrame();
		createMenus();
		createTextDisplayArea();
		createStatusBar();
		setStarTitle();
	}
	
	//Show the Star GUI
	public void show() {		
		starFrame.setVisible(true);	
	}
	
	//The main method
	public static void main(String args[]) {
		try {
			Star star = new Star(args[0]);	
			star.show();
		} catch (Exception e) {}
	}

	//Implement the showText method of the ShowTextHandler interface
	public void showText(String text, int priorityLevel) {
		if (priorityLevel >= HIGHPRIO_LEVEL) {
		    JOptionPane.showMessageDialog(null, text);
		} else if (priorityLevel >= RESULT_LEVEL) {
			textArea.setContent(text);
		} else if (priorityLevel >= STATUS_LEVEL) {
			setStatusMessage(text);
		} else if (priorityLevel >= DEBUG_LEVEL) {
		    System.out.println(text);
		} else {
		    System.out.println("Priority Level in Show Text Handler is not valid.");
		}
	}

}
