 package instrument;

import java.awt.Font;
import javax.swing.ImageIcon;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import data.MapCharacteristics;

public class ChartManager {
	/**
	 * 
	 */
	public ChartManager() {
		
	}
	
	public ImageIcon getPie(MapCharacteristics mc) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Ant", mc.getNbrAntsInTheMap());
		dataset.setValue("Predator", mc.getNbrPredatorsIntheMap());
		dataset.setValue("Small food", mc.getNbrSmallFood());
		dataset.setValue("Big food", mc.getNbrBigFood());
		dataset.setValue("Black rock", mc.getNbrBlackRock());
		dataset.setValue("Green rock", mc.getNbrGreenRock());
		
		JFreeChart chart =ChartFactory.createPieChart("Elements in the map", dataset, true, false, false);
		PiePlot plot = (PiePlot) chart.getPlot();
	    plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
	    plot.setNoDataMessage("No data available");
	    plot.setCircular(true);
	    plot.setIgnoreZeroValues(true);
	    plot.setLabelGap(0.02);

	    return new ImageIcon(chart.createBufferedImage(300,200));
	}
	
	
	public JFreeChart getGraphBar(MapCharacteristics mc) {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.setValue(mc.getNbrAntsInTheMap(),"Ant","Lives ants");
		dataset.setValue(mc.getDeathToll(), "Ant", "Dead ants");
		dataset.setValue(mc.getNbrCapturedFood(),"Food","Captured food");
		
		return ChartFactory.createBarChart("Global statistics ", "Category", "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
	    
	
	}
}
