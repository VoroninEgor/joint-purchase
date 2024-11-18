package uoykaii.ru.jointpurchase.repository

import org.springframework.data.jpa.repository.JpaRepository
import uoykaii.ru.jointpurchase.entity.Item
import java.util.*


interface ItemRepository : JpaRepository<Item, UUID> {
}