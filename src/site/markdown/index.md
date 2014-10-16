[![Build Status](https://travis-ci.org/mwmahlberg/speedy.svg?branch=develop)](https://travis-ci.org/mwmahlberg/speedy)

Speedy Web Application Framework
================================

The Speedy Web Application Framework (Speedy for short) is aimed to make it as simple as possible to get a web application up and running.

It is a mouse standing on the shoulder of Giants, namely:

 * [Jetty][jetty] as the [Servlet Engine][wikiservletEngine]
 * [Google's Guice][guice] for [dependency injection][wikidi]
 * [Jersey][jersey] ([v1.18][jersey1.18]) for query routing
 * [Jackson2][jackson] for easy serializing of objects into JSON and XML
 * [Thymeleaf][thymeleaf] for HTML rendering
 * [Maven][maven] for tying all this and your web application up into a single executable JAR
 
Since the docs are a bit thin except for a quickstart and an introduction, please refer to the documentation of those projects in case you need to know something.

To start with Speedy, please read the [quickstart][quickstart] and the slightly longer [introduction][introduction]

## The missing parts
Speedy is under heavy development at the moment and it is only 2 weeks old at the time of this writing, so there are a few parts missing yet.

### Persistence Layer :(

There isn't a persistence layer yet, but it is the next big thing to come. The first supported database will be [MongoDB][mongodb] via [Morphia][morphia], as this is both relatively easy to implement and use.
Next, [OpenJPA][openjpa] and [HSQLDB][hsqldb] are on the list, though this is not too sure yet.

> What's going to be next depends on You! [File a ticket][issuetracker] and request an enhancement for your favourite DB.

### Decent documentation

Not much to say here. Contributions are more than welcome!

### Tests

Everything is missing at the moment. There are no unit tests, no integration tests, just explorative tests by the "team". More likely than not, this is going to be done even before the persistence layer is added.

[jetty]: http://www.eclipse.org/jetty/
[wikiservletEngine]:http://en.wikipedia.org/wiki/Java_Servlet
[guice]:https://github.com/google/guice
[wikidi]:http://en.wikipedia.org/wiki/Dependency_injection
[jersey]:https://jersey.java.net
[jersey1.18]:https://jersey.java.net/documentation/1.18/index.html
[jackson]: http://wiki.fasterxml.com/JacksonHome
[thymeleaf]: http://www.thymeleaf.org
[maven]: http://maven.apache.org
[quickstart]: quickstart.html
[introduction]: introduction.html
[mongodb]: http://www.mongodb.org
[morphia]: https://github.com/mongodb/morphia
[openjpa]: http://openjpa.apache.org
[hsqldb]: http://hsqldb.org
[issuetracker]: https://github.com/mwmahlberg/speedy/issues