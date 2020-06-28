package com.mpholo.project.grocery.boostrap;

import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private ProductRepository productRepository;

    public Bootstrap(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        Product salt = new Product();
        salt.setProductId(1);
        salt.setProductName("Salt");

        Product milk = new Product();
        milk.setProductName("Milk");
        milk.setProductId(2);

        Product cheese = new Product();
        cheese.setProductId(3);
        cheese.setProductName("Cheese");

        Product coffee = new Product();
        coffee.setProductId(4);
        coffee.setProductName("Coffee");

        Product rice = new Product();
        rice.setProductId(5);
        rice.setProductName("Rice");

        productRepository.save(salt);
        productRepository.save(milk);
        productRepository.save(cheese);
        productRepository.save(coffee);
        productRepository.save(rice);

        System.out.println("Data Loaded = "+productRepository.count());

    }
}
