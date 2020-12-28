package com.dashboard.eleonore.dashboard.service;

import com.dashboard.eleonore.dashboard.dto.CustomerDTO;
import com.dashboard.eleonore.dashboard.dto.DashboardDTO;
import com.dashboard.eleonore.dashboard.mapper.CustomerMapper;
import com.dashboard.eleonore.dashboard.mapper.DashboardMapper;
import com.dashboard.eleonore.dashboard.repository.CustomerRepository;
import com.dashboard.eleonore.dashboard.repository.DashboardRepository;
import com.dashboard.eleonore.dashboard.repository.entity.Customer;
import com.dashboard.eleonore.dashboard.repository.entity.Dashboard;
import com.dashboard.eleonore.element.service.ElementsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;
    private final CustomerRepository customerRepository;
    private final ElementsService elementsService;
    private final DashboardMapper dashboardMapper;
    private final CustomerMapper customerMapper;

    public DashboardServiceImpl(DashboardRepository dashboardRepository, CustomerRepository customerRepository, ElementsService elementsService) {
        this.dashboardRepository = dashboardRepository;
        this.customerRepository = customerRepository;
        this.elementsService = elementsService;
        this.dashboardMapper = Mappers.getMapper(DashboardMapper.class);
        this.customerMapper = Mappers.getMapper(CustomerMapper.class);
    }

    @Override
    public DashboardDTO saveDashboard(DashboardDTO dashboardDTO) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (dashboardDTO.getCreatedDate() == null) {
            dashboardDTO.setCreatedDate(currentDateTime);
        }

        if (dashboardDTO.getModifiedDate() == null) {
            dashboardDTO.setModifiedDate(currentDateTime);
        }

        Dashboard dashboard = this.dashboardRepository.save(this.dashboardMapper.dashboardDTOToDashboard(dashboardDTO));

        return this.dashboardMapper.dashboardToDashboardDTO(dashboard);
    }

    @Override
    public void deleteDashboard(Long id, Long profileId) {
        if (id == null || profileId == null) {
            log.warn("eleonore - Impossible to delete a dashboard if the dashboard id and/or the profile id is/are null");
            return;
        }

        Optional<Dashboard> optionalDashboard = this.dashboardRepository.findById(id, profileId);
        if (optionalDashboard.isEmpty()) {
            return;
        }

        deleteDashboard(optionalDashboard.get(), profileId);
    }

    @Override
    @Transactional
    public void deleteDashboard(String name, Long profileId) {
        if (StringUtils.isEmpty(name) || profileId == null) {
            log.warn("eleonore - Impossible to delete a dashboard if the dashboard name and/or the profile id is/are null");
            return;
        }

        Optional<Dashboard> optionalDashboard = this.dashboardRepository.findByName(name, profileId);
        if (optionalDashboard.isEmpty()) {
            return;
        }

        deleteDashboard(optionalDashboard.get(), profileId);
    }

    @Override
    public Optional<DashboardDTO> getDashboardById(Long id, Long profileId) {
        var optionalDashboard = this.dashboardRepository.findById(id, profileId);
        DashboardDTO dashboardDTO = null;
        if (optionalDashboard.isPresent()) {
            dashboardDTO = this.dashboardMapper.dashboardToDashboardDTO(optionalDashboard.get());
            dashboardDTO.setElements(this.elementsService.getElements(dashboardDTO.getId()));
        }

        return Optional.ofNullable(dashboardDTO);
    }

    @Override
    public Optional<DashboardDTO> getDashboardByName(String name, Long profileId) {
        var optionalDashboard = this.dashboardRepository.findByName(name, profileId);
        DashboardDTO dashboardDTO = null;
        if (optionalDashboard.isPresent()) {
            dashboardDTO = this.dashboardMapper.dashboardToDashboardDTO(optionalDashboard.get());
            dashboardDTO.setElements(this.elementsService.getElements(dashboardDTO.getId()));
        }

        return Optional.ofNullable(dashboardDTO);
    }

    @Override
    public List<DashboardDTO> getDashboards(Long profileId) {
        return this.dashboardRepository.findAllByProfileId(profileId)
                .stream()
                .map(this.dashboardMapper::dashboardToDashboardDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = this.customerRepository.save(this.customerMapper.customerDTOToCustomer(customerDTO));

        return this.customerMapper.customerToCustomerDTO(customer);
    }

    //////////////////////////////////////////////////////////
    //////////////////// Private methods ////////////////////
    /////////////////////////////////////////////////////////

    private void deleteDashboard(Dashboard dashboard, Long profileId) {
        List<Customer> customers = this.customerRepository.findAllByDashboardId(dashboard.getId());

        // Case 1: The customer is the dashboard owner and he is alone to use it
        if (customers.size() == 1 && profileId.equals(customers.get(0).getProfileId())) {
            this.elementsService.deleteElements(dashboard.getId());
            this.customerRepository.delete(customers.get(0));
            this.dashboardRepository.delete(dashboard);
        } else if (customers.size() > 1) {
            Optional<Customer> optionalCustomer = customers.stream().filter(c -> profileId.equals(c.getProfileId())).findFirst();
            // Case 2: The customer is not the dashboard owner, the link between the dashboard and the profile is just removed
            if (optionalCustomer.isPresent() && !optionalCustomer.get().isOwner()) {
                this.customerRepository.delete(optionalCustomer.get());
            }
            // Case 3: The customer is the owner of a shared dashboard
            else if (optionalCustomer.isPresent() && optionalCustomer.get().isOwner()) {
                List<CustomerDTO> customerDTOList = customers.stream()
                        .filter(customer -> !profileId.equals(customer.getProfileId()))
                        .map(this.customerMapper::customerToCustomerDTO)
                        .collect(Collectors.toList());
                // First each customer is associated with a copy of the removal shared dashboard
                copyDashboard(this.dashboardMapper.dashboardToDashboardDTO(dashboard), customerDTOList);
                // Then removal dashboard elements are deleted
                this.elementsService.deleteElements(dashboard.getId());
                // And removal dashboard customers too
                this.customerRepository.deleteAll(customers);
                // To finish to remove the dashboard
                this.dashboardRepository.delete(dashboard);
            }
        }
    }

    private void copyDashboard(DashboardDTO dashboardDTO, List<CustomerDTO> customerDTOList) {
        if (dashboardDTO.getId() != null) {
            dashboardDTO.setId(null);
        }

        customerDTOList.forEach(customerDTO -> {
            var dashboardCopy = this.dashboardRepository.save(
                    this.dashboardMapper.dashboardDTOToDashboard(dashboardDTO));
            customerDTO.setId(null);
            customerDTO.setOwner(true);
            customerDTO.setEditable(true);
            var dashboardIdOriginal = customerDTO.getDashboardId();
            customerDTO.setDashboardId(dashboardCopy.getId());
            this.customerRepository.save(this.customerMapper.customerDTOToCustomer(customerDTO));
            this.elementsService.copyDashboardElements(dashboardIdOriginal, dashboardCopy.getId());
        });
    }
}
