package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Stability {

	private Map<Class<?>, List<Class<?>>> graph = JarReader.getGraph();

	private Map<Class<?>, Double> afferentMapTotal = new HashMap<Class<?>, Double>();
	private Map<Class<?>, Double> efferentMapTotal = new HashMap<Class<?>, Double>();

	private Map<Class<?>, List<Class<?>>> afferentSet = new HashMap<Class<?>, List<Class<?>>>();
	private Map<Class<?>, List<Class<?>>> efferentSet ;
	
	public void setEfferentSet(Map<Class<?>, List<Class<?>>> efferentSet) {
		this.efferentSet = efferentSet;
	}

	public Map<Class<?>, List<Class<?>>> getAfferentSet() {
		return afferentSet;
	}

	public Map<Class<?>, List<Class<?>>> getEfferentSet() {
		return efferentSet;
	}

	
	

	public void fillCeCaLists() {
		List<Class<?>> classList;
		for (Entry<Class<?>, List<Class<?>>> entry : efferentSet.entrySet()) {

			for (Entry<Class<?>, List<Class<?>>> c : graph.entrySet()) {

				if (entry.getValue().contains(c)) {
					classList = new ArrayList<Class<?>>();
					classList.add(entry.getKey());

					if (afferentSet.containsKey(c) && !afferentSet.containsValue(c))
						classList.addAll(afferentSet.get(c));

					afferentSet.put(c.getKey(), classList);
					afferentMapTotal.put(c.getKey(), (double) classList.size());
				}
			}
			efferentMapTotal.put(entry.getKey(), (double) entry.getValue().size());
		}
	}

	public double calculateStability(Entry<Class<?>, List<Class<?>>> cls) {
		double Ca = 0;
		double Ce = 0;
		double posStability = 0;

		//for (Entry<Class<?>, List<Class<?>>> cls : graph.entrySet()) {

			if (afferentMapTotal.get(cls) != null)
				Ca = afferentMapTotal.get(cls).doubleValue();

			if (efferentMapTotal.get(cls) != null)
				Ce = efferentMapTotal.get(cls).doubleValue();

			posStability = Ce / (Ca + Ce);
			if (posStability != posStability)
				posStability = 0;

			System.out.println("\nClass Name : " + cls.getKey().getName() + "\nStability = " + posStability);
		//}
			return posStability;
	}

}
