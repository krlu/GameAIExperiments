package experiments

import java.nio.file.Paths

import com.github.ocraft.s2client.bot.S2Coordinator
import com.github.ocraft.s2client.protocol.game.{Difficulty, LocalMap, Race}


object SC2Experiment {

  def main(args: Array[String]): Unit = {
    val bot = CustomSC2Agent()
    val currentDir = Paths.get(".")
    val s2Coordinator = S2Coordinator.setup()
      .loadSettings(args)
      .setParticipants(
        S2Coordinator.createParticipant(Race.TERRAN, bot),
        S2Coordinator.createComputer(Race.ZERG, Difficulty.VERY_EASY))
      .launchStarcraft().startGame(LocalMap.of(Paths.get(s"${currentDir.toAbsolutePath}/maps/Scenario1.SC2Map")))

    while (s2Coordinator.update()) {
    }
    s2Coordinator.quit()
  }
}
