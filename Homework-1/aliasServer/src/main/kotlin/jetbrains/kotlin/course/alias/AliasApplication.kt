package jetbrains.kotlin.course.alias

import jetbrains.kotlin.course.alias.card.CardService
import jetbrains.kotlin.course.alias.results.GameResult
import jetbrains.kotlin.course.alias.results.GameResultsService
import jetbrains.kotlin.course.alias.team.Team
import jetbrains.kotlin.course.alias.team.TeamService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SpringBootApplication
class AliasApplication {
    @EventListener(ContextClosedEvent::class)
    fun onApplicationShutdown() {
        // Save game state when application shuts down
        saveGameState()
    }
}

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    // Load previous game state if it exists
    loadGameState()
    
    // Register shutdown hook to save game state when JVM exits
    Runtime.getRuntime().addShutdownHook(Thread {
        saveGameState()
    })
    
    runApplication<AliasApplication>(*args)
}

private fun saveGameState() {
    val gameState = GameState(
        gameHistory = GameResultsService.getAllGameResults(),
        teamsStorage = TeamService.teamsStorage,
        teamIdentifierCounter = TeamService.identifierFactory.getCurrentCounter(),
        cardIdentifierCounter = CardService.identifierFactory.getCurrentCounter()
    )
    
    val jsonString = Json.encodeToString(gameState)
    File("game_state.json").writeText(jsonString)
    println("Game state saved to game_state.json")
}

private fun loadGameState() {
    val gameStateFile = File("game_state.json")
    if (!gameStateFile.exists()) return
    
    try {
        val jsonString = gameStateFile.readText()
        val gameState = Json.decodeFromString<GameState>(jsonString)
        
        // Restore game state
        GameResultsService.restoreGameHistory(gameState.gameHistory)
        TeamService.restoreTeamsStorage(gameState.teamsStorage)
        TeamService.identifierFactory.setCounter(gameState.teamIdentifierCounter)
        CardService.identifierFactory.setCounter(gameState.cardIdentifierCounter)
        println("Game state loaded from game_state.json")
    } catch (e: Exception) {
        println("Failed to load game state: ${e.message}")
    }
}

// Data class to represent the complete game state
@Serializable
private data class GameState(
    val gameHistory: List<GameResult>,
    val teamsStorage: Map<Int, Team>,
    val teamIdentifierCounter: Int,
    val cardIdentifierCounter: Int
)

