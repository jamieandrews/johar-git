/*
 * Creates widget for "choice" type
 */

package johar.interfaceinterpreter.star;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import johar.idf.IdfParameter;

public class ChoiceWidget extends JPanel {
	private String _choiceText;
	private long _minNumOfReps;
	private long _maxNumOfReps;
	private Object _defaultValue;
	private String _paramName;
	private String _repModel;
	private JComboBox currentChoiceField;
	private AddMoreButton moreButton;
	private MoveUpButton upButton;
	private MoveDownButton downButton;
	private DeleteButton delButton;
	private CommandController _cc;
	private GridBagConstraints constraints;
	private GridBagLayout layout;
	
	public ChoiceWidget(Object defaultValue, IdfParameter paramObj, CommandController cc) {
		_choiceText = paramObj.getChoices();
		_minNumOfReps = paramObj.getMinNumberOfReps();
		_maxNumOfReps = paramObj.getMaxNumberOfReps();	
		_defaultValue = defaultValue;
		_paramName = paramObj.getParameterName();
		_repModel = paramObj.getRepsModel();
		_cc = cc;
		createChoiceWidget();
	}
	
	private void createChoiceWidget() {		
		layout = new GridBagLayout();
		setLayout(layout);
		if (_maxNumOfReps > 1)
			setBorder(BorderFactory.createEtchedBorder());
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;		
		constraints.gridy = 0;
		constraints.insets = new Insets(5,0,0,5);
		setupChoiceField("cbo" + _paramName + 0, true);
		add(currentChoiceField, constraints);
		
		if (_maxNumOfReps > 1 && _repModel.equals("sequence")){
			upButton = new MoveUpButton(_cc, _paramName, 0);
			downButton = new MoveDownButton(_cc, _paramName, 0);
			upButton.setName("btnUp" + _paramName + 0);
			downButton.setName("btnDown" + _paramName + 0);
			
			constraints.gridx = 1;
			constraints.insets = new Insets(5,0,0,0);	
			add(upButton, constraints);
			
			constraints.gridx = 2;
			constraints.insets = new Insets(5,0,0,0);
			upButton.setEnabled(false);
			downButton.setEnabled(false);
			add(downButton, constraints);
		}
				
		if (_minNumOfReps > 1){		
			setEnabledDownButton("btnDown" + _paramName + 0, true);
			for (int i = 1; i < _minNumOfReps; i++){
				constraints.fill = GridBagConstraints.NONE;
				constraints.anchor = GridBagConstraints.WEST;
				constraints.gridx = 0;
				constraints.gridy = i;
				constraints.insets = new Insets(5,0,0,0);
				setupChoiceField("cbo" + _paramName + i, true);
				add(currentChoiceField, constraints);
				
				constraints.fill = GridBagConstraints.NONE;				
				constraints.anchor = GridBagConstraints.WEST;
				
				if (_repModel.equals("sequence")){
					upButton = new MoveUpButton(_cc, _paramName, i);
					downButton = new MoveDownButton(_cc, _paramName, i);					
					
					upButton.setName("btnUp" + _paramName + i);
					downButton.setName("btnDown" + _paramName + i);
					
					if (i == _minNumOfReps-1){
						downButton.setEnabled(false);
					}
					
					constraints.gridx = 1;
					constraints.insets = new Insets(5,0,0,0);	
					add(upButton, constraints);
					
					constraints.gridx = 2;
					constraints.insets = new Insets(5,0,0,0);
					add(downButton, constraints);
				}		
			}
		}
		
		if (_minNumOfReps < _maxNumOfReps){
			moreButton = new AddMoreButton(_cc, _paramName);
			moreButton.setName("btnMore" + _paramName);
			constraints.gridx = 0;
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.gridy = (int) _maxNumOfReps;
			constraints.insets = new Insets(5,0,0,0);
			add(moreButton, constraints);
		}
	}

	private void setupChoiceField(String name, boolean dvFlag) {
		currentChoiceField = new JComboBox();
		currentChoiceField.setName(name);
		String[] choiceArray = _choiceText.split("\\|");
		for (String choice : choiceArray){
			currentChoiceField.addItem(choice.trim());
		}

		if (dvFlag)
			setDefaultValue(_defaultValue);
	}
	
