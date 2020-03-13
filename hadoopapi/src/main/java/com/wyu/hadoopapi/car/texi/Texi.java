package com.wyu.hadoopapi.car.texi;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by newuser on 2018/7/14.
 */
public class Texi implements WritableComparable<Texi>{
    private String VehicleId;
    private String time;
    private String speed;

    public Texi() {
    }

    public String getVehicleId() {
        return VehicleId;
    }

    public void setVehicleId(String vehicleId) {
        VehicleId = vehicleId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public Texi(String vehicleId, String time, String speed) {
        VehicleId = vehicleId;
        this.time = time;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return String.format("%s\t%s\t%s",getVehicleId(),getTime(),getSpeed());
    }

    public int compareTo(Texi texi) {
        return this.getVehicleId().compareTo(texi.getVehicleId());
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(VehicleId);
        dataOutput.writeUTF(time);
        dataOutput.writeUTF(speed);
    }

    public void readFields(DataInput dataInput) throws IOException {
        VehicleId = dataInput.readUTF();
        time = dataInput.readUTF();
        speed = dataInput.readUTF();
    }
}
