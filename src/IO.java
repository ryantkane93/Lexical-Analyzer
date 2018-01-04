import java.io.*;

public abstract class IO {

	/*The class defines a method of opening/closing the input and output streams, traversing the 
	 * characters of strings throughout an input file */
	public static BufferedReader inStream;
	public static PrintWriter outStream;

	//Global Variables
	public static int current; // the current input character on "inStream"
	public static char c; // used to convert the variable "a" to the char type whenever necessary

	/*Methods for Opening and Closing Input and Output Streams*/
	
	public static void setIO(String inFile, String outFile)

	// Opens the input file "inFile" and output file "outFile."
	// Sets the current input character "current" to the first character on the input stream.

	{
		try
		{
			inStream = new BufferedReader( new FileReader(inFile) ); //Set the BufferedReader to read from "inFile."
			outStream = new PrintWriter( new FileOutputStream(outFile) ); //Set PrintWriter to write to the output file "outFile."
			current = inStream.read(); //Set current to the first character in the file.
		}
		catch(FileNotFoundException e) //Output error message if input or output files cannot be found.
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void closeIO()
	{
		
		//Close both the input and output streams
		try
		{
			inStream.close();
			outStream.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	/*Methods for traversing input file */
	
	public static int getNextChar()

	// Returns the next character on the input stream. If the end of file is reached, -1 is returned.

	{
		try
		{
			return inStream.read();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	public static int getChar()

	// Returns the next non-whitespace character on the input stream.
	// Returns -1, end-of-stream, if getNextChar() detects the end of file character.

	{
		int i = getNextChar();
		while ( Character.isWhitespace((char) i) ) //Traverses the input file until a non-whitespace character is found.
			i = getNextChar();
		return i;
	}

	
	/*Methods for writing to output stream*/
	
	public static void display(String s)
	{
		//Use to write a string to a line in the output stream without moving the cursor
		//to the next line.
		outStream.print(s);
	}

	public static void displayln(String s)
	{ 
		//Use if you wish to consume an entire line in the output stream.
		outStream.println(s);
	}

} 


