---
layout: complex_article
title: User Guide - Ordering
permalink: /user-guide/ordering/
---

# Ordering

## Default behaviour

By default, expectations can be satisfied in any order. For example:

```scala
val mockedFunction = mockFunction[Int, Unit]
mockedFunction expects (1)
mockedFunction expects (2)
```

can be satisfied by:

```scala
mockedFunction(2)
mockedFunction(1)
```

## Ordered expectations

A specific sequence can be enforced with `inSequence`:

```scala
inSequence {
  mockedFunction expects (1)
  mockedFunction expects (2)
}
mockedFunction(2) // throws ExpectationException
mockedFunction(1)
```

Multiple sequences can be specified. As long as the calls within each sequence happen in the correct order, calls within different sequences can be interleaved. For example:

```scala
inSequence {
  mockedFunction expects (1)
  mockedFunction expects (2)
}
inSequence {
  mockedFunction expects (3)
  mockedFunction expects (4)
}
```

can be satisfied by:

```scala
mockedFunction(3)
mockedFunction(1)
mockedFunction(2)
mockedFunction(4)
```

## In any order expectations

To specify that there is no constraint on ordering, use `inAnyOrder` (just remember that there is an implicit `inAnyOrder` at the top level). Calls to `inSequence` and `inAnyOrder` can be arbitrarily nested. For example:

```scala
(mockedObject.a _).expects()
inSequence {
  (mockedObject.b _).expects()
  inAnyOrder {
    (mockedObject.c _).expects()
    inSequence {
      (mockedObject.d _).expects()
      (mockedObject.e _).expects()
    }
    (mockedObject.f _).expects()
  }
  (mockedObject.g _).expects()
}
```

All the following invocation orders of `mockedObject` methods are correct according to the above specification:

```
a, b, c, d, e, f, g
b, c, d, e, f, g, a
b, c, d, a, e, f, g
a, b, d, f, c, e, g
```
