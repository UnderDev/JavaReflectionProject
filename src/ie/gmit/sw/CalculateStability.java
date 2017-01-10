package ie.gmit.sw;

import java.util.List;
import java.util.Map;

public class CalculateStability {
	public Map<Class<?>, List<Class<?>>> graph;
	public Map<Class<?>, Double> afferentMapTotal;
	public Map<Class<?>, Double> efferentMapTotal;
	public Map<Class<?>, List<Class<?>>> afferentSet;
	public Map<Class<?>, List<Class<?>>> efferentSet;

	public CalculateStability(Map<Class<?>, List<Class<?>>> graph, Map<Class<?>, Double> afferentMapTotal,
			Map<Class<?>, Double> efferentMapTotal, Map<Class<?>, List<Class<?>>> afferentSet) {
		this.graph = graph;
		this.afferentMapTotal = afferentMapTotal;
		this.efferentMapTotal = efferentMapTotal;
		this.afferentSet = afferentSet;
	}
}