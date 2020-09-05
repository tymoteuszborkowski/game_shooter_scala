package pl.tymoteuszborkowski.vfc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{Assertions, GivenWhenThen}

class ZoneGeneratorTest extends AnyFlatSpec with Matchers with GivenWhenThen with Assertions {

  "Zone generator " should " generate properly 4 zones with correct ranges" in {
    When("Generating zones")
    val zones = ZoneGenerator.generateFourZones()

    Then("Zones have concrete range")
    assert(zones(0).range.startPosition == (0, 0))
    assert(zones(0).range.endPosition == (200, 150))

    assert(zones(1).range.startPosition == (200, 150))
    assert(zones(1).range.endPosition == (400, 300))

    assert(zones(2).range.startPosition == (400, 300))
    assert(zones(2).range.endPosition == (600, 450))

    assert(zones(3).range.startPosition == (600, 450))
    assert(zones(3).range.endPosition == (800, 600))
  }


}
