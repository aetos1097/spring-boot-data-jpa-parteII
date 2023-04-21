package com.estudio.springbootdatajpa.view.xml;

import com.estudio.springbootdatajpa.models.entity.Cliente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import java.util.List;
import java.util.Map;

/**
 * Vista del xml
 */
@Component("listar.xml")
public class ClienteListXmlView extends MarshallingView {
    @Autowired
    public ClienteListXmlView(Jaxb2Marshaller marshaller) {
        super(marshaller);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.remove("titulo");
        model.remove("page");
        //Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
        List<Cliente> clientes = (List<Cliente>) model.get("clientes");
        model.remove("clientes");
        //model.put("clienteList",new ClienteList(clientes.getContent()));
        model.put("clienteList", new ClienteList(clientes));

        super.renderMergedOutputModel(model, request, response);

    }
}
