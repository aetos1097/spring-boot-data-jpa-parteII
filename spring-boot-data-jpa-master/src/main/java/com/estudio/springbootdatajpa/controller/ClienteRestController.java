package com.estudio.springbootdatajpa.controller;

import com.estudio.springbootdatajpa.models.service.IClienteService;
import com.estudio.springbootdatajpa.view.xml.ClienteList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {
    @Autowired
    private IClienteService clienteService;

    @GetMapping(value="/listar")
    public ClienteList listar(){
        return new ClienteList(clienteService.findAll());
    }
}
