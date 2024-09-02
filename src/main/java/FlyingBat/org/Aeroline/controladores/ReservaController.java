package FlyingBat.org.Aeroline.controladores;

import FlyingBat.org.Aeroline.modelos.Reserva;
import FlyingBat.org.Aeroline.modelos.Usuario;
import FlyingBat.org.Aeroline.modelos.Vuelo;
import FlyingBat.org.Aeroline.servicios.implementaciones.VueloService;
import FlyingBat.org.Aeroline.servicios.interfaces.IReservaService;
import FlyingBat.org.Aeroline.servicios.interfaces.IUsuarioService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private IReservaService reservaService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private VueloService vueloService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int paginaUnica = page.orElse(1) - 1;
        int sizePage = size.orElse(10);
        Pageable pageable = PageRequest.of(paginaUnica, sizePage);


        Page<Reserva> reserva = reservaService.buscarTodospaginados(pageable);
        Page<Vuelo> vuelo = vueloService.obtenerTodosPaginados(pageable);


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

    @GetMapping("/create")
    public String create(Model model, Reserva reserva) {
        model.addAttribute("reserva", reserva);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("vuelos", vueloService.listarVuelos());
        return "reserva/create";
    }




    @PostMapping("/save")
    public void save(Model model, Reserva reserva,
                     @RequestParam Integer usuarioId,
                     @RequestParam Integer vueloId,
                     @RequestParam(value = "generarPdf", required = false) boolean generarPdf,
                     BindingResult result,
                     RedirectAttributes attributes,
                     HttpServletResponse response) throws IOException {

        Usuario usuario = usuarioService.obtenerId(usuarioId);
        Vuelo vuelo = vueloService.obtenerPorId(vueloId).orElse(null);

        if (result.hasErrors()) {
            model.addAttribute("reserva", reserva);
            model.addAttribute("usuarios", usuarioService.listarUsuarios());
            model.addAttribute("vuelos", vueloService.listarVuelos());
            attributes.addFlashAttribute("error", "Error al crear Reserva");
            return;
        }

        reserva.setUsuario(usuario);
        reserva.setVuelo(vuelo);
        reservaService.crearOEditar(reserva);
        attributes.addFlashAttribute("msg", "Reserva creada con éxito");

        if (generarPdf) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reserva_" + reserva.getId() + ".pdf");

            try {
                Document document = new Document();
                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();


                Image logo = Image.getInstance("https://i.pinimg.com/564x/f6/fa/0c/f6fa0c717a2ba10462953bb3a4ceeae5.jpg"); // Reemplaza con la ruta correcta de tu imagen
                logo.scaleToFit(100, 100); //
                logo.setAlignment(Element.ALIGN_RIGHT); //
                document.add(logo);


                Paragraph airlineName = new Paragraph("FlyingBat Aeroline", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
                airlineName.setAlignment(Element.ALIGN_RIGHT);
                document.add(airlineName);

                // Fuentes
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
                Font redFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.RED);


                Paragraph title = new Paragraph("Detalle de Reserva", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setFont(new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLUE)); // Título en color azul
                document.add(title);

                document.add(Chunk.NEWLINE);


                Paragraph details = new Paragraph();
                details.setFont(normalFont);
                details.add("ID de Reserva: " + reserva.getId() + "\n");
                details.add("Usuario: " + usuario.getNombre() + "\n");
                details.add("Vuelo: " + vuelo.getOrigen() + " - " + vuelo.getDestino() + "\n");
                details.add("Fecha de Reserva: " + reserva.getFechaReserva() + "\n");
                details.add("Estado de compra: " + reserva.getStatus());
                document.add(details);


                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);


                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);


                PdfPCell header1 = new PdfPCell(new Phrase("Clave", titleFont));
                header1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(header1);

                PdfPCell header2 = new PdfPCell(new Phrase("Valor", titleFont));
                header2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(header2);


                table.addCell(new PdfPCell(new Phrase("ID de Reserva")));
                table.addCell(new PdfPCell(new Phrase(reserva.getId().toString())));

                table.addCell(new PdfPCell(new Phrase("Usuario")));
                table.addCell(new PdfPCell(new Phrase(usuario.getNombre())));

                table.addCell(new PdfPCell(new Phrase("Vuelo")));
                table.addCell(new PdfPCell(new Phrase(vuelo.getOrigen() + " - " + vuelo.getDestino())));

                table.addCell(new PdfPCell(new Phrase("Fecha de Reserva")));
                table.addCell(new PdfPCell(new Phrase(reserva.getFechaReserva())));

                table.addCell(new PdfPCell(new Phrase("Estado de compra")));
                PdfPCell statusCell = new PdfPCell(new Phrase(reserva.getStatus(), redFont));
                table.addCell(statusCell);

                document.add(table);

                document.close();
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }


        } else {
            response.sendRedirect("/reservas");
        }
    }


    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable("id") Integer id) {
        Vuelo vuelo = vueloService.obtenerPorId(id).orElse(null);
        Reserva reserva = reservaService.buscarPorId(id).orElse(null);
        if (reserva != null) {
            model.addAttribute("reserva", reserva);
            model.addAttribute("vuelo", vuelo);
        } else {
            model.addAttribute("msg", "Reserva no encontrada");
        }
        return "reserva/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        Vuelo vuelo = vueloService.obtenerPorId(id).orElse(null);
        Reserva reserva = reservaService.buscarPorId(id).orElse(null);
        if (reserva == null) {
            attributes.addFlashAttribute("msg", "Reserva no encontrada");
            return "redirect:/reservas";
        }
        model.addAttribute("reserva", reserva);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("vuelos", vueloService.listarVuelos());
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
    public String delete(@RequestParam("id") Integer reservaId, RedirectAttributes attributes) {
        Reserva reserva = reservaService.buscarPorId(reservaId).orElse(null);
        if (reserva != null) {
            reservaService.elimimarPorid(reservaId);
            attributes.addFlashAttribute("msg", "Reserva eliminada con éxito");
        } else {
            attributes.addFlashAttribute("msg", "Reserva no encontrada");
        }
        return "redirect:/reservas";
    }



}
