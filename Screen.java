package uiDesign;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.*;
import javax.swing.*;
//import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;

public class Screen
{
	private static JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel tripSearchPanel;
	private JPanel busSearchPanel;
	private JComboBox<Object> busStopInput;
	private JComboBox<Object> startInput;
	private JComboBox<Object> endInput;
	private JLabel toLabel;
	private JLabel atLabel;
	private JSpinner timeSelecter;
	private JButton goButton;
	private JTable table;
	
	public Screen()
	{
		String testingString[] = {
			"From",
			"item 2",
			"we got to item 3",
			"and theres this one",
			"last one coming up",
			"lets go",
			"0",
			"1",
			"2",
			"3",
			"4",
			"5",
			"6",
			"7",
			"8",
			"9",
			"10"
		};
		
		JPanel topPanel = new JPanel();		
		
		startInput = new JComboBox<Object>(testingString);
		startInput.setEditable(true);
		startInput.addActionListener(new startListener());
		topPanel.add(startInput);
		
		toLabel = new JLabel("to");
		topPanel.add(toLabel);
		
		endInput = new JComboBox<Object>(testingString);
		endInput.setEditable(true);
		endInput.addActionListener(new endListener());
		topPanel.add(endInput);
		
		atLabel = new JLabel("at");
		topPanel.add(atLabel);
		
		SpinnerDateModel model = new SpinnerDateModel();
		timeSelecter = new JSpinner(model);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSelecter, "HH:mm:ss");
		DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
		timeSelecter.setEditor(editor);
		topPanel.add(timeSelecter);
		
		goButton = new JButton("go!");
		goButton.addActionListener(new buttonListener());
		topPanel.add(goButton);
		
		String columnNames[] = {"Stop", "trip id", "stop #", "time"};
		Object data[][] = {
			{ 646, "9017927", 1, "5:25:00" },
			{ 378, "9017927", 2, "5:25:50" },
			{ 379, "9017927", 3, "5:26:28" }
		};
		table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		JPanel tablePanel = new JPanel();
		tablePanel.add(scrollPane);
		
		tripSearchPanel = new JPanel();
		tripSearchPanel.setLayout(new BoxLayout(tripSearchPanel, BoxLayout.Y_AXIS));
		topPanel.setSize(500, 20);
		tripSearchPanel.add(topPanel);
		tablePanel.setSize(500, 450);
		tripSearchPanel.add(tablePanel);
		
		busSearchPanel = new JPanel();
		busStopInput = new JComboBox<Object>(testingString);
		busStopInput.setEditable(true);
		busStopInput.addActionListener(new busStopListener());
		busSearchPanel.setLayout(new GridLayout(4, 0));
		busSearchPanel.add(busStopInput);
		
		tabbedPane = new JTabbedPane();
		//tabbedPane.setBounds(0, 0, 1000, 1000);
		//tabbedPane.set
		tabbedPane.addTab("Trip Search", null, tripSearchPanel, "Search for the shortest trip between two stops");
		tabbedPane.addTab("Bus Stop Search", null, busSearchPanel, "Search for information about a particular bus stop");
	}
	
	private class buttonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object startStop = startInput.getSelectedItem();
			Object endStop = endInput.getSelectedItem();
			Object time = timeSelecter.getValue();
			System.out.println("startStop: " + startStop);
			System.out.println("endStop: " + endStop);
			System.out.println("time: " + time);
			// TODO: interface with rest of program
		}
	}
	
	private class startListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "comboBoxEdited")
			{
				String searchItem = (String) startInput.getSelectedItem();
				System.out.println("get stop of [" + searchItem + "]");
				//TODO: interface with 'match' function to search for stop
				//TODO: update stop data of start with results
				// EG:
				String columnNames[] = {"Stop", "trip id", "stop #", "time", "new info"};
				setStartData(columnNames);
				// end EG
				startInput.showPopup();
			}
		}
	}
	
	private class endListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "comboBoxEdited")
			{
				String searchItem = (String) endInput.getSelectedItem();
				System.out.println("get stop of [" + searchItem + "]");
				//TODO: interface with 'match' function to search for stop
				//TODO: update stop data of end with results
				endInput.showPopup();
			}
		}
	}
	
	private class busStopListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "comboBoxEdited")
			{
				String searchItem = (String) busStopInput.getSelectedItem();
				System.out.println("get stop of [" + searchItem + "]");
				//TODO: interface with 'match' function to search for stop
				//TODO: update stop data of end with results
				busStopInput.showPopup();
			}
		}
	}
	
	public void setTableInformation(Object data[][], Object columnNames[]) {
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		table.setModel(model);
		model.fireTableDataChanged();
	}
	
	public void setStartData(Object data[])
	{
		DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<Object>(data);
		startInput.setModel(model);
	}
	
	public void setEndData(Object data[])
	{
		DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<Object>(data);
		endInput.setModel(model);
	}
	
	public void setBusStopData(Object data[])
	{
		DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<Object>(data);
		busStopInput.setModel(model);
	}
	
	public static void main(String[] args)
	{
		Screen gui = new Screen();
		frame = new JFrame();			//creating instance of JFrame

		frame.setLayout(new GridLayout());
		frame.getContentPane().add(gui.tabbedPane);
		frame.pack();
		//frame.setSize(1000, 1000);
		frame.setVisible(true);				//making the frame visible
		
		String columnNames[] = {"Stop", "trip id", "stop #", "time", "new info"};
		Object data[][] = {
			{ 646, "9017927", 1, "5:25:00", 12 },
			{ 378, "9017927", 2, "5:25:50", "12" },
			{ 379, "9017927", 3, "5:26:28", "newinfo" }
		};
		gui.setTableInformation(data, columnNames);
		//gui.setStopData(columnNames);
	}
}  
