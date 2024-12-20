package uoykaii.ru.jointpurchase.util

enum class PurchaseStatus {
    DRAFT,            // Закупка сохранена в базу, но еще продолжается редактирование
    PUBLISHED,        // Закупка полностью создана и видна пользователям
    AWAITING_INVOICE, // Закупка остановлена, ждем счет для оплаты (организатор связывается с закупщиком и узнает наличие и кол-во всех товаров по списку)
    PENDING_ORDERS, // Обработка заказов выставление чеков на оплату, доставка и тд..
    CLOSED // срок оплаты по всем товарам вышел все товары доставлены в пвз
}