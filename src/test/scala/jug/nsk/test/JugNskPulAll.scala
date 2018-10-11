package jug.nsk.test

import org.apache.ignite.Ignition
import org.apache.ignite.configuration.{CacheConfiguration, IgniteConfiguration}

import scala.collection.JavaConverters._

object JugNskPulAll extends App {

  val cacheConfig = new CacheConfiguration[Int, String]("jug_nsk")

  val igniteConfig = new IgniteConfiguration()
    .setCacheConfiguration(cacheConfig)

  val ignite = Ignition.start(igniteConfig)
  val cache = ignite.cache[Int, String]("jug_nsk")

  val data = (0 to 5).map(i => (i, s"value-$i")).toMap.asJava

  cache.putAll(data)
  cache.get(1)
}
