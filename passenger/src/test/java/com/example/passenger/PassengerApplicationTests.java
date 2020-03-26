package com.example.passenger;

import com.example.passenger.config.AmqpConfig;
import com.example.passenger.service.DeviceService;
import com.example.passenger.utils.IPUtil;
import com.example.passenger.utils.MsgSend;
import com.example.passenger.utils.VisitCount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PassengerApplicationTests {

    @Autowired
    DeviceService deviceService;

    @Test
    public void contextLoads() {
    }

    @Autowired
    private MsgSend send;
    /**
     * @author suxijian
     * 测试获取线路所有设备信息接口
     * */
    @Test
    public void testGetAllDeviceByLineId(){
        List<VisitCount> a = deviceService.getAllDeviceByLineId(9);
        System.out.println("a:");
        System.out.println(a);
        System.out.println("a.size:");
        System.out.println(a.size());
        for (int i = 0; i < a.size(); i++) {
            System.out.println(i+"a.get(i).getDeviceID():");
            System.out.println(a.get(i).getDeviceID());
        }
    }

}
