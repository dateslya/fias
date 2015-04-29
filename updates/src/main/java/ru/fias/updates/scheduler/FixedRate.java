package ru.fias.updates.scheduler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        private String initialDelay;
        private String period;

        private FixedRateBuilder() { }

        public FixedRateBuilder withInitialDelay(String initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }

        public FixedRateBuilder withPeriod(String period) {
            this.period = period;
            return this;
        }

        public FixedRate build() {
            return new FixedRate(parseInitialDelay(initialDelay), parsePeriod(period));
        }

        public long parseInitialDelay(String initialDelay) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime initial = LocalDateTime.now().with(LocalTime.parse(initialDelay));
            initial = now.isAfter(initial) ? initial.plusDays(1) : initial;
            return ChronoUnit.SECONDS.between(now, initial);
        }

        public long parsePeriod(String period) {
            return Duration.ofDays(Long.valueOf(period)).getSeconds();
        }
    }
}
