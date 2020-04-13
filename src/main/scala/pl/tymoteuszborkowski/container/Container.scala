package pl.tymoteuszborkowski.container

import java.util

import pl.tymoteuszborkowski.domain.Identifiable

import scala.collection.JavaConverters._
import java.util.Optional
import java.util.UUID

trait Container[T <: Identifiable] {
  def add(toAdd: T): Unit

  def getAll: util.ArrayList[T]
 
  def stream: util.stream.Stream[T] = getAll.stream()

  def update(delta: Float): Unit

  def getById(id: UUID): Optional[T] = stream.filter(thing => thing.isIdEqual(id)).findAny

  def getById(id: String): Optional[T] = getById(UUID.fromString(id))

  def removeById(id: UUID): Unit = {
    getAll.removeIf(_.isIdEqual(id))
  }

  def removeById(id: String): Unit = {
    removeById(UUID.fromString(id))
  }
}
