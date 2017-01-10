package ie.gmit.sw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.*;

public class TypeSummaryTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 777L;
	private static Map<Class<?>, List<Class<?>>> graph;	
	
	private String[] cols = { "Class Name", "Afferent", "Efferent", "Stability" };
	private List<Object> rowList;
	
	private static Object[][] data; 

	public void gatherData() {
		
		rowList = new ArrayList<Object>();
		
		Stability stability = new Stability();
		stability.setEfferentSet(graph);
		stability.fillCeCaLists();	
						
		for (Entry<Class<?>, List<Class<?>>> cls : graph.entrySet()) {
			 String name = cls.getKey().getSimpleName();
					 
			 List<Class<?>> ce = stability.getEfferentSet().get(cls.getKey());						 
			 List<Class<?>> ca = stability.getAfferentSet().get(cls.getKey());
			 double stab = stability.calculateStability(cls);
			 		 
			 if(ca==null)
				 ca= new ArrayList<Class<?>>();
			 
			 if(ce==null)
				 ce= new ArrayList<Class<?>>();
						 
			 rowList.add (name);
			 rowList.add (getClassNameList(ca));
			 rowList.add (getClassNameList(ce));
			 rowList.add (stab); 
		}
		fillGUItable();
	}

	private List<String> getClassNameList(List<Class<?>> clsLst) {
		List<String> tempList = new ArrayList<String>();
		
		for (Class<?> cls : clsLst) {
			 tempList.add(cls.getSimpleName());			 
		}
		return tempList;
	}
	
	private void fillGUItable(){
		int count = 0;
		data = new Object[rowList.size()/cols.length][cols.length];
		
		for (int x = 0; x < rowList.size()/cols.length; x++) //run 5 times
		{
		    for (int y = 0; y < cols.length; y++) //
		    {
		    	data[x][y] = rowList.get(count++);
		    }
		}
	}
	
	public int getColumnCount() {
		return cols.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return cols[col];
	}

	public Object getValueAt(int row, int col) {
		//System.out.println(matrix[row][col]);
		return data[row][col];
	}

	public Class<?> getColumnClass(int c) {
		
		return getValueAt(0, c).getClass();
	}
}