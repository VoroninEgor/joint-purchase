package uoykaii.ru.jointpurchase.controller

import org.springframework.web.bind.annotation.*
import uoykaii.ru.jointpurchase.dto.purchase.PurchaseCreateRequest
import uoykaii.ru.jointpurchase.dto.purchase.PurchaseCreateResponse
import uoykaii.ru.jointpurchase.dto.purchase.PurchasePreviewsListResponse
import uoykaii.ru.jointpurchase.service.PurchaseService
import java.util.UUID

@RestController
@CrossOrigin(origins = ["http://127.0.0.1:5500"])
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

    @PutMapping("/publish/{id}")
    fun publish(@PathVariable id:UUID) {
        println("Завершение создания закупки: $id ...")
        purchaseService.publish(id)
    }

    @GetMapping("/preview")
    fun getPreviews(): PurchasePreviewsListResponse {
        println("Возврат превью закупок...")
        return purchaseService.getPreviews()
    }
}
