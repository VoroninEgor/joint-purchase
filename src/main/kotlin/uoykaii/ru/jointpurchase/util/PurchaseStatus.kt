package uoykaii.ru.jointpurchase.util

enum class PurchaseStatus {
    DRAFT,            // Закупка сохранена в базу, но еще продолжается редактирование
    PUBLISHED,        // Закупка полностью создана и видна пользователям
    AWAITING_INVOICE, // Закупка остановлена, ждем счет для оплаты
    PAYMENT_REQUIRED, // Нужно оплатить данную закупку всем пользователям
    IN_DELIVERY,      // Продукты из закупки доставляются
    READY_FOR_PICKUP  // Продукты из закупки готовы к получению
}