package ie.gmit.sw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.*;

/**
 * 
 * @author Scott Coyne
 * @class TypeSummaryTableModel, extends AbstractTableModel and implements its
 *        methods. The class is used to gather all the data read in from class
 *        JarReader in a Map, sort it, and fill a two dim array, which in turn
 *        fills a JTable located in the class AppSumary
 */
public class TypeSummaryTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 777L;
	private static Map<Class<?>, List<Class<?>>> graph;

	private String[] cols = { "Class Name", "Afferent", "Efferent", "Stability" };
	private List<Object> rowList;

	private static Object[][] data;

	/**
	 * @Method gatherData, is used to get the graph(containing all the classes)
	 *         find the Efferent/Afferent between them and calculate the
	 *         stability. Once it has the classes
	 *         (Name,Afferent,Efferent,Stability) it adds it to an ArrayList,
	 *         which is traversed and used to fill a JTable
	 */
	public void gatherData() {
		graph = JarReader.getGraph();
		rowList = new ArrayList<Object>();

		Stability stability = new Stability();
		stability.setEfferentSet(graph);
		stability.fillCeCaLists();

		for (Entry<Class<?>, List<Class<?>>> cls : graph.entrySet()) {
			String name = cls.getKey().getSimpleName();

			List<Class<?>> ce = stability.getEfferentSet().get(cls.getKey());
			List<Class<?>> ca = stability.getAfferentSet().get(cls.getKey());
			double stab = stability.calculateStability(cls);

			// Used to insure Afferent/Efferent lists are not Null
			if (ca == null)
				ca = new ArrayList<Class<?>>();

			if (ce == null)
				ce = new ArrayList<Class<?>>();

			rowList.add(name);
			rowList.add(getClassNameList(ca));
			rowList.add(getClassNameList(ce));
			rowList.add(stab);
		}
		// Fill the Object[][] data 2dim array with values
		fillGUItable();
	}

	/**
	 * 
	 * @param clsLst
	 * @return
	 * @Method getClassNameList, is used to loop over all of the classes and add
	 *         there SimpleName to the list tempList
	 */
	private List<String> getClassNameList(List<Class<?>> clsLst) {
		List<String> tempList = new ArrayList<String>();
		for (Class<?> cls : clsLst) {
			tempList.add(cls.getSimpleName());
		}
		return tempList;
	}

	/**
	 * @Method fillGUItable, Fills the Object[][] array with data from the
	 *         ArrayList.
	 */
	private void fillGUItable() {
		int count = 0;
		data = new Object[rowList.size() / cols.length][cols.length];

		for (int x = 0; x < rowList.size() / cols.length; x++) {
			for (int y = 0; y < cols.length; y++) //
			{
				data[x][y] = rowList.get(count++);
			}
		}
	}

	/**
	 * @Method getColumnCount, returns the Column.length
	 */
	public int getColumnCount() {
		return cols.length;
	}

	/**
	 * @Method getRowCount, returns the Row.length
	 */
	public int getRowCount() {
		return data.length;
	}

	/**
	 * @param col
	 * @Method getColumnName, returns the Column at the position passed in
	 */
	public String getColumnName(int col) {
		return cols[col];
	}

	/**
	 * @param col,row
	 * @Method getValueAt, returns the values at the positions passed in from
	 *         data
	 */
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	/**
	 * @param c
	 * @Method getColumnClass, calls the method getValueAt
	 */
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}