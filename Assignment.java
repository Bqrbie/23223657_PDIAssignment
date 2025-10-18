// -----------------------------------------------------
// Author: Bill Ahearne
// Date: 17 Oct 2025
// Purpose: Manage Student Records
// -----------------------------------------------------

//Use of Generative AI:
//Parts of this project were developed with assistance from GitHub Copilot 
//Tool: GitHub Copilot  
//URL: https://github.com/features/copilot  
//Dates used: 2025‑10‑17 to 2025‑10‑18  
//Scope: Used to draft selected methods in Student.java, Details.java, and Assignment.java (input validation, menu flow, CSV parsing).  
//I verified logic, corrected errors, added comments, and am responsible for the final code.

import java.io.*;

public class Assignment
{
    // Maximum number of students that can be stored
    private static final int MAX_STUDENTS = 100;
    // Array to store Student objects
    private static Student[] studentList = new Student[MAX_STUDENTS];
    // Counter for number of students currently loaded
    private static int studentCount = 0;

    // Table formats (same widths for header and rows so columns line up)
    // StudentID | FirstName | FamilyName | CourseEnrolled | YearLevel |  CWA  | Status | CreditsEarned
    private static final String HEADER_FMT = "%-10s %-12s %-12s %-22s %10s %8s %-8s %14s%n";
    private static final String ROW_FMT    = "%-10s %-12s %-12s %-22s %10d %8.2f %-8s %14d%n";
    private static final String SEP_LINE   = "----------------------------------------------------------------------------------------------";

    public static void main (String[] args)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String fileName = "";

        // Ask user for CSV filename at startup
        while (true) {
            try {
                System.out.print("Enter CSV filename (must be in this directory): ");
                fileName = br.readLine();
                if (fileName != null && !fileName.trim().isEmpty()) {
                    fileName = fileName.trim();
                    break;
                }
            } catch (IOException e) {
                System.out.println("Error reading filename. Try again.");
            }
        }

        // Load students from file 
        loadStudentsFromFile(fileName);

        // Main menu loop
        boolean running = true;
        while (running) {
            menuInterface();
            try {
                String choice = br.readLine();
                if (choice == null)
                    break;
                switch (choice.trim()) {
                    case "1":
                        addNewStudent(br, fileName);
                        break;
                    case "2":
                        editStudent(br, fileName);
                        break;
                    case "3":
                        viewAllStudents();
                        break;
                    case "4":
                        filterByCourse(br);
                        break;
                    case "5":
                        filterByStatus(br);
                        break;
                    case "6":
                        highestCWA();
                        break;
                    case "7":
                        averageCWAByCourse();
                        break;
                    case "8":
                        creditAnalysis();
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Input error.");
            }
        }
        // Save students back to file before exit
        saveStudentsToFile(fileName);
        System.out.println("Goodbye.");
    }

