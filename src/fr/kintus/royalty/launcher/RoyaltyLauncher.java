package fr.kintus.royalty.launcher;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import fr.theshark34.openauth.AuthPoints;
import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openauth.Authenticator;
import fr.theshark34.openauth.model.AuthAgent;
import fr.theshark34.openauth.model.response.AuthResponse;
import fr.theshark34.openauth.model.response.RefreshResponse;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.BeforeLaunchingEvent;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;


public class RoyaltyLauncher {
	
	public static final GameVersion ROYALTY_VERSION = new GameVersion("1.12.2", GameType.V1_8_HIGHER);
	public static final GameInfos ROYALTY_INFOS = new GameInfos("royalty", ROYALTY_VERSION, new GameTweak[] {GameTweak.FORGE});
	public static final File ROYALTY_DIR = ROYALTY_INFOS.getGameDir();
	public static final Saver ROYALTY_SAVER = new Saver(new File(ROYALTY_DIR, "royalty.properties"));
	public static final File ROYALTY_CRASH_DIR = new File(ROYALTY_DIR, "crash");
	public static final CrashReporter ROYALTY_CRASH = new CrashReporter(ROYALTY_INFOS.getServerName(), ROYALTY_CRASH_DIR);
	public static final String ROYALTY_URL = "http://149.91.81.63";
    public static final Authenticator ROYALTY_AUTHENTICATOR = new Authenticator("https://authserver.mojang.com/", AuthPoints.NORMAL_AUTH_POINTS);
	public static final SUpdate ROYALTY_UPDATER = new SUpdate(ROYALTY_URL.concat("/launcher"), ROYALTY_DIR);
	public static Font ROYALTY_FONT_BOLD;
	public static Font ROYALTY_FONT_LOW;
	private static AuthInfos authInfos;
	private static Thread updateThread;

	
        
