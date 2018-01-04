/**
 
 Overview:
 
This class is a lexical analyzer that implements a DFA to recognize 16 token categories:
<id> --> <basic id> { "_" <letters and digits> }    With "_" being the underscore char 
<int> --> [+|-] {<digit>}+
<float> --> [+|-] ( {<digit>}+ "." {<digit>}  |  "." {<digit>}+ )
<floatE> --> (<float> | <int>) (e|E) [+|-] {<digit>}+
<add> --> +
<sub> --> -
<mul> --> *
<div> --> /
<lt> --> "<"
<le>--> "<="
<gt> --> ">"
<ge>--> ">="
<eq> --> "="
<LParen> --> "("
<RParen> --> ")"
<comma> --> "," 

The following 4 auxillary categories are used to define the token categories:
<letter>--> a | b | ... | z | A | B | ... | Z
<digit> --> 0 | 1 | ... | 9
<basic id> --> <letter> {<letter> | <digit>}
<letters and digits> --> {<letter> | <digit>}+

In addition, the lexical analyzer is able to detect the following 11 keywords, providing they are lowercase:
int
float
boolean
if
then
else
and
or
not
false
true
--------------------------------------------------------------------
Implementation:

DFA states are implemented as an Enum type called "State."

The DFA has the following 16 final states represented by enum-type literals:

state     token accepted

Id        identifiers
Int       integers
Float     floats without exponentiation part
FloatE    floats with exponentiation part
Add       +
Sub       -
Mul       *
Div       /
LParen    (
RParen    )
Eq		  =
Lt		  <
Le		  <=
Gt		  >
Ge		  >=
Comma	  ,

In addition to the prior 16 accept states, there are accept states for the following 11 keywords (note that each keyword
is first accepted as an identifier. Therefore, an analysis is performed on each identifier to check if
it is a keyword):

State				String Recognized

Keyword_int			"int"
Keyword_float		"float"
Keyword_boolean		"boolean"
Keyword_if			"if"
Keyword_then		"then"
Keyword_else		"else"
Keyword_and			"and"
Keyword_or			"or"
Keyword_not			"not"
Keyword_false		"false"
Keyword_true		"true"


The DFA also uses the following 5 non-final states:

State      String Recognized

Start      	the empty string
Period     	float parts ending with "."
Underscore	"_"
E          	float parts ending with E or e
EPlusMinus 	float parts ending with + or - in exponentiation part
------------------------------------------------------------------------------

The function "driver" operates the DFA. 
The function "nextState" returns the next state given the current state and the input character.

The function "checkIdentifier" examines an existing ID string and determines if it is one of the eleven
keywords. If a keyword is identified, the state is shifted to the keywords represented state.

NOTE: checkIdentifier was placed in the nextState method but could have also performed its tasked adequately
if it had been placed in isFinal() or even prior to the println statements in the main function. nextState
was chosen as the appropriate location to contain all major changes to the original source code in one area
and also to avoid having to conditionally check if every final state is an identifier each time the driver meets one.

To recognize a different token set, modify the following:

  enum type "State" and function "isFinal"
  function "nextState" 

The functions "driver", "getToken" remain the same.

**/
public class LexAnalyzer extends IO {
	
	//LexAnalyzer must extend IO in order to read and write from files
	
	public enum State //Use an Enum type to hold each state of the DFA 
   	{ 
  // non-final states     ordinal number

	Start,             // 0
	Period,            // 1
	E,                 // 2
	EPlusMinus,        // 3
	Underscore,		   // 4

  // final states
	//Start Category Tokens
	Id,                	// 5
	Int,               	// 6
	Float,             	// 7
	FloatE,            	// 8
	Add,              	// 9
	Sub,             	// 10
	Mul,             	// 11
	Div,               	// 12
	LParen,            	// 13
	RParen,            	// 14
	Eq,					// 15
	Lt,					// 16
	Le,					// 17
	Gt, 				// 18
	Ge,					// 19
	Comma,				// 20
	//End Category tokens
	
