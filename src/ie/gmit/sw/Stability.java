package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *  
 * @author Scott Coyne
 * Class Stability, Is used to Calculate the Stability of Each Class Read in By JarReader from the HashMap graph.
 * The stability = Efferent / (Efferent+afferent);
 */
public class Stability {

	private static Map<Class<?>, List<Class<?>>> graph = JarReader.getGraph();

	private Map<Class<?>, Double> afferentMapTotal = new HashMap<Class<?>, Double>();
	private Map<Class<?>, Double> efferentMapTotal = new HashMap<Class<?>, Double>();

	private Map<Class<?>, List<Class<?>>> afferentSet = new HashMap<Class<?>, List<Class<?>>>();
	private Map<Class<?>, List<Class<?>>> efferentSet ;
	
	/**
	 * Method fillCeCaLists, Fills the Efferent/Afferent Lists and the afferentMapTotal/efferentMapTotal
	 */
	public void fillCeCaLists() {
		List<Class<?>> classList;
		for (Entry<Class<?>, List<Class<?>>> entry : efferentSet.entrySet()) {
			for (Entry<Class<?>, List<Class<?>>> c : graph.entrySet()) {

				if (entry.getValue().contains(c.getKey())) {
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

	/**
	 * 
	 * @param cls
	 * @return
	 * Method Calculates the stability of a class passed in.
	 */
	public double calculateStability(Entry<Class<?>, List<Class<?>>> cls) {
		double Ca = 0.0;
		double Ce = 0.0;
		double posStability = 0.0;
			if (afferentMapTotal.get(cls.getKey()) != null)
				Ca = afferentMapTotal.get(cls.getKey()).doubleValue();

			if (efferentMapTotal.get(cls.getKey()) != null)
				Ce = efferentMapTotal.get(cls.getKey()).doubleValue();

			posStability = Ce / (Ca + Ce);
			if (posStability != posStability)
				posStability = 0;

			System.out.println("\nClass Name : " + cls.getKey().getName() + "\nStability = " + posStability);
			return posStability;
	}
	
	public void setEfferentSet(Map<Class<?>, List<Class<?>>> efferentSet) {
		this.efferentSet = efferentSet;
	}

	public Map<Class<?>, List<Class<?>>> getAfferentSet() {
		return afferentSet;
	}

	public Map<Class<?>, List<Class<?>>> getEfferentSet() {
		return efferentSet;
	}
	
	public Map<Class<?>, Double> getAfferentMapTotal() {
		return afferentMapTotal;
	}

	public Map<Class<?>, Double> getEfferentMapTotal() {
		return efferentMapTotal;
	}

}
