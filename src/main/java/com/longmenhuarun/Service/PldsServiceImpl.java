package com.longmenhuarun.Service;

import com.longmenhuarun.model.PldsMsg;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/18 17:19
 * 4
 */
@Service
public class PldsServiceImpl implements PldsService {
    @Override
    public PldsMsg anaCustomFile(String filePath, String fileName) {
        try {
        BufferedReader breader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + fileName), "GBK"));
            System.out.println(breader.readLine());
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
