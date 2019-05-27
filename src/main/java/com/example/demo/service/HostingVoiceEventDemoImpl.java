package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Bean.HiddenNumberBean;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class HostingVoiceEventDemoImpl {

    private Logger logger = Logger.getLogger(HostingVoiceEventDemoImpl.class);
        /**
         * 呼叫事件 for AXB
         *
         * @param jsonBody
         * @breif 详细内容以接口文档为准
         */
        public HiddenNumberBean onCallEvent(JSONObject jsonBody) {
            // 封装JOSN请求
            PropertyConfigurator.configure("C:\\Users\\gjx\\Desktop\\gaokao\\neilinker-mobile\\neilinker-mobile\\neilinker-mobile-svc\\neilinker-mobile-impl\\log4j.properties");
            String eventType = jsonBody.getString("eventType"); // 通知事件类型

            if ("fee".equalsIgnoreCase(eventType)) {
                HiddenNumberBean bean = new HiddenNumberBean();
                logger.info("EventType error: " + eventType);
                bean.setEventError("EventType error: " + eventType);
                return bean;
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
                logger.info(bean);
                return bean;

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
                logger.info(bean);
                return bean;
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
                logger.info(bean);

                return bean;
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
                logger.info(bean);
                return bean;
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
                logger.info(bean);
                return bean;
            }
            return null;
        }
}
