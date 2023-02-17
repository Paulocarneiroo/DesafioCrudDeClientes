package com.desafioCrudDeClientes.DesafioCdC.service;

import com.desafioCrudDeClientes.DesafioCdC.dto.ClientDto;
import com.desafioCrudDeClientes.DesafioCdC.entities.Client;
import com.desafioCrudDeClientes.DesafioCdC.repositories.ClientRepository;
import com.desafioCrudDeClientes.DesafioCdC.service.exceptions.DataBaseException;
import com.desafioCrudDeClientes.DesafioCdC.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public ClientDto findById(Long id){//GET
        return new ClientDto(repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso nao encontrado")));
    }

    @Transactional(readOnly = true)
    public Page<ClientDto> findAll(Pageable pageable){//GET
        Page<Client> result = repository.findAll(pageable);
        return result
                .map(x -> new ClientDto(x));
    }

    @Transactional
    public ClientDto insert(ClientDto dto){//POST
        Client entity = new Client();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ClientDto(entity);
    }
    @Transactional
    public ClientDto update(Long id, ClientDto dto){//PUT
        try{
            Client entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            return new ClientDto(repository.save(entity));
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso nao encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        try{
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Recurso nao encontrado");
        }catch (DataIntegrityViolationException e){
            throw new DataBaseException("Falha na integridade referencial");
        }
    }
    private void copyDtoToEntity(ClientDto dto, Client entity){//Private method to help
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }

}
