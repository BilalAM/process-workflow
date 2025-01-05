package workflow.process.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Loggable {
    protected val logger: Logger by lazy { LoggerFactory.getLogger(this::class.java) }

}