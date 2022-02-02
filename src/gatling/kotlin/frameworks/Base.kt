package frameworks

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.http.HttpDsl
import io.gatling.javaapi.http.HttpDsl.http
import org.apache.commons.lang3.RandomStringUtils

val articleFeeder = generateSequence {
    val tag = RandomStringUtils.randomAlphanumeric(20)
    val description = RandomStringUtils.randomAlphanumeric(100)
    mapOf("title" to "title $tag", "tag" to tag, "description" to description)
}.iterator()


val httpProtocol = http
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .contentTypeHeader("application/json")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")


private val user = """
{
    "user": {
        "email": "karpiniakpiotr@gmail.com",
        "password": "django-test"
    }
}"""

private val article =
    """{"article":{"title":"#{title}","description":"#{description}","body":"#{description}","tagList":["#{tag}"]}}""".trimMargin()


fun scenario(name: String) = CoreDsl.scenario(name)
    .feed(articleFeeder)
    .exec(
        http("login")
            .post("/users/login")
            .body(StringBody(user))
            .check(CoreDsl.jsonPath("$.user.token").saveAs("access_token"))
            .check(HttpDsl.status().shouldBe(200))

    )
    .repeat(100).on(
        CoreDsl.exec(
            http("create article")
                .post("/articles")
                .header("content-type", "application/json")
                .header("Authorization", "Token #{access_token}")
                .body(StringBody(article))
                .check(HttpDsl.status().shouldBe(201))
        )
    )
    .repeat(100).on(
        CoreDsl.exec(
            http("articles by tag")
                .get("/articles?tag=dragons")
                .header("Authorization", "Token #{access_token}")
                .check(HttpDsl.status().shouldBe(200))

        )
    )
