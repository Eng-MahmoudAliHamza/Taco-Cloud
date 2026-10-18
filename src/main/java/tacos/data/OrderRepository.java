
package tacos.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tacos.TacoOrder;

public interface OrderRepository
        extends JpaRepository<TacoOrder, Long> {

    List<TacoOrder> findByUserIdOrderByPlacedAtDesc(
            Long userId,
            Pageable pageable
    );
}
