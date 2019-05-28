package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("hiddenNumberService")
public class HiddenNumberServiceImpl implements HiddenNumberService {

        public static String  subscriptionId;
        private Logger logger = LogManager.getLogger(HiddenNumberServiceImpl.class);

        private String appKey="ui9B5arvF2vxzvNbr3BGSA6fWDz0"; // APP_Key;
        private String appSecret="Z3MZkzEo6HEPxq40wl4jHl5b4PbE"; // APP_Secret;
        private String ompDomainName="https://rtcapi.cn-north-1.myhuaweicloud.com:12543"; // APP接入地址;
        private String buildOmpUrl(String path) {
            return ompDomainName + path;
        }
        @Override
        public String axbBindNumber(String relationNum, String callerNum, String calleeNum) {
            try{
            if (StringUtils.isBlank(relationNum) || StringUtils.isBlank(callerNum) || StringUtils.isBlank(calleeNum)) {
                logger.error("axbBindNumber set params error");
                return "Bind Number Error";
            }


            String url = "/rest/caas/relationnumber/partners/v1.0";
            String realUrl = buildOmpUrl(url);


            JSONObject json = new JSONObject();
            json.put("relationNum", relationNum); // X号码(关系号码)
            json.put("callerNum", callerNum); // A方真实号码(手机或固话)
            json.put("calleeNum", calleeNum); // B方真实号码(手机或固话)


//         json.put("areaCode", "0755"); //城市码
//         json.put("callDirection", 0); //允许呼叫的方向
//         json.put("duration", 86400); //绑定关系保持时间
//         json.put("recordFlag", "false"); //是否通话录音
//         json.put("recordHintTone", "recordHintTone.wav"); //录音提示音
//         json.put("maxDuration", 60); //单次通话最长时间
//         json.put("lastMinVoice", "lastMinVoice.wav"); //通话最后一分钟提示音
//         json.put("privateSms", "true"); //是否支持短信功能
//         JSONObject preVoice = new JSONObject();
//         preVoice.put("callerHintTone", "callerHintTone.wav"); //设置A拨打X号码时的通话前等待音
//         preVoice.put("calleeHintTone", "calleeHintTone.wav"); //设置B拨打X号码时的通话前等待音
//         json.put("preVoice", preVoice); //个性化通话前等待音

            String result = HttpUtil.sendPost(appKey, appSecret, realUrl, json.toString());
            logger.debug("Response is :" + result);
            return result;}
            catch (Exception e)
            {
                logger.error(e);
                return null;
            }

        }

        @Override
        public void axbModifyNumber(String subscriptionId, String callerNum, String calleeNum) {
            try{

            if (StringUtils.isBlank(subscriptionId)) {
                logger.error("axbModifyNumber set params error");
                return;
            }


            String url = "/rest/caas/relationnumber/partners/v1.0";
            String realUrl = buildOmpUrl(url);


            JSONObject json = new JSONObject();
            json.put("subscriptionId", subscriptionId);
            if (StringUtils.isNotBlank(callerNum)) {
                json.put("callerNum", callerNum);
            }
            if (StringUtils.isNotBlank(calleeNum)) {
                json.put("calleeNum", calleeNum);
            }


//         json.put("callDirection", 0); //允许呼叫的方向
//         json.put("duration", 86400); //绑定关系保持时间
//         json.put("maxDuration", 90); //单次通话最长时间
//         json.put("lastMinVoice", "lastMinVoice.wav"); //通话最后一分钟提示音
//         json.put("privateSms", "true"); //是否支持短信功能
//         JSONObject preVoice = new JSONObject();
//         preVoice.put("callerHintTone", "callerHintTone.wav"); //设置A拨打X号码时的通话前等待音
//         preVoice.put("calleeHintTone", "calleeHintTone.wav"); //设置B拨打X号码时的通话前等待音
//         json.put("preVoice", preVoice); //个性化通话前等待音

            String result = HttpUtil.sendPut(appKey, appSecret, realUrl, json.toString());
            logger.debug("Response is :" + result);}
            catch(Exception e)
            {
                logger.error(e);
            }
        }

        @Override
        public String axbUnbindNumber(String subscriptionId, String relationNum) {

            try{
            if (StringUtils.isBlank(subscriptionId) && StringUtils.isBlank(relationNum)) {
                logger.error("axbUnbindNumber set params error");
                return "axbUnbindNumber set params error";
            }


            String url = "/rest/caas/relationnumber/partners/v1.0";
            String realUrl = buildOmpUrl(url);


            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(subscriptionId)) {
                map.put("subscriptionId", subscriptionId);
            }
            if (StringUtils.isNotBlank(relationNum)) {
                map.put("relationNum", relationNum);
            }

            String result = HttpUtil.sendDelete(appKey, appSecret, realUrl, HttpUtil.map2UrlEncodeString(map));
            JSONObject json = JSON.parseObject(result);
            String resultdesc=json.get("resultdesc").toString();
            logger.debug("Response is :" + result);
            return resultdesc;}
            catch (Exception e){
                logger.error(e);
                return null;
            }

        }

        @Override
        public String axbQueryBindRelation(String subscriptionId, String relationNum) {
            try{

            if (StringUtils.isBlank(subscriptionId) && StringUtils.isBlank(relationNum)) {
                logger.error("axbQueryBindRelation set params error");
                return "axbQueryBindRelation set params error";
            }


            String url = "/rest/caas/relationnumber/partners/v1.0";
            String realUrl = buildOmpUrl(url);


            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(subscriptionId)) {
                map.put("subscriptionId", subscriptionId);
            }
            if (StringUtils.isNotBlank(relationNum)) {
                map.put("relationNum", relationNum);
            }

            // map.put("pageIndex", 1); //查询的分页索引,从1开始编号
            // map.put("pageSize", 20); //查询的分页大小,即每次查询返回多少条数据

            String result = HttpUtil.sendGet(appKey, appSecret, realUrl, HttpUtil.map2UrlEncodeString(map));
            JSONObject json = JSONObject.parseObject(result);
            String jsonMessage = json.get("relationNumList").toString();
            logger.debug(result);
            return jsonMessage;}
            catch(Exception e){
                logger.error(e);
                return null;
            }
        }

        @Override
        public void axbGetRecordDownloadLink(String recordDomain, String fileName) {
            try{

            if (StringUtils.isBlank(recordDomain) || StringUtils.isBlank(fileName)) {
                logger.error("axbGetRecordDownloadLink set params error");
                return;
            }

            String url = "/rest/provision/voice/record/v1.0";
            String realUrl = buildOmpUrl(url);


            Map<String, Object> map = new HashMap<String, Object>();
            map.put("recordDomain", recordDomain);
            map.put("fileName", fileName);

            String result = HttpUtil.sendGet(appKey, appSecret, realUrl, HttpUtil.map2UrlEncodeString(map));
            logger.debug("Response is :" + result);}
            catch(Exception e)
            {
                logger.error(e);

            }
        }

        @Override
        public void axbStopCall(String sessionid) {
            try{

            if (StringUtils.isBlank(sessionid)) {
                logger.error("axbStopCall set params error");
                return;
            }


            String url = "/rest/httpsessions/callStop/v2.0";
            String realUrl = buildOmpUrl(url);


            JSONObject json = new JSONObject();
            json.put("sessionid", sessionid);
            json.put("signal", "call_stop");

            String result = HttpUtil.sendPost(appKey, appSecret, realUrl, json.toString());
            logger.debug("Response is :" + result);
        }
        catch(Exception e){
            logger.error(e);

            }
        }


}
