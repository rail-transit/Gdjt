package com.example.passenger.service;

import com.example.passenger.entity.MessageBoard;
import com.example.passenger.mapper.MessageBoardMapper;
import com.example.passenger.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MessageBoardService {
    @Autowired
    MessageBoardMapper messageBoardMapper;

    public PageUtil messageBoardPaging(Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(messageBoardMapper.count());
        pageUtil.setPageData(messageBoardMapper.messageBoardPaging(pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addMessageBoard(MessageBoard messageBoard){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            messageBoard.setCreateDate(sdf.format(new Date()));
            return messageBoardMapper.addMessageBoard(messageBoard);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
