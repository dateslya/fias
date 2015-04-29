package ru.fias.updates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fias.updates.scheduler.FixedRate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dmitry Teslya on 26.04.2015.
 */
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static final String INITIAL_DELAY = "02:00:00";
    private static final String PERIOD = "1";

    public static void main(String... args) {
        log.info("Run");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        final Runnable beeper = new Runnable() {

            public void run() {
                log.info("Beep");
            }
        };

        FixedRate fixedRate = FixedRate.builder().withInitialDelay(INITIAL_DELAY).withPeriod(PERIOD).build();
        log.info("initialDelay={}", fixedRate.getInitialDelay());
        log.info("h={}, m={}, s={}",
                fixedRate.getInitialDelay() / (60 * 60),
                (fixedRate.getInitialDelay() / 60) % 60,
                fixedRate.getInitialDelay() % 60);
        log.info("period={}", fixedRate.getPeriod());
        log.info("h={}, m={}, s={}",
                fixedRate.getPeriod() / (60 * 60),
                (fixedRate.getPeriod() / 60) % 60,
                fixedRate.getPeriod() % 60);
        scheduler.scheduleAtFixedRate(beeper, fixedRate.getInitialDelay(), fixedRate.getPeriod(), TimeUnit.SECONDS);
    }
}
