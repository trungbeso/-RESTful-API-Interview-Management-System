package com.interviewmanagementsystem.services.auth;

import com.ninja_in_pyjamas.dtos.auth.LoginRequestDTO;
import com.ninja_in_pyjamas.dtos.auth.PasswordResetRequestDTO;
import com.ninja_in_pyjamas.dtos.auth.RegisterRequestDTO;
import com.ninja_in_pyjamas.dtos.email.EmailRequestDTO;
import com.ninja_in_pyjamas.dtos.employees.response.EmployeeInformationDTO;
import com.ninja_in_pyjamas.entities.Employee;
import com.ninja_in_pyjamas.entities.PasswordResetToken;
import com.ninja_in_pyjamas.entities.Role;
import com.ninja_in_pyjamas.enums.RoleName;
import com.ninja_in_pyjamas.exceptions.AppException;
import com.ninja_in_pyjamas.exceptions.ErrorCode;
import com.ninja_in_pyjamas.repositories.IDepartmentRepository;
import com.ninja_in_pyjamas.repositories.IEmployeeRepository;
import com.ninja_in_pyjamas.repositories.IPasswordResetTokenRepository;
import com.ninja_in_pyjamas.repositories.IRoleRepository;
import com.ninja_in_pyjamas.services.email.EmailService;
import com.ninja_in_pyjamas.services.employee.ChangePasswordRequestDTO;
import com.ninja_in_pyjamas.services.employee.IEmployeeHelpers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService implements IAuthService, UserDetailsService {
	IEmployeeRepository employeeRepository;
	PasswordEncoder passwordEncoder;
	IRoleRepository roleRepository;
	IDepartmentRepository departmentRepository;
	EmailService emailService;
	PasswordService passwordService;
	IEmployeeHelpers employeeHelpers;
	IPasswordResetTokenRepository passwordResetTokenRepository;

	@NonFinal
	@Value("${nip.email.template.account-information.name}")
	private String accountInformationTemplate;

	@NonFinal
	@Value("${nip.email.template.forgot-password.name}")
	private String resetPasswordEmailTemplateName;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Employee employee = employeeRepository.findByUsername(username);

		if (employee == null) {
			throw new AppException(ErrorCode.USER_NOT_EXISTED);
		}

		Set<GrantedAuthority> authorities = employee.getRoles().stream()
			  .map(role -> "ROLE_" + role.getName())
			  .map(SimpleGrantedAuthority::new)
			  .collect(Collectors.toSet());

		return new User(employee.getUsername(), employee.getPassword(), authorities);
	}

	@Override
	public UUID register(RegisterRequestDTO registerRequestDTO) {

		var existingEmployee = employeeRepository.findByEmailOrPhoneNumber(
			  registerRequestDTO.getEmail(),
			  registerRequestDTO.getPhoneNumber());

		if (existingEmployee != null) {
			throw new AppException(ErrorCode.USER_EXISTED);
		}

		var newEmployee = new Employee();
		newEmployee.setEmail(registerRequestDTO.getEmail());
		newEmployee.setPhoneNumber(registerRequestDTO.getPhoneNumber());
		newEmployee.setFullName(registerRequestDTO.getFullName());
		newEmployee.setGender(registerRequestDTO.isGender());

		var baseUsername = employeeHelpers.generateUsername(registerRequestDTO.getFullName());
		var username = "";
		int i = 1;
		if (!employeeRepository.existsByUsername(baseUsername)) {
			username = baseUsername;
		} else {
			while (true) {
				username = baseUsername + i;
				if (!employeeRepository.existsByUsername(username)) {
					break;
				}
				i++;
			}
		}
		newEmployee.setUsername(username);

		var newPassword = passwordService.generatePassword(8);
		newEmployee.setPassword(newPassword);

		var candidateRole = roleRepository.findByName(RoleName.ADMIN);
		newEmployee.setRoles(Set.of(candidateRole));

		newEmployee.setDepartment(departmentRepository.findByName("IT"));
		// create login request
		LoginRequestDTO request = LoginRequestDTO.builder()
			  .username(newEmployee.getUsername())
			  .password(newEmployee.getPassword())
			  .build();

		// Send mail to confirm info
		EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
		emailRequestDTO.setTo(newEmployee.getEmail());
		emailRequestDTO.setSubject("Congratulations!! You Are On The Right Path To Achieve Success");
		emailRequestDTO.setTemplateName(accountInformationTemplate);

		Map<String, Object> model = Map.of(
			  "username", request.getUsername(),
			  "password", request.getPassword()
		);
		emailRequestDTO.setVariables(model);
		emailService.sendEmailAsync(emailRequestDTO);

		newEmployee.setPassword(passwordEncoder.encode(newPassword));

		newEmployee = employeeRepository.save(newEmployee);

		// double check
		employeeRepository.findById(newEmployee.getId()).orElseThrow(() -> new IllegalArgumentException("Register " +
			  "failed"));

		return newEmployee.getId();
	}

	@Override
	public EmployeeInformationDTO getEmployeeInformationDTO(String username) {

		var employee = employeeRepository.findByUsername(username);

		var employeeInformationDTO = new EmployeeInformationDTO();
		employeeInformationDTO.setFullName(employee.getFullName());
		employeeInformationDTO.setEmail(employee.getEmail());
		employeeInformationDTO.setPhoneNumber(employee.getPhoneNumber());
		employeeInformationDTO.setUsername(employee.getUsername());

		var roles = employee.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
		employeeInformationDTO.setRoles(roles);

		return employeeInformationDTO;
	}

	@Override
	public boolean existsByUsername(String username) {
		return employeeRepository.existsByUsername(username);
	}

	@Override
	public boolean requestResetPassword(PasswordResetRequestDTO requestDTO) {
		Employee employee = employeeRepository.findByEmail(requestDTO.getEmail());
		if (employee == null) {
			throw new AppException(ErrorCode.USER_NOT_EXISTED);
		}
		String token = passwordService.generatePasswordResetToken(requestDTO.getEmail());
		// Send email with token to user
		EmailRequestDTO emailRequestDTO = EmailRequestDTO.builder()
			  .to(requestDTO.getEmail())
			  .subject("Reset Password Verify Email")
			  .templateName(resetPasswordEmailTemplateName)
			  .build();
		Map<String, Object> model = Map.of(
			  "email", employee.getEmail(),
			  "token", token);
		emailRequestDTO.setVariables(model);
		emailService.sendEmailAsync(emailRequestDTO);
		return true;
	}

	@Override
	public boolean changePassword(ChangePasswordRequestDTO request) {
		String email = passwordService.getEmailFromPasswordResetToken(request.getToken());
		Employee employee = employeeRepository.findByEmail(email);
		if (employee == null) {
			throw new AppException(ErrorCode.USER_NOT_EXISTED);
		}
		if (!request.getNewPassword().equals(request.getConfirmPassword())) {
			throw new IllegalArgumentException("New password and confirm password does not match");
		}
		//change password
		employee.setPassword(passwordService.hashPassword(request.getNewPassword()));
		employeeRepository.save(employee);
		// delete password reset token

		PasswordResetToken resetToken = passwordResetTokenRepository.findByEmail(email)
			  .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		passwordResetTokenRepository.delete(resetToken);

		// Send confirmation email
		EmailRequestDTO requestDTO = EmailRequestDTO.builder()
			  .to(email)
			  .subject("Your new password")
			  .templateName(accountInformationTemplate)
			  .build();
		Map<String, Object> model = Map.of(
			  "password", request.getNewPassword(),
			  "username", employee.getUsername()
		);
		requestDTO.setVariables(model);
		emailService.sendEmailAsync(requestDTO);
		return true;
	}
}
