package com.longmenhuarun.Service;


import com.longmenhuarun.Vo.SsdsVo;
import com.longmenhuarun.model.PldsMsg;
import com.longmenhuarun.model.SsdsMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PldsService {
   PldsMsg anaCustomFile(String filepath,String filename);
}
