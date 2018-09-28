package com.cinsc.MainView.service.impl;

import com.cinsc.MainView.constant.MainViewConstant;
import com.cinsc.MainView.dto.NoticeDto;
import com.cinsc.MainView.enums.MsgStatusEnum;
import com.cinsc.MainView.enums.NoticeEnum;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.model.Friends;
import com.cinsc.MainView.model.Notice;
import com.cinsc.MainView.model.UserDetail;
import com.cinsc.MainView.model.UserLogin;
import com.cinsc.MainView.repository.FriendsRepository;
import com.cinsc.MainView.repository.NoticeRepository;
import com.cinsc.MainView.repository.UserDetailRepository;
import com.cinsc.MainView.repository.UserLoginRepository;
import com.cinsc.MainView.service.HomieService;
import com.cinsc.MainView.utils.ResultVoUtil;
import com.cinsc.MainView.utils.ShiroUtil;
import com.cinsc.MainView.utils.convert.PictureToBase64;
import com.cinsc.MainView.utils.key.KeyUtil;
import com.cinsc.MainView.vo.FriendVo;
import com.cinsc.MainView.vo.NoticeVo;
import com.cinsc.MainView.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author: 束手就擒
 * @Date: 18-8-10 下午4:33
 * @Description:
 */
@Service
@Slf4j
@Transactional
public class HomieServiceImpl implements HomieService {

    private FriendsRepository friendsRepository;
    private NoticeRepository noticeRepository;
    private UserDetailRepository userDetailRepository;
    private UserLoginRepository userLoginRepository;

    @Autowired
    public HomieServiceImpl(FriendsRepository friendsRepository,
                            NoticeRepository noticeRepository,
                            UserDetailRepository userDetailRepository,
                            UserLoginRepository userLoginRepository){
        this.friendsRepository = friendsRepository;
        this.noticeRepository = noticeRepository;
        this.userDetailRepository = userDetailRepository;
        this.userLoginRepository = userLoginRepository;
    }



    /**
     * 获得好友们的Id集合
     * @param userId
     * @param friendsList
     * @return
     */
    @Deprecated
    private List<Integer> getHomieIdList(Integer userId, List<Friends> friendsList){
        List<Integer> homieIdList = new ArrayList<>();
        friendsList.forEach(o->{
            if (o.getAId().equals(userId)){
                homieIdList.add(o.getBId());
            }else if (o.getBId().equals(userId)){
                homieIdList.add(o.getAId());
            }else{
                throw new SystemException(ResultEnum.DATA_ERROR);
            }
        });
        return homieIdList;
    }


    private Integer getUserId(String userAccount) {
        UserLogin userLogin = userLoginRepository.findByUserAccount(userAccount);
        log.info("获取用户Id userLogin={}", userLogin);
        if (userLogin == null) {
            throw new SystemException(ResultEnum.UNkNOWN_ACCOUNT);
        }
        return userLogin.getUserId();
    }

    private String getUserAccount(Integer userId) {
        UserLogin userLogin = userLoginRepository.findById(userId).orElseThrow(()->new SystemException(ResultEnum.UNkNOWN_ACCOUNT));
        log.info("获取用户Id userLogin={}", userLogin);
        return userLogin.getUserAccount();
    }
    private Notice getNotice(NoticeDto noticeDto,HttpServletRequest request){
        Integer userId = ShiroUtil.getUserId(request);
        UserLogin userLogin = userLoginRepository.findById(userId).orElseThrow(()->new SystemException(ResultEnum.UNkNOWN_ACCOUNT));
        log.info("装配notice userLogin={}",userLogin);
        Notice notice = new Notice();
        notice.setId(KeyUtil.genUniqueKey());
        notice.setNoticeFrom(userLogin.getUserAccount());
        notice.setNoticeTo(noticeDto.getTo());
        notice.setContent(noticeDto.getContent());
        return notice;
    }
    private Notice getNotice(Integer userId, String content, HttpServletRequest request){
        Integer owerId = ShiroUtil.getUserId(request);
        UserLogin userLogin = userLoginRepository.findById(owerId).orElseThrow(()->new SystemException(ResultEnum.UNkNOWN_ACCOUNT));
        log.info("装配notice userLogin={}",userLogin);
        Notice notice = new Notice();
        notice.setId(KeyUtil.genUniqueKey());
        notice.setNoticeFrom(userLogin.getUserAccount());
        notice.setNoticeTo(getUserAccount(userId));
        notice.setContent(content);
        return notice;
    }

