package com.segarra.bankingsystem.services;

import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class AccountHolderServiceTest {
//    @Autowired
//    private AccountHolderService accountHolderService;
//
//    @MockBean
//    private AccountHolderRepository accountHolderRepository;
//    @MockBean
//    private RoleRepository roleRepository;
//
//    private List<AccountHolder> accountHolderList;
//
//
//    @BeforeEach
//    void setUp() {
//        AccountHolder accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
//                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s",
//                new Address("Spain", "Sabadell", "Carrer de l'Estrella", 6, "08201"));
//        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
//                new Address("Spain", "Madrid", "Luna Avenue", 13, "28700"), "1234", "gema_s");
//
//        accountHolderList = Arrays.asList(accountHolder, accountHolder2);
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    @DisplayName("Unit test - retrieval of all account holders")
//    void getAll() {
//        when(accountHolderRepository.findAll()).thenReturn(accountHolderList);
//        assertEquals(2, accountHolderService.getAllAccountHolders().size());
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    @DisplayName("Unit test - create new account holder")
//    void create() {
//        AccountHolder accountHolder = new AccountHolder("Sergio", LocalDate.of(2020, 1, 28),
//                new Address("Spain", "Madrid", "Luna Avenue", 13, "28700"), "1234", "gabi_c");
//        Role role = new Role("ROLE_ACCOUNTHOLDER", accountHolder);
//        when(accountHolderRepository.save(any(AccountHolder.class))).thenReturn(accountHolder);
//        when(roleRepository.save(any(Role.class))).thenReturn(role);
//
//        AccountHolderVM savedAccHolder = accountHolderService.create(accountHolder);
//        assertEquals("Sergio", savedAccHolder.getName());
//    }
}