	public void addChoiceField(int repNumber){
		int position = getComponentCount();
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;				
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 0;
		constraints.gridy = repNumber;
		constraints.insets = new Insets(5,0,0,5);
		
		setupChoiceField("cbo" + _paramName + repNumber, true);
		add(currentChoiceField, constraints, position-1);
		
		if (_repModel.equals("sequence")){
			upButton = new MoveUpButton(_cc, _paramName, repNumber);
			downButton = new MoveDownButton(_cc, _paramName, repNumber);					
			
			upButton.setName("btnUp" + _paramName + repNumber);
			downButton.setName("btnDown" + _paramName + repNumber);

			downButton.setEnabled(false);
			setEnabledDownButton("btnDown" + _paramName + (repNumber - 1), true);
			
			constraints.gridx = 1;
			constraints.insets = new Insets(5,0,0,0);	
			add(upButton, constraints, position);
			
			constraints.gridx = 2;
			constraints.insets = new Insets(5,0,0,0);
			add(downButton, constraints, position + 1);
		}
		
		if ((repNumber + 1) >= _minNumOfReps){
			delButton = new DeleteButton(_cc, _paramName, repNumber);
			delButton.setName("btnDel" + _paramName + repNumber);
			constraints.gridx = 3;
			constraints.insets = new Insets(5,0,0,0);
			
			if (_repModel.equals("sequence"))
				add(delButton, constraints, position + 2);
			else
				add(delButton, constraints, position);
		}
		
		if ((repNumber + 1) == _maxNumOfReps){
			removeAddMoreButton("btnMore" + _paramName);
		}
		
		revalidate();
		repaint();
	}
	
	public void insertNewFields(int repNumber, int position){
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;				
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 0;
		constraints.gridy = repNumber;
		constraints.insets = new Insets(5,0,0,5);
		
		setupChoiceField("cbo" + _paramName + repNumber, false);
		add(currentChoiceField, constraints, position);
		
		if (_repModel.equals("sequence")){
			upButton = new MoveUpButton(_cc, _paramName, repNumber);
			downButton = new MoveDownButton(_cc, _paramName, repNumber);					
			
			upButton.setName("btnUp" + _paramName + repNumber);
			downButton.setName("btnDown" + _paramName + repNumber);
			
			if ((repNumber + 1) == getChoiceFieldsCount() && getChoiceFieldsCount() == _minNumOfReps)
				downButton.setEnabled(false);
			else {
				if (getChoiceFieldsCount() > _minNumOfReps){
					if ((repNumber + 1) == getChoiceFieldsCount()){
						downButton.setEnabled(false);
						setEnabledDownButton("btnDown" + _paramName + (repNumber - 1), true);
					}					
				}
				else if ((repNumber + 1) == _maxNumOfReps){
					downButton.setEnabled(false);
					setEnabledDownButton("btnDown" + _paramName + (repNumber - 1), true);
				}
			}				
			
			if (repNumber == 0)
				upButton.setEnabled(false);
						
			constraints.gridx = 1;
			constraints.insets = new Insets(5,0,0,0);	
			add(upButton, constraints, position + 1);
			
			constraints.gridx = 2;
			constraints.insets = new Insets(5,0,0,0);
			add(downButton, constraints, position + 2);
		}
		
		if (repNumber >= _minNumOfReps){
			delButton = new DeleteButton(_cc, _paramName, repNumber);
			delButton.setName("btnDel" + _paramName + repNumber);
			constraints.gridx = 3;
			constraints.insets = new Insets(5,0,0,0);
			add(delButton, constraints, position + 3);
		}
	}
	
	public void removeAddMoreButton(String name){
		for (Component c : getComponents()){
			try {
				if (c.getName().equals(name)){
					remove(c);
					break;
				}
			} catch (Exception e) {}
		}
	}
	
	public void setEnabledDownButton(String name, boolean b){
		for (Component c : getComponents()){
			try {
				if (c.getName().equals(name)){
					((MoveDownButton) c).setEnabled(b);
					break;
				}
			} catch (Exception e) {}
		}
	}
	
	public Object getSelectedChoice(int repNumber){
		String name = "cbo" + _paramName + repNumber;
		
		for (Component c : getComponents()){
			try {
				if (c.getName().equals(name)){
					return ((JComboBox) c).getSelectedItem();
				}
			} catch (Exception e) {}
		}
		return null;
	}
	
	public int getChoiceFieldsCount(){
		int counter = 0;
		for (Component c : getComponents()){			
			try {
				if (c.getName().contains("cbo")){
					counter = counter + 1;
				}
			} catch (Exception e) {}
		}
		return counter;
	}
	
