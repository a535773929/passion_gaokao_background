package com.example.demo.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Expert;
import com.example.demo.entity.FinalAppointment;
import com.example.demo.entity.Student;
import com.example.demo.mapper.AppointmentMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentListService {
    @Autowired(required=false)
    AppointmentMapper appointmentMapper;

    public JSONArray findCurrentAppointment(){
        Date now = new Date();
        SimpleDateFormat todayFormat=new SimpleDateFormat("yyyy-MM-dd  00:00:00");
        String today = todayFormat.format(now);
        List<Appointment> appointmentsList = appointmentMapper.findCurrentAppointment(today);
        System.out.println(appointmentsList);
        JSONArray jsonList = JSONUtil.parseArray(appointmentsList);

        return jsonList;
    }

    public JSONArray findAll(){
        List<Appointment> appointmentsList = appointmentMapper.findAll();
        System.out.println(appointmentsList);
        JSONArray jsonList = JSONUtil.parseArray(appointmentsList);
        return jsonList;
    }


    public Student findInfo(int id){return appointmentMapper.findStudentInfo(id);}

    public JSONArray findByGroup(int id){

        String expertsId = appointmentMapper.findByGroup(id);
        List<String> expertsId2 = java.util.Arrays.asList(expertsId.split(","));
//        int[] expertsId3 = new int[expertsId2.length];
//        for (int i=0;i<expertsId.length();i++){
//            expertsId3[i]=Integer.parseInt(expertsId2[i]);
//        }
        List<Expert> experts = appointmentMapper.findExperts(expertsId2);
        JSONArray jsonList = JSONUtil.parseArray(experts);
        return jsonList;
    }

    @Transactional
    public Boolean setExpert(int appointment_id,int expert_id) {
        try {
            appointmentMapper.setExpert(appointment_id,expert_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
    public String findExpert(int id){return appointmentMapper.findExpert(id);}


}

