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
        model.addAttribute("vuelo", vuelo);

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
    public String showCreateForm(Model model) {
        model.addAttribute("vuelo", new Vuelo());
        model.addAttribute("aerolineas", aerolineaService.obtenerTodos());
        return "vuelo/create";
    }

    @PostMapping("/save")
    public String save(Model model, @ModelAttribute("vuelo") Vuelo vuelo, @RequestParam("aerolineaId") Integer aerolineaId,
                       BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("aerolineas", aerolineaService.obtenerTodos());
            attributes.addFlashAttribute("error", "Error al crear el vuelo");
            return "vuelo/create";
        }

        // Encuentra la aerolínea por ID
        Aerolinea aerolinea = aerolineaService.buscarPorId(aerolineaId).orElse(null);
        if (aerolinea == null) {
            attributes.addFlashAttribute("error", "Aerolínea no encontrada");
            return "redirect:/vuelos/create";
        }

        vuelo.setAerolinea(aerolinea);
        vueloService.crearOEditar(vuelo);
        attributes.addFlashAttribute("msg", "Vuelo creado exitosamente");
        return "redirect:/vuelos";
    }


    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable("id") Integer id){
        Vuelo vuelo = vueloService.obtenerPorId(id).orElseThrow(() -> new NoSuchElementException("Vuelo no encontrado"));
        model.addAttribute("vuelo", vuelo);
        return "vuelo/details";
    }

    // Método para mostrar el formulario de edición de un vuelo
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id, RedirectAttributes attributes) {
        Vuelo vuelo = vueloService.obtenerPorId(id).orElse(null);
        if (vuelo != null) {
            model.addAttribute("vuelo", vuelo);
            model.addAttribute("aerolineas", aerolineaService.obtenerTodos());
            return "vuelo/edit";
        } else {
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
    public String delete(@RequestParam("id") Integer id, RedirectAttributes attributes) {
        // Verifica si el vuelo existe antes de intentar eliminarlo
        if (vueloService.obtenerPorId(id).isPresent()) {
            vueloService.eliminarPorId(id);
            attributes.addFlashAttribute("msg", "Vuelo eliminado exitosamente");
        } else {
            attributes.addFlashAttribute("msg", "Error, vuelo no encontrado");
        }
        return "redirect:/vuelos";
    }



}
