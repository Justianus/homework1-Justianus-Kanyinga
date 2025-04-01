package jetbrains.kotlin.course.alias.card

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Word(val word: String) 