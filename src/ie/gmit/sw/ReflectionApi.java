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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionApi {
	private Class<?> c;
	private static Map<Class, List<Class>> graph = new HashMap<Class, List<Class>>();
	private static String pathToJar = "C:/Users/scott/Desktop/New folder/G00316578/string-service.jar";

	private static List<Class> tempClassList = new ArrayList<Class>();
	private static List<Class> list;

	private static int posStability = 0;

	public static void main(String args[]) throws Exception {
		Class<?> cls = null;
		JarFile jarFile = new JarFile(pathToJar);
		Enumeration<JarEntry> item = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		while (item.hasMoreElements()) {
			JarEntry je = item.nextElement();
			if (je.isDirectory() || !je.getName().endsWith(".class")) {
				continue;
			}
			// -6 because of .class
			String className = je.getName().substring(0, je.getName().length() - 6);
			className = className.replace('/', '.');
			cls = cl.loadClass(className);

			// --------------- LOOP THROUGH ALL THE CLASSES --------------
			// --------------- ADD THE CLASS(KEY) TO THE MAP + ANY ASSOIATED
			// CLASSES(LIST)

			new ReflectionApi(cls);
			addToGraph(cls);
		}

		getRelatedClasses();
		// --------------- CALCUALTE AFFERENT COUPLINGS -> CA
		// --------------- CALCUALTE EFFERENT COUPLINGS -> CE
		// --------------- CALCULATE STABILITY FOR CLASS -> (CE / CA+CE)
		System.out.println("STUFF");
	}

	public static void addToGraph(Class cls) {
		// Check the list for the Class passed in.
		list = graph.get(cls);

		// If class is empty, Add to the graph the new Class + an empty List
		if (list == null)
			graph.put(cls, list = new ArrayList<Class>());
		tempClassList.add(cls);
	}

	private static void getStability() {

		int afferent=0,efferent=0;
		
		for (Entry<Class, List<Class>> entry : graph.entrySet()) {
			
			for(Class c: entry.getValue()){
				if (c.getName().startsWith("CE"))
					
				else
			}
			System.out.println("Class Name : " + entry.getKey() + " Stability = "+posStability);
		}

	}

	private static void getRelatedClasses() {

		// Loop to get all the keys from the HashMap
		for (Entry<Class, List<Class>> entry : graph.entrySet()) {

			// Create a new instance of an ArrayList<Class>
			list = new ArrayList<Class>();

			// Loop over each class in the temporary List of classes
			for (Class c : tempClassList) {

				// Check to see if the current class has and instance of
				// class(entry) from
				if (c.isAssignableFrom(entry.getKey()) && c != entry.getKey()) {
					// Add class to list
					list.add(c);

					// Update/overWrite the hashMap every time the list gets
					// updated
					graph.put(entry.getKey(), list);
				}

			}
		}

	}

	public ReflectionApi(Class<?> c) {
		super();
		this.c = c;

		Package pack = c.getPackage(); // Get the package
		boolean iface = c.isInterface(); // Is it an interface?

		System.out.println("\nClass Name: " + c.getName().substring(c.getPackage().getName().length() + 1));
		System.out.println("Package: " + c.getPackage().getName());
		System.out.println("Iterface?: " + c.isInterface());
		printInterfaces();
		printConstructors();
		printFields();
		printMethods();
		// createArray();
	}

	public void printConstructors() {
		Constructor<?> ctorlist[] = c.getDeclaredConstructors();
		System.out.println("--------------" + ctorlist.length + " Constructors --------------");
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
			System.out.println("\t-----");
		}
	}

	public void printFields() {
		Field fieldlist[] = c.getDeclaredFields();
		for (int i = 0; i < fieldlist.length; i++) {
			Field fld = fieldlist[i];
			System.out.println("\tname = " + fld.getName());
			System.out.println("\tdecl class = " + fld.getDeclaringClass());
			System.out.println("\ttype = " + fld.getType());
			int mod = fld.getModifiers();
			System.out.println("\tmodifiers = " + Modifier.toString(mod));
			System.out.println("\t-----");
		}
	}

	public void printMethods() {
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
			System.out.println("\t-----");
		}
	}

	public void printInterfaces() {
		Class[] inf = c.getInterfaces();
		System.out.println("\n-------------- " + inf.length + " InterFaces --------------");
		for (int i = 0; i < inf.length; i++) {
			Class<?> m = inf[i];
			System.out.println("\tInterface Name = " + m.getName() + "\n");
		}
	}

	// Not Used
	public void createArray() {
		try {
			Class<?> cls = Class.forName("java.lang.String");
			Object arr = Array.newInstance(cls, 10);
			Array.set(arr, 5, "Msc OO");
			String s = (String) Array.get(arr, 5);
			System.out.println(s);
		} catch (Throwable e) {
			System.err.println(e);
		}
	}
}