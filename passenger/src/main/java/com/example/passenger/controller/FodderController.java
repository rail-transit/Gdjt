package com.example.passenger.controller;

import com.example.passenger.entity.*;
import com.example.passenger.service.*;
import com.example.passenger.utils.FtpFileUtil;
import com.example.passenger.utils.PageUtil;
import com.example.passenger.utils.VideoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/fodder")
@Controller
public class FodderController {
    private static final Logger logger = LoggerFactory.getLogger(FodderController.class);

    @Autowired
    FodderService fodderService;
    @Autowired
    LineService lineService;
    @Autowired
    StyleContentService styleContentService;
    @Autowired
    FodderTypeService fodderTypeService;
    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/liveBroadcastMaterial")
    public String liveBroadcastMaterial(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageUtil pageUtil = fodderService.selectAllFodder(pageNum, 8);
        List<Line> lineList = lineService.selectAllLine();
        model.addAttribute("lineList", lineList);
        model.addAttribute("pageUtil", pageUtil);
        return "rightContent/system/liveBroadcastMaterial";
    }

    @RequestMapping("/material")
    public String material(Model model) {
        return "rightContent/format/material";
    }

    @RequestMapping("/getFodder")
    @ResponseBody
    public Map<String, Object> getFodder(ModelAndView mv, @RequestParam(defaultValue = "1") Integer pageNum,
                                         Integer type, Integer state, String name, HttpSession session) {
        List<Rights> rightsList = (List<Rights>) session.getAttribute("rightsList");
        PageUtil pageUtil = fodderService.selectAllFodderMaterial(type, state, name, pageNum, 10);
        mv.addObject("rightsList", rightsList);
        mv.addObject("pageUtil", pageUtil);
        return mv.getModel();
    }

    @RequestMapping("/selectFodder")
    @ResponseBody
    public Map<String, Object> selectFodder(ModelAndView mv, Integer id) {
        Fodder fodder = fodderService.selectFodderByID(id);
        mv.addObject("fodder", fodder);
        return mv.getModel();
    }

    @RequestMapping("/selectFodderList")
    @ResponseBody
    public Map<String, Object> selectFodderList(ModelAndView mv, @RequestParam(defaultValue = "0") Integer type) {
        List<Fodder> fodderList = fodderService.selectFodderByType(type);
        mv.addObject("fodderList", fodderList);
        return mv.getModel();
    }

