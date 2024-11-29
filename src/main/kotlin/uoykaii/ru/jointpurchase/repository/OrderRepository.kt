package uoykaii.ru.jointpurchase.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import uoykaii.ru.jointpurchase.entity.Order
import uoykaii.ru.jointpurchase.util.OrderStatus
import java.util.UUID

interface OrderRepository: JpaRepository<Order, UUID> {
    @Query("SELECT o from Order o where o.status = :status")
    fun findByStatus(@Param("status") status: OrderStatus): MutableList<Order>
}