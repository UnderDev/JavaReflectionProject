package ie.gmit.sw;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Scott Coyne
 * @Class populateGraph is used to populated the HashMap with a list of all Afferent/Efferent classes
 */
public class populateGraph {

	private List<Class<?>> classList;
	private Map<Class<?>, List<Class<?>>> graph = JarReader.getGraph();

	/**
	 * @Method getRelatedClasses, Calls methods scanFields,scanMethods,
	 *         scanConstructors, scanInterfaces which populated the HashMap
	 */
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
	}

	/**
	 * 
	 * @param entry
	 * @throws scanConstructors
	 * @method scanFields, Loops through all the Constructors from the class
	 *         passed in, Ignoring all primitive,arrays,java. types and adds it
	 *         to the HashMap
	 */
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

	/**
	 * 
	 * @param entry
	 * @throws NoClassDefFoundError
	 * @method scanFields, Loops through all the Fields from the class passed
	 *         in, Ignoring all primitive,arrays,java. types and adds it to the
	 *         HashMap
	 */
	private void scanFields(Entry<Class<?>, List<Class<?>>> entry) throws NoClassDefFoundError {
		Field fieldlist[] = entry.getKey().getDeclaredFields();
		for (int i = 0; i < fieldlist.length; i++) {
			Field fld = fieldlist[i];
			if (fld.getType().toString().startsWith("class java.") || fld.getType().isPrimitive()
					|| fld.getType().isArray())
				continue;

			classList.add(fld.getType());
			graph.put(entry.getKey(), classList);
		}
	}

	/**
	 * 
	 * @param entry
	 * @throws NoClassDefFoundError
	 * @method scanMethods, Loops through all the Methods from the class passed
	 *         in(Params and return types only), Ignoring all
	 *         primitive,arrays,java. types and adds it to the HashMap
	 */
	private void scanMethods(Entry<Class<?>, List<Class<?>>> entry) throws NoClassDefFoundError {
		Method methlist[] = entry.getKey().getDeclaredMethods();

		for (int i = 0; i < methlist.length; i++) {
			Method m = methlist[i];
			Class<?> pvec[] = m.getParameterTypes();

			// ------------- PARAMS -------------
			for (int j = 0; j < pvec.length; j++) {
				// Skip over if any of below is found
				if (pvec[j].getName().startsWith("java.") || classList.contains(pvec[j]) || pvec[j].isArray()
						|| pvec[j].isPrimitive())
					continue;

				classList.add(pvec[j]);
				graph.put(entry.getKey(), classList);
			}

			// ------------- RETURN TYPES -------------
			// Skip over if any of below is found
			if (m.getReturnType().getName().startsWith("java.") || classList.contains(m.getReturnType())
					|| m.getReturnType().isArray() || m.getReturnType().isPrimitive())
				continue;

			classList.add(m.getReturnType());
			graph.put(entry.getKey(), classList);
		}
	}

	/**
	 * 
	 * @param entry
	 * @throws NoClassDefFoundError
	 * @method scanInterfaces, Loops through all the interfaces from the class
	 *         passed in, and checks if it isAssignableFrom the list of other
	 *         classes. Then adds it to the HashMap
	 */
	private void scanInterfaces(Entry<Class<?>, List<Class<?>>> entry) throws NoClassDefFoundError {
		Class<?>[] inf = entry.getKey().getInterfaces();
		for (int i = 0; i < inf.length; i++) {
			Class<?> m = inf[i];

			// Skips if below is true
			if (inf[i].getName().startsWith("java."))
				continue;

			// Skips if below is false
			if ((inf[i].isAssignableFrom(entry.getKey()) || entry.getKey().isAssignableFrom(inf[i]))
					&& inf[i] != entry.getKey()) {
				classList.add(inf[i]);
				graph.put(entry.getKey(), classList);
			}
		}
	}

}
