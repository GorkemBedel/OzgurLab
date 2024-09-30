package com.ozgur.Hospital.System;

import com.ozgur.Hospital.System.model.Admin;
import com.ozgur.Hospital.System.model.Role;
import com.ozgur.Hospital.System.repository.AdminRepository;
import com.ozgur.Hospital.System.service.RandomHospitalIdGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class HospitalSystemApplication implements CommandLineRunner {

	private final AdminRepository adminRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;


	public HospitalSystemApplication(AdminRepository adminRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.adminRepository = adminRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(HospitalSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		if(adminRepository.existsByUsername("Big Admin")) return;
		Admin admin = Admin.builder()
				.admin_id(1L)
				.name("Big Admin")
				.username("Big Admin")
				.password(bCryptPasswordEncoder.encode("Big Admin"))
				.authorities(new HashSet<>(List.of(Role.ROLE_ADMIN)))
				.role(Role.ROLE_ADMIN)
				.build();
		adminRepository.save(admin);

	}
}
