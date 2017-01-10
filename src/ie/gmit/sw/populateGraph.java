package ie.gmit.sw;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class populateGraph {

	private List<Class<?>> classList;
	private Map<Class<?>, List<Class<?>>> graph = JarReader.getGraph();
	
	public void getRelatedClasses() {	
		// For Each Loop to get all the keys from the HashMap
		for (Entry<Class<?>, List<Class<?>>> entry : graph.entrySet()) {

			// Create a new instance of an ArrayList<Class>
			classList = new ArrayList<Class<?>>();

			try {
				scanFields(entry);
				scanMethods(entry);
				scanConstructors(entry);
				scanInterfaces(entry);
			} catch (NoClassDefFoundError e) {
			}

		}
		//efferentSet = graph;
		//stab.setEfferentSet(graph);
		//stab.fillCeCaLists();
		//getStability();// Stability Per Class
	}

	private void scanConstructors(Entry<Class<?>, List<Class<?>>> entry) throws NoClassDefFoundError {
		Constructor<?> ctorlist[] = entry.getKey().getDeclaredConstructors();

		for (int i = 0; i < ctorlist.length; i++) {
			Constructor<?> ct = ctorlist[i];

			Class<?> pvec[] = ct.getParameterTypes();
			for (int j = 0; j < pvec.length; j++) {
				if (pvec[j].toString().startsWith("class java.") || pvec[j].isPrimitive() || pvec[j].isArray()
						|| classList.contains(pvec[j]))
					continue;

				classList.add(pvec[j]);
				graph.put(entry.getKey(), classList);
			}
		}
	}

	private void scanFields(Entry<Class<?>, List<Class<?>>> entry) throws NoClassDefFoundError {

		Field fieldlist[] = entry.getKey().getDeclaredFields();
		// System.out.println("\n\t------ " + fieldlist.length + " Fields
		// ------");

		for (int i = 0; i < fieldlist.length; i++) {
			Field fld = fieldlist[i];
			if (fld.getType().toString().startsWith("class java.") || fld.getType().isPrimitive()
					|| fld.getType().isArray())
				continue;

			classList.add(fld.getType());
			graph.put(entry.getKey(), classList);
		}
	}

	private void scanMethods(Entry<Class<?>, List<Class<?>>> entry) throws NoClassDefFoundError {
		Method methlist[] = entry.getKey().getDeclaredMethods();

		for (int i = 0; i < methlist.length; i++) {
			Method m = methlist[i];
			// System.out.println("\tname = " + m.getName());
			// System.out.println("\tdecl class = " + m.getDeclaringClass());
			Class<?> pvec[] = m.getParameterTypes();

			// ----------------------------------- PARAMS
			// ------------------------------------------------
			for (int j = 0; j < pvec.length; j++) {
				if (pvec[j].getName().startsWith("java.") || classList.contains(pvec[j]) || pvec[j].isArray()
						|| pvec[j].isPrimitive())
					continue;

				classList.add(pvec[j]);
				graph.put(entry.getKey(), classList);
			}

			// ----------------------------------- RETURN TYPES
			// -------------------------------------------
			if (m.getReturnType().getName().startsWith("java.") || classList.contains(m.getReturnType())
					|| m.getReturnType().isArray() || m.getReturnType().isPrimitive())
				continue;

			classList.add(m.getReturnType());
			graph.put(entry.getKey(), classList);
		}
	}

	private void scanInterfaces(Entry<Class<?>, List<Class<?>>> entry) {
		Class<?>[] inf = entry.getKey().getInterfaces();
		for (int i = 0; i < inf.length; i++) {
			Class<?> m = inf[i];

			// System.out.println("\tInterface Name = " + m.getName() + "\n");
			if (inf[i].getName().startsWith("java."))
				continue;

			if ((inf[i].isAssignableFrom(entry.getKey()) || entry.getKey().isAssignableFrom(inf[i]))
					&& inf[i] != entry.getKey()) {
				classList.add(inf[i]);
				graph.put(entry.getKey(), classList);
			}
		}
	}

}
