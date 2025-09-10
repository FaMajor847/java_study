package org.mav.example.limits.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.mav.example.limits.domain.Limit;
import org.mav.example.limits.service.LimitService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/limits")
public class LimitController {
    private final LimitService limitService;

    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }

    @GetMapping("/{clientId}/today")
    public Limit today(@PathVariable @NotNull @Min(1) Long clientId) {
        return limitService.ensure(clientId, LocalDate.now());
    }

    @PostMapping("/reset-now")
    public void resetNow() {
        limitService.prepareTodayForAllKnownClients();
    }

}
