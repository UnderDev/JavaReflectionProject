package ie.gmit.sw;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class BarChartData {

	private static Map<Class<?>, List<Class<?>>> graph;
	
	public Component fillBarChartData() {
		Random rand = new Random();
		graph = JarReader.getGraph();
		
		String title = "Stability Bar Chart";
		List<Double>values = new ArrayList<Double>();
		List<String>labels = new ArrayList<String>();
		List<Color> colors = new ArrayList<Color>();

		for (Entry<Class<?>, List<Class<?>>> entry : graph.entrySet())
		{	
			values.add((double) entry.getValue().size());
			labels.add(entry.getKey().getSimpleName());
			
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			Color randomColor = new Color(r, g, b);
			
			colors.add(randomColor);
		}		
		BarChart bc = new BarChart(values, labels, colors, title);
		return bc;
	}
	

}
