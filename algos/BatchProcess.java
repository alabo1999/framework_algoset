package com.abc.example.common.impexp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @className		: BatchProcess
 * @description	: 
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/01/20	1.0.0		sheng.zheng		初版
 *
 */
public class BatchProcess {
	
	/**
	 * 
	 * @methodName		: varStepBatchProcess
	 * @description	: 可变步长的批量处理算法
	 * @param <T>		: 泛型类型
	 * @param object1	: 提供batchProcMethod方法的类对象
	 * @param object2	: 提供singleProcMethod方法的类对象
	 * @param request	: 外部方法需要的request对象参数
	 * @param dataRowList		: 待处理的T类型对象数据列表
	 * @param normalList		: 正常处理的对象列表
	 * @param correctList		: 修改处理的对象列表
	 * @param batchProcMethod	: 正常批量处理的方法
	 * @param singleProcMethod	: 单条修正处理的方法
	 * @param errorProcFlag		: 错误处理策略标志值:0-继续处理，1-立即中止处理
	 * @param debugLevel		: 调试信息输出设置，bit0-输出修正处理信息，bit1-输出详细步骤,bit2:输出尝试次数
	 * @return					: 处理过程产生的异常日志列表
	 * @throws Exception
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public static <T> List<String> varStepBatchProcess(
			Object object1,
			Object object2,
			Object request,
			List<T> dataRowList,
			List<T> normalList,
			List<T> correctList,			
			Method batchProcMethod,
			Method singleProcMethod,
			int errorProcFlag,
			int debugLevel) throws Exception{		
		// 返回的异常信息列表
		List<String> errorList = new ArrayList<String>();			    	
    			
        // 算法原理：1+2+4+...+2^n=2^(n+1)-1
        // 引入QoS等级概念，每个等级对应一个值为2^k的批量值，最后一个等级的批量值为1。
        // 1、对于当前等级，如果异常标志为假时：
		// 1.1、成功，数据列表下标偏移量加等级批量值，则提升等级；如果当前等级为等级上限，则保持。
		// 1.2、失败，如果当前等级不是等级下限，则异常标志设置为真，数据列表下标不变，然后下降一个等级；
		// 1.3、失败，如果当前等级已是等级下限，数据列表下标偏移批量加1，记录失败日志信息，然后提升等级。
		// 2、对于当前等级，如果异常标志为真时：      
		// 2.1、成功，数据列表下标偏移量加等级批量值，然后下降等级；如果当前等级为等级下限，则保持。 
		// 2.2、失败，如果当前等级不是等级下限，数据列表下标不变，然后下降一个等级；
		// 2.3、失败，如果当前等级已是等级下限，则数据列表下标加1，记录失败日志信息，然后提升等级
		
		// QoS等级批量值数组，单调下降，且元素为2^k形式，第一个元素为最大处理能力值，最后一个元素值为1。
        int[] arrQosLevel = new int[] {128,64,32,16,8,4,2,1}; 
        // QoS等级，使用数组下标，0对应最高等级，初始值随意，这里使用最高等级作为初始值
        int levelIdx = 0;
        // 当前等级对应的批量值，相当于arrQosLevel[levelIdx]
        int levelNum = 0;
        // dataRowList数据列表下标锚点，为当前尝试各等级批量处理的开始下标
        int anchorIdx = 0;
        // 数据长度，即待处理的数据个数
        int dataLen = dataRowList.size();
        // 异常标志
        boolean bError = false;        
        // 当前实际批量值，考虑到数据列表的剩余数据量，可能不足当前等级的批量值
        int batchNum = 0;
        // 批量处理的尝试次数，用于评价算法的效率
        int tryNum = 0;
        
        // 循环处理，直到dataRowList的所有数据被处理完毕
        while(true) {
    		// 取得当前等级的批量值
    		levelNum = arrQosLevel[levelIdx];
    		// 实际可以处理的剩余批量值
    		batchNum = Math.min(dataLen - anchorIdx, levelNum);
    		if (batchNum == 0) {
    			// 剩余批量值为0，表示已处理完毕
    			break;
    		}
    		// 获取当前批量的数据子列表
    		List<T> subList = dataRowList.subList(anchorIdx, anchorIdx + batchNum);

    		if (!bError) {
        		// 如果异常标记为false        	
        		try {
        			// 尝试批量处理
        			// 尝试次数+1
        			tryNum++;
        			
        			// 为测试增加的输出
        			if ((debugLevel & 0x02) > 0) {
	        			String line = String.format("batch  tryNum = %d, levelIdx = %d,levelNum = %d,batchNum = %d,anchorIdx = %d,bError=%b", 
	        					tryNum,levelIdx,levelNum,batchNum,anchorIdx,bError);
	        			System.out.println(line);
        			}
        			
        			batchProcMethod.invoke(object1, request,subList);
        			
        			// 处理成功后，锚点移动到新位置，即下标偏移量+batchNum
        			anchorIdx += batchNum;
        			
        			// 添加正常处理的记录
        			normalList.addAll(subList);
        			
        			// 如果当前等级不是最高等级，则提升等级
        			if (levelIdx != 0) {
        				levelIdx --;
        			}
        		}catch(Exception e) {
        			// 为测试增加的输出
        			if ((debugLevel & 0x02) > 0) {
            			System.out.println(e);        				
        			}
        			
        			// 如果发生异常
        			if (levelIdx < arrQosLevel.length -1) {
            			// 如果当前等级不是最后一个等级
        				// 下降一个等级
        				levelIdx ++;
        				// 设置异常标志为true
        				bError = true;
        			}else {
        				// 如果当前等级已是最后一个等级
        				// 调用单条数据的处理方法
        				String errInfo = "";
        				try {
        					T item = dataRowList.get(anchorIdx);
                			// 尝试次数+1
                			tryNum++;
                			// 为测试增加的输出
                			if((debugLevel & 0x01) > 0) {
                    			String line = String.format("single tryNum = %d, levelIdx = %d,levelNum = %d,batchNum = %d,anchorIdx = %d,bError=%b", 
                    					tryNum,levelIdx,levelNum,batchNum,anchorIdx,bError);
                    			System.out.println(line);                				
                			}
                			
        					errInfo = (String)singleProcMethod.invoke(object2, request, e, item);
        					
	        				// 添加异常日志
	        				if (!errInfo.isEmpty()) {
	        					// 如果有异常信息
	        					errorList.add(errInfo);
	        					if((debugLevel & 0x01) > 0) {
		        					System.out.println(item.toString() + ": can not be fixed.");	        						
	        					}
	        					if(errorProcFlag == 1) {
	        						// 如果遇到异常，中止处理
	        						throw new Exception(errInfo);
	        					}
	        				}else {
	        					// 修正处理成功
	        					correctList.add(item);
	        					if((debugLevel & 0x01) > 0) {
		        					System.out.println(item.toString() + ": can be fixed.");	        						
	        					}
	        				}
	        				
	            			// 处理成功后，锚点移动到新位置，即下标偏移量+batchNum，注意此时batchNum=1
	            			anchorIdx += batchNum;
	        				
	        				// 设置异常标志为false，表示异常已消除
	        				// bError = false;
        				}catch(Exception ex) {
        					// 如果调用方法抛出异常信息
        					errInfo = ex.getMessage();
        					errorList.add(errInfo);        					
        					if(errorProcFlag == 1) {
        						// 如果遇到异常，中止处理
        						throw new Exception(errInfo);
        					}
        				}
        			}
        		}
        	}else {
        		// 如果异常标记为true
        		try {
        			// 尝试批量处理
        			// 尝试次数+1
        			tryNum++;        	
        			// 为测试增加的输出
        			if((debugLevel & 0x02) > 0) {
            			String line = String.format("batch  tryNum = %d, levelIdx = %d,levelNum = %d,batchNum = %d,anchorIdx = %d,bError=%b", 
            					tryNum,levelIdx,levelNum,batchNum,anchorIdx,bError);
            			System.out.println(line);        				
        			}
        			
        			batchProcMethod.invoke(object1, request, subList);
        			
        			// 处理成功后，锚点移动到新位置，即下标偏移量+batchNum
        			anchorIdx += batchNum;
        			
        			// 添加正常处理的记录
        			normalList.addAll(subList);
        			
        			// 如果当前等级不是最后一个等级，则下降等级
        			if (levelIdx <= arrQosLevel.length -1) {
        				levelIdx ++;
        			}
        		}catch(Exception e) {
        			// 为测试增加的输出
        			if((debugLevel & 0x02) > 0) {
        				System.out.println(e);
        			}
        			
        			// 如果发生异常
        			if (levelIdx < arrQosLevel.length -1) {
            			// 如果当前等级不是最后一个等级
        				// 下降一个等级
        				levelIdx ++;
        				// 设置异常标志为true
        				// bError = true;
        			}else {
        				// 如果当前等级已是最后一个等级
        				// 调用单条数据的处理方法
        				String errInfo = "";
        				try {
        					T item = dataRowList.get(anchorIdx);
                			// 尝试次数+1
                			tryNum++;
                			// 为测试增加的输出
                			if((debugLevel & 0x01) > 0) {
                    			String line = String.format("single tryNum = %d, levelIdx = %d,levelNum = %d,batchNum = %d,anchorIdx = %d,bError=%b", 
                    					tryNum,levelIdx,levelNum,batchNum,anchorIdx,bError);
                    			System.out.println(line);                				
                			}
                			
        					errInfo = (String)singleProcMethod.invoke(object2, request, e, item);
	        				// 添加异常日志
	        				if (!errInfo.isEmpty()) {
	        					// 如果有异常信息
	        					errorList.add(errInfo);
	        					if((debugLevel & 0x01) > 0) {
		        					System.out.println(item + ": can not be fixed.");	        						
	        					}
	        					if(errorProcFlag == 1) {
	        						// 如果遇到异常，中止处理
	        						throw new Exception(errInfo);
	        					}
	        				}else {
	        					if((debugLevel & 0x01) > 0) {
		        					System.out.println(item + ": can be fixed.");	        						
	        					}
	        					// 修正处理成功
	        					correctList.add(item);
	        				}
	        				
	            			// 处理成功后，锚点移动到新位置，即下标偏移量+batchNum，注意此时batchNum=1
	            			anchorIdx += batchNum;
	            				        				
	        				// 设置异常标志为false，表示异常已消除
	        				bError = false;
	        				
	            			// 提升等级
	        				levelIdx --;
	        				
        				}catch(Exception ex) {
        					// 如果调用方法抛出异常信息
        					errInfo = ex.getMessage();
        					errorList.add(errInfo);        					
        					if(errorProcFlag == 1) {
        						// 如果遇到异常，中止处理
        						throw new Exception(errInfo);
        					}
        				}
        			}
        		}        		
        	}
        }
        
        if((debugLevel & 0x04) > 0) {
        	System.out.println("tryNum: " + tryNum);
        }
        // 返回处理异常日志列表
        return errorList;
	}  	
	
}
