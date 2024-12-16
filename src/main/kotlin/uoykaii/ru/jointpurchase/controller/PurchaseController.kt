package uoykaii.ru.jointpurchase.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import uoykaii.ru.jointpurchase.dto.purchase.*
import uoykaii.ru.jointpurchase.reflection.OrganizerApi
import uoykaii.ru.jointpurchase.security.user
import uoykaii.ru.jointpurchase.service.PurchaseService
import uoykaii.ru.jointpurchase.util.PurchaseStatus
import java.util.*
import kotlin.math.log

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/purchase")
class PurchaseController(val purchaseService: PurchaseService) {

    @PostMapping(consumes = ["multipart/form-data"])
    @OrganizerApi
    fun create(
        @Valid @ModelAttribute purchaseCreateRequest: PurchaseCreateRequest
    ): PurchaseCreateResponse {
        logger.info { "creating purchase for user with email: ${user.email}" }
        return purchaseService.create(purchaseCreateRequest)
    }


    @GetMapping("/preview")
    fun getPreviews(
        @RequestParam(required = false) status: PurchaseStatus?,
        @RequestParam(required = false) token: String?
    ): PurchasePreviewsListResponse {
        val allPreviews = purchaseService.getPreviewsByStatus(status ?: PurchaseStatus.PUBLISHED, token ?: "")
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
    @OrganizerApi
    fun publish(@PathVariable id: UUID) {
        logger.info { "Publishing purchase with id: $id" }
        purchaseService.publish(id)
    }

    @PutMapping("/stop/{id}")
    @OrganizerApi
    fun stop(@PathVariable id: UUID) {
        println("Стоп закупки: $id ...")
        purchaseService.stop(id)
    }

    @PutMapping("/extend/{id}")
    @OrganizerApi
    fun extend(@PathVariable id: UUID, @RequestBody request: PurchaseExtendRequest) {
        println("Продление закупки $id")
        purchaseService.extend(id, request)
    }
}
