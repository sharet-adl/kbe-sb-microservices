package guru.sfg.brewery.order.service.web.mappers;

import com.netflix.discovery.converters.Auto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import guru.sfg.brewery.order.service.domain.BeerOrderLine;
import guru.sfg.brewery.order.service.services.BeerService;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(uses = {DateMapper.class})
@DecoratedWith(BeerOrderLineMapperDecorator.class)
public interface BeerOrderLineMapper {
    BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line);

    //@Mapping(target = "beerId", expression = "java(getBeerIdFromUpc(BeerOrderLineDto))")
    BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto);
}
