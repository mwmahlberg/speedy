# MongoDB for Speedy

## Dependencies

Basically, you have two dependencies

### maven artifact

Simply add this to your pom.xml 

    <dependency>
    	<groupId>com.github.mwmahlberg.speedy</groupId>
    	<artifactId>speedy-persist-morphia</artifactId>
    	<version>0.1.2-SNAPSHOT</version>
    </dependency>
    
### MongoDB server

You can either [install MongoDB][installation] on one of your machines or use [mongolab's sandbox][mongolab] for free.

## Usage

Like every other module, the only thing you need to do is to tell Speedy to use the module


    SpeedyApplication app = new SpeedyApplication("org.example.webapp");
    app.configure(args, new AppConfig(), new MorphiaModule("mongodb://10.0.0.1:38123/");
    app.run();
    
The connection string `"mongodb://10.0.0.1:38123/"`is optional. When you leave it out, the client connects to localhost on port 27017 with no authentication.

For the syntax of the connection string, please see the [API docs of the MongoClient class][mongoclient]. 

The module will scan your application for [entities][entities], map them automatically and provide an injectable [datastore][datastore]:

    package org.example.webapp.controller;

    import java.util.HashMap;

    import javax.inject.Inject;
    import javax.ws.rs.GET;
    import javax.ws.rs.Path;
    import javax.ws.rs.PathParam;
    import javax.ws.rs.core.MediaType;
    import javax.ws.rs.core.Response;
    import javax.ws.rs.core.Response.Status;

    import org.example.webapp.Greeting;
    import org.mongodb.morphia.Datastore;

    @Path("/persistence")
    public class Persistence {

	  Datastore ds;

	  @Inject
	  public Persistence(Datastore ds) {
		this.ds = ds;
	  }
	
	  @GET
	  @Path("/insert/{text}")
	  public Response insert(@PathParam("text") String text){

		HashMap<String, String> sender = new HashMap<String, String>();
		sender.put("sender", "Morphia");
		
		Greeting greet = new Greeting();
		greet.setMessage("Hello, "+text);
		greet.setSender(sender);
		ds.save(greet);
		
		return Response.status(Status.CREATED).entity(greet).type(MediaType.APPLICATION_JSON).build();
		
	  }
	
    }
    
 You can use the datastore as [documented in the Morphia docs][datastore].



[installation]: http://docs.mongodb.org/manual/installation/
[mongolab]: https://mongolab.com/plans/pricing/
[entities]: https://github.com/mongodb/morphia/wiki/EntityAnnotation
[datastore]: https://github.com/mongodb/morphia/wiki/Datastore
[mongoclient]: http://api.mongodb.org/java/current/com/mongodb/MongoClientURI.html