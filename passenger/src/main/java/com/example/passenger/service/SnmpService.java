package com.example.passenger.service;

import com.example.passenger.entity.SnmpModel;
import com.example.passenger.utils.SnmpDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class SnmpService {
    private static final Logger logger = LoggerFactory.getLogger(SnmpService.class);
    SnmpDao snmpDao = new SnmpDao();

    public SnmpDao getInstanceSnmpDao() {
        return snmpDao;
    }

    /**
     * 通用方法
     * @param snmpModel
     * @param oid
     * @return
     */
    public Integer work(SnmpModel snmpModel,String oid){
        String type=oid.substring(oid.lastIndexOf(',')+1);
        Integer result=-1;
        if(type.equals("1")){
            result=getMemoryUtilization(snmpModel,oid);
        }else if (type.equals("2")){
            result=getCpuUtilization(snmpModel,oid);
        }else if (type.equals("3")){
            result=getHardDiskUtilization(snmpModel,oid);
        }
        return result;
    }

    /**
     * 获取CPU使用率
     *
     * @param snmpModel
     * @return 正常返回CPU当前使用率，否则返回-1
     */
    public Integer getCpuUtilization(SnmpModel snmpModel,String oid) {
        List<String> result = getInstanceSnmpDao().walkByTable(oid.substring(0,oid.indexOf(",")), snmpModel);
        if (result == null || result.size() == 0) {
            return -1;
        }
        double sum = 0;
        for (String s : result) {
            sum += Double.parseDouble(s);
        }
        return (int) (sum / result.size());
    }

    /**
     * 获取Memory大小
     *
     * @param snmpModel
     * @return 正常返回当前内存大小，否则返回-1
     * @throws IOException
     */
    public Integer getMemoryUtilization(SnmpModel snmpModel,String oid){
        try{
            String allresult = getInstanceSnmpDao().getSnmpGet(oid.substring(0,oid.indexOf(",")), snmpModel);
            if (allresult !=null) {
                Integer data=Integer.parseInt(allresult);
                return  data;
            }
        }catch (Exception e) {
            logger.error("获取内存大小:"+e.getMessage());
        }
        return -1;
    }


    /**
     * 获取硬盘大小
     * @param snmpModel
     * @return
     */
    public Integer getHardDiskUtilization(SnmpModel snmpModel,String oid) {
        try{
            String oneOid=oid.substring(0,oid.indexOf("/"));
            String twoOid=oid.substring(oid.indexOf("/")+1,oid.indexOf(","));
            String oneallresult = getInstanceSnmpDao().getSnmpGet(oneOid, snmpModel);
            String twoallresult = getInstanceSnmpDao().getSnmpGet(twoOid, snmpModel);
            if (oneallresult !=null && twoallresult!=null) {
                double one=Double.parseDouble(oneallresult);
                double two=Double.parseDouble(oneallresult);
                return (int) (one/two);
            }
        }catch (Exception e) {
            logger.error("相除算法:"+e.getMessage());
        }
        return -1;
    }



    /**
     * 设置音量
     * @param snmpModel
     * @return
     */
    public Integer setVolume(SnmpModel snmpModel,String oid) {
        try{
            String oneallresult = getInstanceSnmpDao().getSnmpGet(oid, snmpModel);
            if (oneallresult !=null) {
                Integer data=Integer.parseInt(oneallresult);
                return  data;
            }
        }catch (Exception e) {
            logger.error("设置音量:"+e.getMessage());
        }
        return -1;
    }


    /**
     * 测网络通不通 类似 ping ip
     *
     * @param snmpModel
     * @return
     * @throws IOException
     */
    public boolean isEthernetConnection(SnmpModel snmpModel) throws IOException {
        InetAddress ad = InetAddress.getByName(snmpModel.getHostIp());
        boolean state = ad.isReachable(2000);// 测试是否可以达到该地址 2秒超时
        return state;
    }

}
