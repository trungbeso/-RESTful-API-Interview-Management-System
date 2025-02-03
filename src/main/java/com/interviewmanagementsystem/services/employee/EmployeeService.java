package com.interviewmanagementsystem.services.employee;

import com.ninja_in_pyjamas.dtos.auth.LoginRequestDTO;
import com.ninja_in_pyjamas.dtos.email.EmailRequestDTO;
import com.ninja_in_pyjamas.dtos.employees.request.ChangePasswordDTO;
import com.ninja_in_pyjamas.dtos.employees.request.EmployeeCreationRequest;
import com.ninja_in_pyjamas.dtos.employees.request.EmployeeUpdateRequest;
import com.ninja_in_pyjamas.dtos.employees.response.EmployeeMasterDTO;
import com.ninja_in_pyjamas.entities.Employee;
import com.ninja_in_pyjamas.entities.Role;
import com.ninja_in_pyjamas.mapper.RoleMapper;
import com.ninja_in_pyjamas.repositories.IDepartmentRepository;
import com.ninja_in_pyjamas.repositories.IEmployeeRepository;
import com.ninja_in_pyjamas.repositories.IRoleRepository;
import com.ninja_in_pyjamas.services.auth.PasswordService;
import com.ninja_in_pyjamas.services.email.IEmailService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {
	private final IEmployeeRepository employeeRepository;
	private final IRoleRepository roleRepository;
	private final PasswordService passwordService;
	private final PasswordEncoder passwordEncoder;
	private final EmployeeHelpers employeeHelpers;
	private final IDepartmentRepository departmentRepository;
	private final IEmailService emailService;

	@NonFinal
	@Value("8")
	Integer passwordLength;

	@NonFinal
	@Value("${nip.email.template.account-information.name}")
	String accountInformationTemplate;
	private final RoleMapper roleMapper;


	@Override
	public List<EmployeeMasterDTO> getAll() {
		return employeeRepository.findAll().stream().map(this::convertToEmployeeMasterDto).toList();
	}

	@Override
	public EmployeeMasterDTO findById(UUID id) {
		var employee = employeeRepository.findById(UUID.fromString(id.toString())).
			  orElseThrow(() -> new RuntimeException("Employee not found"));
		return convertToEmployeeMasterDto(employee);
	}

	@Override
	public Employee findByEmail(String email) {
		return employeeRepository.findByEmail(email);
	}

	@Override
	public void save(Employee employee) {
		employeeRepository.save(employee);
	}

	@Override
	public List<EmployeeMasterDTO> findByKeyword(String keyword) {
		Specification<Employee> spec = (root, query, cb) -> {
			if (keyword == null || keyword.isEmpty()) {
				return null;
			}
			//username
			Predicate predicate = cb.like(root.get("username"), "%" + keyword + "%");
			//email
			predicate = cb.or(predicate, cb.like(root.get("email"), "%" + keyword + "%"));
			//phone
			predicate = cb.or(predicate, cb.like(root.get("phoneNumber"), "%" + keyword + "%"));

			return predicate;
		};

		var employeeList = employeeRepository.findAll(spec);

		return employeeList.stream().map(this::convertToEmployeeMasterDto).toList();
	}

	@Override
	public Page<EmployeeMasterDTO> findByPaginated(String keyword, Pageable pageable) {
		Specification<Employee> spec = (root, query, cb) -> {
			if (keyword == null || keyword.isEmpty()) {
				return null;
			}
			return cb.like(root.get("username"), "%" + keyword + "%");
		};

		var employeeList = employeeRepository.findAll(spec, pageable);

		return employeeList.map(this::convertToEmployeeMasterDto);
	}

	@Override
	public EmployeeMasterDTO create(EmployeeCreationRequest request) {
		if (request == null) {
			throw new RuntimeException("Employee creation request cannot be null");
		}

		var existingEmployee = employeeRepository.findByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber());
		if (existingEmployee != null) {
			throw new RuntimeException("Employee already exists");
		}

		var employee = new Employee();
		employee.setFullName(request.getFullName());
		employee.setPhoneNumber(request.getPhoneNumber());
		employee.setGender(request.isGender());
		employee.setEmail(request.getEmail());
		employee.setActive(request.isActive());
		employee.setDob(request.getDateOfBirth());
		employee.setAddress(request.getAddress());

		var newPassword = passwordService.generatePassword(passwordLength);
		var baseUsername = employeeHelpers.generateUsername(request.getFullName());

		var username = usernameCount(baseUsername);

		var department = departmentRepository.findById(request.getDepartmentId())
			  .orElseThrow(() -> new RuntimeException("Department not found"));
		employee.setDepartment(department);

		LoginRequestDTO newRequest = LoginRequestDTO.builder()
			  .username(username)
			  .password(newPassword)
			  .build();

		employee.setPassword(passwordEncoder.encode(newPassword));
		employee.setUsername(username);

		Set<Role> roles = new HashSet<>();
		Set<UUID> roleIds = request.getRoleIds();
		roleIds.forEach(roleId -> {
			Role role = roleRepository.findById(roleId).orElse(null);
			roles.add(role);
		});
		employee.setRoles(roles);

		EmailRequestDTO emailRequestDTO =EmailRequestDTO.builder()
			  .to(request.getEmail())
			  .subject(" Congratulations!! You Are On The Right Path To Achieve Success")
			  .templateName(accountInformationTemplate)
			  .build();

		Map<String, Object> model = Map.of(
			  "username", newRequest.getUsername(),
			  "password", newRequest.getPassword()
		);
		emailRequestDTO.setVariables(model);

		emailService.sendEmailAsync(emailRequestDTO);

		employee = employeeRepository.save(employee);

		return convertToEmployeeMasterDto(employee);
	}

	@Override
	public EmployeeMasterDTO update(UUID id, EmployeeUpdateRequest request) {
		if (request == null) {
			throw new RuntimeException("Employee update request cannot be null");
		}

		var existingEmployee = employeeRepository.findByPhoneNumber(request.getPhoneNumber());
		if (existingEmployee != null && !existingEmployee.getId().equals(id)) {
			throw new RuntimeException("Employee already exists");
		}

		var employee = employeeRepository.findById(id).orElse(null);
		if (employee == null) {
			throw new RuntimeException("Employee not found");
		}

		Set<Role> roles = new HashSet<>();
		Set<UUID> roleIds = request.getRoleIds();
		roleIds.forEach(roleId -> {
			Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
			roles.add(role);
		});

		var newUsername = usernameCount(employeeHelpers.generateUsername(request.getFullName()));

		var newPassword = request.getPassword();
		LoginRequestDTO newRequest = LoginRequestDTO.builder()
			  .username(newUsername)
			  .password(newPassword)
			  .build();

		// update
		employee.setId(id);
		employee.setFullName(request.getFullName());
		employee.setUsername(newUsername);
		employee.setPassword(passwordEncoder.encode(newPassword));
		employee.setEmail(request.getEmail());
		employee.setPhoneNumber(request.getPhoneNumber());
		employee.setGender(request.isGender());
		employee.setActive(request.isActive());
		employee.setRoles(roles);
		employee.setDob(request.getDateOfBirth());
		employee.setUpdatedAt(ZonedDateTime.now());
		employee.setDepartment(departmentRepository
			  .findById(request.getDepartmentId())
			  .orElseThrow(() -> new RuntimeException("Department not found")));

		employee = employeeRepository.save(employee);

		return convertToEmployeeMasterDto(employee);
	}


	@Override
	public boolean delete(UUID id) {
		var employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

		employeeRepository.delete(employee);

		return !employeeRepository.existsById(id);
	}

	private EmployeeMasterDTO convertToEmployeeMasterDto(Employee request) {
		var employeeDto = new EmployeeMasterDTO();
		employeeDto.setId(request.getId());
		employeeDto.setFullName(request.getFullName());
		employeeDto.setUsername(request.getUsername());
		employeeDto.setPhoneNumber(request.getPhoneNumber());
		employeeDto.setGender(request.isGender());
		employeeDto.setEmail(request.getEmail());
		employeeDto.setActive(request.isActive());
		employeeDto.setInsertedAt(request.getInsertedAt());

		if (request.getDepartment() != null) {
			employeeDto.setDepartmentName(request.getDepartment().getName());
		}

		var roleNames = request.getRoles().stream().map(Role::getName).toList();
		employeeDto.setRoleName(roleNames);
		return employeeDto;
	}

	private String usernameCount (String baseUsername) {
		var username = "";
		int i = 1;
		if (!employeeRepository.existsByUsername(baseUsername)) {
			username = baseUsername;
		}else {
			while (true) {
				username = baseUsername + i;
				if (!employeeRepository.existsByUsername(username)) {
					break;
				}
				i++;
			}
		}
		return username;
	}

	@Override
	public boolean changePassword(ChangePasswordDTO request) {
		//retrieve current employeeID from security context
		UserDetails currentEmployee = EmployeeHelpers.getCurrentUser();
		if (currentEmployee == null) {
			throw new IllegalArgumentException("User is not authenticated");
		}

		// fetch the employee entity
		Employee employee = employeeRepository.findByUsername(currentEmployee.getUsername());
		if (employee == null) {
			throw new IllegalArgumentException("Employee not found");
		}
		//verify that the old password matches
		if (!passwordService.matchHashedPassword(request.getOldPassword(), employee.getPassword())) {
			throw new IllegalArgumentException("Password does not match");
		}

		//ensure new password and confirm password match
		if(!request.getNewPassword().equals(request.getConfirmNewPassword())) {
			throw new IllegalArgumentException("New password and confirm password does not match");
		}

		//hash the new password and update
		String hashedNewPassword = passwordService.hashPassword(request.getNewPassword());
		employee.setPassword(hashedNewPassword);

		employeeRepository.save(employee);
		return true;
	}
}
