package play.api.libs.logback

import play.api.{Configuration, Environment}
import play.api.inject._

class LogbackModule extends SimpleModule(
  bind[LogbackCoordinatedShutdownProvider].toProvider[LogbackCoordinatedShutdownProvider]
)
