package com.example.demo.controller;

import cn.hutool.json.JSONObject;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class OrderController {
    @Autowired
    BindNumberService bindNumberService;
    @Autowired
    PrivateNumberServiceImpl axb1;
    @Autowired
    HiddenNumberServiceImpl axb;


    @RequestMapping("/setExpert/{appointment_id}")
    public ResponseData confirmExpert(@PathVariable("appointment_id")  int appointment_id,@RequestParam("expert_id") int expert_id){
//        System.out.println(id);

//        若学生无可用次数，拒绝操作
        int times = bindNumberService.getTimes(appointment_id);
        if(times<1){
            ResponseData rp = new ResponseData("用户无可用预约次数",400);
            return rp;
        }

        int status = bindNumberService.getStatus(appointment_id,times);
        if (status!=1){
            ResponseData rp = new ResponseData("当前状态不可预约专家",400);
            return rp;
        }

//      查询学生、专家真实号码
        BindNumber bindNumber = bindNumberService.searchRealNumber(appointment_id,expert_id);
        if (bindNumber.getExpertPhone()==null){
            System.out.println("expert:"+bindNumber.getExpertPhone()+"student:"+bindNumber.getStudentPhone());
            ResponseData rp = new ResponseData("未查询到专家电话",400);
            return rp;
        }else if (bindNumber.getStudentPhone()==null){
            ResponseData rp = new ResponseData("未查询到学生电话",400);
            return rp;
        }
        String expNumber = bindNumberService.getExpertHideNumber(expert_id);
        if(expNumber.length()<14){
            ResponseData rp = new ResponseData("该专家无可用虚拟号",404);
            return rp;
        }
//        System.out.println("expNumber"+expNumber.toString());

//        绑定虚拟号
        String usefulXNumber = expNumber.substring(0,14);
        System.out.println("userfulnumber:"+usefulXNumber);
        String A= null;
        try {
            String callerNum="+86"+bindNumber.getExpertPhone();
//        String callerNum="17777777777";
            String calleeNum="+86"+bindNumber.getStudentPhone();
//        String calleeNum="17777777778";
            System.out.println("expert:"+calleeNum+"student:"+callerNum);
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
            ResponseData rp = new ResponseData("服务器错误，绑定虚拟号失败"+e.getMessage(),500);
            return rp;
        }
        //        return "隐号功能已开通！"+A;
        System.out.println(A);
        JSONObject jsonObjectA=new JSONObject(A);
        System.out.println(jsonObjectA.get("resultdesc"));
        if (!jsonObjectA.get("resultdesc").equals("Success")){
            ResponseData rp = new ResponseData("绑定失败"+A,400);
            return rp;
        }

        String x_id=jsonObjectA.get("subscriptionId").toString();
        Boolean b =bindNumberService.setHideNumber(appointment_id,expert_id,usefulXNumber,x_id,expNumber,times);
        if (!b){
            ResponseData rp = new ResponseData("数据库存储失败,详见Log文件",400);
            return rp;
        }else {
            ResponseData rp = new ResponseData(usefulXNumber,200);
            return rp;
        }
    }



    @RequestMapping("/confirmOrder/{appointment_id}")
    public ResponseData confirm(@PathVariable("appointment_id")  int id){
        System.out.println("confirm appointment_id:"+id);

//        要求前端设置，只有status=2时才允许confirm！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！11
        int times = bindNumberService.getTimes(id);
        if(times<1){
            ResponseData rp = new ResponseData("用户无可用预约数",400);
            return rp;
        }

        int status = bindNumberService.getStatus(id,times);
        if (status!=2){
            ResponseData rp = new ResponseData("当前状态不可确认订单",400);
            return rp;
        }

//解绑虚拟号
        String axbUnbind= null;
        XNumber xNumber = bindNumberService.findXNumber(id,times);
        if (xNumber.getxNumberSubscriptId()==null){
            ResponseData rp = new ResponseData("未能解绑虚拟号",400);
            return rp;
        }
        axbUnbind = axb.axbUnbindNumber(xNumber.getxNumberSubscriptId(), xNumber.getX_number());
        if (!axbUnbind.equals("Success")){
            ResponseData rp = new ResponseData("未能解绑虚拟号,订单确认失败"+axbUnbind,400);
            return rp;
        }

//        更新专家可用号码
        Boolean a = bindNumberService.add2ExpertXNumber(xNumber);
        if (!a) {
            ResponseData rp = new ResponseData("更新专家号码失败",400);
            return rp;
        }

        Boolean b = bindNumberService.confirmOrder(id,times);
        if (a) {
            ResponseData rp = new ResponseData("订单确认成功",200);
            return rp;
        }else {
            ResponseData rp = new ResponseData("订单确认失败",400);
            return rp;
        }
    }

    @RequestMapping("/rejectOrder/{appointment_id}")
    public ResponseData reject(@PathVariable("appointment_id")  int id){
        System.out.println("reject appointment_id:"+id);

        int times = bindNumberService.getTimes(id);
        int status = bindNumberService.getStatus(id,times);
        if (status!=1){
            ResponseData rp = new ResponseData("当前状态不可拒绝",400);
            return rp;
        }

        Boolean a = bindNumberService.reject(id,times);
        if (a) {
            ResponseData rp = new ResponseData("订单拒绝成功",200);
            return rp;
        }else {
            ResponseData rp = new ResponseData("订单拒绝失败",400);
            return rp;
        }
    }
}
