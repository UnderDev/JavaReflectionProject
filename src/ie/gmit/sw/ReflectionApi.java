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
	
	
	private static HashSet<Class> afferentSet = new HashSet<Class>();
	private static HashSet<Class> efferentSet = new HashSet<Class>();

	private static double afferent = 0;
	private static double efferent = 0;
	private static double posStability = 0;

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

			//new ReflectionApi(cls);

			buildGraph(cls);
		}

		getRelatedClasses();
		// --------------- CALCUALTE AFFERENT COUPLINGS -> CA
		// --------------- CALCUALTE EFFERENT COUPLINGS -> CE
		// --------------- CALCULATE STABILITY FOR CLASS -> (CE / CA+CE)
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

	private static void getRelatedClasses() {

		// For Each Loop to get all the keys from the HashMap
		for (Entry<Class, List<Class>> entry : graph.entrySet()) {

			// Create a new instance of an ArrayList<Class>
			classList = new ArrayList<Class>();

			// Loop over each class in the temporary List of classes
			for (Class<?> c : tempClassList) {

				/*
				 * Check to see if the current class c "Looped" in tempClassList
				 * has and instance of the current class from the graph HashMap.
				 * Afferent + Efferent And dont count Entries of the same name
				 * eg. class1 = class1
				 */
				if ((c.isAssignableFrom(entry.getKey()) || entry.getKey().isAssignableFrom(c)) && c != entry.getKey()) {

					if (c.isAssignableFrom(entry.getKey())){
						afferent++;
						afferentSet.add(c);
					}
					
					else{
						efferent++;
						efferentSet.add(c);
					}

					// Add class to list
					classList.add(c);

					// Update/overWrite the hashMap every time the list gets
					// updated
					graph.put(entry.getKey(), classList);
				}
				
				printFields(entry,c);
				
				
				
				
				
				

			}
			getStability(entry.getKey());// Stability Per Class
		}
		// getStability();//Stability Per Jar

	}

	private static void getStability(Class entry) {

		// try catch in case both CA / CE = 0
		try {
			posStability = efferent / (afferent + efferent );
			if (posStability != posStability)// gets rid of NaN Value
				posStability = 0;
		} catch (Exception e) {
		}

		afferent = 0;
		efferent = 0;

		System.out.println("\nClass Name : " + entry.getName() + "\nStability = " + posStability);
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
			printInterfaces();
			printConstructors();
			//printFields();
			printMethods();
		} catch (NoClassDefFoundError e) {
		}
	}

	private void printConstructors() throws NoClassDefFoundError {
		Constructor<?> ctorlist[] = c.getDeclaredConstructors();
		System.out.println("-------------- " + ctorlist.length + " Constructors --------------");
		for (int i = 0; i < ctorlist.length; i++) {
			Constructor<?> ct = ctorlist[i];
			System.out.println("\tname  = " + ct.getName());
			System.out.println("\tdecl class = " + ct.getDeclaringClass());

			Class<?> pvec[] = ct.getParameterTypes();
			for (int j = 0; j < pvec.length; j++) {
				System.out.println("\tparam #" + j + " " + pvec[j]);
			}

			Class<?> evec[] = ct.getExceptionTypes();
			for (int j = 0; j < evec.length; j++) {
				System.out.println("\texc #" + j + " " + evec[j]);
			}
		}
	}

	private static void printFields(Entry<Class, List<Class>> entry, Class<?> c2) {
		Field fieldlist[] = c2.getDeclaredFields();
		//System.out.println("\n\t------ " + fieldlist.length + " Fields ------");
		for (int i = 0; i < fieldlist.length; i++) {
			Field fld = fieldlist[i];
			//System.out.println("\tname = " + fld.getName());
			System.out.println("\tdecl class = " + fld.getDeclaringClass());
			System.out.println("\ttype = " + fld.getType());
			int mod = fld.getModifiers();
			//System.out.println("\tmodifiers = " + Modifier.toString(mod));
			//System.out.println("\t-----\n");	
			if(fld.getType().toString().startsWith("class java.")) 
				continue;
			
			if(fld.getType().isAssignableFrom(entry.getKey())){
				efferent++;
				efferentSet.add(fld.getType());
				System.out.println(efferentSet);
			}
			
			
			if(entry.getKey().isAssignableFrom(fld.getType())){
				afferent++;
				afferentSet.add(fld.getType());
				System.out.println(afferentSet);
			}
		}
	}

	private void printMethods() {
		Method methlist[] = c.getDeclaredMethods();
		System.out.println("-------------- " + methlist.length + " Methods --------------");
		for (int i = 0; i < methlist.length; i++) {
			Method m = methlist[i];
			System.out.println("\tname = " + m.getName());
			System.out.println("\tdecl class = " + m.getDeclaringClass());
			Class<?> pvec[] = m.getParameterTypes();
			for (int j = 0; j < pvec.length; j++) {
				System.out.println("\tparam #" + j + " " + pvec[j]);
			}
			Class<?> evec[] = m.getExceptionTypes();
			for (int j = 0; j < evec.length; j++) {
				System.out.println("\texc #" + j + " " + evec[j]);
			}
			System.out.println("\treturn type = " + m.getReturnType());
			System.out.println("\t-----\n");
		}
	}

	private void printInterfaces() {
		Class[] inf = c.getInterfaces();
		System.out.println("\n-------------- " + inf.length + " InterFaces ---------------");
		for (int i = 0; i < inf.length; i++) {
			Class<?> m = inf[i];
			System.out.println("\tInterface Name = " + m.getName() + "\n");
		}
	}

}