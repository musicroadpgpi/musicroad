package com.nullpoint.musicroad.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nullpoint.musicroad.config.Constants;
import com.nullpoint.musicroad.domain.Authority;
import com.nullpoint.musicroad.domain.Band;
import com.nullpoint.musicroad.domain.City;
import com.nullpoint.musicroad.domain.User;
import com.nullpoint.musicroad.domain.enumeration.Genre;
import com.nullpoint.musicroad.repository.AuthorityRepository;
import com.nullpoint.musicroad.repository.BandRepository;
import com.nullpoint.musicroad.repository.UserRepository;
import com.nullpoint.musicroad.repository.search.UserSearchRepository;
import com.nullpoint.musicroad.security.AuthoritiesConstants;
import com.nullpoint.musicroad.security.SecurityUtils;
import com.nullpoint.musicroad.service.dto.UserDTO;
import com.nullpoint.musicroad.service.util.RandomUtil;
import com.nullpoint.musicroad.web.rest.errors.EmailAlreadyUsedException;
import com.nullpoint.musicroad.web.rest.errors.InvalidPasswordException;
import com.nullpoint.musicroad.web.rest.errors.LoginAlreadyUsedException;

@Component
public class StripeService {

    @Autowired
    public StripeService() {
        Stripe.apiKey = "sk_test_eXsDnOULTLH9SkICbIpfbIw200QEiGs4GU";
    }

    public Charge chargeCreditCard(String token, double amount) throws Exception {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int)(amount * 100));
        chargeParams.put("currency", "EUR");
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        return charge;
    }
}