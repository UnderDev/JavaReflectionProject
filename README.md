Measuring Stability Using the Reflection API 
==============================================
	
Developed By Scott Coyne.


Introduction
============
The following Java application uses reflection to analyse an arbitrary Java
Application Archive (JAR) and calculates the positional stability of each of the component
classes in its object graph. 

This data is then displayed in a GUI using the java Swing library.
The user will be able to read in a Jar file and calculate the stability of the jar,
displayed in either a JTable or a JBarChart.


Main Features
===============
- Application Creates a UI for the user.
- Application Only Accepts Jar Files.
- BarChart Displaying stability per class.

Running the Program
===================

1) Open up CMD.

2) CD into the SRC Directory Containing all the .java files

3) Run the Following CMD to compile the program: javac ie\gmit\sw\*.java

4) Run the Following CMD to run the Application:  java ie/gmit/sw/Runner

5) Select a .jar file using the Application and click import.

