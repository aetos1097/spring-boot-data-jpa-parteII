package com.estudio.springbootdatajpa.view.xml;

import com.estudio.springbootdatajpa.models.entity.Cliente;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Clase Wrapper(envoltorio) que tendra un listado de clientes los cuales se convertiran
 * en xml
 */
//indicamos que esta es al clase rootXml
@XmlRootElement(name="clientes")
public class ClienteList {
    @XmlElement(name="cliente")
    public List<Cliente> clientes;

    public ClienteList() {
    }

    public ClienteList(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}
