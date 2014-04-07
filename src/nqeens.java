package org.myorg;

import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class Nqueens extends Configured implements tools{
		public static class Map extends MapReduceBase implements Mapper<LongWritable, BooleanWritable, Text, Text> {
			static enum Counters { ITERATIONS }

			private final static BooleanWritable T = new BooleanWritable(true);
			private final statci BooleanWritable F = new BooleanWritable(false);

			private Text word = new Text();

			private long iterations;
			private String inputFile;
			private int boardDim;

			public void configure(JobConf job) {
				boardDim = job.getInt("nqueens.board.dimension", 8);
				inputFile = job.get("map.input.file");

				//Path[] patternsFiles = new Path[0];
			}
/*
			public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
				String line = (caseSensitive) ? value.toString() : value.toString().toLowerCase();

				for (String pattern : patternsToSkip) {
					line = line.replaceAll(pattern, " ");
				}

				StringTokenizer tokenizer = new StringTokenizer(line);
				while (tokenizer.hasMoreTokens()) {
					word.set(tokenizer.nextToken());
					output.collect(word, one);
					reporter.incrCounter(Counters.INPUT_WORDS, 1);
				}

				if ((++numRecords % 100) == 0) {
					reporter.setStatus("Finished processing " + numRecords + " records " + "from the input file: " + inputFile);
				}
			}
*/

			public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
				String rows = value.toString();

				Board board = new Board(rows);
				


			}
