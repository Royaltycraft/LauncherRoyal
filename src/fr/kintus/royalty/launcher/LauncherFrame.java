package fr.kintus.royalty.launcher;

import fr.theshark34.swinger.util.WindowMover;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static fr.kintus.royalty.launcher.RoyaltyLauncher.tryToExit;
import static fr.theshark34.swinger.Swinger.getResource;


// Not a real singleton, had to be fixed
public class LauncherFrame extends JFrame implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3471268266762773049L;
	private static LauncherFrame instance;
	private LauncherPanel panel;

	public LauncherFrame()
	{
		instance = this;

		setTitle(RoyaltyLauncher.ROYALTY_INFOS.getServerName());
		setIconImage(getResource("icon.png"));
		setSize(853, 609);
		setUndecorated(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);

		WindowMover mover = new WindowMover(this);
		addMouseListener(mover);
		addMouseMotionListener(mover);

		addWindowListener(this);

		this.panel = new LauncherPanel();
		add(panel);
	}

	public static LauncherFrame getInstance()
	{
		return instance;
	}

	public LauncherPanel getPanel()
	{
		return panel;
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		tryToExit();
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
}
