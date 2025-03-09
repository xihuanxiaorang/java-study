package fun.xiaorang.study.mapstruct.quickstart.mapper;

import fun.xiaorang.study.mapstruct.quickstart.pojo.dto.CarDTO;
import fun.xiaorang.study.mapstruct.quickstart.pojo.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/03/01 18:39
 */
@Mapper
public interface CarMapper {
  CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

  @Mapping(source = "numberOfSeats", target = "seatCount")
  CarDTO carToCarDTO(Car car);
}
