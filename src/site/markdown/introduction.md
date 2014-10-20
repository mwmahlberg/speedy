# Introduction

If you have not read the [quickstart][quick], please do so now.

While the quickstart gave a very quick introduction, now we are going to do something interesting.

## Adding another controller

Create a project as show in the [quickstart][quick]. Now add a new [POJO][pojo] in the controller directory called `Intro.class`.
Your directory structure should look like this now:

    ExampleApp/
    ├── pom.xml
    └── src
        └── main
            └── java
                └── org
                    └── example
                        └── webapp
                            ├── App.java
                            ├── config
                            │   └── AppConfig.java
                            └── controller
                                ├── Index.java
                                └── Intro.java

Basically, you could create your controllers everywhere in your package, but it makes sense to keep them together.

Now, let's modify the controller so that it actually does something:

    package org.example.webapp.controller;

    import javax.ws.rs.GET;
    import javax.ws.rs.Path;

    @Path("/intro")
    public class Intro {
  
      @GET
      @Path("/plain")
      public String plain(){
        return "My First Controller!";
      }
  
    }

That basically is just o copy of `Index.java`, but we will do interesting stuff shortly. As for now, compile and run the application.
Now, when you call http://localhost:8080/intro/plain , you should see the text "My First Controller!" displayed in your browser.

There are a few things to note here:

 * You did not have to register your new controller. It is found automagically by Speedy.
 * You can extend the base path of a controller by using the `@Path` annotation on your methods
 * Creating new controllers is quite easy and straightforward
 * Deploying your application is as easy as copying a single file and running a single command

## Returning JSON data
The next thing we want to do is to return some [JSON][wikijson]. This is achieved easily by adding a simple annotation to a method and return some data suitable for being converted to JSON.

We do this by modifying our Controller so that it looks like this

    package org.example.webapp.controller;

    import java.util.HashMap;
    import java.util.Map;

    import javax.ws.rs.GET;
    import javax.ws.rs.Path;
    import javax.ws.rs.Produces;

    @Path("/intro")
    public class Intro {
  
      @GET
      @Path("/plain")
      public String plain(){
        return "My First Controller!";
      }

      @GET
      @Path("/json")
      @Produces("application/json")
      public Map<String, String> json() {
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("greeting", "Hello, Json!");
        return map;
      }

    }

When calling http://localhost:8080/intro/json a JSON document which looks like

    {"greeting":"Hello, Json!"}

should be returned.

Again, there are a few things to note:

 * Basic datatypes and collections thereof are converted to JSON automatically (though fine grained control is possible, see below)
 * You can return any data type from your methods, though all but the basic data types and collections thereof can not be converted automatically
 * Adding new methods is quite easy. ;)
 
## Returning complex data
 
As for now, we have only returned very basic data. Now we get to the really interesting things. Speedy uses the [Jackson library][jackson] for serializing data. In order to be able to serialize complex data, Jackson needs some hints how to serialize this data. In Jackson, those hints are annotatins,
and in Speedy [JAXB][wikijaxb] annotations are used by default. Additionally, those complex types have to be [Java Beans][wikibeans]. 

### Creating a JAXB annotated bean

This sounds more complex than it actually is. The following example is a perfectly valid bean which can be converted to both JSON and XML:

    package org.example.webapp;

    import java.util.Map;

    import javax.xml.bind.annotation.XmlRootElement;

    @XmlRootElement
    public class Greeting {

    	String message;
	
    	Integer length;
	
    	Map<String,String> sender;

    	public String getMessage() {
    		return message;
    	}

    	public void setMessage(String message) {
    		this.message = message;
    	}

    	public Integer getLength() {
    		return length;
    	}

    	public void setLength(Integer length) {
    		this.length = length;
    	}

    	public Map<String, String> getSender() {
    		return sender;
    	}

    	public void setSender(Map<String, String> sender) {
    		this.sender = sender;
    	}
	
    }

