package fr.kintus.royalty.launcher;

import static fr.kintus.royalty.launcher.RoyaltyLauncher.ROYALTY_FONT_BOLD;
import static fr.kintus.royalty.launcher.RoyaltyLauncher.ROYALTY_FONT_LOW;
import static fr.kintus.royalty.launcher.RoyaltyLauncher.ROYALTY_SAVER;
import static fr.kintus.royalty.launcher.RoyaltyLauncher.ROYALTY_URL;
import static fr.kintus.royalty.launcher.RoyaltyLauncher.auth;
import static fr.kintus.royalty.launcher.RoyaltyLauncher.browseOnDesktop;
import static fr.kintus.royalty.launcher.RoyaltyLauncher.refresh;
import static fr.kintus.royalty.launcher.RoyaltyLauncher.saveInfos;
import static fr.kintus.royalty.launcher.RoyaltyLauncher.setAuthInfos;
import static fr.kintus.royalty.launcher.RoyaltyLauncher.tryToExit;
import static fr.theshark34.swinger.Swinger.getResource;
import static fr.theshark34.swinger.Swinger.getTransparentWhite;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import fr.theshark34.swinger.textured.STexturedCheckbox;



public class LauncherPanel extends JPanel implements SwingerEventListener
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8151296815163495417L;


	private Saver saver = new Saver(new File(RoyaltyLauncher.ROYALTY_DIR, "launcher.properties"));
	
	
	private Image background = getResource("background.png");
	private Image fields = getResource("fields.png");
	private BufferedImage news = null;

	private boolean willRefresh;

	private JTextField usernameField = new JTextField(this.saver.get("username"));
	private JTextField passwordField = new JPasswordField();
	private STexturedCheckbox keeploginCheckBox = new STexturedCheckbox(getResource("keep-login.png"), getResource("keep-login-check.png"));
	private STexturedButton discordButton = new STexturedButton(getResource("register.png"), getResource("register-hover.png"));
	private STexturedButton disconnectButton = new STexturedButton(getResource("disconnect.png"), getResource("disconnect-hover.png"));
	private JLabel authLabel = new JLabel("", SwingConstants.CENTER);

	private STexturedButton playButton = new STexturedButton(getResource("play.png"), getResource("play-hover.png"));

	private STexturedButton quitButton = new STexturedButton(getResource("close.png"));
	private STexturedButton hideButton = new STexturedButton(getResource("hide.png"));
	private STexturedButton settingsButton = new STexturedButton(getResource("settings.png"));

	private STexturedButton discoverButton = new STexturedButton(getResource("discover.png"), getResource("discover-hover.png"));
	private SColoredBar progressBar = new SColoredBar(getTransparentWhite(25), getTransparentWhite(75));
	private JLabel infoLabel = new JLabel("Clique sur Jouer !", SwingConstants.CENTER);

	public LauncherPanel()
	{
		setLayout(null);
		setSize(853, 609);

		willRefresh = !ROYALTY_SAVER.get("access-token", "").equals("");

		usernameField.setForeground(Color.BLACK);
		usernameField.setFont(ROYALTY_FONT_LOW.deriveFont(22f));
		usernameField.setCaretColor(Color.BLACK);
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setBounds(39, 180, 220, 38);
		usernameField.setVisible(!willRefresh);
		add(usernameField);

		passwordField.setForeground(Color.BLACK);
		passwordField.setFont(this.passwordField.getFont().deriveFont(24.0F));
//		passwordField.setEchoChar('*');
		passwordField.setCaretColor(Color.BLACK);
		passwordField.setOpaque(false);
		passwordField.setBorder(null);
		passwordField.setBounds(39, 255, 220, 38);
		passwordField.setVisible(!willRefresh);
		add(passwordField);

		authLabel.setForeground(Color.WHITE);
		authLabel.setFont(ROYALTY_FONT_BOLD.deriveFont(24f));
		authLabel.setBounds(33, 159, 235, 142);
		authLabel.setVisible(willRefresh);
		add(authLabel);

		keeploginCheckBox.setBounds(31, 312, 156, 25);
		keeploginCheckBox.setEnabled(true);
		keeploginCheckBox.setChecked(willRefresh);
		add(keeploginCheckBox);

//		discordButton.setBounds(43, 467);
//		discordButton.addEventListener(this);
//		discordButton.setVisible(!willRefresh);
//		add(discordButton);
//
//		disconnectButton.setBounds(43, 467);
//		disconnectButton.addEventListener(this);
//		disconnectButton.setVisible(willRefresh);
//		add(disconnectButton);

		playButton.setBounds(43,370);
		playButton.addEventListener(this);
		add(playButton);

		quitButton.setBounds(802, 14);
		quitButton.addEventListener(this);
		add(quitButton);

		hideButton.setBounds(761, 14);
		hideButton.addEventListener(this);
		add(hideButton);

		settingsButton.setBounds(716, 14);
		settingsButton.addEventListener(this);
		add(settingsButton);

		discoverButton.setBounds(701, 455);
		discoverButton.addEventListener(this);
		add(discoverButton);

		progressBar.setBounds(0, 591, 853, 18);
		progressBar.setVisible(false);
		add(progressBar);

		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(ROYALTY_FONT_BOLD.deriveFont(26f));
		infoLabel.setBounds(0, 540, 853, 43);
		add(infoLabel);

		if (willRefresh)
			setAuthText("Bienvenue " + ROYALTY_SAVER.get("username"));

		new Thread("News Loader"){
			@Override
			public void run() {
				try {
					URLConnection connection = new URL(ROYALTY_URL.concat("/news.png")).openConnection();
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
					news = ImageIO.read(connection.getInputStream());
					repaint();
				} catch (IOException ex) {
					System.err.println("Impossible de charger l'image de news (" + ex + ")");
				}

			}
		}.start();
	}

	public SColoredBar getProgressBar()
	{
		return progressBar;
	}

	public void setAuthText(String authText) {
		authLabel.setText(authText);
	}

	public void setInfoText(String infoText)
	{
		infoLabel.setText(infoText);
	}

	@Override
	public void onEvent(SwingerEvent event)
	{
		if (event.getType() == SwingerEvent.BUTTON_CLICKED_EVENT) {
			if (event.getSource() == quitButton)
			{
				tryToExit();
			}
			else if (event.getSource() == hideButton)
			{
				LauncherFrame.getInstance().setState(Frame.ICONIFIED);
			}
			else if (event.getSource() == settingsButton)
			{
				OptionFrame.getInstance().setVisible(true);
			}
			else if (event.getSource() == discordButton)
			{
				browseOnDesktop("https://discord.gg/D4DuMxw");
			}
			else if (event.getSource() == discoverButton)
			{
				browseOnDesktop("https://discord.gg/D4DuMxw");
			}
			else if (event.getSource() == disconnectButton)
			{
				try {
					setAuthInfos("", "", "");
				} catch (AuthenticationException e) {
				}
				saveInfos(false);
				setFieldsEnabled(true);
				willRefresh = false;
			}
			else if (event.getSource() == playButton)
			{
				new Thread("Launch procedure") {
					@Override
					public void run() {
						setFieldsEnabled(false);
						
						setAuthText("Authentification...");
						try {
							if (willRefresh)
								refresh();
							else
								auth(usernameField.getText(), passwordField.getText());
						} catch (AuthenticationException ex) {
							setInfoText(ex.getErrorModel().getErrorMessage());
							setFieldsEnabled(true);
							willRefresh = false;
							return;
						}
						if(keeploginCheckBox.isChecked()) {
							RoyaltyLauncher.saveInfos(true);
//							saver.set("username", usernameField.getText());	//Save the username on the launcher.properties file
						}
//						saveInfos(keeploginCheckBox.isChecked());
						LauncherPanel.this.saver.set("username", LauncherPanel.this.usernameField.getText());
						willRefresh = true;

						try {
							RoyaltyLauncher.update();
						} catch (Exception ex) {
							ex.printStackTrace();
							RoyaltyLauncher.interruptUpdateThread();
							setInfoText("La mise à jour a échoué. (" + ex + ")");
							setFieldsEnabled(true);
							RoyaltyLauncher.reportException(ex);
							return;
						}

						try {
							RoyaltyLauncher.launch();
						} catch (LaunchException | InterruptedException ex) {
							ex.printStackTrace();
							setInfoText("Impossible de lancer le jeu. (" + ex + ")");
							playButton.setEnabled(true);
							RoyaltyLauncher.reportException(ex);
						}
					}
				}.start();

			}
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(background, 0, 0, this);
		if (usernameField.isVisible())
			g.drawImage(fields, 33, 159, this);
		if (news != null)
			g.drawImage(news, 329, 193, 506, 296, this);
	}

	private void setFieldsEnabled(boolean enabled)
	{
		usernameField.setVisible(enabled);
		passwordField.setVisible(enabled);
		playButton.setEnabled(enabled);
		discordButton.setVisible(enabled);
		authLabel.setVisible(!enabled);
		disconnectButton.setVisible(false);
	}
}
