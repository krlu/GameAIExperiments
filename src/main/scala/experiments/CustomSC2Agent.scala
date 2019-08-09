package experiments

import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.gateway.UnitInPool
import com.github.ocraft.s2client.protocol.data.{Abilities, Units}
import com.github.ocraft.s2client.protocol.unit.Alliance

import scala.jdk.CollectionConverters._

class CustomSC2Agent extends S2Agent {
  override def onGameStart(): Unit = {
    println("game starting...")
  }

  override def onStep(): Unit = {
    Thread.sleep(1000 / 30)
    val scvs = observation().getUnits(Alliance.SELF, UnitInPool.isUnit(Units.TERRAN_SCV))
    scvs.asScala.toList.foreach{ scv: UnitInPool =>
      if(isIdle(scv)){
        actions().unitCommand(scv.unit(), Abilities.HARVEST_GATHER, findNearestMineralPatch(scv).unit(), false)
      }
    }
  }

  private def findNearestMineralPatch(scv: UnitInPool): UnitInPool = {
    val currPosition = scv.unit().getPosition
    val units = observation.getUnits(Alliance.NEUTRAL).asScala.toList.filter{ u: UnitInPool =>
      sameUnitType(u, Units.NEUTRAL_MINERAL_FIELD) || sameUnitType(u, Units.NEUTRAL_MINERAL_FIELD750)
    }.map{ mineral =>
      (mineral, mineral.unit().getPosition.distance(currPosition))
    }.sortWith(_._2 < _._2)
    units.head._1
  }

  private def isIdle(unit: UnitInPool): Boolean = unit.unit().getOrders.isEmpty
  private def sameUnitType(u1: UnitInPool, u2: Units): Boolean = u1.unit().getType.getUnitTypeId == u2.getUnitTypeId
}

object CustomSC2Agent{
  def apply(): CustomSC2Agent = new CustomSC2Agent() 
}