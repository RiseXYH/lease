package com.atguigu.lease.web.admin.controller.apartment;


import com.atguigu.lease.common.result.Result;
import com.atguigu.lease.model.entity.PaymentType;
import com.atguigu.lease.web.admin.service.PaymentTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "支付方式管理")
@RequestMapping("/admin/payment")
@RestController
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService service;

    @Operation(summary = "查询全部支付方式列表")
    @GetMapping("list")

    public Result<List<PaymentType>> listPaymentType() {
        //条件查询(删除状态为0的)
       /* LambdaQueryWrapper<PaymentType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentType::getIsDeleted, 0);*/

        List<PaymentType> list = service.list();
        return Result.ok(list);
    }

    @Operation(summary = "保存或更新支付方式")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdatePaymentType(@RequestBody PaymentType paymentType) {
//       saveOrUpdate: id为null就save，不为null先找有没有对应id的唯一数据，有就修改那一条的数据
        service.saveOrUpdate(paymentType);
        return Result.ok();
    }

    @Operation(summary = "根据ID删除支付方式")
    @DeleteMapping("deleteById")
    public Result deletePaymentById(@RequestParam Long id) {
        service.removeById(id);
        return Result.ok();
    }

}















