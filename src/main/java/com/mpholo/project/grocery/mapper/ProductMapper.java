package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.model.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO ProductToProductDTO(Product product);
    Product ProductDTOToProduct(ProductDTO productDTO);


}
