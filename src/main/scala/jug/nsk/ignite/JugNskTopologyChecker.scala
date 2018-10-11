package jug.nsk.ignite

trait JugNskTopologyChecker
{
  val cntStartedNodes = sys.env.getOrElse("WAIT_TOPOLOGY_MORE_N_NODE", "1").toInt
  val minReplicationFactor = sys.env.getOrElse("REQUIRED_N_REPLICAS", "0").toInt

  def checkTopology( cntStartedNodes: Int,
                     replicationFactor: Int,
                     currentTopologyNodes: Int
                   ): Boolean = {

    currentTopologyNodes >= (cntStartedNodes - replicationFactor) &&
        currentTopologyNodes > cntStartedNodes.toDouble / 2

  }
}


