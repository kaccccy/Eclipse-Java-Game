import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class KJASprite implements DisplayableSprite, MovableSprite, CollidingSprite {

	private static Image image;	

	private double centerX = 25;
	private double centerY = 25;
	private double width = 50;
	private double height = 41;

	private boolean dispose = false;	
	private double velocityX = 0;
	private double velocityY = 0;
	private final double VELOCITY = 200;
	private final double PROXIMITY = 100;

	private long score = 0;
	private boolean isAtExit = false;
	private String proximityMessage = "";

	private int animationFrame = 0;
	private int idleFrame = 0;

	private int moveFrames = 20;
	private int stillFrames = 20;

	private String direction = "";


	public KJASprite(double centerX, double centerY) {

		this.centerX = centerX;
		this.centerY = centerY;

		try {
			image = ImageIO.read(new File("res/capy/right/capy_right_standing.png"));
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}
		direction = "right";

	}

	public KJASprite(double centerX, double centerY,  double height, double width) {
		this(centerX, centerY);

		this.height = height;
		this.width = width;
	}


	public Image getImage() {
		return image;
	}

	@Override
	public void setCenterX(double centerX) {
		this.centerX=centerX;
	}

	@Override
	public void setCenterY(double centerY) {
		this.centerY=centerY;
	}

	@Override
	public void setVelocityX(double pixelsPerSecond) {
		this.velocityX=pixelsPerSecond;
	}

	@Override
	public void setVelocityY(double pixelsPerSecond) {
		this.velocityY=pixelsPerSecond;
	}

	@Override
	public boolean getVisible() {
		return true;
	}

	@Override
	public double getMinX() {
		return centerX - (width / 2);
	}

	@Override
	public double getMaxX() {
		return centerX + (width / 2);
	}

	@Override
	public double getMinY() {
		return centerY - (height / 2);
	}

	@Override
	public double getMaxY() {
		return centerY + (height / 2);
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getCenterX() {
		return centerX;
	}

	@Override
	public double getCenterY() {
		return centerY;
	}

	@Override
	public boolean getDispose() {
		return dispose;
	}

	@Override
	public void setDispose(boolean dispose) {
		this.dispose=dispose;
	}


	@Override
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {

		double velocityX = 0;
		double velocityY = 0;

		//LEFT
		if (keyboard.keyDown(65)) {
			width = 50;
			if (keyboard.keyDown(16)) {
				velocityX -= VELOCITY * 1.75;
				direction = "left sprint";
			}
			else {
				velocityX -= VELOCITY;
				direction = "left";
			}
			setAnimationFrame(animationFrame + 1);
			if (animationFrame >= moveFrames) {
				animationFrame = 1;
			}

		}

		// RIGHT
		else if (keyboard.keyDown(68)) {
			width = 50;
			if (keyboard.keyDown(16)) {
				velocityX += VELOCITY * 1.75;
				direction = "right sprint";
			}
			else {
				velocityX += VELOCITY;
				direction = "right";
			}		
			setAnimationFrame(animationFrame + 1);
			if (animationFrame >= moveFrames) {
				animationFrame = 1;
			}
		}

		//UP
		else if (keyboard.keyDown(87)) {
			width = 25;
			if (keyboard.keyDown(16)) {
				velocityY -= VELOCITY * 1.75;
			}
			else {
				velocityY -= VELOCITY;
			}
			direction = "up";
			setAnimationFrame(animationFrame + 1);
			if (animationFrame >= moveFrames) {
				animationFrame = 1;
			}
		}

		// DOWN
		else if (keyboard.keyDown(83)) {
			width = 25;
			if (keyboard.keyDown(16)) {
				velocityY += VELOCITY * 1.75;
			}
			else {
				velocityY += VELOCITY;
			}
			direction = "down";
			setAnimationFrame(animationFrame + 1);
			if (animationFrame >= moveFrames) {
				animationFrame = 1;
			}
		}

		getImageForAnimationFrame(keyboard);
		if (keyboard.keyDown(83) == false && keyboard.keyDown(87) == false && keyboard.keyDown(68) == false && keyboard.keyDown(65) == false) {
			setIdleFrame(idleFrame + 1);
			if (idleFrame >= stillFrames) {
				idleFrame = 1;
			}
			getIdleImage();
		}

		double deltaX = actual_delta_time * 0.001 * velocityX;
		double deltaY = actual_delta_time * 0.001 * velocityY;

		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
		boolean collidingBarrierY = checkCollisionWithBarrier(universe.getSprites(), 0, deltaY);

		checkSpriteOverlap(universe.getSprites());
		//boolean checkProximity = checkProximity(universe.getSprites());


		if (collidingBarrierX == false) {
			this.centerX += deltaX;
		}
		if (collidingBarrierY == false) {
			this.centerY += deltaY;
		}
		/*
		if (checkProximity == false) {
			proximityMessage = "";
		}

		checkCoversCoin(universe.getSprites(), deltaX, deltaY);
		checkInExit(universe.getSprites(), deltaX, deltaY);
		checkProximity(universe.getSprites());
		 */
	}



	private boolean checkCollisionWithBarrier(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof BarrierSprite || sprite instanceof TreeSprite || sprite instanceof ShrubSprite || sprite instanceof BoatSprite) {
				if (CollisionDetection.overlaps(this.getMinX() + deltaX, this.getMinY() + deltaY, 
						this.getMaxX()  + deltaX, this.getMaxY() + deltaY, 
						sprite.getMinX(),sprite.getMinY(), 
						sprite.getMaxX(), sprite.getMaxY())) {
					colliding = true;
					break;					
				}
			}
		}		
		return colliding;		
	}

	private void checkSpriteOverlap(ArrayList<DisplayableSprite> sprites) { 
		double pushOutDistance = 0;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof BarrierSprite || sprite instanceof TreeSprite) {
				if (CollisionDetection.overlaps(this.getMinX(), this.getMinY(), this.getMaxX(), this.getMaxY(), 
						sprite.getMinX(), sprite.getMinY(), sprite.getMaxX(), sprite.getMaxY())) {
					// Collision detected, determine which side of the barrier sprite the player sprite is colliding with
					/*	if (this.getMinY() < sprite.getMinY()) {
						// Player sprite is colliding with the top side of the barrier sprite
						pushOutDistance = this.getMaxY() - sprite.getMinY();
					} else if (this.getMaxY() > sprite.getMaxY()) {
						// Player sprite is colliding with the bottom side of the barrier sprite
						pushOutDistance = this.getMinY() - sprite.getMaxY();
					} else*/ if (this.getMinX() < sprite.getMinX()) {
						// Player sprite is colliding with the left side of the barrier sprite
						pushOutDistance = this.getMaxX() - sprite.getMinX();
					} else if (this.getMaxX() > sprite.getMaxX()) {
						// Player sprite is colliding with the right side of the barrier sprite
						pushOutDistance = this.getMinX() - sprite.getMaxX();
					}
				}
			}
		}

		if (pushOutDistance != 0) {
			// Push player sprite out of the barrier sprite
			double movementX = (velocityX + pushOutDistance);
			//double movementY = (velocityY + pushOutDistance);

			this.centerX -= movementX;
			//this.centerY -= movementY; // No vertical sprite overlapping has been seen for this part 
			// of the code to be needed yet, but it's here to save me the possible headache
		}

	}
	/*
	private void checkCoversCoin(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof CoinSprite) {				
				if (CollisionDetection.overlaps(this, sprite)) {
					sprite.setDispose(true);
					score += 100;

					break;					
				}
			}
		}		

	}

	private void checkInExit(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof ExitSprite) {				
				if (CollisionDetection.inside(this, sprite) && score >= 1000) {
					isAtExit = true;
					break;					
				}
			}
		}

	}

	private boolean checkProximity(ArrayList<DisplayableSprite> sprites) {
		boolean inProximity = false;
		for (DisplayableSprite sprite : sprites) {
			if (sprite.getClass().toString().contains("OtherSprite")) {	
				if (withinProximity(this, sprite) == true) {
					proximityMessage = "1000 points to pass!";
					inProximity = true;
					break;
				}
			}
		}
		return inProximity;
	}

	private boolean withinProximity(DisplayableSprite spriteA, DisplayableSprite spriteB) {
		boolean inProximity = false;

		double a = spriteB.getCenterX() - spriteA.getCenterX();
		double b = spriteB.getCenterY() - spriteA.getCenterY();
		double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

		if (c <= PROXIMITY) {
		inProximity = true;
		}
		return inProximity;
	}
	 */


	@Override
	public long getScore() {
		return score;
	}

	@Override
	public String getProximityMessage() {
		return proximityMessage;
	}

	@Override
	public boolean getIsAtExit() {
		return isAtExit;
	} 

	public int getMoveFrames() {
		return moveFrames;
	}

	public void setAnimationFrame(int frame) {
		this.animationFrame = frame;
	}

	public int getStillFrames() {
		return stillFrames; 
	}
	public void setIdleFrame(int frame) {
		this.idleFrame = frame;
	}

	public void getIdleImage() {
		if (direction == "left") {
			//player is looking left
			try {
				image = ImageIO.read(new File("res/capy/idle/left/capy_idle_left_" + idleFrame + ".png"));
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}
		}

		if (direction == "right") {
			//player is looking right
			try {
				image = ImageIO.read(new File("res/capy/idle/right/capy_idle_right_" + idleFrame + ".png"));
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}
		}

		if (direction == "up") {
			//player is looking up
			try {
				image = ImageIO.read(new File("res/capy/idle/up/capy_idle_up_" + idleFrame + ".png"));
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}
		}

		if (direction == "down") {
			//player is looking down
			try {
				image = ImageIO.read(new File("res/capy/idle/down/capy_idle_down_" + idleFrame + ".png"));
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}

	public void getImageForAnimationFrame(KeyboardInput keyboard) {

		if (keyboard.keyDown(65)) {
			if (keyboard.keyDown(16)) {
				// player is sprinting left
				try {
					image = ImageIO.read(new File("res/capy/left_sprint/capy_left_sprint_" + animationFrame + ".png"));
				}
				catch (IOException e) {
					System.out.println(e.toString());
				}
			}
			else {
				// player is moving left
				try {
					image = ImageIO.read(new File("res/capy/left/capy_left_walk_" + animationFrame + ".png"));
				}
				catch (IOException e) {
					System.out.println(e.toString());
				}
			}
		} else if (keyboard.keyDown(68)) {
			if (keyboard.keyDown(16)) {
				// player is sprinting right
				try {
					image = ImageIO.read(new File("res/capy/right_sprint/capy_right_sprint_" + animationFrame + ".png"));
				}
				catch (IOException e) {
					System.out.println(e.toString());
				}
			}
			else {
				// player is moving right
				try {
					image = ImageIO.read(new File("res/capy/right/capy_right_walk_" + animationFrame + ".png"));
				}
				catch (IOException e) {
					System.out.println(e.toString());
				}
			}
		} else if (keyboard.keyDown(87)) {
			// player is moving up
			try {
				image = ImageIO.read(new File("res/capy/up/capy_up_walk_" + animationFrame + ".png"));
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}
		} else if (keyboard.keyDown(83)) {
			// player is moving down
			try {
				image = ImageIO.read(new File("res/capy/down/capy_down_walk_" + animationFrame + ".png"));
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}
		} 
	}
}

