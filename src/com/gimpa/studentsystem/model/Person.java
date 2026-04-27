package com.gimpa.studentsystem.model;

public abstract class Person {

    // ===== PRIVATE FIELDS =====
    // Common attributes every person has regardless of their role
    private String name;
    private String email;
    private String phone;


    // ===== DEFAULT CONSTRUCTOR =====
    public Person() {
    }


    // ===== PARAMETERIZED CONSTRUCTOR =====
    public Person(String name, String email, String phone) {
        setName(name);
        setEmail(email);
        setPhone(phone);
    }

        // ===== GETTERS =====
        public String getName () {
            return name;
        }
        public String getEmail () {
            return email;
        }
        public String getPhone () {
            return phone;
        }


        // ===== SETTERS WITH VALIDATION =====

        public void setName (String name){
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            this.name = name.trim();
        }
        public void setEmail(String email) {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (!email.contains("@") || !email.contains(".")) {
                throw new IllegalArgumentException("Invalid email format");
            }
            this.email = email.toLowerCase().trim();
        }

        public void setPhone(String phone) {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone cannot be empty");
            }
            this.phone = phone.trim();
        }


// ABSTRACT METHOD - every child must implement these methods

        // every person must be able to return their role
        public abstract String getRole();

        // every person must be able to display their own information
        public abstract void displayInfo();


// SHARED DISPLAY METHOD - Child classes call this to print the common fields,
        protected void displayCommonInfo() {                                    // "protected" means only this class and its children can use it
            System.out.println("========================================");
            System.out.println("Role         : " + getRole());
            System.out.println("Name         : " + name);
            System.out.println("Email        : " + email);
            System.out.println("Phone        : " + phone);
        }


        @Override
        public String toString() {
            return getRole() + " | " + name + " | " + email;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Person person = (Person) obj;
            return email != null && email.equals(person.email);
        }

        @Override
        public int hashCode() {
            return email != null ? email.hashCode() : 0;
        }
    }

