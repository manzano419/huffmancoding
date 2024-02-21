package p2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

import p2.DataStructures.List.List;
import p2.DataStructures.Map.HashTableSC;
import p2.DataStructures.Map.Map;
import p2.DataStructures.SortedList.SortedLinkedList;
import p2.DataStructures.SortedList.SortedList;
import p2.DataStructures.Tree.BTNode;
import p2.Utils.BinaryTreePrinter;

/**
 * The Huffman Encoding Algorithm
 * 
 * This is a data compression algorithm designed 
 * by David A. Huffman and published in 1952
 * 
 * What it does is it takes a string and by constructing 
 * a special binary tree with the frequencies of each character.
 * 
 * This tree generates special prefix codes that make the size 
 * of each string encoded a lot smaller, thus saving space.
 * 
 * @author Fernando J. Bermudez Medina (Template) 
 * @author Anthony Manzano - 802-19-4083 (Implementation)
 * @version 3.0
 * @since 03/28/2023
 */
public class HuffmanCoding {

	public static void main(String[] args) {
		HuffmanEncodedResult();
	}

	/* This method just runs all the main methods developed or the algorithm */
	private static void HuffmanEncodedResult() {
		/* You can create other test input files and add them to the inputData Folder */
		String data = load_data("input3.txt");

		/* If input string is not empty we can encode the text using our algorithm */
		if(!data.isEmpty()) {
			Map<String, Integer> fD = compute_fd(data);
			BTNode<Integer,String> huffmanRoot = huffman_tree(fD);
			Map<String,String> encodedHuffman = huffman_code(huffmanRoot);
			String output = encode(encodedHuffman, data);
			process_results(fD, encodedHuffman,data,output);
		} else 
			System.out.println("Input Data Is Empty! Try Again with a File that has data inside!");
		

	}

	/**
	 * Receives a file named in parameter inputFile (including its path),
	 * and returns a single string with the contents.
	 * 
	 * @param inputFile name of the file to be processed in the path inputData/
	 * @return String with the information to be processed
	 */
	public static String load_data(String inputFile) {
		BufferedReader in = null;
		String line = "";

		try {
			/**
			 * We create a new reader that accepts UTF-8 encoding and 
			 * extract the input string from the file, and we return it
			 */
			in = new BufferedReader(new InputStreamReader(new FileInputStream("inputData/" + inputFile), "UTF-8"));

			/**
			 * If input file is empty just return an 
			 * empty string, if not just extract the data
			 */
			String extracted = in.readLine();
			if(extracted != null)
				line = extracted;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) 
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return line;
	}

	/**
	 * Method that will create a Map with the frequency of each letter in the given string to later build the huffman tree.
	 * 
	 * @param String to count frequency
	 * @return Map with the key as the letters and value of the frequency of that letter
	 */
	public static Map<String, Integer> compute_fd(String inputString) {
		/* TODO Compute Symbol Frequency Distribution of each character inside input string */
		
		//initialize map to store the characters and it s frequency
		HashTableSC<String, Integer> result = new HashTableSC<>();
		String line = inputString;
		//create an array to store each character in the line
		ArrayList<String> chars = new ArrayList<>();
		
		//add each character to the array
		for( int i = 0; i < inputString.length(); i++) {
			Character letter;
			//getting the character at position i
			letter = line.charAt(i);
			//adding it to the array
			chars.add(letter.toString());
		}
		
		//mapping the character, if the character is already in the map then add 1 to the value.
		for(String character : chars) {
			if(result.containsKey(character)) {
				result.put(character, result.get(character)+1);
			}else {
				result.put(character, 1);
			}
		}
		
 		return result; //Dummy Return
	}

	/**
	 * Method that build the huffman tree given the map with the characters and their frequencies.
	 * 
	 * @param - Map containing characters and frequencies
	 * @return - Root node of the tree
	 */
	public static BTNode<Integer, String> huffman_tree(Map<String, Integer> fD) {
	    // if the frequency distribution map is null or empty, return null.
	    if (fD.isEmpty()) {
	        return null;
	    }

	    // create a sorted linked list of BTNodes, where each node corresponds to a
	    // symbol in the frequency distribution map.
		SortedLinkedList<BTNode<Integer,String>> treeList = new SortedLinkedList<>();
		for( String value : fD.getKeys()) {
			BTNode<Integer, String> newval = new BTNode<>();
			newval.setKey(fD.get(value));
			newval.setValue(value);
			treeList.add(newval);
		}

	    // if the sorted linked list is empty, return null.
	    if (treeList.isEmpty()) {
	        return null;
	    }
	    
	    // build the Huffman Tree recursively by merging the nodes in the sorted linked list until
	    // there is only one node left.
	    BTNode<Integer, String> root = buildHuffmanTree(treeList);
	    //BinaryTreePrinter.print(root);
	    
	    //returns the root node of the tree.
	    return root;
	}

