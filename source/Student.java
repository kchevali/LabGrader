//Path Name: TAelliott1/Sales.java

import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.InputMismatchException;
import java.util.ResourceBundle;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.net.*;
import java.text.*;
import java.math.*;

public class Student  { 

	// Student...
	public static void main(String[] args) {

		// input.
		Scanner scan = new Scanner(System.in);
		int option = 0;
		do {
			
			System.out.println(""
					+ "***File Operations Menu***\n" + 
					"1. Enter Sales Figures (in File)-must be done first\n" + 
					"2. Display Sales Figures\n" + 
					"3. Display Total Sales\n" + 
					"4. Display Average Sales\n" + 
					"5. Display Largest Sale\n" + 
					"6. Exit Program\n" + 
					"Enter Choice");
			option = scan.nextInt();
			switch(option) {
			case 1:
				
				// sales figures..
				System.out.print("How many weeks of sales figures do you have to enter? ");
				int salesCount = scan.nextInt();
				// Openning file.
				try {
					PrintWriter writer = new PrintWriter(new FileWriter("data.txt"));
					for(int i = 0; i < salesCount; i++) {
						
						System.out.print("Enter week "+(i+1)+" sales figure: ");
						double saleFigure = scan.nextDouble();
						writer.println(saleFigure);
						
					}
					writer.close();
				} catch (IOException e) {
					System.out.println("Error: "+e.toString());
				}
				
				break;
			case 2:
				
				try {
					Scanner reader = new Scanner(new File("data.txt"));
					while(reader.hasNextDouble()) {
						System.out.println(reader.nextDouble());
					}
					reader.close();
				} catch (FileNotFoundException e) {
					System.out.println("Error: "+e.toString());
				}
				
				break;
			case 3:
				
				double sales = 0;
				try {
					Scanner reader = new Scanner(new File("data.txt"));
					while(reader.hasNextDouble()) {
						sales += reader.nextDouble();
					}
					reader.close();
				} catch (FileNotFoundException e) {
					System.out.println("Error: "+e.toString());
				}
				System.out.println("Total Sales "+String.format("%,.2f", sales));
				
				break;
			case 4:
				
				int count = 0;
				sales = 0;
				try {
					Scanner reader = new Scanner(new File("data.txt"));
					while(reader.hasNextDouble()) {
						sales += reader.nextDouble();
						count++;
					}
					reader.close();
				} catch (FileNotFoundException e) {
					System.out.println("Error: "+e.toString());
				}
				System.out.println("Average Sales "+String.format("%,.2f", sales/count));
				
				
				break;
			case 5:
				
				double largest = 0;
				try {
					Scanner reader = new Scanner(new File("data.txt"));
					while(reader.hasNextDouble()) {
						double sale = reader.nextDouble();
						if(sale > largest) {
							largest = sale;
						}
					}
					reader.close();
				} catch (FileNotFoundException e) {
					System.out.println("Error: "+e.toString());
				}
				System.out.println("Largest Sale "+String.format("%,.2f", largest));
				
				break;
			case 6:
				System.out.println("Thanks for using the program");
				break;
			default:
				System.out.println("Invalid selection, select 1-4");	
			}
			System.out.println();
			
		} while(option != 6);
		scan.close();
		
	}

}