    private static final boolean IS_64BIT = SystemUtils.is64bit0(); 
       
       
	public static void main(String[] args) {
		Swinger.setSystemLookNFeel();
		Swinger.setResourcePath("/");
	    RoyaltyLauncher.ROYALTY_DIR.mkdirs();
		RoyaltyLauncher.ROYALTY_CRASH_DIR.mkdirs();
	    try {
	    	File tmp = new File(RoyaltyLauncher.ROYALTY_DIR, "ram.txt");
	    	tmp.createNewFile();
	    	
	    	File tmp2 = new File(ROYALTY_DIR, "royalty.properties");
	    	tmp2.createNewFile();
	    } catch (IOException e1) {
	      e1.printStackTrace();
	    } 

		ROYALTY_UPDATER.addApplication(new FileDeleter());

		try {
			ROYALTY_FONT_BOLD = Font.createFont(Font.TRUETYPE_FONT, getResourceAsStream("edosz.ttf"));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(ROYALTY_FONT_BOLD);
			ROYALTY_FONT_LOW = Font.createFont(Font.TRUETYPE_FONT, getResourceAsStream("abeezee.ttf"));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(ROYALTY_FONT_LOW);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		new LauncherFrame().setVisible(true);
		
		
		System.out.println("Java version: " + System.getProperty("java.version"));
		if(IS_64BIT) { 
			System.out.println("Java in version x64 bits");
		} else {
			System.out.println("Java in version x86 bits");
		}

	}

	
	public static void auth(String username, String password) throws AuthenticationException {
	    AuthResponse response = ROYALTY_AUTHENTICATOR.authenticate(AuthAgent.MINECRAFT, username, password, "");
	    authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
	}
	
	
	public static void refresh() throws AuthenticationException {
		RefreshResponse response = ROYALTY_AUTHENTICATOR.refresh(
				ROYALTY_SAVER.get("access-token"), ROYALTY_SAVER.get("client-token")
		);

		setAuthInfos(
				response.getSelectedProfile().getName(),
				response.getAccessToken(),
				response.getSelectedProfile().getId()
		);
	}

	public static void setAuthInfos(String username, String accessToken, String uuid) throws AuthenticationException {
		authInfos = new AuthInfos(
				username,
				accessToken,
				uuid
		);
		LauncherFrame.getInstance().getPanel().setAuthText(
				"Bienvenue " + username
		);
		
	}

	public static void saveInfos(boolean keepLogin) {
		if(authInfos.getUsername() == null) { return; }
		
		ROYALTY_SAVER.set("username", authInfos.getUsername());
	}
	

	public static void update() throws Exception
	{
		updateThread = new Thread()
		{
			private int val;
			private int max;

			public void run()
			{
				SColoredBar progressBar = LauncherFrame.getInstance().getPanel().getProgressBar();
				progressBar.setVisible(true);
				while (!isInterrupted())
				{
					if (BarAPI.getNumberOfFileToDownload() == 0)
					{
						LauncherFrame.getInstance().getPanel().setInfoText("Vérification des fichiers...");
						continue;
					}
					this.val = (int) (BarAPI.getNumberOfTotalDownloadedBytes()/1000);
					this.max = (int) (BarAPI.getNumberOfTotalBytesToDownload()/1000);

					progressBar.setValue(this.val);
					progressBar.setMaximum(this.max);

					LauncherFrame.getInstance().getPanel().setInfoText("Téléchargement des fichiers " +
							BarAPI.getNumberOfDownloadedFiles() + "/" + BarAPI.getNumberOfFileToDownload() +
							" " + Swinger.percentage(this.val, this.max) + "%");
				}
				progressBar.setVisible(false);
			}
		};
		updateThread.start();
		ROYALTY_UPDATER.start();
		updateThread.interrupt();
	}

	  static void interruptThread() {
	    updateThread.isInterrupted();
	  }
	  
	  public static void interruptUpdateThread() {
	    updateThread.interrupt();
	  }
	

	
	public static void launch() throws LaunchException, InterruptedException {
		ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(ROYALTY_INFOS,
				GameFolder.BASIC, authInfos);
		
		AllowedMemory am;
		if(IS_64BIT) {
			am = AllowedMemory.XMX4G;				
		} else {
			am = AllowedMemory.XMX2G;				
		}
		
		
		try {
			am = AllowedMemory.valueOf(ROYALTY_SAVER.get("allowed-memory"));//, "XMX2G"));
		} catch (IllegalArgumentException ex) {
			// Pass
		} catch (NullPointerException e) {
			if(IS_64BIT) {
				am = AllowedMemory.XMX4G;				
			} else {
				am = AllowedMemory.XMX2G;				
			}
		}
		profile.getVmArgs().addAll(am.getVmArgs());

//		profile.getArgs().add("--demo");

		ExternalLauncher launcher = new ExternalLauncher(profile, new BeforeLaunchingEvent() {
			@Override
			public void onLaunching(ProcessBuilder processBuilder) {
				String javaPath = ROYALTY_SAVER.get("java-path", "");
				if (javaPath != null && !javaPath.equals(""))
					processBuilder.command().set(0, javaPath);
			}
		});
		
		LauncherFrame.getInstance().setVisible(false);
		int exitCode = launcher.launch().waitFor();
		System.out.println("\nMinecraft finished with exit code " + exitCode);
		System.exit(0);
		
	}
		
		
	
	
	
	  
	public static String reportException(Exception e)
	{
		try {
			File file = ROYALTY_CRASH.writeError(e);
			return file.getCanonicalPath();
		} catch (IOException ex) {
			ex.printStackTrace();
			return "les logs du launcher.";
		}
	}

	public static void browseOnDesktop(String uri)
	{
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(new URI(uri));
			} catch (URISyntaxException | IOException ex) {
				ex.printStackTrace();
			}
		} else {
			System.err.println("Browse on Desktop is not supported !");
			System.err.println("But here is the link : " + uri);
		}
	}

	public static void tryToExit() {
		if (BarAPI.getNumberOfDownloadedFiles() != BarAPI.getNumberOfFileToDownload() &&
				JOptionPane.showConfirmDialog(LauncherFrame.getInstance(),
						"Voulez-vous vraiment interrompre le téléchargement en cours ?", "Téléchargement en cours",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
				) != JOptionPane.YES_OPTION)
			return;

		System.exit(0);
	}

	public static AuthInfos getAuthInfos() {
		return authInfos;
	}

	private static InputStream getResourceAsStream(String name)
	{
		return Swinger.class.getResourceAsStream("/" + name);
	}

    public static boolean is64bit() { 
        return IS_64BIT; 
    } 
	
}
