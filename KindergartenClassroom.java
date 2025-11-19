package kindergarten;
/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }
    private void sortABCList(SNode studentsInLine){
        SNode current = studentsInLine; 
        SNode pointer = null;
        Student temp = null;
        if (studentsInLine == null) {
            return;
        }
        else {
            while (current != null) {
                pointer = current.getNext();
                while (pointer != null) {
                    if (pointer.getStudent().compareNameTo(current.getStudent()) < 0) {
                        temp = current.getStudent();
                        current.setStudent(pointer.getStudent());
                        pointer.setStudent(temp);
                    }
                    pointer = pointer.getNext();
                }
                current = current.getNext();
            }
        }
    }


    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {
        StdIn.setFile(filename);
        int x = StdIn.readInt();
        SNode ptr = null;
        for(int i = 0; i < x; i++){
            Student nextStudent = new Student(StdIn.readString(), StdIn.readString(), StdIn.readInt());
            SNode nextNode = new SNode(nextStudent, null);
            if(studentsInLine == null){
                studentsInLine = nextNode;
                ptr = studentsInLine;
            }
            ptr.setNext(nextNode);
            ptr = ptr.getNext();
        }
        sortABCList(studentsInLine);
    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *  
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {
        StdIn.setFile(seatingChart);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        studentsSitting = new Student[r][c];
        seatingAvailability = new boolean[r][c];
        for(int i=0; i<r; i++){
            for(int j=0; j<c; j++){
                seatingAvailability[i][j] = StdIn.readBoolean();
            }
        }
    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents () {
        for( int i = 0; i < seatingAvailability.length; i++){
            for( int j = 0; j < seatingAvailability[0].length; j++){
                if(musicalChairs != null && seatingAvailability[i][j]){
                    studentsSitting[i][j] = musicalChairs.getStudent();
                    seatingAvailability[i][j] = false;
                    musicalChairs = null;
                }
                if(studentsInLine == null)
                    break;
                if(seatingAvailability[i][j]){
                    studentsSitting[i][j] = studentsInLine.getStudent();
                    studentsInLine = studentsInLine.getNext();
                    seatingAvailability[i][j] = false;
                }
            }
            if(studentsInLine == null)
            break;
        }
    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {
        SNode lead = null;
        for(int i = 0; i < studentsSitting.length; i++){
            for( int j = 0; j < studentsSitting[0].length; j++){
                if(studentsSitting[i][j] != null){
                    SNode node = new SNode(studentsSitting[i][j], null);

                    if(musicalChairs == null){
                        musicalChairs = node;
                        musicalChairs.setNext(musicalChairs);
                        seatingAvailability[i][j] = true;
                        lead = musicalChairs;
                    }
                    else{
                        node.setNext(lead);
                        musicalChairs.setNext(node);
                        seatingAvailability[i][j] = true;
                        musicalChairs = node;
                    }
                    studentsSitting[i][j] = null;
                }
            }
        }
    }

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the 
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in studentsInLine 
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    private SNode sortHeightList(){
        if( studentsInLine == null ){
			return studentsInLine;
		}
		SNode helper = new SNode(); //new starter of the sorted list
		SNode cur = studentsInLine; //the node will be inserted
		SNode pre = helper; //insert node between pre and pre.next
		SNode next = null; //the next node will be inserted
		//not the end of input list
		while( cur != null ){
			next = cur.getNext();
			//find the right place to insert
			while( pre.getNext() != null && pre.getNext().getStudent().getHeight() < cur.getStudent().getHeight() ){
				pre = pre.getNext();
			}
			//insert between pre and pre.next
			cur.setNext(pre.getNext());
			pre.setNext(cur);
			pre = helper;
			cur = next;
		}
		return helper.getNext();
	}


    private int musicalChairsLength(){
        int x = 1;
        SNode pointer = musicalChairs.getNext();
        while(pointer != musicalChairs){
            x++;
            pointer = pointer.getNext();
        }
        return x;
    }

    public void playMusicalChairs() {
        int len = musicalChairsLength();
        SNode linepointer = null;
        for (int i = len; i > 1; i--) {
            int remove = StdRandom.uniform(i);
            int count = 0;
            SNode ptr = musicalChairs;
            while (count != remove) { // iterate until you find the node before the removed student
                ptr = ptr.getNext();
                count++;
            }
            SNode elim = new SNode(ptr.getNext().getStudent(), null); // saved data of removed student
            if (studentsInLine == null) { //check if studentsInLine is populated, if populated add to front
                studentsInLine = elim;
                linepointer = studentsInLine;
            }
            else if(studentsInLine != null){
                linepointer.setNext(elim);
                linepointer = linepointer.getNext();
            }
            if (ptr.getNext().equals(musicalChairs)) musicalChairs = ptr; // edge case where last node is removed need to adjust musicalChair reference
                ptr.setNext(ptr.getNext().getNext()); // node break
        }
        studentsInLine = sortHeightList();
        seatStudents();
    }


    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    public void addLateStudent ( String firstName, String lastName, int height ) {
        if (studentsInLine != null){
            SNode ptr = studentsInLine;
            Student late = new Student(firstName, lastName, height);
            SNode lateStudent = new SNode(late, null);
            while (ptr.getNext() != null)
            ptr = ptr.getNext();
            ptr.setNext(lateStudent);
        } 
        else if(musicalChairs != null){
            Student late = new Student(firstName, lastName, height);	
            SNode lateStudent = new SNode(late, musicalChairs.getNext());
            musicalChairs.setNext(lateStudent);
            musicalChairs = lateStudent;
        } 
        else{
            Student late = new Student(firstName, lastName, height);	
            int k = 0;
            for ( int i = 0; i < seatingAvailability.length; i++){
                for ( int j = 0; j < seatingAvailability[0].length; j++){
                    if(seatingAvailability[i][j]){
                        studentsSitting[i][j] = late;
                        seatingAvailability[i][j] = false;
                        k = j;
                        break;
                    }
                }
                if ( studentsSitting[i][k] == late)
                break;
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
     * @param lastName the student's last name
     */
    public void deleteLeavingStudent ( String firstName, String lastName ) {
        if (studentsInLine != null) {
            SNode prev = new SNode(null, studentsInLine);
            SNode ptr = studentsInLine;
            while (ptr != null) {
                Student currStudent = ptr.getStudent();
                String currFirstName = currStudent.getFirstName().toLowerCase();
                String currLastName = currStudent.getLastName().toLowerCase();
                
                if (currFirstName.equals(firstName.toLowerCase()) && currLastName.equals(lastName.toLowerCase())) {
                    if (ptr.equals(studentsInLine)) studentsInLine = studentsInLine.getNext();
                    else prev.setNext(ptr.getNext());
                    return;
                }
                prev = ptr;
                ptr = ptr.getNext();
            }
        }
        
        else if (musicalChairs != null) {
            SNode prev = musicalChairs;
            SNode ptr = musicalChairs.getNext();
            int count = 0;
            while (count++ != musicalChairsLength()) {
                Student currStudent = ptr.getStudent();
                String currFirstName = currStudent.getFirstName().toLowerCase();
                String currLastName = currStudent.getLastName().toLowerCase();
                if (currFirstName.equals(firstName.toLowerCase()) && currLastName.equals(lastName.toLowerCase())) {
                    if (ptr.equals(musicalChairs)) musicalChairs = prev;
                    prev.setNext(ptr.getNext());
                    return;
                }
                prev = ptr;
                ptr = ptr.getNext();
            }
        }
        
        else {
            for (int i = 0; i < studentsSitting.length; i++) {
                for (int j = 0; j < studentsSitting[0].length; j++) {
                    if (studentsSitting[i][j] != null) {
                        Student currStudent = studentsSitting[i][j];
                        String currFirstName = currStudent.getFirstName().toLowerCase();
                        String currLastName = currStudent.getLastName().toLowerCase();

                        if (currFirstName.equals(firstName.toLowerCase()) && currLastName.equals(lastName.toLowerCase())) {
                            studentsSitting[i][j] = null;
                            seatingAvailability[i][j] = true;
                            return;
                        }
                    }
                }
            }
        }
        // WRITE YOUR CODE HERE

    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
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
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
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

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
