package jetbrains.kotlin.course.alias.results

import jetbrains.kotlin.course.alias.team.TeamService
import org.springframework.stereotype.Service

@Service
class GameResultsService {
    companion object {
        private val gameHistory: MutableList<GameResult> = mutableListOf()

        fun getAllGameResults(): List<GameResult> = gameHistory.asReversed()
        
        /**
         * Get detailed game results with team names and points.
         * This returns a list of game results, where each game result is a list of teams with their points.
         * Each team includes the team name and points scored in that game.
         */
        fun getDetailedGameResults(): List<List<Map<String, Any>>> {
            return gameHistory.asReversed().map { gameResult ->
                gameResult.map { team ->
                    mapOf(
                        "name" to team.name,
                        "points" to team.points,
                        "id" to team.id
                    )
                }
            }
        }

        fun restoreGameHistory(history: List<GameResult>) {
            gameHistory.clear()
            gameHistory.addAll(history)
        }
    }

    fun saveGameResults(result: GameResult) {
        require(result.isNotEmpty()) { "Game result cannot be empty" }
        require(result.all { it.id in TeamService.teamsStorage }) { "All teams must be registered in TeamService" }
        gameHistory.add(result)
    }
}
