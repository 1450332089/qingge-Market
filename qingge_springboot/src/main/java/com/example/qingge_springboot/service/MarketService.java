package com.example.qingge_springboot.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.qingge_springboot.common.Constants;
import com.example.qingge_springboot.common.Result;
import com.example.qingge_springboot.entity.dto.MessageDTO;
import com.example.qingge_springboot.entity.market.MarketGood;
import com.example.qingge_springboot.entity.market.MarketMessage;
import com.example.qingge_springboot.entity.market.MarketSchool;
import com.example.qingge_springboot.exception.ServiceException;
import com.example.qingge_springboot.mapper.MarketMapper;
import com.example.qingge_springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MarketService {
    @Resource
    private MarketMapper marketMapper;

    public List<MarketSchool> getAllSchools() {
        return marketMapper.getAllSchools();
    }

    public List<MarketGood> getSchoolGoods(int schoolId) {
        return marketMapper.getSchoolGoods(schoolId);
    }

    public MarketGood getGoodById(int goodId) {
        return marketMapper.getGoodById(goodId);
    }

    public void sendMessage(MarketMessage message) {
        if(message.getUserId()==null){
            throw new ServiceException(Constants.CODE_402,"用户状态错误");
        }
        if(StrUtil.isBlank(message.getMessage())){
            throw new ServiceException(Constants.CODE_402,"评论为空");
        }
        message.setCreateTime(DateUtil.now());
        marketMapper.sendMessage(message);
    }

    public List<MessageDTO> getMessageByGoodId(Integer goodId) {
        return marketMapper.getMessageByGoodId(goodId);
    }

    public boolean delMessage(Integer id) {
        return marketMapper.delMessage(id);
    }

    @Transactional
    public void uploadGood(MarketGood marketGood,Integer schoolId) {
        //TODO: updateORinsert
        //插入商品信息
        marketGood.setUserId(TokenUtils.getCurrentUser().getId());
        marketGood.setState("在售");
        marketMapper.uploadGood(marketGood);
        //插入商品学校关系
        marketMapper.addSchoolGood(schoolId,marketGood.getId());
    }

    public List<MarketGood> getGoodByUserId(Integer userId) {
        return marketMapper.getGoodByUserId(userId);
    }
}
