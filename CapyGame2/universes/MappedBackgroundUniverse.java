import java.util.ArrayList;
import java.util.Random;

public class MappedBackgroundUniverse implements Universe {


	private boolean complete = false;	
	private Background background1 = null;
	private Background background2 = null;
	private ArrayList<Background> backgrounds = null;
	private DisplayableSprite player1 = null;
	private DisplayableSprite boat = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private ArrayList<DisplayableSprite> disposalList = new ArrayList<DisplayableSprite>();	
	private double xCenter = 0;
	private double yCenter = 0;

	public MappedBackgroundUniverse () {
		background1 = new SkyBoxBackground();
		background2 = new KJABackground();
		backgrounds =new ArrayList<Background>();
		backgrounds.add(background1);
		backgrounds.add(background2);

		ArrayList<DisplayableSprite> barriers = ((KJABackground)background2).getBarriers();
		ArrayList<DisplayableSprite> objects = ((KJABackground)background2).getObjects();
		
		boat = new BoatSprite(KJABackground.TILE_HEIGHT, KJABackground.TILE_WIDTH * 4);
		sprites.add(boat);
		
		sprites.addAll(barriers); 	
		sprites.addAll(objects); 
		
		player1 = new KJASprite(KJABackground.TILE_HEIGHT * 3, KJABackground.TILE_WIDTH * 4);
		sprites.add(player1);
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
	
protected void disposeSprites() {
        
    	//collect a list of sprites to dispose
    	//this is done in a temporary list to avoid a concurrent modification exception
		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
    		if (sprite.getDispose() == true) {
    			disposalList.add(sprite);
    		}
    	}
		
		//go through the list of sprites to dispose
		//note that the sprites are being removed from the original list
		for (int i = 0; i < disposalList.size(); i++) {
			DisplayableSprite sprite = disposalList.get(i);
			sprites.remove(sprite);
			System.out.println("Remove: " + sprite.toString());
    	}
		
		//clear disposal list if necessary
    	if (disposalList.size() > 0) {
    		disposalList.clear();
    	}
    }

	public void update(KeyboardInput keyboard, long actual_delta_time) {

		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, keyboard, actual_delta_time);
		} 
		
		if (player1 instanceof CollidingSprite) {
			if ( ((CollidingSprite)player1).getIsAtExit() ) {
				this.complete = true;
			}
		}
		disposeSprites();
	}

	public String toString() {
		return "";
	}	

}
