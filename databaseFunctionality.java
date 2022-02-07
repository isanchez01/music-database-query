import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;


public class databaseFunctionality {

	
	/* The userInputFrame function serves as a master class that handles all of the actions
	 * that users could do with the spreadsheet. 
	 * Rather than create an Object with custon-made attributes, I thought to do the same 
	 * under one function.
	 * 
	 * userInputFrame takes in three parameters: a string, an XSSFSheet variable, and a HashMap
	 * 
	 * The string parameter is called desiredAction and stores the action that the user
	 * wants to do.
	 * 
	 * The XSSFSheet parameter is called sheet2 and allows the function to iterate through
	 * each row in the spreadsheet and print out the information that they hold
	 * 
	 * The HashMap parameter is called artists and contains each artist and their albums
	 * in a key-value format. The HashMap is used here to search for an artist that a user
	 * has typed in, and print out all the albums that they've released. If an artist hasn't
	 * been included in the spreadsheet, or they aren't a valid artist, the program will
	 * tell the user.
	 */
	public static void userInputFrame(String desiredAction, XSSFSheet sheet2, HashMap artists) {
		// If the user wants to view the entire spreadsheet, i.e typed in "view sheet"
		if (desiredAction.equals("view sheet")) {
			Iterator<Row> rowIterator = sheet2.iterator();
			System.out.println("");
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					System.out.format("%s %s %s",row.getCell(0),row.getCell(1), row.getCell(2), row.getCell(3), row.getCell(4));
					System.out.println("");
					System.out.println("");
				}
				else {
					System.out.format("%s %s %s",row.getCell(0),row.getCell(1), row.getCell(2), row.getCell(3), row.getCell(4));
					System.out.println("");
				}
			}
			System.out.println("");
		}
		// If the user wants to lookup an artist's discography (typed in "lookup artist")
		else if (desiredAction.equals("lookup artist")) {
			String requestedArtist = "";
			try {
				// Prompts the user to type in the artist that they want to lookup
				System.out.print("Type in the name of an artist (with each first letter capitalized 'Like This'): ");
				Scanner myScanner2 = new Scanner(System.in);
				requestedArtist = myScanner2.nextLine();
				ArrayList<String> discography = (ArrayList) artists.get(requestedArtist);
				if (discography.size() > 1) {
					System.out.println("");
					System.out.println(requestedArtist + "'s discography");
					System.out.println("");
					for (String album : discography) {
						System.out.println(album);
					}
				}
			}
			catch(Exception NullPointerException) {
				System.out.println("");
				System.out.println("'"+ requestedArtist + "' either isn't a valid artist, or is an artist that "
						+ "has yet to be inserted into the music database spreadsheet");
				System.out.println("");
			}
		}
		else {
			System.out.println("That isn't a valid action, silly!");
		}
	}
	
	
	/* The main function, aka the meat of the program. 
	 * It extracts the spreedsheet from the "MusicDatabase.xlsx" and iterates through each
	 * row and column.
	 * After eliminating the first row that contains unloved and unwanted data (such as the titles),
	 * the program goes through each column and stores the name of the musical artist and
	 * information about the albums that they have released.
	 * 
	 * Once again, this is a work in progress. 
	 * It is also my first project, so don't laugh.
	 * Constructive criticism is not only something that I'll appreciate, but also something
	 * I'm looking for.
	 * However like a cartoon character, whose name I can't think of at this time, once said,
	 * if you don't have anything nice to say, don't say anything at all.
	 * To adhere to the above phrase, it's simple: criticism isn't mean but useless yelling is
	 * 
	 * Don't send me a paragraph full of obscenities that tells me how worthless I am as a 
	 * programmer and human being just because I didn't write a for loop the way you like
	 *
	 * Simply tell me politely about what I could improve on.
	 * I hope you have a beautiful day after enjoying my project :)
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		// Extracting the spreadsheet from MusicDatabase.xlsx
		String fileName = "/Desktop/MusicDatabase.xlsx";
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(System.getProperty("user.home"), fileName)));
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		/* Hashmap will store discography information, and the two Strings will serve as the
		 * key-value pairs
		 */
		HashMap<String, ArrayList<String>> artists = new HashMap<>();
		String artistName = " ", artistInfo = " ";
		
	
		// Iterating through each row in the spreadsheet
		for (Row row : sheet) {
		StringBuilder sb = new StringBuilder();
		//Condition that eliminates unloved title data
			if (row.getRowNum() != 0) {
				// Iterating through each column and constructing the key (artist's name)
				// and value (album information) pairs
				for (Cell column : row) {
					artistName = row.getCell(0).toString();
					// Constructs album information by appending column data to a StringBuilder
					// The condition prevents the StringBuilder from appending the first column
					// which houses the artists's name
					if (column.getColumnIndex() != 0) {
						sb.append(column);
						sb.append(" ");
					}
					// Converts now constructed StringBuilder into a String,
					// aka something a human being can read
					artistInfo = sb.toString();
				}
				/* Condition that checks whether an artist (key) has an album (value) stored
				 * to their name.
				 * 
				 * If not, then create a new ArrayList, store it to the artist (key), and
				 * populate it to the album being looked at
				 */
				
				if (!artists.containsKey(artistName)) {
					artists.put(artistName, new ArrayList<String>());
					artists.get(artistName).add(artistInfo);
					
				}
				// The above condition failed and the artist (key) will have another album (value)
				// stored to their name
				else {
					artists.get(artistName).add(artistInfo);
				}
				
			}
		}
		
		// User input that prompts the user to type in what they want to do
		Scanner myScanner = new Scanner(System.in);
		System.out.println("Hello, welcome to my music database program");
		System.out.println("This is a mini project that I've decided to launch,"
							+ " mainly for fun");
		System.out.println("I came up with the idea of a program that manages a "
							+ "database based on my love for music");
		System.out.println();
		System.out.println("So anyways, here you could manage a spreadsheet containing "
				+ "the discographies of various musical artists");
		System.out.println("Although basic, the program has a few actions you could "
				+ "perform ");
		System.out.println();
		System.out.println("To view the entire spreadsheet, type in VIEW SHEET "
				+ "(and keep in mind that the program is not case-sensitive\n");
		System.out.println("To lookup the discography of a specific artist, "
				+ "type in the name of your artist\n");
		System.out.println("To stop the program, type in STOP (not case-sensitive)\n");
		System.out.println("Functionalities will increase with time\n");
		System.out.println("With that in mind, lets get started!\n");
		System.out.println("What would you like to do? ");
		
		
		String requestedFunction = myScanner.nextLine();
		requestedFunction = requestedFunction.toLowerCase();
		
		// If the user wants to stop the program right way (typed in "stop" right away)
		if (requestedFunction.equals("stop") ) {
			return;
		}
		// Or else if they want to run the program
		else {
			do {
				// Prompts the user to type in what they want to do
				userInputFrame(requestedFunction,sheet, artists);
				System.out.println("What else do you want to do? ");
				requestedFunction = myScanner.nextLine();
				requestedFunction = requestedFunction.toLowerCase();
			} while (!requestedFunction.equals("stop"));
		}
		
		
		
	}
}
		
		


