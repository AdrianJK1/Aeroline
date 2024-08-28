package FlyingBat.org.Aeroline.servicios.implementaciones;

import FlyingBat.org.Aeroline.modelos.Reserva;
import FlyingBat.org.Aeroline.repositorios.IReservaRepository;
import FlyingBat.org.Aeroline.servicios.interfaces.IReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ReservaServices implements IReservaService {
    @Autowired
    private IReservaRepository reservaRepository;

    @Override
    public Page<Reserva> buscarTodospaginados(Pageable pageable) {
        return reservaRepository.findAll(pageable);
    }

    @Override
    public List<Reserva> buscartodos() {
        return reservaRepository.findAll();
    }

    @Override
    public Optional<Reserva> buscarPorId(Integer id) {
        return reservaRepository.findById(id);
    }

    @Override
    public Reserva crearOEditar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    @Override
    public void elimimarPorid(Integer id) {
        reservaRepository.deleteById(id);
    }


    // Nuevo método para generar PDF
    public void generarPdfReserva(Reserva reserva) throws DocumentException, IOException {
        // Crear un documento PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("reserva_" + reserva.getId() + ".pdf"));

        // Abrir el documento para añadir contenido
        document.open();
        document.add(new Paragraph("Detalles de la Reserva"));
        document.add(new Paragraph("ID: " + reserva.getId()));
        document.add(new Paragraph("Usuario: " + reserva.getUsuario().getNombre()));
        document.add(new Paragraph("Fecha de Reserva: " + reserva.getFechaReserva()));
        document.add(new Paragraph("Estado de Compra: " + reserva.getStatus()));
        // Añadir más detalles según sea necesario

        // Cerrar el documento
        document.close();
    }
}
