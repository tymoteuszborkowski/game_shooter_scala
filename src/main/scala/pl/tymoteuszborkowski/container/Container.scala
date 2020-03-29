package pl.tymoteuszborkowski.container

import java.util

import scala.collection.JavaConverters._

trait Container[Thing] {
  def add(toAdd: Thing): Unit

  def getAll: util.ArrayList[Thing]

  def stream: util.stream.Stream[Thing] = getAll.stream()

  def update(delta: Float): Unit
}
