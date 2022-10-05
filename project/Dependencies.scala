import sbt.Keys.libraryDependencies
import sbt._

object Dependencies {

  implicit class ProjectOps(project: Project) {
    def withIntegrationTests: Project = {
      project
        .configs(IntegrationTest)
        .settings(Defaults.itSettings)
        .settings(libraryDependencies ++= Specs.itDeps)
    }
  }

  object Common {
    private val slf4jVersion = "1.7.36"
    private val logbackVersion = "1.2.11"
    private val pureConfigVersion = "0.17.1"
    private val catsLogVersion = "2.3.0"
    private val monocleVersion = "3.0.0-M6"
    private val sttpVersion = "3.7.1"
    private val nettyVersion = "4.1.67.Final"

    val slf4jApi     = "org.slf4j"                    % "slf4j-api"              % slf4jVersion
    val logback      = ("ch.qos.logback"              % "logback-classic"        % logbackVersion).exclude("org.slf4j", "slf4j")
    val scalaLogging = ("com.typesafe.scala-logging" %% "scala-logging"          % "3.9.5").exclude("org.slf4j", "slf4j")
    val scalaXml     = "org.scala-lang.modules"      %% "scala-xml"              % "2.1.0"
    val config       = "com.github.pureconfig"       %% "pureconfig"             % pureConfigVersion
    val configCats   = "com.github.pureconfig"       %% "pureconfig-cats-effect" % pureConfigVersion

    val log4cats     = "org.typelevel" %% "log4cats-core"  % catsLogVersion
    val log4catsSl4j = "org.typelevel" %% "log4cats-slf4j" % catsLogVersion

    val monocleCore  = "com.github.julien-truffaut" %% "monocle-core"  % monocleVersion
    val monocleMacro = "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion

    val shapeless   = "com.chuusai"   %% "shapeless"           % "2.3.9"
    val catsTagless = "org.typelevel" %% "cats-tagless-macros" % "0.14.0"

    val apacheLang       = "org.apache.commons"        % "commons-lang3" % "3.12.0" % Test
    val apacheHttpClient = "org.apache.httpcomponents" % "httpclient"    % "4.5.13" % Test
    val apacheHttpCore   = "org.apache.httpcomponents" % "httpcore"      % "4.4.15" % Test

    val sttp = "com.softwaremill.sttp.client3" %% "core" % sttpVersion excludeAll ExclusionRule("io.netty")
    val sttpAsyncBackend =
      "com.softwaremill.sttp.client3" %% "async-http-client-backend-fs2" % sttpVersion excludeAll ExclusionRule(
        "io.netty"
      )
    val sttpCirce = "com.softwaremill.sttp.client3" %% "circe" % sttpVersion excludeAll ExclusionRule("io.netty")
    val netty = "io.netty" % "netty-all" % nettyVersion

    val deps: Seq[ModuleID] = Seq(
      slf4jApi,
      logback,
      scalaLogging,
      config,
      configCats,
      catsTagless,
      monocleCore,
      monocleMacro,
      sttp,
      sttpAsyncBackend,
      sttpCirce,
      netty,
      apacheLang,
      apacheHttpCore,
      apacheHttpClient
    )

  }

  object Akka {
    val akkaVersion = "2.6.19"
    val akkaHttpVersion = "10.2.9"

    val typed: Seq[ModuleID] = Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
    ).map(_.exclude("org.slf4j", "slf4j"))

    val akkaTestkit     = "com.typesafe.akka" %% "akka-testkit"        % akkaVersion     % Test
    val streamTestkit   = "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion     % Test
    val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit"   % akkaHttpVersion % Test
  }

  object Cats {
    private val catsVersion = "2.8.0"
    private val effectVersion = "3.3.13"
    private val org = "org.typelevel"

    val core = org %% "cats-core" % catsVersion
    val effect = org %% "cats-effect" % effectVersion

    val deps: Seq[ModuleID] = Seq(core, effect)
  }

  object Circe {
    val enumeratumCirceVersion = "1.7.0"
    val circeVersion           = "0.14.2"

    val deps: Seq[ModuleID] =
      Seq(
        "io.circe"          %% "circe-core"           % circeVersion,
        "io.circe"          %% "circe-generic"        % circeVersion,
        "io.circe"          %% "circe-parser"         % circeVersion,
        "io.circe"          %% "circe-generic-extras" % circeVersion,
        "com.beachape"      %% "enumeratum-circe"     % enumeratumCirceVersion,
        "de.heikoseeberger" %% "akka-http-circe"      % "1.39.2",
      )
  }

  object Http4s {
    val http4sVersion = "0.23.12"
    val server        = "org.http4s"       %% "http4s-blaze-server"       % http4sVersion
    val client        = "org.http4s"       %% "http4s-blaze-client"       % http4sVersion
    val circe         = "org.http4s"       %% "http4s-circe"              % http4sVersion
    val xml           = "org.http4s"       %% "http4s-scala-xml"          % http4sVersion
    val dsl           = "org.http4s"       %% "http4s-dsl"                % http4sVersion

    val deps: Seq[ModuleID] = Seq(server, client, circe, xml, dsl)
  }


  object Specs {
    val scalaTest                   = "org.scalatest"        %% "scalatest"                                % "3.2.12"
    val scalaCheckPlus              = "org.scalatestplus"    %% "scalacheck-1-15"                          % "3.2.11.0"
    val scalaCheckPlusMockito       = "org.scalatestplus"    %% "mockito-3-4"                              % "3.2.10.0"
    val catsEffectTesting           = "com.codecommit"       %% "cats-effect-testing-scalatest"            % "0.5.4"
    val catsEffectTestingScalaCheck = "com.codecommit"       %% "cats-effect-testing-scalatest-scalacheck" % "0.5.4"
    val scalamock                   = "org.scalamock"        %% "scalamock"                                % "5.2.0"

    val deps: Seq[ModuleID] = Seq(
      scalaTest,
      scalaCheckPlus,
      scalaCheckPlusMockito,
      catsEffectTesting,
      catsEffectTestingScalaCheck,
      scalamock
    ) map (_ % Test)

    val itDeps: Seq[ModuleID] = Seq(
      scalaTest,
      scalaCheckPlus,
      scalaCheckPlusMockito,
      catsEffectTesting,
      catsEffectTestingScalaCheck,
      scalamock
    ) map (_ % IntegrationTest)
  }

  object SQL {
    val h2Version           = "2.1.214"
    val doobieVersion       = "0.13.4"

    val doobieHikari = "org.tpolecat"  %% "doobie-hikari"   % doobieVersion
    val h2Database   = "com.h2database" % "h2"              % h2Version     % Test
    val doobieH2     = "org.tpolecat"  %% "doobie-h2"       % doobieVersion % Test

    val deps: Seq[ModuleID] = Seq(h2Database, doobieH2, doobieHikari)
  }

}