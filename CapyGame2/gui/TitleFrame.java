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
import java.awt.Window.Type;
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
		
		this.setUndecorated(true);
	//	this.setBackground(Color.BLACK);
		
		
		setType(Type.NORMAL);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		Font lblFont = AnimationFrame.retro.deriveFont(20.0f);

		JLabel lblTitle = new JLabel("Capy Game 2:");
		lblTitle.setForeground(Color.ORANGE);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(lblFont);
		lblTitle.setBounds(0, 32, 369, 61);
		contentPane.add(lblTitle);
		
		JLabel lblPickup = new JLabel("The Squeakquel");
		lblPickup.setHorizontalAlignment(SwingConstants.CENTER);
		lblPickup.setForeground(Color.ORANGE);
		lblPickup.setFont(lblFont);
		lblPickup.setBounds(0, 87, 369, 61);
		contentPane.add(lblPickup);
		
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
		btnPlay.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnPlay.setBounds(114, 185, 145, 41);
		contentPane.add(btnPlay);
	}
	
	JButton btnQuit = new JButton("Quit");
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
	btnPlay.setFont(new Font("Tahoma", Font.PLAIN, 16));
	btnPlay.setBounds(114, 185, 145, 41);
	contentPane.add(btnPlay);
}
	
	protected void btnPlay_mouseClicked(MouseEvent e) {
		this.dispose();
		isVisible = false;
	}
	
}
