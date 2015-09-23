package com.example.countwords.main;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.countwords.executor.CountWordExecutor;
import com.google.common.base.Preconditions;

public class CountWordsStarter {

	public static CopyOnWriteArrayList<String> DATA = new CopyOnWriteArrayList<String>();

	/**
	 * Method method to count number of occurance for a word. It takes a
	 * numberical value and file paths as argumanet. Numberical value is n
	 * number of elements which needs to be shown as output
	 * 
	 * @param args
	 *            Format should be 5 /data.txt /data_temp.txt... 5 represent n
	 *            number of elements to be shown and other are the file which
	 *            will be processed.
	 */
	public static void main(String[] args) {

		CountWordExecutor executor = new CountWordExecutor();

		Preconditions.checkArgument(args.length > 1,
				"Argument needs to be provided in format <nth Value> <File name>...");
		Integer topNValue = Integer.valueOf(args[0]);
		Preconditions.checkArgument(topNValue > 0, "Top nth value need to be greater than 0");
		List<String> filePaths = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));

		// Starts the thread pool.
		executor.startExecutor(filePaths);
		Map<String, Integer> countWordMap = executor.getWordCountMap(DATA);
		List<Entry<String, Integer>> entries = executor.getSortedMapBasedOnValues(countWordMap);
		// Printing values.
		if(entries.size() - 1 < topNValue) {
			topNValue = entries.size() - 1;
		}
		for (Map.Entry<String, Integer> entry : entries.subList(0, topNValue)) {
			System.out.println("word " + entry.getKey() + " occurred " + entry.getValue() + " times");
		}
	}
}
