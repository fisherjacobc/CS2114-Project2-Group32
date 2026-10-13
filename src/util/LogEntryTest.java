package util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LogEntryTest {
    LogEntry logEntry;

    @BeforeEach
    public void setUp() {
        logEntry = new LogEntry("00:00:00.00", "Billy", "Passenger had a prohibited item", false);
    }

    @Test
    public void testLogEntry() {
        assertNotNull(logEntry);
    }
}