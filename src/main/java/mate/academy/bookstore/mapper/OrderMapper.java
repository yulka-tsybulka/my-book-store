package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.order.OrderDto;
import bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);
}
