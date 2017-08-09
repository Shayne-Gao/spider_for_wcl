import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.*;

public class Analysiser {
	static String filePath =   null;
	public static void main (String []arg) throws IOException{
		filePath = arg[0];
		File csv = new File(filePath);  
	    BufferedReader br = null;
	   
	    try
	    {
	        br = new BufferedReader(new FileReader(csv));
	        FileWriter bw =new FileWriter(filePath,true);
	        bw.write("Processed Data Below:\n");
	        bw.flush();
	    } catch (FileNotFoundException e)
	    {
	        e.printStackTrace();
	    }
	    String line = "";
	    String everyLine = "";
	    List<String[]> result = new ArrayList<>();
	    try {
 
	            while ((line = br.readLine()) != null)   
	            {
	                everyLine = line;
	                String[] singleLine = everyLine.split(",");
	                result.add(singleLine);
	                
	            }
	            
	    } catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    
 
	    /*
	     *  0'keystone',
		    1'name',
		    2'Mas',
		    3'Cri',
		    4'Ver',
		    5'Has',
		    6'Agi',
		    7'Int',
		    8'Sta',
		    9'Lee',
		    10'Avo',
		    11'legend1',
		    12'legend2',
		    13'trinket1',
		    14'trinket2',
		    15't1',
			16't2',
		    17't3',
		    18't4',
		    19't5',
		    20't6',
		    21't7',
	     */
	    Map<String,Integer> titleMap = new HashMap<String, Integer>();
	    String[] titleLine = result.get(0);
	    for(int i=0;i<titleLine.length;i++  ){
	    	titleMap.put(titleLine[i], i);
	    	
	    }
 
	    result.remove(result.size()-1);
	    result.remove(0);
	    int sampleCount = result.size();
	 
	    Map<String,Integer> legend = new HashMap<String,Integer>();
	    Map<String,Integer> trinket = new HashMap<String,Integer>();
	    Map<String,Integer> t1 = new HashMap<String,Integer>();
	    Map<String,Integer> t2 = new HashMap<String,Integer>();
	    Map<String,Integer> t3 = new HashMap<String,Integer>();
	    Map<String,Integer> t4 = new HashMap<String,Integer>();
	    Map<String,Integer> t5 = new HashMap<String,Integer>();
	    Map<String,Integer> t6 = new HashMap<String,Integer>();
	    Map<String,Integer> t7 = new HashMap<String,Integer>();
	    for(String[] r:result){
	    	if(r.length < 21){
	    		break;
	    	}
//	    	System.out.println(r[11]);   
	    	 
	    	legend.put(r[titleMap.get("legend1")],legend.getOrDefault(r[titleMap.get("legend1")], 0)+1);
	    	legend.put(r[titleMap.get("legend2")], legend.getOrDefault(r[titleMap.get("legend2")], 0)+1);
	    	 
	    	trinket.put(r[titleMap.get("trinket1")], trinket.getOrDefault(r[titleMap.get("trinket1")], 0)+1);
	    	trinket.put(r[titleMap.get("trinket2")], trinket.getOrDefault(r[titleMap.get("trinket2")], 0)+1);
	    	//telent
	    	t1.put(r[titleMap.get("t1")], t1.getOrDefault(r[titleMap.get("t1")], 0)+1);
	    	t2.put(r[titleMap.get("t2")], t2.getOrDefault(r[titleMap.get("t2")], 0)+1);
	    	t3.put(r[titleMap.get("t3")], t3.getOrDefault(r[titleMap.get("t3")], 0)+1);
	    	t4.put(r[titleMap.get("t4")], t4.getOrDefault(r[titleMap.get("t4")], 0)+1);
	    	t5.put(r[titleMap.get("t5")], t5.getOrDefault(r[titleMap.get("t5")], 0)+1);
	    	t6.put(r[titleMap.get("t6")], t6.getOrDefault(r[titleMap.get("t6")], 0)+1);
	    	t7.put(r[titleMap.get("t7")], t7.getOrDefault(r[titleMap.get("t7")], 0)+1);
 
	    }
	    //sort
	    
	    writeAppendFile("\n====Legendary items====\n");
	    processItem(legend,6);
	    writeAppendFile("\n====Trinket items====\n");
	    processItem(trinket,6);
	    writeAppendFile("\n====Telents====\n");
	    writeAppendFile("====T1====\n");
	    processItem(t1,3);
	    writeAppendFile("====T2====\n");
	    processItem(t2,3);
	    writeAppendFile("====T3====\n");
	    processItem(t3,3);
	    writeAppendFile("====T4====\n");
	    processItem(t4,3);
	    writeAppendFile("====T5====\n");
	    processItem(t5,3);
	    writeAppendFile("====T6====\n");
	    processItem(t6,3);
	    writeAppendFile("====T7====\n");
	    processItem(t7,3);
	}
	public static void processItem(Map<String,Integer> map,int outputTop){
		 //sort
	    List<Entry<String,Integer>> list = sortMaptoList(map);
	    //output
	    outputList(list,outputTop);
	}
	
	public static List<Entry<String,Integer>> sortMaptoList(Map<String,Integer> legend){
		  List<Entry<String,Integer>> list = new ArrayList<Entry<String,Integer>>(legend.entrySet());	
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			    public int compare(Map.Entry<String, Integer> o1,
			            Map.Entry<String, Integer> o2) {
			        return (o2.getValue() - o1.getValue());
			    }
			});
		return list;
	}
	
	public static void outputList(List<Entry<String,Integer>> list,int outputTop){
//		int outputTop = 6;
		int outputCounter = 0;
		FileWriter bw;
		try {
			bw = new FileWriter(filePath,true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		for (Entry<String, Integer> mapping : list) {  
			if(outputCounter++ >= outputTop) break;
			String outputLine = mapping.getKey() + "," + mapping.getValue() + "\n";
			System.out.print(outputLine);
			try {
		        bw.write(outputLine);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void writeAppendFile(String content){
		System.out.println(content);
		 FileWriter bw;
		try {
			bw = new FileWriter(filePath,true);
			bw.write(content);
		    bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	}
	
}
