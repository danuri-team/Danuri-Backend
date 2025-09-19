package org.aing.danurirest.global.config

import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

class AsyncExceptionHandler : AsyncUncaughtExceptionHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun handleUncaughtException(
        e: Throwable,
        method: Method,
        vararg params: Any?,
    ) {
        if (log.isDebugEnabled) {
            log.error("비동기 작업 중 예외 발생. 메서드: {}, 파라미터: {}", method.name, params, e)
        }
    }
}
