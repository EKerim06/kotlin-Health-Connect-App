import java.time.Month

enum class MonthRename(val displayName: String) {
    OCAK("2024 Ocak "),
    SUBAT("Şubat 2024"),
    MART("Mart 2024"),
    NISAN("Nisan 2024"),
    MAYIS("Mayıs 2024"),
    HAZIRAN("Haziran 2024"),
    TEMMUZ("Temmuz 2024"),
    AGUSTOS("Ağustos 2024"),
    EYLUL("Eylül 2024"),
    EKIM("Ekim 2024"),
    KASIM("Kasım 2024"),
    ARALIK("Aralık 2024");

    companion object {
        fun fromMonth(month: Month): MonthRename {
            return when (month) {
                Month.JANUARY -> OCAK
                Month.FEBRUARY -> SUBAT
                Month.MARCH -> MART
                Month.APRIL -> NISAN
                Month.MAY -> MAYIS
                Month.JUNE -> HAZIRAN
                Month.JULY -> TEMMUZ
                Month.AUGUST -> AGUSTOS
                Month.SEPTEMBER -> EYLUL
                Month.OCTOBER -> EKIM
                Month.NOVEMBER -> KASIM
                Month.DECEMBER -> ARALIK
            }
        }
    }
}
