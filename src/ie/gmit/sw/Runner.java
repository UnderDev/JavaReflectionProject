package ie.gmit.sw;

/**
 * 
 * @author Scott Coyne
 * Runner, Contains the Main method for running the application. It
 *        Creates a new Runnable AppWindow GUI
 */
public class Runner {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new AppWindow();
			}
		});
	}
}
