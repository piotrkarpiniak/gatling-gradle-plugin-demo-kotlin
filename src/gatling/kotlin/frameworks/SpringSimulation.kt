package frameworks

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status

class SpringSimulation : Simulation() {

    init {
        setUp(
            scenario("Spring").injectOpen(atOnceUsers(1))
                .protocols(httpProtocol.baseUrl("https://spring.karpiniak.pl"))
        )
    }
}
