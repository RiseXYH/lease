package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.*;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.atguigu.lease.web.admin.vo.fee.FeeValueVo;
import com.atguigu.lease.web.admin.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Builder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {
//    注入mapper
    @Autowired
    //报错idea问题不影响
    private ApartmentInfoMapper apartmentInfoMapper;
    //    2.查询图片列表
    @Autowired
    private GraphInfoMapper graphInfoMapper;
    //    查询标签列表
    @Autowired
    private LabelInfoMapper labelInfoMapper;
//    查询配套列表
    @Autowired
    private FacilityInfoMapper facilityInfoMapper;
//    查询杂费列表
    @Autowired
    private FeeValueMapper feeValueMapper;
    //    删除图片列表
    @Autowired
    private GraphInfoService graphInfoService;
    //    删除配套列表关系
    @Autowired
    private ApartmentFacilityService apartmentInfo;
    @Autowired
    private ApartmentLabelService apartmentLabelService;

    //    删除杂费列表
    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;

    @Override
    public void saveOrUpdateApartmentSubmitVo(ApartmentSubmitVo apartmentSubmitVo) {
        boolean isUpdate = apartmentSubmitVo.getId() != null;
        super.saveOrUpdate(apartmentSubmitVo);

        if (isUpdate) {

//            1.删除图片列表
            LambdaQueryWrapper<GraphInfo> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            objectLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
            objectLambdaQueryWrapper.eq(GraphInfo::getItemId, apartmentSubmitVo.getId());
            graphInfoService.remove(objectLambdaQueryWrapper);
//            2.删除配套列表关系
            LambdaQueryWrapper<ApartmentFacility> facilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            facilityLambdaQueryWrapper.eq(ApartmentFacility::getApartmentId, apartmentSubmitVo.getId());
            apartmentInfo.remove(facilityLambdaQueryWrapper);
//            3.删除标签列表
            LambdaQueryWrapper<ApartmentLabel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ApartmentLabel::getApartmentId, apartmentSubmitVo.getId());
            apartmentLabelService.remove(lambdaQueryWrapper);
//            4.删除杂费列表
            LambdaQueryWrapper<ApartmentFeeValue> feeQueryWrapper = new LambdaQueryWrapper<>();
            feeQueryWrapper.eq(ApartmentFeeValue::getApartmentId, apartmentSubmitVo.getId());
            apartmentFeeValueService.remove(feeQueryWrapper);


        }
//       1. 保存图片列表
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        if (!CollectionUtils.isEmpty(graphVoList)) {
            ArrayList<GraphInfo> graphInfoList = new ArrayList<>();
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setItemId(apartmentSubmitVo.getId());
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfoList.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfoList);
        }
        //     2.保存配套列表关系
        List<Long> facilityInfoIdList = apartmentSubmitVo.getFacilityInfoIds();
        ArrayList<ApartmentFacility> facility = new ArrayList<>();
        if (!CollectionUtils.isEmpty(facilityInfoIdList)) {
            for (Long facilityInfoId : facilityInfoIdList) {
//                因为这个类加了lombok的@Builder注解，所以用不了构造器，用build静态方法创建对象就可
                ApartmentFacility apartmentFacility = ApartmentFacility.builder().build();
                apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
                apartmentFacility.setFacilityId(facilityInfoId);
                facility.add(apartmentFacility);
            }
            apartmentInfo.saveBatch(facility);
        }
        //3.插入标签列表
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!CollectionUtils.isEmpty(labelIds)) {
            List<ApartmentLabel> apartmentLabelList = new ArrayList<>();
            for (Long labelId : labelIds) {
                ApartmentLabel apartmentLabel = ApartmentLabel.builder().build();
                apartmentLabel.setApartmentId(apartmentSubmitVo.getId());
                apartmentLabel.setLabelId(labelId);
                apartmentLabelList.add(apartmentLabel);
            }
            apartmentLabelService.saveBatch(apartmentLabelList);
        }


        //4.插入杂费列表
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!CollectionUtils.isEmpty(feeValueIds)) {
            ArrayList<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>();
            for (Long feeValueId : feeValueIds) {
                ApartmentFeeValue apartmentFeeValue = ApartmentFeeValue.builder().build();
                apartmentFeeValue.setApartmentId(apartmentSubmitVo.getId());
                apartmentFeeValue.setFeeValueId(feeValueId);
                apartmentFeeValueList.add(apartmentFeeValue);
            }
            apartmentFeeValueService.saveBatch(apartmentFeeValueList);
        }
    }


/*分页查询方法*/
@Override
    public IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo) {

        return apartmentInfoMapper.pageItem(page, queryVo);
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
//    1.查询公寓信息
   ApartmentInfo  apartmentInfo = apartmentInfoMapper.selectById(id);
//    2.查询图片列表
       List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndItemId(ItemType.APARTMENT, id);
//    3.查询标签列表
       List<LabelInfo> LabelInfoList = labelInfoMapper.selectListByApartmentId(id);
//    4.查询配套列表
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentId(id);

//    5.查询杂费列表
       List<FeeValueVo>  feeValueVoListList =  feeValueMapper.selectListByApartmentId(id);
       // 6.组装结果
       ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
       //BeanUtils工具类copyProperties
        BeanUtils.copyProperties(apartmentInfo,apartmentDetailVo);
//        set四个查询列表
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(LabelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setFeeValueVoList(feeValueVoListList);

        return apartmentDetailVo;
    }
}




