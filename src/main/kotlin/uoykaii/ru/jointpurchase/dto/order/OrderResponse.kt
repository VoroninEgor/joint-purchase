package uoykaii.ru.jointpurchase.dto.order

import uoykaii.ru.jointpurchase.dto.item.ItemPreviewResponse
import uoykaii.ru.jointpurchase.util.OrderStatus
import java.util.UUID

data class OrderResponse (
    val id: UUID,
    val count: Int,
    val status: OrderStatus,
    val item: ItemPreviewResponse
)