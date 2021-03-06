package com.changlu;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.changlu.AaliTest2.NO1.CodePrefixSearchRequest;
import com.changlu.beams.MockContextImpl;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @ClassName CodePrefixSearchRequestTest
 * @Author ChangLu
 * @Date 2021/10/27 19:09
 * @Description TODO
 */
public class CodePrefixSearchRequestTest {


    @Test
    public void testHandleRequest() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream inputStream = this.getClass().getResourceAsStream("/test_input.json");
        assert inputStream != null;
        CodePrefixSearchRequest request = new CodePrefixSearchRequest();
        try {
            request.handleRequest(inputStream, baos, new MockContextImpl());
            String outputData = baos.toString();
            Assert.assertNotEquals("", outputData);
            JSONObject outputObj = JSON.parseObject(outputData);
            Assert.assertNotNull(outputObj);
            if (outputObj.containsKey("errorMessage")) {
                Assert.fail(outputObj.getString("errorMessage"));
            } else {
                evaluate(outputObj);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    private void evaluate(JSONObject outputObj) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test_eval.json");
        assert inputStream != null;
        String json = IOUtils.toString(inputStream, "utf-8");
        JSONObject evalObj = JSON.parseObject(json);
        for (Map.Entry<String, Object> entry : evalObj.entrySet()) {
            String key = entry.getKey();
            if (!outputObj.containsKey(key)) {
                Assert.fail("Cannot find eval key:" + key);
            } else {
                JSONArray outputArray = outputObj.getJSONArray(key);
                JSONArray evalArray = (JSONArray) entry.getValue();
                outputArray.retainAll(evalArray);
                Assert.assertEquals(evalArray.size(), outputArray.size());
            }
        }
    }


}