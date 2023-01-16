import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window.Type;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.JRadioButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TitleFrame extends JDialog {
 boolean isVisible = true;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public TitleFrame() {
		
		//this.setUndecorated(true);
	//	this.setBackground(Color.BLACK);
		ImageIcon image = new ImageIcon("res/capy/right/capy_right_standing.png");
		this.setIconImage(image.getImage());
		setTitle("The Quest for Stuff");
        
		setType(Type.NORMAL);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		Font lblTitleFont = AnimationFrame.retro.deriveFont(Font.BOLD, 45.0f);
		Font lblSubtitleFont = AnimationFrame.retro.deriveFont(25.0f);
		Font lblOptionsFont = AnimationFrame.retro.deriveFont(20.0f);
		
		JLabel lblTitle = new JLabel("Capy Island 2:");
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(lblTitleFont);
		lblTitle.setBounds(175, 75, 400, 75);
		contentPane.add(lblTitle);
		
		JLabel lblSubtitle = new JLabel("The Quest for Stuff");
		lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblSubtitle.setForeground(Color.WHITE);
		lblSubtitle.setFont(lblSubtitleFont);
		lblSubtitle.setBounds(175, 130, 400, 75);
		contentPane.add(lblSubtitle);
		
		JLabel capySitting = new JLabel(); 
		capySitting.setHorizontalAlignment(SwingConstants.CENTER);
		capySitting.setIcon(new ImageIcon("res/capy/capy_sitting.png")); 
        Dimension size = capySitting.getPreferredSize(); 
        capySitting.setBounds(320, 225,  size.width, size.height);
        contentPane.add(capySitting);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnQuit_mouseClicked(e);
			}
		});
		btnQuit.setBorder(null);
		btnQuit.setForeground(Color.DARK_GRAY);
		btnQuit.setOpaque(true);
		btnQuit.setBackground(new Color(211, 211, 211));
		btnQuit.setFont(lblOptionsFont);
		btnQuit.setBounds(300, 450, 145, 41);
		contentPane.add(btnQuit);
		
		
		
		JButton btnHelp = new JButton("Controls");
		btnHelp.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnHelp_mouseClicked(e);
			}
		});
		btnHelp.setBorder(null);
		btnHelp.setForeground(Color.DARK_GRAY);
		btnHelp.setOpaque(true);
		btnHelp.setBackground(new Color(211, 211, 211));
		btnHelp.setFont(lblOptionsFont);
		btnHelp.setBounds(300, 400, 145, 41);
		contentPane.add(btnHelp);
	
		
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnPlay_mouseClicked(e);
			}
		});
		btnPlay.setBorder(null);
		btnPlay.setForeground(Color.DARK_GRAY);
		btnPlay.setOpaque(true);
		btnPlay.setBackground(new Color(211, 211, 211));
		btnPlay.setFont(lblOptionsFont);
		btnPlay.setBounds(300, 350, 145, 41);
		contentPane.add(btnPlay);
	}
	
	
	protected void btnPlay_mouseClicked(MouseEvent e) {
		this.dispose();
		isVisible = false;
	}
	
	protected void btnHelp_mouseClicked(MouseEvent e) {
		this.dispose(); //TODO Controls and titleFrame system tray bug; when controls is pressed, everything is set to not be visible while a back button in top left and a list of controls are added. when back is pressed these two disappear and all the other components dissapear
	}
	
	protected void btnQuit_mouseClicked(MouseEvent e) {
		this.dispose();

	}
	

	
}
