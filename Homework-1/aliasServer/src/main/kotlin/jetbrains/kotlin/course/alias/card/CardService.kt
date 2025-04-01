package jetbrains.kotlin.course.alias.card

import jetbrains.kotlin.course.alias.util.IdentifierFactory
import jetbrains.kotlin.course.alias.util.words
import org.springframework.stereotype.Service

@Service
class CardService {
    private val cards: List<Card> = generateCards()

    companion object {
        const val WORDS_IN_CARD = 4
        val identifierFactory: IdentifierFactory = IdentifierFactory()
        val cardsAmount = words.size / WORDS_IN_CARD
    }

    private fun List<String>.toWords(): List<Word> = map { Word(it) }

    private fun generateCards(): List<Card> {
        return words.shuffled()
            .chunked(WORDS_IN_CARD)
            .take(cardsAmount)
            .mapIndexed { index, chunk ->
                Card(identifierFactory.uniqueIdentifier(), chunk.toWords())
            }
    }

    fun getCardByIndex(index: Int): Card {
        return cards.getOrElse(index) {
            throw IllegalArgumentException("Card with index $index not found")
        }
    }
}
