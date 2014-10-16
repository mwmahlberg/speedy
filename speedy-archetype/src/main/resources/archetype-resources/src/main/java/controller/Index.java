#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class Index {

	@GET
	public String sayHello() {
		return "Hello, Speedyworld!";
	}
}
