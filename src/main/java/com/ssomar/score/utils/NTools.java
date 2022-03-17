package com.ssomar.score.utils;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class NTools implements Serializable {
	
	public static boolean isNumber(String s) {
		try {
			Double.valueOf(s);
		}
		catch(NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static double reduceDouble(@NotNull double number, int numbersAfterComma){
		/* Limit numbers after , */
		StringBuilder sb = new StringBuilder("");
		int limit = numbersAfterComma;
		boolean startCount = false;
		int cpt = 0;
		for(char c : (number+"").toCharArray()){
			if(cpt == limit){
				break;
			}
			else if(startCount){
				cpt++;
			}
			else if(c == ',' || c == '.'){
				startCount = true;
			}
			sb.append(c);
		}
		try{
			return Double.parseDouble(sb.toString());
		}catch (Exception e){
			e.printStackTrace();
			return number;
		}
	}
}
