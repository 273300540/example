package com.xlm.example.web.controller;

import com.xlm.example.http.Http2Test;
import com.xlm.example.http.HttpTest;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

@Controller
public class HelloWorldController {
    @Resource
    private HttpTest httpTest;
    @Resource
    private Http2Test http2Test;

    @RequestMapping(value = "/path",produces =("text/plain") )
    @ResponseBody
    public String sayHello(String command) throws IOException {
        URL url = this.getClass().getResource("/config.xml");
        if (command != null && command.length() > 0) {

            InputStream fileInputStream = null;
            InputStreamReader reader = null;
            try {
                fileInputStream = new FileInputStream(url.getPath());
                reader = new InputStreamReader(fileInputStream, "utf-8");
                StringBuilder sb = new StringBuilder();
                int  n = 0;
                while ((n = reader.read())!=-1){
                    sb.append((char)n);
                }
                return sb.toString();
            } finally {
                if (reader != null) {
                    IOUtils.closeQuietly(reader);
                } else {
                    IOUtils.closeQuietly(fileInputStream);
                }
            }
        }
        return url.getPath();
    }

    @RequestMapping(value = "/httpTest",produces =("application/json") )
    @ResponseBody
    public Object httpTest(String command) throws IOException {

        ArrayList list = new ArrayList<>() ;
        list .addAll(httpTest.contributors("square", "retrofit"));
        list.addAll(http2Test.contributors("square", "retrofit"));
        return list;
    }
}
