package com.github.athieriot

import de.flapdoodle.embed._
import process.runtime.Network
import mongo._
import config._
import distribution._
import org.specs2.specification._
import org.specs2.mutable.SpecificationLike
import org.specs2.main.Arguments

trait EmbedConnection extends FragmentsBuilder {
  self: SpecificationLike =>
  isolated

  override def sequential: Arguments = args(isolated = false, sequential = true)

  override def isolated: Arguments = args(isolated = true, sequential = false)

  //Override this method to personalize testing port
  def embedConnectionPort: Int

  //Override this method to personalize MongoDB version
  def embedMongoDBVersion: Version.Main = { Version.Main.PRODUCTION }

  lazy val network = new Net(embedConnectionPort, Network.localhostIsIPv6)

  lazy val mongodConfig = new MongodConfigBuilder()
    .version(embedMongoDBVersion)
    .net(network)
    .build

  lazy val runtime = MongodStarter.getDefaultInstance

  lazy val mongodExecutable = runtime.prepare(mongodConfig)

  override def map(fs: => Fragments) = startMongo ^ fs ^ stopMongo

  def startMongo() = {
    Example("Start Mongo", { mongodExecutable.start; success })
  }

  def stopMongo() = {
    Example("Stop Mongo", { mongodExecutable.stop(); success })
  }
}
