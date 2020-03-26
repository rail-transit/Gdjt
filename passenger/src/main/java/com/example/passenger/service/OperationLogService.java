package com.example.passenger.service;

import com.example.passenger.entity.OperationLog;
import com.example.passenger.mapper.OperationLogMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OperationLogService {
    private static final Logger logger = LoggerFactory.getLogger(OperationLogService.class);
    @Autowired
    OperationLogMapper operationLogMapper;

    public List<Map<String, String>> getJournal(String startTime, String endTime,
                                                String type) {
        return operationLogMapper.getJournal(startTime, endTime, type);
    }

    public PageUtil selectPaging(String startTime, String endTime,
                                 String type, Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(operationLogMapper.count(startTime, endTime, type));
        pageUtil.setPageData(operationLogMapper.selectPaging(startTime, endTime, type, pageNum, pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addOperationLog(OperationLog operationLog) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            operationLog.setTime(sdf.format(new Date()));
            if (operationLog.getIsExported() == null) {
                operationLog.setIsExported(0);
            }
            if (operationLog.getDeviceID() == null) {
                operationLog.setDeviceID(0);
            }
            if (operationLog.getStationID() == null) {
                operationLog.setStationID(0);
            }
            if (operationLog.getLineID() == null) {
                operationLog.setLineID(0);
            }
            return operationLogMapper.addOperationLog(operationLog);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
