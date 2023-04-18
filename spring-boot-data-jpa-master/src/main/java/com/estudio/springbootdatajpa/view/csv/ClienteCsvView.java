package com.estudio.springbootdatajpa.view.csv;


import com.estudio.springbootdatajpa.models.entity.Cliente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.util.List;
import java.util.Map;

@Component("listar")

public class ClienteCsvView extends AbstractView {//tenemos que usar la clase padre de abstractView

    @Override
    protected boolean generatesDownloadContent() {//sobreescribimos este metodo para que sea capaz de descargar el archivo
        return true;
    }

    public ClienteCsvView() {//el tipo de contenido del archivo
        setContentType("text/csv");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"clientes.csv\"");//cambiamos el nombre de la descarga
        response.setContentType(getContentType());
        //pasamos los datos a partir del objeto modelo en los controladores
        //Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
        List<Cliente> clientes = (List<Cliente>) model.get("clientes");
        ICsvBeanWriter beanWriter= new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"id","nombre","apellido","email","createAt"};//cabecera
        //la pasamos al archivo plano
        beanWriter.writeHeader(header);
        //iteramos con los clientes
        for(Cliente cliente : clientes){
            beanWriter.write(cliente,header);
        }
        beanWriter.close();//cerramos el recurso

    }
}
