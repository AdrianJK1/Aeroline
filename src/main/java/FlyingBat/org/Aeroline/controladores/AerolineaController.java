package FlyingBat.org.Aeroline.controladores;

import FlyingBat.org.Aeroline.modelos.Aerolinea;
import FlyingBat.org.Aeroline.servicios.interfaces.IAerolineaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation .BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller

@RequestMapping("/aerolineas")
public class AerolineaController {

    @Autowired
    private IAerolineaService aerolineaService;

    //Pagina index principal
    @GetMapping
    public String Index(Model model, @RequestParam("page")Optional<Integer> page, @RequestParam("size")Optional<Integer> size){
        int paginaAcutual = page.orElse(1)-1;//para asignarle 0 en caso que no lo este
        int tamanoPagina = size.orElse(10); //tamano del paginado
        Pageable pageable = PageRequest.of(paginaAcutual, tamanoPagina);
        Page<Aerolinea> aerolineas = aerolineaService.buscarTodosPaginados(pageable);
        model.addAttribute("aerolineas",aerolineas);

        int paginasTotales = aerolineas.getTotalPages();

        if (paginasTotales > 0){
            List<Integer> numeroPagina = IntStream.rangeClosed(1, paginasTotales)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("numeroPagina", numeroPagina);
        }
        return "aerolinea/index";
    }
    //mostrar formulario
    @GetMapping("/create")
    public String create(Model model, Aerolinea aerolinea) {
        model.addAttribute("aerolinea", aerolinea);
        return "aerolinea/create";
    }


    @PostMapping("/save")
    public String save (Model model, Aerolinea aerolinea, BindingResult result, RedirectAttributes attributes){
        if (result.hasErrors()){
            model.addAttribute(aerolinea);
            attributes.addFlashAttribute("error", "Error a la hora de crear la aerolinea");
            return "aerolinea/create";
        }

        aerolineaService.creoOEditar(aerolinea);
        attributes.addFlashAttribute("msg", "Aerolinea creada exitosamente");
        return "redirect:/aerolineas";
    }

    @GetMapping("/details/{id}")
    public String details (Model model, @PathVariable("id") Integer id){
        Aerolinea aerolinea = aerolineaService.buscarPorId(id).get();
        model.addAttribute("aerolinea", aerolinea);
        return "aerolinea/details";
    }

    @GetMapping("/edit/{id}")
    public String edit (@PathVariable("id") Integer id, Model model, RedirectAttributes attributes){
        Aerolinea aerolinea = aerolineaService.buscarPorId(id).get();
        model.addAttribute("aerolinea", aerolinea);
        attributes.addFlashAttribute("msg", "Aerolinea eliminado con exito");
        return "aerolinea/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove (@PathVariable("id") Integer id, Model model, RedirectAttributes attributes){
        Aerolinea aerolinea = aerolineaService.buscarPorId(id).get();
        model.addAttribute("aerolinea", aerolinea);
        attributes.addFlashAttribute("msg", "Aerolinea eliminada con exito");
        return "aerolinea/delete";

    }
    @PostMapping("/delete")
    public String delete (Model model, Aerolinea aerolinea, RedirectAttributes attributes){
        aerolineaService.eliminarPorId(aerolinea.getId());
        attributes.addFlashAttribute("msg", "Aerolinea eliminada exitosamente");
        return "redirect:/aerolineas";
    }
}
