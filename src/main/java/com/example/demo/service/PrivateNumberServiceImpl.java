package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Bean.HiddenNumberBean;
import com.example.demo.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("privateNumberService")
public  class PrivateNumberServiceImpl implements PrivateNumberService {
    String event=null;
    private Logger logger = LogManager.getLogger(PrivateNumberServiceImpl.class);
    private String appKey="ui9B5arvF2vxzvNbr3BGSA6fWDz0"; // APP_Key;
    private String appSecret="Z3MZkzEo6HEPxq40wl4jHl5b4PbE"; // APP_Secret;
    private String ompDomainName="https://rtcapi.cn-north-1.myhuaweicloud.com:12543"; // APP接入地址;
    private String buildOmpUrl(String path) {
        return ompDomainName + path;
    }
    String id= HiddenNumberServiceImpl.subscriptionId;
    @Override
   public String numberMalloc(String callerNum, String calleeNum, String userfulXNumber )
{

    try{
    String s=userfulXNumber;
    return s;}
    catch(Exception e){
        logger.error(e);
        return null;
    }

}
   public HiddenNumberBean querySelect(String subscriptionId) {
       HiddenNumberBean bean = new HiddenNumberBean();
       try {
           if (StringUtils.isBlank(subscriptionId)) {

               logger.error("axbQueryBindRelation set params error");
               bean.setEventError("axbQueryBindRelation set params error");
               return bean;
           }
           String url = "/rest/caas/relationnumber/partners/v1.0";
           String realUrl = buildOmpUrl(url);
           Map<String, Object> map = new HashMap<String, Object>();
           if (StringUtils.isNotBlank(subscriptionId)) {
               map.put("subscriptionId", subscriptionId);
           }
           // map.put("pageIndex", 1); //查询的分页索引,从1开始编号
           // map.put("pageSize", 20); //查询的分页大小,即每次查询返回多少条数据
           String result = HttpUtil.sendGet(appKey, appSecret, realUrl, HttpUtil.map2UrlEncodeString(map));
           JSONObject json = JSONObject.parseObject(result);
           String callStr = json.get("relationNumList").toString();
           String jsonfirst = callStr.replace("[", "");
           String jsonsecond = jsonfirst.replace("]", "");
           JSONObject json2 = JSONObject.parseObject(jsonsecond);
           String uuid = json2.get("subscriptionId").toString();
           String callerStr = json2.get("callerNum").toString();
           String calleeStr = json2.get("calleeNum").toString();
           String relationNum = json2.get("relationNum").toString();

//            JSONObject jsonMessage1 = JSONObject.parseObject(jsonMessage);
//            String jsonMessage2 = json.get("relationNumList").toString();
           logger.debug("Response is :" + result);
           bean.setUuid(uuid);
           bean.setA(callerStr);
           bean.setB(calleeStr);
           bean.setX(relationNum);
           return bean;
       } catch (Exception e) {
           logger.error(e);
           return null;
       }
   }
   public HiddenNumberBean querynNumberSelect(String a, String b)
   {
       HiddenNumberBean beanAB = new HiddenNumberBean();
       try {
           if (StringUtils.isBlank(a)) {

               logger.error("axbQueryBindRelation set params error");
               beanAB.setEventError("axbQueryBindRelation set params error");
               return beanAB;
           }
           if (StringUtils.isBlank(b)) {

               logger.error("axbQueryBindRelation set params error");
               beanAB.setEventError("axbQueryBindRelation set params error");
               return beanAB;
           }
           String url = "/rest/caas/relationnumber/partners/v1.0";
           String realUrl = buildOmpUrl(url);
           Map<String, Object> map = new HashMap<String, Object>();
           if (StringUtils.isNotBlank(a)) {
               map.put("callerNum", a);
           }
           if (StringUtils.isNotBlank(b)) {
               map.put("calleeNum", b);
           }
           String result = HttpUtil.sendGet(appKey, appSecret, realUrl, HttpUtil.map2UrlEncodeString(map));
           JSONObject json = JSONObject.parseObject(result);
           String callStr = json.get("relationNumList").toString();
           String jsonfirst = callStr.replace("[", "");
           String jsonsecond = jsonfirst.replace("]", "");
           JSONObject json2 = JSONObject.parseObject(jsonsecond);
           String uuid = json2.get("subscriptionId").toString();
           String callerStr = json2.get("callerNum").toString();
           String calleeStr = json2.get("calleeNum").toString();
           logger.debug("Response is :" + result);
           beanAB.setUuid(uuid);
           beanAB.setA(callerStr);
           beanAB.setB(calleeStr);
           return beanAB;
       } catch (Exception e) {
           logger.error(e);
           return null;
       }
   }
   public List<HiddenNumberBean> queryByX(String relationNum)
   {

       List<HiddenNumberBean> bean = new ArrayList<HiddenNumberBean>();
       try {
           if (StringUtils.isBlank(relationNum)) {
               HiddenNumberBean beanError = new HiddenNumberBean();
               logger.error("axbQueryBindRelation set params error");
               beanError.setEventError("axbQueryBindRelation set params error");
               bean.add(beanError);
               return bean;
           }


           String url = "/rest/caas/relationnumber/partners/v1.0";
           String realUrl = buildOmpUrl(url);
           Map<String, Object> map = new HashMap<String, Object>();
           if (StringUtils.isNotBlank(relationNum)) {
               map.put("relationNum", relationNum);
           }

           // map.put("pageIndex", 1); //查询的分页索引,从1开始编号
           // map.put("pageSize", 20); //查询的分页大小,即每次查询返回多少条数据

           String result = HttpUtil.sendGet(appKey, appSecret, realUrl, HttpUtil.map2UrlEncodeString(map));
           JSONObject json = JSONObject.parseObject(result);
           String jsonMessage = json.get("relationNumList").toString();
           JSONArray jsonArr = JSONArray.parseArray(jsonMessage);
           logger.debug(result);
           for(int i=0;i< jsonArr.size();i++) {
               HiddenNumberBean bean1 = new HiddenNumberBean();
            String subscriptionId = jsonArr.getJSONObject(i).get("subscriptionId").toString();
            String callerStr = jsonArr.getJSONObject(i).get("callerNum").toString();
            String calleeStr = jsonArr.getJSONObject(i).get("calleeNum").toString();
            bean1.setUuid(subscriptionId);
            bean1.setA(callerStr);
            bean1.setB(calleeStr);
////            JSONObject jsonMessage1 = JSONObject.parseObject(jsonMessage);
////            String jsonMessage2 = json.get("relationNumList").toString();
//            logger.info("Response is :" + result);
//            this.subscriptionId=jsonMessage2;
               bean.add(bean1);
           }
           return bean;
       } catch (Exception e) {
           logger.error(e);
           return null;
       }

   }
   public String  PrivateNumberEvent(JSONObject numberEvent){
        HostingVoiceEventDemoImpl event= new HostingVoiceEventDemoImpl();
        try{
        String bean=event.onCallEvent(numberEvent,this.event);
        this.event=bean;
            return bean;}
        catch (Exception e){

            logger.error(e);
            return null;
       }



   }
}
