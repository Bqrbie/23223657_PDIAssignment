// -------------------------------------------------
// Class: Student
// Author: Bill Ahearne
// Date: 17 Oct 2025
// Purpose: Store a Students Personal Information and
//          Details via Details Class
// -------------------------------------------------

public class Student {

    // Fields (private for encapsulation)
    private String studentID;
    private String firstName;
    private String lastName;
    private Details details; // Composition: Student has a Details object

    // Constructor
    public Student() {
        details = new Details();
    }

    // Accessors
    public String getStudentID() {
        return studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Details getDetails() {
        return details;
    }

    // Mutators
    public void setStudentID(String newID) {
        if (newID == null) {
            System.out.println("Student ID cannot be empty.");
            return;
        }
        String id = newID.trim();
        // Accept only 1 to 8 digits
        if (id.matches("\\d{1,8}")) {
            studentID = id;
        } else {
            System.out.println("Invalid Student ID. It must be 1-8 integers.");
        }
    }

    public void setFirstName(String newFirst) {
        if (newFirst != null && !newFirst.trim().isEmpty()) {
            firstName = newFirst;
        } else {
            System.out.println("First name cannot be empty.");
        }
    }

    public void setLastName(String newLast) {
        if (newLast != null && !newLast.trim().isEmpty()) {
            lastName = newLast;
        } else {
            System.out.println("Last name cannot be empty.");
        }
    }

    public void setDetails(Details newDetails) {
        if (newDetails != null) {
            details = newDetails;
        } else {
            System.out.println("Details object cannot be null.");
        }
    }

    // Display Student Information
    public void displayStudent() {
        System.out.println("=================================");
        System.out.println("Student ID: " + studentID);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.print("Academic Details: ");
        details.displayDetails();
        System.out.println("=================================");
    }
}