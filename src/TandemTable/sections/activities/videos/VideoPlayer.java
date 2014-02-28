package TandemTable.sections.activities.videos;

import javax.swing.JFrame;



import processing.core.PApplet;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import TandemTable.sections.activities.twitter.ContentGetter;
import TandemTable.sections.activities.twitter.VideoController;

public class VideoPlayer{
	public EmbeddedMediaPlayerComponent mediaPlayerComponent;
	public final JFrame frame = new JFrame();
	VideoActivity vAct;
	ContentGetter cg;
	VideoController vc;
	PApplet applet;
	int user;

	int x, y, width, height;

	@SuppressWarnings("serial")
	public VideoPlayer(PApplet applet, final VideoActivity vAct, int x, int y, int width, int height, final int user){
		this.vAct = vAct;
		this.applet = applet;
		this.user = user;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		String[] opt = new String[] {"--no-video-title-show"};
		if(user == 2){
			opt = new String[] {"--video-filter=transform", "--transform-type=180", "--no-video-title-show"};
			//opt = new String[] {"--video-filter=clone","--clone-vout-list=opengl, glx", "--clone-count=2"};
		}	
		final String[] options = opt;

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent(){
			public void error(MediaPlayer mediaPlayer){
				System.out.println("Video Player Error");
				removePlayerVAct();
			}

			public void finished(MediaPlayer mediaPlayer) {
				removePlayerVAct();				
			}

			protected String[] onGetMediaPlayerFactoryArgs() {
				return options;
			}
		};

		frame.setContentPane(mediaPlayerComponent);
		frame.setBounds(x, y, width, height);
		frame.setUndecorated(true);


	}

	public void removePlayerVAct(){
		if(vAct.playerActive){
			vAct.playerActive = false;

			vAct.videoPlayer1.mediaPlayerComponent.getMediaPlayer().release();
			vAct.videoPlayer1.mediaPlayerComponent.removeAll();
			vAct.videoPlayer1.frame.dispose();

			vAct.videoPlayer2.mediaPlayerComponent.getMediaPlayer().release();
			vAct.videoPlayer2.mediaPlayerComponent.removeAll();
			vAct.videoPlayer2.frame.dispose();

			vAct.videoFlag1 = false;
			//vAct.videoPlayer2.remove();
			vAct.videoFlag2 = false;
			//vAct.videoPlayer1.remove();

			vAct.resumeCurrents();
			vAct.play1.setActive(false);
			vAct.pause1.setActive(false);
			vAct.stop1.setActive(false);
			vAct.play2.setActive(false);
			vAct.pause2.setActive(false);
			vAct.stop2.setActive(false);
			vAct.cBackground.setActive(false);
			vAct.moreVideos1.setActive(true);
			vAct.moreVideos2.setActive(true);
			if(vAct.bothL){

				vAct.changeL1.setActive(true);
				vAct.changeL2.setActive(true);
			}
		}
	}

	@SuppressWarnings("serial")
	public VideoPlayer(int x, int y, int width, int height, PApplet applet, final ContentGetter cg, final VideoController vc, int user){
		this.cg = cg;
		this.applet = applet;
		this.user = user;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		cg.loading.setActive(true);
		this.vc = vc;

		String[] opt = new String[] {"--no-video-title-show"};
		if(user == 2){
			opt = new String[] {"--video-filter=transform", "--transform-type=180", "--no-video-title-show"};
		}	
		final String[] options = opt;


		mediaPlayerComponent = new EmbeddedMediaPlayerComponent(){
			public void error(MediaPlayer mediaPlayer){
				System.out.println("Video Player Error");
				removePlayercg();
			}

			public void finished(MediaPlayer mediaPlayer) {
				removePlayercg();
			}

			protected String[] onGetMediaPlayerFactoryArgs() {
				return options;
			}
		};
		frame.setContentPane(mediaPlayerComponent);
		frame.setBounds(x, y, width, height);
		frame.setUndecorated(true);

	}

