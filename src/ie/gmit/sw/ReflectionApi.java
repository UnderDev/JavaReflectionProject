package ie.gmit.sw;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionApi {
	private Class<?> c;

	private static Map<Class, List<Class>> graph = new HashMap<Class, List<Class>>();

	// New folder/G00316578/string-service
	private static String pathToJar = "C:/Users/scott/Desktop/TestJar.jar";

	private static List<Class> tempClassList = new ArrayList<Class>();
	private static List<Class> classList;

	private static Map<Class, List<Class>> afferentSet = new HashMap<Class, List<Class>>();
	// private static HashSet<Class> afferentSet = new HashSet<Class>();

	private static Map<Class, List<Class>> efferentSet = new HashMap<Class, List<Class>>();
	// private static HashSet<Class> efferentSet = new HashSet<Class>();

	// private static double afferent = 0;
	private static Map<Class, Double> afferentMapTotal = new HashMap<Class, Double>();
	private static Map<Class, Double> efferentMapTotal = new HashMap<Class, Double>();

	public static void main(String args[]) throws Exception {
		Class<?> cls = null;
		int subStrLen = 6;

		@SuppressWarnings("resource")
		JarFile jarFile = new JarFile(pathToJar);
		Enumeration<JarEntry> item = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		while (item.hasMoreElements()) {
			JarEntry je = item.nextElement();

			if (je.isDirectory() || !je.getName().endsWith(".class")) {
				continue;
			}

			String className = je.getName().substring(0, je.getName().length() - subStrLen);
			className = className.replace('/', '.');
			try {
				cls = cl.loadClass(className);
			} catch (ClassNotFoundException e1) {
			} catch (NoClassDefFoundError e) {
			}

			// --------------- LOOP THROUGH ALL THE CLASSES --------------
			// --------------- ADD THE CLASS(KEY) TO THE MAP + ANY ASSOIATED
			// CLASSES(LIST)

			// new ReflectionApi(cls);

			buildGraph(cls);
		}

		getRelatedClasses();

		System.out.println("Finished");
	}

	public ReflectionApi(Class<?> c) {
		super();
		this.c = c;

		printReflection(c);
	}

	public static void buildGraph(Class cls) {
		// Check the list for the Class passed in.
		classList = graph.get(cls);

		// If classList is empty, Add to graph the new Class + an empty List
		if (classList == null)
			graph.put(cls, classList = new ArrayList<Class>());

		tempClassList.add(cls);
	}

	@SuppressWarnings("unchecked")
	private static void getRelatedClasses() {

		// For Each Loop to get all the keys from the HashMap
		for (Entry<Class, List<Class>> entry : graph.entrySet()) {

			// Create a new instance of an ArrayList<Class>
			classList = new ArrayList<Class>();

			try {
				scanFields(entry);
				scanMethods(entry);
				scanConstructors(entry);
				scanInterfaces(entry);
			} catch (NoClassDefFoundError e) {
			}

		}
		efferentSet = graph;
		fillCeCaLists();
		getStability();// Stability Per Class
	}

	private static void getStability() {
		double Ca = 0;
		double Ce = 0;
		double posStability = 0;
		for (Class cls : tempClassList) {

			if (afferentMapTotal.get(cls) != null)
				Ca = afferentMapTotal.get(cls).doubleValue();

			if (efferentMapTotal.get(cls) != null)
				Ce = efferentMapTotal.get(cls).doubleValue();

			posStability = Ce / (Ca + Ce);
			if (posStability != posStability)
				posStability = 0;

			System.out.println("\nClass Name : " + cls.getName() + "\nStability = " + posStability);
		}
	}

	/*
	 * private static void getStability() {
	 * 
	 * // try catch in case CA / CE both = 0 try { float num = efferent +
	 * afferent; posStability = (efferent / num); } catch (Exception e) {
	 * System.out.println("oops"); }
	 * 
	 * afferent = 0; efferent = 0;
	 * 
	 * System.out.println("\nStability = " + posStability); }
	 */
	private void printReflection(Class<?> c) {
		Package pack = c.getPackage(); // Get the package
		boolean iface = c.isInterface(); // Is it an interface?

		System.out.println("\nClass Name: " + c.getName().substring(pack.getName().length() + 1));
		System.out.println("Package: " + pack.getName());
		System.out.println("Iterface?: " + c.isInterface());
		try {
			// printInterfaces();
			// printConstructors();
			// printFields();
			// printMethods();
		} catch (NoClassDefFoundError e) {
		}
	}

	
	
	private static void scanConstructors(Entry<Class, List<Class>> entry) throws NoClassDefFoundError {
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

	private static void scanFields(Entry<Class, List<Class>> entry) throws NoClassDefFoundError {

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

	private static void scanMethods(Entry<Class, List<Class>> entry) throws NoClassDefFoundError {
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

	private static void scanInterfaces(Entry<Class, List<Class>> entry) {
		Class[] inf = entry.getKey().getInterfaces();
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

	private static void fillCeCaLists() {

		for (Entry<Class, List<Class>> entry : efferentSet.entrySet()) {
			for (Class<?> c : tempClassList) {

				if (entry.getValue().contains(c)) {
					classList = new ArrayList<Class>();
					classList.add(entry.getKey());
					
					if (afferentSet.containsKey(c) && !afferentSet.containsValue(c))
						classList.addAll(afferentSet.get(c));

					afferentSet.put(c, classList);
					afferentMapTotal.put(c, (double) classList.size());
				}
			}
			efferentMapTotal.put(entry.getKey(), (double) entry.getValue().size());
		}
	}
}