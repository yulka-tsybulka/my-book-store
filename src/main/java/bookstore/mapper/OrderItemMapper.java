package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.order.OrderItemDto;
import bookstore.model.OrderItem;
import java.util.Set;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Named("toDto")
    OrderItemDto toDto(OrderItem orderItem);

    @IterableMapping(qualifiedByName = "toDto")
    Set<OrderItemDto> toDtoSet(Set<OrderItem> orderItems);
}
