package pl.tymoteuszborkowski.domain
import java.util.UUID

trait Identifiable {

  def getId: UUID

  def isIdEqual(otherId: UUID): Boolean = getId == otherId

}
