package com.TB.push;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;

public class PushMsgToAll {  
    public static void main(String[] args) 
            throws PushClientException,PushServerException {
        // 1. get apiKey and secretKey from developer console
    	String apiKey = "1Rab8Er6A2PG2B9c4T4ubiRV";
		String secretKey = "pesNHKnHWRLcc5y4MlApL2lAwdbmU7Gv";
        PushKeyPair pair = new PushKeyPair(apiKey, secretKey);

        // 2. build a BaidupushClient object to access released interfaces
        BaiduPushClient pushClient = new BaiduPushClient(pair,
                BaiduPushConstants.CHANNEL_REST_URL);

        // 3. register a YunLogHandler to get detail interacting information
        // in this request.
        pushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {
            // 4. specify request arguments
        	PushMsgToAllRequest request = new PushMsgToAllRequest()
                    .addMsgExpires(new Integer(3600)).addMessageType(0)
                    .addMessage("Hello Baidu Push")//本来是add();但是报错了，应该是版本更新 了
                    // 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例70秒后推送
                    .addSendTime(System.currentTimeMillis() / 1000 + 70).
                    addDeviceType(3);
            // 5. http request
            PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
            // Http请求返回值解析
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: "    
                    + response.getSendTime() + ",timerId: "
                    + response.getTimerId());
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMsg: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }
}