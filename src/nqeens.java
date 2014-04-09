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
		public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
			static enum Counters { DFS_STEPS,ITERATIONS };

			private final static Text T = new Text("T");
			private final static Text F = new Text("F");
			private final static Text C = new Text("C");

			private Text word = new Text();

			private long iterations;
			private String inputFile;
			private int boardDim;

			public void configure(JobConf job) {
				boardDim = job.getInt("nqueens.board.dimension", 8);
			}

			public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
				String line = value.toString();
				String[] lineParseRes = line.split("|");

				String descriptor = lineParseRes[2];
				//if current board is a dead end or it is already complete, the just ignore.
				if (descriptor.equals("F")||descriptor.equals("C")) return;

				//otherwise, continue searching
				boardDim = Integer.parseInt(lineParseRes[0]);
				String layout = linePasrseRes[1];
				String[] tempTokens = layout.split(",");

        List<String> newBoards = board.generateNewBoards();

				Text newDescriptor;
				if (tempToken.length==boardDim-1)
					newDescriptor = C;//C for complete
				else
					newDescriptor = T;

				for(String layout: newBoards){
					output.collect(new Text(lineParseRes[0]+"|"+layout), newDescriptor);
				}

        reporter.incCounter(Counters.DFS_STEP,1);
			}

		public static class Reduce extends MapReduceBase implements Reducer<Text,BooleanWritable,Text,BooleanWritable> {
			static enum Counters {DFS_STEPS, ITERATIONS};
			private final static BooleanWritable T = new BooleanWritable(true);
			private final static BooleanWritable F = new BooleanWritable(false);

			public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Result reporter) throws IOException{
				//board layouts are never grouped. process one by one.
				String layout = values.next().toString();
				//process layout with Board class
				if (layout.equals("C")){
					//if received layout is complete, write to the result directly
					output.collect(key,values);
				}else{
					//else continue searching
					//note that layout has only two parts seperated by "|"
					//i.e. dimension|queensPos
					String layout = key.toString();
					String[] token = layout.split("|");
					int dimension = Integer.parseInt(token[0]);
					String queenPos = token[1];
					int currentQueens = queenPos.split(",");

					Board board = new Board(queenPos);
					List<String> newQueenPos = board.generateNewBoards();

					Text newDescriptor;
					if (currentQueens == dimension-1)
						newDescriptor = C;
					else
						newDescriptor = T;

					for(String layout:newQueenPos){
						output.collect(new Text(token[0]+"|"+layout), newDescriptor);
					}
				}

				reporter.incCounter(Counters.DFS_STEP,1);
				reporter.incCounter(Counters.ITERATIONS,1);
			}


			public int run(String[] args) throws Exception {
				JobConf conf = new JobConf(getConf(), WordCount.class);
				conf.setJobName("Nqueens");

				conf.setOutputKeyClass(Text.class);
				conf.setOutputValueClass(Text.class);

				conf.setMapperClass(Map.class);
				conf.setReducerClass(Reduce.class);

				conf.setInputFormat(TextInputFormat.class);
				conf.setOutputFormat(TextOutputFormat.class);

				List<String> other_args = new ArrayList<String>();

				FileInputFormat.setInputPaths(conf, new Path(other_args.get(0)));
				FileOutputFormat.setOutputPath(conf, new Path(other_args.get(1)));

				JobClient.runJob(conf);
				return 0;
			}

			public static void main(String[] args) throws Exception {
				int res = ToolRunner.run(new Configuration(), new WordCount(), args);
				System.exit(res);
			}
		}
