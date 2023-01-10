import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;


public class AnimationFrame extends JFrame {

	final public static int FRAMES_PER_SECOND = 60;
	final public static int SCREEN_HEIGHT = 600;
	final public static int SCREEN_WIDTH = 800;

	private TitleFrame titleFrame = null;

	private int screenCenterX = SCREEN_WIDTH / 2;
	private int screenCenterY = SCREEN_HEIGHT / 2;

	private double scale = 1;
	//point in universe on which the screen will center
	private double logicalCenterX = 0;		
	private double logicalCenterY = 0;

	private JPanel panel = null;
	private JPanel menu = null;
	private JButton btnPauseRun;
	private JLabel lblTop;
	private JLabel lblPause;
	private JLabel lblBottom;
	public Font retro;
	private static boolean stop = false;

	private long current_time = 0;								//MILLISECONDS
	private long next_refresh_time = 0;							//MILLISECONDS
	private long last_refresh_time = 0;
	private long minimum_delta_time = 1000 / FRAMES_PER_SECOND;	//MILLISECONDS
	private long actual_delta_time = 0;							//MILLISECONDS
	private long elapsed_time = 0;
	private static boolean isPaused = false;

	private KeyboardInput keyboard = new KeyboardInput();
	private Universe universe = null;

	//local (and direct references to various objects in universe ... should reduce lag by avoiding dynamic lookup
	private Animation animation = null;
	private DisplayableSprite player1 = null;
	private ArrayList<DisplayableSprite> sprites = null;
	private ArrayList<Background> backgrounds = null;
	private Background background = null;
	boolean centreOnPlayer = false;
	int universeLevel = 0;

