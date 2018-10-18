package com.cinsc.MainView.ctr;

import com.cinsc.MainView.dto.NoticeDto;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.service.HomieService;
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
 * @Date: 18-8-13 下午3:28
 * @Description:
 */

@CrossOrigin
@RestController
@RequestMapping("/homie")
@Slf4j
@Api(tags = "通讯录页面")
public class HomieController {
    private HomieService homieService;


    @Autowired
    public HomieController(HomieService homieService){
        this.homieService = homieService;
    }

    /**
     * 获取好友列表
     * @return
     */
    @ApiOperation("获取好友列表")
    @RequestMapping(value = "/getFriendsList", method = RequestMethod.GET)
    public ResultVo getFriendsList(HttpServletRequest request){
        return homieService.getFriendsList(request);
    }

    /**
     * 寻找好友(加好友)
     * @param userAccount
     * @return
     */
    @ApiOperation(value = "寻找好友(加好友)")
    @RequestMapping(value = "/findHomie",method = RequestMethod.GET)
    public ResultVo findHomie(@RequestParam(value = "userAccount",required = false) String userAccount){
        Assert.isBlank(userAccount,"账户名不能为空|不能含有空格");
        return homieService.findHomie(userAccount);
    }


    /**
     * 发送确认消息
     * @param noticeDto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "发送确认消息")
    @RequestMapping(value = "/sendCheckMessage",method = RequestMethod.POST)
    public ResultVo sendCheckMessage(@Valid  NoticeDto noticeDto,
                                     BindingResult bindingResult,
                                     HttpServletRequest request) {
        if (bindingResult.hasErrors()){
            log.error("[发送验证信息]参数不正确:noticeDto={}",noticeDto);
            throw new SystemException(ResultEnum.PARAM_ERROR);
        }
        return homieService.sendCheckMessage(noticeDto,request);
    }

    /**
     * 发送信息
     * @param userId
     * @param content
     * @param request
     * @return
     */
    @ApiOperation(value = "发送信息")
    @RequestMapping(value = "/sendMessage",method = RequestMethod.POST)
    public ResultVo sendMessage(@RequestParam(value = "userId",required = false) Integer userId,
                                @RequestParam(value = "content",required = false) String content,
                                     HttpServletRequest request) {
        Assert.isNull(userId,"userId 不能为空 | 含有空格 ");
        Assert.isBlank(content,"content 不能为空 | 含有空格");
        return homieService.sendMessage(userId, content, request);
    }


    /**
     * 确认加好友消息
     * @param id
     * @param friendAccount
     * @return
     */
    @ApiOperation(value = "确认加好友消息")
    @RequestMapping(value = "/confirmCheckMessage", method = RequestMethod.GET)
    public ResultVo confirmCheckMessage(@RequestParam(value = "id",required = false) String id,
                                        @RequestParam(value = "friendAccount",required = false) String friendAccount,
                                        HttpServletRequest request){
        Assert.isBlank(id,"id不能为空|不能含有空格");
        Assert.isBlank(friendAccount,"账户名不能为空|不能含有空格");
        return homieService.confirmCheckMessage(id, friendAccount,request);
    }

    /**
     * 设置消息为已读
     * @param id
     * @return
     */
    @ApiOperation(value = "改变消息状态")
    @RequestMapping(value = "/readMessage",method = RequestMethod.GET)
    public ResultVo readMessage(@RequestParam(value = "id",required = false) String id){
        Assert.isBlank(id,"id不能为空|不能含有空格");
        return homieService.readMessage(id);
    }


    @ApiOperation(value = "删除好友")
    @RequestMapping(value = "/deleteFriend/{userId}",method = RequestMethod.DELETE)
        public ResultVo deleteFriend(@PathVariable("userId") Integer userId,
                                 HttpServletRequest request){
        log.info("[删除好友] userId={}",userId);
        Assert.isNull(userId,"userId不能为空|不能含有空格");
        return homieService.deleteFriend(userId, request);
    }

    @ApiOperation(value = "删除消息")
    @RequestMapping(value = "/deleteMessage/{id}",method = RequestMethod.DELETE)
    public ResultVo deleteMessage(@PathVariable("id")String id,
                                 HttpServletRequest request){
        Assert.isNull(id,"id不能为空|不能含有空格");
        return homieService.deleteMessage(id, request);
    }

    /**
     * 获取发给自己未读的消息
     * @param request
     * @return
     */
    @ApiOperation(value = "获取发给自己未读的消息")
    @RequestMapping(value = "/getUnreadMessage",method = RequestMethod.GET)
    public ResultVo getUnreadMessage(HttpServletRequest request) {
        return homieService.getUnreadMessage(request);
    }

    @ApiOperation(value = "获得好友验证消息")
    @RequestMapping(value = "/getCheckMessage",method = RequestMethod.GET)
    public ResultVo getCheckMessage(HttpServletRequest request) {
        return homieService.getCheckMessage(request);
    }

    @ApiOperation(value = "判断是否有未读消息")
    @RequestMapping(value = "/isUnreadMessage",method = RequestMethod.GET)
    public ResultVo isUnreadMessage(HttpServletRequest request) {
        return homieService.isUnreadMessage(request);
    }
    /**
     * 获取发给自己已读的消息
     * @param request
     * @return
     */
    @ApiOperation(value = "获取发给自己已读的消息")
    @RequestMapping(value = "/getReadedMessage",method = RequestMethod.GET)
    public ResultVo getReadedMessage(HttpServletRequest request) {
        return homieService.getReadedMessage(request);
    }

    /**
     * 获取好友名片
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取好友名片")
    @RequestMapping(value = "/getFriendCards",method = RequestMethod.GET)
    public ResultVo getFriendCards(@RequestParam(value = "userId",required = false) Integer userId){
        Assert.isNull(userId,"userId不能为空 | 不能含有空格");
        return homieService.getFriendCards(userId);
    }

    /**
     * 判断是否为朋友
     * @param userAccount
     * @return
     */
    @ApiOperation(value = "判断是否()为朋友")
    @RequestMapping(value = "/isFriend",method = RequestMethod.GET)
    public ResultVo isFriend(@RequestParam(value = "userAccount",required = false) String userAccount,
                             HttpServletRequest request){
        Assert.isBlank(userAccount,"userAcount不能为空 | 不能含有空格");
        return homieService.isFriend(userAccount,request);
    }


}
