import io.circe.Json
import io.circe.syntax._

object GithubActionsGenerator {
  object Step {
    val Checkout: Json = Json.obj(
      "name" := "Checkout",
      "uses" := "actions/checkout@v4",
      "with" := Json.obj(
        "fetch-depth" := 0
      )
    )

    def setupJava(javaVersion: String): Json = Json.obj(
      "name" := "JDK",
      "uses" := "actions/setup-java@v4",
      "with" := Json.obj(
        "java-version" := javaVersion,
        "distribution" := "temurin",
        "cache" := "sbt"
      )
    )
  }

  object Job {
    def apply(
        name: String,
        javaVersion: String,
        steps: List[Json],
        needs: List[String] = Nil,
        condition: Option[String] = None
    ): Json = Json.obj(
      "name" := name,
      "runs-on" := "ubuntu-latest",
      "needs" := needs,
      "if" := condition,
      "steps" := List(
        Step.Checkout,
        Step.setupJava(javaVersion)
      ) ++ steps
    )

    def lint(javaVersion: String): Json = Job(
      name = "Fatal warnings and code formatting",
      javaVersion,
      steps = List(
        Json.obj("run" := "sbt blowoutCheck"),
        Json.obj("run" := "sbt scalafmtCheckAll"),
        Json.obj("run" := "sbt compile")
      )
    )

    def test(javaVersion: String): Json = Job(
      name = "Unit tests",
      javaVersion,
      steps = List(Json.obj("run" := "sbt +test"))
    )

    def documentation(javaVersion: String, uploadArtifact: Boolean): Json = Job(
      name = "Documentation",
      javaVersion,
      steps = List(
        Json.obj("run" := "sbt documentation/paradox")
      ) ++ {
        if (uploadArtifact)
          Json.obj(
            "name" := "Upload",
            "uses" := "actions/upload-artifact@v3",
            "with" := Json.obj(
              "name" := "documentation",
              "path" := "./modules/documentation/.jvm/target/paradox/site/main/",
              "if-no-files-found" := "error",
              "retention-days" := 3
            )
          ) :: Nil
        else Nil
      }
    )

    def deployArtifacts(javaVersion: String): Json = Job(
      name = "Deploy artifacts",
      javaVersion,
      needs = List("test", "lint", "documentation"),
      steps = List(
        Json.obj(
          "run" := "sbt ci-release",
          "env" := Json.obj(
            "PGP_PASSPHRASE" := "${{secrets.PGP_PASSPHRASE}}",
            "PGP_SECRET" := "${{secrets.PGP_SECRET}}",
            "SONATYPE_PASSWORD" := "${{secrets.SONATYPE_PASSWORD}}",
            "SONATYPE_USERNAME" := "${{secrets.SONATYPE_USERNAME}}"
          )
        )
      )
    )

    def deployDocumentation(javaVersion: String): Json = Job(
      name = "Deploy documentation",
      javaVersion,
      needs = List("test", "lint", "documentation"),
      steps = List(
        Json.obj(
          "name" := "Download artifact",
          "uses" := "actions/download-artifact@v3",
          "with" := Json.obj(
            "name" := "documentation",
            "path" := "./documentation"
          )
        ),
        Json.obj(
          "name" := "Deploy",
          "uses" := "JamesIves/github-pages-deploy-action@v4",
          "with" := Json.obj(
            "branch" := "gh-pages",
            "folder" := "documentation"
          )
        )
      )
    )
  }

  def main(javaVersion: String): Json = Json.obj(
    "name" := "CI",
    "on" := Json.obj(
      "push" := Json.obj(
        "branches" := List("main"),
        "tags" := List("*.*.*")
      )
    ),
    "env" := Json.obj(
      "SBT_TPOLECAT_CI" := "true"
    ),
    "jobs" := Json.obj(
      "lint" := Job.lint(javaVersion),
      "test" := Job.test(javaVersion),
      "documentation" := Job.documentation(javaVersion, uploadArtifact = false),
      "deploy-artifacts" := Job.deployArtifacts(javaVersion)
    )
  )

  def tag(javaVersion: String): Json = Json.obj(
    "name" := "CD",
    "on" := Json.obj(
      "push" := Json.obj(
        "branches" := List("main"),
        "tags" := List("*.*.*")
      )
    ),
    "env" := Json.obj(
      "SBT_TPOLECAT_RELEASE" := "true"
    ),
    "jobs" := Json.obj(
      "lint" := Job.lint(javaVersion),
      "test" := Job.test(javaVersion),
      "documentation" := Job.documentation(javaVersion, uploadArtifact = true),
      "deploy-artifacts" := Job.deployArtifacts(javaVersion),
      "deploy-documentation" := Job.deployDocumentation(javaVersion)
    )
  )

  def pullRequest(javaVersion: String): Json = Json.obj(
    "name" := "CI",
    "on" := Json.obj(
      "pull_request" := Json.obj(
        "branches" := List("main")
      )
    ),
    "env" := Json.obj(
      "SBT_TPOLECAT_CI" := "true"
    ),
    "jobs" := Json.obj(
      "lint" := Job.lint(javaVersion),
      "test" := Job.test(javaVersion),
      "documentation" := Job.documentation(javaVersion, uploadArtifact = false)
    )
  )
}
