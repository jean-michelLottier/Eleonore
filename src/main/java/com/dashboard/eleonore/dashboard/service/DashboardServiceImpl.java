package com.dashboard.eleonore.dashboard.service;

import com.dashboard.eleonore.element.service.ElementService;
import com.dashboard.eleonore.dashboard.dto.CustomerDTO;
import com.dashboard.eleonore.dashboard.dto.DashboardDTO;
import com.dashboard.eleonore.dashboard.repository.CustomerRepository;
import com.dashboard.eleonore.dashboard.repository.DashboardRepository;
import com.dashboard.eleonore.dashboard.repository.entity.Customer;
import com.dashboard.eleonore.dashboard.repository.entity.Dashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DashboardServiceImpl implements DashboardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ElementService elementService;

    @Override
    public DashboardDTO saveDashboard(DashboardDTO dashboardDTO) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (dashboardDTO.getCreatedDate() == null) {
            dashboardDTO.setCreatedDate(currentDateTime);
        }

        if (dashboardDTO.getModifiedDate() == null) {
            dashboardDTO.setModifiedDate(currentDateTime);
        }

        Dashboard dashboard = this.dashboardRepository.save(new Dashboard(dashboardDTO));

        return new DashboardDTO(dashboard);
    }

    @Override
    public void deleteDashboard(Long id, Long profileId) {
        if (id == null || profileId == null) {
            LOGGER.warn("eleonore - Impossible to delete a dashboard if the dashboard id and/or the profile id is/are null");
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
            LOGGER.warn("eleonore - Impossible to delete a dashboard if the dashboard name and/or the profile id is/are null");
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
            dashboardDTO = new DashboardDTO(optionalDashboard.get());
            dashboardDTO.setElements(this.elementService.getElements(dashboardDTO.getId()));
        }

        return Optional.ofNullable(dashboardDTO);
    }

    @Override
    public Optional<DashboardDTO> getDashboardByName(String name, Long profileId) {
        var optionalDashboard = this.dashboardRepository.findByName(name, profileId);
        DashboardDTO dashboardDTO = null;
        if (optionalDashboard.isPresent()) {
            dashboardDTO = new DashboardDTO(optionalDashboard.get());
            dashboardDTO.setElements(this.elementService.getElements(dashboardDTO.getId()));
        }

        return Optional.ofNullable(dashboardDTO);
    }

    @Override
    public List<DashboardDTO> getDashboards(Long profileId) {
        return this.dashboardRepository.findAllByProfileId(profileId)
                .stream()
                .map(DashboardDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = this.customerRepository.save(new Customer(customerDTO));

        return new CustomerDTO(customer);
    }

    //////////////////////////////////////////////////////////
    //////////////////// Private methods ////////////////////
    /////////////////////////////////////////////////////////

    private void deleteDashboard(Dashboard dashboard, Long profileId) {
        List<Customer> customers = this.customerRepository.findAllByDashboardId(dashboard.getId());

        // Case 1: The customer is the dashboard owner and he is alone to use it
        if (customers.size() == 1 && profileId.equals(customers.get(0).getProfileId())) {
            this.elementService.deleteElements(dashboard.getId());
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
                        .map(CustomerDTO::new)
                        .collect(Collectors.toList());
                // First each customer is associated with a copy of the removal shared dashboard
                copyDashboard(new DashboardDTO(dashboard), customerDTOList);
                // Then removal dashboard elements are deleted
                this.elementService.deleteElements(dashboard.getId());
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

        customerDTOList.stream().forEach(customerDTO -> {
            var dashboardCopy = this.dashboardRepository.save(new Dashboard(dashboardDTO));
            customerDTO.setId(null);
            customerDTO.setOwner(true);
            customerDTO.setEditable(true);
            var dashboardIdOriginal = customerDTO.getDashboardId();
            customerDTO.setDashboardId(dashboardCopy.getId());
            this.customerRepository.save(new Customer(customerDTO));
            this.elementService.copyDashboardElements(dashboardIdOriginal, dashboardCopy.getId());
        });
    }
}
