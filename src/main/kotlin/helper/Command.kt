package helper

//used to describe in what mode and how many levels to pump
class Command(
    val mode: Mode,
    val levels: Int,
) {
    enum class Mode(val number: Int) {
        TEST(0), SIMPLE(1)
    }
}