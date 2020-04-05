package pl.tymoteuszborkowski.dto

import java.io.IOException

import com.fasterxml.jackson.databind.ObjectMapper

trait Dto {
  val objectMapper = new ObjectMapper

  def toJsonString: String =
    try objectMapper.writeValueAsString(this)
    catch {
      case e: IOException =>
        throw new RuntimeException("Error while converting Dto to JSON", e)
    }
}

object Dto {

  val objectMapper = new ObjectMapper

  def fromJsonString[DtoType <: Dto](json: String, dtoTypeClass: Class[DtoType]): DtoType =
    try objectMapper.readValue(json, dtoTypeClass)
    catch {
      case e: IOException =>
        throw new RuntimeException("Error while creating Dto from JSON", e)
    }

}