package jug.nsk.test

import org.apache.ignite.cache.CachePeekMode.PRIMARY
import org.apache.ignite.lang.IgniteCallable
import org.apache.ignite.{Ignite, Ignition}
import org.scalatest.Matchers
import org.testcontainers.containers.Network

import scala.collection.JavaConversions._

class LifeCycleSpec
  extends IgniteServerContainerService(1, replicas = 1, network = Some(Network.newNetwork()))
  with Matchers
{
  lazy val igniteClient: Ignite = Ignition.start("src/test/resources/ignite-client.test.xml")
  val cacheName = "jug_nsk"
  lazy val cache = igniteClient.cache[Int, Int](cacheName)

  "JugNsk Ignite" should "work" in {
    containers.forall(_.isRunning) shouldBe true
  }

  it should "put/get" in {
    cache.put(1, 1)
    cache.get(1) shouldBe 1
  }

  it should "affinityCall" in {

    val data = (0 to 5).map(i => (i, i)).toMap
    cache.putAll(data)

    val compute = igniteClient.compute
    val key = 1

    implicit def toFunction[A](f: () => A): IgniteCallable[A] = new IgniteCallable[A] {
      override def call(): A = f.apply
    }

    val sumByKey = compute.affinityCall(cacheName, key, () => {
      // to do on the server node
      Ignition.localIgnite().cache[Int, Int]("jug_nsk").localEntries(PRIMARY).map(_.getValue).sum
    })

    sumByKey shouldBe (0 to 5).sum
  }

}
