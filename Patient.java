package com.hospitalmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    
    private Connection con;
    private Scanner sc;

    // Constructor
    public Patient(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    // Method 1: Add Patient
    public void addPatients(){
        System.out.print("Enter Patient Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Patient Age: ");
        int age = sc.nextInt();
        sc.nextLine(); // Consume remaining newline after nextInt()

        System.out.print("Enter Patient Gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter Patient Phone Number: ");
        String phone = sc.nextLine(); // Changed to String to handle 10+ digit numbers

        String query = "INSERT INTO patients(Name, Age, Gender, Phone) VALUES (?,?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, phone);

            int res = ps.executeUpdate();
            if(res > 0){
                System.out.println("\nPatient added successfully.");
            } else {
                System.out.println("Failed Operation!");
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method 2: View Patients
    public void viewPatients(){
        String query = "SELECT * FROM patients";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nPatient's Table: ");
            System.out.println("+-------+-------------------------+-------+------------+--------------------+");
            System.out.println("| Id    | Patient's Name          | Age   | Gender     | Phone Number       |");
            System.out.println("+-------+-------------------------+-------+------------+--------------------+");
            
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("Name");
                int age = rs.getInt("Age");
                String gender = rs.getString("Gender");
                String phone = rs.getString("Phone");

                // Added missing specifier types (%d for int, %s for String)
                System.out.printf("| %-5d | %-23s | %-5d | %-10s | %-18s |\n", id, name, age, gender, phone);
                System.out.println("+-------+-------------------------+-------+------------+--------------------+");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}