    private String getUsername(String userAccount){
        UserDetail userDetail = userDetailRepository.findByUserId(getUserId(userAccount));
        if (userDetail == null){
            log.info("通讯录 获取用户名 userDetail=null");
            throw new SystemException(ResultEnum.DATA_ERROR);
        }
        return userDetail.getUserName();
    }
    private List<NoticeVo> getNoticeVo(List<Notice> noticeList){
        List<NoticeVo> noticeVoList = new ArrayList<>();
        noticeList.forEach(o->{
            NoticeVo noticeVo = new NoticeVo();
            BeanUtils.copyProperties(o,noticeVo);
            noticeVo.setUsername(getUsername(o.getNoticeFrom()));
            noticeVoList.add(noticeVo);
        });
        return noticeVoList;
    }

    private boolean ifFriend(String friendAccount, HttpServletRequest request) {
       return (friendsRepository.findByAIdAndBId(ShiroUtil.getUserId(request),getUserId(friendAccount)) == null);

    }
    @Override
    public ResultVo getFriendsList(HttpServletRequest request) {
        Integer userId = ShiroUtil.getUserId(request);
        List<Friends> friendsList = friendsRepository.findByAId(userId);
        if (friendsList == null){
            log.error("获取好友列表 friendsList = null");
            throw new SystemException(ResultEnum.FRIENDLIST_NULL);
        }
        List<Integer> homieIdList = new ArrayList<>();
        friendsList.forEach(o->homieIdList.add(o.getBId()));
        List<FriendVo> friendVoList = new ArrayList<>();

        homieIdList.forEach(o->{
            FriendVo friendVo = new FriendVo();
            UserDetail userDetail = userDetailRepository.findByUserId(o);
            BeanUtils.copyProperties(userDetail,friendVo);
            friendVo.setUserIcon(PictureToBase64.getImageStr(userDetail.getUserIcon()));
            friendVoList.add(friendVo);
        });



        return ResultVoUtil.success(friendVoList);
    }

    @Override
    public ResultVo findHomie(String userAccount) {
        Map<String,Object> homieVo = new HashMap<>();
        UserLogin userLogin = userLoginRepository.findByUserAccount(userAccount);
        if (userLogin == null){
            log.error("好友搜寻 userLogin = null");
            throw new SystemException(ResultEnum.UNkNOWN_ACCOUNT);
        }
        UserDetail userDetail = userDetailRepository.findByUserId(userLogin.getUserId());
        String userIcon = PictureToBase64.getImageStr(userDetail.getUserIcon());
        homieVo.put("userIcon",userIcon);
        homieVo.put("userName",userDetail.getUserName());
        homieVo.put("userGender",userDetail.getUserGender());
        return ResultVoUtil.success(homieVo);
    }

    @Override
    public ResultVo sendCheckMessage(NoticeDto noticeDto,HttpServletRequest request) {
        /*判断之前是否为好友*/
        if (ifFriend(noticeDto.getTo(),request)){
            log.info("[发送确认信息] 已经为好友");
            throw new SystemException(ResultEnum.DATA_ERROR);

        }
        Notice notice = getNotice(noticeDto,request);
        notice.setId(KeyUtil.genUniqueKey());
        notice.setTitle(MainViewConstant.NOTICE_FRIEND_TITLE);
        Notice noticeSave = noticeRepository.save(notice);
        log.info("发送验证信息 noticeSave={}",noticeSave);
        return ResultVoUtil.success();
    }

    @Override
    public ResultVo confirmCheckMessage(String id, String friendAccount, HttpServletRequest request) {
        /*读消息*/
        readMessage(id);
        /*判断是否已经是好友*/
        if (ifFriend(friendAccount,request)){
            log.info("确认添加好友 已经是好友 friendAccount={}",friendAccount);
            throw new SystemException(ResultEnum.DATA_ERROR);
        }

        /*保存好友 (在数据库中存在两条顺序不相同的记录)*/
        Integer friendId = getUserId(friendAccount);
        Integer ownerId = ShiroUtil.getUserId(request);
        if (friendId.equals(ownerId)){
            log.error("确认验证信息 friendId == ownerId");
            throw new SystemException(ResultEnum.DATA_ERROR);
        }
        List<Friends> friendsList = new ArrayList<>();
        Friends friendsA = new Friends();
        friendsA.setAId(friendId);
        friendsA.setBId(ownerId);

        friendsList.add(friendsA);

        Friends friendsB = new Friends();
        friendsB.setAId(ownerId);
        friendsB.setBId(friendId);

        friendsList.add(friendsB);

        List<Friends> friendsListSave = friendsRepository.saveAll(friendsList);
        log.info("确认加好友信息 friendsListSave={}",friendsListSave);
        return ResultVoUtil.success();
    }

