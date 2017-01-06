package ie.gmit.sw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.*;

public class TypeSummaryTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 777L;
	private static Map<Class<?>, List<Class<?>>> graph = JarReader.getGraph();	
	
	private String[] cols = { "Class Name", "Afferent", "Efferent", "Stability" };
	
	static {
		rowList = new ArrayList<String[]>();
		fillDataTable();
	}
	
	
	private static Object[][] data ;
		/* = { 
			{ "Stuff 1", "Other Stuff 1", "Even More Stuff 1" ," " },		
			{ "Stuff 2", "Other Stuff 2", "Even More Stuff 2" ," " }, 
			{ "Stuff 3", "Other Stuff 3", "Even More Stuff 3" ," " },
			{ "Stuff 4", "Other Stuff 4", "Even More Stuff 4" ," " }, 
			{ "Stuff 5", "Other Stuff 5", "Even More Stuff 5" ," " },
			{ "Stuff 6", "Other Stuff 6", "Even More Stuff 6" ," " }, 
			{ "Stuff 7", "Other Stuff 7", "Even More Stuff 7" ," " } };*/

	private static List<String[]> rowList;

	    
	
	private static void fillDataTable(){
		Stability stability = new Stability();
		stability.setEfferentSet(graph);
		stability.fillCeCaLists();	
		
		for (Entry<Class<?>, List<Class<?>>> cls : graph.entrySet()) {
			
			 String name = cls.getKey().getSimpleName();
			 String ce = "";
			 //Map<Class<?>, List<Class<?>>> ce = stability.getEfferentSet();
			 
			 String ca = "";//stability.getAfferentSet().get(cls);
			 String stab = "0.0";

			 rowList.add(new String[] {name, ca, ce, stab});
			  //data[0][1] = {name, ca, ce, stab};		 
		}	
	}
	
	
	
	public int getColumnCount() {
		return cols.length;
	}

	public int getRowCount() {
		return rowList.size();
	}

	public String getColumnName(int col) {
		return cols[col];
	}

	public Object getValueAt(int row, int col) {
		//rowList.get(1);
		return rowList.get(col);
	}

	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}