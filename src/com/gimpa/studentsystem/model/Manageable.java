package com.gimpa.studentsystem.model;

    public interface Manageable {

        //Adds this entity to the system.
        void add();

      //  Displays this entity's full details.
        void view();

        //Updates this entity's information.
        void update();

        // Removes this entity from the system permanently.
        void delete();

       // Checks whether this entity's data is valid and complete
       boolean validate();

      //  Returns a short one-line summary of this entity.
        String getSummary();
    }

