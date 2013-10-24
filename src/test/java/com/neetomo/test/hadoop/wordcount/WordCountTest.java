package com.neetomo.test.hadoop.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Mapper;

import org.junit.Test;

import org.apache.hadoop.mrunit.mapreduce.MapDriver;

public class WordCountTest {
    @Test
    public void mapperTest() throws Exception {
        Mapper<LongWritable, Text, Text, IntWritable> mapper = new WordCountMapper();
        MapDriver<LongWritable, Text, Text, IntWritable> driver = 
            new MapDriver<LongWritable, Text, Text, IntWritable>(mapper);

        driver.withInput(new LongWritable(1l), new Text("This is a pen"));
        IntWritable value = new IntWritable(1);
        driver.withOutput(new Text("This"), value);
        driver.withOutput(new Text("is"), value);
        driver.withOutput(new Text("a"), value);
        driver.withOutput(new Text("pen"), value);
        driver.runTest();
    }
}
