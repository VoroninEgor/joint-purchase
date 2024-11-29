package uoykaii.ru.jointpurchase.controller

import org.springframework.web.bind.annotation.*
import uoykaii.ru.jointpurchase.dto.purchase.*
import uoykaii.ru.jointpurchase.service.PurchaseService
import uoykaii.ru.jointpurchase.util.PurchaseStatus
import java.util.*

@RestController
@RequestMapping("/purchase")
class PurchaseController(val purchaseService: PurchaseService) {

    @PostMapping(consumes = ["multipart/form-data"])
    fun create(
        @ModelAttribute purchaseCreateRequest: PurchaseCreateRequest
    ): PurchaseCreateResponse {
        println("Создание закупки...")
        println(purchaseCreateRequest)
        return purchaseService.create(purchaseCreateRequest)
    }

    @GetMapping("/preview")
    fun getPreviews(
         @RequestParam(required = false) status: PurchaseStatus?
    ): PurchasePreviewsListResponse {
        val allPreviews = purchaseService.getPreviewsByStatus(status)
        println("Возврат превью закупок:$allPreviews ")
        return allPreviews
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): PurchaseResponse {
        val purchase: PurchaseResponse = purchaseService.getById(id)
        println("Возврат инфы о закупке: $purchase")
        return purchase
    }

    @PutMapping("/publish/{id}")
    fun publish(@PathVariable id: UUID) {
        println("Завершение создания закупки: $id ...")
        purchaseService.publish(id)
    }

    @PutMapping("/stop/{id}")
    fun stop(@PathVariable id: UUID) {
        println("Стоп закупки: $id ...")
        purchaseService.stop(id)
    }

    @PutMapping("/extend/{id}")
    fun extend(@PathVariable id: UUID, @RequestBody request: PurchaseExtendRequest) {
        println("Продление закупки $id")
        purchaseService.extend(id, request)
    }
}
