package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.mapper.MonthlyGroceryMapper;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.repositories.MonthlyGroceryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MonthlyGroceryServiceImpl  implements  MonthlyGroceryService {

    private final MonthlyGroceryRepository monthlyGroceryRepository;
    private MonthlyGroceryMapper monthlyGroceryMapper;

    public MonthlyGroceryServiceImpl(MonthlyGroceryRepository monthlyGroceryRepository, MonthlyGroceryMapper monthlyGroceryMapper) {
        this.monthlyGroceryRepository = monthlyGroceryRepository;
        this.monthlyGroceryMapper = monthlyGroceryMapper;
    }

    @Override
    public Optional<MonthlyGroceryDTO> findById(Integer id) {
        return Optional.of(monthlyGroceryRepository.findById(id)
                .map(monthlyGroceryMapper::monthlyGroceryToMonthlyGroceryDTO)
                .orElseThrow(RuntimeException::new));
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

    private MonthlyGroceryDTO saveAndReturn(MonthlyGrocery monthlyGrocery) {
        MonthlyGrocery savedMonthlyGrocery =monthlyGroceryRepository.save(monthlyGrocery);
        return monthlyGroceryMapper.monthlyGroceryToMonthlyGroceryDTO(savedMonthlyGrocery);
    }


}