	public void removePlayercg(){
		if(vc.playerActive){
			vc.playerActive = false;
			vc.videoPlayer1.mediaPlayerComponent.getMediaPlayer().release();
			vc.videoPlayer1.mediaPlayerComponent.removeAll();
			vc.videoPlayer1.frame.dispose();

			vc.videoPlayer2.mediaPlayerComponent.getMediaPlayer().release();
			vc.videoPlayer2.mediaPlayerComponent.removeAll();
			vc.videoPlayer2.frame.dispose();

			cg.videoFlag = false;

			vc.play1.setActive(false);
			vc.pause1.setActive(false);
			vc.stop1.setActive(false);
			vc.play2.setActive(false);
			vc.pause2.setActive(false);
			vc.stop2.setActive(false);
			vc.cBackground.setActive(false);
		}
	}



	public void init(String videoLink){


		mediaPlayerComponent.getMediaPlayer().prepareMedia(videoLink);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);

		if(user == 2){
			mediaPlayerComponent.getMediaPlayer().mute();
		}
	}

	public void startVideo(){
		mediaPlayerComponent.getMediaPlayer().play();
		vAct.playerActive = true;

	}

	public void startVideocg(){
		mediaPlayerComponent.getMediaPlayer().play();
		vc.playerActive = true;
		cg.loading.setActive(false);

	}

	public void remove(){
		mediaPlayerComponent.getMediaPlayer().stop();
		mediaPlayerComponent.getMediaPlayer().release();
		frame.dispose();
		if(vAct != null){

			if(user == 1){
				vAct.videoFlag1 = false;
			} else if (user == 2){
				vAct.videoFlag2 = false;
			}
		}
		if(cg != null){
			vc.playerActive = false;
			cg.videoFlag = false;
		}

	}


	public String getVideoStream(String href){
		/////////////
		// Old way

		/*String[] response = applet.loadStrings(href);
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		String resp2 = "";
		for(String s: response){
			resp2 += " " + s;

		}
		int index = resp2.indexOf("url_encoded_fmt_stream_map");
		resp2 = resp2.substring(index + 27);

		String[] ss = resp2.split("=");
		String links = ss[0];
		System.out.println(links);
		String script2 = "var s=unescape(links);" +
				"var abc = s.split(',url=');" +
				"var fmt=abc[0].split('|')[0];" + 
				"var url = fmt.substring(4,fmt.indexOf('fallback_host')-1);" + 
				"var a = unescape(unescape(url));" +
				"links = a.split('url%3D')[0];";

		try {
			// evaluate JavaScript code from String
			engine.put("links",links);
			engine.eval(script2);
			links = (String) engine.get("links");
			return links;
		} catch (ScriptException e) {
			e.printStackTrace();
		}*/


		/////////////////////
		// youtube-dl.exe way
		/*String links = "";
			Process process;
			try {
				process = new ProcessBuilder("youtube-dl.exe","--get-url", href).start();
				InputStream in = process.getInputStream() ;
				 links = convertStreamToString(in).trim();
				 //System.out.println(links);


				 process.destroy() ;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		 */

		///////////
		// New way
		
		String[] response = applet.loadStrings(href);
		// create a script engine manager
		// create a JavaScript engine
		String resp2 = "";
		for(String s: response){
			resp2 += " " + s;

		}
		int index = resp2.indexOf(".xml\");yt.preload.start(\"");
		int index2 = resp2.indexOf("\");</script>");
		String links = resp2.substring(index + 25, index2).trim();

		links = links.replaceFirst("generate_204", "videoplayback");

		links = links.replace("\\u0026", "&");
		links = links.replace("\\", "");


		return links;
	}

	String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	///////////////////////////////////////////////////////Change "C:\\Program Files\\VLC" !!!!!!!!!!!
	//////////////////////////////////
	//TODO close the video player when finished! please!
	//public static void loadVLClibs(){   
	//NativeLibrary.addSearchPath(
	//		RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VLC");
	//Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
	//}

}
