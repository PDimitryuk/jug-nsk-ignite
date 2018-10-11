package jug.nsk.test

import java.util.Date

import org.apache.ignite.Ignition
import org.apache.ignite.cache.query.SqlFieldsQuery
import org.apache.ignite.cache.query.annotations.QuerySqlField
import org.apache.ignite.configuration.{CacheConfiguration, IgniteConfiguration}

import scala.collection.JavaConversions._

object JugNskSQL extends App {

  val cacheConfig = new CacheConfiguration[Int, JugNsk]("jug_nsk")
  val igniteConfig = new IgniteConfiguration().setCacheConfiguration(cacheConfig)

  val ignite = Ignition.start(igniteConfig)
  val cacheName = "jug_nsk"
  val cache = ignite.cache[Int, JugNsk](cacheName)

  val data = (0 to 5).map(i => (i, JugNsk(s"name-$i", new Date))).toMap
  cache.putAll(data)


  case class JugNsk(
                     @QuerySqlField(index = true)
                     name: String,
                     @QuerySqlField
                     date: Date
                   )

  cache.query(
    new SqlFieldsQuery(s"select * from JugNsk where date > '2018-10-11 00:00:00.0'")
  )
    .getAll
    .foreach(println)
}
