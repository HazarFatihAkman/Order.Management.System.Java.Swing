package extension;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import domain.models.Order;

public class SelectedProductsExtension {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(List<Order> products) throws Exception {
        return mapper.writeValueAsString(products);
    }

    public static List<Order> fromJson(String json) throws Exception {
        return mapper
            .readValue(json,
                mapper
                .getTypeFactory()
                .constructCollectionLikeType(List.class, Order.class)
            );
    }
}
