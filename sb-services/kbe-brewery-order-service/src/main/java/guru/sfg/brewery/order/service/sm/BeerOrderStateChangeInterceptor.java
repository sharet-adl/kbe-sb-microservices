package guru.sfg.brewery.order.service.sm;

import guru.sfg.brewery.model.OrderStatusUpdate;
import guru.sfg.brewery.order.service.domain.BeerOrder;
import guru.sfg.brewery.order.service.domain.BeerOrderEventEnum;
import guru.sfg.brewery.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.brewery.order.service.repositories.BeerOrderRepository;
import guru.sfg.brewery.order.service.services.BeerOrderManagerImpl;
import guru.sfg.brewery.order.service.web.mappers.DateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static guru.sfg.brewery.order.service.services.BeerOrderManagerImpl.ORDER_OBJECT_HEADER;

/**
 * Created by jt on 2019-09-08.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderStateChangeInterceptor implements StateMachineInterceptor<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final RestTemplateBuilder restTemplateBuilder;
    private final DateMapper dateMapper = new DateMapper();


    @Override
    public Message<BeerOrderEventEnum> preEvent(Message<BeerOrderEventEnum> message, StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine) {
        return null;
    }

    @Transactional
    @Override
    public void preStateChange(State<BeerOrderStatusEnum, BeerOrderEventEnum> state, Message<BeerOrderEventEnum> message, Transition<BeerOrderStatusEnum, BeerOrderEventEnum> transition, StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine, StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine1) {
        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault(BeerOrderManagerImpl.ORDER_ID_HEADER, " ")))
                .ifPresent(orderId -> {
                    log.debug("Saving state for order id: " + orderId + " Status: " + state.getId());

                    BeerOrder beerOrder = beerOrderRepository.getOne(UUID.fromString(orderId));
                    beerOrder.setOrderStatus(state.getId());
                    beerOrderRepository.saveAndFlush(beerOrder);
                });
    }

    @Override
    public void postStateChange(State<BeerOrderStatusEnum, BeerOrderEventEnum> state, Message<BeerOrderEventEnum> message, Transition<BeerOrderStatusEnum, BeerOrderEventEnum> transition, StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine, StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine1) {
        log.debug("Post State Change");

        BeerOrder beerOrder = stateMachine.getExtendedState()
                .get(ORDER_OBJECT_HEADER, BeerOrder.class);

        try{
            if (beerOrder.getOrderStatusCallbackUrl() != null) {

                OrderStatusUpdate update = OrderStatusUpdate.builder()
                        .id(beerOrder.getId())
                        .orderId(beerOrder.getId())
                        .version(beerOrder.getVersion() != null ? beerOrder.getVersion().intValue() : null)
                        .createdDate(dateMapper.asOffsetDateTime(beerOrder.getCreatedDate()))
                        .lastModifiedDate(dateMapper.asOffsetDateTime(beerOrder.getLastModifiedDate()))
                        .orderStatus(beerOrder.getOrderStatus() != null ? beerOrder.getOrderStatus().toString() : null)
                        .customerRef(beerOrder.getCustomerRef())
                        .build();

                log.debug("Posting to callback url");
                RestTemplate restTemplate = restTemplateBuilder.build();
                restTemplate.postForObject(beerOrder.getOrderStatusCallbackUrl(), update, String.class);
            }
        } catch (Throwable t){
            log.error("Error Preforming callback for order: " + beerOrder.getId(), t);
        }

        log.debug("Post State change complete");
    }

    @Override
    public StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> preTransition(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        return null;
    }

    @Override
    public StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> postTransition(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        return null;
    }

    @Override
    public Exception stateMachineError(StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine, Exception e) {
        return null;
    }

}
