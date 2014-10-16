[![Build Status](https://travis-ci.org/mwmahlberg/speedy.svg?branch=develop)](https://travis-ci.org/mwmahlberg/speedy)

# Speedy Web Application Framework

Not really happy with the lightweight web frameworks for Java around (some where too lightweight, where others didn't really deserver the label), I threw together Speedy.

Speedy consists of the following parts:

 * [Guice][guice], Google's approach on Dependency injection
 * [Jersey 1.18][jersey], The reference implementation of JAX-RS
 * [Jackson2][jackson], an awesome powerful serialization/deserialization framework
 * [Jetty][jetty], a relatively lightweight servlet engine
 
While this combination isn't exactly new (Dropwizard uses it), I wanted to keep it as basic as possible while allowing the user maximum flexibility.
The aim is to provide a framework which makes it as easy as possible to create web applications by using POJOs and some annotations and to hide the painful details from the developer.
Running the webapp is aimed to be as easy as possible, too:

    java -jar yourWebApp.jar -p 9090 -h 192.168.0.1

will start your web application on port 9090 and binding it to the IP address 192.168.0.1.

Please see http://mwmahlberg.github.io/speedy for details.

Please note that while there are release packages available via maven central, this project is still highly experimental and by no means fit for productions use.

Any suggestion, bug reports, enhancement wishes and alike are welcome!

[guice]: https://github.com/google/guice
[jersey]: https://jersey.java.net/documentation/1.18/index.html
[jackson]: http://wiki.fasterxml.com/JacksonHome
[jetty]: http://www.eclipse.org/jetty/