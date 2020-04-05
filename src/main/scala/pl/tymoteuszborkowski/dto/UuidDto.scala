package pl.tymoteuszborkowski.dto

import com.fasterxml.jackson.annotation.JsonProperty

class UuidDto(@JsonProperty("uuid") uuid: String) extends Dto {

  def getUuid: String = uuid

}
