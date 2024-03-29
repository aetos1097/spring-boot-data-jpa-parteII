package com.estudio.springbootdatajpa.controller;

import com.estudio.springbootdatajpa.models.entity.Cliente;
import com.estudio.springbootdatajpa.models.entity.Factura;
import com.estudio.springbootdatajpa.models.entity.ItemFactura;
import com.estudio.springbootdatajpa.models.entity.Producto;
import com.estudio.springbootdatajpa.models.service.IClienteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * En esta clase se pondrán los metodos controladores para las solicitudes http
 */
@Controller
@RequestMapping("/factura")//url base->http://localhost:8082/factura
@SessionAttributes("factura")//guarda los atributos hasta que se envie
public class FacturaController {
    private  final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IClienteService clienteService;//lo inyectamso porque necesitamos buscar por id

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value="id") Long id,Model model,RedirectAttributes flash, Locale locale){

        Factura factura = clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProducto(id);//clienteService.findFacturaById(id);
        //nos aseguramos que la factura no este vacia
        if(factura == null){
            flash.addFlashAttribute("error",messageSource.getMessage("text.factura.flash.db.error", null, locale));
            return "redirect:/listar";
        }

        model.addAttribute("factura", factura);
        model.addAttribute("titulo",String.format(messageSource.getMessage("text.factura.ver.titulo", null, locale), factura.getDescripcion()));
        return "factura/ver";
    }

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value="clienteId") Long clienteId, Map<String,Object> model, RedirectAttributes flash, Locale locale){
        Cliente cliente = clienteService.findOne(clienteId);
        if(cliente == null){
            flash.addFlashAttribute("error",messageSource.getMessage("text.cliente.flash.db.error", null, locale));
            return "redirect:/listar";
        }
        Factura factura = new Factura();
        factura.setCliente(cliente);//asiganamos el cliente a la factura
        model.put("factura", factura);
        model.put("titulo",messageSource.getMessage("text.factura.form.titulo", null, locale));
        return "factura/form";
    }
    //controlador para cargar los productos
    @GetMapping(value="/cargar-productos/{term}", produces = {"application/json"})//produces={"application/json"} denota la salida(respuesta) de el metodo que sera en json
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
        //suprime una vista de thymleaf y en ves de eso toma el resultado convertido en json y eso lo va a poblar en el body de la respuesta
        //y guarda le repuesta en el body
        return clienteService.findByNombre(term);
    }

    //controlador para guardar las facturas
    @PostMapping("/form")
    public String guardar(@Valid Factura factura,
                          BindingResult result, Model model,
                          @RequestParam(name="item_id[]",required=false) Long[] itemId,
                          @RequestParam(name="cantidad[]",required=false) Integer[] cantidad,
                          RedirectAttributes flash, SessionStatus status, Locale locale){
        if(result.hasErrors()){
            model.addAttribute("titulo",messageSource.getMessage("text.factura.form.titulo", null, locale));
            return "factura/form";
        }
        if(itemId == null || itemId.length == 0){
            model.addAttribute("titulo",messageSource.getMessage("text.factura.form.titulo", null, locale));
            model.addAttribute("error",messageSource.getMessage("text.factura.flash.lineas.error", null, locale));
            return "factura/form";
        }

        /*Este guardar es un poco especial porque es necesario obtener primero el parametro item_id[] y cantidad_id[]
        para poder guardar la factura, el @RequestParam es porque se guardaran las lineas creadas con jquery
         es decir son parametros a parte de la entidad factura*/
        for (int i = 0; i <itemId.length; i++){//como itemid e sun arreglo por eso se pone.lenth
            Producto producto = clienteService.findProductoById(itemId[i]);

            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);
            log.info("ID: "+ itemId[i].toString() + ", Cantidad: "+ cantidad[i].toString());//debug
        }
        clienteService.saveFactura(factura);
        status.setComplete();;
        flash.addFlashAttribute("success",messageSource.getMessage("text.factura.flash.crear.success", null, locale));

        return "redirect:/ver/" + factura.getCliente().getId();

    }
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value="id") Long id,RedirectAttributes flash, Locale locale){
//        Validamos que exista factura
        Factura factura = clienteService.findFacturaById(id);
        if( factura != null){
            clienteService.deleteFactura(id);
            flash.addFlashAttribute("success",messageSource.getMessage("text.factura.flash.eliminar.success", null, locale));
            return "redirect:/ver/" + factura.getCliente().getId();

        }
        flash.addFlashAttribute("error",messageSource.getMessage("text.factura.flash.db.error", null, locale));

        return "redirect:/listar";
    }


}
