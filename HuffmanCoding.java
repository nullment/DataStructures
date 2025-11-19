package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);
        sortedCharFreqList = new ArrayList<CharFreq>();
        double asciiCountSize = 0;//size of array
        int[] asciiCount = new int[128]; //keep track of the number of occurrences of each character in an array of size 128
        
        while(StdIn.hasNextChar()){ //while there is a next character loop keeps going
            char nextChar = StdIn.readChar();//reads char
            asciiCountSize++;
            asciiCount[nextChar]++;//inputs file into array and loops
        }
        for(int i = 0; i < asciiCount.length; i++){
                if(asciiCount[i] != 0){
                double prob = asciiCount[i];
                char character = (char) i;
                sortedCharFreqList.add(new CharFreq(character, prob/asciiCountSize));
            }
        }
        if(sortedCharFreqList.size() == 1){//If you are already at ASCII value 127, wrap around to ASCII 0. Doesnt work if there is only 1 distinct character.
            if( (int)sortedCharFreqList.get(0).getCharacter() == 127){//if the character is 127, first make character into int then compare 
                sortedCharFreqList.add(new CharFreq((char)0,0));
            }
            else{ 
                char temp = sortedCharFreqList.get(0).getCharacter();
                int temp2 = (int) temp+1;
                sortedCharFreqList.add(new CharFreq((char)temp2,0));
            }
        }
        Collections.sort(sortedCharFreqList);
	/* Your code goes here */
    }


    // private TreeNode checkQueue(Queue<TreeNode> source, Queue<TreeNode> target){
    //     TreeNode left = new TreeNode();
    //     TreeNode right = new TreeNode();

    //     while ( target.size() != 1 || !source.isEmpty()){
    //         if(target.isEmpty()){
    //             if (!source.isEmpty()){
    //             left = source.dequeue();
    //             }
    //         }
    //         else if(source.peek().getData().getProbOcc() <= target.peek().getData().getProbOcc()){
    //             left = source.dequeue();
    //         }
    //         else{
    //             left = target.dequeue();
    //         }

    //         if(source.isEmpty()){//source empty
    //             if (!target.isEmpty()){//target not empty
    //             right = target.dequeue();//right is old target node
    //             }
    //         }
    //         else if(source.peek().getData().getProbOcc() <= target.peek().getData().getProbOcc()){ // 
    //             right = source.dequeue();
    //         }
    //         else{
    //             right = target.dequeue();
    //         }
    //         System.out.println(left.getData().getProbOcc());
    //         System.out.println(right.getData().getProbOcc());

    //         CharFreq sumProb = new CharFreq(null, (right.getData().getProbOcc() + left.getData().getProbOcc()));
    //         TreeNode parentNode = new TreeNode(sumProb, left, right);
    //         target.enqueue(parentNode);
    //     }
    //     return target.dequeue();
    // }

            // if( source.peek().getData().getProbOcc() < target.peek().getData().getProbOcc()){
            //     target.peek().setLeft(source.dequeue());
            // }
            // else{
            //     target.peek().setRight(source.dequeue());
            // }
            // if (source.isEmpty()){
            //     target.dequeue();
            // }
            // else if(target.isEmpty()){
            //     Queue<TreeNode> treeSource = new TreeNode(source.dequeue(), null, null);
            // }
            // else if( treeSource.peak().getData().getProb0cc() >= target.getData().getProb0cc()){
            //     target.dequeue();
            // }
            // else {
            //     treeSource.dequeue();
            // }



    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {
        Queue<TreeNode> source = new Queue<TreeNode>(); // source queue initialized
        Queue<TreeNode> target = new Queue<TreeNode>(); // target queue initialized

        // Fill Queue Source with sortedCharFreqList
        for ( int i = 0; i < sortedCharFreqList.size(); i++ ){//for loop to take data from sortedCharFreqList and input it into source queue
            source.enqueue(new TreeNode(sortedCharFreqList.get(i), null, null));
        }
        // Rules: target size has to be 1 node which will be the probability occurance of 1.0
        // Do this loop until source is empty
        //while ( target.size() > 1 || source.isEmpty() != true){
            TreeNode left = new TreeNode();
            TreeNode right = new TreeNode();
            while ( target.size() != 1 || !source.isEmpty()){
                if(target.isEmpty()){
                    left = source.dequeue();
                }
                else if (source.isEmpty()){
                    left = target.dequeue();
                }
                else if(source.peek().getData().getProbOcc() <= target.peek().getData().getProbOcc()){
                    left = source.dequeue();
                }
                else{
                    left = target.dequeue();
                }
    
                if(target.isEmpty()){
                    right = source.dequeue();
                }
                else if (source.isEmpty()){
                    right = target.dequeue();
                }
                else if(source.peek().getData().getProbOcc() <= target.peek().getData().getProbOcc()){
                    right = source.dequeue();
                }
                else{
                    right = target.dequeue();
                }
                System.out.println(left.getData().getProbOcc());
                System.out.println(right.getData().getProbOcc());

                CharFreq sumProb = new CharFreq(null, (right.getData().getProbOcc() + left.getData().getProbOcc()));
                TreeNode parentNode = new TreeNode(sumProb, left, right);
                target.enqueue(parentNode);
            }
        huffmanRoot = target.dequeue();
        
        /* Your code goes here */
    }

    private void printInorder(String[] stringArray, String bitString, TreeNode node)
    {
        if (node == null)
            return;
 
        /* first recur on left child */
        printInorder(stringArray, bitString + "0", node.getLeft());
        if(node.getData().getCharacter() != null){
            stringArray[(int)node.getData().getCharacter()] = bitString;        
        }
 
        /* now recur on right child */
        printInorder(stringArray, bitString + "1", node.getRight());
    }
    // private String[] find(String[] stringArray, String bitString, TreeNode temp){
    //     if( temp != null){
    //         find(stringArray, "0", temp.getLeft());
    //         find(stringArray, "1", temp.getRight());
    //         if(temp.getRight() == null){
    //             if(temp.getLeft() == null){
    //                 int character = (int)temp.getData().getCharacter();
    //                 stringArray[character] = bitString;
    //             }
    //         }
    //         return stringArray;
    //     } 
    //     else{
    //         bitString = bitString.substring(0, bitString.length());
    //         return null;
    //     }
    // }
    

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
        String[] stringArray = new String[128];
        printInorder( stringArray, "", huffmanRoot); 
        encodings = stringArray;
	/* Your code goes here */
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
        String blankString = "";
        while(StdIn.hasNextChar()){
            char temp = StdIn.readChar();
            blankString += encodings[(int)temp];
        }
        writeBitString(encodedFile, blankString);


	/* Your code goes here */
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);
        String encode = readBitString(encodedFile);
        String blankString = "";
        while(encode.length() != 0){
            TreeNode ptr = huffmanRoot;
            while(ptr.getData().getCharacter() == null){
                if(encode.charAt(0) == '0'){  
                    ptr = ptr.getLeft();
                    encode = encode.substring(1);  
                }
                else{
                    ptr = ptr.getRight();
                    encode = encode.substring(1);  
                }
            }
            blankString += ptr.getData().getCharacter();
        }
        StdOut.print(blankString);
	/* Your code goes here */
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
