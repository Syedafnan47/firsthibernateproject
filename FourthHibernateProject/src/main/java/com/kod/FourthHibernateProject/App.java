package com.kod.FourthHibernateProject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

public class App {

    private static SessionFactory sessionFactory;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // Build SessionFactory once
        Configuration cfg = new Configuration().configure();
        cfg.addAnnotatedClass(Student.class);
        sessionFactory = cfg.buildSessionFactory();

        while (true) {
            // menu statements directly in the loop (no separate method)
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. Get Student by ID");
            System.out.println("3. List All Students");
            System.out.println("4. Update Student (uses setters + merge)");
            System.out.println("5. Delete Student");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createStudent();
                    break;
                case "2":
                    getStudentById();
                    break;
                case "3":
                    listStudents();
                    break;
                case "4":
                    updateStudentByIdUsingMerge();
                    break;
                case "5":
                    deleteStudentById();
                    break;
                case "0":
                    System.out.println("Exiting...");
                    sessionFactory.close();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ----------------------- CREATE -------------------------
    private static void createStudent() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter marks: ");
        int marks = Integer.parseInt(scanner.nextLine());

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Student s = new Student(name, email, marks);
        session.persist(s);

        tx.commit();
        session.close();

        System.out.println("Student added successfully!");
    }

    // ----------------------- READ ----------------------------
    private static void getStudentById() {
        System.out.print("Enter student ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        Session session = sessionFactory.openSession();
        Student s = session.get(Student.class, id);

        if (s == null) {
            System.out.println("No student found with ID " + id);
        } else {
            System.out.println(s);
        }

        session.close();
    }

    // ----------------------- LIST ALL ------------------------
    private static void listStudents() {
        Session session = sessionFactory.openSession();
        List<Student> list = session.createQuery("from Student", Student.class).list();

        if (list.isEmpty()) {
            System.out.println("No students found.");
        } else {
            list.forEach(System.out::println);
        }

        session.close();
    }

    // ----------------------- UPDATE (setters + merge) ---------------------------
    private static void updateStudentByIdUsingMerge() {
        System.out.print("Enter student ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Student s = session.get(Student.class, id);

        if (s == null) {
            System.out.println("No student found with ID " + id);
            tx.commit();
            session.close();
            return;
        }

        System.out.println("Current: " + s);

        // ask for new values and use setters
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        s.setName(newName);

        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();
        s.setEmail(newEmail);

        System.out.print("Enter new marks: ");
        int newMarks = Integer.parseInt(scanner.nextLine());
        s.setMarks(newMarks);

        // use merge to update the entity (merge returns the managed instance)
        session.merge(s);

        tx.commit();
        session.close();

        System.out.println("Student updated successfully!");
    }

    // ----------------------- DELETE ---------------------------
    private static void deleteStudentById() {
        System.out.print("Enter student ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Student s = session.get(Student.class, id);

        if (s == null) {
            System.out.println("No student found with ID " + id);
            tx.commit();
            session.close();
            return;
        }

        session.remove(s);
        tx.commit();
        session.close();

        System.out.println("Student deleted successfully!");
    }
}
