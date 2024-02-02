package guru.sfg.brewery.order.service.web.mappers;

import com.netflix.discovery.converters.Auto;
import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import guru.sfg.brewery.order.service.domain.BeerOrderLine;
import guru.sfg.brewery.order.service.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
//@AllArgsConstructor
//@NoArgsConstructor
public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper {

    @Autowired
    private BeerService beerService;

    @Autowired
    private BeerOrderLineMapper beerOrderLineMapper;

    // Cannot use, due to diff in DTO and ENT, where DTO contains upc(required) which is nor persisted
//    @Autowired
//    @Qualifier("delegate")
//    public void setBeerOrderLineMapper(BeerOrderLineMapper beerOrderLineMapper) {
//        this.beerOrderLineMapper = beerOrderLineMapper;
//    }

    @Autowired
    private DateMapper dateMapper;

    @Override
    public BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto)  {
        if ( dto == null ) {
            return null;
        }

        BeerOrderLine.BeerOrderLineBuilder beerOrderLine = BeerOrderLine.builder();

        beerOrderLine.id( dto.getId() );
        if ( dto.getVersion() != null ) {
            beerOrderLine.version( dto.getVersion().longValue() );
        }
        beerOrderLine.createdDate( dateMapper.asTimestamp( dto.getCreatedDate() ) );
        beerOrderLine.lastModifiedDate( dateMapper.asTimestamp( dto.getLastModifiedDate() ) );
        beerOrderLine.beerId(beerService.getBeerByUpc(dto.getUpc()).get().getId());
        beerOrderLine.orderQuantity( dto.getOrderQuantity() );
        beerOrderLine.quantityAllocated( dto.getQuantityAllocated() );

        return beerOrderLine.build();
    }

    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {
        BeerOrderLineDto orderLineDto = beerOrderLineMapper.beerOrderLineToDto(line);
        Optional<BeerDto> beerDtoOptional = beerService.getBeerById(line.getBeerId());

        beerDtoOptional.ifPresent(beerDto -> {
            orderLineDto.setBeerName(beerDto.getBeerName());
            orderLineDto.setBeerStyle(beerDto.getBeerName());
            orderLineDto.setUpc(beerDto.getUpc());
            orderLineDto.setPrice(beerDto.getPrice());
        });

        return orderLineDto;
    }
}
