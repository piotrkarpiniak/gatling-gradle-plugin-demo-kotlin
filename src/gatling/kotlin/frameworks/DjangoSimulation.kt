package frameworks

import io.gatling.javaapi.core.CoreDsl.atOnceUsers
import io.gatling.javaapi.core.Simulation

class DjangoSimulation : Simulation() {

    init {
        setUp(
            scenario("Django").injectOpen(atOnceUsers(10))
                .protocols(httpProtocol.baseUrl("https://django.karpiniak.pl/api"))
        )
    }
}
