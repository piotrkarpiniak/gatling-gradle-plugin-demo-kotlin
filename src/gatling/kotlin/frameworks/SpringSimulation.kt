package frameworks

import io.gatling.javaapi.core.CoreDsl.atOnceUsers
import io.gatling.javaapi.core.Simulation

class SpringSimulation : Simulation() {

    init {
        setUp(
            scenario("Spring").injectOpen(atOnceUsers(10))
                .protocols(httpProtocol.baseUrl("https://spring.karpiniak.pl"))
        )
    }
}
