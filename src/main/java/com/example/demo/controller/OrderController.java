package com.example.demo.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.example.demo.entity.*;
import com.example.demo.exception.WrongTypeException;
import com.example.demo.service.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class OrderController {
    @Autowired
    AppointmentListService appointmentListService;
    @Autowired
    BindNumberService bindNumberService;
    @Autowired
    PrivateNumberServiceImpl axb1;
    @Autowired
    HiddenNumberServiceImpl axb;

    @GetMapping("/appointmentList")
    public ResponseData listAppointment(){
        JSONArray forms = appointmentListService.findAll();
        ResponseData rp = new ResponseData(forms,"success",200);
        return rp;
    }

    @RequestMapping("/order/{appointment_id}")
    public ResponseData confirm(@PathVariable("appointment_id")  int id,@RequestParam("status") int status){
        System.out.println(id);
            if(!(status==3||status==4)){
                ResponseData rp = new ResponseData("状态字不合法",416);
                return rp;
            }

            Boolean a = appointmentListService.confirm(id,status);
            if (a){


                    String axbUnbind= null;
                    XNumber xNumber = bindNumberService.findXNumber(id);
                    if (xNumber==null){
                        ResponseData rp = new ResponseData("订单状态已更新，但未能解绑虚拟号",400);
                        return rp;
                    }

                    axbUnbind = axb.axbUnbindNumber(xNumber.getxNumberSubscriptId(), xNumber.getX_number());

                    bindNumberService.updateExpertXNumber(xNumber);
                    ResponseData rp = new ResponseData(axbUnbind,200);
                    return rp;
            }else {
                ResponseData rp = new ResponseData("未能更新订单状态",400);
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
    public JSONObject confirmExpert(@PathVariable("appointment_id")  int appointment_id,@RequestParam("expert_id") int expert_id){
//        System.out.println(id);

        BindNumber bindNumber = bindNumberService.searchRealNumber(appointment_id,expert_id);
        if (bindNumber.getExpertPhone()==null){
            System.out.println(bindNumber.getExpertPhone());
            System.out.println(bindNumber.getStudentPhone());
            JSONObject result = JSONUtil.createObj();
            result.put("msg","未查询到专家电话");
            result.put("status",400);
            return result;
        }else if (bindNumber.getStudentPhone()==null){
            JSONObject result = JSONUtil.createObj();
            result.put("msg","未查询到学生电话");
            result.put("status",400);
            return result;
        }
        String expNumber = bindNumberService.getExpertHideNumber(expert_id);
        if(expNumber==null){
            JSONObject result = JSONUtil.createObj();
            result.put("msg","绑定专家成功，该专家无可用隐号");
            result.put("status",404);
            return result;
        }



        String usefulXNumber = expNumber.substring(0,14);
        System.out.println("userfulnumber:"+usefulXNumber);


        String A= null;
        try {
            String callerNum=bindNumber.getExpertPhone();
//        String callerNum="17777777777";
            String calleeNum=bindNumber.getStudentPhone();
//        String calleeNum="17777777778";
            System.out.println("expert:"+calleeNum);
            System.out.println("student:"+callerNum);
            /**
             * 功能日志记录
             */
            //第一步 分配隐私号
            /**
             * 根据 A 和 B 号，分配隐私号
             * @param a A 号
             * @param b B 号
             * @throws Exception 分配号码失败，注意有多种可能
             */
            String xNumber=axb1.numberMalloc(callerNum,calleeNum,usefulXNumber);
            // 第二步: 号码绑定,即调用AXB模式绑定接口
            A = axb.axbBindNumber(xNumber, callerNum, calleeNum);
            //String B=axb.axbBindNumber(xNumber, "+8612345678901", "+8612345678902");
            //    /**
            // 当用户发起通话时,隐私保护通话平台会将呼叫事件推送到商户应用,参考: HostingVoiceEventDemoImpl
            // 当用户使用短信功能,隐私保护通话平台将短信事件推送到商户应用,参考: HostingVoiceEventDemoImpl

            // 第x步: 用户通过隐私号码发起呼叫后,商户可随时终止一路呼叫,即调用终止呼叫接口
            // axb.axbStopCall("1200_366_0_20161228102743@callenabler.home1.com");

            // 第x步: 用户通话结束,若设置录音,则商户可以获取录音文件下载地址,即调用获取录音文件下载地址接口
            // axb.axbGetRecordDownloadLink("ostor.huawei.com", "1200_366_0_20161228102743.wav");


            // 第x步: 根据业务需求,可更改绑定关系,即调用AXB模式绑定信息修改接口
            // axb.axbModifyNumber(xNumberSubscriptId, xNumber, calleeNum);
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject result = JSONUtil.createObj();
            result.put("msg","服务器错误，绑定虚拟号失败");
            result.put("error",e.getMessage());
            result.put("status",500);
            return result;

        }


//        return "隐号功能已开通！"+A;
        System.out.println(A);
        JSONObject jsonObjectA=new JSONObject(A);
        System.out.println(jsonObjectA.get("resultdesc"));

//        JSONObject jsonObjectB=JSON.parseObject(A);
//        The number +8617138070945 has been bound.     Success
        if (!jsonObjectA.get("resultdesc").equals("Success")){
            JSONObject result = JSONUtil.createObj();
            result.put("msg","该专家无可用虚拟号");
            result.put("error",A);
            result.put("status",500);
            return result;
        }

//        jsonObject.get("relationNum");
//
//
//        String s1=a.toString();
//        String regex = "\\d{13}";
////        String s = "dgdg6534+12345678911112222dsfger";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher m = pattern.matcher(s1);
//        System.out.println(m.find());
//        System.out.println(m.group());
//        String s2 = "+"+m.group();
//        jsonObject.put("number",s2);
        String x_id=jsonObjectA.get("subscriptionId").toString();
        Boolean b =bindNumberService.setHideNumber(appointment_id,expert_id,usefulXNumber,x_id,expNumber);

        if (!b){
            JSONObject result = JSONUtil.createObj();
            result.put("x_number",usefulXNumber);
            result.put("msg","隐私号绑定成功但存储失败,详见Log文件");
            result.put("status",400);
            return result;
        }

        JSONObject result = JSONUtil.createObj();
        result.put("x_number",usefulXNumber);
        result.put("msg","成功绑定专家，隐号功能已开通！");
        result.put("status",200);
        return result;
    }

    @RequestMapping("/getExpert/{expert_id}")
    public String getExpert(@PathVariable("expert_id")  int expert_id){
        String phone = appointmentListService.findExpert(expert_id);
        return phone;

    }

}
