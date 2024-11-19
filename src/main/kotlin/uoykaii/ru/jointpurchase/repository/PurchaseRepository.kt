package uoykaii.ru.jointpurchase.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import uoykaii.ru.jointpurchase.entity.Purchase
import uoykaii.ru.jointpurchase.util.PurchaseStatus
import java.util.*

interface PurchaseRepository : JpaRepository<Purchase, UUID> {

    @Query("SELECT p from Purchase p where p.status = :status")
    fun findAllByStatus(@Param("status") status: PurchaseStatus): MutableList<Purchase>
}
