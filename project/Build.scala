import sbt._
import sbt.Keys._

object ProjectBuild extends Build {

  lazy val buildVersion =  "0.7.1-SNAPSHOT"

  lazy val root = Project(id = "specs2-embedmongo", base = file("."), settings = Project.defaultSettings).settings(
    organization := "com.github.athieriot",
    description := "Specs2 helper to configure a EmbedMongo based instance",
    version := buildVersion,
    scalaVersion := "2.11.7",

    resolvers += "ba-group repository" at "http://repository.ba-group.fi/content/groups/public/",
    resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
    resolvers += "Novus Snapshots" at "http://repo.novus.com/snapshots/",
    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    resolvers += "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",

    libraryDependencies <++= scalaVersion(sv => Seq(
      "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "1.48.2",
      "org.specs2"  %% "specs2" % "2.3.12",
      "org.mongodb" %% "casbah-core" % "2.7.2" % "provided",
      "com.novus" %% "salat-core" % "1.9.8" % "test",
      "junit" % "junit" % "4.11" % "test"
      )),

    parallelExecution in Test := false,

    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    pomExtra := <url>http://github.com/athieriot/specs2-embedmongo</url>
      <licenses>
        <license>
          <name>Apache 2.0</name>
          <url>http://www.opensource.org/licenses/Apache-2.0</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:athieriot/specs2-embedmongo.git</url>
        <connection>scm:git:git@github.com:athieriot/specs2-embedmongo.git</connection>
      </scm>
      <developers>
        <developer>
          <id>athieriot</id>
          <name>Aurélien Thieriot</name>
          <url>http://athieriot.github.com/resume</url>
        </developer>
      </developers>,
    credentials += Credentials(Path.userHome / ".m2" / "nexus-credentials"),
    publishTo <<= version { (v: String) =>
      val nexus = "http://repository.ba-group.fi/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots/")
      else
        Some("snapshots" at nexus + "content/repositories/releases/")
    }
  )
}
