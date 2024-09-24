package pe.edu.cibertec.rueditas_frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.rueditas_frontend.dto.AutoRequestDTO;
import pe.edu.cibertec.rueditas_frontend.dto.AutoResponseDTO;
import pe.edu.cibertec.rueditas_frontend.viewmodel.AutoModel;


@Controller
@RequestMapping("/auto")
public class AutoController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/inicio")
    public String inicio(Model model){
        AutoModel autoModel = new AutoModel("00", "","","","","","");
        model.addAttribute("autoModel", autoModel);
        return "inicio";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("placa") String placa,
                             Model model) {
        //Validar Campos
        if( placa == null || placa.trim().length() == 0){

            AutoModel autoModel = new AutoModel("01", "Debe ingresar una placa correcta","","","","","");
            model.addAttribute("autoModel", autoModel);
            return "inicio";
        }

        if(placa.trim().length() != 8){
            AutoModel autoModel = new AutoModel("01", "Debe ingresar una placa correcta", "", "", "", "", "");
            model.addAttribute("autoModel", autoModel);
            return "inicio";
        }

        String endpoint = "http://localhost:8081/autenticacion/auto";
        AutoRequestDTO autoRequestDTO = new AutoRequestDTO(placa);
        AutoResponseDTO autoResponseDTO = restTemplate.postForObject(endpoint, autoRequestDTO, AutoResponseDTO.class);

        //Validar respuesta
        if(autoResponseDTO.codigo().equals("00")){
            AutoModel autoModel = new AutoModel("00", "",
                    autoResponseDTO.marca(),
                    autoResponseDTO.modelo(),
                    autoResponseDTO.nroasiento(),
                    autoResponseDTO.precio(),
                    autoResponseDTO.color());
            model.addAttribute("autoModel", autoModel);
            return "principal";
        } else {
            AutoModel autoModel = new AutoModel("02", "Error: Autenticacion fallida  ","","","","","");
            model.addAttribute("autoModel", autoModel);
            return "inicio";
        }
    }
}