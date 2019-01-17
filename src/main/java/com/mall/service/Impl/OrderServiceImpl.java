package com.mall.service.Impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.dao.*;
import com.mall.pojo.*;
import com.mall.service.IOrderService;
import com.mall.util.BigDecimalUtil;
import com.mall.util.DateTimeUtil;
import com.mall.util.FtpUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.OrderItemVo;
import com.mall.vo.OrderProductVo;
import com.mall.vo.OrderVo;
import com.mall.vo.ShippingVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {
    private static  final Logger log=LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
private PayInfoMapper payInfoMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderMapper orderMapper;
@Autowired
private CartMapper cartMapper;
@Autowired
private ProductMapper productMapper;
@Autowired
private ShippingMapper shippingMapper;
    private static  AlipayTradeService tradeService;
    static {

        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }
    public ServerResponse createOrder(Integer userId,Integer shippingId){
List<Cart> cartList=cartMapper.selectCheckedCartByUserId(userId);
ServerResponse serverResponse=this.getCartOrderItem(userId,cartList);
if(!serverResponse.isSuccess()){
    return serverResponse;
}
List<OrderItem> orderItemList= (List<OrderItem>) serverResponse.getData();
 BigDecimal payment= getOrderTotalPrice(orderItemList);
Order order=assembleOrder(userId,shippingId,payment);
if(CollectionUtils.isEmpty(orderItemList)){
    return ServerResponse.createByErrorMessage("购物车为空");
}
if(order==null){
    return ServerResponse.createByErrorMessage("订单生成错误");
}
        for (OrderItem orderItem:orderItemList
             ) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        orderItemMapper.batchInsert(orderItemList);
reduceProductStock(orderItemList);
clearCart(cartList);
OrderVo orderVo=assembleOrderVo(order,orderItemList);
return  ServerResponse.createBySuccess(orderVo);
    }
    private OrderVo assembleOrderVo(Order order,List<OrderItem> orderItemList){
OrderVo orderVo=new OrderVo();
orderVo.setOrderNo(order.getOrderNo());
orderVo.setPayment(order.getPayment());
orderVo.setPaymentType(order.getPaymentType());
orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());
orderVo.setPostage(order.getPostage());
orderVo.setStatus(order.getStatus());
orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getStatus());
orderVo.setShippingId(order.getShippingId());
Shipping shipping=shippingMapper.selectByPrimaryKey(order.getShippingId());
if(shipping!=null){
    orderVo.setReceverName(shipping.getReceiverName());
    orderVo.setShippingVo(assembleShippingVo(shipping));
}
orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));
order.setCloseTime(order.getCloseTime());
order.setCreateTime(order.getPaymentTime());
order.setEndTime(order.getEndTime());
orderVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
List<OrderItemVo > orderItemVoList=Lists.newArrayList();
        for (OrderItem orderItem: orderItemList) {
orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderVo.setOrderItemVoList(orderItemVoList);
    return  orderVo;
    }
private OrderItemVo assembleOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo=new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
    orderItemVo.setProductId(orderItem.getProductId());
    orderItemVo.setProductName(orderItem.getProductName());
    orderItemVo.setProductImage(orderItem.getProductImage());
orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
    orderItemVo.setQuantity(orderItem.getQuantity());
    orderItemVo.setTotalPrice(orderItem.getTotalPrice());
    orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
    return  orderItemVo;
}



    private ShippingVo assembleShippingVo(Shipping shipping){
        ShippingVo shippingVo=new ShippingVo();
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverPhone(shipping.getReceiverPhone());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverName(shipping.getReceiverName());
return shippingVo;

    }
