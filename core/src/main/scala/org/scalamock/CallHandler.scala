// Copyright (c) 2011-2012 Paul Butcher
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package org.scalamock

abstract class CallHandler[R](target: FakeFunction, private[scalamock] val argumentMatcher: Product => Boolean) extends Handler {
  
  type Derived <: CallHandler[R]
  
  def repeat(range: Range) = {
    expectedCalls = range
    this.asInstanceOf[Derived]
  }
  
  def repeat(count: Int): CallHandler[R] = repeat(count to count)
  
  def never() = repeat(0)
  def once() = repeat(1)
  def twice() = repeat(2)
  
  def anyNumberOfTimes() = repeat(0 to scala.Int.MaxValue - 1)
  def atLeastOnce() = repeat(1 to scala.Int.MaxValue - 1)
  def atLeastTwice() = repeat(2 to scala.Int.MaxValue - 1)

  def noMoreThanOnce() = repeat(0 to 1)
  def noMoreThanTwice() = repeat(0 to 2)
  
  def repeated(range: Range) = repeat(range)
  def repeated(count: Int) = repeat(count)
  def times() = this.asInstanceOf[Derived]

  def returns(value: R) = onCall({_ => value})
  def returning(value: R) = returns(value)
  
  def throws(e: Throwable) = onCall({_ => throw e})
  def throwing(e: Throwable) = throws(e)
  
  def onCall(handler: Product => R) = {
    onCallHandler = handler
    this.asInstanceOf[Derived]
  }
  
  override def toString = s"${target}${argumentMatcher}"

  private[scalamock] def handle(call: Call) = {
    if (!isExhausted && argumentMatcher(call.arguments)) {
      actualCalls += 1
      Some(onCallHandler(call.arguments))
    } else {
      None
    }
  }
  
  private[scalamock] def verify(call: Call) = false
  
  private[scalamock] def isSatisfied = expectedCalls contains actualCalls
  
  private[scalamock] def isExhausted = expectedCalls.last <= actualCalls
  
  private[scalamock] var expectedCalls: Range = 1 to 1
  private[scalamock] var actualCalls: Int = 0
  private[scalamock] var onCallHandler: Product => R = {_ => null.asInstanceOf[R]}
}

trait Verify { self: CallHandler[_] =>
  
  private[scalamock] override def handle(call: Call) = sys.error("verify should appear after all code under test has been exercised")
  
  private[scalamock] override def verify(call: Call) = {
    if (argumentMatcher(call.arguments)) {
      actualCalls += 1
      true
    } else {
      false
    }
  }
}

class CallHandler0[R](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler0[R]
  
  def this(target: FakeFunction) = this(target, new ArgumentMatcher(None))
  
  def onCall(handler: () => R): CallHandler0[R] = super.onCall(new FunctionAdapter0(handler))
}

class CallHandler1[T1, R](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {
  
  type Derived = CallHandler1[T1, R]

  def this(target: FakeFunction, v1: MockParameter[T1]) = this(target, new ArgumentMatcher(new Tuple1(v1)))
  
  def onCall(handler: (T1) => R): CallHandler1[T1, R] = super.onCall(new FunctionAdapter1(handler))
}

class CallHandler2[T1, T2, R](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {
  
  type Derived = CallHandler2[T1, T2, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2]) = this(target, new ArgumentMatcher((v1, v2)))
  
  def onCall(handler: (T1, T2) => R): CallHandler2[T1, T2, R] = super.onCall(new FunctionAdapter2(handler))
}