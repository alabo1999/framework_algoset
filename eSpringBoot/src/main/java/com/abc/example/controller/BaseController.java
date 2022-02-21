package com.abc.example.controller;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.abc.example.exception.ExceptionCodes;
import com.abc.example.vo.common.BaseResponse;
import com.abc.example.vo.common.Page;
import com.github.pagehelper.PageInfo;

/**
 * @className		: BaseController
 * @description	: 控制器基类
 * @summary		: 支持事务处理，并提供操作成功的方法
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@RestController
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class BaseController {
	/**
	 * 
	 * @methodName		: successResponse
	 * @description	: 操作成功，返回信息不含数据体
	 * @param <T>		: 模板类型
	 * @return			: 操作成功的返回码和消息，不含数据体
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    protected <T> BaseResponse<T> successResponse() {
        BaseResponse<T> response = new BaseResponse<T>();
        response.setCode(ExceptionCodes.SUCCESS.getCode());
        response.setMessage(ExceptionCodes.SUCCESS.getMessage());
        return response;
    }

    /**
     * 
     * @methodName		: successResponse
     * @description	: 操作成功，返回信息含数据体
     * @param <T>		: 模板类型
     * @param data		: 模板类型的数据
     * @return			: 操作成功的返回码和消息，并包含数据体
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    protected <T> BaseResponse<T> successResponse(T data) {
        BaseResponse<T> response = new BaseResponse<T>();
        response.setCode(ExceptionCodes.SUCCESS.getCode());
        response.setMessage(ExceptionCodes.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    /**
     * 
     * @methodName		: successResponse
     * @description	: 操作成功，返回信息含数据体和分页信息
     * @param <T>		: 模板类型
     * @param pageInfo	: 分页信息
     * @return			: 操作成功的返回码和消息，并包含数据体和分页信息
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    protected <T> BaseResponse<List<T>> successResponse(PageInfo<T> pageInfo) {
        Page page = new Page();
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setPages(pageInfo.getPages());
        page.setTotal(pageInfo.getTotal());

        BaseResponse<List<T>> response = new BaseResponse<List<T>>();
        response.setCode(ExceptionCodes.SUCCESS.getCode());
        response.setMessage(ExceptionCodes.SUCCESS.getMessage());
        response.setData(pageInfo.getList());
        response.setPage(page);
        return response;
    }

}
