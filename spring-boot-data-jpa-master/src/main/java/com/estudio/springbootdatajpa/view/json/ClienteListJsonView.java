package com.estudio.springbootdatajpa.view.json;

import com.estudio.springbootdatajpa.models.entity.Cliente;
import com.estudio.springbootdatajpa.view.xml.ClienteList;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.List;
import java.util.Map;

/**
 * Clase para convertir a json nuestros objetos
 */
@Component("listar.json")
public class ClienteListJsonView extends MappingJackson2JsonView {
    //filtra o quita elementos del model de la vista
    @Override
    protected Object filterModel(Map<String, Object> model) {
        model.remove("titulo");
        model.remove("page");
        List<Cliente> clientes = (List<Cliente>) model.get("clientes");
        model.remove("clientes");
        model.put("clientes",new ClienteList(clientes));

        return super.filterModel(model);
    }
}
