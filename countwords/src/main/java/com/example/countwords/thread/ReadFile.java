package com.example.countwords.thread;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.countwords.main.CountWordsStarter;
import com.google.common.collect.ImmutableList;

public class ReadFile implements Runnable {

	private String resourcePath;

	public ReadFile(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	/**
	 * Gets called when the thread start running. This method will read all the
	 * file names and add lines from those files to List<String>
	 */
	public void run() {
		URL resource = CountWordsStarter.class.getResource(resourcePath);
		try {
			List<URI> fileNames = getFileNames(new ArrayList<URI>(), (Paths.get(resource.toURI())));
			for (URI fileName : fileNames) {
				List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
				CountWordsStarter.DATA.addAll(lines);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns all the file URI which are in folder or sub folder.
	 * 
	 * @param fileNames
	 * @param dir
	 * @return
	 */
	private List<URI> getFileNames(List<URI> fileNames, Path dir) {
		if (!Files.isDirectory(dir)) {
			return ImmutableList.<URI> of(dir.toUri());
		}
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for (Path path : stream) {
				if (path.toFile().isDirectory()) {
					getFileNames(fileNames, path);
				} else {
					fileNames.add(path.toUri());
					System.out.println(path.getFileName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileNames;
	}
}