	/*
	 * Builds the huffman tree recursively given the list containing the nodes.
	 * @param - list with nodes.
	 * @return - the root node of the huffman tree.
	 */
	private static BTNode<Integer, String> buildHuffmanTree(SortedLinkedList<BTNode<Integer, String>> treeList) {
	    // if there is only one node left, it is the root node of the Huffman Tree.
	    if (treeList.size() == 1) {
	        return treeList.get(0);
	    }

	    // get the two nodes with the smallest keys from the sorted linked list
	    BTNode<Integer, String> node1 = treeList.get(0);
	    treeList.remove(node1);

	    BTNode<Integer, String> node2 = treeList.get(0);
	    treeList.remove(node2);

	    // create a new parent node for the two nodes with the sum of their keys as its
	    // key and the concatenation of their values as its value
	    BTNode<Integer, String> parent = new BTNode<>();
	    parent.setLeftChild(node1);
	    parent.setRightChild(node2);
	    parent.setKey(node1.getKey() + node2.getKey());
	    parent.setValue(node1.getValue() + node2.getValue());

	    // set the parent node as the parent of the two child nodes
	    node1.setParent(parent);
	    node2.setParent(parent);

	    // add the parent node to the sorted linked list
	    treeList.add(parent);

	    // recursively build the Huffman Tree
	    return buildHuffmanTree(treeList);
	}

	/**
	 * Method that recursevly creates the code for each character in the huffman tree
	 * 
	 * @param Takes the root of the huffman tree and the hashtable where to store it.
	 * @return return the hashtable with the values.
	 */
	public static HashTableSC<String, String> huffmancoder (HashTableSC<String, String> code, BTNode <Integer, String> root, String binarycode){

			//if leaf node is found, add it to the map and its given value
		if (root.getLeftChild() == null && root.getRightChild() == null) {
			code.put(root.getValue(), binarycode);
			return code;
		}
	
		//recursevly call the function adding the correct value given the direction it is moving in.
		huffmancoder(code, root.getLeftChild(), binarycode + "0");
		huffmancoder(code, root.getRightChild(), binarycode + "1");
		
		//return the hashtable.
		return code;
	}
		
	/*Method that calls the recursive method and returns the hashtable.
	 * @param- root node of the huffman tree.
	 * @return- returns the table with the encoded values of each character.
	 * */
	 
 	public static Map<String, String> huffman_code(BTNode<Integer,String> huffmanRoot) {
		/* TODO Construct Prefix Codes */
 		HashTableSC<String, String> code = new HashTableSC<>();
 		 code = huffmancoder(code, huffmanRoot, "");
 		 
		return code; //Dummy Return
	}

	/**
	 * Method that encodes the new output with the values of the huffman tree. It does so by getting the values from the hash table.
	 * 
	 * @param -  the map containing the given binary value of each character
	 * @param - the string to code
	 * @return - the new encoded string with the huffman values
	 */
	public static String encode(Map<String, String> encodingMap, String inputString) {
		/* TODO Encode String */
		//initalize the variables to use.
		String result ="";
		String line = inputString;
		ArrayList<String> chars = new ArrayList<>();
		
		//same ad earlier method, we add each character to an array to then code them 1 by 1.
		for( int i = 0; i < inputString.length(); i++) {
			Character letter;
			letter = line.charAt(i);
			chars.add(letter.toString());
		}
		
		//iterate through each character in the array and get their huffman code and add it to the result string.
		for(String character : chars) {
			result += encodingMap.get(character);
		}
		
		//return the new huffman code.
		return result; //Dummy Return
	}

