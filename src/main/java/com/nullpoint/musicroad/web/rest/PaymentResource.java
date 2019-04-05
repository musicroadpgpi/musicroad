package com.nullpoint.musicroad.web.rest;

import com.nullpoint.musicroad.config.Constants;
import com.nullpoint.musicroad.domain.User;
import com.nullpoint.musicroad.repository.UserRepository;
import com.nullpoint.musicroad.repository.search.UserSearchRepository;
import com.nullpoint.musicroad.security.AuthoritiesConstants;
import com.nullpoint.musicroad.service.MailService;
import com.nullpoint.musicroad.service.UserService;
import com.nullpoint.musicroad.service.dto.UserDTO;
import com.nullpoint.musicroad.web.rest.errors.BadRequestAlertException;
import com.nullpoint.musicroad.web.rest.errors.EmailAlreadyUsedException;
import com.nullpoint.musicroad.web.rest.errors.LoginAlreadyUsedException;
import com.nullpoint.musicroad.web.rest.util.HeaderUtil;
import com.nullpoint.musicroad.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

@RestController
@RequestMapping("/payment")
public class PaymentResource {

    private StripeService stripeClient;

    @Autowired
    public PaymentResource(StripeService stripeClient) {
        this.stripeClient = stripeClient;
    }

    @PostMapping("/charge")
    public Charge chargeCard(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Double amount = Double.parseDouble(request.getHeader("amount"));
        return this.stripeClient.chargeCreditCard(token, amount);
    }
}