	//Start keywords
	Keyword_int,		// 21
	Keyword_float, 		// 22
	Keyword_boolean,	// 23
	Keyword_if,			// 24
	Keyword_then,		// 25
	Keyword_else,		// 26
	Keyword_and,		// 27
	Keyword_or,			// 28
	Keyword_not,		// 29
	Keyword_false,		// 30
	Keyword_true,		// 31
	//End keywords
	UNDEF;


	private boolean isFinal()
	{
		// By enumerating the non-final states first and then the final states,
				// test for a final state can be done by testing if the state's ordinal number
				// is greater than or equal to that of Id.
		//This is because all ordinal numbers greater than 0 when compared to the ID value represent a final state (token) in the DFA
		
		/* Note that using checkIdentifier(); would work here as well. It was decided to utilize the checkIdentifier()
		 * function inside of the nextState function instead to keep all changes to the original source code in one area.
		 * This should allow for easier debugging and implementation of future improvements. */
		return ( this.compareTo(State.Id) >= 0 );  
	}	
}
	
	public static String t; // holds an extracted token
	public static State state; // the current state of the FA
	// The following variables of "IO" class are used:

			//   static int current; the current input character
			//   static char c; used to convert the variable "current" to the char type whenever necessary

		private static int driver()

		// This is the driver of the FA. 
		// If a valid token is found, assigns it to "t" and returns 1.
		// If an invalid token is found, assigns it to "t" and returns 0.
		// If end-of-stream is reached without finding any non-whitespace character, returns -1.

		{
			State nextSt; // the next state of the FA

			t = "";
			state = State.Start;

			if ( Character.isWhitespace((char) current) )
				current = getChar(); // get the next non-whitespace character
			if ( current == -1 ) // end-of-stream is reached
				return -1;

			while ( current != -1 ) // do the body if "current" is not end-of-stream
			{
				c = (char) current;
				nextSt = nextState( state, c );
				if ( nextSt == State.UNDEF ) // The FA will halt.
				{
					if ( state.isFinal() )
					{
					
					return 1; // valid token extracted
				}
					
				else // "c" is an unexpected character
					{
						t = t+c;
						current = getNextChar();
						return 0; // invalid token found
					}
				}
				else // The FA will go on.
				{
					state = nextSt;
					t = t+c;
					current = getNextChar();
				}
			}

			// end-of-stream is reached while a token is being extracted

			if ( state.isFinal() )
				return 1; // valid token extracted
			else
				return 0; // invalid token found
		} // end driver
		
		public static void getToken()
		{
		// Extract the next token using the driver of the FA.
		// If an invalid token is found, issue an error message.
			int i = driver();
			if ( i == 0 )
				displayln(t + " : Lexical Error, invalid token");
		}

		private static State nextState(State s, char c)

		// Returns the next state of the FA given the current state and input char;
		// if the next state is undefined, UNDEF is returned.

