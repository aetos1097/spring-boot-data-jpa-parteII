package com.estudio.springbootdatajpa.view.pdf;

import com.estudio.springbootdatajpa.models.entity.Factura;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.util.Map;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //traemos los datos de la entidad factura desde el controlador
        Factura factura = (Factura)model.get("factura");

        if (factura == null) {
            throw new Exception("No se encontró la factura en el modelo.");
        }

        // Depuración: imprimir los datos de la factura
        /*
        System.out.println("Datos de factura:");
        System.out.println("ID: " + factura.getId());
        System.out.println("Descripción: " + factura.getDescripcion());
        System.out.println("Fecha: " + factura.getCreateAt());
        System.out.println("Cliente:");
        System.out.println("Nombre: " + factura.getCliente().getNombre());
        System.out.println("Apellido: " + factura.getCliente().getApellido());
        System.out.println("Correo electrónico: " + factura.getCliente().getEmail());*/

        //creamos nuestra tabla
        PdfPTable tabla = new PdfPTable(1);
        tabla.setSpacingAfter(20);
        tabla.addCell("Datos del cliente");
        tabla.addCell("Nombre: "+factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla.addCell("Correo: "+factura.getCliente().getEmail());

        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.setSpacingAfter(20);
        tabla2.addCell("Datos de la factura");
        tabla2.addCell("Folio: " + factura.getId());
        tabla2.addCell("Descripcion: " + factura.getDescripcion());
        tabla2.addCell("Fecha: " + factura.getCreateAt());

        //gurdammos la tabla
        document.add(tabla);
        document.add(tabla2);

    }
}
