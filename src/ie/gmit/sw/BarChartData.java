package ie.gmit.sw;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BarChartData {

	private Map<Class<?>, List<Class<?>>> graph = JarReader.getGraph();
	public Component fillBarChartData() {
		String title = "My Title";
		List<Double>values = new ArrayList<Double>();
		List<String>labels = new ArrayList<String>();
		//double[] values = new double[10]; //{ 1, 2, 3, 4, 5 };
		//String[] labels = new String[] { "A", "B", "C", "D", "E" };
		
		for (Entry<Class<?>, List<Class<?>>> entry : graph.entrySet())
		{	
			values.add((double) entry.getValue().size());
			labels.add(entry.getKey().getSimpleName());
		}		

		Color[] colors = new Color[] { Color.blue, Color.red, Color.orange, Color.yellow, Color.green };
		BarChart bc = new BarChart(values, labels, colors, title);
		return bc;
	}
	

}
