package ie.gmit.sw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class Introspection {

	private Map <Class<?>, List<Class<?>>> graph = new HashMap<>();
	private static String pathToJar = "C:/Users/scott/Desktop/TestJar.jar";
	
	public static void readJar(){
		try {
			JarInputStream in = new JarInputStream(new FileInputStream(new File(pathToJar)));
			JarEntry next = in.getNextJarEntry();
			
			while(next!=null)
			{
				if (next.getName().endsWith(".class")) {
					String name = next.getName().replaceAll("/", "\\.");
					name = name.replaceAll(".class", "");
					if (!name.contains("$"))
						name.substring(0, name.length() - ".class".length());
					System.out.println(name);					
				}
				next = in.getNextJarEntry();
				try {
					getClasses(next.getName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}					
	}
	
	@SuppressWarnings("rawtypes")
	public static void getClasses(String classPath) throws Exception{
		Class cls=null;
		
		JarFile jarFile = new JarFile(pathToJar);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		while (e.hasMoreElements()) {
			JarEntry je = e.nextElement();
			if (je.isDirectory() || !je.getName().endsWith(".class")) {
				continue;
			}
			
			// -6 because of .class
			String className = je.getName().substring(0, je.getName().length() - 6);
			className = className.replace('/', '.');
			cls = cl.loadClass(className);
			 
				
			    Package pack = cls.getPackage(); //Get the package
				boolean iface = cls.isInterface(); //Is it an interface?
				Class[] interfaces = cls.getInterfaces(); //Get the set of interface it implements
				Constructor[] cons = cls.getConstructors(); //Get the set of constructors
				//Class[] params = cons[i].getParameterTypes(); //Get the parameters
				Field[] fields = cls.getFields(); //Get the fields / attributes
				Method[] methods = cls.getMethods(); //Get the set of methods
				//Class c = methods[i].getReturnType(); //Get a method return type
				//Class[] params1 = methods[i].getParameterTypes(); //Get method parameters
				
				System.out.println("Pack :"+pack.getName());
				System.out.println("iface :"+iface);
				System.out.println("interfaces :"+interfaces[0].getName());
				System.out.println("cons :"+cons[0].getName());
				System.out.println("fields :"+fields[0].getName());
				System.out.println("methods :"+methods[0].getName());
			 
			 
		}
		
		
		
	}
	
	
	
	public static void main(String[] args) {
		readJar();	
	}
}
