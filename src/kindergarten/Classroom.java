package kindergarten;

/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student
 * in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given
 * seat), and
 * - a Student array parallel to seatingAvailability to show students filed into
 * seats
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in
 * studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine; // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs; // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability; // represents the classroom seats that are available to students
    private Student[][] studentsSitting; // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * 
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom(SNode l, SNode m, boolean[][] a, Student[][] s) {
        studentsInLine = l;
        musicalChairs = m;
        seatingAvailability = a;
        studentsSitting = s;
    }

    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in
     * line.
     * 
     * Reads students from input file and inserts these students in alphabetical
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the
     * file, say x
     * 2) x lines containing one student per line. Each line has the following
     * student
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom(String filename) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(filename);
        Student[] studentArray = new Student[StdIn.readInt()];
        for (int i = 0; i < studentArray.length; i++) {
            studentArray[i] = new Student(StdIn.readString(), StdIn.readString(), StdIn.readInt());
        }
        quickSort(studentArray, 0, studentArray.length - 1);
        studentsInLine = new SNode();
        SNode ptr = studentsInLine;
        for (int i = 0; i < studentArray.length; i++) {
            ptr.setStudent(studentArray[i]);
            ptr.setNext(new SNode());
            if (i + 1 == studentArray.length)
                ptr.setNext(null);
            ptr = ptr.getNext();
        }
    }

    private static void quickSort(Student[] studentArray, int low, int high) {
        if (low < high) {
            int i = split(studentArray, low, high);
            quickSort(studentArray, low, i - 1);
            quickSort(studentArray, i + 1, high);
        }
    }

    private static int split(Student[] studentArray, int low, int high) {
        Student pivot = studentArray[high];
        int i = low - 1;
        for (int j = low; j <= high - 1; j++) {
            if (studentArray[j].compareNameTo(pivot) < 0) {
                i++;
                swap(studentArray, i, j);
            }
        }
        swap(studentArray, i + 1, high);
        return i + 1;
    }

    private static void swap(Student[] studentArray, int i, int j) {
        Student tempStudent = studentArray[i];
        studentArray[i] = studentArray[j];
        studentArray[j] = tempStudent;
    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of
     * available seats inside the classroom. Imagine that unavailable seats are
     * broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an
     * available seat)
     * 
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(seatingChart);
        int r = StdIn.readInt(), c = StdIn.readInt();
        seatingAvailability = new boolean[r][c];
        studentsSitting = new Student[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                seatingAvailability[i][j] = StdIn.readBoolean();
            }
        }
    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the
     * front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into
     * studentsSitting according to
     * seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents() {

        // WRITE YOUR CODE HERE
        for (int i = 0; i < seatingAvailability.length; i++) {
            for (int j = 0; j < seatingAvailability[0].length; j++) {
                if (seatingAvailability[i][j] && studentsInLine != null && studentsSitting[i][j] == null) {
                    studentsSitting[i][j] = studentsInLine.getStudent();
                    studentsInLine = studentsInLine.getNext();
                }
            }
        }
    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then
     * moves
     * into second row.
     */
    public void insertMusicalChairs() {

        // WRITE YOUR CODE HERE

        for (int i = studentsSitting.length - 1; i >= 0; i--) {
            for (int j = studentsSitting[0].length - 1; j >= 0; j--) {
                if (studentsSitting[i][j] != null) {
                    SNode temp = new SNode();
                    if (musicalChairs == null) {
                        temp.setStudent(studentsSitting[i][j]);
                        temp.setNext(temp);
                        musicalChairs = temp;
                        studentsSitting[i][j] = null;
                    } else {
                        temp.setStudent(studentsSitting[i][j]);
                        temp.setNext(musicalChairs.getNext());
                        musicalChairs.setNext(temp);
                        studentsSitting[i][j] = null;
                    }
                }
            }
        }

    }

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is
     * only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using
     * StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first
     * student in the
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in
     * studentsInLine
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students
     * can be seated.
     */
    public void playMusicalChairs() {

        // WRITE YOUR CODE HERE
        int n = 0;
        while (sizeOfList() != 1) {
            // print();
            int lolol = sizeOfList();
            // System.out.println("banana");
            int x = StdRandom.uniform(sizeOfList());
            SNode removed = null;
            if (x == 0) {
                removed = new SNode(musicalChairs.getNext().getStudent(), null);
                musicalChairs.setNext(musicalChairs.getNext().getNext());

            } else {
                SNode temp = musicalChairs;
                for (int i = 0; i < x; i++) {
                    temp = temp.getNext();

                }

                removed = new SNode(temp.getNext().getStudent(), null);
                if (x == sizeOfList() - 1) {
                    // System.out.println("<------------------");
                    musicalChairs = temp;
                }
                temp.setNext(temp.getNext().getNext());

            }
            addToStudentsInLine(removed);
            // n++;
            // System.out.println(n);

        }

        // System.out.println("apple");

        label: for (int i = 0; i < seatingAvailability.length; i++) {
            for (int j = 0; j < seatingAvailability[0].length; j++) {
                if (seatingAvailability[i][j] && studentsSitting[i][j] == null) {
                    studentsSitting[i][j] = musicalChairs.getStudent();
                    break label;
                }
            }
        }
        musicalChairs = null;
        seatStudents();
    }

    // ---------------------
    // | |
    // 0 - 1 - 2 - 3 - 4 - 5

    private void print() {
        SNode ptr = musicalChairs.getNext();
        while (ptr != musicalChairs) {
            System.out.print(ptr.getStudent().getFirstName() + " ---> ");
            ptr = ptr.getNext();
        }
        System.out.print(musicalChairs.getStudent().getFirstName());
        System.out.println();
        System.out.println();

    }

    private void print2() {
        SNode ptr = studentsInLine;
        while (ptr != null) {
            System.out.print(ptr.getStudent().getFirstName() + "(" + ptr.getStudent().getHeight() + ") ---> ");
            ptr = ptr.getNext();
        }
        System.out.print("null");
        System.out.println();
        System.out.println();

    }

    private void addToStudentsInLine(SNode x) { // for some reason height does not do in order in some cases

        /*
         * 
         * F. Wolf G. Mata P. Ferg M. Brow S. Sink W. Ryde N. Schn
         * X X X C. McLa X M. Hawk X
         * X J. Keer X C. Heat X P. Reis X
         * X X X D. Mont X D. Harb X
         * M. Modi EMPTY EMPTY EMPTY EMPTY EMPTY EMPTY
         * 
         * P. Ferg and G. Mata should be switched
         * 
         * 
         * 
         */

        if (studentsInLine == null)
            studentsInLine = x;
        else if (studentsInLine.getNext() == null) {
            if (studentsInLine.getStudent().getHeight() > x.getStudent().getHeight()) {
                x.setNext(studentsInLine);
                studentsInLine = x;
            } else {
                studentsInLine.setNext(x);
            }

        } else {
            SNode prev = studentsInLine;
            SNode curr = studentsInLine.getNext();

            int xHeight = x.getStudent().getHeight();
            int currHeight = curr.getStudent().getHeight();

            while (curr != null && curr.getStudent().getHeight() <= xHeight) {
                // System.out.println("orange");
                prev = curr;
                curr = curr.getNext();
                if (curr != null) {
                    currHeight = curr.getStudent().getHeight();
                }
            }

            if (prev.getStudent().getHeight() > xHeight) {
                x.setNext(studentsInLine);
                studentsInLine = x;
            } else {
                x.setNext(curr);
                prev.setNext(x);
            }
        }
        // print2();
    }

    private int sizeOfList() {
        SNode temp = musicalChairs.getNext();
        int n = 1;
        while (temp != musicalChairs) {
            n++;
            temp = temp.getNext();
        }
        return n;
    }

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is
     * not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * 
     * @param firstName the first name
     * @param lastName  the last name
     * @param height    the height of the student
     */
    public void addLateStudent(String firstName, String lastName, int height) {

        // WRITE YOUR CODE HERE
        Student s = new Student(firstName, lastName, height);
        if (studentsInLine != null) {
            SNode ptr = studentsInLine;
            while (ptr.getNext() != null) {
                ptr = ptr.getNext();
            }
            ptr.setNext(new SNode(s,null));
        }
        else if (musicalChairs != null) {

            SNode temp = new SNode(s, musicalChairs.getNext());
            musicalChairs.setNext(temp);
            musicalChairs = temp;
        }
        else {
            label: for (int i = 0; i < seatingAvailability.length; i++) {
                for (int j = 0; j < seatingAvailability[0].length; j++) {
                    if (seatingAvailability[i][j] && studentsSitting[i][j] == null) {
                        studentsSitting[i][j] = s;
                        break label;
                    }
                }
            }
        }
    }

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName  the student's last name
     */
    public void deleteLeavingStudent(String firstName, String lastName) {

        // WRITE YOUR CODE HERE
        String fullName = firstName + " " + lastName;
        if (studentsInLine != null) {
            SNode prv = studentsInLine;
            SNode ptr = studentsInLine.getNext();
            if (studentsInLine.getStudent().getFullName().equals(fullName)) {
                studentsInLine = studentsInLine.getNext();
                return;
            }
            while (ptr != null && !ptr.getStudent().getFullName().equals(fullName)) {
                prv = ptr;
                ptr = ptr.getNext();
            }
            if (ptr != null)
                prv.setNext(ptr.getNext());
        }
        else if (musicalChairs != null) {

            SNode prv = musicalChairs.getNext();
            SNode ptr = musicalChairs.getNext().getNext();

            if (prv.getStudent().getFullName().equals(fullName)) {
                musicalChairs.setNext(ptr);
                return;
            }

            while (ptr != musicalChairs && !ptr.getStudent().getFullName().equals(fullName)) {
                prv = ptr;
                ptr = ptr.getNext();
            }
            if (ptr != null)
                prv.setNext(ptr.getNext());
            if (ptr == musicalChairs) {
                musicalChairs = prv;
            }
        }
        else {
            label: for (int i = 0; i < seatingAvailability.length; i++) {
                for (int j = 0; j < seatingAvailability[0].length; j++) {
                    if (seatingAvailability[i][j] && studentsSitting[i][j] != null && studentsSitting[i][j].getFullName().equals(fullName)) {
                        studentsSitting[i][j] = null;
                        break label;
                    }
                }
            }
        }
    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine() {

        // Print studentsInLine
        StdOut.println("Students in Line:");
        if (studentsInLine == null) {
            StdOut.println("EMPTY");
        }

        for (SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext()) {
            StdOut.print(ptr.getStudent().print());
            if (ptr.getNext() != null) {
                StdOut.print(" -> ");
            }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents() {

        StdOut.println("Sitting Students:");

        if (studentsSitting != null) {

            for (int i = 0; i < studentsSitting.length; i++) {
                for (int j = 0; j < studentsSitting[i].length; j++) {

                    String stringToPrint = "";
                    if (studentsSitting[i][j] == null) {

                        if (seatingAvailability[i][j] == false) {
                            stringToPrint = "X";
                        } else {
                            stringToPrint = "EMPTY";
                        }

                    } else {
                        stringToPrint = studentsSitting[i][j].print();
                    }

                    StdOut.print(stringToPrint);

                    for (int o = 0; o < (10 - stringToPrint.length()); o++) {
                        StdOut.print(" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs() {
        StdOut.println("Students in Musical Chairs:");

        if (musicalChairs == null) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for (ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext()) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if (ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() {
        return studentsInLine;
    }

    public void setStudentsInLine(SNode l) {
        studentsInLine = l;
    }

    public SNode getMusicalChairs() {
        return musicalChairs;
    }

    public void setMusicalChairs(SNode m) {
        musicalChairs = m;
    }

    public boolean[][] getSeatingAvailability() {
        return seatingAvailability;
    }

    public void setSeatingAvailability(boolean[][] a) {
        seatingAvailability = a;
    }

    public Student[][] getStudentsSitting() {
        return studentsSitting;
    }

    public void setStudentsSitting(Student[][] s) {
        studentsSitting = s;
    }

}