	public void deleteChoiceField(int repNumber) {
		String name1 = "btnDel" + _paramName + repNumber;
		String name2 = "cbo" + _paramName + repNumber;
		String name3 = "btnUp" + _paramName + repNumber;
		String name4 = "btnDown" + _paramName + repNumber;
		int index = getComponentIndex(name2);
		GridBagConstraints cons = null;
		
		for (Component c : getComponents()){
			try {
				if (c.getName().equals(name1)){
					remove(c);
				}
				else if (c.getName().equals(name2)){
					cons = layout.getConstraints(c);
					remove(c);
				}
				else if (c.getName().equals(name3)){
					remove(c);
				}
				else if (c.getName().equals(name4)){
					remove(c);
				}
			} catch (Exception e) {}
		}
	
		renameFields(repNumber, index);
		redistributeFields(repNumber, index, cons);
		
		
		if (repNumber == getChoiceFieldsCount()){
			setEnabledDownButton("btnDown" + _paramName + (repNumber - 1), false);
		}		
		
		if (getChoiceFieldsCount() + 1 == _maxNumOfReps){			
			moreButton = new AddMoreButton(_cc, _paramName);
			moreButton.setName("btnMore" + _paramName);
			constraints.gridx = 0;
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.gridy = getChoiceFieldsCount() + 1;
			constraints.insets = new Insets(5,0,0,0);
			add(moreButton, constraints);
		}
		
		revalidate();
		repaint();
	}
	
	public void deleteComponent(String name) {
		for (Component c : getComponents()){
			try {
				if (c.getName().equals(name)){
					remove(c);
					break;
				}
			} catch (Exception e) {}
		}
	}
	
	public int getComponentIndex(String name){
		Component[] c = getComponents();
		for (int i = 0; i < c.length; i++){
			try {
				if (c[i].getName().equals(name)){
					return i;
				}
			} catch (Exception e) {}
		}
		return -1;
	}
	
	public void renameFields(int repNumber, int startIndex){
		Component[] c = getComponents();
		int counter = repNumber - 1;
		for (int i = startIndex; i < c.length; i++){
			try {
				if (c[i].getName().contains("cbo")){
					counter++;
					c[i].setName("cbo" + _paramName + counter);					
				}
				else if (c[i].getName().contains("btnUp")){
					c[i].setName("btnUp" + _paramName + counter);
					((MoveUpButton) c[i]).setRepNumber(counter);
				}
				else if (c[i].getName().contains("btnDown")){
					c[i].setName("btnDown" + _paramName + counter);
					((MoveDownButton) c[i]).setRepNumber(counter);
				}
				else if (c[i].getName().contains("btnDel")){
					c[i].setName("btnDel" + _paramName + counter);
					((DeleteButton) c[i]).setRepNumber(counter);
				}
			} catch (Exception e) {}
		}
	}
	
	public void redistributeFields(int repNumber, int startIndex, GridBagConstraints cons){
		Component[] c = getComponents();
		int counter = repNumber - 1;

		GridBagConstraints constraint = new GridBagConstraints();
		
		for (int i = startIndex; i < c.length; i++){
			try {
				if (c[i].getName().contains("cbo")){
					counter++;
					constraint.fill = cons.fill;
					constraint.anchor = cons.anchor;
					constraint.gridx = cons.gridx;
					constraint.gridy = counter;
					constraint.insets = cons.insets;
					layout.setConstraints(c[i], constraint);
				}
				else if (c[i].getName().contains("btnUp")){
					constraint.fill = GridBagConstraints.NONE;				
					constraint.anchor = GridBagConstraints.WEST;
					constraint.gridx = cons.gridx + 1;
					constraint.gridy = counter;
					constraint.insets = new Insets(5,0,0,0);
					layout.setConstraints(c[i], constraint);
				}
				else if (c[i].getName().contains("btnDown")){
					constraint.fill = GridBagConstraints.NONE;				
					constraint.anchor = GridBagConstraints.WEST;
					constraint.gridx = cons.gridx + 2;
					constraint.gridy = counter;
					constraint.insets = new Insets(5,0,0,0);
					layout.setConstraints(c[i], constraint);
				}
				else if (c[i].getName().contains("btnDel")){
					constraint.fill = GridBagConstraints.NONE;				
					constraint.anchor = GridBagConstraints.WEST;
					constraint.gridx = cons.gridx + 3;
					constraint.gridy = counter;
					constraint.insets = new Insets(5,0,0,0);
					layout.setConstraints(c[i], constraint);
				}
			} catch (Exception e) {}
		}
		
		revalidate();
	}
	
