package play.api.libs.logback

import scala.concurrent.Future

import akka.actor.{ActorSystem, CoordinatedShutdown}
import javax.inject.{Inject, Provider}
import org.slf4j.LoggerFactory

object LogbackCoordinatedShutdownProvider {
  private val logger =
    LoggerFactory.getLogger(classOf[LogbackCoordinatedShutdownProvider])
}

class LogbackCoordinatedShutdownProvider @Inject()(actorSystem: ActorSystem,
                                                   loggerConfigurator: LogbackLoggerConfigurator)
    extends Provider[CoordinatedShutdown] {
  import LogbackCoordinatedShutdownProvider.logger

  lazy val get: CoordinatedShutdown = {
    val cs = CoordinatedShutdown(actorSystem)
    cs.addTask(CoordinatedShutdown.PhaseBeforeActorSystemTerminate,
               "logback-delayed-shutdown") { () =>
      logger.info("Shutting down logback via coordinated shutdown")
      loggerConfigurator.shutdown()
      Future.successful(akka.Done)
    }
    cs
  }
}
