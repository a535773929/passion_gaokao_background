package com.example.demo.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FinalAppointment {
    public int getAppointment_id() {
        return appointment_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public int getStatus() {
        return status;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public int getAppointment_type() {
        return appointment_type;
    }

    public String getAppointment_address() {
        return appointment_address;
    }

    public String getStudent_name() {
        return student_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getOrder_time() {
        return order_time;
    }

    private int appointment_id;
    private int student_id;
    private int group_id;
    private int status;
    private String appointment_time;
    private int appointment_type;
    private String appointment_address;
    private String student_name;
    private String group_name;
    private String order_time;

    public FinalAppointment(Appointment appointment) {
        this.appointment_id=appointment.getAppointment_id();
        this.appointment_address=appointment.getAppointment_address();
        this.student_id=appointment.getStudent_id();
        this.group_id=appointment.getGroup_id();
        this.status=appointment.getStatus();
        this.appointment_type=appointment.getAppointment_type();
        this.appointment_address=appointment.getAppointment_address();
        this.student_name=appointment.getStudent_name();
        this.group_name=appointment.getGroup_name();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        this.order_time=sdf1.format(appointment.getBuy_time());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (appointment.getPeriod()==0){
            this.appointment_time=sdf.format(appointment.getAppointment_time())+"上午";
        }
        else {
            this.appointment_time=sdf.format(appointment.getAppointment_time())+"下午";
        }
    }
}
