package transactionvertx

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import io.vertx.reactivex.mysqlclient.MySQLPool
import io.vertx.reactivex.sqlclient.SqlClient
import io.vertx.reactivex.sqlclient.SqlConnection
import io.vertx.reactivex.sqlclient.Transaction
import jakarta.inject.Singleton
import reactor.adapter.rxjava.RxJava2Adapter
import reactor.core.publisher.Mono

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime

@Singleton
class MainClass implements ApplicationEventListener<ServerStartupEvent> {

    private final MySQLPool pool

    MainClass(MySQLPool pool) {
        this.pool = pool
    }

    @Override
    void onApplicationEvent(ServerStartupEvent event) {
        pool.rxGetConnection().flatMap { c ->
            return c.rxBegin().flatMap { trx ->
                return c.query("INSERT INTO my_user(name, email) VALUES('John John 1', 'mayerjohn1@mail.com')").rxExecute()
                        .flatMap { return c.query("INSERT INTO my_user(name, email) VALUES('John John 2', 'mayerjohn2@mail.com')").rxExecute() }
                        .flatMap {
                            throw new Error("Error whatever")
                            return c.query("INSERT INTO my_user(name, email) VALUES('John John 3', 'mayerjohn3@mail.com')").rxExecute()
                        }
                        .flatMap { return c.query("INSERT INTO my_user(name, email) VALUES('John John 4', 'mayerjohn3@mail.com')").rxExecute() }
                        .flatMap { return c.query("SELECT * FROM new_user").rxExecute() }
                        .doOnError {
                            trx.rollback()
                        }
                        .doOnSuccess { trx.commit() }
                        .doFinally { c.close() }
            }
        }.subscribe({
            it.each { println(it.toString()) }
        }, { println(it.message) })
    }
}
