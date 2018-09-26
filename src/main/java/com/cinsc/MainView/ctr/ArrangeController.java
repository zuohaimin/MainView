package com.cinsc.MainView.ctr;

import com.cinsc.MainView.dto.ArrangeDto;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.service.ArrangeService;
import com.cinsc.MainView.utils.Assert;
import com.cinsc.MainView.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Author: 束手就擒
 * @Date: 18-9-2 下午4:18
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/arrange")
@Slf4j
@Api(tags = "工作安排页面")
public class ArrangeController {
    private ArrangeService arrangeService;
    @Autowired
    public ArrangeController(ArrangeService arrangeService){
        this.arrangeService = arrangeService;
    }

    //TODO 授权管理
    @ApiOperation(value = "新建工作安排")
    @RequestMapping(value = "/addArrange", method = RequestMethod.POST)
    public ResultVo addArrange(@Valid @RequestBody ArrangeDto arrangeDto,
                               BindingResult bindingResult,
                               HttpServletRequest request){
        if (bindingResult.hasErrors()){
            log.info("[新建工作安排] erro={}",bindingResult.getFieldError().getDefaultMessage());
            throw new SystemException(ResultEnum.PARAM_ERROR);
        }

        return arrangeService.addArrange(arrangeDto,request);
    }

    @ApiOperation(value = "完成安排(按键 建议控制响应间隔)")
    @RequestMapping(value = "/finishArrange", method = RequestMethod.GET)
    public ResultVo finishArrange(@RequestParam(value = "arrangeId",required = false)String arrangeId,
                                  HttpServletRequest request){
        Assert.isBlank(arrangeId,"安排Id不能为空|不能含有空格");
        log.info("[获取未完成的工作安排]");
        return arrangeService.finishArrange(arrangeId,request);
    }

    @ApiOperation(value = "获取未完成的工作安排")
    @RequestMapping(value = "/getUnfinishedArrange", method = RequestMethod.GET)
    public ResultVo getUnfinishedArrange(HttpServletRequest request){
       log.info("[获取未完成的工作安排]");
        return arrangeService.getUnfinishedArrange(request);
    }

    @ApiOperation(value = "获取已经完成的工作安排")
    @RequestMapping(value = "/getFinishedArrange", method = RequestMethod.GET)
    public ResultVo getFinishedArrange(HttpServletRequest request){
        log.info("[获取已经完成的工作安排]");
        return arrangeService.getFinishedArrange(request);
    }

    @ApiOperation(value = "获取我发出的工作安排")
    @RequestMapping(value = "/getSendedArrange", method = RequestMethod.GET)
    public ResultVo getSendedArrange(HttpServletRequest request){
        log.info("[获取我发出的工作安排]");
        return arrangeService.getSendedArrange(request);
    }

    @ApiOperation(value = "获取我执行的工作安排")
    @RequestMapping(value = "/getDoneArrange", method = RequestMethod.GET)
    public ResultVo getDoneArrange(HttpServletRequest request){
        log.info("[获取我执行的工作安排]");
        return arrangeService.getDoneArrange(request);
    }

    @ApiOperation(value = "获取抄送我的工作安排")
    @RequestMapping(value = "/getCCArrange", method = RequestMethod.GET)
    public ResultVo getCCArrange(HttpServletRequest request){
        log.info("[获取抄送我的工作安排]");
        return arrangeService.getCCArrange(request);
    }
}
