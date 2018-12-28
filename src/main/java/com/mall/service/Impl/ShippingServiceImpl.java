package com.mall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mall.common.ServerResponse;
import com.mall.dao.ShippingMapper;
import com.mall.pojo.Shipping;
import com.mall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
@Autowired
private ShippingMapper shippingMapper;
    public ServerResponse add(Integer userId, Shipping shipping){
shipping.setUserId(userId);
int rowCount=shippingMapper.insert(shipping);
if(rowCount>0){
    Map mapResult=Maps.newHashMap();
    mapResult.put("shippingId",shipping.getId());
    return  ServerResponse.createBySuccess("地址新增成功",mapResult);
}
return  ServerResponse.createByErrorMessage("地址新增失败");
    }



    public ServerResponse del(Integer userId,Integer shoppingId){
//防止用户越权删除，重写mybatis-生成器生成的增加sql语句
int rowCount=shippingMapper.deleteByShippingIdUserId(shoppingId,userId);
if(rowCount>0){
    return  ServerResponse.createBySuccessMessage("删除成功");
}
return  ServerResponse.createByErrorMessage("删除失败");
    }


    public ServerResponse update(Integer userId,Shipping shipping){
//防止恶意用户提交虚假的ShippingId数据，重新赋值为当前用户的
shipping.setUserId(userId);
        int rowCount=shippingMapper.updateByShipping(shipping);
        if(rowCount>0){
            return  ServerResponse.createByErrorMessage("删除成功");
        }
        return  ServerResponse.createByErrorMessage("删除失败");
    }
    public ServerResponse<Shipping> select(Integer userId,Integer shippingId){
Shipping shipping =shippingMapper.selectByUserIdShippingId(shippingId,userId);
if(shipping!=null){
    return  ServerResponse.createBySuccess("更新成功",shipping);
}
return ServerResponse.createByErrorMessage("查询无此记录");
    }

public ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize){
PageHelper.startPage(pageNum,pageSize);
    List<Shipping> shippingList=shippingMapper.selectByUserId(userId);
PageInfo pageInfo=new PageInfo(shippingList);
return  ServerResponse.createBySuccess(pageInfo);
}


    public ServerResponse<Shipping> defaultAddress(Integer userId) {
         Shipping shipping=shippingMapper.getDefaultAddress(userId);
         if(shipping!=null){
             return ServerResponse.createBySuccess(shipping);
         }
         return ServerResponse.createByErrorMessage("暂无地址数据，请选择或新增一个默认地址");
    }

}
