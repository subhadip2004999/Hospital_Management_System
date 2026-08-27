package com.hospitalmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Appointment {
    private Connection con;
    private Scanner sc;

    // Constructor
    public Appointment(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    // Helper method to check if a record exists in patients/doctors tables
    private boolean checkEntityExists(String tableName, int id) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
        return false;
    }

    // Method 1: Book Appointment
    public void bookAppointments(){
        System.out.print("Enter Patient Id: ");
        int pid = sc.nextInt();

        System.out.print("Enter Doctor Id: ");
        int did = sc.nextInt();

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.next();
        sc.nextLine(); // Clear scanner buffer

        // Validate Patient & Doctor existence
        if (!checkEntityExists("patients", pid)) {
            System.out.println("Error: Patient ID " + pid + " does not exist!");
            return;
        }

        if (!checkEntityExists("doctors", did)) {
            System.out.println("Error: Doctor ID " + did + " does not exist!");
            return;
        }

        // Check availability
        String checkQuery = "SELECT COUNT(*) FROM appointments WHERE DoctorId = ? AND Date = ?";
        try (PreparedStatement ps = con.prepareStatement(checkQuery)) {
            ps.setInt(1, did);
            ps.setString(2, date);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("\nThis doctor is already booked on " + date + ".");
                    return;
                }
            }

            // Insert new appointment (AppointmentId auto-increments automatically)
            String insertQuery = "INSERT INTO appointments (PatientId, DoctorId, Date) VALUES (?, ?, ?)";
            try (PreparedStatement insertPs = con.prepareStatement(insertQuery)) {
                insertPs.setInt(1, pid);
                insertPs.setInt(2, did);
                insertPs.setString(3, date);
                
                int rows = insertPs.executeUpdate();
                if (rows > 0) {
                    System.out.println("\nAppointment booked successfully.");
                } else {
                    System.out.println("Failed to book appointment.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method 2: View Appointments
    public void viewAppointments(){
        String query = "SELECT * FROM appointments";

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\nAppointments Table: ");
            System.out.println("+---------------+------------+-----------+---------------+");
            System.out.println("| AppointmentId | Patient Id | Doctor Id | Date          |");
            System.out.println("+---------------+------------+-----------+---------------+");

            while(rs.next()){
                // Fetched using exact schema column name: AppointmentId
                int appointmentId = rs.getInt("AppointmentId"); 
                int pid = rs.getInt("PatientId");
                int did = rs.getInt("DoctorId");
                String date = rs.getString("Date");

                System.out.printf("| %-13d | %-10d | %-9d | %-13s |\n", appointmentId, pid, did, date);
                System.out.println("+---------------+------------+-----------+---------------+");
            }

        } catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}