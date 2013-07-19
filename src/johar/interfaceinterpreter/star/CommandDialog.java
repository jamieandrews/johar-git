package johar.interfaceinterpreter.star;

import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import johar.gem.GemSetting;
import johar.idf.Idf;
import johar.idf.IdfCommand;
import johar.idf.IdfParameter;

/**
 * The Command Dialog Box creator.
 * 
 */
public class CommandDialog extends JFrame {
	private Idf _idf;
	private GemSetting _gem;
	private CommandController _cc;
	private IdfCommand _currentCommand;
	private Container container;
	private TreeMap<Integer, StageGUI> stageGUIMap;
	private StageGUI stageGUI;
	private JPanel buttonsPanel;
	private int _currentStage;
	private IdfAnalyzer idfAnalyzer;
	private int numOfQueryableStages;
	private int windowHeight = 0;
	private int screenHeight;
	private int height = 0;

	/**
	 * The CommandDialog constructor
	 * @param cc
	 * the Command Controller
	 * @param gem
	 * the GemSetting
	 * @param idf
	 * the IDF
	 * @param currentCommand
	 * the current IdfCommand
	 */
	public CommandDialog(CommandController cc, GemSetting gem, Idf idf,
			IdfCommand currentCommand) {
		_idf = idf;
		_cc = cc;
		_currentCommand = currentCommand;
		_gem = gem;
		currentCommand.getNumStages();
		stageGUIMap = new TreeMap<Integer, StageGUI>();
		idfAnalyzer = new IdfAnalyzer(_idf);
		numOfQueryableStages = idfAnalyzer.getNumOfQueryableStages(currentCommand);
		initialize();
	}

	//Initializes the Command Dialog
	private void initialize() {
		container = getContentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
	
		screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height - 100;

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLocation(300, 30);
		setResizable(false);
		setTitle(_currentCommand.getLabel());
		setName(_currentCommand.getCommandName());		
	}

	/**
	 * Initializes the GUI for the specified stage
	 * @param stageNumber
	 * the stage number
	 */
	public void initializeStageGUI(int stageNumber) {
		try {
			_currentStage = stageNumber;
			
			// Perform if the current stage has never been initialized so far
			if (!stageGUIMap.containsKey(stageNumber)) {
				stageGUI = new StageGUI(_cc, _gem, _idf, _currentCommand,
						stageNumber);
				stageGUI.setBorder(new EmptyBorder(10, 10, 10, 10));
				
				container.add(stageGUI);			
				stageGUIMap.put(stageNumber, stageGUI);

				revalidate();
			}
		} catch (Exception e) {
			MessageDialog.showError("An error occurred while performing the requested operation. [Error Details: "
					+ e.getMessage() + "]");
		}
	}

	/**
	 * Updates the Command Dialog to reflect changes
	 */
	public void revalidate() {
		if (stageGUIMap.size() > 0) {			
			for (int stageNumber : stageGUIMap.keySet()) {
				height = stageGUIMap.get(stageNumber).getPreferredSize().height;
				
				if (height > screenHeight)
					height = screenHeight;
				else
					height = height + 100;
				
				if (height > windowHeight)
					windowHeight = height;
				
				if (stageNumber == _currentStage) {
					
					//Make the current stage visible
					stageGUIMap.get(stageNumber).setVisible(true);
				} else
					stageGUIMap.get(stageNumber).setVisible(false);
				
				/* Disable all inactive parameters in current stage and
				   enable the active ones */
				deactivateParams(stageNumber);
			}
		}

		validateWindow();
		repaint();

		pack();
		setVisible(true);
	}

	private void validateWindow() {
		addButtons();	//Add the Cancel, Previous, Next, and OK buttons

		// Disables/enables buttons based on the number of queryable stages
		if (idfAnalyzer.getNumOfQueryableStagesBefore(_currentCommand,
				_currentStage) > 0
				&& idfAnalyzer.getNumOfQueryableStagesAfter(_currentCommand,
						_currentStage) > 0) {
			setPreviousButtonEnabled(true);
			setNextButtonEnabled(true);
		} else {
			if (idfAnalyzer.getNumOfQueryableStagesBefore(_currentCommand,
					_currentStage) == 0
					&& idfAnalyzer.getNumOfQueryableStagesAfter(
							_currentCommand, _currentStage) == 0) {
				setPreviousButtonEnabled(false);
				setNextButtonEnabled(false);
			} else if (idfAnalyzer.getNumOfQueryableStagesBefore(
					_currentCommand, _currentStage) == 0) {
				setPreviousButtonEnabled(false);
				setNextButtonEnabled(true);
			} else if (idfAnalyzer.getNumOfQueryableStagesAfter(
					_currentCommand, _currentStage) == 0) {
				setPreviousButtonEnabled(true);
				setNextButtonEnabled(false);
			}
		}
	}

	/**
	 * Get the GUI of the specified stage
	 * @param stageNumber
	 * stage number
	 * @return
	 * the stage GUI
	 */
	public StageGUI getStageGUI(int stageNumber) {
		if (stageGUIMap.containsKey(stageNumber)) {
			return stageGUIMap.get(stageNumber);
		} else
			return null;
	}
	
	/**
	 * Get the number of Stages that have been initialized so far
	 * @return
	 * the number of initialized stages
	 */
	public int getStageCount(){
		return stageGUIMap.size();
	}