	/**
	 * Receives the frequency distribution map, the Huffman Prefix Code HashTable, the input string, 
	 * and the output string, and prints the results to the screen (per specifications).
	 * 
	 * Output Includes: symbol, frequency and code. 
	 * Also includes how many bits has the original and encoded string, plus how much space was saved using this encoding algorithm
	 * 
	 * @param fD Frequency Distribution of all the characters in input string
	 * @param encodedHuffman Prefix Code Map
	 * @param inputData text string from the input file
	 * @param output processed encoded string
	 */
	public static void process_results(Map<String, Integer> fD, Map<String, String> encodedHuffman, String inputData, String output) {
		/*To get the bytes of the input string, we just get the bytes of the original string with string.getBytes().length*/
		int inputBytes = inputData.getBytes().length;

		/**
		 * For the bytes of the encoded one, it's not so easy.
		 * 
		 * Here we have to get the bytes the same way we got the bytes 
		 * for the original one but we divide it by 8, because 
		 * 1 byte = 8 bits and our huffman code is in bits (0,1), not bytes. 
		 * 
		 * This is because we want to calculate how many bytes we saved by 
		 * counting how many bits we generated with the encoding 
		 */
		DecimalFormat d = new DecimalFormat("##.##");
		double outputBytes = Math.ceil((float) output.getBytes().length / 8);

		/**
		 * to calculate how much space we saved we just take the percentage.
		 * the number of encoded bytes divided by the number of original bytes 
		 * will give us how much space we "chopped off".
		 * 
		 * So we have to subtract that "chopped off" percentage to the total (which is 100%) 
		 * and that's the difference in space required
		 */
		String savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));


		/**
		 * Finally we just output our results to the console 
		 * with a more visual pleasing version of both our 
		 * Hash Tables in decreasing order by frequency.
		 * 
		 * Notice that when the output is shown, the characters 
		 * with the highest frequency have the lowest amount of bits.
		 * 
		 * This means the encoding worked and we saved space!
		 */
		System.out.println("Symbol\t" + "Frequency   " + "Code");
		System.out.println("------\t" + "---------   " + "----");

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		/**
		 * To print the table in decreasing order by frequency, 
		 * we do the same thing we did when we built the tree.
		 * 
		 * We add each key with it's frequency in a node into a SortedList, 
		 * this way we get the frequencies in ascending order
		 */
		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}

		/**
		 * Since we have the frequencies in ascending order, 
		 * we just traverse the list backwards and start printing 
		 * the nodes key (character) and value (frequency) and find 
		 * the same key in our prefix code "Lookup Table" we made 
		 * earlier on in huffman_code(). 
		 * 
		 * That way we get the table in decreasing order by frequency
		 */
		for (int i = sortedList.size() - 1; i >= 0; i--) {
			BTNode<Integer,String> node = sortedList.get(i);
			System.out.println(node.getValue() + "\t" + node.getKey() + "\t    " + encodedHuffman.get(node.getValue()));
		}

		System.out.println("\nOriginal String: \n" + inputData);
		System.out.println("Encoded String: \n" + output);
		System.out.println("Decoded String: \n" + decodeHuff(output, encodedHuffman) + "\n");
		System.out.println("The original string requires " + inputBytes + " bytes.");
		System.out.println("The encoded string requires " + (int) outputBytes + " bytes.");
		System.out.println("Difference in space requiered is " + savings + "%.");
	}


	/*************************************************************************************
	 ** ADD ANY AUXILIARY METHOD YOU WISH TO IMPLEMENT TO FACILITATE YOUR SOLUTION HERE **
	 *************************************************************************************/

	/**
	 * Auxiliary Method that decodes the generated string by the Huffman Coding Algorithm
	 * 
	 * Used for output Purposes
	 * 
	 * @param output - Encoded String
	 * @param lookupTable 
	 * @return The decoded String, this should be the original input string parsed from the input file
	 */
	public static String decodeHuff(String output, Map<String, String> lookupTable) {
		String result = "";
		int start = 0;
		List<String>  prefixCodes = lookupTable.getValues();
		List<String> symbols = lookupTable.getKeys();

		/**
		 * Loop through output until a prefix code is found on map and 
		 * adding the symbol that the code that represents it to result 
		 */
		for(int i = 0; i <= output.length();i++){

			String searched = output.substring(start, i);

			int index = prefixCodes.firstIndex(searched);

			if(index >= 0) { // Found it!
				result= result + symbols.get(index);
				start = i;
			}
		}
		return result;    
	}
		
}
