package jetbrains.kotlin.course.alias.team

import jetbrains.kotlin.course.alias.util.Identifier
import jetbrains.kotlin.course.alias.util.IdentifierFactory
import org.springframework.stereotype.Service

@Service
class TeamService {
    companion object {
        val teamsStorage: MutableMap<Identifier, Team> = mutableMapOf()
        val identifierFactory: IdentifierFactory = IdentifierFactory()

        fun restoreTeamsStorage(storage: Map<Identifier, Team>) {
            teamsStorage.clear()
            teamsStorage.putAll(storage)
        }
    }

    fun generateTeamsForOneRound(teamsNumber: Int): List<Team> {
        val teams = List(teamsNumber) { 
            val id = identifierFactory.uniqueIdentifier()
            Team(id).also { teamsStorage[id] = it }
        }
        return teams
    }
}
