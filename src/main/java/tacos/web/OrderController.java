package tacos.web;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import tacos.TacoOrder;
import tacos.User;
import tacos.data.OrderRepository;

@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    private final OrderRepository orderRepo;

    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping
    public String ordersForUser(
            @AuthenticationPrincipal User user,
            Model model) {

        Pageable pageable = PageRequest.of(0, 20);

        model.addAttribute(
                "orders",
                orderRepo.findByUserIdOrderByPlacedAtDesc(
                        user.getId(), pageable)
        );

        return "orderList";
    }

    @PostMapping
    public String processOrder(
            @Valid TacoOrder order,
            Errors errors,
            SessionStatus sessionStatus,
            @AuthenticationPrincipal User user) {

        if (errors.hasErrors()) {
            return "orderForm";
        }

        // Associate the order with the logged-in user
        order.setUserId(user.getId());

        // Save the order in MongoDB
        orderRepo.save(order);

        // Clear the completed session attributes
        sessionStatus.setComplete();

        return "redirect:/";
    }
}