### Creating a method returning a complex type
Now, all we have to do is to create a new instance of our complex type and return it. Now our class looks like this:

    package org.example.webapp.controller;

    import java.util.HashMap;
    import java.util.Map;

    import javax.ws.rs.GET;
    import javax.ws.rs.Path;
    import javax.ws.rs.Produces;

    import org.example.webapp.Greeting;

    @Path("/intro")
    public class Intro {
  
      @GET
      @Path("/plain")
      public String plain(){
        return "My First Controller!";
      }

      @GET
      @Path("/json")
      @Produces("application/json")
      public Map<String, String> json() {
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("greeting", "Hello, Json!");
        return map;
      }

      @GET
      @Path("/complex")
      @Produces("application/xml")
      public Greeting complexXml() {
	  
    	  Greeting greet = new Greeting();
	  
    	  greet.setMessage("Hello, complex XML!");
    	  greet.setLength(greet.getMessage().length());
	  
    	  HashMap<String,String> sender = new HashMap<String, String>();
    	  sender.put("class", this.getClass().getName());
	  
    	  greet.setSender(sender);
	  
    	  return greet;
      }
    }
    
After you compile and run your project as shown in quickstart, calling http://localhost:8080/complex/xml will return an XML document which looks like

    <greeting>
      <length>19</length>
      <message>Hello, complex XML!</message>
      <sender>
        <entry>
          <key>class</key>
          <value>org.example.webapp.controller.Intro</value>
        </entry>
      </sender>
    </greeting>

Now, that's pretty interesting, isn't it? Now let's assume you want to return this complex type either as JSON or XML, depending on the [HTTP request's "Accept" header][wikirequest]. Actually, it is harder to test that than it is to implement.

For the implementation, you simply modify the `@Produces` annotation of the `complex()` method accordingly:

```java
    @GET
    @Path("/complex")
    @Produces({"application/xml","application/json"})
    public Greeting complexXml() {
	  
  	  Greeting greet = new Greeting();
	  
  	  greet.setMessage("Hello, complex World!");
  	  greet.setLength(greet.getMessage().length());
	  
  	  HashMap<String,String> sender = new HashMap<String, String>();
  	  sender.put("class", this.getClass().getName());
	  
  	  greet.setSender(sender);
	  
  	  return greet;
    }
```


Compile and start your app. You need a tool like [curl][wikicurl] or wget[wikiwget] to call http://localhost:8080/intro/complex with different "Accept" headers. In this introduction, we will use curl.

    curl -i -v -H "Accept: application/xml" http://localhost:8080/intro/complex

should return something like:

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <greeting>
      <length>19</length>
      <message>Hello, complex World!</message>
      <sender>
        <entry>
          <key>class</key>
          <value>org.example.webapp.controller.Intro</value>
        </entry>
      </sender>
    </greeting>
    
Now, when we change the "Accept" header to only accept JSON, like

    curl -i -v -H "Accept: application/json" http://localhost:8080/intro/complex

a JSON document looking like

    {
        "length": 19,
        "message": "Hello, complex World!",
        "sender": {
            "class": "org.example.webapp.controller.Intro"
        }
    }

## Returning (X)HTML(5)

At the moment, Speedy only supports one template engine: [Thymeleaf][thymeleaf]. The good thing: Thymeleaf is extremely powerful and comes with almost all features one can think of. Plus, it allows natural templating: All Thymeleaf templates are valid HTML, so not only the rendered page, but the template already is displayed correctly in your browser.

The use of the Thymeleaf template engine is already configured by the maven archetype, so there is nothing to do here.


### Create the template

Let's create a very simple template in `src/main/resources/META-INF/templates`. Speedy expects all templates to be in or beyond this base path. For details about the template labguage, please see the Theleaf documentation. We'll skip the details for now.

    <!DOCTYPE html>
    <html xmlns="http://www.w3.org/1999/xhtml"
    	xmlns:th="http://www.thymeleaf.org">
    <head>
    <meta charset="UTF-8" />
    <title th:text="${title}">Insert title here</title>
    </head>
    <body>
    	<h1 th:text="${greeting.message}">Hello, Template World!</h1>
    	<p>
    		from <span th:text="${greeting.sender.name}">the sender</span>
    	</p>
    </body>
    </html>
    
You directory structure should look like this now:

    ExampleApp/
    ├── pom.xml
    └── src
        └── main
            ├── java
            │   └── org
            │       └── example
            │           └── webapp
            │               ├── App.java
            │               ├── Greeting.java
            │               ├── config
            │               │   └── AppConfig.java
            │               └── controller
            │                   ├── Index.java
            │                   └── Intro.java
            └── resources
                └── META-INF
                    └── templates
                        └── FirstTemplate.html
                        

Open the template file in a browser. It doesn't look nice, but it's perfectly valid HTML5, so it is displayed as intended. No strange template tags or control structures getting in your way. Great for prototype presentations! And it makes the cooperation between designer and Frontend developer much easier.

