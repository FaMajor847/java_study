package org.mav.example.limits.service;

import jakarta.transaction.Transactional;
import org.mav.example.limits.config.LimitsProperties;
import org.mav.example.limits.domain.Limit;
import org.mav.example.limits.exception.LimitExceededException;
import org.mav.example.limits.repository.LimitRepository;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LimitService {
    private final LimitRepository repo;
    private final LimitsProperties props;

    public LimitService(LimitRepository repo, LimitsProperties props) {
        this.repo = repo;
        this.props = props;
    }

    /** Найти или создать запись лимита на конкретный день. */
    @Transactional
    public Limit ensure(Long clientId, LocalDate day) {
        return repo.findByClientIdAndDay(clientId, day)
                .orElseGet(() -> {
                    Limit l = new Limit();
                    l.setClientId(clientId);
                    l.setDay(day);
                    l.setRemaining(props.getDefaultAmount());
                    return repo.save(l);
                });
    }

    /** Зарезервировать сумму из дневного лимита клиента (сегодня). */
    @Transactional
    public Limit tryReserve(Long clientId, BigDecimal amount) throws LimitExceededException {
        LocalDate today = LocalDate.now();
        Limit l = ensure(clientId, today);

        if (l.getRemaining().compareTo(amount) < 0) {
            throw new LimitExceededException("Daily limit exceeded for clientId=" + clientId);
        }
        l.setRemaining(l.getRemaining().subtract(amount));
        return repo.save(l); // @Version защитит от гонок
    }

    /** Вернуть сумму в лимит (например, при неуспешном платеже). */
    @Transactional
    public Limit restore(Long clientId, BigDecimal amount) {
        LocalDate today = LocalDate.now();
        Limit l = ensure(clientId, today);
        l.setRemaining(l.getRemaining().add(amount));
        return repo.save(l);
    }

    @Transactional
    public void prepareTodayForAllKnownClients() {
        List<Long> clients = repo.findDistinctClientIds();
        LocalDate today = LocalDate.now();
        for (Long clientId : clients) {
            Limit l = repo.findByClientIdAndDay(clientId, today).orElse(null);
            if (l == null) {
                ensure(clientId, today);
            } else {
                l.setRemaining(props.getDefaultAmount());
                repo.save(l);
            }
        }
    }

}
