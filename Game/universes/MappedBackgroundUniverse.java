import java.util.ArrayList;

public class MappedBackgroundUniverse implements Universe {


	private boolean complete = false;	
	private Background background1 = null;
	private Background background2 = null;
	private ArrayList<Background> backgrounds = null;
	private DisplayableSprite player1 = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private double xCenter = 0;
	private double yCenter = 0;

	public MappedBackgroundUniverse () {
		background1 = new SkyBoxBackground();
		background2 = new KJABackground();
		ArrayList<DisplayableSprite> barriers = ((KJABackground)background2).getBarriers();
		backgrounds =new ArrayList<Background>();
		backgrounds.add(background1);
		backgrounds.add(background2);
		
		player1 = new KJASprite(KJABackground.TILE_HEIGHT * 2, KJABackground.TILE_WIDTH * 4);
		sprites.add(player1);
		sprites.addAll(barriers);

	}

	public double getScale() {
		return 1;
	}	
	
	public double getXCenter() {
		return this.xCenter;
	}

	public double getYCenter() {
		return this.yCenter;
	}
	
	public void setXCenter(double xCenter) {
		this.xCenter = xCenter;
	}

	public void setYCenter(double yCenter) {
		this.yCenter = yCenter;
	}
	
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		complete = true;
	}

	@Override
	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}	
	public DisplayableSprite getPlayer1() {
		return player1;
	}

	public ArrayList<DisplayableSprite> getSprites() {
		return sprites;
	}
		
	public boolean centerOnPlayer() {
		return true;
	}		
	
	public void update(KeyboardInput keyboard, long actual_delta_time) {

		if (keyboard.keyDownOnce(27)) {
			complete = true;
		}
		
		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, keyboard, actual_delta_time);
    	} 
	}

	public String toString() {
		return "";
	}	

}
