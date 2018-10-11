package jug.nsk.test

import jug.nsk.ignite.JugNskTopologyChecker
import org.scalatest.{FlatSpec, Matchers}

class TopologyCheckerSpec extends FlatSpec
  with JugNskTopologyChecker
  with Matchers {

  "A Topology Checker" should "reboot ignite node" in {
    checkTopology(cntStartedNodes = 1, replicationFactor = 0, currentTopologyNodes = 1) shouldBe true
    checkTopology(cntStartedNodes = 1, replicationFactor = 1, currentTopologyNodes = 1) shouldBe true
    checkTopology(cntStartedNodes = 1, replicationFactor = 2, currentTopologyNodes = 1) shouldBe true

    checkTopology(cntStartedNodes = 2, replicationFactor = 0, currentTopologyNodes = 1) shouldBe false
    checkTopology(cntStartedNodes = 2, replicationFactor = 1, currentTopologyNodes = 1) shouldBe false
    checkTopology(cntStartedNodes = 2, replicationFactor = 2, currentTopologyNodes = 1) shouldBe false

    checkTopology(cntStartedNodes = 3, replicationFactor = 0, currentTopologyNodes = 1) shouldBe false
    checkTopology(cntStartedNodes = 3, replicationFactor = 1, currentTopologyNodes = 1) shouldBe false
    checkTopology(cntStartedNodes = 3, replicationFactor = 1, currentTopologyNodes = 2) shouldBe true
    checkTopology(cntStartedNodes = 3, replicationFactor = 1, currentTopologyNodes = 3) shouldBe true
    checkTopology(cntStartedNodes = 3, replicationFactor = 2, currentTopologyNodes = 3) shouldBe true
    checkTopology(cntStartedNodes = 3, replicationFactor = 3, currentTopologyNodes = 3) shouldBe true
    checkTopology(cntStartedNodes = 3, replicationFactor = 0, currentTopologyNodes = 2) shouldBe false

    checkTopology(cntStartedNodes = 10, replicationFactor = 2, currentTopologyNodes = 8) shouldBe true
    checkTopology(cntStartedNodes = 10, replicationFactor = 2, currentTopologyNodes = 7) shouldBe false
    checkTopology(cntStartedNodes = 10, replicationFactor = 5, currentTopologyNodes = 5) shouldBe false
  }
}
