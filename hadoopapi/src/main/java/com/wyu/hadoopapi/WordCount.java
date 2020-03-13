package com.wyu.hadoopapi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by newuser on 2018/7/11.
 */
public class WordCount {
    public static class WordCountMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String lines = value.toString();
            String[] words = lines.split(",");
            for (String word:words){
                context.write(new Text(word),new IntWritable(1));
            }
        }
    }

    public static class WordCountReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
        /**
         * 
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value:values){
                int i = value.get();
                sum+=i;
            }
            context.write(key,new IntWritable(sum));
        }
    }

    public static void main(String[] args)throws Exception{
        Configuration config = new Configuration();
        Job job = Job.getInstance(config);

        job.setJarByClass(WordCount.class);

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, new Path("E:\\data.log"));
        Path path = new Path("E:\\out");
        FileSystem fs = FileSystem.get(config);
        if (fs.exists(path))
        {
            fs.delete(path,true);
        }
        FileOutputFormat.setOutputPath(job,path);
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