    // Loads student data from a CSV file into the studentList array 
    public static void loadStudentsFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean header = true;
            studentCount = 0; // Reset count for fresh load
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; } // skip header row
                String[] fields = line.split(",");
                if (fields.length < 8) continue; // skip malformed line

                // Trim all fields once
                String f0 = trimSafe(fields[0]);
                String f1 = trimSafe(fields[1]);
                String f2 = trimSafe(fields[2]);
                String f3 = trimSafe(fields[3]);
                String f4 = trimSafe(fields[4]);
                String f5 = trimSafe(fields[5]);
                String f6 = trimSafe(fields[6]);
                String f7 = trimSafe(fields[7]);

                // Pre-validate quietly to avoid setter prints:
                // - ID: 1..8 digits
                if (!isValidStudentID(f0)) continue;

                // - Required text fields non-empty
                if (isBlank(f1) || isBlank(f2) || isBlank(f3)) continue;

                // - Numbers parse and within ranges
                Integer year = tryParseInt(f4);
                Double cwa = tryParseDouble(f5);
                Integer credits = tryParseInt(f7);
                if (year == null || cwa == null || credits == null) continue;
                if (year < 1 || year > 4) continue;
                if (cwa < 0.0 || cwa > 100.0) continue;
                if (credits < 0 || credits > 400) continue;

                // - Status FT/PT
                if (!(f6.equalsIgnoreCase("FT") || f6.equalsIgnoreCase("PT"))) continue;

                // Build objects only after valid
                Student s = new Student();
                Details d = new Details();

                s.setStudentID(f0);         // now valid, setter won't print
                s.setFirstName(f1);
                s.setLastName(f2);

                d.setCourse(f3);
                d.setYearLevel(year);
                d.setCWA(cwa);
                d.setStatus(f6);
                d.setCreditsEarned(credits);

                s.setDetails(d);

                // Add student to array if space remains
                if (studentCount < MAX_STUDENTS) {
                    studentList[studentCount++] = s;
                }
            }
            System.out.println("Data loaded from file: " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("File not found — starting with empty list.");
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }

    // Saves student data in studentList array back to CSV file
    public static void saveStudentsToFile(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // Write header row
            bw.write("StudentID,FirstName,FamilyName,CourseEnrolled,YearLevel,CWA,Status,CreditsEarned\n");
            // Write each student as a row
            for (int i = 0; i < studentCount; i++) {
                Student s = studentList[i];
                Details d = s.getDetails();
                bw.write(s.getStudentID() + "," +
                         s.getFirstName() + "," +
                         s.getLastName() + "," +
                         d.getCourse() + "," +
                         d.getYearLevel() + "," +
                         d.getCWA() + "," +
                         d.getStatus() + "," +
                         d.getCreditsEarned() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving to file.");
        }
    }

    // Displays the main menu to the user
    public static void menuInterface()
    {
        System.out.println("--------------------------------------------");
        System.out.println("Welcome to the Student Record Manager!");
        System.out.println("--------------------------------------------");
        System.out.println();
        System.out.println("Please Select an Option");
        System.out.println();
        System.out.println("1> Add new student");
        System.out.println("2> Edit existing student");
        System.out.println("3> View all students");
        System.out.println("4> Filter by course");
        System.out.println("5> Filter by status");
        System.out.println("6> Highest CWA");
        System.out.println("7> Average CWA for each course");
        System.out.println("8> Credit analysis");
        System.out.println("0> Exit the Program");
        System.out.println();
        System.out.println("--------------------------------------------");
        System.out.print("Enter Your Choice: ");
    }

    // Adds a new student record, gets input from user, saves to file
    public static void addNewStudent(BufferedReader br, String fileName) throws IOException {
        if (studentCount >= MAX_STUDENTS) {
            System.out.println("Cannot add more students.");
            return;
        }
        Student s = new Student();
        Details d = new Details();

        // Prompt and set each field with input validation
        System.out.print("Enter Student ID (1-8 digits): ");
        s.setStudentID(readStudentID(br)); // loops until valid

        System.out.print("Enter First Name: ");
        s.setFirstName(readNonEmptyString(br));

        System.out.print("Enter Last Name: ");
        s.setLastName(readNonEmptyString(br));

        System.out.print("Enter Course Enrolled: ");
        d.setCourse(readNonEmptyString(br));

        d.setYearLevel(readIntInRange(br, "Year Level (1-4): ", 1, 4));
        d.setCWA(readDoubleInRange(br, "CWA (0-100): ", 0, 100));
        d.setStatus(readStatus(br));
        d.setCreditsEarned(readIntInRange(br, "Credits Earned (0-400): ", 0, 400));

        s.setDetails(d);

        studentList[studentCount++] = s;
        saveStudentsToFile(fileName);
        System.out.println("Student added.");
    }

    // Edits an existing student record found by Student ID
    public static void editStudent(BufferedReader br, String fileName) throws IOException {
        // Validate the ID used to locate the record
        String idToFind;
        while (true) {
            System.out.print("Enter Student ID to edit (1-8 digits): ");
            idToFind = br.readLine();
            if (idToFind != null && idToFind.trim().matches("\\d{1,8}")) break;
            System.out.println("Invalid Student ID. Must be 1 to 8 digits. Please retype.");
        }

        int idx = findStudentIndexByID(idToFind.trim());
        if (idx == -1) {
            System.out.println("Student not found.");
            return;
        }
        Student s = studentList[idx];
        Details d = s.getDetails();

        // Optional: allow changing the StudentID with validation
        System.out.print("New Student ID (1-8 digits, blank to keep): ");
        String newId = br.readLine();
        if (newId != null) {
            newId = newId.trim();
            while (!newId.isEmpty() && !newId.matches("\\d{1,8}")) {
                System.out.print("Invalid Student ID. Must be 1 to 8 digits. Retype (or blank to keep): ");
                newId = br.readLine();
                if (newId == null) { newId = ""; }
                newId = newId.trim();
            }
            if (!newId.isEmpty()) s.setStudentID(newId);
        }

        // Prompt for new values; blank input keeps old value
        System.out.print("New First Name (blank to keep): ");
        String val = br.readLine();
        if (val != null && !val.trim().isEmpty()) s.setFirstName(val.trim());

        System.out.print("New Family Name (blank to keep): ");
        val = br.readLine();
        if (val != null && !val.trim().isEmpty()) s.setLastName(val.trim());

        System.out.print("New Course Enrolled (blank to keep): ");
        val = br.readLine();
        if (val != null && !val.trim().isEmpty()) d.setCourse(val.trim());

        System.out.print("New Year Level (1-4, blank to keep): ");
        val = br.readLine();
        if (val != null && !val.trim().isEmpty()) {
            try { d.setYearLevel(Integer.parseInt(val.trim())); } catch (Exception e) {}
        }

        System.out.print("New CWA (0-100, blank to keep): ");
        val = br.readLine();
        if (val != null && !val.trim().isEmpty()) {
            try { d.setCWA(Double.parseDouble(val.trim())); } catch (Exception e) {}
        }

        System.out.print("New Status (FT/PT, blank to keep): ");
        val = br.readLine();
        if (val != null && !val.trim().isEmpty()) d.setStatus(val.trim());

        System.out.print("New Credits Earned (0-400, blank to keep): ");
        val = br.readLine();
        if (val != null && !val.trim().isEmpty()) {
            try { d.setCreditsEarned(Integer.parseInt(val.trim())); } catch (Exception e) {}
        }

        saveStudentsToFile(fileName);
        System.out.println("Student record updated.");
    }

    // Displays all students (header and rows use matching widths)
    public static void viewAllStudents() {
        // Print header
        System.out.printf(HEADER_FMT,
            "StudentID", "FirstName", "FamilyName", "CourseEnrolled",
            "YearLevel", "CWA", "Status", "CreditsEarned");

        // Separator line
        System.out.println(SEP_LINE);

        // Print each student row using the same column widths
        for (int i = 0; i < studentCount; i++) {
            Student s = studentList[i];
            Details d = s.getDetails();
            System.out.printf(ROW_FMT,
                s.getStudentID(),
                s.getFirstName(),
                s.getLastName(),
                d.getCourse(),
                d.getYearLevel(),
                d.getCWA(),
                d.getStatus(),
                d.getCreditsEarned());
        }
    }

    // Filters students by course and displays matching students
    public static void filterByCourse(BufferedReader br) throws IOException {
        System.out.print("Enter course to filter: ");
        String course = br.readLine().trim();

        boolean found = false;

        for (int i = 0; i < studentCount; i++) {
            if (studentList[i].getDetails().getCourse().equalsIgnoreCase(course)) {
                studentList[i].displayStudent();
                found = true;
            }
        }
        if (!found) System.out.println("No students found for course: " + course);
    }

    // Filters students by status (FT/PT) and displays matching students
    public static void filterByStatus(BufferedReader br) throws IOException {
        System.out.print("Enter status to filter by (FT/PT): ");
        String status = br.readLine().trim().toUpperCase();

        boolean found = false;

        for (int i = 0; i < studentCount; i++) {
            if (studentList[i].getDetails().getStatus().equalsIgnoreCase(status)) {
                studentList[i].displayStudent();
                found = true;
            }
        }
        if (!found) System.out.println("No students found with status: " + status);
    }

    // Displays student(s) with the highest CWA
    public static void highestCWA() {
        double max = -1;

        // Find highest CWA
        for (int i = 0; i < studentCount; i++) {
            double cwa = studentList[i].getDetails().getCWA();
            if (cwa > max) max = cwa;
        }
        System.out.println("Student(s) with highest CWA (" + max + "):");

        // Display students with highest CWA
        for (int i = 0; i < studentCount; i++) {
            if (studentList[i].getDetails().getCWA() == max) {
                studentList[i].displayStudent();
            }
        }
    }

    // Calculates and displays average CWA for each course
    public static void averageCWAByCourse() {
        String[] courses = new String[studentCount];
        int courseCount = 0;

        // Find unique courses
        for (int i = 0; i < studentCount; i++) {
            String course = studentList[i].getDetails().getCourse();

            boolean exists = false;
            for (int j = 0; j < courseCount; j++) {
                if (courses[j].equalsIgnoreCase(course)) { exists = true; break; }
            }
            if (!exists) courses[courseCount++] = course;
        }

        // Calculate average per course
        for (int k = 0; k < courseCount; k++) {
            String course = courses[k];
            double total = 0;
            int count = 0;

            for (int i = 0; i < studentCount; i++) {
                if (studentList[i].getDetails().getCourse().equalsIgnoreCase(course)) {
                    total += studentList[i].getDetails().getCWA();
                    count++;
                }
            }
            if (count > 0) {
                System.out.println(course + ": " + String.format("%.2f", total / count));
            }
        }
    }

    // Identifies and displays students eligible for graduation (credits >= 400)
    public static void creditAnalysis() {
        System.out.println("Students eligible for graduation (>=400 credits):");

        boolean found = false;

        for (int i = 0; i < studentCount; i++) {
            if (studentList[i].getDetails().getCreditsEarned() >= 400) {
                studentList[i].displayStudent();
                found = true;
            }
        }
        if (!found) System.out.println("No students eligible for graduation.");
    }

    // Finds a student by ID and returns their index in the array, or -1 if not found
    public static int findStudentIndexByID(String id) {
        if (id == null) return -1;
        for (int i = 0; i < studentCount; i++) {
            String sid = studentList[i].getStudentID();
            if (sid != null && sid.equalsIgnoreCase(id)) return i;
        }
        return -1;
    }

    // Reads a non-empty string from user input
    public static String readNonEmptyString(BufferedReader br) throws IOException {
        String s = "";

        while ((s = br.readLine()) != null) {
            s = s.trim();
            if (!s.isEmpty()) break;
            System.out.print("Input cannot be empty, try again: ");
        }
        return s;
    }

    // Reads a valid StudentID (1-8 digits); loops until valid
    public static String readStudentID(BufferedReader br) throws IOException {
        while (true) {
            String s = br.readLine();
            if (s == null) return null; // EOF: let caller handle
            s = s.trim();
            if (s.matches("\\d{1,8}")) return s; // 1 to 8 digits
            System.out.print("Invalid Student ID. Must be 1 to 8 digits. Please re-enter: ");
        }
    }

    // Reads an integer from user input, enforcing a valid range
    public static int readIntInRange(BufferedReader br, String prompt, int min, int max) throws IOException {
        while (true) {
            System.out.print(prompt);
            String s = br.readLine();

            try {
                int val = Integer.parseInt(s.trim());
                if (val >= min && val <= max) return val;
            }
            catch (Exception e) {}
            System.out.println("Invalid, must be between " + min + " and " + max + ".");
        }
    }

    // Reads a double from user input, enforcing a valid range
    public static double readDoubleInRange(BufferedReader br, String prompt, double min, double max) throws IOException {
        while (true) {
            System.out.print(prompt);
            String s = br.readLine();

            try {
                double val = Double.parseDouble(s.trim());
                if (val >= min && val <= max) return val;
            }
            catch (Exception e) {}
            System.out.println("Invalid, must be between " + min + " and " + max + ".");
        }
    }

    // Reads and validates student status (FT/PT) from user input
    public static String readStatus(BufferedReader br) throws IOException {
        while (true) {
            System.out.print("Status (FT/PT): ");
            String s = br.readLine();
            if (s != null && (s.trim().equalsIgnoreCase("FT") || s.trim().equalsIgnoreCase("PT"))) {
                return s.trim().toUpperCase();
            }
            System.out.println("Must be FT or PT.");
        }
    }

    private static String trimSafe(String s) {
        return s == null ? "" : s.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static boolean isValidStudentID(String s) {
        return s != null && s.matches("\\d{1,8}");
    }

    private static Integer tryParseInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return null; }
    }

    private static Double tryParseDouble(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return null; }
    }
}