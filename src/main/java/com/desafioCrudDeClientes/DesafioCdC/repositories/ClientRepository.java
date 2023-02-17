package com.desafioCrudDeClientes.DesafioCdC.repositories;

import com.desafioCrudDeClientes.DesafioCdC.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
