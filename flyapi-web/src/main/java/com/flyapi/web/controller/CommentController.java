package com.flyapi.web.controller;

import com.flyapi.core.base.BaseController;
import com.flyapi.core.constant.JSONResult;
import com.flyapi.model.CmsComment;
import com.flyapi.model.UcenterUser;
import com.flyapi.pojo.dto.CommentDto;
import com.flyapi.service.api.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: qfwang
 * Date: 2017-11-1 上午11:13
 */
@Controller
public class CommentController extends BaseController{
    @Autowired
    private CommentService commentService;

    /**
     * 获取评论列表
     * @title: findCommentList
     * @author qfwang
     * @params [pageNum, pageSize, articleId]
     * @return com.flyapi.core.constant.JSONResult
     * @date 2017/11/1 下午5:57
     */
    @ResponseBody
    @GetMapping("comment/{articleId}")
    public JSONResult findCommentList(int pageNum, int pageSize, @PathVariable long articleId){

        PageInfo<CmsComment> pageInfo = null;
        PageHelper.startPage(pageNum, pageSize);
        try{
            List<CmsComment> list = commentService.findCommentById(articleId);
            pageInfo = new PageInfo<CmsComment>(list);
        }catch (Exception ex){
            return JSONResult.error();
        }

        return JSONResult.ok(pageInfo);
    }
    /**
     * 发表评论或回复
     * @title: addComment
     * @author flyhero <http://www.iflyapi.cn>
     * @param commentDto
     * @return com.flyapi.core.constant.JSONResult
     * @date 2018/3/15 下午11:49
     */
    @ResponseBody
    @PostMapping("comment")
    public JSONResult addComment(CommentDto commentDto){
        UcenterUser user = (UcenterUser) currentUser();
        if(user == null){
            return JSONResult.error();
        }
        int num = commentService.comment(commentDto,user.getUserId());
        return num >0 ? JSONResult.ok() : JSONResult.error();
    }

    /**
     * 标记为已读
     * @title: updateComment
     * @author flyhero <http://www.iflyapi.cn>
     * @param commentId （0 表示全部）
     * @return com.flyapi.core.constant.JSONResult
     * @date 2018/3/18 上午12:17
     */
    @PutMapping("comment/read/{commentId}")
    @ResponseBody
    public JSONResult updateComment(@PathVariable Long commentId){

        UcenterUser user = (UcenterUser) currentUser();
        if(user == null){
            return JSONResult.error();
        }
        boolean flag = commentService.readComment(commentId,user.getUserId());
        return flag ? JSONResult.ok():JSONResult.error();
    }
}
