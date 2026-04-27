package com.gimpa.studentsystem.model;

public class PolymorphismDemo {

    // This method accepts ANY Person object - Student or Instructor.
    public static void printPersonDetails(Person person) {
        System.out.println("====================================");
        System.out.println("Role detected : " + person.getRole());
        System.out.println("Name          : " + person.getName());
        System.out.println("Email         : " + person.getEmail());
        System.out.println("------------------------------------");

        person.displayInfo();
    }

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     POLYMORPHISM DEMONSTRATION       ║");
        System.out.println("╚══════════════════════════════════════╝");

        Person student1 = new Student(
                "GIMPA001",
                "Kwame Mensah",
                "kwame@gmail.com",
                "0244123456",
                "Computer Science",
                2
        );

        Person instructor1 = new Instructor(
                "EMP001",
                "Daniel Apuri",
                "apuri@gimpa.edu.gh",
                "0201234567",
                "Computer Science",
                "Dr."
        );

        Person student2 = new Student(
                "GIMPA002",
                "Ama Owusu",
                "ama@gmail.com",
                "0277654321",
                "Information Technology",
                1
        );

        System.out.println("\n--- Passing each Person to printPersonDetails() ---\n");

        // Same method called three times.
        // Each call behaves differently based on the actual object type inside.
        printPersonDetails(student1);
        printPersonDetails(instructor1);
        printPersonDetails(student2);


        // ===== ARRAY DEMONSTRATION =====

        System.out.println("\n--- Person array holding mixed types ---\n");

        // One array declared as Person[] but holding Students and Instructors.
        // This is only possible because of polymorphism.
        Person[] people = new Person[3];
        people[0] = student1;
        people[1] = instructor1;
        people[2] = student2;


        for (Person person : people) {
            System.out.printf("%-12s : %s%n", person.getRole(), person.getName());
        }

        // ===== INSTANCEOF CHECK =====

        System.out.println("\n--- Checking actual type with instanceof ---\n");

        // Sometimes we need to know the real type.
        // instanceof checks what an object actually is at runtime.
        for (Person person : people) {
            if (person instanceof Student) {
                // Cast to Student so we can call Student-specific methods
                Student s = (Student) person;
                System.out.println(s.getName() + " is a Student. Summary: "
                        + s.getSummary());

            } else if (person instanceof Instructor) {
                // Cast to Instructor so we can call Instructor-specific methods
                Instructor i = (Instructor) person;
                System.out.println(i.getName() + " is an Instructor. Full title: "
                        + i.getFullNameWithTitle());
            }
        }

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║     DEMONSTRATION COMPLETE           ║");
        System.out.println("╚══════════════════════════════════════╝");
    }
}

