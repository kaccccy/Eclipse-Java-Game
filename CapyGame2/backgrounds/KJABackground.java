import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class KJABackground implements Background {
	private Image grass;
	private Image water;
	private Image sand;
	private Image sandyGrass;
	private Image border;
	private int maxCols = 0;
	private int maxRows = 0;        	

	protected static int TILE_WIDTH = 50;
	protected static int TILE_HEIGHT = 50;

	private int map[][] = new int[][] { 
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,0},
		{0,3,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,0},
		{0,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,0},
		{0,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,3,0},
		{0,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,3,0},
		{0,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,3,0},
		{0,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,3,0},
		{0,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,3,0},
		{0,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,3,0},
		{0,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0},
		{0,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
	};

	public KJABackground() {

		try {
			this.border = null;
			this.grass = ImageIO.read(new File("res/KJA/grass.png"));
			this.water = ImageIO.read(new File("res/KJA/water.png"));
			this.sand = ImageIO.read(new File("res/KJA/sand.png"));
			//this.sandyGrass = ImageIO.read(new File("res/KJA/sandy_grass.png")); //TODO make sandy grass
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}
		maxRows = map.length - 1;
		maxCols = map[0].length - 1;
	}

	@Override
	public Tile getTile(int col, int row) {
		Image image = null;

		if (row < 0 || row > maxRows || col < 0 || col > maxCols) {
			image = null;
		}
		else if (map[row][col] == 0) {
			image = border;
		}
		else if (map[row][col] == 1) {
			image = grass;
		}
		else if (map[row][col] == 2) {
			image = water;
		}
		else if (map[row][col] == 3) {
			image = sand;
		}
		else if (map[row][col] == 4) {
			image = sandyGrass;
		}
		else {
			image = null;
		}

		int x = (col * TILE_WIDTH);
		int y = (row * TILE_HEIGHT);

		Tile newTile = new Tile(image, x, y, TILE_WIDTH, TILE_HEIGHT, false);

		return newTile;
	}


	@Override
	public int getCol(double x) {
		int col = (int) (x / TILE_WIDTH);
		if (x < 0) {
			return col - 1;
		}
		else {
			return col;
		}
	}
	@Override
	public int getRow(double y) {
		//which row is y sitting at?
		int row = 0;

		row = (int) (y / TILE_HEIGHT);
		if (y < 0) {
			return row - 1;
		}
		else {
			return row;
		}
	}

	public ArrayList<DisplayableSprite> getBarriers() {
		ArrayList<DisplayableSprite> barriers = new ArrayList<DisplayableSprite>();
		for (int col = 0; col < map[0].length; col++) {
			for (int row = 0; row < map.length; row++) {
				if (map[row][col] == 0 || map[row][col] == 2) {
					barriers.add(new BarrierSprite(col * TILE_WIDTH, row * TILE_HEIGHT, (col + 1) * TILE_WIDTH, (row + 1) * TILE_HEIGHT, false));
				}
			}
		}
		return barriers;
	}

	@Override
	public double getShiftX() {
		// ignore
		return 0;
	}

	@Override
	public double getShiftY() {
		// ignore
		return 0;
	}

	@Override
	public void setShiftX(double shiftX) {
		// ignore
	}

	@Override
	public void setShiftY(double shiftY) {
		// ignore

	}

	public int[][] getMap() {
		return map;
	}


}
