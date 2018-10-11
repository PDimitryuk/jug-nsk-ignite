package jug.nsk.test

import org.apache.ignite.Ignition
import org.apache.ignite.cache.CachePeekMode.PRIMARY
import org.apache.ignite.configuration.{CacheConfiguration, IgniteConfiguration}
import org.apache.ignite.lang.IgniteCallable

import scala.collection.JavaConversions._

object JugNskAffinityRun extends App {

  val cacheConfig = new CacheConfiguration[Int, Int]("jug_nsk")

  val igniteConfig = new IgniteConfiguration()
    .setCacheConfiguration(cacheConfig)

  val ignite = Ignition.start(igniteConfig)
  val cacheName = "jug_nsk"
  val cache = ignite.cache[Int, Int](cacheName)

  val data = (0 to 5).map(i => (i, i)).toMap
  cache.putAll(data)

  val compute = ignite.compute
  val key = 1

  implicit def toFunction[A](f: () => A): IgniteCallable[A] = new IgniteCallable[A] {
    override def call(): A = f.apply
  }

  val sumByKey = compute.affinityCall(cacheName, key, () => {
    // to do on the server node
    cache.localEntries(PRIMARY).map(_.getValue).sum
  })

  println(s"sumByKey: $sumByKey")

  ignite.close()
}
