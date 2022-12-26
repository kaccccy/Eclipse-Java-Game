import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TreeSprite implements DisplayableSprite {

	private static Image image;
	private boolean visible = true;
	private static double centerX = 45;
	private static double centerY = 64;
	private double width = 90;
	private double height = 128;
	private boolean dispose = false;	


	public TreeSprite(double centerX, double centerY) {

		this.centerX = centerX;
		this.centerY = centerY;

		if (image == null && visible) {
			try {
				image = ImageIO.read(new File("res/KJA/tree.png"));
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}

		}
	}
		public TreeSprite(double minX, double minY, double maxX, double maxY, boolean visible) {
			this(centerX, centerY);

			this.height = height;
			this.width = width;	

	}


	public Image getImage() {
		return image;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	//DISPLAYABLE

	public boolean getVisible() {
		return this.visible;
	}

	public double getMinX() {
		return centerX - (width / 2);
	}

	public double getMaxX() {
		return centerX + (width / 2);
	}

	public double getMinY() {
		return centerY - (height / 2);
	}

	public double getMaxY() {
		return centerY + (height / 2);
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getCenterX() {
		return centerX;
	};

	public double getCenterY() {
		return centerY;
	};


	public boolean getDispose() {
		return dispose;
	}

	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}

	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {

	}
}

