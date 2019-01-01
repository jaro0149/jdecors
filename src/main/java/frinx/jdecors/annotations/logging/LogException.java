package frinx.jdecors.annotations.logging;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.logging.Level;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogException {

    @SuppressWarnings("unused")
    enum LevelDispatcher {
        OFF(Level.OFF), SEVERE(Level.SEVERE), WARNING(Level.WARNING), INFO(Level.INFO), CONFIG(Level.CONFIG),
        FINE(Level.FINE), FINER(Level.FINER), FINEST(Level.FINEST);

        private Level level;

        LevelDispatcher(Level level) {
            this.level = level;
        }

        public Level getLevel() {
            return level;
        }
    }

    LevelDispatcher loggingLevel() default LevelDispatcher.INFO;
}