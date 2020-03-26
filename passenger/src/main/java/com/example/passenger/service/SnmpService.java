package com.example.passenger.service;

import com.example.passenger.entity.SnmpModel;
import com.example.passenger.utils.SnmpDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class SnmpService {
    private static final Logger logger = LoggerFactory.getLogger(SnmpService.class);
    SnmpDao snmpDao = new SnmpDao();

    public SnmpDao getInstanceSnmpDao() {
        return snmpDao;
    }

    /**
     * 通用方法
     *
     * @param snmpModel
     * @param oid
     * @return
     */
    public Integer work(SnmpModel snmpModel, String oid) {
        String type = oid.substring(oid.lastIndexOf(',') + 1);
        Integer result = -1;
        if (type.equals("1")) {
            result = getMemoryUtilization(snmpModel, oid);
        } else if (type.equals("2")) {
            result = getCpuUtilization(snmpModel, oid);
        } else if (type.equals("3")) {
            result = getHardDiskUtilization(snmpModel, oid);
        } else {
            result = operationVolume(snmpModel, oid);
        }
        return result;
    }

    /**
     * 获取CPU使用率
     *
     * @param snmpModel
     * @return 正常返回CPU当前使用率，否则返回-1
     */
    public Integer getCpuUtilization(SnmpModel snmpModel, String oid) {
        List<String> result = getInstanceSnmpDao().walkByTable(oid.substring(0, oid.indexOf(",")), snmpModel);
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
    public Integer getMemoryUtilization(SnmpModel snmpModel, String oid) {
        try {
            String allresult = getInstanceSnmpDao().getSnmpGet(oid.substring(0, oid.indexOf(",")), snmpModel);
            if (allresult != null) {
                Integer data = Integer.parseInt(allresult) / 1024;
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取内存大小:" + e.getMessage());
        }
        return -1;
    }

    /**
     * 获取硬盘大小
     *
     * @param snmpModel
     * @return
     */
    public Integer getHardDiskUtilization(SnmpModel snmpModel, String oid) {
        try {
            String oneOid = oid.substring(0, oid.indexOf("/"));
            String twoOid = oid.substring(oid.indexOf("/") + 1, oid.indexOf(","));
            String oneallresult = getInstanceSnmpDao().getSnmpGet(oneOid, snmpModel);
            String twoallresult = getInstanceSnmpDao().getSnmpGet(twoOid, snmpModel);
            if (oneallresult != null && twoallresult != null) {
                int one = Integer.parseInt(oneallresult);
                int two = Integer.parseInt(twoallresult);
                return one / two;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("相除算法:" + e.getMessage());
        }
        return -1;
    }


    /**
     * 操作音量
     *
     * @param snmpModel
     * @return
     */
    public Integer operationVolume(SnmpModel snmpModel, String oid) {
        try {
            String oneallresult = getInstanceSnmpDao().getSnmpGet(oid, snmpModel);
            if (oneallresult != null) {
                Integer data = Integer.parseInt(oneallresult);
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("音量操作:" + e.getMessage());
            logger.error("操作oid:" + oid);
        }
        return null;
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
        // 测试是否可以达到该地址 2秒超时
        boolean state = ad.isReachable(2000);
        return state;
    }

    /**
     * 发送幻数据包，唤醒设备(打开设备)
     *
     * @param port 目标设备端口
     * @param mac  目标设备mac地址
     * @author suxijian
     */
    public static void wake(int port, String mac) {
        //幻数据包简单构成：首先6字节255(FF FF FF FF FF FF FF),
        //紧接着48位6字节的MAC地址，重复16次，即6*16，则总数据包：6+6*16字节
        //将String类型的MAC地址分割为相应的十六进制数的字符串数组
        String[] macStr = mac.split(":");
        char[] macChar = new char[6];
        char[] packet = new char[6 + 6 * 16];
        char[] head = new char[6];
        for (int i = 0; i < 6; i++) {
            head[i] = 0xFF;
        }

        //将MAC的十六进制数的字符串数组转换为char数组
        for (int i = 0; i < 6; i++) {
            macChar[i] = (char) Integer.parseInt(macStr[i].replaceAll("^0[x|X]", ""), 16);
        }

        // 拼接数据包
        System.arraycopy(head, 0, packet, 0, head.length);

        // 重复16次目的主机MAC
        for (int i = 0; i < 16; i++) {
            System.arraycopy(macChar, 0, packet, 6 + i * 6, head.length);
        }
        String packetStr = new String(packet);
        try {
            byte[] data = packetStr.getBytes("ISO-8859-1");

            //广播地址
            InetAddress inetAddr = InetAddress.getByName("255.255.255.255");

            //socket 用来广播
            DatagramSocket client = new DatagramSocket();

            //封装数据包
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddr, port);

            //设置为广播
            client.setBroadcast(true);

            //发送数据包
            client.send(datagramPacket);

            //关闭socket
            client.close();
        } catch (UnknownHostException e) {
            //Ip地址错误时候抛出的异常
            e.printStackTrace();
        } catch (IOException e) {
            //获取socket失败时候抛出的异常
            e.printStackTrace();
        }

    }

}
