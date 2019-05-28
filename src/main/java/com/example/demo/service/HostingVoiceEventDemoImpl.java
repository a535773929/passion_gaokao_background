package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Bean.HiddenNumberBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.validation.constraints.Null;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class HostingVoiceEventDemoImpl {

    private Logger logger = LogManager.getLogger(HostingVoiceEventDemoImpl.class);
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
                logger.info("呼叫时间"+bean.getPrivacy_platform_time());
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
                logger.info("呼出时间"+bean.getPrivacy_platform_time());
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
                logger.info("振铃时间"+bean.getPrivacy_platform_time());

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
                logger.info("应答时间"+bean.getPrivacy_platform_time());
                this.time=bean.getPrivacy_platform_time();
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
                logger.info(bean.getPrivacy_platform_time());
                System.out.println(this.time);
                if (this.time!=null && this.time!="结束！"){
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d1=sdf.parse(this.time);
                Date d2=sdf.parse(bean.getPrivacy_platform_time());
                long nd = 1000 * 24 * 60 * 60;
                long nh = 1000 * 60 * 60;
                long nm = 1000 * 60;
                long diff = d2.getTime() - d1.getTime();
                long day = diff / nd;
                long hour = diff % nd / nh;
                long min = diff % nd % nh / nm;
                logger.info(sessionId+":"+day + "天" + hour + "小时" + min + "分钟");
                    return "结束！";}else{logger.info("对方已挂断");}
            }
            return null;
        }
}
