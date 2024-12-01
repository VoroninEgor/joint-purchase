package uoykaii.ru.jointpurchase.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import uoykaii.ru.jointpurchase.dto.OrderCreateRequest
import uoykaii.ru.jointpurchase.dto.OrderCreateResponse
import uoykaii.ru.jointpurchase.dto.order.OrderCountChangeRequest
import uoykaii.ru.jointpurchase.dto.order.OrderListResponse
import uoykaii.ru.jointpurchase.service.OrderService
import uoykaii.ru.jointpurchase.util.OrderStatus
import java.util.*

@RestController
@RequestMapping("/order")
class OrderController(
    val orderService: OrderService
) {
    @PostMapping
    fun create(@Valid @RequestBody request: OrderCreateRequest): OrderCreateResponse {
        return orderService.create(request)
    }

    @GetMapping
    fun getAll(@RequestParam(required = false) status: OrderStatus?): OrderListResponse {
        return orderService.getAllByStatus(status)
    }

    @DeleteMapping("/{id}")
    fun remove(@PathVariable id: UUID) {
        orderService.remove(id)
    }

    @PutMapping("/count/{id}")
    fun changeCount(@PathVariable id: UUID, @RequestBody request: OrderCountChangeRequest) {
        orderService.changeCount(id, request)
    }
}