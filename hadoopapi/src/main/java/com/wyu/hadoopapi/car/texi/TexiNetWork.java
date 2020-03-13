package com.wyu.hadoopapi.car.texi;

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
 * Created by newuser on 2018/7/14.
 */
public class TexiNetWork {
    public static class TexiNetWorkMapper extends Mapper<LongWritable,Text,Texi,Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String lines = value.toString();
            String[] datas = lines.split("\\t");
            //if(datas.length==7){
            Texi texi = new Texi(datas[0], TimeUtils.getTime(datas[1]),datas[datas.length-3]);
            context.write(texi,new Text(datas[datas.length-1]));
            //}
        }
    }
    public static class TexiNetWorkReducer extends Reducer<Texi,Text,Text,Text> {
        @Override
        protected void reduce(Texi texi, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for(Text value:values){
                context.write(new Text(String.format("%s\t%s\t%s",texi.getVehicleId(),texi.getTime(),texi.getSpeed())),new Text(value));
            }
        }
    }
    public static void main(String[] args)throws Exception{
        Configuration config = new Configuration();
        Job job = Job.getInstance(config);

        job.setJarByClass(TexiNetWork.class);

        job.setMapperClass(TexiNetWorkMapper.class);
        job.setReducerClass(TexiNetWorkReducer.class);

        job.setMapOutputKeyClass(Texi.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path("c:\\gps.csv"));
        Path path = new Path("c:/handle_spg_out");
        FileSystem fs = FileSystem.get(config);
        if(fs.exists(path)){
            fs.delete(path,true);
        }
        FileOutputFormat.setOutputPath(job, path);
        System.exit(job.waitForCompletion(true)?0:1);



    }
}
