sonatypeProfileName := "com.adendamedia"

publishMavenStyle := true

licenses := Seq("GNU Lesser General Public License Version 3" -> url("https://www.gnu.org/licenses/lgpl-3.0.en.html"))

homepage := Some(url("https://github.com/adenda/salad"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/adenda/salad"),
    "scm:git@github.com:adenda/salad.git"
  )
)

developers := List(
  Developer(
    id    = "fdoumet",
    name  = "Francis Doumet",
    email = "francis.doumet@adendamedia.com",
    url   = url("http://www.adendamedia.com/")
  ),
  Developer(
    id    = "kliewkliew",
    name  = "Kevin Liew",
    email = "kliewkliew@users.noreply.github.com",
    url   = url("https://github.com/kliewkliew")
  ),
  Developer(
    id    = "sjking",
    name  = "Steve King",
    email = "steve@steveking.site",
    url   = url("https://steveking.site")
  )
)

