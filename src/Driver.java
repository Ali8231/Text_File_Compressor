import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Driver extends JFrame{
	
	static final Object lock = new Object();
	static JFrame frame = new JFrame();
	static JPanel panel = new JPanel();
	static File file;
	static String file_name, file_directory, chosen_directory;
	
	public static class ConvertFile implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			synchronized(lock) {
				lock.notify();
			}
		}
	}
	
	public static class ChooseDirectory implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				chosen_directory = fc.getSelectedFile().getAbsolutePath();
				
				JButton convert = new JButton("Convert");
				JLabel path = new JLabel();
				path.setText(file_directory);
				convert.addActionListener(new ConvertFile());
				
				frame.repaint();
				panel.removeAll();
				panel.add(convert);
				panel.add(path);
				frame.add(panel);
				frame.setVisible(true);
			}
		}
	}
	
	public static class browseFile implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser fc = new JFileChooser();
			
			fc.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter restrict = new FileNameExtensionFilter("txt and cmp files", "txt", "cmp");
			fc.addChoosableFileFilter(restrict);
			
			int returnVal = fc.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				
				file = fc.getSelectedFile();
				file_name = file.getName();
				file_directory = file.getAbsolutePath();
				
				JButton chooseDirectory = new JButton("Choose");
				JLabel label = new JLabel();
				label.setText("Please choose a directory ");
				chooseDirectory.addActionListener(new ChooseDirectory());
				
				frame.repaint();
				panel.removeAll();;
				panel.add(label);
				panel.add(chooseDirectory);
				frame.add(panel);
				frame.setVisible(true);
			}
			
		}

	}
	
	
	public static void main(String[] args) throws IOException {
		
		JButton browse = new JButton("Browse");
		JLabel label = new JLabel();
		label.setText("Please choose a txt or cmp file ");
		browse.addActionListener(new browseFile());
		
		panel.add(label);
		panel.add(browse);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(300, 150);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	    
	    synchronized(lock) {
	    	try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
	    
	    String type = file_name.substring(file_name.lastIndexOf('.') + 1);
	       
	    if(type.contains("txt")) {
	        
	    	String line = "";
	    	FileReader freader = new FileReader(file);
	    	BufferedReader br = new BufferedReader(freader);
	    	Map<String, Integer> char_frequency = new HashMap<String, Integer>();
		     
		    while((line = br.readLine()) != null) {
		        	
		        String string[] = line.split("");	    //split one line to single characters
		        for(String character : string){				    //iterate over characters	
		        	if(!char_frequency.containsKey(character)){
		        		char_frequency.put(character, 1);
		            }
		            else {
		            	char_frequency.put(character, char_frequency.get(character) + 1);
		            }
		        }
		            
		    }
		        
		    br.close();
		    Compress.Code(file , char_frequency, chosen_directory);
	    }
	        
	    else if(type.contains("cmp")) {
	    	Extract.deCode(file , chosen_directory);
	    }
	    
	    frame.setVisible(false);  
	    JOptionPane.showMessageDialog(null, "Conversion was successful", "message",
                JOptionPane.INFORMATION_MESSAGE);
	
	}    
	
		    
}
	

