package pl.tymoteuszborkowski.vfc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{Assertions, GivenWhenThen}

class ZoneGeneratorTest extends AnyFlatSpec with Matchers with GivenWhenThen with Assertions {

  "Zone generator " should " generate properly 4 zones with correct ranges" in {
    When("Generating zones")
    val zones = ZoneGenerator.generateFourZones()

    Then("Zones have concrete range")
    assert(zones(0).range.startVelocity == (0, 0))
    assert(zones(0).range.endVelocity == (200, 150))

    assert(zones(1).range.startVelocity == (200, 150))
    assert(zones(1).range.endVelocity == (400, 300))

    assert(zones(2).range.startVelocity == (400, 300))
    assert(zones(2).range.endVelocity == (600, 450))

    assert(zones(3).range.startVelocity == (600, 450))
    assert(zones(3).range.endVelocity == (800, 600))
  }

  "Zone generator " should " generate properly 4 zones with correct consistency" in {
    When("Generating zones")
    val zones = ZoneGenerator.generateZones(4)

    Then("Zones have concrete range")
    assert(zones(0).maxConsistencyScale.delay == 0)
    assert(zones(0).maxConsistencyScale.lostUpdates == 0)
    assert(zones(0).maxConsistencyScale.difference == 0)

    assert(zones(1).maxConsistencyScale.delay == 0.1F)
    assert(zones(1).maxConsistencyScale.lostUpdates == 1)
    assert(zones(1).maxConsistencyScale.difference == 0.1F)

    assert(zones(2).maxConsistencyScale.delay == 0.4F)
    assert(zones(2).maxConsistencyScale.lostUpdates == 4)
    assert(zones(2).maxConsistencyScale.difference == 0.4F)

    assert(zones(3).maxConsistencyScale.delay == 0.9F)
    assert(zones(3).maxConsistencyScale.lostUpdates == 9)
    assert(zones(3).maxConsistencyScale.difference == 0.9F)
  }

  "Zone generator " should " generate properly 4 zones with valid ids" in {
    When("Generating zones")
    val zones = ZoneGenerator.generateZones(4)

    Then("Zones have concrete range")
    assert(zones(0).id == 1)
    assert(zones(1).id == 2)
    assert(zones(2).id == 3)
    assert(zones(3).id == 4)
  }

}
