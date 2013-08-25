package com.neetomo.test.hadoop.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private Text outputKey = new Text();
    private IntWritable outputValue = new IntWritable(1);

    @Override
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        String[] words = value.toString().toLowerCase().split(" ");
        for(String word : words) {
            outputKey.set(word);
            context.write(outputKey, outputValue);
        }
    }
}
        
