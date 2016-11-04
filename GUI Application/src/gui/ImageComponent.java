package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageComponent extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -758553724542988364L;
	Image image;

	public ImageComponent(byte byteArray[]) throws IOException {
		image = ImageIO.read(new ByteArrayInputStream(byteArray));
	}

	public void paint(Graphics g) {
		synchronized (image) {
			super.paint(g);
			int w = getWidth();
			int h = getHeight();
			int imageWidth = image.getWidth(this);
			int imageHeight = image.getHeight(this);
			int x = (w - imageWidth) / 2;
			int y = (h - imageHeight) / 2;
			g.drawImage(image, x, y, this);
		}
	}

	public void setImage(byte byteArray[]) throws IOException {
		synchronized (image) {
			image = ImageIO.read(new ByteArrayInputStream(byteArray));
		}
	}

	/**
	 * For the ScrollPane or other Container.
	 */
	public Dimension getPreferredSize() {
		return new Dimension(image.getWidth(this), image.getHeight(this));
	}
}