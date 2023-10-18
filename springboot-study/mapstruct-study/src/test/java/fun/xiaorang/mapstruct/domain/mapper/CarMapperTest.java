package fun.xiaorang.mapstruct.domain.mapper;

import fun.xiaorang.mapstruct.domain.dto.CarDto;
import fun.xiaorang.mapstruct.domain.enity.Car;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/18 11:55
 */
class CarMapperTest {
    @Test
    public void shouldMapCarToDto() {
        //given
        Car car = new Car("Morris", 5, Car.CarType.SEDAN);
        //when
        CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);
        //then
        assertThat(carDto).isNotNull();
        assertThat(carDto.getMake()).isEqualTo("Morris");
        assertThat(carDto.getSeatCount()).isEqualTo(5);
        assertThat(carDto.getType()).isEqualTo("SEDAN");
    }
}