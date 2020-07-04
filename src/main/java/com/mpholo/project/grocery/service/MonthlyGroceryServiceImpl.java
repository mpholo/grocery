package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.mapper.MonthlyGroceryMapper;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.repositories.MonthlyGroceryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public void save(MonthlyGroceryDTO monthlyGroceryDTO) {

        monthlyGroceryRepository.save(monthlyGroceryMapper.monthlyGroceryDTOToMonthlyGrocery(monthlyGroceryDTO));
    }

    @Override
    public MonthlyGroceryDTO findByPeriod(String period) {
        return monthlyGroceryMapper.monthlyGroceryToMonthlyGroceryDTO(monthlyGroceryRepository.findByPeriod(period));
    }
}

