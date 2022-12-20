import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SkyBoxBackground implements Background {

	private Image sky = null;
	private int backgroundWidth = 50;
	private int backgroundHeight = 50;

	public SkyBoxBackground() {

		try {
			this.sky = ImageIO.read(new File("res/KJA/ocean.png"));

		}
		catch (IOException e) {
			//System.out.println(e.toString());
		}		
	}

	@Override
	public Tile getTile(int col, int row) {
		int x = (col * backgroundWidth);
		int y = (row * backgroundHeight);
		Tile newTile = null;

		newTile = new Tile(sky, x, y, backgroundWidth, backgroundHeight, false);

		return newTile;
	}


	@Override
	public int getCol(double x) {
		int col = 0;
		if (backgroundWidth != 0) {
			col = (int) (x / backgroundWidth);
			if (x < 0) {
				return col - 1;
			}
			else {
				return col;
			}
		}
		else {
			return 0;
		}
	}
	@Override
	public int getRow(double y) {
		int row = 0;

		if (backgroundHeight != 0) {
			row = (int) (y / backgroundHeight);
			if (y < 0) {
				return row - 1;
			}
			else {
				return row;
			}
		}
		else {
			return 0;
		}
	}

	@Override
	public double getShiftX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getShiftY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setShiftX(double shiftX) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setShiftY(double shiftY) {
		// TODO Auto-generated method stub

	}


}
