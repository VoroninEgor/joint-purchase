package uoykaii.ru.jointpurchase.repository

import org.springframework.data.jpa.repository.JpaRepository
import uoykaii.ru.jointpurchase.entity.Purchase
import java.util.UUID

interface PurchaseRepository: JpaRepository<Purchase, UUID> {
}