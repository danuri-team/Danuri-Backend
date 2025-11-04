package org.aing.danurirest.global.util

import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

object QueryDslUtil {
    fun getOrderSpecifiers(pageable: Pageable, qClass: EntityPathBase<*>): List<OrderSpecifier<*>> {
        return pageable.sort.map { order ->
            val path = PathBuilder(qClass.type, qClass.metadata)
            val property = path.get(order.property, Comparable::class.java)
            OrderSpecifier(if (order.isAscending) Order.ASC else Order.DESC, property)
        }.toList()
    }
}
