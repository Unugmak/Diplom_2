package orders;

import java.util.ArrayList;
import java.util.List;

public class OrderData {
        private List<String> ingredients;

        public OrderData(List<String> ingredients) {
                this.ingredients = ingredients;
        }

        public OrderData() {
                ingredients = new ArrayList<>();
        }

        public List<String> getIngredients() {
                return ingredients;
        }

        public void setIngredients(List<String> ingredients) {
                this.ingredients = ingredients;
        }
}