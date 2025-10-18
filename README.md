# 23223657_PDIAssignment
Bill Ahearne (23223657) PDI Assignment

CHECKLIST:
| Functionality | Implemented | Working Correctly | Notes |
|----------------|-------------|-------------------|--------|
| Load student records from CSV file | ✅ | ✅ | Successfully reads data from a correctly formatted CSV file and validates each record before adding it to the array. |
| Save student records back to CSV file | ✅ | ✅ | Writes all student data, including academic details, into the same file using a consistent header and field order. |
| Add new student | ✅ | ✅ | Fully functional with input validation for StudentID, names, course, year level, CWA, status, and credits earned. |
| Edit existing student | ✅ | ⚠️ Partially | Works for most fields, but invalid numeric input (like letters instead of numbers) can cause skipped edits. Needs stronger validation to re-prompt on error. |
| View all students | ✅ | ✅ | Displays a formatted table of all students with aligned columns for readability. |
| Filter students by course | ✅ | ✅ | Matches course name case-insensitively and displays all matching students. |
| Filter students by status (FT/PT) | ✅ | ✅ | Case-insensitive filter, outputs all students with matching status. |
| Display student(s) with highest CWA | ✅ | ✅ | Correctly identifies and displays all students sharing the highest CWA value. |
| Display average CWA for each course | ✅ | ✅ | Calculates averages per unique course and displays them correctly. |
| Credit analysis (graduation eligibility) | ✅ | ✅ | Correctly displays all students with ≥400 credits. |
| Input validation (general) | ✅ | ⚠️ Partially | Most validation implemented; however, exceptions from invalid typed input (e.g., non-numeric data) could be handled more gracefully. |
| Error handling (file not found, empty file) | ✅ | ✅ | Handles missing or unreadable files and starts with an empty list when needed. |
| Object-Oriented Design (use of classes, mutators, accessors) | ✅ | ✅ | `Student` and `Details` classes properly encapsulate data with accessors/mutators and validation. |

All core functionality has been implemented and verified. My program demonstrates proper object-oriented design principles using constructors, accessors, mutators, and encapsulation. Minor limitations include incomplete exception handling for invalid user input and limited CSV format validation.  These issues don't prevent program execution but may affect user experience in rare cases.  If further time were available, improvements would focus on full exception handling with user re-prompting, and advanced CSV error reporting.  


