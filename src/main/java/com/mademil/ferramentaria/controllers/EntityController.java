package com.mademil.ferramentaria.controllers;

import com.mademil.ferramentaria.entities.Chuck;
import com.mademil.ferramentaria.entities.Tool;
import com.mademil.ferramentaria.entities.Template;
import com.mademil.ferramentaria.entities.Vise;
import com.mademil.ferramentaria.entities.User;
import com.mademil.ferramentaria.repositories.UserRepository;
import com.mademil.ferramentaria.service.ChuckService;
import com.mademil.ferramentaria.service.ToolService;
import com.mademil.ferramentaria.service.TemplateService;
import com.mademil.ferramentaria.service.ViseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class EntityController {
    @Autowired
    private ChuckService chuckService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ViseService viseService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/entidades")
    public String entitiesTable(
        @RequestParam(value = "error", required = false) String error,
        @AuthenticationPrincipal UserDetails springUser,
        Model model){

        List<Chuck> chucks = chuckService.getAllActiveChucks();
        List<Tool> tools = toolService.getAllActiveTools();
        List<Template> templates = templateService.getAllActiveTemplates();
        List<Vise> vises = viseService.getAllActiveVises();

        User user = userRepository.findByUsername(springUser.getUsername())
        .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("chucks", chucks);
        model.addAttribute("tools", tools);
        model.addAttribute("templates", templates);
        model.addAttribute("vises", vises);

        if (error != null) {
            model.addAttribute("error", error);
        }
        return "entities";
    }

    @GetMapping("/formulario-castanha")
    public String addChuckForm(
        @RequestParam(value = "chuckId", required = false) Integer chuckId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model){

        if (chuckId != null) {
            Optional<Chuck> optionalChuck = chuckService.getChuckById(chuckId);

            if (optionalChuck.isPresent()) {
                Chuck chuck = optionalChuck.get();
                model.addAttribute("chuck", chuck);
            }else{
                model.addAttribute("error", "Castanha não encontrada: " + chuckId);
            }
        }
        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }
        return "form-chuck";
    }

    @PostMapping("/adicionar-castanha")
    public String addChuckToDatabase(
        @RequestParam(value = "chuckId", required = false) Integer chuckId,
        @RequestParam("chuckName") String chuckName,
        @RequestParam("chuckLocation") String chuckLocation,
        @RequestParam("chuckImage") MultipartFile chuckImage,
        RedirectAttributes redirectAttributes){

        try {
            Chuck chuck = new Chuck();
            if (chuckId != null) chuck.setChuckId(chuckId);
            chuck.setChuckName(chuckName.toUpperCase());
            chuck.setChuckLocation(chuckLocation.toUpperCase());
            chuck.setChuckImage(chuckImage.getBytes());
            chuck.setIsDeleted(false);
            chuckService.saveChuck(chuck);
        }catch (DataIntegrityViolationException e){
            System.err.println("Data Integrity error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Já existe uma castanha com este nome");
            return "redirect:/formulario-castanha";
        }catch (IOException e){
            System.err.println("IO Error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Não foi possível ler a imagem");
            return "redirect:/formulario-castanha";
        }
        return "redirect:/entidades";
    }


    @GetMapping("/formulario-ferramenta")
    public String addToolForm(
        @RequestParam(value = "toolId", required = false) Integer toolId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model){

        if (toolId != null) {
            Optional<Tool> optionalTool = toolService.getToolById(toolId);

            if (optionalTool.isPresent()) {
                Tool tool = optionalTool.get();
                model.addAttribute("tool", tool);
            }else{
                model.addAttribute("error", "Ferramenta não encontrada: " + toolId);
            }
        }
        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }
        return "form-tool";
    }

    @PostMapping("/adicionar-ferramenta")
    public String addToolToDatabase(
        @RequestParam(value = "toolId", required = false) Integer toolId,
        @RequestParam("toolName") String toolName,
        @RequestParam("toolImage") MultipartFile toolImage,
        RedirectAttributes redirectAttributes){

        try {
            Tool Tool = new Tool();
            if (toolId != null) Tool.setToolId(toolId);
            Tool.setToolName(toolName.toUpperCase());
            Tool.setToolImage(toolImage.getBytes());
            Tool.setIsDeleted(false);
            toolService.saveTool(Tool);
        }catch (DataIntegrityViolationException e){
            System.err.println("Data Integrity error " + e.getMostSpecificCause());
            redirectAttributes.addAttribute("error", "Já existe uma ferramenta com este nome.");
            return "redirect:/formulario-ferramenta";
        }catch (IOException e){
            System.err.println("IO Error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Não foi possível ler a imagem");
            return "redirect:/formulario-ferramenta";
        }
        return "redirect:/entidades";
    }

    @GetMapping("/formulario-gabarito")
    public String addTemplateForm(
        @RequestParam(value = "templateId", required = false) Integer templateId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model){

        if (templateId != null) {
            Optional<Template> optionalTemplate = templateService.getTemplateById(templateId);

            if (optionalTemplate.isPresent()) {
                Template template = optionalTemplate.get();
                model.addAttribute("template", template);
            }else{
                model.addAttribute("error", "Gabarito não encontrado: " + templateId);
            }
        }
        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }
        return "form-template";
    }

    @PostMapping("/adicionar-gabarito")
    public String addTemplateToDatabase(
        @RequestParam(value = "templateId", required = false) Integer templateId,
        @RequestParam("templateName") String templateName,
        @RequestParam("templateLocation") String templateLocation,
        @RequestParam("templateImage") MultipartFile templateImage,
        RedirectAttributes redirectAttributes){

        try {
            Template template = new Template();
            if (templateId != null) template.setTemplateId(templateId);
            template.setTemplateName(templateName.toUpperCase());
            template.setTemplateLocation(templateLocation.toUpperCase());
            template.setTemplateImage(templateImage.getBytes());
            template.setIsDeleted(false);
            templateService.saveTemplate(template);
        }catch (DataIntegrityViolationException e){
            System.err.println("Data Integrity error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Já existe um gabarito com este nome");
            return "redirect:/formulario-gabarito";
        }catch (IOException e){
            System.err.println("IO Error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Não foi possível ler a imagem");
            return "redirect:/formulario-gabarito";
        }
        return "redirect:/entidades";
    }

    @GetMapping("/formulario-morsa")
    public String addViseForm(
        @RequestParam(value = "viseId", required = false) Integer viseId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model){

        if (viseId != null) {
            Optional<Vise> optionalVise = viseService.getViseById(viseId);

            if (optionalVise.isPresent()) {
                Vise vise = optionalVise.get();
                model.addAttribute("vise", vise);
            }else{
                model.addAttribute("error", "Morsa não encontrado: " + viseId);
            }
        }
        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }
        return "form-vise";
    }

    @PostMapping("/adicionar-morsa")
    public String addViseToDatabase(
        @RequestParam(value = "viseId", required = false) Integer viseId,
        @RequestParam("viseName") String viseName,
        @RequestParam("viseLocation") String viseLocation,
        @RequestParam("viseImage") MultipartFile viseImage,
        RedirectAttributes redirectAttributes){

        try {
            Vise vise = new Vise();
            if (viseId != null) vise.setViseId(viseId);
            vise.setViseName(viseName.toUpperCase());
            vise.setViseLocation(viseLocation.toUpperCase());
            vise.setViseImage(viseImage.getBytes());
            vise.setIsDeleted(false);
            viseService.saveVise(vise);
        }catch (DataIntegrityViolationException e){
            System.err.println("Data Integrity error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Já existe uma morsa com este nome");
            return "redirect:/formulario-morsa";
        }catch (IOException e){
            System.err.println("IO Error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Não foi possível ler a imagem");
            return "redirect:/formulario-morsa";
        }
        return "redirect:/entidades";
    }

    @PostMapping("/inativar-entidade")
    public String deactivateEntity(
        @AuthenticationPrincipal UserDetails springUser,
        @RequestParam("entityIdentifier") String entityIdentifier,
        @RequestParam(value = "chuckId", required = false) Integer chuckId,
        @RequestParam(value = "toolId", required = false) Integer toolId,
        @RequestParam(value = "templateId", required = false) Integer templateId,
        @RequestParam(value = "viseId",required = false) Integer viseId,
        RedirectAttributes redirectAttributes){

        User user = userRepository.findByUsername(springUser.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        if (!user.getRole().equalsIgnoreCase("ADMIN")) {
            redirectAttributes.addAttribute("error", "Usuário sem permissão");
            return "redirect:/entidades";
        }

        switch (entityIdentifier.toUpperCase()) {
            case "CHUCK":
                if (chuckId != null) {
                    Optional<Chuck> optionalChuck = chuckService.getChuckById(chuckId);

                    if (optionalChuck.isPresent()) {
                        Chuck chuck = optionalChuck.get();
                        chuck.setIsDeleted(true);
                        chuckService.saveChuck(chuck);
                    }else{
                        redirectAttributes.addAttribute("error", "Erro ao inativar castanha: castanha com o ID "+ chuckId + "não existe");
                        return "redirect:/entidades";
                    }
                }else{
                    redirectAttributes.addAttribute("error", "Erro ao inativar castanha: nenhum ID foi providenciado");
                    return "redirect:/entidades";
                }

                break;

            case "TOOL":
                if (toolId != null) {
                    Optional<Tool> optionalTool = toolService.getToolById(toolId);
                    
                    if (optionalTool.isPresent()) {
                        Tool tool = optionalTool.get();
                        tool.setIsDeleted(true);
                        toolService.saveTool(tool);
                    }else{
                        redirectAttributes.addAttribute("error", "Erro ao inativar ferramenta: ferramenta com o ID "+ toolId + "não existe");
                        return "redirect:/entidades";
                    }
                }else{
                    redirectAttributes.addAttribute("error", "Erro ao inativar Ferramenta: nenhum ID foi providenciado");
                    return "redirect:/entidades";
                }

                break;

            case "TEMPLATE":
                if (templateId != null) {
                    Optional<Template> optionalTemplate = templateService.getTemplateById(templateId);

                    if (optionalTemplate.isPresent()) {
                        Template template = optionalTemplate.get();
                        template.setIsDeleted(true);
                        templateService.saveTemplate(template);
                    }else{
                        redirectAttributes.addAttribute("error", "Erro ao inativar gabarito: gabarito com o ID "+ templateId + "não existe");
                        return "redirect:/entidades";
                    }
                }else{
                    redirectAttributes.addAttribute("error", "Erro ao inativar gabarito: nenhum ID foi providenciado");
                    return "redirect:/entidades";
                }

                break;

            case "VISE":
                if (viseId != null) {
                    Optional<Vise> optionalVise = viseService.getViseById(viseId);

                    if (optionalVise.isPresent()) {
                        Vise vise = optionalVise.get();
                        vise.setIsDeleted(true);
                        viseService.saveVise(vise);
                    }else{
                        redirectAttributes.addAttribute("error", "Erro ao inativar morsa: morsa com o ID "+ viseId + "não existe");
                        return "redirect:/entidades";
                    }
                }else{
                    redirectAttributes.addAttribute("error", "Erro ao inativar morsa: nenhum ID foi providenciado");
                    return "redirect:/entidades";
                }

                break;
        
            default:
                redirectAttributes.addAttribute("error", "Erro inesperado: entrar em contato com o administrador");
                return "redirect:/entidades";
        }
        return "redirect:/entidades";
    }
}