	public AnimationFrame(Animation animation)
	{
		super("");

		try {
			retro = Font.createFont(Font.TRUETYPE_FONT, new File("res/Fonts/80s-retro-future.ttf"));
			retro = retro.deriveFont(20.0f);
		} catch (Exception ex) {}

		ImageIcon image = new ImageIcon("res/capy/right/capy_right_standing.png");
		this.setIconImage(image.getImage());


		this.animation = animation;
		this.setVisible(false);		
		this.setFocusable(true);
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				keyboard.keyPressed(arg0);
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				keyboard.keyReleased(arg0);
			}
		});
		getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				contentPane_mouseMoved(e);
			}
		});

		Container cp = getContentPane();
		cp.setBackground(Color.BLACK);
		cp.setLayout(null);
		
		panel = new DrawPanel();
		panel.setLayout(null);
		panel.setSize(SCREEN_WIDTH + 5, SCREEN_HEIGHT);
		

			getContentPane().add(panel, BorderLayout.CENTER);
		
		

		setTitle("Capy Game 2; Working Title"); //TODO make a good title for the game
		setResizable(false);
		/*
		btnPauseRun = new JButton("||");
		btnPauseRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnPauseRun_mouseClicked(arg0);
			}
		});

		btnPauseRun.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnPauseRun.setBounds(SCREEN_WIDTH - 64, 20, 48, 32);
		btnPauseRun.setFocusable(false);
		getContentPane().add(btnPauseRun);
		getContentPane().setComponentZOrder(btnPauseRun, 0);
		 */
		lblTop = new JLabel("Time: ");
		lblTop.setForeground(Color.WHITE);
		lblTop.setFont(retro);
		lblTop.setBounds(16, 22, SCREEN_WIDTH - 16, 30);
		getContentPane().add(lblTop);
		getContentPane().setComponentZOrder(lblTop, 0);
	}

	public void start()
	{
		Thread thread = new Thread()
		{
			public void run()
			{
				animationLoop();
				System.out.println("run() complete");
			}
		};

		thread.start();

		//create a title frame
		titleFrame = new TitleFrame();
		//center on the parent
		titleFrame.setLocationRelativeTo(this);
		//display title screen
		//set the modality to APPLICATION_MODAL
		titleFrame.setModalityType(ModalityType.APPLICATION_MODAL);
		//by setting the dialog to visible, the application will start running the dialog
		titleFrame.setVisible(true);
		
		//when title screen has been closed, execution will resume here.
		titleFrame.dispose();
		this.setVisible(true);

		System.out.println("main() complete");

	}	
	private void animationLoop() {

		universe = animation.getNextUniverse();
		universeLevel++;

		while (stop == false && universe != null) {

			sprites = universe.getSprites();
			player1 = universe.getPlayer1();
			backgrounds = universe.getBackgrounds();
			centreOnPlayer = universe.centerOnPlayer();
			this.scale = universe.getScale();
			this.logicalCenterX = universe.getXCenter();
			this.logicalCenterY = universe.getYCenter();

			//Main Menu
			if (titleFrame.isVisible == false)
				this.setVisible(true);
			
			// main game loop
			while (stop == false && universe.isComplete() == false) {

				//adapted from http://www.java-gaming.org/index.php?topic=24220.0
				last_refresh_time = System.currentTimeMillis();
				next_refresh_time = current_time + minimum_delta_time;

				//sleep until the next refresh time
				while (current_time < next_refresh_time)
				{
					//allow other threads (i.e. the Swing thread) to do its work
					Thread.yield();

					try {
						Thread.sleep(1);
					}
					catch(Exception e) {    					
					} 

					//track current time
					current_time = System.currentTimeMillis();
				}

				//read input
				keyboard.poll();
				handleKeyboardInput();

				//UPDATE STATE
				updateTime();

				universe.update(keyboard, actual_delta_time);
				updateControls();

				//REFRESH
				this.logicalCenterX = universe.getXCenter();
				this.logicalCenterY = universe.getYCenter();
				this.repaint();
			}

			universe = animation.getNextUniverse();
			keyboard.poll();

		}

		System.out.println("animation complete");
		AudioPlayer.setStopAll(true);
		dispose();	

	}

	private void updateControls() {

		this.lblTop.setText(String.format("Time: %4.3f", elapsed_time / 1000.0));

	}

	private void updateTime() {

		current_time = System.currentTimeMillis();
		actual_delta_time = (getIsPaused() ? 0 : current_time - last_refresh_time);
		last_refresh_time = current_time;
		elapsed_time += actual_delta_time;

	}
	/*
	protected void btnPauseRun_mouseClicked(MouseEvent arg0) {
		if (isPaused) {
			isPaused = false;
			this.btnPauseRun.setText("||");
		}
		else {
			isPaused = true;
			this.btnPauseRun.setText(">");
		}
	}
	 */
	private void handleKeyboardInput() {
		/*
		if (keyboard.keyDown(80) && ! isPaused) {
			btnPauseRun_mouseClicked(null);	
		}
		if (keyboard.keyDown(79) && isPaused ) {
			btnPauseRun_mouseClicked(null);
		}
		if (keyboard.keyDown(112)) {
			scale *= 1.01;
		}
		if (keyboard.keyDown(113)) {
			scale /= 1.01;
		}

		if (keyboard.keyDown(65)) {
			screenCenterX -= 1;
		}
		if (keyboard.keyDown(68)) {
			screenCenterX += 1;
		}
		if (keyboard.keyDown(83)) {
			screenCenterY -= 1;
		}
		if (keyboard.keyDown(88)) {
			screenCenterY += 1;
		}
		 */

		if (keyboard.keyDownOnce(27)) {

			if (isPaused == false) {
				isPaused = true;
				
				lblPause = new JLabel("Paused");
				lblPause.setForeground(Color.WHITE);
				Font retroPause = retro.deriveFont(50.0f);				
				lblPause.setFont(retroPause);
				lblPause.setBounds(16, 450, 200, 100);
				getContentPane().add(lblPause);
				getContentPane().setComponentZOrder(lblPause, 0);
			}
			else {
				isPaused = false;
				getContentPane().remove(lblPause);
			}
		}
	}

	class DrawPanel extends JPanel {

		public void paintComponent(Graphics g)
		{	
			if (universe == null) {
				return;
			}

			if (player1 != null && centreOnPlayer) {
				logicalCenterX = player1.getCenterX();
				logicalCenterY = player1.getCenterY();     
			}

			if (backgrounds != null) {
				for (Background background: backgrounds) {
					paintBackground(g, background);
				}
			}

			if (sprites != null) {
				for (DisplayableSprite activeSprite : sprites) {
					DisplayableSprite sprite = activeSprite;
					if (sprite.getVisible()) {
						if (sprite.getImage() != null) {
							g.drawImage(sprite.getImage(), translateToScreenX(sprite.getMinX()), translateToScreenY(sprite.getMinY()), scaleLogicalX(sprite.getWidth()), scaleLogicalY(sprite.getHeight()), null);
						}
						else {
							g.setColor(Color.BLUE);
							g.fillRect(translateToScreenX(sprite.getMinX()), translateToScreenY(sprite.getMinY()), scaleLogicalX(sprite.getWidth()), scaleLogicalY(sprite.getHeight()));
						}
					}
				}				
			}
		}

		private void paintBackground(Graphics g, Background background) {

			if ((g == null) || (background == null)) {
				return;
			}

			//what tile covers the top-left corner?
			double logicalLeft = (logicalCenterX  - (screenCenterX / scale) - background.getShiftX());
			double logicalTop =  (logicalCenterY - (screenCenterY / scale) - background.getShiftY()) ;

			int row = background.getRow((int)(logicalTop - background.getShiftY() ));
			int col = background.getCol((int)(logicalLeft - background.getShiftX()  ));
			Tile tile = background.getTile(col, row);

			boolean rowDrawn = false;
			boolean screenDrawn = false;
			while (screenDrawn == false) {
				while (rowDrawn == false) {
					tile = background.getTile(col, row);
					if (tile.getWidth() <= 0 || tile.getHeight() <= 0) {
						//no increase in width; will cause an infinite loop, so consider this screen to be done
						g.setColor(Color.GRAY);
						g.fillRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);					
						rowDrawn = true;
						screenDrawn = true;						
					}
					else {
						Tile nextTile = background.getTile(col+1, row+1);
						int width = translateToScreenX(nextTile.getMinX()) - translateToScreenX(tile.getMinX());
						int height = translateToScreenY(nextTile.getMinY()) - translateToScreenY(tile.getMinY());
						g.drawImage(tile.getImage(), translateToScreenX(tile.getMinX() + background.getShiftX()), translateToScreenY(tile.getMinY() + background.getShiftY()), width, height, null);
					}					
					//does the RHE of this tile extend past the RHE of the visible area?
					if (translateToScreenX(tile.getMinX() + background.getShiftX() + tile.getWidth()) > SCREEN_WIDTH || tile.isOutOfBounds()) {
						rowDrawn = true;
					}
					else {
						col++;
					}
				}
				//does the bottom edge of this tile extend past the bottom edge of the visible area?
				if (translateToScreenY(tile.getMinY() + background.getShiftY() + tile.getHeight()) > SCREEN_HEIGHT || tile.isOutOfBounds()) {
					screenDrawn = true;
				}
				else {
					col = background.getCol(logicalLeft);
					row++;
					rowDrawn = false;
				}
			}
		}				
	}

	private int translateToScreenX(double logicalX) {
		return screenCenterX + scaleLogicalX(logicalX - logicalCenterX);
	}		
	private int scaleLogicalX(double logicalX) {
		return (int) Math.round(scale * logicalX);
	}
	private int translateToScreenY(double logicalY) {
		return screenCenterY + scaleLogicalY(logicalY - logicalCenterY);
	}		
	private int scaleLogicalY(double logicalY) {
		return (int) Math.round(scale * logicalY);
	}

	private double translateToLogicalX(int screenX) {
		int offset = screenX - screenCenterX;
		return offset / scale;
	}
	private double translateToLogicalY(int screenY) {
		int offset = screenY - screenCenterY;
		return offset / scale;			
	}

	protected void contentPane_mouseMoved(MouseEvent e) {
		MouseInput.screenX = e.getX();
		MouseInput.screenY = e.getY();
		MouseInput.logicalX = translateToLogicalX(MouseInput.screenX);
		MouseInput.logicalY = translateToLogicalY(MouseInput.screenY);
	}

	protected void this_windowClosing(WindowEvent e) {
		System.out.println("windowClosing()");
		stop = true;
		dispose();	
	}

	public static boolean getIsPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}
}
