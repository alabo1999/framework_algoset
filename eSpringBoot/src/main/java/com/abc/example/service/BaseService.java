package com.abc.example.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.abc.example.common.utils.Utility;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.vo.common.Page;

/**
 * @className		: BaseService
 * @description	: 服务类基类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class BaseService {
	// 账户缓存服务
	@Autowired
	protected AccountCacheService accountCacheService;
	
	// 测试开关
	protected boolean debug = false;
	
	// debug的getter
	public boolean getDebug() {
		return debug;
	}
	
	// debug的setter
	public void setDebug(boolean debug) {
		this.debug = debug; 
	}
	
	/**
	 * 
	 * @methodName		: checkValidForParams
	 * @description	: 输入参数校验，子类根据需要重载此方法
	 * @param request	: request对象
	 * @param methodName: 方法名称
	 * @param params	: 输入参数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void checkValidForParams(HttpServletRequest request,String methodName, Object params) {
		
	}
	
	/**
	 * 
	 * @methodName		: processPageInfo
	 * @description	: 处理分页信息
	 * @param params	: 包含分页信息的map
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void processPageInfo(Map<String,Object> params) {
    	// 分页处理
    	Integer pageNum = (Integer)params.get("pagenum");
    	if(Objects.isNull(pageNum)) {
    		pageNum = 1;
    	}
    	Integer pageSize = (Integer)params.get("pagesize");
    	if(Objects.isNull(pageSize)) {
    		pageSize = 10;
    	}
    	PageHelper.startPage(pageNum, pageSize);				
	}
	
	/**
	 * 
	 * @methodName		: calcPageInfo
	 * @description	: 计算分页信息
	 * @param pageNum	: 页码
	 * @param pageSize	: 每页条数
	 * @param total		: 总记录数
	 * @return			: Page对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public Page calcPageInfo(int pageNum, int pageSize,long total) {
		Page page = new Page();
		int currentPageNo = pageNum;
		
		if (pageSize <= 0) {
			page.setPageSize(10);
		}else {
			page.setPageSize(pageSize);			
		}
		
		if (pageNum <= 0) {
			currentPageNo = 1;
		}

		// 计算合适的分页
		if (total < (pageNum - 1) * pageSize) {
			currentPageNo = 1;
		}
		
		page.setPageNum(currentPageNo);
		page.setTotal(total);
		
		int pagesize = page.getPageSize();
		int remaider = (int)(total % pagesize);
		if (remaider == 0) {
			// 整除
			page.setPages((int)total/pagesize);
		}else {
			page.setPages((int)total/pagesize + 1);
		}
		
		return page;
	}
	
	/**
	 * 
	 * @methodName		: checkKeyFields
	 * @description	: 检查关键字段是否缺失
	 * @param params	: 输入参数
	 * @param fieldNames: 关键字段名
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void checkKeyFields(Map<String,Object> params,String[] fieldNames) {
		for(int i = 0; i < fieldNames.length; i++) {
			String fieldName = fieldNames[i];
			if (!params.containsKey(fieldName)) {
				String prompt = "，缺失字段：" + fieldName;
				throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR.getCode(),
						ExceptionCodes.ARGUMENTS_ERROR.getMessage() + prompt);
			}
		}
	}
	
	/**
	 * 
	 * @methodName		: getUserName
	 * @description	: 获取访问账户的用户名
	 * @param request	: request对象
	 * @return			: 访问账户的用户名
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public String getUserName(HttpServletRequest request) {
		String userName = "";
		// 获取accountId
		String accountId = accountCacheService.getId(request);
		// 获取用户名	
		Object userNameObj = accountCacheService.getAttribute(accountId, "userName");		
		if (userNameObj != null) {
			userName = (String)userNameObj;
		}else {
			// 说明此时accountId无效，或属性"username"未注册			
			return "";			
		}
		return userName;
	}
	
	//========================= 文件下载 ======================================    
	/**
	 * 
	 * @methodName		: download
	 * @description	: 下载指定路径的文件
	 * @param response	: reponse对象
	 * @param filePath	: 需要下载的文件路径
	 * @param filename	: 下载文件的文件名
	 * @param contentType	: response header中的ContentType，常用取值如下：
	 * 		普通二进制文件	: application/octet-stream
	 * 		Excel文件		: application/vnd.ms-excel
	 * 		文本文件		: text/plain; charset=utf-8
	 * 		html文件		: text/html; charset=utf-8
	 * 		xml文件			: text/xml; charset=utf-8
	 * 		jpeg文件		: image/jpeg
	 * @throws Exception	: 异常发生时，抛出。没有异常，说明下载成功。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void download(HttpServletResponse response,String filePath,
			String filename,String contentType) throws Exception{
    	
    	File file = new File(filePath);
    	if (file.exists()) {
            //设置读取流的缓存为1K
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null; 
            try {
                //设置ContentType
    			response.setContentType(contentType);
    			// 下载文件能正常显示中文
    			//String filename = filePath.substring(filePath.lastIndexOf("/")+1);
    			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
    			// 获取输入流
    			fis = new FileInputStream(file);
    			bis = new BufferedInputStream(fis);
    			// 输出流
    			OutputStream os = response.getOutputStream();
    			int len = bis.read(buffer);
    			while (len != -1) {
    				os.write(buffer, 0, len);
    				len = bis.read(buffer);
    			}             	
            }catch(Exception e) {
            	throw e;            	
            }finally {
    			// 关闭流
    			Utility.closeStream(bis);
    			Utility.closeStream(fis);            	
            }			
    	} 
    }	
	
	/**
	 * 
	 * @methodName		: downloadStream
	 * @description	: 下载指定路径的文件
	 * @param response	: reponse对象
	 * @param is		: 需要下载的文件流
	 * @param filename	: 下载文件的文件名
	 * @param contentType	: response header中的ContentType，常用取值如下：
	 * 		普通二进制文件	: application/octet-stream
	 * 		Excel文件		: application/vnd.ms-excel
	 * 		文本文件		: text/plain; charset=utf-8
	 * 		html文件		: text/html; charset=utf-8
	 * 		xml文件			: text/xml; charset=utf-8
	 * 		jpeg文件		: image/jpeg
	 * @throws Exception	: 异常发生时，抛出。没有异常，说明下载成功。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void downloadStream(HttpServletResponse response,InputStream is,
			String filename,String contentType) throws Exception{
    	
        // 设置读取流的缓存为1K
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null; 
        try {
            // 设置ContentType
    		response.setContentType(contentType);
    		// 下载文件能正常显示中文
    		//String filename = filePath.substring(filePath.lastIndexOf("/")+1);
    		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
    		// 获取输入流
    		bis = new BufferedInputStream(is);
    		// 输出流
    		OutputStream os = response.getOutputStream();
    		int len = bis.read(buffer);
    		while (len != -1) {
    			os.write(buffer, 0, len);
    			len = bis.read(buffer);
    		}         	
        }catch(Exception e) {
        	throw e;
        }finally {
    		// 关闭流
        	Utility.closeStream(bis);
        }
    }		
}
