package com.example.passenger.mapper;

import com.example.passenger.entity.StyleContent;
import com.example.passenger.entity.vo.StyleContentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StyleContentMapper {

    StyleContent selectContentById(Integer id);

    List<StyleContent> selectStyleContent(@Param("id") Integer id,@Param("layoutID") String layoutID);

    List<StyleContentVo> selectStyleContentVo(Integer styleID);


    List<StyleContent> selectPaging(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);

    Integer selectContentByMaterialID(Integer materialID);

    Integer count();

    Integer StyleContentID(Integer styleID);

    Integer addStyleContent(StyleContent styleContent);

    Integer updateStyleContent(StyleContent styleContent);

    Integer deleteStyleContent(Integer id);

    Integer deleteStyleContentByStyleID(Integer styleID);
}
