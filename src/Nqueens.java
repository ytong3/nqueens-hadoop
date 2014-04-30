package nqueens;

import nqueens.Board;

import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class Nqueens extends Configured implements Tool{
		public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
			static enum Counters {RECORDS};

			private final static Text T = new Text("T");
			private final static Text F = new Text("F");
			private final static Text C = new Text("C");

			private int boardDim;
			private String inputFile;

			public void configure(JobConf job) {
				boardDim = job.getInt("nqueens.board.dimension", 8);
				inputFile = job.get("map.input.file");
			}

			public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
				String line = value.toString();
				
				//System.out.println("Line being processed: "+line);

				//reporter.setStatus("Searching board layout "+line+" from file "+inputFile);
			
				//line's format, e.g.: 8|1,2|T
				String[] lineParseRes = line.split("[|]");

				String descriptor = lineParseRes[2];
				//if current board is a dead end or it is already complete, the just ignore.
				if (descriptor.equals("F")||descriptor.equals("C")) {
					output.collect(new Text(lineParseRes[0]+"|"+lineParseRes[1]),new Text(descriptor));
					return;
				}

				//otherwise, continue searching
				//boardDim = Integer.parseInt(lineParseRes[0]);
				String layout = lineParseRes[1];
				String[] tempTokens = layout.split(",");
				Board board = new Board(layout,boardDim);

                List<String> newBoards = board.generateNewBoards();

				Text newDescriptor;
				if (tempTokens.length==boardDim-1)
					newDescriptor = C;//C for complete
				else
					newDescriptor = T;

				for(String newlayout: newBoards){
					output.collect(new Text(lineParseRes[0]+"|"+newlayout), newDescriptor);
				}
				
				reporter.incrCounter(Counters.RECORDS,1);

			}
        }

		public static class Reduce extends MapReduceBase implements Reducer<Text,Text,Text,Text> {
			static enum Counters {RECORDS};

			private final static Text T = new Text("T");
			private final static Text F = new Text("F");
			private final static Text C = new Text("C");
			private int boardDim;
			
			public void configure(JobConf job) {
				boardDim = job.getInt("nqueens.board.dimension", 8);
			}

			public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
				//board layouts are never grouped. process one by one.
				
				String descriptor = values.next().toString();
				//System.out.println("Reducer is currently processing "+key.toString()+" "+descriptor);
				
				//process layout with Board class
				if (descriptor.equals("C")){
					//if received layout is complete, write to the result directly
					output.collect(key,new Text(descriptor));
				}else{
					//else continue searching
					//note that layout has only two parts seperated by "\|"
					//i.e. dimension|queensPos
					String layout = key.toString();
					String[] token = layout.split("[|]");
					int dimension = Integer.parseInt(token[0]);
					String queenPos = token[1];
					int currentQueens = queenPos.split(",").length;
					//System.out.println("CurrentQueens: "+currentQueens);

					Board board = new Board(queenPos,boardDim);
					List<String> newQueenPos = board.generateNewBoards();

					Text newDescriptor;
					if (currentQueens == dimension-1)
						newDescriptor = C;
					else
						newDescriptor = T;

					for(String newlayout:newQueenPos){
						output.collect(new Text(token[0]+"|"+newlayout+"|"), newDescriptor);
					}
				}

				reporter.incrCounter(Counters.RECORDS,1);
			}
        }


        public int run(String[] args) throws Exception {
            int dfsStep = 0;
            JobConf conf;
            
            int boardDim = Integer.parseInt(args[2]);
			int numIteration = 0;
            //while there dfsSteps is less than the dimension, proceed the searching
            while (dfsStep<boardDim){
                String input, output;

                conf = new JobConf(getConf(),Nqueens.class);
                conf.setJobName("nqueens"+dfsStep);

                conf.setOutputKeyClass(Text.class);
                conf.setOutputValueClass(Text.class);

                conf.setMapperClass(Map.class);
                conf.setReducerClass(Reduce.class);

                conf.setInputFormat(TextInputFormat.class);
                conf.setOutputFormat(TextOutputFormat.class);

				if (dfsStep==0)
					FileInputFormat.setInputPaths(conf, new Path(args[0]));
				else
					FileInputFormat.setInputPaths(conf, new Path(args[1]+(numIteration-1)));
                
				FileOutputFormat.setOutputPath(conf, new Path(args[1]+(numIteration)));

                JobClient.runJob(conf);
                dfsStep = dfsStep+2;
				numIteration = numIteration+1;
            }
            return 0;
        }

        public static void main(String[] args) throws Exception {
            int res = ToolRunner.run(new Configuration(), new Nqueens(), args);
            System.exit(res);
        }
    }
