package com.forum.controller;

import com.forum.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/forum/nlp")
@Api(tags="NLP")
public class NlpController {

    @ApiOperation(("事件要素抽取"))
    @GetMapping("/extraction")
    @PreAuthorize("hasAnyRole('ROLE_user')")
    public CommonResult eventElementExtraction(@ApiParam("事件") @RequestParam(value = "text")String text) {
        Map<String,Object>result=new HashMap<>();
        String[] cmdArgs = new String[]{"bash","./extraction.bat",text};
        try {
            //执行这个.exe程序
            Process process = Runtime.getRuntime().exec(cmdArgs);// 执行py文件
            // 获取程序执行后返回的结果
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                String []a=line.split("\\s+");
                List<String> list=new ArrayList();
                switch (a[0])
                {
                    case "事故类型":
                        for(int i=1;i<a.length;i++)
                            list.add(a[i]);
                        result.put("事故类型",list);
                        break;
                    case"罪名":
                        for(int i=1;i<a.length;i++)
                            list.add(a[i]);
                        result.put("罪名",list);
                        break;
                    case"主次责任":
                        for(int i=1;i<a.length;i++)
                            list.add(a[i]);
                        result.put("主次责任",list);
                        break;
                    case"减刑因素":
                        for(int i=1;i<a.length;i++)
                            list.add(a[i]);
                        result.put("减刑因素",list);
                        break;
                    case"加刑因素":
                        for(int i=1;i<a.length;i++)
                            list.add(a[i]);
                        result.put("加刑因素",list);
                        break;
                    case"判决结果" :
                        for(int i=1;i<a.length;i++)
                            list.add(a[i]);
                        result.put("判决结果",list);
                        break;
                    default:;
                }
            }
            in.close();
            //java代码中的process.waitFor()返回值为0表示我们执行.exe文件成功，
            //返回值为1表示执行.exe文件失败，这和我们通常意义上见到的0与1定义正好相反
            //int re = process.waitFor();
            //System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.ok().data(result);
    }

    @ApiOperation(("相似度计算"))
    @GetMapping("/similarity")
    @PreAuthorize("hasAnyRole('ROLE_user')")
    public CommonResult judgment_similarity(@ApiParam("事件") @RequestParam(value = "text")String text) {
        String[] cmdArgs = new String[]{"bash","./similarity.bat",text};
        Map<String,Object> result = new HashMap<>();
        try {
            //执行这个.exe程序
            Process process = Runtime.getRuntime().exec(cmdArgs);// 执行py文件
            // 获取程序执行后返回的结果
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                String []a=line.split(":");
                switch (a[0]){
                    case "案件":
                        result.put(a[0]+a[1],a[2]);
                        break;
                    case "相似度":
                        result.put(a[0],a[1]);
                        break;
                    default:;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.ok().data(result);
    }

    @ApiOperation(("判决结果预测"))
    @GetMapping("/prediction")
    @PreAuthorize("hasAnyRole('ROLE_user')")
    public CommonResult judgment_prediction(@ApiParam("事件") @RequestParam(value = "text")String text) {
        String[] cmdArgs = new String[]{"bash","./prediction.bat",text};
        float months=0;
        try {
            //执行这个.exe程序
            Process process = Runtime.getRuntime().exec(cmdArgs);// 执行py文件
            // 获取程序执行后返回的结果
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                months=Float.valueOf(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  CommonResult.ok().data("months",months);
    }
}
