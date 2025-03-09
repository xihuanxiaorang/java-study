package fun.xiaorang.study.mapstruct.quickstart.convert;

import fun.xiaorang.study.mapstruct.quickstart.mapper.CarMapper;
import fun.xiaorang.study.mapstruct.quickstart.pojo.dto.CarDTO;
import fun.xiaorang.study.mapstruct.quickstart.pojo.entity.Car;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/03/01 19:03
 */
class CarMapperTest {
  @Test
  public void shouldMapCarToCarDTO() {
    // given
    final Car car = new Car("Morris", 5, Car.CarType.SEDAN);

    // when
    final CarDTO carDTO = CarMapper.INSTANCE.carToCarDTO(car);

    // then
    assertNotNull(carDTO);
    assertEquals("Morris", carDTO.getMake());
    assertEquals(5, carDTO.getSeatCount());
    assertEquals("SEDAN", carDTO.getType());
  }
}