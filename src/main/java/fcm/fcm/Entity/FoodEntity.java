package fcm.fcm.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FoodEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
