package uoykaii.ru.jointpurchase.repository

import org.springframework.data.jpa.repository.JpaRepository
import uoykaii.ru.jointpurchase.entity.Image
import java.util.*


interface ImageRepository : JpaRepository<Image, UUID> {
}