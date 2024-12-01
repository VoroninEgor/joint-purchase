package uoykaii.ru.jointpurchase.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import uoykaii.ru.jointpurchase.dto.OrderCreateRequest
import uoykaii.ru.jointpurchase.dto.OrderCreateResponse
import uoykaii.ru.jointpurchase.dto.item.ItemPreviewResponse
import uoykaii.ru.jointpurchase.dto.order.OrderCountChangeRequest
import uoykaii.ru.jointpurchase.dto.order.OrderListResponse
import uoykaii.ru.jointpurchase.dto.order.OrderResponse
import uoykaii.ru.jointpurchase.entity.Order
import uoykaii.ru.jointpurchase.repository.ItemRepository
import uoykaii.ru.jointpurchase.repository.OrderRepository
import uoykaii.ru.jointpurchase.util.OrderStatus
import uoykaii.ru.jointpurchase.util.canEditOrder
import java.time.LocalDateTime
import java.util.*


@Service
class OrderService(
    val orderRepository: OrderRepository,
    val itemRepository: ItemRepository,
    val imageService: ImageService
) {

    @Transactional
    fun create(request: OrderCreateRequest): OrderCreateResponse {
        val itemById = itemRepository.findById(request.itemId)
            .orElseThrow { throw IllegalArgumentException("Нет товара ${request.itemId}") }

        val existingOrder =
            itemById.orders.firstOrNull { true } //todo сделать проверку на наличие заказа такого товара у юзера

        val order = existingOrder?.apply {
            count = (count ?: 0) + request.count
        }
            ?: Order().apply {
                id = UUID.randomUUID()
                status = OrderStatus.NEW
                count = request.count
                item = itemById
                createdDate = LocalDateTime.now()
            }

        orderRepository.save(order)

        itemById.purchase!!.collectedMoney += itemById.price * request.count

        return OrderCreateResponse(order.id!!)
    }

    fun getAllByStatus(status: OrderStatus? = null): OrderListResponse {
        val orders = if (status == null) orderRepository.findAll() else orderRepository.findByStatus(status)
        val orderResponses = mutableListOf<OrderResponse>()
        orders.forEach {
            val item = it.item ?: throw NullPointerException()
            orderResponses.add(
                OrderResponse(
                    id = it.id!!,
                    count = it.count!!,
                    status = it.status!!,
                    item = ItemPreviewResponse(
                        id = item.id!!,
                        name = item.name,
                        description = item.description,
                        price = item.price,
                        type = item.type,
                        createdDate = item.createdDate!!,
                        imageIds = item.images.map { image -> imageService.getDownloadId(image) }.toList()
                    )
                )
            )
        }
        return OrderListResponse(orderResponses)
    }

    @Transactional
    fun remove(id: UUID) {
        val order = orderRepository.findById(id).orElseThrow { throw IllegalArgumentException("нет товара $id") }
        if (!canEditOrder(order.status)) {
            throw IllegalArgumentException("Заказ не доступен для удаления")
        }
        order.item!!.purchase!!.collectedMoney -= order.count!! * order.item!!.price
        orderRepository.delete(order)
    }

    @Transactional
    fun changeCount(id: UUID, request: OrderCountChangeRequest) {
        val order = orderRepository.findById(id).orElseThrow { throw IllegalArgumentException("нет товара $id") }
        if (!canEditOrder(order.status)) {
            throw IllegalArgumentException("Заказ не доступен для изменения")
        }

        val countChange = if (request.isAdding) 1 else -1
        val updatedCount = (order.count ?: 0) + countChange

        if (updatedCount < 1) {
            throw IllegalArgumentException("Количество товара в заказе не может быть меньше 1")
        }

        order.item!!.purchase!!.collectedMoney += countChange * order.item!!.price
    }
}