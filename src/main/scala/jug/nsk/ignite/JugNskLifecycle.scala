package jug.nsk.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.events.DiscoveryEvent
import org.apache.ignite.events.EventType._
import org.apache.ignite.lang.IgnitePredicate
import org.apache.ignite.lifecycle.LifecycleEventType._
import org.apache.ignite.lifecycle.{LifecycleBean, LifecycleEventType}
import org.apache.ignite.resources.IgniteInstanceResource

import scala.collection.JavaConversions._

class JugNskLifecycle extends JugNskTopologyChecker with LifecycleBean {
  @IgniteInstanceResource private val ignite : Ignite = null

  lazy val log = ignite.log()

  private val localListener: IgnitePredicate[DiscoveryEvent] = new IgnitePredicate[DiscoveryEvent] {
    override def apply(event: DiscoveryEvent): Boolean = {
      val currentTopologyNodes = event.topologyNodes().filterNot(_.isClient).size

      if ( !checkTopology(cntStartedNodes, minReplicationFactor, currentTopologyNodes) ) {
        // to do something
      }
      true
    }
  }

  override def onLifecycleEvent(evt: LifecycleEventType): Unit = evt match {
    case AFTER_NODE_START =>
      ignite.events().localListen(localListener, EVT_NODE_LEFT, EVT_NODE_FAILED)
      log.info(">>> JugNskIgnite was started")
    case _ =>
  }
}


