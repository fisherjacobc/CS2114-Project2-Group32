package util;

/**
 * <h3>LogEntry</h3>
 * 
 * Record class for logging entires for when a passenger is detained
 * 
 * @author fisherjc
 * @version 13 Oct 2026
 * 
 * @param timestamp             The timestamp during the simulation that his
 *                              happened
 * @param passengerName         The passenger in question
 * @param reason                The reason the passenger was detained
 * @param wasWrongfullyDetained If the passenger ended up being wrongfully
 *                              detained
 */
public record LogEntry(String timestamp, String passengerName, String reason, boolean wasWrongfullyDetained) {
}