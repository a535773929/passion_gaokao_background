package com.example.demo.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment {
    private int appointment_id;
    private int student_id;

    private int group_id;
    private int status;
    private String appointment_time;
    private int appointment_type;
    private String appointment_address;
    private String student_name;
    private String group_name;
    private String buy_time;
    private int period;
    private String expert_name;
    private int expert_id;
    private String x_number;
    private int times;

    private int status2;
    private String appointment_time2;
    private int period2;
    private String x_number2;

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(Date appointment_time) {
        SimpleDateFormat appointTimeFormat=new SimpleDateFormat("yyyy-MM-dd");
        this.appointment_time = appointTimeFormat.format(appointment_time);
    }

    public int getAppointment_type() {
        return appointment_type;
    }

    public void setAppointment_type(int appointment_type) {
        this.appointment_type = appointment_type;
    }

    public String getAppointment_address() {
        return appointment_address;
    }

    public void setAppointment_address(String appointment_address) {
        this.appointment_address = appointment_address;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(Date buy_time) {
        SimpleDateFormat butimeFormat=new SimpleDateFormat("yyyy-MM-dd");
        this.buy_time = butimeFormat.format(buy_time);
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getExpert_name() {
        return expert_name;
    }

    public void setExpert_name(String expert_name) {
        this.expert_name = expert_name;
    }

    public int getExpert_id() {
        return expert_id;
    }

    public void setExpert_id(int expert_id) {
        this.expert_id = expert_id;
    }

    public String getX_number() {
        return x_number;
    }

    public void setX_number(String x_number) {
        this.x_number = x_number;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }



    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public int getStatus2() {
        return status2;
    }

    public void setStatus2(int status2) {
        this.status2 = status2;
    }

    public String getAppointment_time2() {
        return appointment_time2;
    }

    public void setAppointment_time2(String appointment_time2) {
        this.appointment_time2 = appointment_time2;
    }

    public int getPeriod2() {
        return period2;
    }

    public void setPeriod2(int period2) {
        this.period2 = period2;
    }

    public String getX_number2() {
        return x_number2;
    }

    public void setX_number2(String x_number2) {
        this.x_number2 = x_number2;
    }



}
