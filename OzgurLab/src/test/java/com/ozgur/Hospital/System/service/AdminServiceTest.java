package com.ozgur.Hospital.System.service;

import com.ozgur.Hospital.System.dto.AdminDto;
import com.ozgur.Hospital.System.exception.AdminNotFoundException;
import com.ozgur.Hospital.System.exception.MissingParameterException;
import com.ozgur.Hospital.System.exception.UnauthorizedException;
import com.ozgur.Hospital.System.model.Admin;
import com.ozgur.Hospital.System.model.Laborant;
import com.ozgur.Hospital.System.model.LaborantRequest;
import com.ozgur.Hospital.System.model.Role;
import com.ozgur.Hospital.System.repository.*;
import com.ozgur.Hospital.System.rules.CreateAdminValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {


    private AdminService adminService;

    private LaborantRepository laborantRepository;
    private LaborantService laborantService;
    private LaborantRequestRepository laborantRequestRepository;
    private AdminRepository adminRepository;
    private ReportRepository reportRepository;
    private PatientRepository patientRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private CreateAdminValidator validator;
    @Captor
    private ArgumentCaptor<Admin> adminServiceArgumentCaptor;

    @BeforeEach
    void setUp() {
        laborantRepository = Mockito.mock(LaborantRepository.class);
        laborantService = Mockito.mock(LaborantService.class);
        laborantRequestRepository = Mockito.mock(LaborantRequestRepository.class);
        adminRepository = Mockito.mock(AdminRepository.class);
        reportRepository = Mockito.mock(ReportRepository.class);
        patientRepository = Mockito.mock(PatientRepository.class);
        bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        validator = Mockito.mock(CreateAdminValidator.class);
        adminService = new AdminService(
                laborantRepository,
                laborantRequestRepository,
                adminRepository,
                reportRepository,
                laborantService,
                bCryptPasswordEncoder,
                validator,
                patientRepository);
    }


    @Test
    //Adim 1: Test ismini yaz
    void shouldReturnAllLaborantRequests() {
        // Adim 2: Test verilerinin hazırlanması
        LaborantRequest laborantRequest1 = LaborantRequest.builder().
                id(1L).
                name("test1").
                surname("test1").
                username("test1").
                password("test1").
                department("test1").
                build();

        LaborantRequest laborantRequest2 = LaborantRequest.builder().
                id(2L).
                name("test2").
                surname("test2").
                username("test2").
                password("test2").
                department("test2").
                build();

        List<LaborantRequest> expectedRequestList = new ArrayList<>();
        expectedRequestList.add(laborantRequest1);
        expectedRequestList.add(laborantRequest2);

        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(laborantRequestRepository.findAll()).thenReturn(expectedRequestList);

        //Adim 4: Test edilecek methodun çalıştırılması
        List<LaborantRequest> result = adminService.getAllLaborantRequests();

        //Adım 5: Test sonucunun dogrulunmasi
        assertEquals(expectedRequestList, result);

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(laborantRequestRepository).findAll();
        Mockito.verify(laborantRequestRepository, new Times(1)).findAll();
    }


    @Test
    //Adim 1: Test ismini yaz
    void shouldReturnAllAdmins() {
        // Adim 2: Test verilerinin hazırlanması
        Admin admin1 = new Admin(
                1L,
                "test1",
                "test1",
                "test1",
                "test1",
                true,
                true,
                true,
                true,
                Role.ROLE_ADMIN,
                new HashSet<>(List.of(Role.ROLE_ADMIN))
        );

        Admin admin2 = new Admin(
                2L,
                "test2",
                "test2",
                "test2",
                "test2",
                true,
                true,
                true,
                true,
                Role.ROLE_ADMIN,
                new HashSet<>(List.of(Role.ROLE_ADMIN))
        );

        List<Admin> expectedAdminList = new ArrayList<>();
        expectedAdminList.add(admin1);
        expectedAdminList.add(admin2);

        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(adminRepository.findAll()).thenReturn(expectedAdminList);

        //Adim 4: Test edilecek methodun çalıştırılması
        List<Admin> result = adminService.getAllAdmins();

        //Adım 5: Test sonucunun dogrulunmasi
        assertEquals(expectedAdminList, result);

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(adminRepository).findAll();
        Mockito.verify(adminRepository, new Times(1)).findAll();
    }

    @Test
    //Adim 1: Test ismini yaz
    void shouldApproveLaborantRequestAndReturnNewLaborant_whenLaborantRequestExistsWithGivenId() {
        // Adim 2: Test verilerinin hazırlanması
        Long id = 1L;
        LaborantRequest laborantRequest1 = LaborantRequest.builder().
                id(1L).
                name("test1").
                surname("test1").
                username("test1").
                password("test1").
                department("test1").
                build();

        Laborant expectedLaborant1 = Laborant.builder()
                .laborant_id(1L)
                .name(laborantRequest1.getName())
                .surname(laborantRequest1.getSurname())
                .department(laborantRequest1.getDepartment())
                .username(laborantRequest1.getUsername())
                .password(laborantRequest1.getPassword())
                .role(Role.ROLE_LABORANT)
                .authorities(new HashSet<>(List.of(Role.ROLE_LABORANT)))
                .build();

        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(laborantRequestRepository.findById(id)).thenReturn(Optional.of(laborantRequest1));
        Mockito.when(laborantService.createNewLaborantByLaborantRequest(laborantRequest1)).thenReturn(expectedLaborant1);
        Mockito.when(laborantRepository.save(expectedLaborant1)).thenReturn(expectedLaborant1);

        //Adim 4: Test edilecek methodun çalıştırılması
        Laborant result = adminService.approveLaborantRequest(id);

        //Adım 5: Test sonucunun dogrulunmasi
        assertEquals(expectedLaborant1, result);

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(laborantRequestRepository).findById(Mockito.any(Long.class));
        Mockito.verify(laborantService).createNewLaborantByLaborantRequest(Mockito.any(LaborantRequest.class));
        Mockito.verify(laborantRequestRepository).deleteById(Mockito.any(Long.class));
        Mockito.verify(laborantRepository).save(Mockito.any(Laborant.class));
    }


    @Test
        //Adim 1: Test ismini yaz
    void shouldThrowAdminNotFoundException_whenLaborantRequestNotExistsWithGivenId() {
        // Adim 2: Test verilerinin hazırlanması
        Long id = 1L;


        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(laborantRequestRepository.findById(id)).thenReturn(Optional.empty());

        //Adim 4: Test edilecek methodun çalıştırılması

        //Adım 5: Test sonucunun dogrulunmasi
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> adminService.approveLaborantRequest(id))
                .isInstanceOf(AdminNotFoundException.class)
                        .hasMessageContaining("There is no laborant request with id : " + id);

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(laborantRequestRepository).findById(Mockito.any(Long.class));
        Mockito.verify(laborantRequestRepository, new Times(0)).deleteById(Mockito.any(Long.class));
        Mockito.verifyNoInteractions(laborantService);
        Mockito.verifyNoInteractions(laborantRepository);
    }

    @Test
    //Adim 1: Test ismini yaz
    void shouldGetAdminByName() {
        // Adim 2: Test verilerinin hazırlanması
        String name = "admin";
        Admin admin1 = new Admin(
                1L,
                "test1",
                "test1",
                "test1",
                "test1",
                true,
                true,
                true,
                true,
                Role.ROLE_ADMIN,
                new HashSet<>(List.of(Role.ROLE_ADMIN))
        );

        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(adminRepository.findByusername(name)).thenReturn(Optional.of(admin1));

        //Adim 4: Test edilecek methodun çalıştırılması
        Admin result = adminService.getByAdminName(name).get();

        //Adım 5: Test sonucunun dogrulunmasi
        assertEquals(admin1, result);

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(adminRepository).findByusername(Mockito.any(String.class));
    }


    @Test
    //Adim 1: Test ismini yaz
    void shouldCreateAdmin_whenNameSurnamePasswordUsernameAreGivenAndUsernameIsUnique() {
        // Adim 2: Test verilerinin hazırlanması
        AdminDto adminDto = new AdminDto("name", "surname", "password", "username");
        Long randomId;

        try (MockedStatic<RandomHospitalIdGenerator> mockedGenerator = Mockito.mockStatic(RandomHospitalIdGenerator.class)) {
            randomId = 1234567L;
            mockedGenerator.when(RandomHospitalIdGenerator::generateUniqueSevenDigitId).thenReturn(randomId);

            //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
            Mockito.doNothing().when(validator).validateCreateAdminAdminDto(Mockito.any(AdminDto.class));
            Mockito.doNothing().when(validator).validateCreateAdminUsernameUnique(Mockito.any(AdminRepository.class), Mockito.any(String.class));
            Mockito.when(bCryptPasswordEncoder.encode(Mockito.any(String.class))).thenReturn("password");

            //Adim 4: Test edilecek methodun çalıştırılması
            adminService.createAdmin(adminDto);

            //Adım 5: Test sonucunun dogrulunmasi
            Mockito.verify(adminRepository).save(adminServiceArgumentCaptor.capture());
            Admin capturedAdmin = adminServiceArgumentCaptor.getValue();

            assertEquals(randomId, capturedAdmin.getAdmin_id());
            assertEquals("password", capturedAdmin.getPassword());
            assertEquals("name", capturedAdmin.getName());
            assertEquals("username", capturedAdmin.getUsername());


            //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
            Mockito.verify(adminRepository).save(Mockito.any(Admin.class));
        }



    }


    @Test
    //Adim 1: Test ismini yaz
    void shouldThrowMissingParameterException_whenNameOrSurnameOrPasswordOrUsernameAreNotProperlyGiven() {
        // Adim 2: Test verilerinin hazırlanması
        AdminDto adminDto = new AdminDto(null, "surname", "password", "username");


        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.doThrow(new MissingParameterException("You must provide your name.")).when(validator).validateCreateAdminAdminDto(Mockito.any(AdminDto.class));

        //Adim 4: Test edilecek methodun çalıştırılması

        //Adım 5: Test sonucunun dogrulunmasi
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> adminService.createAdmin(adminDto))
                .isInstanceOf(MissingParameterException.class)
                .hasMessageContaining("You must provide your name.");

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verifyNoInteractions(adminRepository);
    }

    @Test
    //Adim 1: Test ismini yaz
    void shouldUpdateLoggedInAdminAccountsUsername_whenAdminDtoIsGivenAsParameterAndUserLoggedInAsAnAdmin() {
        // Adim 2: Test verilerinin hazırlanması
        String username = "admin";
        Admin admin1 = new Admin(
                1L,
                "test1",
                "test1",
                username,
                "test1",
                true,
                true,
                true,
                true,
                Role.ROLE_ADMIN,
                new HashSet<>(List.of(Role.ROLE_ADMIN))
        );

        AdminDto adminDto = new AdminDto(null, null,null, "newUsername");


        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        try (MockedStatic<WhoAuthenticated> mockedGenerator = Mockito.mockStatic(WhoAuthenticated.class)) {
            mockedGenerator.when(WhoAuthenticated::whoIsAuthenticated).thenReturn(username);
            Mockito.when(adminRepository.findByusername(username)).thenReturn(Optional.of(admin1));
            Mockito.doNothing().when(validator).validateCreateAdminUsernameUnique(Mockito.any(AdminRepository.class),Mockito.any(String.class));

            //Adim 4: Test edilecek methodun çalıştırılması
            adminService.updateOwnAdminAccount(adminDto);

            //Adım 5: Test sonucunun dogrulunmasi
            Mockito.verify(adminRepository).save(adminServiceArgumentCaptor.capture());
            Admin capturedAdmin = adminServiceArgumentCaptor.getValue();

            assertEquals("newUsername", capturedAdmin.getUsername());
            assertEquals("test1", capturedAdmin.getPassword());
            assertEquals("test1", capturedAdmin.getName());
            assertEquals("test1", capturedAdmin.getSurname());


            //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
            mockedGenerator.verify(WhoAuthenticated::whoIsAuthenticated);
            Mockito.verify(adminRepository).save(Mockito.any(Admin.class));
        }

    }


    @Test
    //Adim 1: Test ismini yaz
    void shouldDeleteAdmin_whenGivenIdNotBelongsToBigAdminAndAuthenticatedUserIsBigAdmin(){
        // Adim 2: Test verilerinin hazırlanması
        Long id = 1L;
        Admin admin1 = new Admin(
                id,
                "test1",
                "test1",
                "Admin1",
                "test1",
                true,
                true,
                true,
                true,
                Role.ROLE_ADMIN,
                new HashSet<>(List.of(Role.ROLE_ADMIN))
        );


        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        try (MockedStatic<WhoAuthenticated> mockedGenerator = Mockito.mockStatic(WhoAuthenticated.class)) {
            mockedGenerator.when(WhoAuthenticated::whoIsAuthenticated).thenReturn("Big Admin"); //Authenticated user = Big Admin
            Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admin1)); // To be deleted admin is not Big Admin

            //Adim 4: Test edilecek methodun çalıştırılması
            adminService.deleteAdminById(id);

            //Adım 5: Test sonucunun dogrulunmasi
            Mockito.verify(adminRepository).deleteById(id);

            //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi

        }

    }


    @Test
    //Adim 1: Test ismini yaz
    void shouldThrowUnauthorizedException_whenGivenIdBelongsToBigAdminAndAuthenticatedUserIsBigAdmin(){
        // Adim 2: Test verilerinin hazırlanması
        Long id = 1L;
        Admin admin1 = new Admin(
                id,
                "test1",
                "test1",
                "Big Admin",
                "test1",
                true,
                true,
                true,
                true,
                Role.ROLE_ADMIN,
                new HashSet<>(List.of(Role.ROLE_ADMIN))
        );



        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        try (MockedStatic<WhoAuthenticated> mockedGenerator = Mockito.mockStatic(WhoAuthenticated.class)) {
            mockedGenerator.when(WhoAuthenticated::whoIsAuthenticated).thenReturn("Big Admin"); //Authenticated user = Big Admin
            Mockito.when(adminRepository.findById(1L)).thenReturn(Optional.of(admin1)); // To be deleted admin is  Big Admin

            //Adim 4: Test edilecek methodun çalıştırılması


            //Adım 5: Test sonucunun dogrulunmasi
            org.assertj.core.api.Assertions.assertThatThrownBy(() -> adminService.deleteAdminById(id))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("Big Admin can not be deleted!!!!");



            //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
            Mockito.verify(adminRepository,new Times(0)).deleteById(Mockito.anyLong());
        }

    }


    @Test
    //Adim 1: Test ismini yaz
    void shouldDeleteAdmin_whenAnAdminOhterThanBigAdminTriesToDeleteOwnAccount(){
        // Adim 2: Test verilerinin hazırlanması
        Long id = 1L;
        Admin admin1 = new Admin(
                id,
                "test1",
                "test1",
                "Admin1",
                "test1",
                true,
                true,
                true,
                true,
                Role.ROLE_ADMIN,
                new HashSet<>(List.of(Role.ROLE_ADMIN))
        );


        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        try (MockedStatic<WhoAuthenticated> mockedGenerator = Mockito.mockStatic(WhoAuthenticated.class)) {
            mockedGenerator.when(WhoAuthenticated::whoIsAuthenticated).thenReturn("Admin1"); //Authenticated user = Not Big Admin
            Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admin1)); // To be deleted admin is not Big Admin and same as authenticated user

            //Adim 4: Test edilecek methodun çalıştırılması
            adminService.deleteAdminById(id);

            //Adım 5: Test sonucunun dogrulunmasi
            Mockito.verify(adminRepository).deleteById(id);

            //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi

        }

    }

    @Test
    //Adim 1: Test ismini yaz
    void shouldThrowUnauthorizedException_whenAuthenticatedUserIsNotBigAdminButTryingToDeleteAnotherAdmin(){
        // Adim 2: Test verilerinin hazırlanması
        Long id = 1L;
        Admin admin1 = new Admin(
                id,
                "test1",
                "test1",
                "Admin1",
                "test1",
                true,
                true,
                true,
                true,
                Role.ROLE_ADMIN,
                new HashSet<>(List.of(Role.ROLE_ADMIN))
        );


        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        try (MockedStatic<WhoAuthenticated> mockedGenerator = Mockito.mockStatic(WhoAuthenticated.class)) {
            mockedGenerator.when(WhoAuthenticated::whoIsAuthenticated).thenReturn("Admin2"); //Authenticated user = Not Big Admin
            Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admin1)); // To be deleted admin is not Big Admin but not same as authenticated user

            //Adim 4: Test edilecek methodun çalıştırılması


            //Adım 5: Test sonucunun dogrulunmasi
            org.assertj.core.api.Assertions.assertThatThrownBy(() -> adminService.deleteAdminById(id))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("You are trying to update another user's informations.");

            //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi

        }
    }












    @AfterEach
    void tearDown() {
    }
}