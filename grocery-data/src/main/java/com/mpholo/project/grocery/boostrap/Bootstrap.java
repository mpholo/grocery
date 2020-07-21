package com.mpholo.project.grocery.boostrap;

import com.mpholo.project.grocery.domain.GroceryBasket;
import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.repositories.GroceryBasketRepository;
import com.mpholo.project.grocery.repositories.MonthlyGroceryRepository;
import com.mpholo.project.grocery.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class Bootstrap implements CommandLineRunner {

    private ProductRepository productRepository;
    private MonthlyGroceryRepository monthlyGroceryRepository;
    private GroceryBasketRepository groceryBasketRepository;

    private List<Product> products = new ArrayList<>();
    private  List<MonthlyGrocery> monthlyGroceries = new ArrayList<>();

    public Bootstrap(ProductRepository productRepository,
                     MonthlyGroceryRepository monthlyGroceryRepository,
                     GroceryBasketRepository groceryBasketRepository) {

        this.productRepository = productRepository;
        this.monthlyGroceryRepository = monthlyGroceryRepository;
        this.groceryBasketRepository = groceryBasketRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        createProductData();
        createMonthlyGrocery();
        createGroceryBasket();

    }

    private void createProductData() {
        Product salt = new Product();
        salt.setProductId(1);
        salt.setProductName("Salt");
        salt.setProductDescription("Himalayan salts");
        salt.setProductPrice(23.87);

        Product milk = new Product();
        milk.setProductName("Milk");
        milk.setProductDescription("Fresh milk");
        milk.setProductPrice(22.88);
        milk.setProductId(2);

        Product cheese = new Product();
        cheese.setProductId(3);
        cheese.setProductName("Cheese");
        cheese.setProductDescription("Cheddar Cheese");
        cheese.setProductPrice(78.0);

        Product coffee = new Product();
        coffee.setProductId(4);
        coffee.setProductName("Coffee");
        coffee.setProductDescription("Jacobs coffee");
        coffee.setProductPrice(102.5);

        Product rice = new Product();
        rice.setProductId(5);
        rice.setProductName("Rice");
        rice.setProductDescription("Tastic brown rice");
        rice.setProductPrice(30.5);

        products.add(salt);
        products.add(milk);
        products.add(cheese);
        products.add(coffee);
        products.add(rice);

        productRepository.save(salt);
        productRepository.save(milk);
        productRepository.save(cheese);
        productRepository.save(coffee);
        productRepository.save(rice);

        System.out.println("Products Loaded = "+productRepository.count());
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


        MonthlyGrocery month4 = new MonthlyGrocery();
        month4.setMonthlyGroceryId(4);
        month4.setBudgetAmount(5500.00);
        month4.setPeriod("April 2020");
        month4.setStartDate(LocalDate.of(2020,04,01));
        month4.setEndDate(LocalDate.of(2020,04,30));

        monthlyGroceries.add(month1);
        monthlyGroceries.add(month2);
        monthlyGroceries.add(month3);
        monthlyGroceries.add(month4);

        monthlyGroceryRepository.save(month1);
        monthlyGroceryRepository.save(month2);
        monthlyGroceryRepository.save(month3);
        monthlyGroceryRepository.save(month4);
        System.out.println("Monthly budgets"+monthlyGroceryRepository.count());


    }

    private void createGroceryBasket() {

        GroceryBasket item1 =new GroceryBasket();
        item1.setGroceryBasketId(1);
        item1.setActualPrice(5.0);
        item1.setQuantity(2);
        item1.setMonthlyGrocery(monthlyGroceries.get(0));
        item1.setProduct(products.get(0));

        GroceryBasket item2 =new GroceryBasket();
        item2.setGroceryBasketId(2);
        item2.setActualPrice(10.0);
        item2.setQuantity(1);
        item2.setMonthlyGrocery(monthlyGroceries.get(0));
        item2.setProduct(products.get(1));


        GroceryBasket item3 =new GroceryBasket();
        item3.setGroceryBasketId(3);
        item3.setActualPrice(15.0);
        item3.setQuantity(1);
        item3.setMonthlyGrocery(monthlyGroceries.get(0));
        item3.setProduct(products.get(2));

        GroceryBasket item4 =new GroceryBasket();
        item4.setGroceryBasketId(4);
        item4.setActualPrice(50.0);
        item4.setQuantity(3);
        item4.setMonthlyGrocery(monthlyGroceries.get(0));
        item4.setProduct(products.get(3));

        List<GroceryBasket>  groceryBasketList = Arrays.asList(item1,item2,item3,item4);

        groceryBasketRepository.save(item1);
        groceryBasketRepository.save(item2);
        groceryBasketRepository.save(item3);
        groceryBasketRepository.save(item4);
        System.out.println("Number of MonthlyGrocery items "+groceryBasketRepository.count());


    }
}