    @Override
    public ResultVo readMessage(String id) {

        Notice notice = noticeRepository.findById(id).orElseThrow(()->new SystemException(ResultEnum.DATA_ERROR));
        notice.setStatus(NoticeEnum.READ.getCode());
        noticeRepository.save(notice);
        return ResultVoUtil.success();
    }

    @Override
    public ResultVo unreadMessage(String id) {
        Notice notice = noticeRepository.findById(id).orElseThrow(()->new SystemException(ResultEnum.DATA_ERROR));
        notice.setStatus(NoticeEnum.UNREAD.getCode());
        noticeRepository.save(notice);
        return ResultVoUtil.success();
    }


    @Override
    @Transactional
    public ResultVo deleteFriend(Integer userId,HttpServletRequest request) {
        /*可以单方面删除好友*/
        Integer ownerId = ShiroUtil.getUserId(request);
        friendsRepository.deleteByAIdAndBId(userId, ownerId);
        friendsRepository.deleteByAIdAndBId(ownerId,userId);
        return ResultVoUtil.success();
    }

    @Override
    public ResultVo sendMessage(Integer userId, String content, HttpServletRequest request) {
        Notice noticeSave = noticeRepository.save(getNotice(userId,content,request));
        log.info("发送验证信息 noticeSave={}",noticeSave);
        return ResultVoUtil.success();
    }

    @Override
    public ResultVo getOwnerMessage(HttpServletRequest request) {

        Integer userId = ShiroUtil.getUserId(request);
        List<Notice> noticeList = noticeRepository.findByNoticeTo(getUserAccount(userId));
        log.info("得到属于自己的消息 noticeList={}",noticeList);
        return ResultVoUtil.success(noticeList);
    }

    @Override
    public ResultVo getFriendCards(Integer userId) {
        UserDetail userDetail = userDetailRepository.findByUserId(userId);
        Map<String,Object> userMsg = new HashMap<>();
        userMsg.put("userName",userDetail.getUserName());
        userMsg.put("userGender",userDetail.getUserGender());
        userMsg.put("origin",userDetail.getOrigin());
        userMsg.put("education",userDetail.getEducation());
        userMsg.put("description",userDetail.getDescription());
        //设置 qq,phone,mailbox为空
        userMsg.put("qq","");
        userMsg.put("phone","");
        userMsg.put("mailbox","");
        if (userDetail.getQqStatus().equals(MsgStatusEnum.OPEN.getCode())){
            userMsg.put("qq",userDetail.getQq());
        }
        if (userDetail.getPhoneStatus().equals(MsgStatusEnum.OPEN.getCode())){
            userMsg.put("phone",userDetail.getUserPhone());
        }
        if (userDetail.getMailboxStatus().equals(MsgStatusEnum.OPEN.getCode())){
            userMsg.put("mailbox",userDetail.getUserMailbox());
        }
        log.info("获取好友名片 userMsg={}",userMsg);
        return ResultVoUtil.success(userMsg);
    }

    @Override
    public ResultVo isFriend(String userAccount, HttpServletRequest request) {
       return ResultVoUtil.success(ifFriend(userAccount,request));
    }

    @Override
    public ResultVo getUnreadMessage(HttpServletRequest request) {
        List<Notice> noticeList = noticeRepository.findByNoticeToAndStatus(getUserAccount(ShiroUtil.getUserId(request)),NoticeEnum.UNREAD.getCode());
        log.info("得到属于自己的未读消息 noticeList={}",noticeList);

        return ResultVoUtil.success(getNoticeVo(noticeList));
    }

    @Override
    public ResultVo getReadedMessage(HttpServletRequest request) {
        List<Notice> noticeList = noticeRepository.findByNoticeToAndStatus(getUserAccount(ShiroUtil.getUserId(request)),NoticeEnum.READ.getCode());
        log.info("得到属于自己的已读消息 noticeList={}",noticeList);
        return ResultVoUtil.success(getNoticeVo(noticeList));
    }
}
