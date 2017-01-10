package ie.gmit.sw;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/***
 * 
 * @author Scott Coyne.
 * Class AppSummary, is used to create a New JTable in a new JFrame. The
 *        table's dimensions,properties,data is then added and displayed on
 *        screen.
 *
 */
public class AppSummary extends JDialog {
	private static final long serialVersionUID = 777L;
	private TypeSummaryTableModel tm = null;
	private JTable table = null;
	private JScrollPane tableScroller = null;
	private JButton btnClose = null;
	private JPanel tablePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private Container c;

	private final int MAX_WIDTH_WINDOW = 900;
	private final int MAX_HEIGHT_WINDOW = 400;

	/**
	 * 
	 * @param parent.
	 * @param modal.
	 *            Method AppSummary, Takes in JFrame, sets the dimensions,
	 *            Creates a JTable, Configures the Table,
	 */
	public AppSummary(JFrame parent, boolean modal) {
		super(parent, modal);
		super.setTitle("Summary");
		super.setResizable(true);

		this.setSize(new Dimension(MAX_WIDTH_WINDOW, MAX_HEIGHT_WINDOW));

		c = getContentPane();
		c.setLayout(new FlowLayout());

		createTable();
		configureButtonPanel();

		// Set location Relative to parent
		setLocationRelativeTo(parent);

		c.add(tablePanel);
		c.add(buttonPanel);
	}

	/**
	 * Method Creates a new JTable from class TypeSummaryTableModel(); Sets the
	 * JTables dimensions/Row colours. Adds a JScrollPane as needed
	 */
	private void createTable() {
		tm = new TypeSummaryTableModel();
		table = new JTable(tm) {
			private static final long serialVersionUID = 777L;

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				// Alternate row colour
				if (!isRowSelected(row))
					c.setBackground(row % 2 == 0 ? getBackground() : Color.getHSBColor(0.1F, 0.2F, 0.9F));
				return c;
			}
		};
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.changeSelection(0, 0, false, false);
		resizeColumnWidth(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionBackground(Color.YELLOW);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tableScroller = new JScrollPane(table);

		///////////////////////////////////////////////////////////////////
		tableScroller.setPreferredSize(new java.awt.Dimension(500, 200));
		tableScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		tablePanel.add(tableScroller, FlowLayout.LEFT);
	}

	/**
	 * 
	 * @param table
	 *            Method resizeColumnWidth, Sets the Column Widths in the JTable
	 *            according to the table contents
	 */
	public void resizeColumnWidth(JTable table) {

		final TableColumnModel columnModel = table.getColumnModel();

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 90; // Min width Per Column

			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 10, width);
			}

			if (width > 900)
				width = 900;
			columnModel.getColumn(column).setPreferredWidth(width);
			table.getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
		}
	}

	/**
	 * Method configureButtonPanel, Configures what happens on Btn close event
	 */
	private void configureButtonPanel() {
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		// Configure the Cancel button
		btnClose = new JButton("Close");
		btnClose.setToolTipText("Close this Window");
		btnClose.setPreferredSize(new java.awt.Dimension(100, 40));
		btnClose.setMaximumSize(new java.awt.Dimension(100, 40));
		btnClose.setMargin(new java.awt.Insets(2, 2, 2, 2));
		btnClose.setMinimumSize(new java.awt.Dimension(100, 40));
		btnClose.setIcon(new ImageIcon("images/close.gif"));
		btnClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dispose();
			}
		});
		buttonPanel.add(btnClose);
	}
}
