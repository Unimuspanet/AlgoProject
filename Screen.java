package uiDesign;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;

public class Screen
{
	private static TST tst;
	private static shortestPath pathFinder;
	
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
	
	private static final String columnNames[] = {"ID", "Code", "Name", "Desc", "Latitude", "Longitude", "Zone ID", "URL", "Type", "Parent Station"};
	
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
		scrollPane.setPreferredSize(new Dimension(650, 400));
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
			int startStop = Integer.parseInt((String) startInput.getSelectedItem());
			int endStop = Integer.parseInt((String) endInput.getSelectedItem());
			System.out.println("startStop: " + startStop);
			System.out.println("endStop: " + endStop);
			
			Object result[] = pathFinder.findPath(startStop, endStop).toArray();
			Object data[][] = new Object[result.length][10];
			for (int i = 0; i < result.length; i++)
			{
				try {
					data[i] = getStopInfo(((Double) result[i]).intValue());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			setTableInformation(data, columnNames);
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
				ArrayList<String> result = tst.search(searchItem);
				result.add(0, searchItem);
				setStartData(result.toArray());
				startInput.showPopup();
			}
		}
	}
	
	private class endListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "comboBoxEdited")
			{
				String searchItem = ((String) endInput.getSelectedItem()).toUpperCase();
				ArrayList<String> result = tst.search(searchItem);
				result.add(0, searchItem);
				setEndData(result.toArray());
				endInput.showPopup();
			}
		}
	}
	
	private class busStopListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "comboBoxEdited")
			{
				String searchItem = ((String) busStopInput.getSelectedItem()).toUpperCase();
				ArrayList<String> result = tst.search(searchItem);
				result.add(0, searchItem);
				setBusStopData(result.toArray());
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
	
	public static Object[] getStopInfo(int stopId) throws FileNotFoundException
	{
		Object result[] = new Object[10];
		File stopsFile = new File("stops.txt");
		Scanner scanner = new Scanner(stopsFile);
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			String lineSplit[] = line.split(",");
			if (lineSplit[0].equals(String.valueOf(stopId)))
			{
				System.out.println("found it");
				System.out.println(line);
				result = lineSplit;
				break;
			}
		}
		scanner.close();
		return result;
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		tst = new TST("stops.txt");
		Screen gui = new Screen();
		frame = new JFrame();

		frame.setLayout(new GridLayout());
		frame.getContentPane().add(gui.tabbedPane);
		frame.pack();
		frame.setVisible(true);
		
		Object data[][] = {};
		gui.setTableInformation(data, columnNames);
		
		pathFinder = new shortestPath();
		//System.out.println("got here");
		//System.out.println(pathFinder.tracePath(1888, 11940));
		//System.out.println(pathFinder.findPath(1888, 11940));
		getStopInfo(1279);
		System.out.println("here");
	}
}  
