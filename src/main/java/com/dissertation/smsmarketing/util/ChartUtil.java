package com.dissertation.smsmarketing.util;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dissertation.smsmarketing.model.HistoricalPriceModel;

public class ChartUtil {

	private static Logger log = LoggerFactory.getLogger("CharUtil");
	public static BufferedImage createDataset(Map<String,List<HistoricalPriceModel>> stockData) throws IOException, NumberFormatException, ParseException {

		SimpleDateFormat inputdateformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outputdateformat = new SimpleDateFormat("MMM yyyy");
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// create the dataset
		List<HistoricalPriceModel> stockDataList = new ArrayList<HistoricalPriceModel>();
		
		for (Map.Entry<String, List<HistoricalPriceModel>> iterable_element : stockData.entrySet()) {
			stockDataList = iterable_element.getValue();
			String stockName=iterable_element.getKey();
			for (int i = 0; i < stockDataList.size(); i++) {
	//			dataset.addValue(Double.parseDouble(historicalPriceModel.get(i).getAdjClose()), stockData.get(""), outputdateformat.format(inputdateformat.parse(historicalPriceModel.get(i).getAllDate())));
				dataset.addValue(Double.parseDouble(stockDataList.get(i).getAdjClose()),stockName, outputdateformat.format(inputdateformat.parse(stockDataList.get(i).getAllDate())));
			}
		}
		final JFreeChart chart = createChart(dataset);
		BufferedImage image = generateChart(chart);
		return image;
	}

	public static JFreeChart createChart(final CategoryDataset dataset) {
		final JFreeChart chart = ChartFactory.createBarChart("STOCK INFORMATION", // chart
				"MONTHS", // domain axis label
				"STOCK PRICE (USD)", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		
		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		/*Font font = new Font("Dialog", Font.PLAIN, 30);
		rangeAxis.setTicklabelFont(font);*/
//		rangeAxis.setTickLabelFont(Font.getFont("SANS_SERIF"));

		// disable bar outlines...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		
		// set up gradient paints for series...
		final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
				0.0f, 0.0f, Color.blue);
		/*final GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
				0.0f, 0.0f, Color.lightGray);*/
		renderer.setSeriesPaint(0, gp0);
		/*renderer.setSeriesPaint(1, gp1);
		renderer.setSeriesPaint(2, gp2);*/

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		return chart;
	}

	public static BufferedImage generateChart(JFreeChart chart) throws IOException {
//		ChartUtilities.saveChartAsJPEG(new File("d:/chart.jpg"), chart, 500, 300);
		BufferedImage objBufferedImage = chart.createBufferedImage(400, 400);
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		try {
			ImageIO.write(objBufferedImage, "jpg", bas);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] byteArray = bas.toByteArray();
		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage image = ImageIO.read(in);
//		File outputfile = new File("d:/image.png");
//		ImageIO.write(image, "png", outputfile);
//		return "Image Generated";
		return image;
	}
}