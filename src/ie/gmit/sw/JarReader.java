package ie.gmit.sw;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarReader {

	private static Map<Class<?>, List<Class<?>>> graph;

	public JarReader() {
		super();
	}

	public void readInJar(String pathToJar) throws Exception {
		graph = new HashMap<Class<?>, List<Class<?>>>();
		Class<?> cls = null;
		int subStrLen = 6;// 6 because of .class

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
			buildSkelGraph(cls);
		}
		System.out.println("Jar Read In and Skeloton Graph Built");
	}

	private void buildSkelGraph(Class<?> cls) {
		// Check the list for the Class passed in.
		List<Class<?>> classList = graph.get(cls);

		// If classList is empty, Add to graph the new Class + an empty List
		if (classList == null)
			graph.put(cls, classList = new ArrayList<Class<?>>());
	}

	public static Map<Class<?>, List<Class<?>>> getGraph() {
		return graph;
	}

	public Map<Class<?>, List<Class<?>>> setBeanMap() {
		return graph = new HashMap<Class<?>, List<Class<?>>>();
	}

}
