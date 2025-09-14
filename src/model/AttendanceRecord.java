package model;

import db.DBConnection;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class AttendanceRecord {

    private int studentId;
    private Timestamp lastMarked;

    public AttendanceRecord(int studentId) {
        this.studentId = studentId;
        this.lastMarked = fetchLastAttendanceTime();
    }

    private Timestamp fetchLastAttendanceTime() {
        Timestamp ts = null;
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT timestamp FROM attendance WHERE student_id = ? ORDER BY timestamp DESC LIMIT 1";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ts = rs.getTimestamp("timestamp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }

    public boolean canMarkAttendance() {
        if (lastMarked == null) {
            return true;
        }

        LocalDateTime lastTime = lastMarked.toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.systemDefault());

        Duration duration = Duration.between(lastTime, currentTime);
        return duration.toHours() >= 6;
    }

    public boolean markAttendance() {
        if (!canMarkAttendance()) {
            return false;
        }

        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO attendance (student_id, timestamp) VALUES (?, NOW())";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, studentId);
            int result = ps.executeUpdate();

            // Update lastMarked if insert successful
            if (result > 0) {
                lastMarked = Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault()));
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Timestamp getLastMarkedTime() {
        return lastMarked;
    }
}
