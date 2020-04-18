package pl.tymoteuszborkowski.dto

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}

@JsonCreator
class IndexedDto[UnderlyingDto]( @JsonProperty("dto") dto: UnderlyingDto,
                                 @JsonProperty("index") index: Long)  extends Dto{

  def getDto: UnderlyingDto = dto

  def getIndex: Long = index


}