    @RequestMapping("/addFodder")
    @ResponseBody
    public Map<String, Object> addFodder(ModelAndView mv, HttpSession session, Fodder fodder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = fodderService.selectFodderByName(fodder.getName(), null, 4);
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                fodder.setType(4);
                fodder.setSize("0");
                fodder.setState(1);
                fodder.setEditorID(users.getName());
                fodder.setEditTime(sdf.format(new Date()));
                fodder.setGuid(UUID.randomUUID().toString());
                fodder.setMd5("abc");
                Integer i = fodderService.addFodder(fodder);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + users.getName() + ") 添加(" + fodder.getName() + ")直播地址!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }


    @RequestMapping("/addMaterial")
    @ResponseBody
    public Map<String, Object> addMaterial(@RequestParam(value = "file", required = false) MultipartFile[] multipartFile,
                                           HttpSession session, ModelAndView mv) throws IOException {
        for (int i = 0; i < multipartFile.length; i++) {
            if (multipartFile[i] != null) {
                Fodder fodder1 = new Fodder();
                //生成新文件名，防止文件名重复而导致文件覆盖
                //1、获取原文件后缀名 .img .jpg ....
                String originalFileName = multipartFile[i].getOriginalFilename();
                originalFileName = originalFileName.substring(originalFileName.lastIndexOf('\\') + 1);

                InputStream inputStream = multipartFile[i].getInputStream();
                String suffix = originalFileName.substring(originalFileName.lastIndexOf('.'));
                //2、使用UUID生成新文件名
                String newFileName = UUID.randomUUID() + suffix;

                //判断文件类型类型
                FodderType fodderType = fodderTypeService.selectFodderTypeByName(suffix.substring(suffix.lastIndexOf('.') + 1));

                if (fodderType != null) {
                    Integer count = fodderService.selectFodderByName(originalFileName, null, fodderType.getType());
                    if (count > 0) {
                        mv.addObject("result", "exit");
                        return mv.getModel();
                    } else {
                        try {

                            fodder1.setType(fodderType.getType());
                            if (fodderType.getType() == 0 || fodderType.getType() == 1) {
                                String timeLength = VideoUtil.ReadVideoTimeMs(multipartFile[i]);
                                if (timeLength.equals("-1")) {
                                    mv.addObject("result", "failed");
                                }
                                if (!timeLength.equals("00:00:00")) {
                                    fodder1.setTimeLength(timeLength);
                                } else {
                                    fodder1.setTimeLength("");
                                }
                                if (VideoUtil.height != null && VideoUtil.width != null) {
                                    fodder1.setResolution(VideoUtil.width + "*" + VideoUtil.height);
                                } else {
                                    fodder1.setResolution("");
                                }
                            }
                            //上传
                            Boolean flag = FtpFileUtil.uploadFile(newFileName, inputStream);
                            if (flag == true) {
                                //上传路径
                                String Path = FtpFileUtil.getPath() + newFileName;
                                Users users = (Users) session.getAttribute("user");
                                fodder1.setName(originalFileName);
                                fodder1.setPath(Path);
                                fodder1.setSize(multipartFile[i].getSize() + "");
                                fodder1.setState(0);
                                fodder1.setEditorID(users.getName());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                fodder1.setEditTime(sdf.format(new Date()));
                                fodder1.setGuid(UUID.randomUUID().toString());
                                fodder1.setMd5("ccc");
                                Integer not = fodderService.addFodder(fodder1);
                                if (not > 0) {
                                    //日志记录
                                    OperationLog operationLog = new OperationLog();
                                    operationLog.setOperator(users.getId().toString());
                                    operationLog.setType("版式管理");
                                    operationLog.setContent("用户(" + users.getName() + ") 添加素材(" + originalFileName + ")!");
                                    operationLogService.addOperationLog(operationLog);
                                    mv.addObject("result", "success");
                                } else {
                                    mv.addObject("result", "error");
                                    return mv.getModel();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error(e.getMessage());
                        }
                    }
                } else {
                    mv.addObject("result", "noSupport");
                    return mv.getModel();
                }
            }
        }
        return mv.getModel();
    }


    @RequestMapping("/addFodderInfo")
    @ResponseBody
    public Map<String, Object> addFodderInfo(ModelAndView mv, HttpSession session, Fodder fodder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = fodderService.selectFodderByName(fodder.getName(), null, 3);
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                fodder.setType(3);
                fodder.setSize("0");
                fodder.setState(0);
                fodder.setEditorID(users.getName());
                fodder.setEditTime(sdf.format(new Date()));
                fodder.setGuid(UUID.randomUUID().toString());
                fodder.setMd5("abc");
                fodder.setNote("");
                Integer i = fodderService.addFodder(fodder);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("版式管理");
                    operationLog.setContent("用户(" + users.getName() + ") 添加信息素材(" + fodder.getName() + ")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/updateFodder")
    @ResponseBody
    public Map<String, Object> updateFodder(ModelAndView mv, Fodder fodder, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        try {
            Integer count = fodderService.selectFodderByName(fodder.getName(), fodder.getId(), 4);
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                Integer i = fodderService.updateFodder(fodder);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(user.getId().toString());
                    operationLog.setType("系统配置管理");
                    operationLog.setContent("用户(" + user.getName() + ") 修改直播地址(" + fodder.getName() + ")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/updateFodderState")
    @ResponseBody
    public Map<String, Object> updateFodderState(ModelAndView mv, Fodder fodder, HttpSession session) {
        Fodder fodder1 = fodderService.selectFodderByID(fodder.getId());
        Users users = (Users) session.getAttribute("user");
        try {
            Integer i = fodderService.updateFodderState(fodder);
            if (i > 0) {
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("版式管理");
                if (fodder.getState() == 1) {
                    operationLog.setContent("用户(" + users.getName() + ") 审核素材(" + fodder1.getName() + ")审核通过!");
                } else {
                    operationLog.setContent("用户(" + users.getName() + ") 审核素材(" + fodder1.getName() + ")审核不通过!");
                }
                operationLogService.addOperationLog(operationLog);
                mv.addObject("result", "success");
            } else {
                mv.addObject("result", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteFodder")
    @ResponseBody
    public Map<String, Object> deleteFodder(ModelAndView mv, Fodder fodder, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer i = fodderService.deleteFodder(fodder);
            if (i > 0) {
                //日志记录
                OperationLog operationLog = new OperationLog();
                operationLog.setOperator(users.getId().toString());
                operationLog.setType("版式管理");
                operationLog.setContent("用户(" + users.getName() + ") 删除直播地址(" + fodder.getName() + ")!");
                operationLogService.addOperationLog(operationLog);
                mv.addObject("result", "success");
            } else {
                mv.addObject("result", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }

    @RequestMapping("/deleteMaterial")
    @ResponseBody
    public Map<String, Object> deleteMaterial(ModelAndView mv, Fodder fodder, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        try {
            Integer count = styleContentService.selectContentByMaterialID(fodder.getId());
            if (count > 0) {
                mv.addObject("result", "exit");
            } else {
                Integer i = fodderService.deleteFodder(fodder);
                if (i > 0) {
                    //日志记录
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperator(users.getId().toString());
                    operationLog.setType("版式管理");
                    operationLog.setContent("用户(" + users.getName() + ") 删除素材(" + fodder.getName() + ")!");
                    operationLogService.addOperationLog(operationLog);
                    mv.addObject("result", "success");
                } else {
                    mv.addObject("result", "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("result", "error");
            logger.error(e.getMessage());
        }
        return mv.getModel();
    }


    //读取文件内容
    @RequestMapping("/readTxt")
    @ResponseBody
    public Map<String, Object> readFile(@RequestParam(value = "path", required = false) String path, ModelAndView mv) {
        String content = fodderService.readTxt(path);
        mv.addObject("content", content);
        return mv.getModel();
    }

}
