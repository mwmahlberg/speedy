#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import ${package}.config.AppConfig;
import com.github.mwmahlberg.speedy.SpeedyApplication;
import com.github.mwmahlberg.speedy.template.ThymeleafModule;

public class App {

	public static void main(String[] args) {
		/*
		 * Create a new SpeedyApplication and tell it where to
		 * look for components
		 */
		SpeedyApplication app = new SpeedyApplication("${package}");
		
		/*
		 * Configure the app using the command line args
		 * and any number of Guice modules you define in your
		 * application
		 */
		app.configure(args, new AppConfig(), new ThymeleafModule());
		
		try {
			app.run();
		} catch (Exception e) {
			
			e.printStackTrace();
			System.exit(1);
		}
	}

}
