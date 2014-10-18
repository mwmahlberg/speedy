/*
 * Copyright 2014 Markus W Mahlberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import ${package}.config.AppConfig;
import com.github.mwmahlberg.speedy.SpeedyApplication;
import com.github.mwmahlberg.speedy.template.ThymeleafModule;

public class App {

	// TODO Change copyright info of this file
	
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