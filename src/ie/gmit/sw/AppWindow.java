package ie.gmit.sw;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class AppWindow {
	private JFrame frame;
	private String filePath;

	public AppWindow() {

		// final String filePath;
		// Create a window for the application
		frame = new JFrame();
		frame.setTitle("B.Sc. in Software Development - GMIT");
		frame.setSize(550, 500);
		frame.setResizable(false);
		frame.setLayout(new FlowLayout());
		frame.setLocationRelativeTo(null);

		// The file panel will contain the file chooser
		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEADING));
		top.setBorder(new javax.swing.border.TitledBorder("Select Jar File"));
		top.setPreferredSize(new java.awt.Dimension(500, 100));
		top.setMaximumSize(new java.awt.Dimension(500, 100));
		top.setMinimumSize(new java.awt.Dimension(500, 100));

		final JTextField txtFileName = new JTextField(20);
		txtFileName.setPreferredSize(new java.awt.Dimension(100, 30));
		txtFileName.setMaximumSize(new java.awt.Dimension(100, 30));
		txtFileName.setMargin(new java.awt.Insets(2, 2, 2, 2));
		txtFileName.setMinimumSize(new java.awt.Dimension(100, 30));

		JButton btnChooseFile = new JButton("Browse...");
		btnChooseFile.setToolTipText("Select File to Encode");
		btnChooseFile.setPreferredSize(new java.awt.Dimension(90, 30));
		btnChooseFile.setMaximumSize(new java.awt.Dimension(90, 30));
		btnChooseFile.setMargin(new java.awt.Insets(2, 2, 2, 2));
		btnChooseFile.setMinimumSize(new java.awt.Dimension(90, 30));
		btnChooseFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JFileChooser fc = new JFileChooser("./");
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile().getAbsoluteFile();
					String name = file.getAbsolutePath();
					txtFileName.setText(name);
					filePath = name;
					System.out.println("You selected the following file: " + name);
				}
			}
		});

		JButton btnOther = new JButton("Import");
		btnOther.setToolTipText("Import");
		btnOther.setPreferredSize(new java.awt.Dimension(150, 30));
		btnOther.setMaximumSize(new java.awt.Dimension(150, 30));
		btnOther.setMargin(new java.awt.Insets(2, 2, 2, 2));
		btnOther.setMinimumSize(new java.awt.Dimension(150, 30));
		btnOther.addActionListener(new java.awt.event.ActionListener() {
			// ------------------------- READ IN JAR FILE
			// -----------------------------
			public void actionPerformed(ActionEvent evt) {
				JarReader readJar = new JarReader();
				try {
					readJar.readInJar(filePath);
				} catch (Exception e) {
					// PRINT ERROR TO USER ITS NOT A JAR
				}
				populateGraph populate = new populateGraph();
				populate.getRelatedClasses();
			}
		});

		top.add(txtFileName);
		top.add(btnChooseFile);
		top.add(btnOther);
		frame.getContentPane().add(top); // Add the panel to the window

		// A separate Panel for the programme output
		/*
		 * JPanel mid = new JPanel(new FlowLayout(FlowLayout.LEADING));
		 * mid.setBorder(new BevelBorder(BevelBorder.RAISED));
		 * mid.setPreferredSize(new java.awt.Dimension(500, 300));
		 * mid.setMaximumSize(new java.awt.Dimension(500, 300));
		 * mid.setMinimumSize(new java.awt.Dimension(500, 300));
		 */

		CustomControl cc = new CustomControl(new java.awt.Dimension(500, 300));
		cc.setBackground(Color.WHITE);
		cc.setPreferredSize(new java.awt.Dimension(300, 300));
		cc.setMaximumSize(new java.awt.Dimension(300, 300));
		cc.setMinimumSize(new java.awt.Dimension(300, 300));
		// midFrame.add(cc);
		// frame.getContentPane().add(midFrame);

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottom.setPreferredSize(new java.awt.Dimension(500, 50));
		bottom.setMaximumSize(new java.awt.Dimension(500, 50));
		bottom.setMinimumSize(new java.awt.Dimension(500, 50));

		JButton btnDialog = new JButton("Show Dialog"); // Create Show Dialog
														// button
		btnDialog.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				TypeSummaryTableModel tstm = new TypeSummaryTableModel();
				tstm.gatherData();
				AppSummary as = new AppSummary(frame, true);
				as.show();
			}
		});

		// -------------------------BAR CHART BUTTON
		// ------------------------------------
		JButton btnBarchart = new JButton("BarChart"); // Create BarChart button
		btnBarchart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				BarChartData data = new BarChartData();
				JFrame bcFrame = new JFrame("Bar Chart Example");
				bcFrame.setLocationRelativeTo(frame);
				bcFrame.setSize(350, 300);
				
				//Get the New Bar chart
				bcFrame.add(data.fillBarChartData());
				bcFrame.setVisible(true);
			}
		});

		JButton btnQuit = new JButton("Quit"); // Create Quit button
		btnQuit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		bottom.add(btnDialog);
		bottom.add(btnQuit);
		bottom.add(btnBarchart);

		frame.getContentPane().add(bottom);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new AppWindow();
	}
}