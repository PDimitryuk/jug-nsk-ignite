package jug.nsk.test

import java.util.function.Predicate

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec}
import org.slf4j.LoggerFactory
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.{BindMode, GenericContainer, Network}

abstract class IgniteServerContainerService(nodes: Int = 1, replicas: Int = 1, network: Option[Network] = None)
  extends FlatSpec
    with BeforeAndAfterAll
    with BeforeAndAfterEach
{
  private val log = LoggerFactory.getLogger(this.getClass.getName)

  implicit def toJavaPredicate[A](f: A => Boolean) = new Predicate[A] {
    override def test(a: A): Boolean = f(a)
  }

  private val image = "apacheignite/ignite:2.6.0" //todo: to config

  val net = network.getOrElse(Network.newNetwork())

  lazy val containers = (0 until nodes)
  .map { idx =>
    val container = new GenericContainer(image)
    container.setNetworkMode("host")
    container.withEnv("CONFIG_URI", "/opt/ignite/apache-ignite-fabric/config/ignite-server.test.xml")
    container.withEnv("JVM_OPTS", "-DIGNITE_QUIET=false -Xmx512M -XX:+UseG1GC -XX:+DisableExplicitGC -DIGNITE_QUIET=false -Dlogback.configurationFile=/opt/ignite/apache-ignite-fabric/config/logback.xml")
    container.withEnv("WAIT_TOPOLOGY_MORE_N_NODE", nodes.toString)
    container.withEnv("REQUIRED_N_REPLICAS", replicas.toString)
    container.withFileSystemBind("src/test/resources/ignite-server.test.xml", "/opt/ignite/apache-ignite-fabric/config/ignite-server.test.xml", BindMode.READ_ONLY)
    container.withFileSystemBind("src/test/resources/logback.xml", "/opt/ignite/apache-ignite-fabric/config/logback.xml", BindMode.READ_ONLY)
    container.withFileSystemBind("target/scala-2.11/jug-nsk-ignite-assembly-2.6.3.jar", "/opt/ignite/apache-ignite-fabric/libs/jug-nsk-ignite-assembly.jar", BindMode.READ_ONLY)
    container.withLogConsumer(new Slf4jLogConsumer(log))
    container.waitingFor(Wait.forLogMessage(".*JugNskIgnite was started.*\n", 1))
    container
  }

  override protected def beforeAll() {
    super.beforeAll()

    containers.par.foreach(_.start())
  }

  override def afterAll() {
    containers.par.foreach(_.stop())

    super.afterAll()
  }
}