	//Makes parameter widgets active or inactive
	private boolean deactivateParams(int stageNumber) {
		boolean isDeactivated = false;
		
		if (stageGUIMap.containsKey(stageNumber)) {
			JComponent stageGUI = getStageGUI(stageNumber);

			for (Component c : stageGUI.getComponents()) {
				if (c instanceof ParameterWidget) {
					if (isParamActive(c.getName(), stageNumber))
						setEnabledComponents(c, true);
					else {
						setEnabledComponents(c, false);
						isDeactivated = true;
					}
				}
			}
		}
		
		return isDeactivated;
	}

	//Enable or disable components
	private void setEnabledComponents(Component comp, boolean enable) {
		JComponent paramWidget = (JComponent) comp;
		ParameterWidget pWidget = (ParameterWidget) paramWidget;
		IdfParameter paramObj = pWidget.getParamObject();
		int repNumber = -1;
		// System.out.println(comp);
		for (Component c : paramWidget.getComponents()) {
			try {
				if (!(c instanceof MoveUpButton || c instanceof MoveDownButton || c instanceof DeleteButton))
					c.setEnabled(enable);
				else{
					if (!enable)
						c.setEnabled(enable);
					else{
						repNumber = -1;
						if (c instanceof MoveUpButton){
							repNumber = ((MoveUpButton) c).getRepNumber();
							if (repNumber == 0)
								c.setEnabled(false);
							else
								c.setEnabled(true);
						}
						else if (c instanceof MoveDownButton){
							repNumber = ((MoveDownButton) c).getRepNumber();
							if (repNumber == pWidget.getFieldsCount() - 1)
								c.setEnabled(false);
							else
								c.setEnabled(true);
						}
						else if (c instanceof DeleteButton){
							if (paramObj.getMinNumberOfReps() == pWidget.getFieldsCount())
								c.setEnabled(false);
							else
								c.setEnabled(true);
						}
						
					}
				}
			} catch (Exception e) {}
		}
	}
	
	//Adds buttons to the Command Dialog
	private void addButtons() {
		deleteButtonPanel();
		
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		buttonsPanel.setName("buttonsPanel");

		CommandCancelButton cancelButton = new CommandCancelButton(_cc);
		buttonsPanel.add(cancelButton);
		
		buttonsPanel.add(Box.createHorizontalGlue());

		if (numOfQueryableStages > 1) {
			PreviousButton prevButton = new PreviousButton(_cc);
			prevButton.setText("Previous");
			prevButton.setName("PreviousButton");

			if (idfAnalyzer.getNumOfQueryableStagesBefore(_currentCommand,
					_currentStage) == 0)
				prevButton.setEnabled(false);

			buttonsPanel.add(prevButton);

			NextButton nextButton = new NextButton(_cc);
			nextButton.setText("Next");
			nextButton.setName("NextButton");

			if (idfAnalyzer.getNumOfQueryableStagesAfter(_currentCommand,
					_currentStage) == 0)
				nextButton.setEnabled(false);

			buttonsPanel.add(nextButton);
		}

		CommandOKButton okButton = new CommandOKButton(_cc);
		buttonsPanel.add(okButton);

		container.add(buttonsPanel);
	}

	//Get the index of a button in the buttons panel
	private int getButtonIndex(String name){
		return WidgetAnalyzer.getWidgetIndexInFrame(this, name);
	}
	
	//Delete the buttons panel
	private void deleteButtonPanel(){
		int index = WidgetAnalyzer.getWidgetIndexInFrame(this, "buttonsPanel");

		if (index > -1)
			container.remove(index);
	}
	
	//Enables/Disables the previous button
	private void setPreviousButtonEnabled(boolean enable) {
		int index = getButtonIndex("PreviousButton");

		if (index > -1)
			((PreviousButton) container.getComponent(index)).setEnabled(enable);
	}

	//Enables/Disables the next button
	private void setNextButtonEnabled(boolean enable) {
		int index = getButtonIndex("NextButton");

		if (index > -1)
			((NextButton) container.getComponent(index)).setEnabled(enable);
	}
	
	/**
	 * Checks if the specified parameter is active.
	 * @param paramName
	 * name of parameter
	 * @param stageNumber
	 * stage number
	 * @return
	 * true or false
	 */
	public boolean isParamActive(String paramName, int stageNumber) {
		idfAnalyzer.setCurrentCommand(_currentCommand.getCommandName());
		IdfParameter param = idfAnalyzer.getIdfParameter(paramName, stageNumber);

		// Check if the parameter has a ParentParameter
		boolean isActive = false;
		if (param.getParentParameter() == null || param.getParentParameter().equals("")) {
			isActive = true;
		} else {
			/*
			 * Since the parameter has a ParentParameter, verify if the
			 * parameter's ParentValue is same as the parent parameter's value
			 */
			IdfParameter parentParam = idfAnalyzer.getIdfParameter(param
					.getParentParameter());
			String paramParentValue = param.getParentValue();
			String actualParentValue = "";
			
			try {
				for (int t = 0; t < parentParam.getMaxNumberOfReps(); t++) {
					actualParentValue = _gem.getParameter(
							parentParam.getParameterName(), t).toString();

					if (actualParentValue.equals(paramParentValue)) {
						isActive = true;
						break;
					}
				}
			} catch (Exception e) {
				isActive = false;
			}

			// If parent parameter has no value in Gem, then get its default
			// value (if any)
			if (!isActive) {
				actualParentValue = "";
				if (parentParam.getDefaultValueMethod() != null
						&& !parentParam.getDefaultValueMethod().equals(""))
					actualParentValue = _gem.callDefaultValueMethod(
							parentParam.getParameterName()).toString();
				else
					actualParentValue = parentParam.getDefaultValue();

				if (actualParentValue.equals(paramParentValue))
					isActive = true;
			}
		}

		return isActive;
	}

}
