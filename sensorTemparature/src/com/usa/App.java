package com.usa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		try {
			InputStream in = new App().getClass().getResourceAsStream("/input.txt");
			InputStreamReader ins= new InputStreamReader(in);
			BufferedReader br = new BufferedReader(ins);
			InputStream in2 = new App().getClass().getResourceAsStream("/duration.txt");
			InputStreamReader ins2= new InputStreamReader(in2);
			BufferedReader br2 = new BufferedReader(ins2);
			processFundData(br, br2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static Map<Integer, Integer> tempMap = new HashMap<Integer, Integer>();

	public static void processFundData(BufferedReader br, BufferedReader config) throws IOException {
		String data;
		int duration = 1;
		SortedSet<Integer> timeSet = new TreeSet<Integer>();

		StringBuilder fileData = new StringBuilder();
		StringBuilder logData = new StringBuilder();
		FileWriter fos = new FileWriter(new File("output.txt"));
		FileWriter foslog = new FileWriter(new File("log.txt"));
		try {
			while ((data = config.readLine()) != null) {
				duration = Integer.parseInt(data);
				duration=(duration*1000)-1;
			}
			while ((data = br.readLine()) != null) {
				String[] tempData = data.split(",");
				if (tempData.length == 3) {
					int sensorId = Integer.parseInt(tempData[0]);
					int millisecs = Integer.parseInt(tempData[1]);
					timeSet.add(millisecs);

					int temparature = Integer.parseInt(tempData[2]);
					tempMap.put(millisecs, temparature);
				}
			}
			int first = timeSet.first();
			int limit = first + duration;
			calculateAvg(first,first, limit, timeSet, duration);
			System.out.println(avg);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fos.close();
			foslog.close();
		}

	}

	static StringBuilder avg = new StringBuilder();

	private static void calculateAvg(int first,int next, int limit, SortedSet<Integer> timeSet, int duration) {
		if (limit != 0) {
			avg.append(next + "-");
			double avgValue=0.00;
			int temp = tempMap.get(first);
			int index = 0;
			List<Integer> used=new ArrayList<Integer>();
			used.add(first);
			for (Integer timesec : timeSet) {
				if (timesec <= limit) {
					if (index != 0) {
						temp = temp + tempMap.get(timesec);
						used.add(timesec);
					}
					index++;
				}else{
					first=timesec;
					break;
				}
			}
			timeSet.removeAll(used);
			avgValue = Double.valueOf((temp / (double)index));
			if(timeSet.isEmpty()){
				avg.append((next+duration) + ": " + avgValue);
				avg.append("\n");
				return;
			}
			int last =timeSet.last();
			if(limit==last){
				avg.append((next+duration) + ": " + avgValue);
				avg.append("\n");
				return;
			}
			avg.append(limit + ": " + avgValue);
			avg.append("\n");
			
			next=limit+1;
			limit = next + duration;
			if(limit >last){
				limit=last;
			}
			calculateAvg(first,next, limit, timeSet, duration);
		}
	}
}
