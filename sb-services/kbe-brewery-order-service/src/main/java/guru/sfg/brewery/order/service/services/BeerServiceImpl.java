package guru.sfg.brewery.order.service.services;

import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerPagedList;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

//@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
//@EnableConfigurationProperties
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    public static final String BEER_PATH_V1 = "/api/v1/beer/";
    public static final String BEER_UPC_PATH_V1 = "/api/v1/beerUpc/";
    //private final RestTemplate restTemplate;
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${sfg.brewery.beer-service-host}")
    private String beerServiceHost;

//    public BeerServiceImpl(RestTemplateBuilder restTemplateBuilder) {
//        this.restTemplate = restTemplateBuilder.build();
//    }

    @Override
    public Optional<BeerDto> getBeerById(UUID uuid){
        RestTemplate restTemplate = restTemplateBuilder.build();
        return Optional.ofNullable(restTemplate.getForObject(beerServiceHost + BEER_PATH_V1 + uuid.toString(), BeerDto.class));
    }

    @Override
    public Optional<BeerDto> getBeerByUpc(String upc) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        return Optional.ofNullable(restTemplate.getForObject(beerServiceHost + BEER_UPC_PATH_V1 + upc, BeerDto.class));
    }

    @Override
    public Optional<BeerPagedList> getListofBeers() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        return Optional.ofNullable(restTemplate.getForObject(beerServiceHost + BEER_PATH_V1, BeerPagedList.class));
    }

//    public void setBeerServiceHost(String beerServiceHost) {
//        this.beerServiceHost = beerServiceHost;
//    }
}
