package orders;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class OrderData {
        private List<String> ingredients;

        public OrderData() {
                ingredients = new ArrayList<>();
        }
}