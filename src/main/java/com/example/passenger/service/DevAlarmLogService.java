package com.example.passenger.service;

import com.example.passenger.entity.DevAlarmLog;
import com.example.passenger.mapper.DevAlarmLogMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DevAlarmLogService {
    private static final Logger logger = LoggerFactory.getLogger(DevAlarmLogService.class);

    @Autowired
    DevAlarmLogMapper devAlarmLogMapper;

    public List<Map<String,String>> getWarning(Integer alarmType, Integer lineID, Integer stationID,
                                               Integer deviceID,Integer type ,String startDate, String endDate){
        List<Map<String,String>> mapList=new ArrayList<>();
        try {
            mapList=devAlarmLogMapper.getWarning(alarmType,lineID,stationID,deviceID,type,
                    startDate,endDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mapList;
    }

    public PageUtil warningPaging(Integer alarmType,Integer lineID,Integer stationID,Integer deviceID,
                                  Integer type,String startDate,String endDate,Integer pageNum,
                                  Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(devAlarmLogMapper.warningCount(alarmType,lineID,stationID,deviceID,
                type,startDate,endDate));
        pageUtil.setPageData(devAlarmLogMapper.warningPaging(alarmType,lineID,stationID,deviceID,
                type,startDate,endDate,pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addDevAlarmLog(DevAlarmLog devAlarmLog){
        try {
            return devAlarmLogMapper.addDevAlarmLog(devAlarmLog);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return -1;
    }
}
