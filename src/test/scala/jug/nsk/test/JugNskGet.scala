package jug.nsk.test

import org.apache.ignite.Ignition
import org.apache.ignite.configuration.{CacheConfiguration, IgniteConfiguration}

object JugNskGet extends App {

  val cacheConfig = new CacheConfiguration[Int, String]("jug_nsk")

  val igniteConfig = new IgniteConfiguration()
    .setCacheConfiguration(cacheConfig)

  val ignite = Ignition.start(igniteConfig)
  val cache = ignite.cache[Int, String]("jug_nsk")

  cache.put(1, "value")
  cache.get(1)
}
