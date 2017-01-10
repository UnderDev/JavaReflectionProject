package ie.gmit.sw;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * 
 * @author Scott Coyne
 * class BarChartData, is used to populate the ArrayList values,labels,colors
 *        from the HashMap graph from class JarReader. Pass the List to the
 *        Constructor of the class BarChart and return the new component
 */
public class BarChartData {

	private static Map<Class<?>, List<Class<?>>> graph;

	/**
	 * Method fillBarChartData, Populates all the Lists needed with data to
	 *         Create a new BarChart
	 * @return Component.
	 */
	public Component fillBarChartData() {
		Random rand = new Random();
		graph = JarReader.getGraph();

		String title = "Stability Bar Chart";
		List<Double> values = new ArrayList<Double>();
		List<String> labels = new ArrayList<String>();
		List<Color> colors = new ArrayList<Color>();

		for (Entry<Class<?>, List<Class<?>>> entry : graph.entrySet()) {
			values.add((double) entry.getValue().size());
			labels.add(entry.getKey().getSimpleName());

			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			Color randomColor = new Color(r, g, b);

			colors.add(randomColor);
		}
		BarChart bc = new BarChart(values, labels, colors, title);
		return bc;// Return the new component
	}
}
