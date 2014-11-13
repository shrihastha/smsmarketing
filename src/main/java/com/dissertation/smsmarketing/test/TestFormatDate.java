package com.dissertation.smsmarketing.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

public class TestFormatDate {
	public static BufferedImage getImage() throws IOException {
		Font f = new Font(Font.MONOSPACED, Font.PLAIN, 24);
		BufferedImage img = new BufferedImage(1, 1,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		g.setFont(f);
		FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
		Rectangle2D rect = f.getStringBounds("No Data Available", frc);
		// release resources
		g.dispose();
		img = new BufferedImage((int) Math.ceil(rect.getWidth()),
				(int) Math.ceil(rect.getHeight()),
				BufferedImage.TYPE_4BYTE_ABGR);

		g = img.getGraphics();
		g.setColor(Color.BLUE);
		g.setFont(f);
		g.drawString("No Data Available", 0, 0);
		// release resources
		g.dispose();
		
		File outputfile = new File("d:/test.jpg");
		ImageIO.write(img, "jpg", outputfile);
		return img;
	}

	public static void main(String a[]) throws ParseException, IOException {
		getImage();
		/*
		 * String date = "2014-09-02"; SimpleDateFormat inputdateformat = new
		 * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat outputdateformat =
		 * new SimpleDateFormat("MMM yyyy"); date =
		 * outputdateformat.format(inputdateformat.parse(date));
		 */
	}

}
