/**
 * Represents a student who can claim items.
 * Java Concept: Classes & Objects, Constructors
 */
public class Student {

    private String studentId;
    private String name;
    private String contact;
    private String department;

    public Student(String studentId, String name, String contact, String department) {
        this.studentId = studentId;
        this.name = name;
        this.contact = contact;
        this.department = department;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getDepartment() { return department; }

    @Override
    public String toString() {
        return String.format("%-8s | %-20s | %-14s | %-15s", studentId, name, contact, department);
    }
}
