package com.hospitalmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection con;
    private Scanner sc;

    // Constructor
    public Doctor(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    // Method 1: Add Doctor
    public void addDoctors(){
        System.out.print("Enter Doctor Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Doctor Specialization: ");
        String specialization = sc.nextLine();

        System.out.print("Enter Doctor Phone Number: ");
        String phone = sc.nextLine(); // Changed to String to handle 10+ digits reliably

        String query = "INSERT INTO doctors(Name, Specialization, Phone) VALUES (?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, specialization);
            ps.setString(3, phone);

            int res = ps.executeUpdate();
            if(res > 0){
                System.out.println("\nDoctor added successfully.");
            } else {
                System.out.println("Failed Operation !");
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method 2: View Doctors
    public void viewDoctors(){
        String query = "SELECT * FROM doctors";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nDoctor's Table: ");
            System.out.println("+-------+-------------------------+-------------------------+--------------------+");
            System.out.println("| Id    | Doctor's Name           | Specialization          | Phone Number       |");
            System.out.println("+-------+-------------------------+-------------------------+--------------------+");
            
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("Name");
                String specialization = rs.getString("Specialization");
                String phone = rs.getString("Phone");

                // Added missing specifier types (%d for int, %s for String)
                System.out.printf("| %-5d | %-23s | %-23s | %-18s |\n", id, name, specialization, phone);
                System.out.println("+-------+-------------------------+-------------------------+--------------------+");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}