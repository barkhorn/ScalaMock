# ScalaMock 
[![Build Status](https://travis-ci.org/paulbutcher/ScalaMock.svg?branch=master)](https://travis-ci.org/paulbutcher/ScalaMock) [![Scaladex](https://index.scala-lang.org/paulbutcher/scalamock/scalamock-scalatest-support/latest.svg?color=orange)](https://index.scala-lang.org/paulbutcher/scalamock) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/d7250cea177b468c94bb07eb8d3366a4)](https://www.codacy.com/app/barkhorn/ScalaMock?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=paulbutcher/ScalaMock&amp;utm_campaign=Badge_Grade)

Native Scala mocking.

Official website: [http://scalamock.org/](http://scalamock.org/)

## Examples

### Expectations-First Style

```scala
test("drawline interaction with turtle") {
  // Create mock Turtle object
  val m = mock[Turtle]
  
  // Set expectations
  (m.setPosition _).expects(10.0, 10.0)
  (m.forward _).expects(5.0)
  (m.getPosition _).expects().returning(15.0, 10.0)

  // Exercise System Under Test
  drawLine(m, (10.0, 10.0), (15.0, 10.0))
}
```

### Record-then-Verify (Mockito) Style

```scala
test("drawline interaction with turtle") {
  // Create stub Turtle
  val m = stub[Turtle]
  
  // Setup return values
  (m.getPosition _).when().returns(15.0, 10.0)

  // Exercise System Under Test
  drawLine(m, (10.0, 10.0), (15.0, 10.0))

  // Verify expectations met
  (m.setPosition _).verify(10.0, 10.0)
  (m.forward _).verify(5.0)
}
```

A more complete example is on our [Quickstart](http://scalamock.org/quick-start/) page.

## Features

* Fully typesafe
* Full support for Scala features such as:
  * Polymorphic (type parameterised) methods
  * Operators (methods with symbolic names)
  * Overloaded methods
  * Type constraints
* ScalaTest and Specs2 integration
* Mock and Stub support
* Macro Mocks and Proxy Mocks
* Scala.js support
* built for Scala 2.10, 2.11, 2.12

## Using ScalaMock

Artefacts are published to Maven Central and JCenter.

For ScalaTest, to use ScalaMock in your Tests, add the following to your `build.sbt`:

```scala
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % Test
```

## Documentation

For usage in Maven or Gradle, integration with Specs2, and more example examples see the [User Guide](http://scalamock.org/user-guide/)


### Acknowledgements

YourKit is kindly supporting open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:
[YourKit Java Profiler](http://www.yourkit.com/java/profiler/index.jsp) and
[YourKit .NET Profiler](http://www.yourkit.com/.net/profiler/index.jsp).
