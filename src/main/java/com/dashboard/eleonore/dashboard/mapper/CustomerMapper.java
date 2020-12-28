package com.dashboard.eleonore.dashboard.mapper;

import com.dashboard.eleonore.dashboard.dto.CustomerDTO;
import com.dashboard.eleonore.dashboard.repository.entity.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    CustomerDTO customerToCustomerDTO(Customer customer);

    Customer customerDTOToCustomer(CustomerDTO customerDTO);
}
