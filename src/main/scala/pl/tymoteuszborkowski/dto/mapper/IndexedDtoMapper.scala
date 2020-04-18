package pl.tymoteuszborkowski.dto.mapper

import pl.tymoteuszborkowski.dto.{Dto, IndexedDto}


object IndexedDtoMapper {

  def wrapWithIndex[UnderlyingDto <: Dto](dto: UnderlyingDto, index: Long) = new IndexedDto[UnderlyingDto](dto, index)

}
