package com.example.passenger.service;

import com.example.passenger.entity.Fodder;
import com.example.passenger.mapper.FodderMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;

@Service
public class FodderService {
    private static final Logger logger = LoggerFactory.getLogger(FodderService.class);

    @Autowired
    FodderMapper fodderMapper;

    public PageUtil selectAllFodder(Integer pageNum, Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(fodderMapper.count());
        pageUtil.setPageData(fodderMapper.selectAllFodder(pageNum,pageSize));
        return pageUtil;
    }

    public Integer selectFodderByName(String name,Integer id,Integer type){
        return fodderMapper.selectFodderByName(name,id,type);
    }
 /*   public List<Fodder> selectAllFodderMaterial(Fodder fodder){
        return fodderMapper.selectAllFodderMaterial(fodder);
    }*/
    public List<Fodder> selectFodderByType(Integer type){
        return fodderMapper.selectFodderByType(type);
    }

    public Fodder selectFodderByID(Integer id){
        return fodderMapper.selectFodderByID(id);
    }

    public PageUtil selectAllFodderMaterial(Integer type,Integer state,String name,Integer pageNum, Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(fodderMapper.selectAllFodderMaterialCount(type,state,name));
        pageUtil.setPageData(fodderMapper.selectAllFodderMaterial(type,state,name,pageNum, pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addFodder(Fodder fodder){
        try {
            return fodderMapper.addFodder(fodder);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateFodder(Fodder fodder){
        try {
            return fodderMapper.updateFodder(fodder);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateFodderState(Fodder fodder){
        try {
            return fodderMapper.updateFodderState(fodder);
        }catch (Exception  e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteFodder(Fodder fodder){
        try {
            return fodderMapper.deleteFodder(fodder);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
    
    /**
     * 解析普通文本文件  流式文件 如txt
     * @param path 路径
     * @return
     */
    public String readTxt(String path){
        String localPath = path.replace("/Path/","D://ftp/");
        StringBuilder content = new StringBuilder();
        try {
            String code = resolveCode(localPath);
            File file = new File(localPath);
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, code);
            BufferedReader br = new BufferedReader(inputStreamReader);
//          char[] buf = new char[1024];
//          int i = br.read(buf);
//          String s= new String(buf);
//          System.out.println(s);
            String str = null;
            while (null != (str = br.readLine())) {
                content.append(str);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("读取文件:" + localPath + "失败!");
        }
        return content.toString();
    }

    /**
     * 解析文件内容的编码
     * @param path
     * @return
     * @throws Exception
     */
    public String resolveCode(String path) throws Exception {
        InputStream inputStream = new FileInputStream(path);
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";  //或GBK
        if (head[0] == -1 && head[1] == -2 ){
            code = "UTF-16";
        }
        else if (head[0] == -2 && head[1] == -1 ){
            code = "Unicode";
        }
        else if(head[0]==-17 && head[1]==-69 && head[2] ==-65){
            code = "UTF-8";
        }
        inputStream.close();
//        System.out.println(code);
        return code;
    }

}
