import scala.sys.process._

val scalacVersion = "2.13.5"
val flovverVersion = "0.1-SNAPSHOT"

name := "compiler"
organization := "org.flovver"
scalaVersion := scalacVersion

val ScalatraVersion = "2.7.+"

libraryDependencies ++= Seq(
  "org.scalatra"            %% "scalatra"           % ScalatraVersion,
  "org.scalatra"            %% "scalatra-specs2"    % ScalatraVersion    % Test,
  "ch.qos.logback"          %  "logback-classic"    % "1.2.3"            % Compile,
  "org.eclipse.jetty"       %  "jetty-webapp"       % "9.4.35.v20201120" % Compile,
  "javax.servlet"           %  "javax.servlet-api"  % "3.1.0"            % Provided,
  "org.json4s"              % "json4s-jackson_2.13" % "3.7.0-RC1"        % Compile,
)

enablePlugins(JettyPlugin)

lazy val buildIde = taskKey[Seq[File]]("build IDE and copy to resources")

buildIde := {
  val executeNpmBuild = false

  val buildCode = if (executeNpmBuild) "npm run build --prefix ..//ide".! else 0

  if (buildCode == 0) IO.copyDirectory(file("..//ide//public"), (Compile / resourceManaged).value / "public")

  def tree(f: File): Array[File] = {
    val files = IO.listFiles(f)

    files ++ files.filter(_.isDirectory).flatMap(tree)
  }

  tree((Compile / resourceManaged).value / "public").toSeq
}

Compile / resourceGenerators += buildIde.taskValue

lazy val app = (project in file("."))
  .settings(
    assembly / mainClass := Some("org.flovver.cmd.Launcher"),
    assembly / assemblyJarName := "flovver.jar",
    assembly / test := {},
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case _ => MergeStrategy.first
    }
  )
