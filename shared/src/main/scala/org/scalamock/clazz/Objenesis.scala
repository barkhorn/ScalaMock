package org.scalamock.clazz

import org.objenesis.ObjenesisStd

object Objenesis {
  val objenesis = new ObjenesisStd(true)

  def newInstance[T](forClass: Class[T]): T = {
    val i = objenesis getInstantiatorOf forClass
    i.newInstance()
  }

}
