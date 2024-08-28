package FlyingBat.org.Aeroline.controladores;


import FlyingBat.org.Aeroline.modelos.Aerolinea;
import FlyingBat.org.Aeroline.modelos.Vuelo;
import FlyingBat.org.Aeroline.servicios.interfaces.IAerolineaService;
import FlyingBat.org.Aeroline.servicios.interfaces.IVueloService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/vuelos")
public class VueloController {
    @Autowired
    private IVueloService vueloService;

    @Autowired
    private IAerolineaService aerolineaService;

    @GetMapping
    public String index(Model model, @RequestParam("page")Optional<Integer> page, @RequestParam("size")Optional<Integer> size){
        int paginaPrincipal = page.orElse(1)-1;
        int sizePage = size.orElse(5);
        Pageable pageable = PageRequest.of(paginaPrincipal, sizePage);
        Page<Vuelo> vuelo = vueloService.obtenerTodosPaginados(pageable);
        model.addAttribute("vuelos", vuelo);

        int totalPage = vuelo.getTotalPages();
        if (totalPage>0){
            List<Integer> numberPage = IntStream.rangeClosed(1, totalPage)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("numberPage", numberPage);

        }
        return "vuelo/index";
    }

    @GetMapping("/create")
    public String create(Model model, Vuelo vuelo){
        model.addAttribute("vuelo", vuelo);
        model.addAttribute("aerolinea", aerolineaService.obtenerTodos());

        return "vuelo/create";

    }

    @PostMapping("/save")
    public String save (Model model, Vuelo vuelo,
                        @RequestParam String nombre,
                        @RequestParam Integer aerolineaId,
                        BindingResult result,
                        RedirectAttributes attributes){
        Aerolinea aerolinea = aerolineaService.findByName(nombre);

        if (result.hasErrors()){
            model.addAttribute(vuelo);
            attributes.addFlashAttribute("error","Error al crear");
            return "vuelo/create";
        }
        vuelo.setAerolinea(aerolinea);
        vueloService.crearOEditar(vuelo);
        attributes.addFlashAttribute("msg", "Creado exitosamente");
        return "redirect:/vuelos";
    }

    @GetMapping("/details({id}")
    public String details(Model model, @PathVariable("id") Integer id){
        Vuelo vuelo = vueloService.obtenerPorId(id).get();
        model.addAttribute("vuelo", vuelo);
        return "vuelo/details";
    }

    @GetMapping("/edit/{id}")
    public String edit (Model model, @PathVariable("id") Integer id,RedirectAttributes attributes, @PathVariable("nombre")String nombre){
        Vuelo vuelo = vueloService.obtenerPorId(id).orElse(null);
        if (vuelo!= null){
            model.addAttribute("vuelo", vuelo);
            model.addAttribute("aerolinea", aerolineaService.findByName(nombre));
            attributes.addFlashAttribute("msg", "Vuelo editado correctamente");
            return "vuelo/edit";
        }
        else{
            attributes.addFlashAttribute("msg", "Vuelo no encontrado :[");
            return "redirect:/vuelos";
        }

    }

    @GetMapping("/remove/{id}")
    public String remove (@PathVariable("id") Integer id, Model model, RedirectAttributes attributes){
        Vuelo vuelo = vueloService.obtenerPorId(id).orElse(null);
        if (vuelo == null){
            attributes.addFlashAttribute("msg", "Error, vuelo no encontrado");
            return "redirect:/vuelos";
        }
        model.addAttribute("vuelo", vuelo);
        return "vuelo/delete";
    }

    @PostMapping("/delete")
    public String delete (Vuelo vuelo, RedirectAttributes attributes){
        vueloService.eliminarPorId(vuelo.getId());
        attributes.addFlashAttribute("msg", "Vuelo eliminado exitosamente");
        return "redirect:/vuelos";
    }


}
