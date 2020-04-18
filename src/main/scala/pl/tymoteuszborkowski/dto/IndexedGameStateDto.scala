package pl.tymoteuszborkowski.dto

import com.fasterxml.jackson.annotation.JsonProperty

class IndexedGameStateDto(@JsonProperty("dto") dto: GameStateDto,
                          @JsonProperty("index") index: Long)
  extends IndexedDto[GameStateDto](dto, index) {


}
