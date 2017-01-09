package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.*;

public class TypeSummaryTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 777L;
	private static Map<Class<?>, List<Class<?>>> graph = JarReader.getGraph();	
	
	private static String[] cols = { "Class Name", "Afferent", "Efferent", "Stability" };
	
	static {
		rowList = new ArrayList<String>();
		fillDataTable();
	}
	
	private static String[][] matrix; 
	private static Object[][] data ;
		/* = { 
			{ "Stuff 1", "Other Stuff 1", "Even More Stuff 1" ," " },		
			{ "Stuff 2", "Other Stuff 2", "Even More Stuff 2" ," " }, 
			{ "Stuff 3", "Other Stuff 3", "Even More Stuff 3" ," " },
			{ "Stuff 4", "Other Stuff 4", "Even More Stuff 4" ," " }, 
			{ "Stuff 5", "Other Stuff 5", "Even More Stuff 5" ," " },
			{ "Stuff 6", "Other Stuff 6", "Even More Stuff 6" ," " }, 
			{ "Stuff 7", "Other Stuff 7", "Even More Stuff 7" ," " } };*/

	private static List<String> rowList;

	    
	
	private static void fillDataTable(){
		Stability stability = new Stability();
		stability.setEfferentSet(graph);
		stability.fillCeCaLists();	
		int count = 0;
		//int aff
		
		matrix = new String[5][4];
		
		for (Entry<Class<?>, List<Class<?>>> cls : graph.entrySet()) {
			 String name = cls.getKey().getSimpleName();			 
			 double ce = stability.getEfferentSet().get(cls.getKey()).size();			 
			 double ca = stability.getAfferentSet().get(cls.getKey()).size();
			 double stab = stability.calculateStability(cls);
			 
			 rowList.add (name);
			 rowList.add (String.valueOf(ca));
			 rowList.add (String.valueOf(ce));
			 rowList.add (String.valueOf(stab)); 
		}	
		
		
		for (int x = 0; x < rowList.size()/cols.length; x++) //run 5 times
		{
		    for (int y = 0; y < (rowList.size()/cols.length)-1; y++) //
		    {
		    	matrix[x][y] = rowList.get(count++);
		    }
		}
	}
	
	
	
	public int getColumnCount() {
		return cols.length;
	}

	public int getRowCount() {
		return matrix.length;
	}

	public String getColumnName(int col) {
		return cols[col];
	}

	public Object getValueAt(int row, int col) {
		return matrix[row][col];
	}

	public Class<?> getColumnClass(int c) {
		
		return getValueAt(0, c).getClass();
	}
}