		{
			switch( state )
			{
			case Start:
				if ( Character.isLetter(c) )
					return State.Id;
				else if ( Character.isDigit(c) )
					return State.Int;
				else if (c=='.')
					return State.Period;
				else if ( c == '+' )
					return State.Add;
				else if ( c == '-' )
					return State.Sub;
				else if ( c == '*' )
					return State.Mul;
				else if ( c == '/' )
					return State.Div;
				else if ( c == '(' )
					return State.LParen;
				else if ( c == ')' )
					return State.RParen;
				else if (c == '=')
					return State.Eq;
				else if (c=='<')
					return State.Lt;
				else if (c == '>')
					return State.Gt;
				else if (c== ',')
					return State.Comma;
				else
					return State.UNDEF;
			case Id:
				if ( Character.isLetterOrDigit(c) )
					return State.Id;
				else if(c == '_')
					return State.Underscore;
				else
					{
					//If there is an undefined character after an ID, it is safe to assume that no other characters
					//will be added to an ID. We can now check to see if the identifier is a keyword and move
					//to the correct state if needed.
					checkIdentifier(); //Check the identifier to see if it is a keyword prior to outputting it.
					return State.UNDEF;
					}
			case Int:
				if ( Character.isDigit(c) )
					return State.Int;
				else if ( c == '.' )
					return State.Float;
				else if (c=='e' || c =='E')
						return State.E;
				else
					return State.UNDEF;
			case Period:
				if ( Character.isDigit(c) )
					return State.Float;
				else
					return State.UNDEF;
			case Float:
				if ( Character.isDigit(c) )
					return State.Float;
				else if ( c == 'e' || c == 'E' )
					return State.E;
				else
					return State.UNDEF;
			case E:
				if (Character.isDigit(c))
					return State.FloatE;
				else if ( c == '+' || c == '-' )
					return State.EPlusMinus;
				else
					return State.UNDEF;
			case EPlusMinus:
				if (Character.isDigit(c))
					return State.FloatE;
				else
					return State.UNDEF;
			case FloatE:
				if (Character.isDigit(c))
					return State.FloatE;
				else
					return State.UNDEF;
			case Underscore:
				if(Character.isLetterOrDigit(c))
					return State.Id;
				else
					return State.UNDEF;
			case Add:
				if(c== '.')
					return State.Period;
				else if (Character.isDigit(c))
					return State.Int;
				else
					return State.UNDEF;
			case Sub:
				if (c=='.')
					return State.Period;
				else if (Character.isDigit(c))
					return State.Int;
				else 
					return State.UNDEF;
			case Lt:
				if (c== '=')
					return State.Le;
				else
					return State.UNDEF;
			case Gt:
				if (c== '=')
					return State.Ge;
				else
					return State.UNDEF;
			default:
				return State.UNDEF;
			}
		} // end nextState
		
		private static void checkIdentifier()
		{
			//Prior to declaring a token an indentifier, we must check and see if it matches
			//Any of the eleven keywords using a set of conditional statements
				
					if(t.equals("int"))
					{
						state = State.Keyword_int;
					}
					else if(t.equals("if"))
					{
						state = State.Keyword_if;
					}
					else if (t.equals("float"))
					{
						state = State.Keyword_float;
					}
					else if (t.equals("boolean"))
					{
						state = State.Keyword_boolean;
					}
					else if (t.equals("then"))
					{
						state = State.Keyword_then;
					}
					else if (t.equals("else"))
					{
						state = State.Keyword_else;
					}
					else if (t.equals("and"))
					{
						state = State.Keyword_and;
					}
					else if (t.equals("or"))
					{
						state = State.Keyword_or;
					}
					else if (t.equals("not"))
					{
						state = State.Keyword_not;
					}
					else if (t.equals("false"))
					{
						state = State.Keyword_false;
					}
					else if (t.equals("true"))
					{
						state = State.Keyword_true;
					}
					else //The token is actually an identifier and not a keyword
					{
						state = State.Id; //This is redundant as the state would have never changed if it was truly an ID, the else statement was included for good practice.
					}
				} //end checkIdentifier
		
		public static void main(String argv[])

		{		
			// argv[0]: input file (testInput.txt) containing source code using tokens defined above
			// argv[1]: output file (testOutput.txt) displaying a list of the tokens 

			setIO( argv[0], argv[1] );
			
			int i;

			while ( current != -1 ) // while "current" is not end-of-stream
			{
				i = driver(); // extract the next token
				if ( i == 1 ) //Print the resulting token category if the DFA is in an accept state.
					displayln( t+"   : "+state.toString() );
				else if ( i == 0 ) //Print an error message if an invalid token is detected.
					displayln( t+" : Lexical Error, invalid token");
			} 

			closeIO();
		}

}
