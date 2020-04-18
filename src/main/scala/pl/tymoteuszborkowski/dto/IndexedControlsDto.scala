package pl.tymoteuszborkowski.dto

import com.fasterxml.jackson.annotation.JsonProperty

class IndexedControlsDto(@JsonProperty("dto") dto: ControlsDto,
                         @JsonProperty("index") index: Long)
  extends IndexedDto[ControlsDto](dto, index) {


}
