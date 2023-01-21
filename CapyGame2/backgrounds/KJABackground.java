import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class KJABackground implements Background {
	private Image grass;
	private Image water;
	private Image[][] waterSprites;
	private Image sand;
	private Image sandyGrass;
	private Image dungeonFloor;
	private Image dungeonWall;
	private Image border;
	private int maxCols = 0;
	private int maxRows = 0;        	

	protected static int TILE_WIDTH = 50;
	protected static int TILE_HEIGHT = 50;

	private int map[][] = new int[][] { 
	};

	public KJABackground() {

		try {
			map = CSVReader.importFromCSV("res/Map/GameMap.csv");
			maxRows = map.length - 1;
			maxCols = map[0].length - 1;

			this.border = null;
			this.grass = ImageIO.read(new File("res/KJA/grass.png"));
			this.sand = ImageIO.read(new File("res/KJA/sand.png"));
			this.dungeonFloor = ImageIO.read(new File("res/KJA/floor_plain.png"));
			this.dungeonWall = ImageIO.read(new File("res/KJA/wall_front.png"));

			// Initialize the waterSprites array with the same dimensions as the map
			waterSprites = new Image[maxRows][maxCols];

			// Randomly choose a sprite for each water tile and store it in the waterSprites array
			Random rand = new Random();
			for (int row = 0; row < maxRows; row++) {
				for (int col = 0; col < maxCols; col++) {
					if (map[row][col] == 2) {
						int index = rand.nextInt(16) + 1;
						try {
							waterSprites[row][col] = ImageIO.read(new File("res/KJA/water/water_" + index + ".png"));
						} catch (IOException e) {
							System.out.println(e.toString());
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e.toString());
		}


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
			image = waterSprites[row][col];
		}
		else if (map[row][col] == 3) {
			image = sand;
		}
		else if (map[row][col] == 4) {
			image = grass; // FOR TREE TO WORK
		}
		else if (map[row][col] == 5) {
			image = grass; // FOR SHRUB TO WORK
		}
		else if (map[row][col] == 6) {
			image = dungeonFloor; 
		}
		else if (map[row][col] == 7) {
			image = dungeonWall; 
		}
		else if (map[row][col] == 8) {
			image = grass; // FOR TORCHES TO WORK
		}
		else if (map[row][col] == 9) {
			image = dungeonFloor; //FOR DOOR TO WORK
		}
		else if (map[row][col] == 10) {
			image = dungeonFloor; //FOR KEY TO WORK
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
				if (map[row][col] == 0 || map[row][col] == 2 || map[row][col] == 7) {
					barriers.add(new BarrierSprite(col * TILE_WIDTH, row * TILE_HEIGHT, (col + 1) * TILE_WIDTH, (row + 1) * TILE_HEIGHT, false));
				}
			}
		}
		return barriers;
	}

	public ArrayList<DisplayableSprite> getObjects() {
		ArrayList<DisplayableSprite> objects = new ArrayList<DisplayableSprite>();
		for (int col = 0; col < map[0].length; col++) {
			for (int row = 0; row < map.length; row++) {
				if (map[row][col] == 4) {
					objects.add(new TreeSprite(col * TILE_WIDTH, (row - 2) * TILE_HEIGHT, (col + 2) * TILE_WIDTH, (row + 1) * TILE_HEIGHT, true));
				}
				if (map[row][col] == 5) {
					objects.add(new ShrubSprite(col * TILE_WIDTH, row * TILE_HEIGHT, (col + 1) * TILE_WIDTH, (row + 1) * TILE_HEIGHT, true));
				}
				if (map[row][col] == 8) {
					objects.add(new TorchSprite(col * TILE_WIDTH, row * TILE_HEIGHT, (col + .5) * TILE_WIDTH, (row + 1.25) * TILE_HEIGHT, true));
				}
				if (map[row][col] == 9) {
					objects.add(new ExitSprite(col * TILE_WIDTH, row * TILE_HEIGHT, (col + 1) * TILE_WIDTH, (row + 1) * TILE_HEIGHT, true));
				}
				if (map[row][col] == 10) {
					objects.add(new CoinSprite((col + .25) * TILE_WIDTH, (row + .25) * TILE_HEIGHT, (col + .75) * TILE_WIDTH, (row + .5) * TILE_HEIGHT, true));
				}
			}
		}
		return objects;
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
