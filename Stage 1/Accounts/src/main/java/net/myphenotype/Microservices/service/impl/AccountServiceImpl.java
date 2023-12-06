package net.myphenotype.Microservices.service.impl;

import lombok.AllArgsConstructor;
import net.myphenotype.Microservices.constants.AccountConstants;
import net.myphenotype.Microservices.dto.AccountsDto;
import net.myphenotype.Microservices.dto.CustomerDto;
import net.myphenotype.Microservices.entity.Accounts;
import net.myphenotype.Microservices.entity.Customer;
import net.myphenotype.Microservices.exception.CustomerAlreadyExistsException;
import net.myphenotype.Microservices.exception.ResourceNotFoundException;
import net.myphenotype.Microservices.mapper.AccountsMapper;
import net.myphenotype.Microservices.mapper.CustomerMapper;
import net.myphenotype.Microservices.repository.AccountsRepository;
import net.myphenotype.Microservices.repository.CustomerRepository;
import net.myphenotype.Microservices.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    /*
    Because we used @AllArgsConstructor, there is only one constructor here.
    So, we don't need @Autowired for accountsRepository and customerRepository.
    Spring will automatically autowire
     */
    @Autowired
    AccountsRepository accountsRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobile number "
                    +customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("UndefinedUser");
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountConstants.SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("UndefinedUser");
        return newAccount;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        /*
        findByMobileNumber will return an object of type Optional<>.
        Optional<> has an inbuild method called orElseThrow(). We can use that
        to throw an exception.

        orElseThrow accepts one parameter exceptionSupplier which is
        the supplying function that produces an exception to be thrown
         */
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer","mobileNumber", mobileNumber));

        Accounts account = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Accounts","Customer ID", customer.getCustomerId().toString()));
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account, new AccountsDto()));
        return customerDto;
    }

    /**
     * @param customerDto@return Accounts Details based on a given mobileNumber
     */
    @Override
    public Boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        boolean isDeleted = false;
        Customer cutomer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "Mobile Number",mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(cutomer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Accounts", "Customer Id", cutomer.getCustomerId().toString())
        );

        accountsRepository.delete(accounts);
        customerRepository.delete(cutomer);
        /*
        Alternatively, we can also use the custom method void deleteByCustomerId(Long customerId);

        Unlike reads, update or delete using custom methods requires two annotations

        @Modifying to tell Spring JPA that this method is going update and run this query within a transaction
        @Transactional ensures unit of work and two phase commit
         */

        return true;
    }


}
