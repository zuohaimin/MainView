package com.cinsc.MainView.ctr;

import com.cinsc.MainView.annotation.CheckPermission;
import com.cinsc.MainView.annotation.enums.PermsEnum;
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
import java.util.Date;

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

    @CheckPermission(perms = PermsEnum.TEACHER)
    @ApiOperation(value = "新建工作安排")
    @RequestMapping(value = "/addArrange", method = RequestMethod.POST)
    public ResultVo addArrange(@Valid ArrangeDto arrangeDto,
                               BindingResult bindingResult,
                               HttpServletRequest request){
        if (bindingResult.hasErrors()){
            log.info("[新建工作安排] erro={}",bindingResult.getFieldError().getDefaultMessage());
            throw new SystemException(ResultEnum.PARAM_ERROR);
        }

        return arrangeService.addArrange(arrangeDto,request);
    }

    @CheckPermission(perms = PermsEnum.TEACHER)
    @ApiOperation(value = "改变安排状态(完成/未完成)")
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
    @ApiOperation(value = "添加日程安排")
    @RequestMapping(value = "/addScheduleArrange", method = RequestMethod.GET)
    public ResultVo addScheduleArrange(@RequestParam(value = "deadLine",required = false)Date deadLine,
                                       @RequestParam(value = "description",required = false)String description,
                                       HttpServletRequest request){
        log.info("[添加日程安排]");
        Assert.isNull(deadLine,"deadLine不能为空|含有空格");
        Assert.isBlank(description,"description不能为空|含有空格");
        return arrangeService.addScheduleArrange(deadLine,description,request);
    }

    @ApiOperation(value = "完成日程安排")
    @RequestMapping(value = "/finishScheduleArrange", method = RequestMethod.GET)
    public ResultVo finishScheduleArrange(@RequestParam(value = "arrangeId",required = false)String arrangeId,
                                       HttpServletRequest request){
        log.info("[完成日程安排]");
        Assert.isBlank(arrangeId,"arrangeId不能为空|含有空格");
        return arrangeService.finishScheduleArrange(arrangeId,request);
    }

    @ApiOperation(value = "获得日程安排")
    @RequestMapping(value = "/getScheduleArrange", method = RequestMethod.GET)
    public ResultVo getScheduleArrange(@RequestParam(value = "time",required = false)Date time,
                                          HttpServletRequest request){
        log.info("[获得日程安排]");
        Assert.isNull(time,"time不能为空|含有空格");
        return arrangeService.getScheduleArrange(time,request);
    }

    @ApiOperation(value = "删除工作安排")
    @RequestMapping(value = "/deleteWorkArrange/{arrangeId}",method = RequestMethod.DELETE)
    public ResultVo deleteWorkArrange(@PathVariable("arrangeId") String arrangeId,
                                 HttpServletRequest request){
        Assert.isNull(arrangeId,"arrangeId不能为空|不能含有空格");
        return arrangeService.deleteWorkArrange(arrangeId,request);
    }

    @ApiOperation(value = "删除日程安排")
    @RequestMapping(value = "/deleteScheduleArrange/{arrangeId}",method = RequestMethod.DELETE)
    public ResultVo deleteScheduleArrange(@PathVariable("arrangeId") String arrangeId,
                                      HttpServletRequest request){
        Assert.isNull(arrangeId,"arrangeId不能为空|不能含有空格");
        return arrangeService.deleteScheduleArrange(arrangeId,request);
    }

    @ApiOperation(value = "得到安排的成员信息")
    @RequestMapping(value = "/getArrangeTransactors", method = RequestMethod.GET)
    public ResultVo getArrangeTransactors(@RequestParam(value = "arrangeId",required = false)String arrangeId){
        log.info("[得到安排的成员信息]");
        Assert.isNull(arrangeId,"arrangeId不能为空|含有空格");
        return arrangeService.getArrangeTransactors(arrangeId);
    }
}
