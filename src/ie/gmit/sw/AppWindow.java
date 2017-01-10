package ie.gmit.sw;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

/***
 * 
 * @author Scott Coyne Class AppWindow, Creates a GUI using javax.Swing
 *         components. The GUI allows the user to browse there computer for a
 *         jar file, read it in, and provide Statistics based on the information
 *         passed in.
 */

public class AppWindow {
	private JFrame frame;
	private String filePath;
	private JButton btnBarchart;
	private JButton btnDialog;
	private JButton btnImport;

	public AppWindow() {

		// Create a window for the application
		frame = new JFrame();
		frame.setTitle("B.Sc. in Software Development - GMIT");
		frame.setSize(550, 200);
		frame.setResizable(false);
		frame.setLayout(new FlowLayout());
		frame.setLocationRelativeTo(null);

		// The file panel will contain the file chooser
		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEADING));
		top.setBorder(new javax.swing.border.TitledBorder("Select A Jar File"));
		top.setPreferredSize(new java.awt.Dimension(500, 70));
		top.setMaximumSize(new java.awt.Dimension(500, 70));
		top.setMinimumSize(new java.awt.Dimension(500, 70));

		final JTextField txtFileName = new JTextField(20);
		txtFileName.setPreferredSize(new java.awt.Dimension(100, 30));
		txtFileName.setMaximumSize(new java.awt.Dimension(100, 30));
		txtFileName.setMargin(new java.awt.Insets(2, 2, 2, 2));
		txtFileName.setMinimumSize(new java.awt.Dimension(100, 30));

		JButton btnChooseFile = new JButton("Browse...");
		btnChooseFile.setToolTipText("Please Choose A Jar File");
		btnChooseFile.setPreferredSize(new java.awt.Dimension(90, 30));
		btnChooseFile.setMaximumSize(new java.awt.Dimension(90, 30));
		btnChooseFile.setMargin(new java.awt.Insets(2, 2, 2, 2));
		btnChooseFile.setMinimumSize(new java.awt.Dimension(90, 30));
		btnChooseFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JFileChooser fc = new JFileChooser("./");
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					btnImport.setEnabled(true);// Enable Btn
					File file = fc.getSelectedFile().getAbsoluteFile();
					String name = file.getAbsolutePath();
					txtFileName.setText(name);
					filePath = name;
					System.out.println("You selected the following file: " + name);
				}
			}
		});

		btnImport = new JButton("Import");
		btnImport.setEnabled(false);
		btnImport.setToolTipText("Import");
		btnImport.setPreferredSize(new java.awt.Dimension(150, 30));
		btnImport.setMaximumSize(new java.awt.Dimension(150, 30));
		btnImport.setMargin(new java.awt.Insets(2, 2, 2, 2));
		btnImport.setMinimumSize(new java.awt.Dimension(150, 30));
		btnImport.addActionListener(new java.awt.event.ActionListener() {

			/***
			 * -------------- READ IN JAR FILE -------------- Once VALID jar is
			 * read in, it enables all the other btns on the GUI
			 */

			public void actionPerformed(ActionEvent evt) {
				JarReader readJar = new JarReader();

				try {
					readJar.readInJar(filePath);

					// Set Viability on Btns To True
					btnBarchart.setEnabled(true);
					btnDialog.setEnabled(true);
					populateGraph populate = new populateGraph();
					populate.getRelatedClasses();
				} catch (Exception e) {
					System.out.println("Error not a jar");
					JOptionPane.showMessageDialog(null, "Incorrect Filepath: " + filePath);
				}
			}
		});
		top.add(txtFileName);
		top.add(btnChooseFile);
		top.add(btnImport);

		// -------------- Add New JFrame --------------
		frame.getContentPane().add(top); // Add the panel to the window
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bottom.setBorder(new EmptyBorder(10, 10, 10, 10));
		bottom.setPreferredSize(new java.awt.Dimension(500, 70));
		bottom.setMaximumSize(new java.awt.Dimension(500, 70));
		bottom.setMinimumSize(new java.awt.Dimension(500, 70));

		// -------------- Stability Table --------------
		btnDialog = new JButton("Show Stability Table");
		btnDialog.setEnabled(false);
		btnDialog.addActionListener(new java.awt.event.ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent evt) {
				TypeSummaryTableModel tstm = new TypeSummaryTableModel();
				tstm.gatherData();
				AppSummary as = new AppSummary(frame, true);
				as.show();
			}
		});

		// -------------- BAR CHART BUTTON --------------
		btnBarchart = new JButton("BarChart"); // Create BarChart button
		btnBarchart.setEnabled(false);
		btnBarchart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				BarChartData data = new BarChartData();
				JFrame bcFrame = new JFrame("Bar Chart");
				bcFrame.setSize(1400, 500);// Window Size

				// Add New JScrollPane with the BarChart passed in
				JScrollPane jsp = new JScrollPane(data.fillBarChartData());
				jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

				// Get the New Bar chart
				bcFrame.add(jsp);
				bcFrame.setVisible(true);
			}
		});

		// -------------- Add Quit Button --------------
		JButton btnQuit = new JButton("Quit"); // Create Quit button
		btnQuit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		bottom.add(btnDialog);
		bottom.add(btnBarchart);
		bottom.add(btnQuit);

		frame.getContentPane().add(bottom);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new AppWindow();
	}
}