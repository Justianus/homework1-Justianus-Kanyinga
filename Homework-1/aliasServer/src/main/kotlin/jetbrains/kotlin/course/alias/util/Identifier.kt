package jetbrains.kotlin.course.alias.util

typealias Identifier = Int

class IdentifierFactory {
    private var counter: Int = 0

    fun uniqueIdentifier(): Identifier {
        return ++counter
    }

    fun getCurrentCounter(): Int = counter

    fun setCounter(value: Int) {
        counter = value
    }
} 