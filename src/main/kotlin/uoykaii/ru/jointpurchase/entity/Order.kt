package uoykaii.ru.jointpurchase.entity

import jakarta.persistence.*
import uoykaii.ru.jointpurchase.util.OrderStatus
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "orders")
open class Order {
    @Id
    open var id: UUID? = null

    @Enumerated(EnumType.STRING)
    open var status: OrderStatus? = null
    open var createdDate: LocalDateTime? = null

    open var count: Int? = null

    @ManyToOne
    @JoinColumn(name = "item_id")
    open var item: Item? = null

    @ManyToOne
    @JoinColumn(name = "user_id")
    open var user: User? = null

}