private void clearCart(List<Cart> cartList){
        for (Cart cart:cartList){
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
}
    private void reduceProductStock(List<OrderItem> orderItemList){
        for (OrderItem orderItem:orderItemList) {
            Product product=productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock()-orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }
 private Order assembleOrder(Integer userId,Integer shippingId,BigDecimal payment){
     Order order = new Order();
     long orderNo = this.generateOrderNo();
     order.setOrderNo(orderNo);
     order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
     order.setPostage(0);
     order.setPaymentType(Const.PaymentTypeEnum.ON_LINE_PAY.getCode());
     order.setPayment(payment);

     order.setUserId(userId);
     order.setShippingId(shippingId);
     //发货时间等等
     //付款时间等等
int rowCount=orderMapper.insert(order);
if(rowCount>0){
    return  order;
}
return  null;
 }
 //使用时间戳生成一个唯一的订单号，并不适合实际业务
 private long generateOrderNo(){
return  System.currentTimeMillis()+new Random().nextInt(100);
 }
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList){
BigDecimal payment=new BigDecimal("0");
for(OrderItem orderItem:orderItemList){
    payment=  BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
}
return  payment;
    }
    private ServerResponse<List<OrderItem>> getCartOrderItem(Integer userId,List<Cart> cartList){
        List<OrderItem> orderItemList=Lists.newArrayList();

        if(CollectionUtils.isEmpty(cartList)){
            return  ServerResponse.createByErrorMessage("购物车为空");
        }
        for (Cart cartItem:cartList) {
            OrderItem orderItem=new OrderItem();
            Product product=productMapper.selectByPrimaryKey(cartItem.getProductId());
            if(Const.ProductStatusEnum.ON_SALE.getCode()!=product.getStatus()){
                return  ServerResponse.createByErrorMessage("该商品已下架"+product.getName());
            }
            if(cartItem.getQuantity()>product.getStock()){
                return  ServerResponse.createByErrorMessage("库存不足");
            }
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setCurrentUnitPrice(product.getPrice());//为价格产生快照，避免商户修改单价后无法获取价格
            orderItem.setProductImage(product.getMainImage());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartItem.getQuantity()));
            orderItemList.add(orderItem);

        }
        return  ServerResponse.createBySuccess(orderItemList);
    }



    public ServerResponse  pay(Integer userId, Long orderNo, String path){
Map<String,String> resultMap=Maps.newHashMap();
Order order=orderMapper.selectByUserIdOrderNo(userId,orderNo);
if(order==null){
    return  ServerResponse.createBySuccessMessage("结账信息异常，请重试");
}
        resultMap.put("orderNo",String.valueOf(order.getOrderNo()));
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店消费”
        String subject = new StringBuilder().append("CJ商城-扫码支付-订单号").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount =order.getPayment().toString();

        // (必填) 付款条码，用户支付宝钱包手机app点击“付款”产生的付款条码
        String authCode = "用户自己的支付宝付款码"; // 条码示例，286648048691290423
        // (可选，根据需要决定是否使用) 订单可打折金额，可以配合商家平台配置折扣活动，如果订单部分商品参与打折，可以将部分商品总价填写至此字段，默认全部商品可打折
        // 如果该值未传入,但传入了【订单总金额】,【不可打折金额】 则该值默认为【订单总金额】- 【不可打折金额】
        //        String discountableAmount = "1.00"; //

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0.0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品3件共20.00元"
        String body = new StringBuilder().append("订单").append(order.getOrderNo()).append("共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "worker_cj";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        String providerId = "2088100200300400500";
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId(providerId);

        // 支付超时，线下扫码交易定义为5分钟
        String timeoutExpress = "5m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(userId,orderNo);
        for(OrderItem orderItem : orderItemList){
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(),new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);


        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(path);
                if(!folder.exists()){
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                String qrPath = String.format(path+"/qr-%s.png",response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png",response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File targetFile = new File(path,qrFileName);
                try {
                    FtpUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    log.error("上传二维码异常",e);
                }
                log.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile.getName();
                resultMap.put("qrUrl",qrUrl);
                return ServerResponse.createBySuccess(resultMap);
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }

    }

    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
    public  ServerResponse aLiPayCallback(Map<String,String> params){
Long orderNo=Long.parseLong(params.get("out_trade_no"));
String tradeNo=params.get("trade_no");
String tradeStatus=params.get("trade_status");
Order order=orderMapper.selectOrderByOrderNo(orderNo);
        System.out.println(tradeNo+","+tradeStatus);
        System.out.println("order"+order);
if(order==null){
    return ServerResponse.createByErrorMessage("查无此记录");
}
if(order.getStatus()>= Const.OrderStatusEnum.PAID.getCode()){
return  ServerResponse.createBySuccessMessage("支付宝重复回调");
}
if(Const.AliPayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
    order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
order.setStatus(Const.OrderStatusEnum.PAID.getCode());
orderMapper.updateByPrimaryKeySelective(order);
}
        PayInfo payInfo=new PayInfo();
payInfo.setUserId(order.getUserId());
payInfo.setOrderNo(order.getOrderNo());
payInfo.setPayPlatform(Const.PayPlatFromEnum.ALiPay.getCode());
payInfo.setPlatformNumber(tradeNo);
payInfo.setPlatformStatus(tradeStatus);
payInfoMapper.insert(payInfo);
        System.out.println("payinfo"+payInfo);
return  ServerResponse.createBySuccess();
    }

    public ServerResponse queryPayStatus(Integer userId,Long orderNo){
Order order=orderMapper.selectByUserIdOrderNo(userId,orderNo);
if(order==null){
    return  ServerResponse.createByErrorMessage("查无此订单");
}
        if(order.getStatus()>= Const.OrderStatusEnum.PAID.getCode()){
            return  ServerResponse.createBySuccess();
        }
        return  ServerResponse.createByError();
    }

public  ServerResponse cancel(Integer userId,Long orderNo){
Order order=orderMapper.selectByUserIdOrderNo(userId,orderNo);
if(order==null){
    return ServerResponse.createByErrorMessage("订单不存在");
}
if(order.getStatus()!=Const.OrderStatusEnum.PAID.getCode()){
    return ServerResponse.createByErrorMessage("已付款,无法取消此订单");
}
Order updateOrder=new Order();
updateOrder.setId(order.getId());
updateOrder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
int row=orderMapper.updateByPrimaryKeySelective(updateOrder);
if(row>0){
    return ServerResponse.createBySuccess();
}
return  ServerResponse.createByError();
}






public ServerResponse getOrderCartProduct(Integer userId){
    OrderProductVo orderProductVo=new OrderProductVo();
    List<Cart> cartList=cartMapper.selectCheckedCartByUserId(userId);
    ServerResponse response= this.getCartOrderItem(userId,cartList);
if(!response.isSuccess()){
    return response;
}
List<OrderItem> orderItemList=(List<OrderItem>) response.getData();
List<OrderItemVo> orderItemVoList=new ArrayList<>();
BigDecimal payment=new BigDecimal("0");
for (OrderItem orderItem:orderItemList){
    payment=BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
    orderItemVoList.add(assembleOrderItemVo(orderItem));
}
orderProductVo.setProductTotalPrice(payment);
orderProductVo.setOrderItemVoList(orderItemVoList);
orderProductVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
return ServerResponse.createBySuccess(orderProductVo);


}
public ServerResponse orderDetail(Integer userId,Long orderNo){
Order order=orderMapper.selectByUserIdOrderNo(userId,orderNo);
    if(order!=null){
List<OrderItem> orderItemList=orderItemMapper.getByOrderNoUserId(userId,orderNo);
if(orderItemList!=null){
OrderVo orderVo=assembleOrderVo(order,orderItemList);
    return ServerResponse.createBySuccess(orderVo);
}

}
return ServerResponse.createByErrorMessage("无法查询到该订单的任何信息!!!");
}

public ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize){
    PageHelper.startPage(pageNum,pageSize);
    List<Order> orderList=orderMapper.getOrderListByUserId(userId);
    List<OrderVo> orderVoList=assembleOrderVoList(orderList,userId);
    PageInfo pageResult=new PageInfo(orderList);
    pageResult.setList(orderVoList);
    return ServerResponse.createBySuccess(pageResult);
}

private List<OrderVo> assembleOrderVoList(List<Order> orderList,Integer userId){
    List<OrderVo> orderVoList=Lists.newArrayList();
    for(Order order:orderList){
    List<OrderItem> orderItemList=Lists.newArrayList();
    if(userId==null){
        //方法重用，管理员调用接口
    orderItemList=orderItemMapper.getByOrderNo(order.getOrderNo());
    }else{
        orderItemList=orderItemMapper.getByOrderNoUserId(userId,order.getOrderNo());
        }
        OrderVo orderVo=assembleOrderVo(order,orderItemList);
    orderVoList.add(orderVo);
    }
    return orderVoList;
}

    //todo manage  for backend
    public  ServerResponse<PageInfo> manageList(int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList=orderMapper.selectAllOrder();
        List<OrderVo> orderVoList=this.assembleOrderVoList(orderList,null);
        PageInfo pageInfo=new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    public ServerResponse<OrderVo> manageDetail(Long orderNo){
        Order order=orderMapper.selectOrderByOrderNo(orderNo);
        if(order!=null){
            List<OrderItem> orderItemList=orderItemMapper.getByOrderNo(orderNo);
            OrderVo orderVo=assembleOrderVo(order,orderItemList);
            return ServerResponse.createBySuccess(orderVo);
        }
        return ServerResponse.createByErrorMessage("该订单不存在!!");
    }
    public ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        Order order=orderMapper.selectOrderByOrderNo(orderNo);
        if(order!=null){
            List<OrderItem> orderItemList=orderItemMapper.getByOrderNo(orderNo);
            OrderVo orderVo=assembleOrderVo(order,orderItemList);
            PageInfo pageInfo=new PageInfo(Lists.newArrayList(orderVo));
            pageInfo.setList(Lists.newArrayList(orderVo));
            return ServerResponse.createBySuccess(pageInfo);
        }
        return ServerResponse.createByErrorMessage("该订单不存在!!");
    }
    public ServerResponse<String> sendGoods (Long orderNo){
        Order order=orderMapper.selectOrderByOrderNo(orderNo);
        if(order!=null){
order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
order.setSendTime( new Date());
orderMapper.updateByPrimaryKeySelective(order);
return ServerResponse.createBySuccessMessage("发货成功!!!");
        }
return ServerResponse.createByErrorMessage("该订单不存在");
    }
    public ServerResponse<String> setShipped (Long orderNo){
        Order order=orderMapper.selectOrderByOrderNo(orderNo);
        if(order!=null){
            order.setStatus(Const.OrderStatusEnum.SUCCESS.getCode());
            order.setSendTime( new Date());
            orderMapper.updateByPrimaryKeySelective(order);
            return ServerResponse.createBySuccessMessage("签收成功!!!");
        }
        return ServerResponse.createByErrorMessage("该订单不存在");
    }
    public ServerResponse<String> closeOrder (Long orderNo){
        Order order=orderMapper.selectOrderByOrderNo(orderNo);
        if(order!=null){
            order.setStatus(Const.OrderStatusEnum.CLOSE.getCode());
            order.setSendTime( new Date());
            orderMapper.updateByPrimaryKeySelective(order);
            return ServerResponse.createBySuccessMessage("成功关闭订单!!!");
        }
        return ServerResponse.createByErrorMessage("该订单不存在");
    }

}

