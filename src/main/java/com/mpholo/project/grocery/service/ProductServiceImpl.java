package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.mapper.ProductMapper;
import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDTO findById(Integer id) {

        return productMapper.ProductToProductDTO(productRepository.findById(id).orElse(null));
    }

    @Override
    public List<ProductDTO> findAll() {

        return productRepository.findAll()
                .stream()
                .map(productMapper::ProductToProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
      productRepository.deleteById(id);
    }

    @Override
    public void delete(ProductDTO productDTO) {
       productRepository.delete(productMapper.ProductDTOToProduct(productDTO));
    }

    @Override
    public void save(ProductDTO product) {

        productRepository.save(productMapper.ProductDTOToProduct(product));
    }


    @Override
    public ProductDTO findByProductName(String name) {
        return productMapper.ProductToProductDTO(productRepository.findByProductName(name));
    }
}
