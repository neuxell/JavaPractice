import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DotaCrawler {
	Document doc;
	
	public DotaCrawler(){
	}
	
	public ArrayList<ArrayList<String>> gatherItems(){
		Control.cout("DotaCrawler: gatherItems()\n");
		ArrayList<ArrayList<String>> items = new ArrayList<ArrayList<String>>();
		
		
		File file = new File("D:\\worklipse\\Dota2Parser\\src\\items_with_stats");
		print("Opening " + file.getName() + "...");
		Scanner fi = null;
		try {
			fi = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		println(" completed.");
		
		
		
		// get the ids
		print("Parsing items for item ids...");
		
		ArrayList<String> itemIDs = new ArrayList<String>();
		while(fi.hasNext()){
			String current = fi.nextLine();
			itemIDs.add(current);
		}
		
		println(" completed. " + itemIDs);
		fi.close();
		
		
		// parse all links for the item information
		println("Rummaging through the links...");

		
		try {
			findAttribute(itemIDs);
		} catch (IOException e) {
			println(" not found.");
			e.printStackTrace();
		}
		
		return items;
	}
	
	private void findAttribute(ArrayList<String> itemIDs) throws IOException{
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(new File("D:\\worklipse\\Dota2Parser\\src\\items"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(String itemName : itemIDs){
			String url = "http://dota2.gamepedia.com/" + itemName;
			println("Visiting " + url);
			doc = Jsoup.connect(url).userAgent("Mozilla").timeout(10*1000).get();
			
			Element stats = doc.body();
			
			String attributes = null;
			
			if(stats.text().split("Bonus").length > 1)
				attributes = stats.text().split("Bonus")[1].split("Disassemble?")[0];
			println(attributes);
			
			if(attributes == null) continue;
			String[] attrs = attributes.split("\\+");
			
			for(String a : attrs) print(a + " ");
			println(" ");
			
			String attr = "";
			for(String a : attrs){
				attr += a.replace(" ", "");
				if(attrs.length > 1)
					attr+=" ";
			}
			pw.write(itemName + " =" + attr + "\n");
			pw.flush();
		}
		
		pw.close();
	}
	
	public String[] split(String[] s){
		String[] temp = s;
		
		String[] ats = {"Str", "Agi", "Int"};
		for(int i = 0; i < temp.length; i++){
			int space = findin(s[i], ' ');
			temp[i] = s[i].replace(" ", "");
			//temp[i] = s[i].split(s[i].substring(0, space+1))[0].replace(" ", "");
		}
		
		return temp;
	}
	
	public int findin(String s, char c){
		for(int i = 0; i < s.length(); i++){
			if(s.charAt(i) == c);
				return i;
		}
		return 0;
	}
	
	public void print(Object msg){ System.out.print(msg); }	
	public void println(Object msg){ System.out.println(msg); }
}
