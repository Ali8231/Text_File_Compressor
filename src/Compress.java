import java.util.*;
import java.io.*;

public class Compress {
	
		//hcode is a map from character to its equivalent huffman coding
		static Map<String, String> hcode = new HashMap<String, String>();
		
		public static int getDecimal(String binary) {
			
			int n = binary.length(), j=0, decimal=0;
			for(int i=n-1; i>=0; i--) {
				if(binary.charAt(i) == '1') {
					decimal += Math.pow(2, j);
				}
				j++;
			}
			/*following cases are ascii values of whitespace characters.
			due to problem of converting whitespace characters to equivalent ascii values 
			I mapped them to other ascii values*/
			
			switch(decimal) {
				case(9):{
					decimal = 200;
					break;
				}
				case(10):{
					decimal = 201;
					break;
				}	
				case(11):{
					decimal = 202;
					break;
				}
				case(12):{
					decimal = 203;
					break;
				}	
				case(13):{
					decimal = 204;
					break;
				}	
				case(28):{
					decimal = 205;
					break;
				}
				case(29):{
					decimal = 206;
					break;
				}
				case(30):{
					decimal = 207;
					break;
				}
				case(31):{
					decimal = 208;
					break;
				}
				case(32):{
					decimal = 209;
					break;
				}
				default:
					break;
			}
			
			return decimal;
		}
		
		
		public static void buildFile(File original, String directory) throws IOException{
			
			File compressed = new File(directory + "\\compressed.cmp");
			compressed.createNewFile();
			
			FileWriter writer = new FileWriter(directory + "\\compressed.cmp");
			
			String newline = System.getProperty("line.separator");
			
			for (Map.Entry<String, String> e : hcode.entrySet()) {
	            String character = (String)e.getKey();
	            String code = (String)e.getValue();
	            writer.write(character + "=" + code + newline);
	        }
			writer.write(newline);
			
			FileReader freader = new FileReader(original);
			BufferedReader br = new BufferedReader(freader);
			
			String line = "", binary="";
			int binary_length=0, decimal=0;
			
			while((line = br.readLine()) != null) { 
				
				String string[] = line.split(""); //split one line to single characters
		        for(String character : string) {		  //iterate over characters	
		            String code = hcode.get(character);	  //get equivalent huffman code of character
		            
		        	for(int j=0; j<code.length(); j++) {	//iterate over huffman code
		        		if(binary_length == 7) {			//if binary length reach 7
		        			decimal = getDecimal(binary);	//convert binary to decimal
		        			writer.write((char) decimal);	//write equivalent char into file
		        			binary_length = 0;
		        			binary = "";
		        		}
		        		
		        		binary += code.charAt(j);			//write huffman code in a temporary variable	
		        		binary_length++;
		        	}
		        	
		
		        }
		        
		        if(hcode.containsKey("") && !line.isEmpty()) {  //if original file contains newline and current line is not empty 
		        	String code = hcode.get("");				//get equivalent huffmand code of newline
		        	for(int j=0; j<code.length(); j++) {
		        		if(binary_length == 7) {
		        			decimal = getDecimal(binary);
		        			writer.write((char) decimal);
		        			binary_length = 0;
		        			binary = "";
		        		}
		        		
		        		binary += code.charAt(j);
		        		binary_length++;
		        	}
		       
		        }
		        
		        if(binary_length == 7) {
        			decimal = getDecimal(binary);
        			writer.write((char) decimal);
        			binary_length = 0;
        			binary = "";
        		}
			
			}
			br.close();
			writer.close();
		}

		public static void getCode(HuffmanNode root, String code)
		{
			 
			if (root.left == null && root.right == null ) {
				hcode.put(root.text, code);
				return;
			}

			getCode(root.left, code + "0");
			getCode(root.right, code + "1");
		}

		public static void Code(File file, Map<String,Integer> char_frequency, String directory)
		{
			int capacity = char_frequency.size();

			PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>(capacity, new MyComparator());

			for (Map.Entry<String,Integer> e : char_frequency.entrySet()) {

				HuffmanNode hn = new HuffmanNode();
				hn.text = (String)e.getKey();
				hn.frequency = (int)e.getValue();
				
				hn.left = null;
				hn.right = null;

				q.add(hn);
			}

			HuffmanNode root = null;

			while (q.size() > 1) {

				HuffmanNode x = q.peek();
				q.poll();
				HuffmanNode y = q.peek();
				q.poll();
				HuffmanNode f = new HuffmanNode();
				f.frequency = x.frequency + y.frequency;
				f.text = x.text + y.text;
				f.left = x;
				f.right = y;
				root = f;
				q.add(f);
			}
			
			getCode(root, "");
			
			try {
				buildFile(file, directory);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	class HuffmanNode {
		int frequency;
		String text;

		HuffmanNode left;
		HuffmanNode right;
	}

	class MyComparator implements Comparator<HuffmanNode> {
		public int compare(HuffmanNode x, HuffmanNode y)
		{
			return x.frequency - y.frequency;
		}
	}



