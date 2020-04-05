package pl.tymoteuszborkowski.dto

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}

@JsonCreator
class ControlsDto(@JsonProperty("forward") forward: Boolean,
                  @JsonProperty("left") left: Boolean,
                  @JsonProperty("right") right: Boolean,
                  @JsonProperty("shoot") shoot: Boolean) extends Dto {

  def getForward: Boolean = forward

  def getLeft: Boolean = left

  def getRight: Boolean = right

  def getShoot: Boolean = shoot


}
