package org.scalamock.scalatest

import org.scalamock.clazz.{Mock => MacroMock}
import org.scalamock.proxy.ProxyMockFactory

import scala.reflect.ClassTag

/**
  * allows combining of macro mocks wih proxy mocks in the same Suite
  * {{{
  * val macroMock = mock[Foo]
  * val proxyMock = Proxy.mock[Bar]
  * }}}
 * @deprecated Proxy mocks are going to go away
  */
@deprecated("Proxy mocks are going to go away", "4.5.0")
trait MixedMockFactory extends AbstractMockFactory with MacroMock {

  object Proxy extends ProxyMockFactory {
    import org.scalamock.proxy._
    def mock[T: ClassTag]: T with Mock = super.mock[T]
    def stub[T: ClassTag]: T with Stub = super.stub[T]
  }
}
