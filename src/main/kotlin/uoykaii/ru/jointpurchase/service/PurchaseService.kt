package uoykaii.ru.jointpurchase.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import uoykaii.ru.jointpurchase.client.AuthenticationClient
import uoykaii.ru.jointpurchase.dto.purchase.*
import uoykaii.ru.jointpurchase.entity.Purchase
import uoykaii.ru.jointpurchase.exception.AuthenticationException
import uoykaii.ru.jointpurchase.exception.EntityNotFoundException
import uoykaii.ru.jointpurchase.repository.PurchaseRepository
import uoykaii.ru.jointpurchase.repository.UserRepository
import uoykaii.ru.jointpurchase.security.user
import uoykaii.ru.jointpurchase.util.ImageOwnerType
import uoykaii.ru.jointpurchase.util.OrderStatus
import uoykaii.ru.jointpurchase.util.PurchaseStatus
import uoykaii.ru.jointpurchase.util.purchaseCanBeStopped
import java.time.LocalDateTime
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val imageService: ImageService,
    private val itemService: ItemService,
    private val userRepository: UserRepository,
    private val authenticationClient: AuthenticationClient
) {

    @Transactional
    fun create(purchaseCreateRequest: PurchaseCreateRequest): PurchaseCreateResponse {
        val userByEmail = userRepository.findByEmail(user.email) ?: throw EntityNotFoundException("User not founded")

        val purchase: Purchase = Purchase().apply {
            id = UUID.randomUUID()
            name = purchaseCreateRequest.name
            description = purchaseCreateRequest.description
            moneyGoal = purchaseCreateRequest.moneyGoal
            organizationalFee = purchaseCreateRequest.organizationalFee
            stopDate = purchaseCreateRequest.stopDate
            deliveryMethod = purchaseCreateRequest.deliveryMethod
            paymentMethod = purchaseCreateRequest.paymentMethod
            status = PurchaseStatus.DRAFT
            createdDate = LocalDateTime.now()
            user = userByEmail
        }.also { purchaseRepository.save(it) }

        imageService.upload(purchaseCreateRequest.image, purchase.id!!, ImageOwnerType.PURCHASE)

        return PurchaseCreateResponse(purchase.id!!)
    }

    @Transactional
    fun publish(id: UUID) {
        val purchase = purchaseRepository.findById(id)
            .orElseThrow { throw EntityNotFoundException("Purchase with id: $id not founded") }

        if (purchase.user?.email != user.email) throw AuthenticationException("U can't publish purchase with id: $id")

        purchase.apply {
            status = PurchaseStatus.PUBLISHED
            publishedDate = LocalDateTime.now()
        }
    }

    fun getPreviewsByStatus(status: PurchaseStatus, token: String): PurchasePreviewsListResponse {
        val previews: MutableList<PurchasePreviewResponse> = mutableListOf()

        val purchases = purchaseRepository.findAllByStatus(status)
        if (token != "") {
            val userId = authenticationClient.getUserByToken(token).uuid
            val user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow { EntityNotFoundException("User with id: $userId not founded") }
            purchaseRepository.findAllByStatusAndUser(status, user) // todo
        }

        logger.info { "getting previews for $purchases" }

        purchases.forEach {
            previews.add(
                PurchasePreviewResponse(
                    id = it.id!!,
                    name = it.name,
                    moneyGoal = it.moneyGoal,
                    collectedMoney = it.collectedMoney,
                    organizationalFee = it.organizationalFee,
                    stopDate = it.stopDate,
                    deliveryMethod = it.deliveryMethod,
                    paymentMethod = it.paymentMethod,
                    status = it.status,
                    createdDate = it.createdDate,
                    publishedDate = it.publishedDate,
                    canBeStopped = purchaseCanBeStopped(it.stopDate, it.status),
                    imageId = imageService.getDownloadId(it.image!!)
                )
            )
        }
        return PurchasePreviewsListResponse(previews)
    }

    fun getById(id: UUID): PurchaseResponse {
        val purchase =
            purchaseRepository.findById(id).orElseThrow { throw IllegalArgumentException("нет такой закупки: $id") }
        print("возврат purchaseResponse для: $purchase")
        return purchase.let {
            PurchaseResponse(
                id = it.id!!,
                name = it.name,
                moneyGoal = it.moneyGoal,
                collectedMoney = it.collectedMoney,
                organizationalFee = it.organizationalFee,
                stopDate = it.stopDate,
                deliveryMethod = it.deliveryMethod,
                paymentMethod = it.paymentMethod,
                status = it.status,
                createdDate = it.createdDate,
                publishedDate = it.publishedDate,
                canBeStopped = purchaseCanBeStopped(it.stopDate, it.status),
                imageId = imageService.getDownloadId(it.image!!),
                itemsPreviews = itemService.getPreviewsByPurchase(it)
            )
        }
    }

    @Transactional
    fun stop(id: UUID) {
        val purchase = purchaseRepository.findById(id)
            .orElseThrow { throw IllegalArgumentException("ошибка стопа, закупка: $id не найден") }

        if (!purchaseCanBeStopped(purchase.stopDate, purchase.status)) {
            throw IllegalArgumentException("нельзя стопнуть закупку: $id")
        }
        purchase.status = PurchaseStatus.AWAITING_INVOICE
        purchase.items.forEach { item ->
            item.orders.forEach { order ->
                order.status = OrderStatus.AWAITING_INVOICE
            }
        }
    }

    @Transactional
    fun extend(id: UUID, request: PurchaseExtendRequest) {
        val purchase = purchaseRepository.findById(id)
            .orElseThrow { throw IllegalArgumentException("ошибка продления, закупка: $id не найден") }

        if (purchase.stopDate == null || purchase.stopDate!!.isBefore(request.stopDate)) {
            purchase.stopDate = request.stopDate
        } else {
            throw IllegalArgumentException("нельзя продлить закупку: $id")
        }
    }
}
