package com.example.demo.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.FinalAppointment;
import com.example.demo.mapper.AppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentListService {
    @Autowired(required=false)
    AppointmentMapper appointmentMapper;

    public JSONArray findAll(){
        Date now = new Date();
        SimpleDateFormat todayFormat=new SimpleDateFormat("yyyy-MM-dd  00:00:00");
        String today = todayFormat.format(now);
        List<Appointment> appointmentsList = appointmentMapper.findAll(today);
//        ArrayList<FinalAppointment> finalAppointments = new ArrayList<>();
//        for(Appointment a:appointmentsList){
//            FinalAppointment f = new FinalAppointment(a);
//            finalAppointments.add(f);
//        }
//        JSONArray jsonList = JSONUtil.parseArray(finalAppointments);
//        System.out.println(appointmentsList);
        JSONArray jsonList = JSONUtil.parseArray(appointmentsList);

        return jsonList;
    }

    @Transactional
    public int confirm(int id,int confirmType) {return appointmentMapper.confirm(id,confirmType);}
}

