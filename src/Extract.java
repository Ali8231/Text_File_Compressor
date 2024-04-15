import java.io.*;
import java.util.*;

public class Extract {
	
	public static int getBinary(int decimal) {
		if(decimal < 2) return decimal;
		return getBinary(decimal/2) * 10 + (decimal%2);
	}
	
	public static void deCode(File codeFile, String directory) throws IOException {
		
		FileReader file = new FileReader(codeFile);
		BufferedReader br = new BufferedReader(file);
		
		File extracted = new File(directory + "\\extracted.txt");
		extracted.createNewFile();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(directory + "\\extracted.txt"));
		
		//hcode is a map from huffman coding to its equivalent character
		Map<String, String> hcode = new HashMap<String, String>();
		
		String newline = System.getProperty("line.separator");
		String line = "", code = "", binary = "", remaining_zero = "";
		int flag = 0, decimal = 0;
		
		while((line = br.readLine()) != null) {
		    if(line.isEmpty()) {	
		    	flag = 1;
		    	//continue;
		    }
	        
		    if(flag == 0) {
		    	String string[] = line.split("=");
		    	hcode.put(string[1], string[0]);		//string[1] is huffman code and string[0] is its equivalent character
		    }
		    
		    else {
		    	
		    	for(int i=0; i<line.length(); i++) {	//iterate over characters of a line
		    		char ch = line.charAt(i);			
		    		
		    		decimal = (int) ch;					//decimal is equivalent ascii value of character
		    		
		    		//if decimal is one of the mapped ascii values convert to the initial ascii value 
		    		
		    		switch(decimal) {
						case(200):{
							decimal = 9;
							break;
						}
						case(201):{
							decimal = 10;
							break;
						}	
						case(202):{
							decimal = 11;
							break;
						}
						case(203):{
							decimal = 12;
							break;
						}	
						case(204):{
							decimal = 13;
							break;
						}	
						case(205):{
							decimal = 28;
							break;
						}
						case(206):{
							decimal = 29;
							break;
						}
						case(207):{
							decimal = 30;
							break;
						}
						case(208):{
							decimal = 31;
							break;
						}
						case(209):{
							decimal = 32;
							break;
						}
						default:
							break;
		    		}
		    		
		    		binary = Integer.toString(getBinary(decimal)); //convert decimal to binary
		    		for(int k=0; k<7-binary.length(); k++) {	   //if length of binary is less than 7 add enough 0s to reach 7
		    			remaining_zero += '0';
		    		}
		    		binary = remaining_zero + binary;
		    		remaining_zero = "";
		    			
		    		for(int j=0; j<binary.length(); j++) {			//iterate over binary
		    			
		    			if(hcode.containsKey(code)) {				//if code is equal to one of huffman's codings
		    				if(hcode.get(code) == "") {				//if equivalent character of the coding is newline
		    					writer.write(newline);
		    				}
		    				else {									
		    					writer.write(hcode.get(code));		//write equivalent character of the coding into extracted file
		    				}
		    				code = "";								//empty the temporary variable
		    			}
		    			
		    			code += binary.charAt(j);					//write binaries to a temporary variable
		    		}
		    	}
		    		
		    	if(hcode.containsKey(code)) {
    				if(hcode.get(code) == "") {
    					writer.write(newline);
    				}
    				else {
    					writer.write(hcode.get(code));
    				}
    				code = "";
    			}
		    		
		    }
		    	
		       
		}
		br.close();
		writer.close();
		
	}
}
