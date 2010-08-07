import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val specs = (if (buildScalaVersion startsWith "2.7.")
    "org.scala-tools.testing" % "specs" % "1.6.2.2"
  else
    "org.scala-tools.testing" %% "specs" % "1.6.5") % "test"
  val jetty = "org.eclipse.jetty" % "jetty-webapp" % "7.0.2.v20100331"
  val servlet_api = "javax.servlet" % "servlet-api" % "2.3" % "provided"
}