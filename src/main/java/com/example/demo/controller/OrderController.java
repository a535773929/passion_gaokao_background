package com.example.demo.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.entity.ResponseData;
import com.example.demo.exception.WrongTypeException;
import com.example.demo.service.AppointmentListService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @Autowired
    AppointmentListService appointmentListService;

    @GetMapping("/appointmentList")
    public ResponseData listAppointment(){
        JSONArray forms = appointmentListService.findAll();
        ResponseData rp = new ResponseData(forms,"success",200);
        return rp;
    }

    @PutMapping("/order/{appointment_id}")
    public ResponseData confirm(@PathVariable("appointment_id")  int id,int status){
        System.out.println(id);
        try {
            if(!(status==2||status==3||status==4)){
                throw new WrongTypeException();
            }
            appointmentListService.confirm(id,status);
            ResponseData rp = new ResponseData("success",200);
            return rp;
        } catch (WrongTypeException e) {
            e.printStackTrace();
            ResponseData rp = new ResponseData("状态字不合法",416);
            return rp;
        }catch (Exception e) {
            e.printStackTrace();
            ResponseData rp = new ResponseData("UnknownException",400);
            return rp;
        }
    }

}
