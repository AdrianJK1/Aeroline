package FlyingBat.org.Aeroline.controladores;

import FlyingBat.org.Aeroline.modelos.Reserva;
import FlyingBat.org.Aeroline.modelos.Usuario;
import FlyingBat.org.Aeroline.servicios.implementaciones.ReservaServices;
import FlyingBat.org.Aeroline.servicios.interfaces.IReservaService;
import FlyingBat.org.Aeroline.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping ("/reservas")
public class ReservaController {
@Autowired
private IReservaService reservaService;

@Autowired
private IUsuarioService usuarioService;

    @GetMapping
     public String index (Model model, @RequestParam ("page")Optional<Integer> page, @RequestParam ("size") Optional<Integer> size ){
        int paginaUnica= page.orElse(1)-1;
        int sizePage = size.orElse(10);
        Pageable pageable =  PageRequest.of(paginaUnica, sizePage);
        Page<Reserva> reserva = reservaService.buscarTodospaginados(pageable);
        model.addAttribute("reservas", reserva);

        int paginastotales= reserva.getTotalPages();
        if (paginastotales>0){
            List<Integer> numeropagina = IntStream.rangeClosed(1,paginastotales)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("numeropagina", numeropagina);


        }
        return "reserva/index";


    }
    @GetMapping ("/create")
    public String create(Model model, Reserva resereva){
        model.addAttribute("reserva", resereva);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "reserva/create";

    }

    @PostMapping("/save")
    public String save (Model model , Reserva reserva,
                        @RequestParam Integer usuarioId,
                        BindingResult result, RedirectAttributes attributes) {
        Usuario usuario = usuarioService.obtenerId(usuarioId);
        if (result.hasErrors()) {
            model.addAttribute(reserva);
            attributes.addFlashAttribute("error", "Error al crear Reserva");
            return "reserva/create";

        }
        reserva.setUsuario(usuario);
        reservaService.crearOEditar(reserva);
        attributes.addFlashAttribute("msg" ,"Reserva creada con exito");
        return "redirect:/reservas";
    }

    @GetMapping("/details/{id}")
    public String details (Model model, @PathVariable("id") Integer id ){
        Reserva reserva =reservaService.buscarPorId(id).get();
        model.addAttribute("reserva", reserva);
        return "reserva/details";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        Reserva reserva = reservaService.buscarPorId(id).orElse(null);
        if (reserva == null) {
            attributes.addFlashAttribute("msg", "Reserva no encontrada");
            return "redirect:/reservas";
        }
        model.addAttribute("reserva", reserva);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        attributes.addFlashAttribute("mss", "Reserva Editada con éxito");
        return "reserva/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        // Buscar la reserva por id
        Reserva reserva = reservaService.buscarPorId(id).orElse(null);
        if (reserva == null) {
            attributes.addFlashAttribute("msg", "Reserva no encontrada");
            return "redirect:/reservas";
        }
        model.addAttribute("reserva", reserva);
        return "reserva/delete"; // Mostrar página de confirmación de eliminación
    }

    @PostMapping("/delete")
    public String delete(Reserva reserva, RedirectAttributes attributes) {
        reservaService.elimimarPorid(reserva.getId());
        attributes.addFlashAttribute("msg", "Reserva eliminada con éxito");
        return "redirect:/reservas";
    }
}
