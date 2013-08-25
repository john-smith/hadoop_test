package com.neetomo.test.hadoop.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Reducer;

import java.util.Iterator;
import java.io.IOException;

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable outputValue = new IntWritable();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException{
        int sum = 0;

        for(IntWritable i : values) {
            sum += i.get();
        }
        outputValue.set(sum);
        context.write(key, outputValue);
    }
}
