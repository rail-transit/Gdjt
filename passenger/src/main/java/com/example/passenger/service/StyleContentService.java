package com.example.passenger.service;

import com.example.passenger.entity.StyleContent;
import com.example.passenger.entity.vo.StyleContentVo;
import com.example.passenger.mapper.StyleContentMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StyleContentService {
    private static final Logger logger = LoggerFactory.getLogger(StyleContentService.class);

    @Autowired
    StyleContentMapper styleContentMapper;

    public StyleContent selectContentById(Integer id) {
        return styleContentMapper.selectContentById(id);
    }

    public List<StyleContent> selectStyleContent(Integer id, String layoutID) {
        return styleContentMapper.selectStyleContent(id, layoutID);
    }

    public List<StyleContentVo> selectStyleContentVo(Integer styleID) {
        return styleContentMapper.selectStyleContentVo(styleID);
    }

    public Integer selectContentByMaterialID(Integer materialID) {
        return styleContentMapper.selectContentByMaterialID(materialID);
    }

    public PageUtil selectPaging(Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(styleContentMapper.count());
        pageUtil.setPageData(styleContentMapper.selectPaging(pageNum, pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addStyleContent(StyleContent styleContent) {
        try {
            Integer count = styleContentMapper.StyleContentID(styleContent.getStyleID());
            count += 1;
            styleContent.setLayoutID("1");
            styleContent.setContentID(count);
            styleContent.setPlaytimes(1);
            return styleContentMapper.addStyleContent(styleContent);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateStyleContent(StyleContent styleContent) {
        try {
            return styleContentMapper.updateStyleContent(styleContent);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteStyleContent(Integer id) {
        try {
            return styleContentMapper.deleteStyleContent(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}
