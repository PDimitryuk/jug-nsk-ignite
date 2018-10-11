package jug.nsk.ignite

import java.util

import org.apache.ignite.Ignite
import org.apache.ignite.cluster.ClusterNode
import org.apache.ignite.configuration.TopologyValidator
import org.apache.ignite.resources.{CacheNameResource, IgniteInstanceResource}

class JugNskTopologyValidator extends TopologyValidator with JugNskTopologyChecker {

  @IgniteInstanceResource private val ignite : Ignite = null
  @CacheNameResource private val cacheName: String = null

  override def validate(nodes: util.Collection[ClusterNode]): Boolean = {
    val currentTopologyNodes = ignite.cluster().forServers().nodes().size

    checkTopology(cntStartedNodes, minReplicationFactor, currentTopologyNodes)
  }
}
