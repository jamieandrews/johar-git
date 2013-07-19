package johar.interfaceinterpreter.star;

import johar.idf.*;
import johar.utilities.TextInputValidator;
import johar.gem.*;

/**
 * The Star Interface Interpreter main class.
 */

public class Star implements ShowTextHandler {
	private Idf idf;
	private GemSetting gem;
	private StarWindow starFrame;
	private String starTitle;
	private CommandMenu cmdMenu;
	private CommandMenuItem cmdMenuItem;
	private StatusBar statusBar;
	private TextDisplayArea textArea;
	private ScrollingWidget scrollWidget;
	private CommandController cc;
	private TableArea tableArea;
	
	/**
	 * Star Constructor: implements the top-level behaviour specification.
	 * @param idfName
	 * Name of the IDF
	 */
	public Star(String idfName) {
		try {
			idf = Idf.idfFromFile(idfName);     //Read the IDF
			
			//Check the IDF version
			if (!idf.getIdfVersion().equals("1.0")){
				showText("The specified IDF Version is not supported. The only IDF version allowed is 1.0.", 3000);
				System.exit(1);
			}
			
			//Create the GemSetting and validate it
			gem = GemFactory.newGemSetting(idf, this);
			gem.validate();
			
			cc = new CommandController(gem, idf, this);  //Create the Command Controller
			createStarGUI();	//Create the Main Panel and the Text Display Area
			
			//Call the application engine's initialization method and refresh the tables
			gem.initializeAppEngine();		
			
			createTableArea();	//Creates the Table Area and refreshes the tables.	
			
			starFrame.appendHorizontalLineToTextArea();	/* Append a horizontal line 
														to the bottom of the Text Area. */
		} catch (IdfFormatException ex) {
			showText(ex.getMessage(), 3000);	//Show message to user
		} catch (Exception e) {
			MessageDialog.showError("An error occurred while launching the application. " +					
					"[Error Details: " + e.getMessage() + "]");
		}
	}

	/*Create the Menus and Call the ActiveIfMethod method of every command, and make each menu item of each menu
	active or inactive (greyed out) according to the result of the ActiveIfMethodmethod of the command. */
	private void createMenus() {
		IdfAnalyzer idfAnalyzer = new IdfAnalyzer(idf); /* Provides access to various information about the specified IDF 
														* e.g. list of queryable/non-queryable params in a command/stage, 
														* list of questions in a command, list of tables, 
														* list of browsable tables, etc. */
		
		IdfCommandGroup cmdGroup;
		IdfCommand cmd;
		int numOfCmdGrps = idf.getNumCommandGroups();
		int numOfCmds = idf.getNumCommands();
		int numOfMembers = 0;
		boolean isActive;
				
		for (int i = 0; i < numOfCmdGrps; i++){			
			cmdGroup = idf.getCommandGroupNumber(i);
			cmdMenu = new CommandMenu(cmdGroup.getCommandGroupName(), cmdGroup.getLabel());			
			starFrame.addMenu(cmdMenu);
			numOfMembers = cmdGroup.getNumMembers();
			for (int j = 0; j < numOfMembers; j++){
				for (int k = 0; k < numOfCmds; k++){
					cmd = idf.getCommandNumber(k);
					isActive = gem.methodIsActive(cmd.getCommandName());
					if (cmdGroup.getMemberNumber(j).equals(cmd.getCommandName())){
						cmdMenuItem = new CommandMenuItem(cmd.getCommandName(), cc, false);
						idfAnalyzer.setCurrentCommand(cmd.getCommandName());
						
						if (idfAnalyzer.hasQueryableParams())
							cmdMenuItem.setText(cmd.getLabel() + "...");
						else
							cmdMenuItem.setText(cmd.getLabel());
						
						cmdMenuItem.setEnabled(isActive);
						starFrame.addMenuItem(cmdMenuItem, cmdGroup.getCommandGroupName());
						break;
					}
				}
			}
		}
		
		//Add the Star menu 
		cmdMenu = new CommandMenu("star", "Star");
		starFrame.addMenu(cmdMenu);
		
		//Add the Help menu item to Star
		cmdMenuItem = new CommandMenuItem("help", cc, true);
		cmdMenuItem.setText("Help");
		cmdMenuItem.setEnabled(true);
		starFrame.addMenuItem(cmdMenuItem, "star");
	}
	
	//Create the Status bar
	private void createStatusBar() {
		statusBar = new StatusBar();		
		starFrame.setStatusBar(statusBar);
	}
	
	//Create the Text Display Area
	private void createTextDisplayArea() {
		textArea = new TextDisplayArea();
		scrollWidget = new ScrollingWidget(textArea);
		scrollWidget.setName("textDisplayArea");
		starFrame.setTextArea(scrollWidget);
	}
	
	//Create the Table Area
	private void createTableArea() {
		tableArea = new TableArea(idf, gem, cc);
		tableArea.setName("tableDisplayArea");
		starFrame.setTableArea(tableArea);
	}	
	
	//Set the title of the Star GUI
	private void setStarTitle() {
		starTitle = idf.getApplication();
		starFrame.setTitle(TextInputValidator.titleCaseTranslation(starTitle));
	}
		
	//Create the Star GUI
	private void createStarGUI() {
		starFrame = new StarWindow();
		createMenus();
		createTextDisplayArea();		
		createStatusBar();
		setStarTitle();
	}
	
	/**
	 * Show the Star GUI
	 */
	public void show() {
		if (starFrame != null)
			starFrame.setVisible(true); 
	}
	
	/**
	 * Get a Star Window (or Star Frame) instance
	 * @return
	 * Star Window instance
	 */
	public StarWindow getStarFrame(){
		return starFrame;
	}
	
	/**
	 * Implements the showText method of the ShowTextHandler interface
	 */
	public void showText(String text, int priorityLevel) {
		if (priorityLevel >= HIGHPRIO_LEVEL) {
		   MessageDialog.show(text);
		} else if (priorityLevel >= RESULT_LEVEL) {
			textArea.setContent(text);
			cc.lastDisplayedText += text;
		} else if (priorityLevel >= STATUS_LEVEL) {
			starFrame.setStatusMessage(text);
		} else if (priorityLevel >= DEBUG_LEVEL) {
		    System.out.println(text);
		} else {
		    System.out.println("Priority Level in Show Text Handler is not valid.");
		}
	}
	
	/**
	 * The main method.
	 * @param args
	 * Command-Line arguments
	 */
	public static void main(String args[]) {
		Star star = new Star(args[0]);	
		star.show();		
	}

}
