package ru.fias.updates.scheduler;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/**
 * Created by Dmitry Teslya on 27.04.2015.
 */
public class FixedRate {

    private final long initialDelay;
    private final long period;

    private FixedRate(long initialDelay, long period) {
        this.initialDelay = initialDelay;
        this.period = period;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public long getPeriod() {
        return period;
    }

    public static FixedRateBuilder builder() {
        return new FixedRateBuilder();
    }

    public static class FixedRateBuilder {

        private long initialDelay;
        private long period;

        private FixedRateBuilder() { }

        public FixedRateBuilder parseInitialDelay(String initialDelay) {
            long offset = ChronoUnit.SECONDS.between(LocalTime.now(), LocalTime.parse(initialDelay));
            this.initialDelay = offset < 0 ? 24 * 60 * 60 + offset : offset;
            return this;
        }

        public FixedRateBuilder parsePeriod(String period) {
            this.period = LocalDateTime.parse(period).getLong(ChronoField.INSTANT_SECONDS);
            return this;
        }

        public FixedRate build() {
            return new FixedRate(initialDelay, period);
        }
    }
}
