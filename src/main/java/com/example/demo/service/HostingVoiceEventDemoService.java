package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Bean.HiddenNumberBean;
import com.example.demo.mapper.CallingRecordMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.mapper.CallingRecordMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class HostingVoiceEventDemoService {
    @Autowired(required = false)
    CallingRecordMapper callingRecordMapper;

       private Logger logger = LogManager.getLogger(HostingVoiceEventDemoService.class);
        /**
         * 呼叫事件 for AXB
         *
         * @param jsonBody
         * @breif 详细内容以接口文档为准
         */
        String time= null;

        public String onCallEvent(JSONObject jsonBody,String data_time) throws ParseException{
            this.time=data_time;
            // 封装JOSN请求
            String eventType = jsonBody.getString("eventType"); // 通知事件类型
            if ("fee".equalsIgnoreCase(eventType)) {
                HiddenNumberBean bean = new HiddenNumberBean();
                logger.info("EventType error: " + eventType);
                bean.setEventError("EventType error: " + eventType);
                return "结束！";
            }

            JSONObject statusInfo = jsonBody.getJSONObject("statusInfo"); // 呼叫状态事件信息

            logger.info("eventType: " + eventType); // 打印通知事件类型
            //callin：呼入事件
            if ("callin".equalsIgnoreCase(eventType)) {

                /**
                 * Example: 此处以解析notifyMode为例,请按需解析所需参数并自行实现相关处理
                 *
                 * 'timestamp': 呼叫事件发生时隐私保护通话平台的UNIX时间戳
                 * 'sessionId': 通话链路的标识ID
                 * 'caller': 主叫号码
                 * 'called': 被叫号码
                 * 'subscriptionId': 绑定关系ID,AXE/X模式不携带该参数
                 * 'notifyMode': 通知模式,仅X模式场景携带
                 */
                HiddenNumberBean bean = new HiddenNumberBean();
                String sessionId = statusInfo.getString("sessionId");
                String subscriptionId=statusInfo.getString("subscriptionId");
                String startTime = statusInfo.getString("timestamp");
                String caller = statusInfo.getString("caller");
                String called = statusInfo.getString("called");

                logger.info("sessionId: " + sessionId);
                bean.setUuid(subscriptionId);
                bean.setA(caller);
                bean.setPrivacy_platform_time(startTime);
                bean.setB(called);
                bean.setEventDesc("callin");
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d2=sdf.parse(bean.getPrivacy_platform_time());
                long rightTime = d2.getTime()+8*60*60*1000;
                String rightTime2 = sdf.format(rightTime);
                logger.info("呼入时间:"+rightTime2);

                return "结束！";

            }

            //callout：呼出事件
            if ("callout".equalsIgnoreCase(eventType)) {
                /**
                 * Example: 此处以解析sessionId为例,请按需解析所需参数并自行实现相关处理
                 *
                 * 'timestamp': 呼叫事件发生时隐私保护通话平台的UNIX时间戳
                 * 'userData': 用户附属信息,仅X模式场景携带
                 * 'sessionId': 通话链路的标识ID
                 * 'caller': 主叫号码
                 * 'called': 被叫号码
                 * 'subscriptionId': 绑定关系ID
                 */
                HiddenNumberBean bean = new HiddenNumberBean();
                String sessionId = statusInfo.getString("sessionId");
                String subscriptionId=statusInfo.getString("subscriptionId");
                String startTime = statusInfo.getString("timestamp");
                String caller = statusInfo.getString("caller");
                String called = statusInfo.getString("called");
                logger.info("sessionId: " + sessionId);
                bean.setUuid(subscriptionId);
                bean.setA(caller);
                bean.setPrivacy_platform_time(startTime);
                bean.setB(called);
                bean.setEventDesc("callout");
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d2=sdf.parse(bean.getPrivacy_platform_time());
                long rightTime = d2.getTime()+8*60*60*1000;
                String rightTime2 = sdf.format(rightTime);
                logger.info("呼出时间:"+rightTime2);
                return "结束！";
            }
            //alerting：振铃事件
            if ("alerting".equalsIgnoreCase(eventType)) {
                /**
                 * Example: 此处以解析sessionId为例,请按需解析所需参数并自行实现相关处理
                 *
                 * 'timestamp': 呼叫事件发生时隐私保护通话平台的UNIX时间戳
                 * 'userData': 用户附属信息,仅X模式场景携带
                 * 'sessionId': 通话链路的标识ID
                 * 'caller': 主叫号码
                 * 'called': 被叫号码
                 * 'subscriptionId': 绑定关系ID
                 */
                HiddenNumberBean bean = new HiddenNumberBean();
                String sessionId = statusInfo.getString("sessionId");
                String subscriptionId=statusInfo.getString("subscriptionId");
                String startTime = statusInfo.getString("timestamp");
                String caller = statusInfo.getString("caller");
                String called = statusInfo.getString("called");
                logger.info("sessionId: " + sessionId);
                bean.setUuid(subscriptionId);
                bean.setA(caller);
                bean.setPrivacy_platform_time(startTime);
                bean.setB(called);
                bean.setEventDesc("alerting");
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d2=sdf.parse(bean.getPrivacy_platform_time());
                long rightTime = d2.getTime()+8*60*60*1000;
                String rightTime2 = sdf.format(rightTime);
                logger.info("振铃时间:"+rightTime2);
                return "结束！";
            }
            //answer：应答事件
            if ("answer".equalsIgnoreCase(eventType)) {
                /**
                 * Example: 此处以解析sessionId为例,请按需解析所需参数并自行实现相关处理
                 *
                 * 'timestamp': 呼叫事件发生时隐私保护通话平台的UNIX时间戳
                 * 'userData': 用户附属信息,仅X模式场景携带
                 * 'sessionId': 通话链路的标识ID
                 * 'caller': 主叫号码
                 * 'called': 被叫号码
                 * 'subscriptionId': 绑定关系ID
                 */
                HiddenNumberBean bean = new HiddenNumberBean();
                String sessionId = statusInfo.getString("sessionId");
                String subscriptionId=statusInfo.getString("subscriptionId");
                String startTime = statusInfo.getString("timestamp");
                String caller = statusInfo.getString("caller");
                String called = statusInfo.getString("called");
                logger.info("sessionId: " + sessionId);
                bean.setUuid(subscriptionId);
                bean.setA(caller);
                bean.setPrivacy_platform_time(startTime);
                bean.setB(called);
                bean.setEventDesc("answer");
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d2=sdf.parse(bean.getPrivacy_platform_time());
                long rightTime = d2.getTime()+8*60*60*1000;
                String rightTime2 = sdf.format(rightTime);
                logger.info("应答时间:"+rightTime2);

                this.time=rightTime2;
                return this.time;
            }
            //disconnect：挂机事件
            if ("disconnect".equalsIgnoreCase(eventType)) {
                /**
                 * Example: 此处以解析sessionId为例,请按需解析所需参数并自行实现相关处理
                 *
                 * 'timestamp': 呼叫事件发生时隐私保护通话平台的UNIX时间戳
                 * 'userData': 用户附属信息,仅X模式场景携带
                 * 'sessionId': 通话链路的标识ID
                 * 'caller': 主叫号码
                 * 'called': 被叫号码
                 * 'stateCode': 通话挂机的原因值
                 * 'stateDesc': 通话挂机的原因值的描述
                 * 'subscriptionId': 绑定关系ID
                 */
                HiddenNumberBean bean = new HiddenNumberBean();
                String sessionId = statusInfo.getString("sessionId");
                String subscriptionId=statusInfo.getString("subscriptionId");
                String startTime = statusInfo.getString("timestamp");
                String caller = statusInfo.getString("caller");
                String called = statusInfo.getString("called");
                String stateDesc = statusInfo.getString("stateDesc");
                logger.info("sessionId: " + sessionId);
                bean.setUuid(subscriptionId);
                bean.setA(caller);
                bean.setPrivacy_platform_time(startTime);
                bean.setB(called);
                bean.setEventDesc(stateDesc);
                SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d3=sdf1.parse(bean.getPrivacy_platform_time());
                long rightTime3 = d3.getTime()+8*60*60*1000;
                String rightTime4 = sdf1.format(rightTime3);
                logger.info("挂断时间:"+rightTime4);
                System.out.println(this.time);
                if (this.time!=null && this.time!="结束！"){
                    SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d1=sdf.parse(this.time);
                    Date d2=sdf.parse(bean.getPrivacy_platform_time());
                    long rightTime = d2.getTime()+8*60*60*1000;
                    String rightTime2 = sdf.format(rightTime);
                    logger.info("挂断时间:"+rightTime2);
                    long nd = 1000 * 24 * 60 * 60;
                    long nh = 1000 * 60 * 60;
                    long nm = 1000 * 60;
                    long ns = 1000 * 1;
                    long diff = rightTime - d1.getTime();
                    long day = diff / nd;
                    long hour = diff % nd / nh;
                    long min = diff % nd % nh / nm;
                    long s = diff % nd % nh % nm /ns;
                    logger.info(sessionId+":"+day + "天" + hour + "小时" + min + "分钟"+s+"秒");
                    String callingTime = "拨打时间： "+rightTime2+" 通话时长："+Long.toString(hour)+":h "+Long.toString(min)+":min "+Long.toString(s)+":s";
                    System.out.println("callingTime:"+callingTime);

//                    Calendar myCalendar = new GregorianCalendar(2014, 2, 11);
                    try {
                        callingRecordMapper.recordCalling(subscriptionId,callingTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info(sessionId+"记录写入数据库失败"+subscriptionId);
                    }
                    return "结束！";}else{logger.info("对方已挂断");}
            }
            return null;
        }
}
