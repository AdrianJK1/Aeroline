package FlyingBat.org.Aeroline.controladores;

import FlyingBat.org.Aeroline.modelos.Rol;
import FlyingBat.org.Aeroline.modelos.Usuario;
import FlyingBat.org.Aeroline.servicios.implementaciones.RolService;
import FlyingBat.org.Aeroline.servicios.implementaciones.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.PasswordView;
import java.util.Optional;

@Controller
public class RegistroController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.listarRoles());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") @Valid Usuario usuario,
                                   BindingResult result,
                                   @RequestParam("rolSeleccionado") String rolSeleccionado,
                                   Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolService.listarRoles());
            return "registro";
        }

        // Buscar el rol seleccionado
        Optional<Rol> rolOptional = rolService.obtenerRolPorNombre(rolSeleccionado);

        if (!rolOptional.isPresent()) {
            result.rejectValue("rol", "error.usuario", "El rol seleccionado no es válido.");
            model.addAttribute("roles", rolService.listarRoles());
            return "registro";
        }

        // Asignar el rol al usuario
        usuario.setRol(rolOptional.get());


        // Guardar el usuario en la base de datos
        usuarioService.guardarUsuario(usuario);

        return "redirect:/login";
    }

}
