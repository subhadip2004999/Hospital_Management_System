package com.hospitalmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "";
    private static final String password = "";

    public static void main(String[] args) {
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver Class Not Found: " + e.getMessage());
            return;
        }

        Scanner sc = new Scanner(System.in);
        
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            Patient patient = new Patient(con, sc);
            Doctor doctor = new Doctor(con, sc);
            Appointment appointment = new Appointment(con, sc);

            while(true){
                System.out.println("\n|| HOSPITAL MANAGEMENT SYSTEM ||\n - Made by Subhadip Biswas\n");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient Table");
                System.out.println("3. Add Doctor");
                System.out.println("4. View Doctor Table");
                System.out.println("5. Book Appointment");
                System.out.println("6. Show Appointment Table");
                System.out.println("7. Exit\n");
                System.out.print("Enter Your Choice: ");

                int choice;
                try {
                    choice = sc.nextInt();
                    sc.nextLine(); // Clear newline character after reading choice integer
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a valid menu number.\n");
                    sc.nextLine(); // Clear the invalid token from buffer
                    continue;
                }

                switch(choice){
                    case 1:
                        patient.addPatients();
                        System.out.println();
                        sc.nextLine();
                        break;
                    
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                        sc.nextLine();
                        break;

                    case 3:
                        doctor.addDoctors();
                        System.out.println();
                        sc.nextLine();
                        break;

                    case 4:
                        doctor.viewDoctors();
                        System.out.println();
                        sc.nextLine();
                        break;

                    case 5:
                        appointment.bookAppointments();
                        System.out.println();
                        sc.nextLine();
                        break;

                    case 6:
                        appointment.viewAppointments();
                        System.out.println();
                        sc.nextLine();
                        break;

                    case 7:
                        System.out.println("Exiting System... Thank you!");
                        return;

                    default:
                        System.out.println("Enter a valid choice!\n");
                        sc.nextLine();
                        break;
                }
            }
            
        } catch (SQLException e){
            System.out.println("Database Error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}