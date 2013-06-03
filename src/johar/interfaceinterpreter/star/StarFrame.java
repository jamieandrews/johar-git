/*
 * Used for creating the Star GUI
 */

package johar.interfaceinterpreter.star;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class StarFrame extends JFrame {	
    private JMenuBar mainMenu;
    private JPanel mainPanel;
    private JSplitPane splitter;
    private JPanel statusPanel;
    private GroupLayout statusPanelLayout;
        
	public StarFrame() {
		initialize();
	}
	
	private void initialize() {
        try {
			mainPanel = new JPanel();
			splitter = new JSplitPane();
			statusPanel = new JPanel();
			mainMenu = new JMenuBar();			

			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			splitter.setDividerLocation(450);
			splitter.setDividerSize(2);
			splitter.setOneTouchExpandable(true);
			splitter.setPreferredSize(new Dimension(400, 200));
			
			splitter.setRightComponent(new JPanel());
			
			statusPanelLayout = new GroupLayout(statusPanel);
			statusPanel.setLayout(statusPanelLayout);
			
			GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
			mainPanel.setLayout(mainPanelLayout);
			mainPanelLayout.setHorizontalGroup(
			    mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			    .addComponent(splitter, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
			    .addComponent(statusPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			);
			mainPanelLayout.setVerticalGroup(
			    mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			    .addGroup(mainPanelLayout.createSequentialGroup()
			        .addComponent(splitter, GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addComponent(statusPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
			);
			
			try {
	            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	                if ("Nimbus".equals(info.getName())) {
	                    UIManager.setLookAndFeel(info.getClassName());
	                    break;
	                }
	            }
	        } catch (Exception ex) {}

			getContentPane().add(mainPanel, BorderLayout.CENTER);

			setJMenuBar(mainMenu);

			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public void addMenu(CommandMenu cmdMenu){
		try {
			mainMenu.add(cmdMenu);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addMenuItem(CommandMenuItem cmdItem, String parentMenuName){
		try {			
			int numOfMenus = mainMenu.getMenuCount();
			for (int i = 0; i < numOfMenus; i++){
				if (mainMenu.getMenu(i).getName().equals(parentMenuName)){
					mainMenu.getMenu(i).add(cmdItem);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setTextArea(ScrollingWidget scrollingTextArea) {
		splitter.setLeftComponent(scrollingTextArea);
	}
	
	public void setStatusBar(StatusBar statusBar){
		statusPanelLayout.setHorizontalGroup(
			    statusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			    .addComponent(statusBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			);
			statusPanelLayout.setVerticalGroup(
			    statusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			    .addComponent(statusBar, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
			);
	}
	
	public Component getStatusBar(String name){
		Component[] widgets = statusPanel.getComponents();
		for (Component widget : widgets){
			if (widget.getName().equals(name)){
				return widget;
			}
		}
		return null;
	}
	
}
