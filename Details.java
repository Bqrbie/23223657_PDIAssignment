// -------------------------------------------------
// Class: Details
// Author: Bill Ahearne
// Date: 17 Oct 2025
// Purpose: Store a Students Academic Information
// -------------------------------------------------

public class Details {

    // Fields (private for encapsulation)
    private String course;
    private int yearLevel;
    private double CWA;
    private String status;
    private int creditsEarned;

    // Accessors

    public String getCourse(){
        return course;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public double getCWA() {
        return CWA;
    }

    public String getStatus() {
        return status;
    }

    public int getCreditsEarned() {
        return creditsEarned;
    }

    // Mutators

    public void setCourse(String newCourse) {
        if (newCourse != null && !newCourse.trim().isEmpty()) {
            course = newCourse;
        } else {
            System.out.println("Course name cannot be empty.");
        }
    }

    public void setYearLevel(int newYear) {
        if (newYear >= 1 && newYear <= 4) {
            yearLevel = newYear;
        } else {
            System.out.println("Invalid year level entered (must be between 1 and 4).");
        }
    }

    public void setCWA(double newCWA) {
        if (newCWA >= 0 && newCWA <= 100) {
            CWA = newCWA;
        } else {
            System.out.println("CWA must be between 0 and 100.");
        }
    }

    public void setStatus(String newStatus) {
        if (newStatus.equalsIgnoreCase("FT") || newStatus.equalsIgnoreCase("PT")) {
            status = newStatus.toUpperCase();
        } else {
            System.out.println("Status must be FT (Full Time) or PT (Part Time).");
        }
    }

    public void setCreditsEarned(int newCredits) {
        if (newCredits >= 0 && newCredits <= 400) {
            creditsEarned = newCredits;
        } else {
            System.out.println("Credits must be between 0 and 400.");
        }
    }

    // Display Details Information
    
    public void displayDetails() {
        System.out.println("Course: " + course + ", Year Level: " + yearLevel +
                           ", CWA: " + CWA + ", Status: " + status +
                           ", Credits Earned: " + creditsEarned);
    }
}