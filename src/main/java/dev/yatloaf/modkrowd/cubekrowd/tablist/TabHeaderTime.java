package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public record TabHeaderTime(ZonedDateTime time) {
    public static final TabHeaderTime FAILURE = new TabHeaderTime(Instant.EPOCH.atZone(ZoneOffset.UTC));

    // "01:23 on Feb 01, 2003"
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm 'on' MMM dd, uuuu", Locale.ROOT)
            .withZone(ZoneOffset.UTC);

    public static TabHeaderTime read(StyledStringReader source) {
        String timeString = source.readAll().toUnstyledString();

        try {
            ZonedDateTime time = ZonedDateTime.parse(timeString, FORMATTER);
            return new TabHeaderTime(time);
        } catch (DateTimeParseException e) {
            return FAILURE; // Debug this maybe?
        }
    }

    public StyledString appearance() {
        return StyledString.fromString(this.time.format(FORMATTER));
    }
}
