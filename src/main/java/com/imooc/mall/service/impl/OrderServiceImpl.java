package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.OrderItemMapper;
import com.imooc.mall.dao.OrderMapper;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.enums.OrderStatusEnum;
import com.imooc.mall.enums.PaymentTypeEnum;
import com.imooc.mall.enums.ProductStatusEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.*;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.service.IOrderService;
import com.imooc.mall.vo.OrderItemVo;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.imooc.mall.enums.OrderStatusEnum.NO_PAY;

/**
 * @author shuhao
 * @date 2020/6/15 9:17
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public ResponseVo cancel(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null || !order.getUserId().equals(uid)){
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        //这里设定只有[未付款]订单可以取消,看公司业务
        if(!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())){
            return ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);
        }

        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if(row <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUid(uid);

        Set<Long> orderNoSet = orderList.stream().map(Order::getOrderNo).collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        //这里需要把获得的orderItemList转成map,对应规则为根据orderNo划分为不同的类
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream()
            .collect(Collectors.groupingBy(OrderItem::getOrderNo));

        Set<Integer> shippingIdSet = orderList.stream()
                                                .map(Order::getShippingId)
                                                .collect(Collectors.toSet());
        List<Shipping> ShippingList = shippingMapper.selectByIdSet(shippingIdSet);
        Map<Integer, Shipping> shippingMap = ShippingList.stream()
            .collect(Collectors.toMap(Shipping::getId, shipping -> shipping));


        List<OrderVo> orderVoList = new ArrayList<>();
        for(Order order : orderList){
            OrderVo orderVo = buildOrderVo(order,
                orderItemMap.get(order.getOrderNo()),
                shippingMap.get(order.getShippingId()));

            orderVoList.add(orderVo);
        }
        PageInfo pageInfo = new PageInfo(orderVoList);

        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null || !order.getUserId().equals(uid)){
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        Set<Long> orderNoSet = new HashSet<>();
        orderNoSet.add(orderNo);
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);

        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);

        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //收货地址校验(总之都要查出来)
        Shipping shipping = shippingMapper.selectByUIdAndShippingId(uid, shippingId);
        if(shipping == null){
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }

        //获取购物车,校验(是否有商品/库存)
        List<Cart> cartList = cartService.listForCart(uid).stream()
            .filter(Cart::getProductSelected)
            .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(cartList)){
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }

        //获取cartList里的productIds
        Set<Integer> productIdSet = cartList.stream()
            .map(Cart::getProductId)
            .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> map  = productList.stream()
            .collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();
        for (Cart cart : cartList) {
            //根据productId查数据库
            Product product = map.get(cart.getProductId());
            //是否有商品
            if (product == null) {
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST,
                    "商品不存在. productId = " + cart.getProductId());
            }
            //商品上下架状态
            if (!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())) {
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE,
                    "商品不是在售状态. " + product.getName());
            }

            //库存是否充足
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(ResponseEnum.PROODUCT_STOCK_ERROR,
                    "库存不正确. " + product.getName());
            }

            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            //减库存
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row <= 0) {
                return ResponseVo.error(ResponseEnum.ERROR);
            }
        }


        //计算总价，只计算选中的商品
        //生成订单，入库：order和order_item，事务
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);

        int rowForOrder = orderMapper.insertSelective(order);
        if (rowForOrder <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if (rowForOrderItem <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        //更新购物车（选中的商品）
        //Redis有事务(打包命令)，不能回滚
        for (Cart cart : cartList) {
            cartService.delete(uid, cart.getProductId());
        }

        //构造orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);

        return ResponseVo.success(orderVo);

    }



    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);

        List<OrderItemVo> OrderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(OrderItemVoList);

        if (shipping != null) {
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }

        return orderVo;
    }

    private Order buildOrder(Integer uid,
                             Long orderNo,
                             Integer shippingId,
                             List<OrderItem> orderItemList
    ) {
        BigDecimal payment = orderItemList.stream()
            .map(OrderItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(NO_PAY.getCode());
        return order;
    }


    /**
     * 企业级：分布式唯一id/主键
     * @return
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    /**
     * 构建 OrderItem 对象
     * @param uid
     * @param orderNo
     * @param quantity
     * @param product
     * @return
     */
    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }
}
