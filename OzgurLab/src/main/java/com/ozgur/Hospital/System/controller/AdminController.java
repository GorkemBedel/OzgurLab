package com.ozgur.Hospital.System.controller;

import com.ozgur.Hospital.System.dto.AdminDto;
import com.ozgur.Hospital.System.model.Admin;
import com.ozgur.Hospital.System.model.Laborant;
import com.ozgur.Hospital.System.model.LaborantRequest;
import com.ozgur.Hospital.System.model.Patient;
import com.ozgur.Hospital.System.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/laborantRequests")
    public ResponseEntity<List<LaborantRequest>> getAllLaborantRequests() {
        return ResponseEntity.ok(adminService.getAllLaborantRequests());
    }

    @GetMapping("/getAllAdmins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<Admin> createAdmin(@RequestBody AdminDto adminDto) {
        return ResponseEntity.ok(adminService.createAdmin(adminDto));
    }

    @PutMapping("/updateOwnAdminAccount")
    public ResponseEntity<Admin> updateOwnAdminAccount(@RequestBody AdminDto adminDto){
        return ResponseEntity.ok(adminService.updateOwnAdminAccount(adminDto));
    }

    @DeleteMapping("/deleteAdminById/{id}")
    public ResponseEntity<String> deleteAdminById(@PathVariable("id") Long id){
        adminService.deleteAdminById(id);
        return ResponseEntity.ok("Admin with id " + id + " deleted");
    }

    @DeleteMapping("/deleteAdminByUsername/{username}")
    public ResponseEntity<String> deleteAdminByUsername(@PathVariable("username") String username){
        adminService.deleteAdminByUsername(username);
        return ResponseEntity.ok("Admin with username " + username + " deleted");
    }

    @PostMapping("/approveLaborantRequest/{id}")
    public ResponseEntity<Laborant> approveLaborantRequest(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adminService.approveLaborantRequest(id));
    }

    @GetMapping("/getAllPatients")
    public ResponseEntity<List<Patient>> getAllPatients(){
        return ResponseEntity.ok(adminService.getAllPatients());
    }

    @GetMapping("/getPatientByName/{name}")
    public ResponseEntity<Patient> getPatientByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(adminService.getByPatientName(name));
    }
}
