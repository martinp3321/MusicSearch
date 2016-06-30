package servlets;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import musiclibrary.MultiThreadLibrary;
import musiclibrary.SongBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;



public class MusicServer {
	
	private static int PORT = 3001;

	public static void main(String[] args) {
		Server server = new Server(PORT);

		ServletContextHandler servhandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		
		
		servhandler.addEventListener(new ServletContextListener() {

			
			public void contextDestroyed(ServletContextEvent sce) {
				
			}
			
			public void contextInitialized(ServletContextEvent sce) {

				Path path = Paths.get("input/lastfm_subset");
				// build
				SongBuilder songBuild = new SongBuilder(path,10);
				MultiThreadLibrary lib = songBuild.getLibrary();

				sce.getServletContext().setAttribute("MusicLibrary", lib);
			}

		});
		
		servhandler.addServlet(UserServlet.class,    "/login");
		servhandler.addServlet(SearchServlet.class,    "/search");
		servhandler.addServlet(RegisterServlet.class, "/register");
		servhandler.addServlet(LoginDirectServlet.class,  "/logindirect");
		servhandler.addServlet(FavoriteDirectServlet.class,  "/favoritedirect");
		servhandler.addServlet(RegisterDirectServlet.class,  "/registerdirect");
		servhandler.addServlet(FavoriteServlet.class,  "/favorite");
		servhandler.addServlet(RedirectServlet.class, "/*");

		server.setHandler(servhandler);

		try {
			server.start();
			server.join();

			
		}
		catch (Exception ex) {
		
			System.exit(-1);
		}
	}
}