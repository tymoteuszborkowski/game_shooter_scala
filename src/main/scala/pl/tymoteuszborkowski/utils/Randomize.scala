package pl.tymoteuszborkowski.utils

import scala.util.Random

object Randomize {

  def fromList[T](things: List[T]): T = Random.shuffle(things).head
}
