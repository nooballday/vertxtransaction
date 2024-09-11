package transactionvertx

import io.micronaut.runtime.Micronaut
import groovy.transform.CompileStatic
import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.runtime.server.event.ServerStartupEvent

@CompileStatic
class Application {

    static void main(String[] args) {
        Micronaut.run(Application, args)
    }
}
