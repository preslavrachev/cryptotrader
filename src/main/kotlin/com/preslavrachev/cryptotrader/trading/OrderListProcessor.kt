package com.preslavrachev.cryptotrader.trading

import com.preslavrachev.cryptotrader.mvc.model.Order
import com.preslavrachev.cryptotrader.mvc.model.OrderStateEnum
import com.preslavrachev.cryptotrader.session.AppSession
import com.preslavrachev.cryptotrader.trading.api.TradingApi
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.LocalDateTime
import java.util.concurrent.ForkJoinPool
import javax.annotation.PostConstruct
import javax.inject.Inject

@Component
class OrderListProcessor {

    companion object {
        val PARALLEL_TASK_COUNT = 4
    }

    @Inject
    lateinit var session: AppSession

    @Inject
    lateinit var tradingApi: TradingApi

    lateinit var orderProcessingPool: ForkJoinPool

    @PostConstruct
    fun postConstruct(): Unit {
        orderProcessingPool = ForkJoinPool(PARALLEL_TASK_COUNT)
    }

    @Scheduled(fixedRate = 100)
    fun scheduleOrderProcessing(): Unit {
        synchronized(session.orders) {
            processOrders(session.orders).subscribe()
        }
    }

    internal fun processOrders(orders: List<Order>): Flux<Order> {
        return orders.asSequence()
                .filter { OrderStateEnum.NEW == it.state }
                .filter { it.executionDateTime <= LocalDateTime.now() }
                .sortedByDescending { it.executionDateTime }
                .onEach { it.apply { state = OrderStateEnum.PROCESSING } }
                // TODO: replace the direct API service dependancy with an interface, or skip it altogether
                // TODO: by using @Configurable
                .map { OrderProcessorTask(it, tradingApi) }
                .map {
                    Mono.fromCallable(it)
                            .subscribeOn(Schedulers.fromExecutor(orderProcessingPool))
                            .doOnError { (it as OrderProcessingException).order.state = OrderStateEnum.FAILED }
                            .doOnSuccess { it.state = OrderStateEnum.DONE }
                            .flux()
                }
                .fold(Flux.empty()) { acc, flux -> acc.mergeWith(flux) }
    }
}
