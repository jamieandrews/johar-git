package johar.interfaceinterpreter.star;

public interface CommandControllerBase {
	
	//The method is called when a menu item is clicked
	public void commandMenuItemClicked(String commandName);
	
	//The method is called when the Previous button (in a Command Window) is clicked 
	public void previousButtonClicked();
	
	//The method is called when the Next button (in a Command Window) is clicked
	public void nextButtonClicked();
	
	//The method is called when the OK button (in a Command Window) is clicked
	public void okButtonClicked();
	
	//The method is called when the Cancel button (in a Command Window) is clicked
	public void cancelButtonClicked();
	
	//The method is called when the Add another button (in a Command Window) is clicked
	public void addMoreButtonClicked(String paramName);
	
	//The method is called when the Move up button (in a Command Window) is clicked
	public void moveUpButtonClicked(String paramName, int repNumber);
	
	//The method is called when the Move down button (in a Command Window) is clicked
	public void moveDownButtonClicked(String paramName, int repNumber);
	
	//The method is called when the Delete button (in a Command Window) is clicked
	public void deleteButtonClicked(String paramName, int repNumber);
	
}
