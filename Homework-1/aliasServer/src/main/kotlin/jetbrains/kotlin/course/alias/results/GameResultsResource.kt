package jetbrains.kotlin.course.alias.results

import alias.JsTeam
import jetbrains.kotlin.course.alias.util.toGameResult
import org.springframework.web.bind.annotation.*

// We can not use a typealias here because the Spring framework can not parse it
class GameJsResult : ArrayList<JsTeam>()

@RestController
@RequestMapping("/api/results/")
class GameResultsResource(val service: GameResultsService) {
    @CrossOrigin
    @PostMapping("/save")
    fun saveGameResults(@RequestBody result: GameJsResult) = service.saveGameResults(result.toGameResult())

    @CrossOrigin
    @GetMapping("/all")
    fun getAllGameResults(): List<List<JsTeam>> {
        val allGames = GameResultsService.getAllGameResults()
        
        // Convert to JsTeam objects with explicit name property
        return allGames.map { game ->
            game.map { team ->
                JsTeam(
                    id = team.id,
                    points = team.points,
                    name = team.name
                )
            }
        }
    }
    
    @CrossOrigin
    @GetMapping("/detailed")
    fun getDetailedGameResults() = GameResultsService.getDetailedGameResults()
    
    @CrossOrigin
    @GetMapping("/leaderboard")
    fun getLeaderboard(): List<Map<String, Any>> {
        val allGames = GameResultsService.getAllGameResults()
        
        // Format each game result with complete team information
        return allGames.mapIndexed { index, game ->
            val gameNumber = index + 1
            val formattedTeams = game.map { team ->
                "${team.name}: ${team.points} points"
            }
            
            mapOf(
                "gameNumber" to gameNumber,
                "teams" to formattedTeams,
                "date" to "Game #${gameNumber}"
            )
        }
    }
    
    @CrossOrigin
    @GetMapping("/formatted")
    fun getFormattedResults(): List<String> {
        val allGames = GameResultsService.getAllGameResults()
        
        // Create a simple formatted string for each game
        return allGames.mapIndexed { index, game ->
            val gameNumber = index + 1
            val teamsInfo = game.joinToString(", ") { team -> 
                "${team.name}: ${team.points} points" 
            }
            
            "Game #${gameNumber}: $teamsInfo"
        }
    }
}
