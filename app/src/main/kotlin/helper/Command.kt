package helper

//used to describe in what mode and how many levels to pump
data class Command(
    val mode: Mode,
    val levels: Int,
) {
    enum class Mode(val number: Int) {
        //todo @Kagetana name it
        TEST(0), SIMPLE(1), RAREST_FIRST(2), FURTHEST(3)
    }
}