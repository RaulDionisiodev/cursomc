package com.rauldionisio.cursomc.services.Validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.rauldionisio.cursomc.domain.Cliente;
import com.rauldionisio.cursomc.domain.enuns.TipoCliente;
import com.rauldionisio.cursomc.dto.ClienteNewDTO;
import com.rauldionisio.cursomc.repositories.ClienteRepository;
import com.rauldionisio.cursomc.resources.exceptions.FieldMessage;
import com.rauldionisio.cursomc.services.Validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();

		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && 
				!BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("CpfOuCnpj", "Cpf Inválido"));
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && 
				!BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("CpfOuCnpj", "Cnpj Inválido"));
		}
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if(aux != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}