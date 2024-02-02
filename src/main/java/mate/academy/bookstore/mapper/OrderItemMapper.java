package mate.academy.bookstore.mapper;

import java.util.Set;
import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.order.OrderItemDto;
import mate.academy.bookstore.model.OrderItem;
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
