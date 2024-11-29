package uoykaii.ru.jointpurchase.util

enum class OrderStatus{
    NEW, // можно менять количество товаров и удалять заказы
    AWAITING_INVOICE, // нельзя удалить и менять кол-во ждем ответа организатора
    PAYMENT_REQUIRED, // нужно оплатить в указанные сроки или бан
    IN_DELIVERY, // заказ оплачен и доставляется организатором
    READY_FOR_PICKUP, // заказ в пункте выдачи
}
