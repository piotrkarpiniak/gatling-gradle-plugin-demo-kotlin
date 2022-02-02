package frameworks

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.http.HttpDsl
import io.gatling.javaapi.http.HttpDsl.http


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
    """{    "article": {        "title": "How to train your dragon",        "description": "Ever wonder how?",        "body": "Very carefully.",        "tagList": [            "training",            "dragons"        ]    }}"""


fun scenario(name: String) = CoreDsl.scenario(name)
    .exec(
        http("login")
            .post("/users/login")
            .body(CoreDsl.StringBody(user))
            .check(CoreDsl.jsonPath("$.user.token").saveAs("access_token"))
            .check(HttpDsl.status().shouldBe(200))
    )
    .repeat(10).on(
        CoreDsl.exec(
            http("create article")
                .post("/articles")
                .header("content-type", "application/json")
                .header("Authorization", "Token #{access_token}")
                .body(CoreDsl.StringBody(article))
                .check(HttpDsl.status().shouldBe(201))
            //.check(bodyString().saveAs("BODY"))
        )
        /*.exec { session ->
            println("Response body: ${session.get<String>("BODY")}")
            session
        }*/
    )
