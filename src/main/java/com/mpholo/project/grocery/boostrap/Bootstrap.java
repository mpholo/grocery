package com.mpholo.project.grocery.boostrap;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.repositories.MonthlyGroceryRepository;
import com.mpholo.project.grocery.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Bootstrap implements CommandLineRunner {

    private ProductRepository productRepository;
    private MonthlyGroceryRepository monthlyGroceryRepository;

    public Bootstrap(ProductRepository productRepository, MonthlyGroceryRepository monthlyGroceryRepository) {
        this.productRepository = productRepository;
        this.monthlyGroceryRepository = monthlyGroceryRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        createProductData();
        createMonthlyGrocery();

    }

    private void createProductData() {
        Product salt = new Product();
        salt.setProductId(1);
        salt.setProductName("Salt");
        salt.setProductDescription("Salt Description");

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

    private void createMonthlyGrocery() {
        MonthlyGrocery month1 = new MonthlyGrocery();
        month1.setMonthlyGroceryId(1);
        month1.setBudgetAmount(5500.00);
        month1.setPeriod("January 2020");
        month1.setStartDate(LocalDate.of(2020,01,01));
        month1.setEndDate(LocalDate.of(2020,01,31));

        MonthlyGrocery month2 = new MonthlyGrocery();
        month2.setMonthlyGroceryId(2);
        month2.setBudgetAmount(5500.00);
        month2.setPeriod("February 2020");
        month2.setStartDate(LocalDate.of(2020,02,01));
        month2.setEndDate(LocalDate.of(2020,02,29));



        MonthlyGrocery month3 = new MonthlyGrocery();
        month3.setMonthlyGroceryId(3);
        month3.setBudgetAmount(5500.00);
        month3.setPeriod("March 2020");
        month3.setStartDate(LocalDate.of(2020,03,01));
        month3.setEndDate(LocalDate.of(2020,03,29));

        monthlyGroceryRepository.save(month1);
        monthlyGroceryRepository.save(month2);
        monthlyGroceryRepository.save(month3);



    }
}
