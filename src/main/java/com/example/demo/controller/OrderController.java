package com.example.demo.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.exception.WrongTypeException;
import com.example.demo.service.AppointmentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @Autowired
    AppointmentListService appointmentListService;

    @GetMapping("/appointmentList")
    public JSONArray listAppointment(){
            JSONArray forms = appointmentListService.findAll();
            return forms;
    }

    @PutMapping("/order/{appointment_id}")
    public JSONObject confirm(@PathVariable("appointment_id")  int id,int confirmType){
        JSONObject result = JSONUtil.createObj();
        try {
            if(!(confirmType==1||confirmType==2)){
                throw new WrongTypeException();
            }
            appointmentListService.confirm(id,confirmType);
            result.put("msg","成功");
            result.put("status",200);
            return result;
        } catch (WrongTypeException e) {
            e.printStackTrace();
            result.put("msg","修改状态字错误");
            result.put("status",416);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            result.put("msg","未知错误");
            result.put("status",400);
            return result;
        }
    }

}
