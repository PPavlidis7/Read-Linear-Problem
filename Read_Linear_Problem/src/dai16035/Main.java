/*
 This program reads a linear problem from a .txt file and tries to find
 any syntax mistakes 
 The program read from an given input file and the values c,A,b,Equin and the type of problem (Min or Max) 
 are written to a LP-2-Results.txt output ile
 This program is written by Pavlidis Pavlos for a task at Linear and Netwορκ Programming course, May 2017

*/

package dai16035;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		ArrayList<Character> charList = new ArrayList<Character>(); /*
																	 * The array
																	 * where I
																	 * save my
																	 * input
																	 * file
																	 */
		int counter61 = 0; /* a counter for '=' */
		boolean noterror = true; /*
									 * a flag which become false if something
									 * goes wrong with my input file
									 */
		int[] MinMAx = new int[1]; /*
									 * A table where I save-1 if I have a
									 * minimization problem or +1 if I have a
									 * maximization problem
									 */
		int n = 0;
		ArrayList<Integer> c = new ArrayList<>();
		ArrayList<Integer> b = new ArrayList<>();
		ArrayList<Integer> Eqin = new ArrayList<>();
		ArrayList<Integer> coeffiecients;
		ArrayList<Integer> indexes;
		/* A 2 dimension array where I save the st' s coefficients */
		ArrayList<ArrayList<Integer>> A = new ArrayList<ArrayList<Integer>>();

		/*
		 * try catch block. Read characters from file and add them in ArrayList
		 * charList
		 */
		try {
			int ca;
			BufferedReader reader = new BufferedReader(new FileReader("ok.txt"));

			while ((ca = reader.read()) != -1) {

				char character = (char) ca;

				charList.add(character);
			}
			reader.close();
		} catch (IOException e) {
		}
		/*
		 * Search if there is the word 'end' or 'END' at the end of the file and
		 * delete it
		 */
		for (int i = charList.size() - 1; i > 0; i--) {
			if (charList.get(i) == 'D' || charList.get(i) == 'd')
				if (charList.get(i - 1) == 'N' || charList.get(i - 1) == 'n')
					if (charList.get(i - 2) == 'E' || charList.get(i - 2) == 'e') {
						charList.remove(i);
						charList.remove(i - 1);
						charList.remove(i - 2);
						break;
					}

		}

		/*
		 * put '/' wherever my z equation or my s.t. line are finished and check
		 * if there 're missing the right value of my equation
		 */
		for (int i = 0; i < charList.size(); i++) {
			// make lines to my array
			if (charList.get(i) == 'S' || charList.get(i) == 's') {
				charList.add(i, '/');
				i += 2;
				continue;
			}
			if (charList.get(i) == 'Z' || charList.get(i) == 'z')
				counter61++;
			if (charList.get(i) == '=') {
				if (counter61 == 1) {
					counter61 = 0;
					continue;
				} else {

					int t = i + 1;
					// remove space , 32 = Space
					while (charList.get(t) == ' ' || charList.get(t) == 10 || charList.get(t) == 13) {
						charList.remove(t);
					}
					while (charList.get(t) == '+' || charList.get(t) == '-') {
						t++;
					}
					if (charList.get(t) >= 48 && charList.get(t) <= 57) {

						while (charList.get(t) >= 48 && charList.get(t) <= 57) {
							t++;
						}
						i = t;
					}
					if (charList.get(t) != 'x' && charList.get(t) != 'X') {
						charList.add(t, '/');
					} else {
						noterror = false;
						System.out.println("Syntax error . I can't find the right part of one equation");
						break;
					}
				}
			}
		}

		// remove Spaces, CR and LF and '*'
		for (int i = 0; i < charList.size(); i++) {

			if (charList.get(i) == 32 || charList.get(i) == 10 || charList.get(i) == 13 || charList.get(i) == 42) {
				charList.remove(i);
				i--;
			}
		}

		int Tindex = 0; /* Position of 'T' or 't' in my array */
		if (noterror) {
			/*
			 * S.T. can be written like s.t. or S.T. or st o ST in my file. So
			 * if I find the 't' or 'T' i can start my for-loop from there to
			 * find values for my tables
			 */
			// Find the 't' or 'T' dot
			for (Tindex = 0; Tindex < charList.size(); Tindex++) {
				if (charList.get(Tindex) == 't' || charList.get(Tindex) == 'T')
					break;
			}
		}
		/*
		 * Search if there is any syntax error. Check if there 're missing any
		 * +,-,<=,>= or =
		 */

		if (noterror) {

			for (int i = Tindex + 1; i < charList.size(); i++) {
				if (charList.get(i) == 'x' || charList.get(i) == 'X') {
					int t = i + 1;

					if (charList.get(t) >= 48 && charList.get(t) <= 57) {
						while (charList.get(t) >= 48 && charList.get(t) <= 57) {
							t++;
						}
						i = t;
					}
					if (charList.get(i) == 'x' || charList.get(i) == 'X') {
						noterror = false;
						System.out.println(
								"Syntax error. There is missing at least one '+' or '-' or '<=' or '=' or '=> ");
						break;
					}
					if (charList.get(i) == '>' || charList.get(i) == '<') {
						if (charList.get(t + 1) != '=') {
							noterror = false;
							System.out.println("Syntax error. There is missing at least one '=' ");
							break;
						}
					}

				}
			}
		}

		if (noterror) {
			/*
			 * the first 3 charList's items should be the word min or max . If
			 * it doesn't i have an error and my my program stops working
			 */
			if (!(charList.get(0) == 'm' || charList.get(0) == 'M'))
				noterror = false;
			if (!(charList.get(1) == 'a' || charList.get(1) == 'A' || charList.get(1) == 'i' || charList.get(1) == 'I'))
				noterror = false;
			if (!(charList.get(2) == 'x' || charList.get(2) == 'X' || charList.get(2) == 'n' || charList.get(2) == 'N'))
				noterror = false;
			if (noterror) {

				if (charList.get(2) == 'x' || charList.get(2) == 'X')
					MinMAx[0] = 1;
				else
					MinMAx[0] = -1;
			} else
				System.out.println("Syntax error. I can't find if your problem is maximization or minimization");
		}

		// Find the C table
		if (noterror) {
			/*
			 * the third item from my arraylist is X or x or N or n, the 4th
			 * could be Z or z, and the 5th could be '='. This for - loop stop
			 * when it fines the end of first line
			 */
			int minus = 0;
			boolean coef = true;
			coeffiecients = new ArrayList<>();
			indexes = new ArrayList<>();
			for (int i = 3; i < charList.size(); i++) {
				/* The end of line */
				if (charList.get(i) == '/')
					break;
				/* Save the sign */
				if (charList.get(i) == '-') {
					minus = -1;
					i++;
				} else if (charList.get(i) == '+') {
					minus = 1;
					i++;
				}
				/* If I found a number */
				if (charList.get(i) >= 48 && charList.get(i) <= 57) {

					int[] helper = new int[5];
					int sum = 0;
					int k = i;
					int j = 0;
					int counter = -1;
					/*
					 * Find all the digits from this number and save them to the
					 * table helper. Count how many they are and then find which
					 * number these digits shape
					 */
					while (charList.get(k) >= 48 && charList.get(k) <= 57) {
						helper[j] = Character.getNumericValue(charList.get(k));
						j++;
						k++;
						counter++;
					}
					for (int a = 0; a < j; a++) {
						sum += helper[a] * Math.pow(10, (counter - a));
					}
					/*
					 * Check if this number is index or coefficient and store it
					 * at the appropriate ArrayList
					 */
					if (coef) {
						i = k;
						coef = false;
						if (charList.get(i) == 'x' || charList.get(i) == 'X') {
							if (minus == -1) {
								sum = sum * (-1);
								minus = 0;
							}
							coeffiecients.add(sum);
							n++;
						}
					} else {
						coef = true;
						indexes.add(sum);
					}
				}
				/*
				 * If i don't find a number but a 'X' or 'x' and I expect a
				 * coefficient; then that's mine that I'll save the value 1 or
				 * -1
				 */
				else if (charList.get(i) == 'x' || charList.get(i) == 'X') {
					if (coef) {
						if (minus == -1) {
							coeffiecients.add(-1);
							coef = false;
							minus = 0;
							n++;
						} else {
							coeffiecients.add(1);
							coef = false;
							n++;
						}
					}
				}
			}

			for (int i = 0; i < coeffiecients.size(); i++) {
				c.add(coeffiecients.get(i));
			}
		}

		/*
		 * Find the A,b and Eqin table
		 */
		int lines = 0; /* Counter for my lines of my A ArrayList */
		if (noterror) {

			/*
			 * I start the for-loop from Tindex to create the A array and I stop
			 * when I cross the whole array
			 */
			int ind = Tindex;
			while (ind < charList.size()) {

				int minus = 0;
				boolean coef = true;
				A.add(new ArrayList<Integer>());
				coeffiecients = new ArrayList<>();
				indexes = new ArrayList<>();
				for (int i = ind; i < charList.size(); i++) {
					/* The end of line */
					if (charList.get(i) == '/') {
						ind = i + 1;
						break;
					}
					/* Save the sign */
					if (charList.get(i) == '-') {
						minus = -1;
						i++;
					} else if (charList.get(i) == '+') {
						minus = 1;
						i++;
					}
					/* If I found a number */
					if (charList.get(i) >= 48 && charList.get(i) <= 57) {

						int[] helper = new int[5];
						int sum = 0;
						int k = i;
						int j = 0;
						int counter = -1;
						/*
						 * Find all the digits from this number and save them to
						 * the table helper. Count how many they are and then
						 * find which number these digits shape
						 */
						while (charList.get(k) >= 48 && charList.get(k) <= 57) {
							helper[j] = Character.getNumericValue(charList.get(k));
							j++;
							k++;
							counter++;
						}
						for (int a = 0; a < j; a++) {
							sum += helper[a] * Math.pow(10, (counter - a));
						}
						/*
						 * Check if this number is index or coefficient and
						 * store it at the appropriate ArrayList
						 */
						if (coef) {
							i = k;
							coef = false;
							if (charList.get(i) == 'x' || charList.get(i) == 'X') {
								if (minus == -1) {
									sum = sum * (-1);
									minus = 0;
								}
								coeffiecients.add(sum);
							}
						} else {
							coef = true;
							indexes.add(sum);
						}
					}
					/*
					 * If i don't find a number but a 'X' or 'x' and I expect a
					 * coefficient; then that's mine that I'll save the value 1
					 * or -1
					 */
					else if (charList.get(i) == 'x' || charList.get(i) == 'X') {
						if (coef) {
							if (minus == -1) {
								coeffiecients.add(-1);
								coef = false;
								minus = 0;
							} else {
								coeffiecients.add(1);
								coef = false;
							}
						}
					}
					/*
					 * Update my Eqin array if i find < ,= or >
					 */
					if (charList.get(i) == '<') {
						Eqin.add(-1);
						/*
						 * Update the i to point to a '-' or '+' or a number
						 */
						while (charList.get(i) == '=' || charList.get(i) == '>' || charList.get(i) == '<') {
							i++;
						}
						/* Save the sign you can find in this line */
						if (charList.get(i) == '-') {
							minus = -1;
							i++;
						} else if (charList.get(i) == '+') {
							minus = 1;
							i++;
						}
						/*
						 * Increase i until I find a number
						 */
						while (charList.get(i) == '-' || charList.get(i) == '+') {
							i++;
						}
						while (charList.get(i) < 48 || charList.get(i) > 57) {
							i++;
						}
						/*
						 * If i find a number then find all the digits from this
						 * number and save them to the table helper. Count how
						 * many they are and then find which number these digits
						 * shape. Then save it to b array
						 */
						if (charList.get(i) >= 48 && charList.get(i) <= 57) {

							int[] helper = new int[5];
							int sum = 0;
							int k = i;
							int j = 0;
							int counter = -1;
							while (charList.get(k) >= 48 && charList.get(k) <= 57) {
								helper[j] = Character.getNumericValue(charList.get(k));
								j++;
								k++;
								counter++;
							}
							for (int a = 0; a < j; a++) {
								sum += helper[a] * Math.pow(10, (counter - a));
							}
							if (minus == -1) {
								sum = sum * (-1);
								minus = 0;
							}
							b.add(sum);
							i = k - 1;
						}
					} else if (charList.get(i) == '>') {
						Eqin.add(1);
						/*
						 * Update the i to point to a '-' or '+' or a number
						 */
						while (charList.get(i) == '=' || charList.get(i) == '>' || charList.get(i) == '<') {
							i++;
						}
						/* Save the sign you can find in this line */
						if (charList.get(i) == '-') {
							minus = -1;
							i++;
						} else if (charList.get(i) == '+') {
							minus = 1;
							i++;
						}
						/*
						 * Increase i until I find a number
						 */
						while (charList.get(i) == '-' || charList.get(i) == '+') {
							i++;
						}
						while (charList.get(i) < 48 || charList.get(i) > 57) {
							i++;
						}
						/*
						 * If i find a number then find all the digits from this
						 * number and save them to the table helper. Count how
						 * many they are and then find which number these digits
						 * shape. Then save it to b array
						 */
						if (charList.get(i) >= 48 && charList.get(i) <= 57) {

							int[] helper = new int[5];
							int sum = 0;
							int k = i;
							int j = 0;
							int counter = -1;
							while (charList.get(k) >= 48 && charList.get(k) <= 57) {
								helper[j] = Character.getNumericValue(charList.get(k));
								j++;
								k++;
								counter++;
							}
							for (int a = 0; a < j; a++) {
								sum += helper[a] * Math.pow(10, (counter - a));
							}
							if (minus == -1) {
								sum = sum * (-1);
								minus = 0;
							}
							b.add(sum);
							i = k - 1;
						}
					} else if (charList.get(i) == '=') {
						Eqin.add(0);
						/*
						 * Update the i to point to a '-' or '+' or a number
						 */
						while (charList.get(i) == '=' || charList.get(i) == '>' || charList.get(i) == '<') {
							i++;
						}
						/* Save the sign you can find in this line */
						if (charList.get(i) == '-') {
							minus = -1;
							i++;
						} else if (charList.get(i) == '+') {
							minus = 1;
							i++;
						}
						/*
						 * Increase i until I find a number
						 */
						while (charList.get(i) == '-' || charList.get(i) == '+') {
							i++;
						}
						while (charList.get(i) < 48 || charList.get(i) > 57) {
							i++;
						}
						/*
						 * If i find a number then find all the digits from this
						 * number and save them to the table helper. Count how
						 * many they are and then find which number these digits
						 * shape. Then save it to b array
						 */
						if (charList.get(i) >= 48 && charList.get(i) <= 57) {

							int[] helper = new int[5];
							int sum = 0;
							int k = i;
							int j = 0;
							int counter = -1;
							while (charList.get(k) >= 48 && charList.get(k) <= 57) {
								helper[j] = Character.getNumericValue(charList.get(k));
								j++;
								k++;
								counter++;
							}
							for (int a = 0; a < j; a++) {
								sum += helper[a] * Math.pow(10, (counter - a));
							}
							if (minus == -1) {
								sum = sum * (-1);
								minus = 0;
							}
							b.add(sum);
							i = k - 1;
						}
					}
				}
				int op = 0; /*
							 * A counter which I use to store the values from
							 * coefficient arraylist to A
							 */
				/*
				 * Store the appropriate values to A
				 */
				for (int f = 1; f <= n; f++) {
					if (op < indexes.size()) {
						if (f == indexes.get(op)) {
							A.get(lines).add(coeffiecients.get(op));
							op++;
						} else
							A.get(lines).add(0);
					} else
						A.get(lines).add(0);

				}
				lines++;
			}
		}

		/*
		 * Create my ouput file
		 */
		if (noterror) {

			/*
			 * Print my arrays
			 */
			System.out.println("c = " + c);
			System.out.println("A = ");
			for (int i = 0; i < A.size(); i++) {
				String getValue = A.get(i).toString();
				System.out.println(getValue);
			}
			System.out.println("b = " + b);
			System.out.println("Eqin = " + Eqin);
			System.out.println("MinMax = " + MinMAx[0]);

			try {
				PrintWriter writer = new PrintWriter("LP-2-Results.txt", "UTF-8");
				writer.print("c = ");
				writer.println(c);
				writer.println();
				writer.print("A = ");
				writer.println();
				for (int i = 0; i < A.size(); i++) {
					String getValue = A.get(i).toString();
					writer.println(getValue);
				}
				writer.println();
				writer.print("b = ");
				writer.println(b);
				writer.println();
				writer.print("Eqin = ");
				writer.println(Eqin);
				writer.println();
				writer.print("MinMax = ");
				writer.println(MinMAx[0]);
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			System.out.println("Something went wrong with the input file's syntax!");
	}
}
