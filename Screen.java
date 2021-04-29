package uiDesign;
import java.awt.Dimension;
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
	private static TST tst;
	
	private static JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel tripSearchPanel;
	private JPanel busSearchPanel;
	private JPanel timeSearchPanel;
	private JComboBox<Object> busStopInput;
	private JComboBox<Object> startInput;
	private JComboBox<Object> endInput;
	private JLabel toLabel;
	private JSpinner timeSelecter;
	private JButton goButton;
	private JButton goTimeButton;
	private JTable table;
	private JTable timeTable;
	
	public Screen()
	{
		String startStr[] = {"Stop from"};
		String stopStr[] = {"Stop to"};
		String busStopStr[] = {"Bus Stop"};
		
		JPanel topPanel = new JPanel();		
		
		startInput = new JComboBox<Object>(startStr);
		startInput.setPreferredSize(new Dimension(300, 25));
		startInput.setEditable(true);
		startInput.addActionListener(new startListener());
		topPanel.add(startInput);
		
		toLabel = new JLabel("to");
		topPanel.add(toLabel);
		
		endInput = new JComboBox<Object>(stopStr);
		endInput.setPreferredSize(new Dimension(300, 25));
		endInput.setEditable(true);
		endInput.addActionListener(new endListener());
		topPanel.add(endInput);
		
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
		busStopInput = new JComboBox<Object>(busStopStr);
		busStopInput.setEditable(true);
		busStopInput.addActionListener(new busStopListener());
		busSearchPanel.setLayout(new GridLayout(4, 0));
		busSearchPanel.add(busStopInput);
		
		SpinnerDateModel model = new SpinnerDateModel();
		timeSelecter = new JSpinner(model);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSelecter, "HH:mm:ss");
		DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
		timeSelecter.setEditor(editor);
		JPanel timeTopPanel = new JPanel();
		timeTopPanel.add(timeSelecter);
		goTimeButton = new JButton("go!");
		goTimeButton.addActionListener(new timeButtonListener());
		timeTopPanel.add(goTimeButton);
		
		timeTable = new JTable(data, columnNames);
		JScrollPane timeScrollPane = new JScrollPane(timeTable);
		
		timeSearchPanel = new JPanel();
		timeSearchPanel.setLayout(new BoxLayout(timeSearchPanel, BoxLayout.Y_AXIS));
		timeSearchPanel.add(timeTopPanel);
		timeSearchPanel.add(timeScrollPane);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Trip Search", null, tripSearchPanel, "Search for the shortest trip between two stops");
		tabbedPane.addTab("Bus Stop Search", null, busSearchPanel, "Search for information about a particular bus stop");
		tabbedPane.addTab("Time Search", null, timeSearchPanel, "Search for stop changes based on a time");
	}
	
	private class buttonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object startStop = startInput.getSelectedItem();
			Object endStop = endInput.getSelectedItem();
			System.out.println("startStop: " + startStop);
			System.out.println("endStop: " + endStop);
			// TODO: interface with rest of program
		}
	}
	
	private class timeButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.print(e);
			// TODO: interface with rest of program
		}
	}
	
	private class startListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "comboBoxEdited")
			{
				String searchItem = ((String) startInput.getSelectedItem()).toUpperCase();
				Object result[] = tst.search(searchItem).toArray();
				setStartData(result);
				startInput.showPopup();
			}
		}
	}
	
	private class endListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "comboBoxEdited")
			{
				String searchItem = ((String) endInput.getSelectedItem()).toUpperCase();
				Object result[] = tst.search(searchItem).toArray();
				setEndData(result);
				endInput.showPopup();
			}
		}
	}
	
	private class busStopListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "comboBoxEdited")
			{
				String searchItem = ((String) busStopInput.getSelectedItem()).toUpperCase();
				System.out.println("get stop of [" + searchItem + "]");
				Object result[] = tst.search(searchItem).toArray();
				setBusStopData(result);
				busStopInput.showPopup();
			}
		}
	}
	
	public void setTableInformation(Object data[][], Object columnNames[])
	{
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		table.setModel(model);
		model.fireTableDataChanged();
	}
	
	public void setTimeTableInformation(Object data[][], Object columnNames[])
	{
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		timeTable.setModel(model);
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
		tst = new TST("stops.txt");
		Screen gui = new Screen();
		frame = new JFrame();

		frame.setLayout(new GridLayout());
		frame.getContentPane().add(gui.tabbedPane);
		frame.pack();
		frame.setVisible(true);
		
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