### Create a controller that returns HTML

Until now, we only returned some object and it was automatically rendered to something meaningful. Now, we add the design element. And to let Speedy know which design we want to have applied to our data, we have to tell Speedy which template to use additionally to the data we want to have presented to the client.

This can be done using a simple return value which is called [`Viewable`][viewableapi]. The constructor takes two arguments: A template name and the data you want to apply on that template.

So let's write our method:

    package org.example.webapp.controller;

    import java.util.HashMap;
    import java.util.Map;

    import javax.ws.rs.GET;
    import javax.ws.rs.Path;
    import javax.ws.rs.Produces;
    import javax.ws.rs.core.MediaType;
    import javax.ws.rs.core.Response;

    import org.example.webapp.Greeting;

    import com.sun.jersey.api.view.Viewable;

    @Path("/intro")
    public class Intro {
      
      @GET
      @Path("/plain")
      public String plain(){
        return "My First Controller!";
      }
      
      @GET
      @Path("/json")
      @Produces("application/json")
      public Map<String, String> json() {
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("greeting", "Hello, Json!");
        return map;
      }
      
      @GET
      @Path("/complex")
      @Produces({"application/json","application/xml"})
      public Greeting complexXml() {
	  
    	  Greeting greet = new Greeting();
	  
    	  greet.setMessage("Hello, complex World!");
    	  greet.setLength(greet.getMessage().length());
	  
    	  HashMap<String,String> sender = new HashMap<String, String>();
    	  sender.put("class", this.getClass().getName());
	  
    	  greet.setSender(sender);
	  
    	  return greet;
      }
      
      @GET
      @Path("/viewable")
      @Produces(MediaType.TEXT_HTML)
      public Viewable viewable() {
	  
    	  Greeting greet = new Greeting();
	  
    	  greet.setMessage("Hello, viewable World!");
	  
    	  HashMap<String, String> sender = new HashMap<String, String>();
    	  sender.put("name", this.getClass().getName());
    	  greet.setSender(sender);
	  
    	  HashMap<String, Object> model = new HashMap<String, Object>();
	  
    	  model.put("greeting", greet);
	  
    	  return new Viewable("FirstTemplate", model);
      }
      
    }
    
Compile the project as usual, run it and open http://localhost:8080/intro/viewable and you will see the rendered HTML page!

Things to note here:

 * The template name does not have ".html" appended
 * You can create subdirectories under `META-INF/templates`. The template would then be adressed as `<subdir>/<templateName>`
 * As of now, there is no possibility to have the same path return all three content types. It is either HTML or JSON and/or XML.
   This _may_ change in future versions.
 * Viewable can take any `Object` as an argument. It will simply pass it to the template engine as is.

### More granular control over the response

So far, we worked with annotations and implicit assumptions to create our response. In the last part of the introduction, it is shown how to take more control over what is returned to the client.

We use the [`ResponseBuilder`][responsebuilder] for this:

    @GET
    @Path("/response")
    public Response html() {

      return Response.status(203).entity(this.viewable()).type("text/html").build();
      
    }

The ResponseBuilder allows us to create our response dynamically. Note that entity can take every argument Speedy is able to serialize, for example a `Greeting` object from this example.

This ends the short introduction. Please [file a ticket][issues] if you would like to see parts of the documentation expanded. Since time is a limited resource, please be specific. Let's call it an agile approach to documentation. ;)

[quick]: quickstart.html
[pojo]: http://en.wikipedia.org/wiki/Plain_Old_Java_Object
[wikijson]: http://de.wikipedia.org/wiki/JavaScript_Object_Notation
[jackson]: http://wiki.fasterxml.com/JacksonHome
[wikibeans]: http://en.wikipedia.org/wiki/JavaBeans
[wikijaxb]: http://en.wikipedia.org/wiki/Java_Architecture_for_XML_Binding
[wikirequest]: http://en.wikipedia.org/wiki/List_of_HTTP_header_fields#Request_fields
[thymeleaf]: http://www.thymeleaf.org
[wikicurl]: http://en.wikipedia.org/wiki/CURL
[wikiwget]: http://en.wikipedia.org/wiki/Wget
[viewableapi]: https://jersey.java.net/apidocs/1.18/jersey/com/sun/jersey/api/view/Viewable.html
[responsebuilder]: https://jersey.java.net/apidocs/1.18/jersey/javax/ws/rs/core/Response.html
[issues]: https://github.com/mwmahlberg/speedy/issues