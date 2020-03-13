package com.wyu.hadoopapi.car;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by newuser on 2018/7/13.
 */
public class Car implements WritableComparable<Car> {
    private String VenicleId;
    private String time;

    public Car() {
    }

    public Car(String venicleId, String time) {
        VenicleId = venicleId;
        this.time = time;
    }

    public String getVenicleId() {
        return VenicleId;
    }

    public void setVenicleId(String venicleId) {
        VenicleId = venicleId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("%s\t%s",getVenicleId(),getTime());
    }

    public int compareTo(Car car) {
        return this.getVenicleId().compareTo(car.getVenicleId());
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(VenicleId);
        dataOutput.writeUTF(time);
    }

    public void readFields(DataInput dataInput) throws IOException {
        VenicleId = dataInput.readUTF();
        time = dataInput.readUTF();
    }
}
