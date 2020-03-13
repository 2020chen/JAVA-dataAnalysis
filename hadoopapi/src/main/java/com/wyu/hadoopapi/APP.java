package com.wyu.hadoopapi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by newuser on 2018/7/12.
 */
public class APP {
    public static class PhoneFlowMapper extends Mapper<LongWritable,Text,Text,Phone>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String lines = value.toString();
            String[] words = lines.split("\\t");
            String phoneNumber = words[0];
            int upFlow = Integer.parseInt(words[1]);
            int downFlow = Integer.parseInt(words[2]);
            Phone phone = new Phone(downFlow,upFlow);
            context.write(new Text(phoneNumber),phone);
        }
    }

    public static class PhoneFlowReducer extends Reducer<Text,Phone,Text,Text>{
        /**
         *
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<Phone> values, Context context) throws IOException, InterruptedException {
           int upFlow = 0;
            int downFlow = 0;
            for (Phone phone:values){
               upFlow += phone.getUpFlow();
               downFlow += phone.getDownFlow();
           }
            context.write(key,new Text(String.format("%d\t%d",upFlow,downFlow)));
        }
    }
    public static void main(String[] args) throws Exception{
        Configuration config = new Configuration();
        Job job = Job.getInstance(config);

        job.setJarByClass(WordCount.class);

        job.setMapperClass(PhoneFlowMapper.class);
        job.setReducerClass(PhoneFlowReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Phone.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path("E:\\out_phone"));
        Path path = new Path("E:\\handle_out_phone");
        FileSystem fs = FileSystem.get(config);
        if(fs.exists(path)){
            fs.delete(path,true);
        }
        FileOutputFormat.setOutputPath(job,path);
        System.exit(job.waitForCompletion(true)?0:1);

    }
}
