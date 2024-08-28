package FlyingBat.org.Aeroline.controladores;

import FlyingBat.org.Aeroline.modelos.Reserva;
import FlyingBat.org.Aeroline.modelos.Usuario;
import FlyingBat.org.Aeroline.servicios.interfaces.IReservaService;
import FlyingBat.org.Aeroline.servicios.interfaces.IUsuarioService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private IReservaService reservaService;

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int paginaUnica = page.orElse(1) - 1;
        int sizePage = size.orElse(10);
        Pageable pageable = PageRequest.of(paginaUnica, sizePage);
        Page<Reserva> reserva = reservaService.buscarTodospaginados(pageable);
        model.addAttribute("reservas", reserva);

        int paginastotales = reserva.getTotalPages();
        if (paginastotales > 0) {
            List<Integer> numeropagina = IntStream.rangeClosed(1, paginastotales)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("numeropagina", numeropagina);
        }
        return "reserva/index";
    }

    @PostMapping("/save")
    public void save(Model model, Reserva reserva,
                     @RequestParam Integer usuarioId,
                     @RequestParam(value = "generarPdf", required = false) boolean generarPdf,
                     BindingResult result,
                     RedirectAttributes attributes,
                     HttpServletResponse response) throws IOException {
        Usuario usuario = usuarioService.obtenerId(usuarioId);

        if (result.hasErrors()) {
            model.addAttribute(reserva);
            attributes.addFlashAttribute("error", "Error al crear Reserva");
            return;
        }

        // Set usuario and save reserva
        reserva.setUsuario(usuario);
        reservaService.crearOEditar(reserva);
        attributes.addFlashAttribute("msg", "Reserva creada con éxito");

        if (generarPdf) {
            // Configurar la respuesta HTTP para descargar el PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reserva_" + reserva.getId() + ".pdf");

            // Generar el PDF y enviarlo a través de la respuesta
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();
                document.add(new Paragraph("Detalle de Reserva"));
                document.add(new Paragraph("ID de Reserva: " + reserva.getId()));
                document.add(new Paragraph("Usuario: " + usuario.getNombre()));
                document.add(new Paragraph("Fecha de Reserva: " + reserva.getFechaReserva()));
                document.add(new Paragraph("Estado de compra: " + reserva.getStatus()));
                document.add(new Paragraph("Generar Boleto Electrónico: " + reserva.getBoletoElectronico()));
                document.close();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

        } else {
            // Si no se genera el PDF, redirigir normalmente
            response.sendRedirect("/reservas");
        }
    }

    @GetMapping("/create")
    public String create(Model model, Reserva reserva) {
        model.addAttribute("reserva", reserva);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "reserva/create";
    }


    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable("id") Integer id) {
        Reserva reserva = reservaService.buscarPorId(id).orElse(null);
        if (reserva != null) {
            model.addAttribute("reserva", reserva);
        } else {
            model.addAttribute("msg", "Reserva no encontrada");
        }
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
        attributes.addFlashAttribute("msg", "Reserva Editada con éxito");
        return "reserva/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
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
