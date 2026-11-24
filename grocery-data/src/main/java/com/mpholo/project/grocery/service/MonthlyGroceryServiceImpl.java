package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.GroceryItem;
import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.exceptions.NotFoundException;
import com.mpholo.project.grocery.mapper.MonthlyGroceryMapper;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.repositories.GroceryItemRepository;
import com.mpholo.project.grocery.repositories.MonthlyGroceryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MonthlyGroceryServiceImpl  implements  MonthlyGroceryService {

    private final MonthlyGroceryRepository monthlyGroceryRepository;
    private final GroceryItemRepository groceryItemRepository;
    private MonthlyGroceryMapper monthlyGroceryMapper;

    public MonthlyGroceryServiceImpl(MonthlyGroceryRepository monthlyGroceryRepository, GroceryItemRepository groceryItemRepository, MonthlyGroceryMapper monthlyGroceryMapper) {
        this.monthlyGroceryRepository = monthlyGroceryRepository;
        this.groceryItemRepository = groceryItemRepository;
        this.monthlyGroceryMapper = monthlyGroceryMapper;
    }

    @Override
    public Optional<MonthlyGroceryDTO> findById(Integer id) {
        return Optional.of(monthlyGroceryRepository.findById(id)
                .map(monthlyGroceryMapper::monthlyGroceryToMonthlyGroceryDTO)
                .orElseThrow(()->new NotFoundException("monthly grocery not found with ID: "+id.toString() )));
    }

    @Override
    public List<MonthlyGroceryDTO> findAll() {
        return monthlyGroceryRepository.findAll()
                .stream()
                .map(monthlyGroceryMapper::monthlyGroceryToMonthlyGroceryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        monthlyGroceryRepository.deleteById(id);

    }

    @Override
    public void delete(MonthlyGroceryDTO monthlyGroceryDTO) {
            monthlyGroceryRepository.delete(monthlyGroceryMapper.monthlyGroceryDTOToMonthlyGrocery(monthlyGroceryDTO));
    }

    @Override
    public MonthlyGroceryDTO save(MonthlyGroceryDTO monthlyGroceryDTO) {

        MonthlyGrocery monthlyGrocery = monthlyGroceryMapper.monthlyGroceryDTOToMonthlyGrocery(monthlyGroceryDTO);
        return  saveAndReturn(monthlyGrocery);

    }

    @Override
    public MonthlyGroceryDTO edit(Integer id, MonthlyGroceryDTO monthlyGroceryDTO) {
        MonthlyGrocery monthlyGrocery = monthlyGroceryMapper.monthlyGroceryDTOToMonthlyGrocery(monthlyGroceryDTO);
        return saveAndReturn(monthlyGrocery);
    }

    @Override
    public MonthlyGroceryDTO findByPeriod(String period) {
        return monthlyGroceryMapper.monthlyGroceryToMonthlyGroceryDTO(monthlyGroceryRepository.findByPeriod(period));
    }

    @Override
    public  List<MonthlyGroceryDTO> findByYear(Integer year) {

        int searchYear = year==null?LocalDate.now().getYear():year.intValue();

        log.info("getting monthly groceries for year {}",searchYear);
        BiPredicate<LocalDate, LocalDate> match =
                (startDate,endDate)-> startDate.getYear()==searchYear && endDate.getYear()==searchYear;

        List<MonthlyGroceryDTO> monthlyGroceryDTOList =  monthlyGroceryRepository.findAll()
                .stream()
                .filter(record -> match.test(record.getStartDate(),record.getEndDate()))
                .map(monthlyGroceryMapper::monthlyGroceryToMonthlyGroceryDTO)
                .collect(Collectors.toList());


        log.info("monthly groceries {}",monthlyGroceryDTOList.size());
        return monthlyGroceryDTOList;


    }

    @Transactional
    @Override
    public Optional<MonthlyGroceryDTO> copy(Integer monthlyGroceryId) {

        Optional<MonthlyGrocery> monthlyGrocery  = monthlyGroceryRepository.findById(monthlyGroceryId);

       if(monthlyGrocery.isPresent()) {


           //get last monthly grocery
           Optional<MonthlyGrocery> monthlyGroceries = monthlyGroceryRepository.findAll()
                   .stream()
                   .sorted(Comparator.comparing((MonthlyGrocery m) -> m.getStartDate()).reversed())
                   .findFirst();

           if (monthlyGroceries.isPresent()) {
               MonthlyGrocery lastMonthGrocery = monthlyGroceries.get();
               MonthlyGrocery newMonthlyGrocery = new MonthlyGrocery();
               newMonthlyGrocery.setStartDate(lastMonthGrocery.getStartDate().plusMonths(1));
               newMonthlyGrocery.setEndDate(lastMonthGrocery.getEndDate().plusMonths(1));
               Month monthName = newMonthlyGrocery.getEndDate().getMonth();
               String newPeriod = newMonthlyGrocery.getStartDate().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
               newMonthlyGrocery.setPeriod(newPeriod);
               newMonthlyGrocery.setBudgetAmount(monthlyGrocery.get().getBudgetAmount());

               MonthlyGrocery  savedMonthlyGrocery =monthlyGroceryRepository.save(newMonthlyGrocery);

               savedMonthlyGrocery.addItems(monthlyGrocery.get().getGroceryItems());
               groceryItemRepository.saveAll(savedMonthlyGrocery.getGroceryItems());
               return Optional.of(saveAndReturn(savedMonthlyGrocery));
           }
       }

        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<MonthlyGroceryDTO> copyToMonth(Integer monthlyGroceryId, int month, int year, Double budgetAmount) {

        Optional<MonthlyGrocery> source = monthlyGroceryRepository.findById(monthlyGroceryId);
        if (!source.isPresent()) {
            return Optional.empty();
        }

        MonthlyGrocery src = source.get();
        YearMonth yearMonth = YearMonth.of(year, month);

        MonthlyGrocery newMonthlyGrocery = new MonthlyGrocery();
        newMonthlyGrocery.setStartDate(yearMonth.atDay(1));
        newMonthlyGrocery.setEndDate(yearMonth.atEndOfMonth());
        newMonthlyGrocery.setPeriod(yearMonth.atDay(1).format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        newMonthlyGrocery.setBudgetAmount(budgetAmount != null ? budgetAmount : src.getBudgetAmount());

        MonthlyGrocery savedMonthlyGrocery = monthlyGroceryRepository.save(newMonthlyGrocery);

        // addItems() clones each item without the purchased flag, so copies start
        // "not in basket" (toggle reset) as required.
        savedMonthlyGrocery.addItems(src.getGroceryItems());
        groceryItemRepository.saveAll(savedMonthlyGrocery.getGroceryItems());

        log.info("copied monthly grocery {} into {} ({} items)",
                monthlyGroceryId, newMonthlyGrocery.getPeriod(), savedMonthlyGrocery.getGroceryItems().size());

        return Optional.of(saveAndReturn(savedMonthlyGrocery));
    }

    @Transactional
    @Override
    public Optional<MonthlyGroceryDTO> updateBudget(Integer monthlyGroceryId, double budgetAmount) {
        return monthlyGroceryRepository.findById(monthlyGroceryId)
                .map(monthlyGrocery -> {
                    monthlyGrocery.setBudgetAmount(budgetAmount);
                    return saveAndReturn(monthlyGrocery);
                });
    }

    @Transactional
    @Override
    public boolean deleteIfBasketEmpty(Integer monthlyGroceryId) {
        MonthlyGrocery monthlyGrocery = monthlyGroceryRepository.findById(monthlyGroceryId)
                .orElseThrow(() -> new NotFoundException("Monthly grocery not found for id " + monthlyGroceryId));

        boolean hasBasketItems = monthlyGrocery.getGroceryItems().stream()
                .anyMatch(GroceryItem::isPurchased);
        if (hasBasketItems) {
            return false;
        }

        // Remove the month's items first to satisfy the foreign key, then the month.
        groceryItemRepository.deleteAll(monthlyGrocery.getGroceryItems());
        monthlyGroceryRepository.delete(monthlyGrocery);
        return true;
    }

    private MonthlyGroceryDTO saveAndReturn(MonthlyGrocery monthlyGrocery) {
        MonthlyGrocery savedMonthlyGrocery =monthlyGroceryRepository.save(monthlyGrocery);
        return monthlyGroceryMapper.monthlyGroceryToMonthlyGroceryDTO(savedMonthlyGrocery);
    }


}

