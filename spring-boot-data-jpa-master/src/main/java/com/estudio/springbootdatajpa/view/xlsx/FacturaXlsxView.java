package com.estudio.springbootdatajpa.view.xlsx;

import com.estudio.springbootdatajpa.models.entity.Factura;
import com.estudio.springbootdatajpa.models.entity.ItemFactura;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import java.util.Map;

@Component("factura/ver")
public class FacturaXlsxView extends AbstractXlsView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Factura factura = (Factura) model.get("factura");//obtenemos los datos

        Sheet sheet = workbook.createSheet("Factura");

        //Creamos el objeto row y el objeto celda
        Row row= sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(("Datos del Cliente"));

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getNombre()+" "+ factura.getCliente().getApellido());

        row = sheet.createRow(2);
        cell= row.createCell(0);
        cell.setCellValue(factura.getCliente().getEmail());

        //podemos encadenar metodosd para crear la celda y columna

        sheet.createRow(4).createCell(0).setCellValue("Datos de la factura");
        sheet.createRow(5).createCell(0).setCellValue("Folio: " + factura.getId());
        sheet.createRow(6).createCell(0).setCellValue("Descripcion: "+ factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue("Fecha: "+ factura.getCreateAt());

        //header de la tabla detalle del cliente
        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");

        //creamos un iniziliador
        int rownum = 10;// porque será el número de filas y como el encabezado es 9 entonces necesitamos iniciar desde 10

        for(ItemFactura item: factura.getItems()){
            //Por cada item creamos una nueva fila
            Row fila = sheet.createRow(rownum ++);
            //creamos las celda por cada atributo
            fila.createCell(0).setCellValue(item.getProducto().getNombre());
            fila.createCell(1).setCellValue(item.getProducto().getPrecio());
            fila.createCell(2).setCellValue(item.getCantidad());
            fila.createCell(3).setCellValue(item.calcularImporte());
        }
        //Crear el gran total
        Row filtaTotal = sheet.createRow(rownum);//fijamos en que fila se iniciara
        filtaTotal.createCell(2).setCellValue("Gran Total");
        filtaTotal.createCell(3).setCellValue(factura.getTotal());


    }
}
