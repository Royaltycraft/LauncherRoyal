package fr.kintus.royalty.launcher;

import static fr.kintus.royalty.launcher.RoyaltyLauncher.ROYALTY_SAVER;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class OptionFrame extends JDialog implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -14074675854082004L;

	private static OptionFrame instance;

	private JLabel memoryLabel = new JLabel("RAM allouée");
	private JComboBox<AllowedMemory> memoryComboBox = new JComboBox<>(
			new AllowedMemory[]{
					AllowedMemory.XMX2G, 
					AllowedMemory.XMX3G, 
					AllowedMemory.XMX4G, 
					AllowedMemory.XMX5G, 
					AllowedMemory.XMX6G, 
					AllowedMemory.XMX7G, 
					AllowedMemory.XMX8G, 
					AllowedMemory.XMX9G, 
					AllowedMemory.XMX10G,
					AllowedMemory.XMX12G, 
					AllowedMemory.XMX14G, 
					AllowedMemory.XMX15G, 
					AllowedMemory.XMX16G, 
					AllowedMemory.XMX32G
				}
	);
	private JButton saveButton = new JButton("Valider");
//	private JLabel dabsLabel = new JLabel("<html><u>Développé par Dabsunter</u></html>");

	public static OptionFrame getInstance() {
		if (instance == null)
			instance = new OptionFrame();
		return instance;
	}

	private OptionFrame() {
		super(LauncherFrame.getInstance(), "Options", true);

		setSize(250, 150);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setLocationRelativeTo(LauncherFrame.getInstance());
		setLayout(null);

		memoryLabel.setBounds(30, 0, 200, 70);
		add(memoryLabel);

		memoryComboBox.setBounds(150, 23, 70, 25);
		add(memoryComboBox);

		saveButton.setBounds(75, 70, 100, 30);
		saveButton.addMouseListener(this);
		add(saveButton);

//		dabsLabel.setForeground(Color.BLUE);
//		dabsLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//		dabsLabel.setBounds(115, 100, 200, 25);
//		dabsLabel.addMouseListener(this);
//		add(dabsLabel);
	}

	@Override
	public void setVisible(boolean b) {
		if (b) {
			try {
				AllowedMemory am = AllowedMemory.valueOf(ROYALTY_SAVER.get("allowed-memory", "XMX1G"));
				memoryComboBox.setSelectedItem(am);
			} catch (IllegalArgumentException ex) {
				memoryComboBox.setSelectedIndex(1);
			}
		}

		super.setVisible(b);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == saveButton)
		{
			ROYALTY_SAVER.set("allowed-memory", ((AllowedMemory) memoryComboBox.getSelectedItem()).name());
			setVisible(false);
		}
//		else if (e.getSource() == dabsLabel)
//		{
//			browseOnDesktop("https://github.com/Dabsunter");
//		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
