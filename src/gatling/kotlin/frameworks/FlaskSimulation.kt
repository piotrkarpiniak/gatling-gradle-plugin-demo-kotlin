package frameworks

import io.gatling.javaapi.core.CoreDsl.atOnceUsers
import io.gatling.javaapi.core.Simulation

class FlaskSimulation : Simulation() {

    init {
        setUp(
            scenario("Flask").injectOpen(atOnceUsers(10))
                .protocols(httpProtocol.baseUrl("https://flask.karpiniak.pl/api"))
        )
    }
}
