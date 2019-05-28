package com.example.demo.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.entity.Expert;
import com.example.demo.entity.ResponseData;
import com.example.demo.entity.Student;
import com.example.demo.exception.WrongTypeException;
import com.example.demo.service.AppointmentListService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @RequestMapping("/order/{appointment_id}")
    public ResponseData confirm(@PathVariable("appointment_id")  int id,@RequestParam("status") int status){
        System.out.println(id);
            if(!(status==2||status==3||status==4)){
                ResponseData rp = new ResponseData("状态字不合法",416);
                return rp;
            }
            Boolean a = appointmentListService.confirm(id,status);
            if (a){
                ResponseData rp = new ResponseData("success",200);
                return rp;
            }else {
                ResponseData rp = new ResponseData("failed",400);
                return rp;
            }

    }
    @GetMapping("/studentInfo/{student_id}")
    public JSONObject studentInfo(@PathVariable("student_id") int id){
        Student stu = appointmentListService.findInfo(id);
        JSONObject stuInfo = new JSONObject(stu);
        return stuInfo;
    }
    @GetMapping("/expertGroup/{group_id}")
    public ResponseData listExpert(@PathVariable("group_id") int id){
        JSONArray forms = appointmentListService.findByGroup(id);

        if (forms==null){
            ResponseData rp = new ResponseData("don't have the group",404);
            return rp;
        }
        ResponseData rp = new ResponseData(forms,"success",200);
        return rp;
    }
    @RequestMapping("/setExpert/{appointment_id}")
    public ResponseData confirmExpert(@PathVariable("appointment_id")  int appointment_id,@RequestParam("expert_id") int expert_id){
//        System.out.println(id);
           Boolean a = appointmentListService.setExpert(appointment_id,expert_id);
        if (a){
            ResponseData rp = new ResponseData("success",200);
            return rp;
        }else {
            ResponseData rp = new ResponseData("failed",400);
            return rp;
        }

    }

    @RequestMapping("/getExpert/{expert_id}")
    public String getExpert(@PathVariable("expert_id")  int expert_id){
        String phone = appointmentListService.findExpert(expert_id);
        return phone;

    }

}
