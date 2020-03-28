package pl.tymoteuszborkowski.network.common

object Packets {

  class GetClientId(var id: Int = 0)

  class AddPlayer(var x: Int = 0,
                  var y: Int = 0,
                  var id: Int = 0)


}
