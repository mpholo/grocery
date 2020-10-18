package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.model.ProductDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-10-18T10:23:15+0200",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.8 (Ubuntu)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO ProductToProductDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setProductId( product.getProductId() );
        productDTO.setProductName( product.getProductName() );
        productDTO.setProductDescription( product.getProductDescription() );
        productDTO.setProductPrice( product.getProductPrice() );

        return productDTO;
    }

    @Override
    public Product ProductDTOToProduct(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product product = new Product();

        product.setProductId( productDTO.getProductId() );
        product.setProductName( productDTO.getProductName() );
        product.setProductDescription( productDTO.getProductDescription() );
        product.setProductPrice( productDTO.getProductPrice() );

        return product;
    }
}
