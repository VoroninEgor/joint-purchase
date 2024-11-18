package uoykaii.ru.jointpurchase.util


fun getFileSuffix(fileName: String): String? {
    return fileName.substringAfterLast('.', "").takeIf { it.isNotEmpty() }
}