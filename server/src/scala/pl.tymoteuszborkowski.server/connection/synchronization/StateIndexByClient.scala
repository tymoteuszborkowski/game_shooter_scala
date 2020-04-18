package pl.tymoteuszborkowski.server.connection.synchronization

import java.util.UUID

import scala.collection.mutable

class StateIndexByClient(val indices: mutable.Map[UUID, Long]) {

  def this(){
    this(new mutable.HashMap[UUID, Long]())
  }

  def lastIndexFor(id: UUID): Long = {
    val sequence = indices.get(id)
    sequence.getOrElse(-1L)
  }

  def setIndexFor(id: UUID, value: Long): Unit = {
    indices.put(id, value)
  }

}
