package com.estudio.springbootdatajpa.view.xlsx;

import com.estudio.springbootdatajpa.models.entity.Factura;
import com.estudio.springbootdatajpa.models.entity.ItemFactura;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import java.util.Map;

@Component("factura/ver")
public class FacturaXlsxView extends AbstractXlsView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //cambiamos el nombre del archivo
        response.setHeader("Content-Disposition", "attachment; filename =\"factura_view.xls\"");

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

        //Estilos
        CellStyle theaderStyle = workbook.createCellStyle();
        theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        theaderStyle.setBorderTop(BorderStyle.MEDIUM);
        theaderStyle.setBorderRight(BorderStyle.MEDIUM);
        theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
        theaderStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.index);
        theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle tbodyStyle = workbook.createCellStyle();
        tbodyStyle.setBorderBottom(BorderStyle.THIN);
        tbodyStyle.setBorderTop(BorderStyle.THIN);
        tbodyStyle.setBorderRight(BorderStyle.THIN);
        tbodyStyle.setBorderLeft(BorderStyle.THIN);

        CellStyle total = workbook.createCellStyle();
        total.setFillForegroundColor(IndexedColors.GOLD.index);

        //header de la tabla detalle del cliente
        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");

        header.getCell(0).setCellStyle(theaderStyle);
        header.getCell(1).setCellStyle(theaderStyle);
        header.getCell(2).setCellStyle(theaderStyle);
        header.getCell(3).setCellStyle(theaderStyle);

        //creamos un iniziliador
        int rownum = 10;// porque será el número de filas y como el encabezado es 9 entonces necesitamos iniciar desde 10

        for(ItemFactura item: factura.getItems()){
            //Por cada item creamos una nueva fila
            Row fila = sheet.createRow(rownum ++);
            cell = fila.createCell(0);
            //creamos las celda por cada atributo
            cell.setCellValue(item.getProducto().getNombre());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(1);
            cell.setCellValue(item.getProducto().getPrecio());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(2);
            cell.setCellValue(item.getCantidad());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(3);
            cell.setCellValue(item.calcularImporte());
            cell.setCellStyle(tbodyStyle);
        }
        //Crear el gran total
        Row filaTotal = sheet.createRow(rownum);//fijamos en que fila se iniciara

        cell= filaTotal.createCell(2);
        cell.setCellValue("Gran Total");
        cell.setCellStyle(tbodyStyle);

        cell=filaTotal.createCell(3);
        cell.setCellValue(factura.getTotal());
        cell.setCellStyle(tbodyStyle);


    }
}
