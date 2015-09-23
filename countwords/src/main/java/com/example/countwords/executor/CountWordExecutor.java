package com.example.countwords.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.countwords.thread.ReadFile;

/**
 * This is the main class which executes all the logic for getting the count and
 * sorting the same.
 * 
 * @author Lalit
 *
 */
public class CountWordExecutor {

	public void startExecutor(List<String> filePaths) {
		ExecutorService threadService = Executors.newFixedThreadPool(filePaths.size());
		for (String filePath : filePaths) {
			threadService.execute(new ReadFile(filePath));
		}
		threadService.shutdown();

		try {
			threadService.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public Map<String, Integer> getWordCountMap(CopyOnWriteArrayList<String> data) {
		Map<String, Integer> countWordMap = new HashMap<String, Integer>();
		for (String line : data) {
			// Splitting words based on space or , or . Using regex.
			String[] words = line.split("[\\s,.]+");
			for (String word : words) {
				// If the word already exists then we update the count by 1 or
				// else we insert the record with count 1.
				if (countWordMap.containsKey(word)) {
					countWordMap.put(word, countWordMap.get(word) + 1);
				} else {
					countWordMap.put(word, 1);
				}
			}
		}
		return countWordMap;
	}

	public List<Entry<String, Integer>> getSortedMapBasedOnValues(Map<String, Integer> countWordMap) {
		// We are sorting the map based on values.
		Set<Entry<String, Integer>> entrySet = countWordMap.entrySet();
		List<Entry<String, Integer>> entryList = new ArrayList<Entry<String, Integer>>(entrySet);
		Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		return entryList;
	}

}
