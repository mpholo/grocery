package com.mpholo.project.grocery.bootstrap;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.repositories.MonthlyGroceryRepository;
import com.mpholo.project.grocery.repositories.ProductRepository;
import com.mpholo.project.grocery.security.domain.AccountStatus;
import com.mpholo.project.grocery.security.domain.Role;
import com.mpholo.project.grocery.security.domain.User;
import com.mpholo.project.grocery.security.domain.UserRole;
import com.mpholo.project.grocery.security.repositories.RoleRepository;
import com.mpholo.project.grocery.security.repositories.UserRepository;
import com.mpholo.project.grocery.security.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Ensures the pre-existing (single-tenant) grocery data has an owner once
 * per-user isolation is introduced: creates the active "kinny" account and
 * assigns every owner-less product and month to it. Idempotent — after the
 * first run everything already has an owner, so it's a no-op.
 */
@Component
@Order(2)
@Slf4j
public class KinnyOwnershipBootstrap implements CommandLineRunner {

    private static final String KINNY_USERNAME = "kinny";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final MonthlyGroceryRepository monthlyGroceryRepository;

    public KinnyOwnershipBootstrap(UserRepository userRepository, RoleRepository roleRepository,
                                   BCryptPasswordEncoder passwordEncoder,
                                   ProductRepository productRepository,
                                   MonthlyGroceryRepository monthlyGroceryRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
        this.monthlyGroceryRepository = monthlyGroceryRepository;
    }

    @Override
    public void run(String... args) {
        User kinny = userRepository.findByUsername(KINNY_USERNAME).orElseGet(this::createKinny);

        int products = 0;
        for (Product product : productRepository.findAll()) {
            if (product.getOwner() == null) {
                product.setOwner(kinny);
                productRepository.save(product);
                products++;
            }
        }
        int months = 0;
        for (MonthlyGrocery month : monthlyGroceryRepository.findAll()) {
            if (month.getOwner() == null) {
                month.setOwner(kinny);
                monthlyGroceryRepository.save(month);
                months++;
            }
        }
        if (products > 0 || months > 0) {
            log.info("Assigned {} products and {} months to '{}'", products, months, KINNY_USERNAME);
        }
    }

    private User createKinny() {
        Role userRole = roleRepository.findByName(AuthService.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not configured"));
        User u = new User();
        u.setUsername(KINNY_USERNAME);
        u.setEmail("kinny@grocery.local");
        u.setPassword(passwordEncoder.encode("kinny"));
        u.setFirstName("Kinny");
        u.setLastName("");
        u.setEnabled(true);
        u.setStatus(AccountStatus.ACTIVE);
        u.setCreatedDate(LocalDateTime.now());
        Set<UserRole> roles = new HashSet<>();
        roles.add(new UserRole(u, userRole));
        u.setUserRoles(roles);
        User saved = userRepository.save(u);
        log.info("Created '{}' account", KINNY_USERNAME);
        return saved;
    }
}
