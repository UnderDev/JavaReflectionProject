package ie.gmit.sw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

/**
 * 
 * @author scott
 * class BarChart, extends JPanel and implements methods. Class is used to
 *        Create a new BarChart and populate it with Data from a Jar Read in
 *        previously.
 */
public class BarChart extends JPanel {
	private static final long serialVersionUID = 1L;
	private List<Double> values;
	private List<String> labels;
	private List<Color> colors;
	private String title;

	public BarChart(List<Double> values2, List<String> labels2, List<Color> colors2, String title) {
		this.labels = labels2;
		this.values = values2;
		this.colors = colors2;
		this.title = title;
	}

	/**
	 * Method is used to Create/Set dimensions and populate a BarChart Adapted
	 *         From http://www.javacodex.com/Graphics/Bar-Chart
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (values == null || values.size() == 0) {
			return;
		}

		double minValue = 0;
		double maxValue = 0;
		for (int i = 0; i < values.size(); i++) {
			if (minValue > values.get(i)) {
				minValue = values.get(i);
			}
			if (maxValue < values.get(i)) {
				maxValue = values.get(i);
			}
		}

		Dimension dim = getSize();
		int panelWidth = dim.width;
		int panelHeight = dim.height;
		int barWidth = panelWidth / values.size();

		// Title Font
		Font titleFont = new Font("Book Antiqua", Font.BOLD, 15);
		FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);

		// Label Font
		Font labelFont = new Font("Book Antiqua", Font.PLAIN, 14);
		FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

		int titleWidth = titleFontMetrics.stringWidth(title);
		int stringHeight = titleFontMetrics.getAscent();
		int stringWidth = (panelWidth - titleWidth) / 2;
		g.setFont(titleFont);
		g.drawString(title, stringWidth, stringHeight);

		int top = titleFontMetrics.getHeight();
		int bottom = labelFontMetrics.getHeight();
		if (maxValue == minValue) {
			return;
		}
		double scale = (panelHeight - top - bottom) / (maxValue - minValue);
		stringHeight = panelHeight - labelFontMetrics.getDescent();
		g.setFont(labelFont);

		for (int j = 0; j < values.size(); j++) {
			int valueP = j * barWidth + 1;
			int valueQ = top;
			int height = (int) (values.get(j) * scale);
			if (values.get(j) >= 0) {
				valueQ += (int) ((maxValue - values.get(j)) * scale);
			} else {
				valueQ += (int) (maxValue * scale);
				height = -height;
			}

			g.setColor(colors.get(j));
			g.fillRect(valueP, valueQ, barWidth - 2, height);
			g.setColor(Color.black);
			g.drawRect(valueP, valueQ, barWidth - 2, height);

			int labelWidth = labelFontMetrics.stringWidth(labels.get(j));
			stringWidth = j * barWidth + (barWidth - labelWidth) / 2;
			g.drawString(labels.get(j), stringWidth, stringHeight);
		}
	}
}