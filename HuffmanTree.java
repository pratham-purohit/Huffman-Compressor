import java.util.*;
import java.io.*;

class HuffmanTree {
	protected static HashMap<Character, Integer> freq;
	protected static HashMap<Character, String> code;
	protected static PriorityQueue<node> queue;
	static node root;
	public static void main(String[] args) {
		
		Scanner stdin = new Scanner(System.in);
		System.out.print("Enter message: ");
		String filename = stdin.nextLine();
		String message = fileReader(filename);
		if(message == null) { return; }
		System.out.println();
		stdin.close();
		
		// TODO:
		// 1. calculate frequency of each character
		// 2. pair two character with lowest frequency to form a tree
		// 3. repeat untill only one node remains
		// 4. extract code from tree structure
		// 5. encode message using code book
		freq = new HashMap<>();
		getFrequency(message);
		
		queue = new PriorityQueue<>(new my_cmp());
		for(char ch: freq.keySet()) {
			queue.add(new node(null, null, ch, freq.get(ch)));
		}
		treemaker();
		code = new HashMap<>();
		keygen(root, "");
		aPrinter(root);
		System.out.println(getEncodedMessage(message));
	}

	//A filereader to read the data of a file to compress
	// TODO
	static String fileReader(String filename) {
		Scanner sc;
		try {
			sc = new Scanner(new File(filename));
		} catch(FileNotFoundException) {
			System.out.println("No such file...");
			return null;
		}
		StringBuilder build = new StringBuilder();
		while(sc.hasNextLine()) {
			build.append(sc.nextLine() + "\n");
		}
		sc.close();
		return build.toString();
	}

	//Calculates the frequency of each letter in the message
	static void getFrequency(String message) {
		int l = message.length();
		for(int i = 0; i < l; i++){
			char c = message.charAt(i);
			if(freq.containsKey(c)){
				int temp = freq.get(c);
				freq.put(c,++temp);
			}
			else{
				freq.put(c, 1);
			}
		}
	}
	
	//recursively connects the two smallest nodes in the queue to make a tree
	static void treemaker() {
		if(queue.size() > 1) {
			node less = queue.peek();
			queue.poll();
			node great = queue.peek();
			queue.poll();
			node temp = new node(less, great, (less.c < great.c)?less.c:great.c , (less.fr + great.fr));
			root = temp;
			queue.add(temp);
			treemaker();
		}
	}
	
	//builds a code book by traversing the tree
	static void keygen(node root, String path ) {
		if(root.greater == null || root.lesser == null) {
			code.put(root.c, path);
			return;
		}
		keygen(root.lesser, path+"1");
		keygen(root.greater, path+"0");
	}
	
	static String getEncodedMessage(String message) {
		String encodedMessage = "";
		for(char c: message.toCharArray()) {
			encodedMessage += code.get(c);
		}
		return encodedMessage;
	}
	
	//pretty printer?
	static void aPrinter(node root) {
		if(root.greater == null || root.lesser == null)
			System.out.println("'"+root.c+"': "+ code.get(root.c));
		else{
			dumbPrinter(root.greater);
			dumbPrinter(root.lesser);
		}
	}
}

//Node class for the Huffman tree
class node{
	node greater;
	node lesser;
	char c;
	int fr;
	node(node lesser, node greater, char c, int fr) {
		this.greater = greater;
		this.lesser = lesser;
		this.c = c;
		this.fr = fr;
	}
}

//Comparator for priority queue
class my_cmp extends HuffmanTree implements Comparator<node> {
	public int compare(node node1, node node2) {
		if(node1.fr == node2.fr) {
			return((int)node1.c - (int)node2.c);
		}
		return(node1.fr - node2.fr);
	}
}