	public void moveUpFields(int repNumber){
		Map<Integer,List<String>> locationMap = new TreeMap<Integer,List<String>>();	
		List<String> compData = new ArrayList<String>();
		String name = "cbo" + _paramName + repNumber;
		String name1 = "cbo" + _paramName + (repNumber - 1);
		String name2 = "btnUp" + _paramName + repNumber;
		String name3 = "btnUp" + _paramName + (repNumber - 1);
		String name4 = "btnDown" + _paramName + repNumber;
		String name5 = "btnDown" + _paramName + (repNumber - 1);
		String name6 = "btnDel" + _paramName + repNumber;
		String name7 = "btnDel" + _paramName + (repNumber - 1);
		
		Component[] c = getComponents();
		for (int i = 0; i < c.length; i++){
			try {				
				if (c[i].getName().equals(name1)){
					compData.add(((JComboBox) c[i]).getSelectedItem().toString());
					compData.add(String.valueOf(i));
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name3)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name5)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name7)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name)){
					locationMap.put(repNumber - 1, compData);					
					compData = new ArrayList<String>();
					compData.add(((JComboBox) c[i]).getSelectedItem().toString());
					compData.add(String.valueOf(i));
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name2)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name4)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
					locationMap.put(repNumber, compData);
				}
				else if (c[i].getName().equals(name6)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
					locationMap.put(repNumber, compData);
					break;
				}
			} catch (Exception e) {e.printStackTrace();}			
		}
		
		revalidate();
		
		List<Integer> keys = new ArrayList<Integer>();
		
		for (int key : locationMap.keySet()){
			keys.add(key);
		}

		insertNewFields(keys.get(1)-1, Integer.parseInt(locationMap.get(keys.get(0)).get(1)));
		currentChoiceField.setSelectedItem(locationMap.get(keys.get(1)).get(0));				
		 
		insertNewFields(keys.get(0)+1, Integer.parseInt(locationMap.get(keys.get(1)).get(1)));
		currentChoiceField.setSelectedItem(locationMap.get(keys.get(0)).get(0));		
		
		revalidate();
	}
	
	public void moveDownFields(int repNumber){
		Map<Integer,List<String>> locationMap = new TreeMap<Integer,List<String>>();	
		List<String> compData = new ArrayList<String>();
		String name = "cbo" + _paramName + repNumber;
		String name1 = "cbo" + _paramName + (repNumber + 1);
		String name2 = "btnUp" + _paramName + repNumber;
		String name3 = "btnUp" + _paramName + (repNumber + 1);
		String name4 = "btnDown" + _paramName + repNumber;
		String name5 = "btnDown" + _paramName + (repNumber + 1);
		String name6 = "btnDel" + _paramName + repNumber;
		String name7 = "btnDel" + _paramName + (repNumber + 1);
		
		Component[] c = getComponents();
		for (int i = 0; i < c.length; i++){
			try {				
				if (c[i].getName().equals(name)){
					compData.add(((JComboBox) c[i]).getSelectedItem().toString());
					compData.add(String.valueOf(i));
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name2)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name4)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name6)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name1)){
					locationMap.put(repNumber, compData);					
					compData = new ArrayList<String>();
					compData.add(((JComboBox) c[i]).getSelectedItem().toString());
					compData.add(String.valueOf(i));
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name3)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
				}
				else if (c[i].getName().equals(name5)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
					locationMap.put(repNumber + 1, compData);
				}
				else if (c[i].getName().equals(name7)){
					compData.add(c[i].getName());
					deleteComponent(c[i].getName());
					locationMap.put(repNumber + 1, compData);
					break;
				}
			} catch (Exception e) {e.printStackTrace();}			
		}
		
		revalidate();
		
		List<Integer> keys = new ArrayList<Integer>();
		
		for (int key : locationMap.keySet()){
			keys.add(key);
		}

		insertNewFields(keys.get(1)-1, Integer.parseInt(locationMap.get(keys.get(0)).get(1)));
		currentChoiceField.setSelectedItem(locationMap.get(keys.get(1)).get(0));				
		 
		insertNewFields(keys.get(0)+1, Integer.parseInt(locationMap.get(keys.get(1)).get(1)));
		currentChoiceField.setSelectedItem(locationMap.get(keys.get(0)).get(0));	
		
		revalidate();
	}
	
	private void setDefaultValue(Object defaultValue) {
		try {
			if (defaultValue != null && !defaultValue.toString().trim().equals("")){
				currentChoiceField.setSelectedItem(defaultValue);
			}
		} catch (Exception e) {}		
	}
	
	public String getSelectedChoice(){
		return currentChoiceField.getSelectedItem().toString();
	}
	
}