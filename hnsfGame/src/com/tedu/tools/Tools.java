package com.tedu.tools;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;


//地图大小（方块单位_像素）：40*29_800*580
//方块大小：1*1_20*20
//基地大小：2*2_40*40
//基地外围：360,560;360,540;360,520;380,520;400,520;420,520;420,540;420,560;

/**
 * @说明 地图配置文件的工具类，与游戏无关
 */

public class Tools {
	
	private static String opString = "";	//操作名
	
	/*
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		
		
		//以下可能要按需更改
		//----------------------------------------------------------------------
		String filePathString = "R://1.txt";	//读取文件
		int operator = 1;	//选择操作，选择完后要去相应的函数更改相关参数
		//1：方块去重
		//2：方块移位
		//----------------------------------------------------------------------
		
		
		FileInputStream fileInputStream;
		Properties pro = new Properties();
		String resultString = "";		//每种操作的最后结果
		try {
			fileInputStream = new FileInputStream(filePathString);
			pro.load(fileInputStream);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	
		switch(operator) {
		
			case 1:
				opString = "方块去重";
				for (String key : pro.stringPropertyNames()) {	//对每一行遍历（即每一种方块）
					resultString += (key +"=");
		            String line = pro.getProperty(key);
		            String result = removeDuplicates(line);		//去重
		            resultString += result;
		            resultString += "\n";
		        }
				break;
				
			case 2:
				opString = "方块移位";
				for (String key : pro.stringPropertyNames()) {	//对每一行遍历（即每一种方块）
					resultString += (key +"=");
		            String line = pro.getProperty(key);
		            
		            
		            //----------------------------------------------------------------------
		            String result = moveBlocks(line,1,1,38,29,2,0);		//移位
		            //第一、二个数字：左上角的方块单元位置(m,n)
		            //第三、四个数字：右下角的方块单元位置(m,n)
		            //第五、六个数字：区域内方块的移动单元数(m,n)：m正右移负左移，n正下移负上移
		            //----------------------------------------------------------------------
		            if(result.equals("error")) {
		            	fail();
		            	return;
		            }
		            
		            //moveBlocks(String line, int begin_m, int begin_n, int end_m, int end_n, int change_m, int change_n)
		            resultString += result;
		            resultString += "\n";
		        }
				break;
				
			default:
				fail();
				return;
			
		}

		writeFile(filePathString, resultString);
		System.out.println(opString+" 操作完成");
		
	}
	*/
	
	
	//覆盖重写原文件（注意是否需要先保存副本）
	private static void writeFile(String filePath, String content) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	//操作失败
	private static void fail() {
		System.out.println(opString+" 操作失败，未进行任何修改");
	}
	
	
	//1.方块去重
	private static String removeDuplicates(String line) {

        String[] dataUnits = line.split(";");	//以分号分割，存放一行中的每个数据单元
        Set<String> dataSet = new HashSet<>(Arrays.asList(dataUnits));	//转存到一个集合中（去重）
        
        String result = String.join(";", dataSet);	//重新加回分号
        result += ";";
        return result;
		
	}


	//2.方块移位
	//begin为左上，end为右下，m和n为方块位置，从1开始，change为移动的方块数，横向右正左负，纵向下正上负
	private static String moveBlocks(String line, int begin_m, int begin_n, int end_m, int end_n, int change_m, int change_n) { 
		String[] dataUnits = line.split(";");	//以分号分割，存放一行中的每个数据单元
        List<String> init_dataList = new ArrayList<String>(Arrays.asList(dataUnits));
        List<String> changed_dataList = new ArrayList<String>();
        for(String single: init_dataList) {
        	Integer init_m = ((Integer.parseInt(single.split(",")[0]))/20)+1;
        	Integer init_n = ((Integer.parseInt(single.split(",")[1]))/20)+1;
        	if(init_m >= begin_m && init_m <= end_m && init_n >= begin_n && init_n <= end_n) {
        		init_m = (init_m+change_m-1)*20;	//顺便直接换回像素位置
        		init_n = (init_n+change_n-1)*20;
        		
        		if(init_m<0 || init_m>780 || init_n<0 || init_n>560) {	//可能需要修改边界
        			System.out.println("移位后有方块超出边界");
        			return "error";
        		}
        		changed_dataList.add(init_m+","+init_n);
        		continue;
        	}
        	changed_dataList.add(single);
        }
        
        String result=String.join(";", changed_dataList);	//重新加回分号
        result += ";";
        return result;
	}
}

