package com.xlm.example.web.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.URL;

@Controller
public class HelloWorldController {


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
}
